package net.beelabs.sos.mixin;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.event.MultiplyMovementSpeedEvent;
import net.beelabs.sos.common.init.SoSAttributes;
import net.beelabs.sos.common.init.SoSItems;
import net.beelabs.sos.common.init.SoSSoundEvents;
import net.beelabs.sos.common.item.PigsteelShieldItem;
import net.beelabs.sos.common.util.IEntityBashingGetter;
import net.beelabs.sos.common.util.IEntityImpaledShardGetter;
import net.beelabs.sos.common.util.IRocketJumpVelocityGetter;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements IEntityImpaledShardGetter, IEntityBashingGetter, IRocketJumpVelocityGetter {
    @Unique
    private static final TrackedData<ItemStack> LODGED_TRIDENT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    @Unique
    private static final TrackedData<Integer> IMPALED_AMETHYST_SHARDS = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> IMPALED_AMETHYST_SHARD_TIMER = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Boolean> IS_BASHING = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Integer> BASHING_POST_TICKS = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Boolean> ROCKET_JUMPING = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @ModifyArg(method = "applyMovementInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
    private float multiplyMovementSpeed(float value) {
        LivingEntity entity = (LivingEntity) (Object) this;

        return value * MultiplyMovementSpeedEvent.EVENT.invoker().multiply(1, entity.getWorld(), entity);
    }


    @Inject(method = "createLivingAttributes", at = @At("HEAD"), cancellable = true)
    private static void initAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir){
        SweetsOSpirits.LOGGER.info("INIT ATTRIBUTE");
        cir.setReturnValue(DefaultAttributeContainer.builder()
                .add(EntityAttributes.GENERIC_MAX_HEALTH)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                .add(EntityAttributes.GENERIC_ARMOR)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)
                .add(SoSAttributes.TURNING_SPEED, 5));
        cir.cancel();
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initTrackedData(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.getDataTracker().startTracking(LODGED_TRIDENT, ItemStack.EMPTY);
        entity.getDataTracker().startTracking(IMPALED_AMETHYST_SHARDS, 0);
        entity.getDataTracker().startTracking(IMPALED_AMETHYST_SHARD_TIMER, 50);
        entity.getDataTracker().startTracking(IS_BASHING, false);
        entity.getDataTracker().startTracking(BASHING_POST_TICKS, 0);
        entity.getDataTracker().startTracking(ROCKET_JUMPING, false);

    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        nbt.put("LodgedTridentStack", ((IEntityImpaledShardGetter) entity).getLodgedTridentStack().writeNbt(new NbtCompound()));
        nbt.putInt("ImpaledShardsCount", ((IEntityImpaledShardGetter) entity).getImpaledShardCount());
        nbt.putInt("ImpaledShardTimer", ((IEntityImpaledShardGetter) entity).getImpaledShardTimer());
        nbt.putBoolean("Bashing", ((IEntityBashingGetter) entity).isBashing());
        nbt.putInt("BashingPostTicks", ((IEntityBashingGetter) entity).getPostBashTicks());
        nbt.putBoolean("RocketJumping", ((IRocketJumpVelocityGetter) entity).isRocketJumping());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readNbt(NbtCompound nbt, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (nbt.contains("LodgedTridentStack"))
            ((IEntityImpaledShardGetter) entity).setLodgedTridentStack(ItemStack.fromNbt(nbt.getCompound("LodgedTridentStack")));
        if (nbt.contains("ImpaledShardsCount"))
            ((IEntityImpaledShardGetter) entity).setImpaledShardCount(nbt.getInt("ImpaledShardsCount"));
        if (nbt.contains("ImpaledShardTimer"))
            ((IEntityImpaledShardGetter) entity).setImpaledShardTimer(nbt.getInt("ImpaledShardTimer"));
        if (nbt.contains("Bashing"))
            ((IEntityBashingGetter) entity).setBashing(nbt.getBoolean("Bashing"));
        if (nbt.contains("BashingPostTicks"))
            ((IEntityBashingGetter) entity).setPostBashTicks(nbt.getInt("BashingPostTicks"));
        if (nbt.contains("RocketJumping"))
            ((IRocketJumpVelocityGetter) entity).setRocketJumping(nbt.getBoolean("RocketJumping"));
    }

    @Unique
    @Override
    public boolean isRocketJumping() {
        LivingEntity entity = (LivingEntity) (Object) this;
        return entity.getDataTracker().get(ROCKET_JUMPING);
    }

    @Unique
    @Override
    public void setRocketJumping(Boolean jumping) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.getDataTracker().set(ROCKET_JUMPING, jumping);
    }

    @Unique
    @Override
    public void setBashing(Boolean value) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.getDataTracker().set(IS_BASHING, value);
        int i = value ? 20 : 0;
        entity.getDataTracker().set(BASHING_POST_TICKS, i);

    }

    @Unique
    @Override
    public boolean isBashing() {
        LivingEntity entity = (LivingEntity) (Object) this;
        return entity.getDataTracker().get(IS_BASHING);
    }

    @Unique
    @Override
    public void setPostBashTicks(int value) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.getDataTracker().set(BASHING_POST_TICKS, value);
    }

    @Unique
    @Override
    public int getPostBashTicks() {
        LivingEntity entity = (LivingEntity) (Object) this;
        return entity.getDataTracker().get(BASHING_POST_TICKS);
    }

    @Unique
    @Override
    public void decrementPostBashTicks() {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.getDataTracker().set(BASHING_POST_TICKS, ((IEntityBashingGetter)entity).getPostBashTicks() - 1);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickDownShards(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.isOnGround() || entity.isTouchingWater()) {
            if (((IRocketJumpVelocityGetter)entity).isRocketJumping()){
                ((IRocketJumpVelocityGetter)entity).setRocketJumping(false);
            }

            if (entity.getActiveItem().getItem() instanceof PigsteelShieldItem shield && ((IEntityBashingGetter)entity).isBashing()){
                PigsteelShieldItem.postBash(entity);
            }
        }


        Optional<Integer> shardCount = Optional.of(((IEntityImpaledShardGetter) entity).getImpaledShardCount());
        if (shardCount.isPresent()) {
            if (((IEntityImpaledShardGetter) entity).getImpaledShardCount() > 0) {
                if (((IEntityImpaledShardGetter) entity).getImpaledShardCount() >= 8) ((IEntityImpaledShardGetter) entity).explodeAmethystCluster();
                ((IEntityImpaledShardGetter) entity).setImpaledShardTimer((((IEntityImpaledShardGetter) entity).getImpaledShardTimer() - 1));
                if (((IEntityImpaledShardGetter) entity).getImpaledShardTimer() <= 0) {
                    ((IEntityImpaledShardGetter) entity).setImpaledShardTimer(50);
                    ((IEntityImpaledShardGetter) entity).setImpaledShardCount((((IEntityImpaledShardGetter) entity).getImpaledShardCount() - 1));
                    if (entity.getWorld() instanceof ServerWorld server) {
                        server.spawnParticles(ParticleTypes.EXPLOSION, entity.getX(), entity.getEyeY(), entity.getZ(), 1, 0, 0, 0, 0);
                        server.playSound(null, entity.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.NEUTRAL, 1f, 1.5f);
                        for (int i = 0; i < 50; i++) server.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.AMETHYST_BLOCK, 1)), entity.getX(), entity.getEyeY(), entity.getZ(), 1, 0.2, 0.2, 0.2, 0.1);
                        entity.damage(entity.getDamageSources().generic(), 2f);
                        entity.getWorld().playSound(null, entity.getBlockPos(),SoSSoundEvents.MISC_AMETHYST_SHARD_BURST, SoundCategory.NEUTRAL, 1F, 1F);
                    }
                }
            }
        }
    }

    @Override
    @Unique
    public void explodeAmethystCluster() {
        LivingEntity entity = (LivingEntity) (Object) this;
        ((IEntityImpaledShardGetter) entity).setImpaledShardCount(((IEntityImpaledShardGetter) entity).getImpaledShardCount() - 8);
        if (((IEntityImpaledShardGetter) entity).getImpaledShardCount() > 0) {
            ((IEntityImpaledShardGetter) entity).setImpaledShardTimer(50);
        } else {
            ((IEntityImpaledShardGetter) entity).setImpaledShardTimer(0);
        }
        if (!entity.getWorld().isClient) {
            entity.getWorld().createExplosion(entity, entity.getX(), entity.getY(), entity.getZ(), 4, World.ExplosionSourceType.MOB);
        }
    }

    @Override
    @Unique
    public void setLodgedTridentStack(ItemStack stack) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.getDataTracker().set(LODGED_TRIDENT, stack);
    }

    @Override
    @Unique
    public ItemStack getLodgedTridentStack() {
        LivingEntity entity = (LivingEntity) (Object) this;
        return entity.getDataTracker().get(LODGED_TRIDENT);
    }

    @Override
    @Unique
    public void setImpaledShardCount(int count) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.getDataTracker().set(IMPALED_AMETHYST_SHARDS, count);
    }

    @Override
    @Unique
    public int getImpaledShardCount() {
        LivingEntity entity = (LivingEntity) (Object) this;
        return entity.getDataTracker().get(IMPALED_AMETHYST_SHARDS);
    }

    @Override
    @Unique
    public void setImpaledShardTimer(int timer) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.getDataTracker().set(IMPALED_AMETHYST_SHARD_TIMER, timer);
    }

    @Override
    @Unique
    public int getImpaledShardTimer() {
        LivingEntity entity = (LivingEntity) (Object) this;
        return entity.getDataTracker().get(IMPALED_AMETHYST_SHARD_TIMER);
    }


}
