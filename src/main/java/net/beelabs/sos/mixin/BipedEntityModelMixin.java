package net.beelabs.sos.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin <T extends LivingEntity> extends AnimalModel<T> {

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Inject(at = @At("TAIL"), method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
       if (entityIn instanceof PiglinBruteEntity) {
           if (entityIn.getMainArm() == Arm.RIGHT) {
               this.bucklerAnimationsRightArm(Hand.MAIN_HAND, entityIn);
               this.bucklerAnimationsLeftArm(Hand.OFF_HAND, entityIn);
           } else {
               this.bucklerAnimationsLeftArm(Hand.MAIN_HAND, entityIn);
               this.bucklerAnimationsRightArm(Hand.OFF_HAND, entityIn);
           }
       }
    }

    @Unique
    public void bucklerAnimationsLeftArm(Hand hand, T entityIn) {
        if (entityIn.getActiveHand() == hand && entityIn.getStackInHand(entityIn.getActiveHand()).getItem() instanceof ShieldItem) {
            float useDuration = (float) entityIn.getStackInHand(entityIn.getActiveHand()).getMaxUseTime();
            float useDurationClamped = MathHelper.clamp((float) entityIn.getItemUseTime(), 0.0F, useDuration);
            float result = useDurationClamped / useDuration;
            this.leftArm.pivotY = MathHelper.lerp(result, leftArm.pivotY, 1.1466812652970528F);
            this.leftArm.pivotX = MathHelper.lerp(result, leftArm.pivotX, this.leftArm.pivotX * 0.1F - 1.5F);
        }
        if (entityIn.isBlocking()) {
            ItemStack handItems = hand == Hand.MAIN_HAND ? entityIn.getOffHandStack() : entityIn.getMainHandStack();
            if (!handItems.isEmpty()) {
                this.rightArm.pivotX = 0.5F - (float) Math.PI * 0.5F - 0.9424779F;
                this.rightArm.pivotY = ((float) Math.PI / 6F);
            }
            this.leftArm.pivotX = this.leftArm.pivotX * 0.1F - 1.5F;
            this.leftArm.pivotY = 1.1466812652970528F;
        }
    }

    public void bucklerAnimationsRightArm(Hand hand, T entityIn) {
        if (entityIn.getActiveHand() == hand && entityIn.getStackInHand(entityIn.getActiveHand()).getItem() instanceof ShieldItem) {
            float useDuration = (float) entityIn.getStackInHand(entityIn.getActiveHand()).getMaxUseTime();
            float useDurationClamped = MathHelper.clamp((float) entityIn.getItemUseTime(), 0.0F, useDuration);
            float result = useDurationClamped / useDuration;
            this.rightArm.pivotY = MathHelper.lerp(result, rightArm.pivotY, -1.1466812652970528F);
            this.rightArm.pivotX = MathHelper.lerp(result, rightArm.pivotX, this.rightArm.pivotX * 0.1F - 1.5F);
        }
        if (entityIn.isBlocking()) {
            ItemStack handItems = hand == Hand.MAIN_HAND ? entityIn.getOffHandStack() : entityIn.getMainHandStack();
            if (!handItems.isEmpty()) {
                this.leftArm.pivotX = 0.5F - (float) Math.PI * 0.5F - 0.9424779F;
                this.leftArm.pivotY = (-(float) Math.PI / 6F);
            }
            this.rightArm.pivotX = 0.0F * 0.1F - 1.5F;
            this.rightArm.pivotY = -1.1466812652970528F;
        }
    }
}
