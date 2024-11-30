package net.beelabs.sos.client.item;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class PigsteelShieldModel extends Model {
    public final ModelPart root;
    public final ModelPart base;
    public final ModelPart handle;

    public PigsteelShieldModel(ModelPart part) {
        super(RenderLayer::getEntityTranslucent);
        this.root = part;
        this.base = part.getChild("base");
        this.handle = part.getChild("handle");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -5.0F, -2.0F, 10.0F, 10.0F, 1.0F), ModelTransform.NONE);
        modelPartData.addChild("handle", ModelPartBuilder.create().uv(0, 11).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 6.0F), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 32, 32);
    }

    public ModelPart getBase() {
        return this.base;
    }

    public ModelPart getHandle() {
        return this.handle;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay);
    }
}
