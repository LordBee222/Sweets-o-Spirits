package net.beelabs.sos.client.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MallowpadJrProjectileModel extends Model {
    private final ModelPart root;

    public MallowpadJrProjectileModel(ModelPart root) {
        super(RenderLayer::getEntitySolid);
        this.root = root.getChild("root");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -3.0F, -5.0F, 10.0F, 3.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 4.0F, 0.0F));

        ModelPartData strap_front_r1 = root.addChild("strap_front_r1", ModelPartBuilder.create().uv(26, 13).cuboid(-1.0F, -3.0F, -1.5F, 2.0F, 3.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 0.0F, -3.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData strap_back_r1 = root.addChild("strap_back_r1", ModelPartBuilder.create().uv(0, 13).cuboid(-1.0F, -3.0F, -5.5F, 2.0F, 3.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 3.0F, 0.0F, -1.5708F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }



}
