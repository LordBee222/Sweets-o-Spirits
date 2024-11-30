package net.beelabs.sos.mixin;

import net.beelabs.sos.common.init.SoSItems;
import net.beelabs.sos.common.item.MarshmallowOnAStickItem;
import net.beelabs.sos.mixin.accessor.HeldItemRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class CookingMarshmallowMixin {


    @Shadow protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);

    @Inject(
            method = "renderFirstPersonItem",
            at = @At("HEAD")
    )
    private void modifyArmPoseBeforeRendering(
            AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci
    ) {
        // Check if the player is holding the Marshmallow item
        if (item.getItem() == SoSItems.MARSHMALLOW_ON_A_STICK && item.getNbt().contains(MarshmallowOnAStickItem.COOKING_KEY)) { // Replace `YourModItems.MARSHMALLOW` with your actual item reference
            // Adjust the arm position BEFORE default rendering logic
            if (hand == Hand.MAIN_HAND) { // Modify only the main hand if desired
                matrices.push();
            Arm arm = player.getMainArm();
            int l = arm == Arm.RIGHT ? 1 : -1;
                //matrices.translate(- 0.05F, 0.05F, 0F); // Move the arm forward
                //matrices.translate(-0.055F, 0.6F, -0.25F); // Move the arm forward
                matrices.translate(-0.055F, 0.6F, -0.45F); // Move the arm forward

               // applyEquipOffset(matrices, arm, equipProgress);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) l * -80));
                //matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) l * -13.935F));

              //  matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) l * 30F));                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) l * 14F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) l * 30F));



                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) l * 30f));
//                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) l * 10f));


            }
        }
    }

    @Inject(
            method = "renderFirstPersonItem",
            at = @At("RETURN")
    )
    private void revertMatrixAfterRendering(
            AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci
    ) {
        // Check if the player is holding the Marshmallow item
        if (item.getItem() == SoSItems.MARSHMALLOW_ON_A_STICK) { // Replace `YourModItems.MARSHMALLOW` with your actual item reference
            // Revert transformations AFTER default rendering logic
            if (hand == Hand.MAIN_HAND) {
               // matrices.pop();
            }
        }
    }
}
