package net.beelabs.sos.common.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.entity.ai.brain.SoulmallowBrain;
import net.beelabs.sos.common.init.SoSEntities;
import net.beelabs.sos.common.init.SoSMemoryModules;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhispEntity extends ProjectileEntity implements FlyingItemEntity {
    private static final double SPEED = 0.5;
    private static final float EXPLOSION_RADIUS = 5.0F;
    private LivingEntity target;
    private int ticksTillHoming;


    public WhispEntity(EntityType<? extends WhispEntity> entityType, World world) {
        super(entityType, world);
    }

    public WhispEntity(World world, LivingEntity target, LivingEntity owner) {
        this(SoSEntities.WHISP, world);
        this.target = target;
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getY() + 0.5f, owner.getZ());  // Start above the target\
        this.ticksTillHoming = owner.getRandom().nextBetween(25, 100);
        this.setVelocity(new Vec3d(0, 0.1, 0));
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.getWorld() instanceof ServerWorld) {
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 255, false, World.ExplosionSourceType.TNT);
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            if (target != null) {
                if (this.target.isDead() && this.getOwner() instanceof LivingEntity owner && owner.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET))
                    this.target = owner.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
                if (this.ticksTillHoming > 0) --this.ticksTillHoming;
                if (ticksTillHoming == 0) {
                    if (target != null && target.isAlive()) {
                        Vec3d direction = target.getPos().add(0, target.getHeight() / 2.0, 0).subtract(this.getPos()).normalize();
                        Vec3d velocity = direction.multiply(SPEED);
                        this.setVelocity(velocity);
                        if (this.squaredDistanceTo(target.getPos().add(0, target.getHeight() / 2.0, 0)) < 0.1)
                            explode();
                    }
                }
                this.move(MovementType.SELF, this.getVelocity());
                serverWorld.spawnParticles(ParticleTypes.SCULK_SOUL, this.getX(), this.getY(), this.getZ(), 5, 0.1, 0.1, 0.1, 0.02);
            } else {
                this.discard();
            }
        }
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && !entity.noClip;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    private void explode() {
        this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 1, World.ExplosionSourceType.MOB);
        this.playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH, 1.0f, 2.0f);
        //this.explode();
        this.target.setOnFireFor(10);

        if (this.getWorld() instanceof ServerWorld server) server.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        this.discard();
    }

    private void destroy() {
        this.discard();
        this.getWorld().emitGameEvent(GameEvent.ENTITY_DAMAGE, this.getPos(), GameEvent.Emitter.of(this));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.getWorld().isClient) {
            this.playSound(SoundEvents.PARTICLE_SOUL_ESCAPE, 1.0f, 2.0f);
            ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2, 0.2, 0.2, 0.0);
            this.destroy();
        }
        return true;
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(Items.SOUL_LANTERN);
    }

}
