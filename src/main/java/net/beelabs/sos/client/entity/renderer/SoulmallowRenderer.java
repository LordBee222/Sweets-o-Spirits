package net.beelabs.sos.client.entity.renderer;

import net.beelabs.sos.client.entity.model.SoulmallowModel;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.entity.SoulmallowEntity;
import net.beelabs.sos.common.init.SosModelLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SoulmallowRenderer extends MobEntityRenderer<SoulmallowEntity, SoulmallowModel<SoulmallowEntity>> {
    private static final Identifier TEXTURE = new Identifier(SweetsOSpirits.MOD_ID, "textures/entity/soulmallow.png");

    public SoulmallowRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SoulmallowModel<>(ctx.getPart(SosModelLayers.SOULMALLOW)), 0.6f);
    }

    @Override
    public Identifier getTexture(SoulmallowEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(SoulmallowEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.scale(0.6f, 0.6f, 0.6f);
        //if (livingEntity.isBaby()) matrixStack.scale(0.5f, 0.5f, 0.5f);
        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
