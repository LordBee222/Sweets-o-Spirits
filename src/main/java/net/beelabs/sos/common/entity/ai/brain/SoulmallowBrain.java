package net.beelabs.sos.common.entity.ai.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.beelabs.sos.common.entity.SoulmallowEntity;
import net.beelabs.sos.common.entity.ai.task.FollowOwnerTask;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class SoulmallowBrain {

    public static Brain<?> init(Brain<SoulmallowEntity> brain, SoulmallowEntity soulmallow) {
        addCoreTasks(brain);
        addIdleTasks(brain, soulmallow);
        addFightTasks(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreTasks(Brain<SoulmallowEntity> brain) {
        brain.setTaskList(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new FollowOwnerTask(0.5f, 2.0f, 100.0f),
                        new LookAroundTask(45, 90),
                        new WanderAroundTask()));
    }

    private static void addIdleTasks(Brain<SoulmallowEntity> brain, SoulmallowEntity soulmallow) {
        brain.setTaskList(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        LookAtMobWithIntervalTask.follow(8.0f, UniformIntProvider.create(30, 60)),
                        initRandomWalkTask(soulmallow)));
    }

    private static void addFightTasks(Brain<SoulmallowEntity> brain) {
        brain.setTaskList(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        ForgetAttackTargetTask.create()),
                MemoryModuleType.ATTACK_TARGET);
    }

    public static void refreshActivities(SoulmallowEntity porcupine) {
        Brain<SoulmallowEntity> brain = (Brain<SoulmallowEntity>) porcupine.getBrain();
        brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.IDLE));
        porcupine.setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
    }

    private static RandomTask<SoulmallowEntity> initRandomWalkTask(SoulmallowEntity soulmallow) {
        return new RandomTask<SoulmallowEntity>(ImmutableList.of(Pair.of(StrollTask.create((float) soulmallow.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)), 2), Pair.of(GoTowardsLookTargetTask.create((float) soulmallow.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED), 3), 2), Pair.of(new WaitTask(30, 60), 1)));
    }
}
