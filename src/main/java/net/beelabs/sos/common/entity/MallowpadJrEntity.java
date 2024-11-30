package net.beelabs.sos.common.entity;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.init.SoSEntities;
import net.beelabs.sos.common.init.SoSStatusEffects;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class MallowpadJrEntity extends Entity {
    private int ticksTillDeletion;


    public MallowpadJrEntity(EntityType<? extends MallowpadJrEntity> entityType, World world) {
        super(entityType, world);
    }

    public MallowpadJrEntity(World world) {
        this(SoSEntities.MALLOWPAD_JR, world);
        this.ticksTillDeletion = 200;
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
        this.calculateDimensions();
        this.ticksTillDeletion = 200;
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksTillDeletion--;
        List<Entity> entities = this.getWorld().getEntitiesByClass(Entity.class, this.getBoundingBox(), e -> true);
        for (Entity entity : entities) {
            if (this.getBoundingBox().intersects(entity.getBoundingBox())) {
               this.bounceEntity(entity);
            }
        }
        if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
        }
        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.98));
        if (ticksTillDeletion <= 0){
            this.burst();
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    private void bounceEntity(Entity entity){
        if (entity.getType() == this.getType()) return;
        Vec3d velocity = entity.getVelocity();
        entity.setVelocity(velocity.getX() , 2, velocity.getZ());
        entity.velocityModified = true;
        entity.fallDistance = 0;
        if (entity instanceof LivingEntity living) living.addStatusEffect(new StatusEffectInstance(SoSStatusEffects.MALLOWED, 500, 0));
        if (entity.getWorld() instanceof ServerWorld server){
            server.playSound(null, this.getBlockPos(), SoundEvents.BLOCK_HONEY_BLOCK_STEP, SoundCategory.NEUTRAL,1f, 1.5f);
            server.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);

        }
    }

    public Item getBreakItemParticle() {
        return Items.SNOW_BLOCK;
    }

    private void burst(){
        if (this.getWorld() instanceof ServerWorld server) {
            server.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
            server.playSound(null, this.getBlockPos(), SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, SoundCategory.NEUTRAL,1f, 2f);
            for (int i = 0; i < 50; i++) {
                server.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(getBreakItemParticle(), 1)), this.getX(), this.getY(), this.getZ(), 1, 0.2, 0.2, 0.2, 0.1);
            }
            this.discard();
        }
    }
}
