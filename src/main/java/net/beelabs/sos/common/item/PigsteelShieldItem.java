package net.beelabs.sos.common.item;

import io.netty.buffer.Unpooled;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.init.SoSItems;
import net.beelabs.sos.common.util.IEntityBashingGetter;
import net.beelabs.sos.common.util.IRocketJumpVelocityGetter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class PigsteelShieldItem extends ShieldItem {
    public static final Identifier ID = new Identifier(SweetsOSpirits.MOD_ID, "pigstel_shield_bash");
    public static final TrackedData<Boolean> BASHING = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static String TICKS_POST_BASH = "post_bash";

    private boolean isBlocking = false;

    public PigsteelShieldItem(Settings settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }


    private void bash(LivingEntity user) {
        Vec3d velocity = user.getVelocity().add(user.getRotationVec(1.0F)).multiply(2.5);

        if (user.getWorld() instanceof ServerWorld server) {
            server.spawnParticles(SweetsOSpirits.EAT_GAPPLE, user.getX(), user.getY(), user.getZ(), 1, 0, 0, 0, 0);
            server.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.NEUTRAL, 1f, 1f);
            for (int i = 0; i < 8; i++) server.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.GOLD_BLOCK, 1)), user.getParticleX(1), user.getRandomBodyY(), user.getParticleZ(1), 1, velocity.getX(), velocity.getY(), velocity.getZ(), 0.05);
        }

        user.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
        ((IRocketJumpVelocityGetter)user).setRocketJumping(true);
        user.velocityModified = true;
        user.fallDistance = 0;

        ((IEntityBashingGetter)user).setBashing(true);
    }

    private void hitEntityBash(LivingEntity user){
        List<LivingEntity> list = user.getWorld().getEntitiesByClass(LivingEntity.class, user.getBoundingBox().expand(1.5D), e -> true);
        if (list.isEmpty()) return;
        for (LivingEntity entityHit : list){
            if (entityHit != user && entityHit.damage(user.getDamageSources().mobAttack(user), 4f)){
                entityHit.pushAwayFrom(user);
                if (entityHit.getWorld() instanceof ServerWorld server){
                    for (int duration = 0; duration < 10; ++duration) {
                        double d0 = entityHit.getRandom().nextGaussian() * 0.02D;
                        double d1 = entityHit.getRandom().nextGaussian() * 0.02D;
                        double d2 = entityHit.getRandom().nextGaussian() * 0.02D;
                        DefaultParticleType type = entityHit instanceof WitherEntity || entityHit instanceof WitherSkeletonEntity ? ParticleTypes.SMOKE : ParticleTypes.CLOUD;
                        server.spawnParticles(type, entityHit.getParticleX(1.0D), entityHit.getRandomBodyY() + 1.0D, entityHit.getParticleZ(1.0D), 1, d0, d1, d2, 1.0D);
                    }
                    server.spawnParticles(SweetsOSpirits.EAT_GAPPLE, user.getX(), user.getY(), user.getZ(), 1, 0, 0, 0, 0);
                }
                float knockbackStrength = 3.0F;
                entityHit.takeKnockback(knockbackStrength, MathHelper.sin(entityHit.getYaw() * ((float) Math.PI / 180F)), -MathHelper.cos(entityHit.getYaw() * ((float) Math.PI / 180F)));
            }
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if(((IEntityBashingGetter)user).isBashing()) postBash(user);
    }

    public static void postBash(LivingEntity user){
        ((IEntityBashingGetter)user).setBashing(false);
        if (user instanceof PlayerEntity player) player.getItemCooldownManager().set(SoSItems.PIGSTEEL_SHIELD, 100);
        user.clearActiveItem();
    }

    public static void registerClientTickEvent() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            if (client.player.getActiveItem().getItem() != SoSItems.PIGSTEEL_SHIELD) return;
            if (client.player.getItemCooldownManager().isCoolingDown(SoSItems.PIGSTEEL_SHIELD)) return;
            if (((IEntityBashingGetter)client.player).isBashing()) return;
            boolean isLeftClickPressed = client.options.attackKey.isPressed(), isRightClickPressed = client.options.useKey.isPressed();
            if (isLeftClickPressed && isRightClickPressed) PigsteelShieldItem.sendPacket();
        });
    }

    public static void registerServerTickEvent() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {

            server.getWorlds().forEach(world -> {
                for (ServerPlayerEntity player : world.getPlayers()){
                    if (player == null) return;
                    if (player.getActiveItem().getItem() instanceof PigsteelShieldItem shield && ((IEntityBashingGetter)player).isBashing()) {
                        shield.hitEntityBash(player);
                        ((IEntityBashingGetter)player).decrementPostBashTicks();
                        if (((IEntityBashingGetter)player).getPostBashTicks() <= 0){
                            postBash(player);
                        }
                    }
                }
            });
        });
    }

    public static void registerPacketReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                if (player != null && player.getActiveItem().getItem() instanceof PigsteelShieldItem shield) {
                    shield.bash(player);
                }
            });
        });
    }

    public static void sendPacket() {
        ClientPlayNetworking.send(ID, new PacketByteBuf(Unpooled.buffer()));
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }
}
