package net.beelabs.sos.client.entity.model;

import net.beelabs.sos.common.entity.SoulmallowEntity;
import net.beelabs.sos.common.init.SoSAnimations;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class SoulmallowModel<T extends SoulmallowEntity> extends SinglePartEntityModel<T> {
    private final ModelPart body;
    private final ModelPart legs;
    private final ModelPart left_leg;
    private final ModelPart right_leg;
    private final ModelPart head;
    private final ModelPart crown;
    public SoulmallowModel(ModelPart root) {
        this.body = root.getChild("body");
        this.legs = this.body.getChild("legs");
        this.left_leg = this.legs.getChild("left_leg");
        this.right_leg = this.legs.getChild("right_leg");
        this.head = this.body.getChild("head");
        this.crown = this.head.getChild("crown");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, 20.0F, 0.0F));

        ModelPartData legs = body.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, 4.0F, -3.0F));

        ModelPartData left_leg = legs.addChild("left_leg", ModelPartBuilder.create().uv(18, 18).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, -4.0F, 3.5F));

        ModelPartData right_leg = legs.addChild("right_leg", ModelPartBuilder.create().uv(18, 26).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, -4.0F, 3.5F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.5F, -9.0F, -4.5F, 9.0F, 9.0F, 9.0F, new Dilation(0.0F))
                .uv(0, 18).cuboid(0.0F, -12.0F, -4.5F, 0.0F, 3.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, 0.5F));

        ModelPartData crown = head.addChild("crown", ModelPartBuilder.create().uv(39, 40).cuboid(5.5F, -1.0F, -0.7F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F))
                .uv(39, 40).cuboid(-0.5F, -1.0F, -0.7F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F))
                .uv(39, 40).cuboid(4.5F, 0.0F, -0.7F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                .uv(39, 40).cuboid(0.5F, 0.0F, -0.7F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                .uv(39, 40).cuboid(0.5F, -2.0F, -0.7F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                .uv(39, 40).cuboid(4.5F, -2.0F, -0.7F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                .uv(39, 40).cuboid(2.5F, 0.0F, 8.6F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F))
                .uv(39, 40).cuboid(1.5F, 3.0F, 8.6F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, -8.0F, -4.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(SoulmallowEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(entity, netHeadYaw, headPitch, ageInTicks);

        this.animateMovement(SoSAnimations.SOULMALLOW_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.updateAnimation(entity.idleAnimationState, SoSAnimations.SOULMALLOW_IDLE, ageInTicks, 1f);
    }

    private void setHeadAngles(SoulmallowEntity entity, float headYaw, float headPitch, float animationProgress) {
        headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);
        headPitch = MathHelper.clamp(headPitch, -25.0F, 45.0F);

        this.head.yaw = headYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;
    }


    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return body;
    }

}
