package net.beelabs.sos.common.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.beelabs.sos.client.SweetsOSpiritsClient;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.entity.ai.brain.SoulmallowBrain;
import net.beelabs.sos.common.init.SoSMemoryModules;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSoulmallowEntity extends TameableEntity {
    public AbstractSoulmallowEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    enum Types{
        PROTECTOR,
        STRIKER,
        SOUL_SPOKEN,
        CHAOTIC
    }
}
