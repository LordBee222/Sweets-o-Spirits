package net.beelabs.sos.client.entity.model;

import net.beelabs.sos.common.entity.MallowpadJrEntity;
import net.beelabs.sos.common.entity.SoulmallowEntity;
import net.beelabs.sos.common.init.SoSAnimations;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class MallowpadJrModel extends Model {
    private final ModelPart mallowpad;


    public MallowpadJrModel(ModelPart root) {
        super(RenderLayer::getEntitySolid);
        this.mallowpad = root.getChild("mallowpad");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData mallowpad = modelPartData.addChild("mallowpad", ModelPartBuilder.create().uv(0, 0).cuboid(-7.0F, -4.0F, -7.0F, 14.0F, 4.0F, 14.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 4.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.mallowpad.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
