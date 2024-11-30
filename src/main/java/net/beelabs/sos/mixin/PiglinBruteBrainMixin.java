package net.beelabs.sos.mixin;

import com.google.common.collect.ImmutableList;
import net.beelabs.sos.common.entity.ai.task.UseShieldTask;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.task.ForgetAngryAtTargetTask;
import net.minecraft.entity.ai.brain.task.LookAroundTask;
import net.minecraft.entity.ai.brain.task.OpenDoorsTask;
import net.minecraft.entity.ai.brain.task.WanderAroundTask;
import net.minecraft.entity.mob.PiglinBruteBrain;
import net.minecraft.entity.mob.PiglinBruteEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinBruteBrain.class)
public class PiglinBruteBrainMixin {
    @Inject(method = "addCoreActivities", at = @At("HEAD"), cancellable = true)
    private static void initCore(PiglinBruteEntity piglinBrute, Brain<PiglinBruteEntity> brain, CallbackInfo ci){
        brain.setTaskList(
                Activity.CORE,
                0, ImmutableList.of(
                        new LookAroundTask(45, 90),
                        new UseShieldTask<>())
        );

        ci.cancel();
    }
}
