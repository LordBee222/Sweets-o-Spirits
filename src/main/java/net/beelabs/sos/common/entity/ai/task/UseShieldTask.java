package net.beelabs.sos.common.entity.ai.task;

import com.google.common.collect.ImmutableMap;
import net.beelabs.sos.common.entity.SoulmallowEntity;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.CrossbowAttackTask;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class UseShieldTask <E extends MobEntity, T extends LivingEntity> extends MultiTickTask<E> {
    public static final TrackedData<Boolean> CAN_BLOCK = DataTracker.registerData(PiglinBruteEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final int RUN_TIME = 1200;
    private ShieldState state = ShieldState.UNCHARGED;
    private int chargingCooldown;


    public UseShieldTask() {
        super(
                ImmutableMap.of(
                        MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED,
                        MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT));

    }

    @Override
    protected boolean shouldRun(ServerWorld world, E entity) {
        LivingEntity target = getAttackTarget(entity);
        return entity.isHolding(Items.SHIELD);
    }

    @Override
    protected void run(ServerWorld world, E entity, long time) {
        entity.setCurrentHand(Hand.OFF_HAND);
    }

    /*
    protected boolean shouldKeepRunning(ServerWorld serverWorld, E entity, long l) {
        return entity.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET) && this.shouldRun(serverWorld, entity);
    }

    protected void keepRunning(ServerWorld serverWorld, E entity, long l) {
        LivingEntity livingEntity = getAttackTarget(entity);
        this.setLookTarget(entity, livingEntity);
        this.tickState(entity, livingEntity);
    }

    protected void finishRunning(ServerWorld serverWorld, E entity, long l) {
        if (entity.isUsingItem()) {
            entity.clearActiveItem();
        }
        if (entity.isHolding(Items.SHIELD)) {
            entity.getDataTracker().set(CAN_BLOCK, false);
        }
    }



    private void tickState(E entity, LivingEntity target) {
        if (this.state == ShieldState.UNCHARGED) {
            entity.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(entity, Items.CROSSBOW));
            this.state = ShieldState.ON_COOLDOWN;
            entity.getDataTracker().set(CAN_BLOCK, true);
        } else if (this.state == ShieldState.ON_COOLDOWN) {
            if (!entity.isUsingItem()) {
                this.state = ShieldState.UNCHARGED;
            }

            int i = entity.getItemUseTime();
            ItemStack itemStack = entity.getActiveItem();
            if (i >= CrossbowItem.getPullTime(itemStack)) {
                entity.stopUsingItem();
                this.state = ShieldState.OFF_COOLDOWN;
                this.chargingCooldown = 20 + entity.getRandom().nextInt(20);
                entity.getDataTracker().set(CAN_BLOCK, false);
            }
        } else if (this.state == ShieldState.OFF_COOLDOWN) {
            this.chargingCooldown--;
            if (this.chargingCooldown == 0) {
                this.state = ShieldState.READY_TO_BLOCK;
            }
        } else if (this.state == ShieldState.READY_TO_BLOCK) {
            entity.attack(target, 1.0F);
            ItemStack itemStack2 = entity.getStackInHand(ProjectileUtil.getHandPossiblyHolding(entity, Items.CROSSBOW));
            CrossbowItem.setCharged(itemStack2, false);
            this.state = ShieldState.UNCHARGED;
        }
    }

     */

    private void setLookTarget(MobEntity entity, LivingEntity target) {
        entity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(target, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity entity) {
        return (LivingEntity)entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).get();
    }



    static enum ShieldState {
        UNCHARGED,
        ON_COOLDOWN,
        OFF_COOLDOWN,
        READY_TO_BLOCK;
    }
}
