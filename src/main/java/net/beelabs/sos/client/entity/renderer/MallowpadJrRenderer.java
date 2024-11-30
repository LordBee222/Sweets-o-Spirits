package net.beelabs.sos.client.entity.renderer;

import net.beelabs.sos.client.entity.model.MallowpadJrModel;
import net.beelabs.sos.client.entity.model.SoulmallowModel;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.entity.MallowpadJrEntity;
import net.beelabs.sos.common.entity.SoulmallowEntity;
import net.beelabs.sos.common.init.SosModelLayers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class MallowpadJrRenderer extends EntityRenderer<MallowpadJrEntity> {
    private static final Identifier TEXTURE = new Identifier(SweetsOSpirits.MOD_ID, "textures/entity/mallowpad_jr.png");
    private final MallowpadJrModel model;


    public MallowpadJrRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new MallowpadJrModel(ctx.getPart(SosModelLayers.MALLOWPAD_JR));
    }

    @Override
    public Identifier getTexture(MallowpadJrEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(MallowpadJrEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        // Render the model
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(TEXTURE));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
