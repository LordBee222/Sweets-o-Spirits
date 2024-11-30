package net.beelabs.sos.common.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.entity.ai.brain.SoulmallowBrain;
import net.beelabs.sos.common.init.SoSMemoryModules;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SoulmallowEntity extends TameableEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int countOtherTicks = 0;

    public static final ImmutableList<? extends SensorType<? extends Sensor<? super SoulmallowEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS);
    public static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_MODULE_TYPES = (ImmutableList<? extends MemoryModuleType<?>>) ImmutableList.of(
            MemoryModuleType.MOBS,
            MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.AVOID_TARGET,
            SoSMemoryModules.REDIRECTED_DAMAGE_TAKEN,
            SoSMemoryModules.REDIRECT_DAMAGE_ON_COOLDOWN);

    private final Map<Integer, Float> redirectedDamageCausers;



    public SoulmallowEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.redirectedDamageCausers = new HashMap<>();
    }

    public boolean canRedirectDamage() {
        return this.getOwner() != null &&
                !this.getBrain().hasMemoryModule(SoSMemoryModules.REDIRECT_DAMAGE_ON_COOLDOWN) &&
                this.hasAliveOwner() &&
                this.distanceTo(this.getOwner()) <= 30;
    }

    public Map<Integer, Float> getRedirectedDamageCausers() {
        return redirectedDamageCausers;
    }

    protected Brain.Profile<SoulmallowEntity> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULE_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return SoulmallowBrain.init(this.createBrainProfile().deserialize(dynamic), this);
    }

    public Brain<SoulmallowEntity> getBrain() {
        return (Brain<SoulmallowEntity>) super.getBrain();
    }


    @Override
    protected void mobTick() {
        this.getWorld().getProfiler().push("soulmallowBrain");
        this.getBrain().tick((ServerWorld) this.getWorld(), this);
        this.getWorld().getProfiler().pop();
        SoulmallowBrain.refreshActivities(this);
        ++this.countOtherTicks;
        if (this.canRedirectDamage() && countOtherTicks % 5 == 0) {
            if (this.getOwner() != null && this.getWorld() instanceof ServerWorld server) {
                server.spawnParticles(ParticleTypes.SCULK_SOUL, this.getX(), this.getRandomBodyY(), this.getZ(), 1, 0.1, 0.1, 0.1, 0.02);
                server.spawnParticles(ParticleTypes.SCULK_SOUL, this.getOwner().getX(), this.getOwner().getRandomBodyY(), this.getOwner().getZ(), 1, 0.1, 0.1, 0.2, 0.02);
            }
        }
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    protected void updateLimbs(float v) {
        float f;
        if (this.getPose() == EntityPose.STANDING) {
            f = Math.min(v * 6.0F, 1.0F);
        } else {
            f = 0.0F;
        }

        this.limbAnimator.updateLimbs(f, 0.2F);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        }
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }

    public boolean hasAliveOwner() {
        return this.getOwner() != null && this.getOwner().isAlive();
    }

}
