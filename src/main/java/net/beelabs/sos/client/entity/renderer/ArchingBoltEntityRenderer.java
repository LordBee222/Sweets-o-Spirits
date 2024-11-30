package net.beelabs.sos.client.entity.renderer;

import net.beelabs.sos.common.entity.ArchingBoltEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class ArchingBoltEntityRenderer extends EntityRenderer<ArchingBoltEntity> {

    public ArchingBoltEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }


    private static void drawBranch(
            Matrix4f matrix,
            VertexConsumer buffer,
            float x1,
            float z1,
            int y,
            float x2,
            float z2,
            float red,
            float green,
            float blue,
            float offset2,
            float offset1,
            boolean shiftEast1,
            boolean shiftSouth1,
            boolean shiftEast2,
            boolean shiftSouth2
    ) {
        buffer.vertex(matrix, x1 + (shiftEast1 ? offset1 : -offset1), (float)(y * 16), z1 + (shiftSouth1 ? offset1 : -offset1)).color(red, green, blue, 0.3F).next();
        buffer.vertex(matrix, x2 + (shiftEast1 ? offset2 : -offset2), (float)((y + 1) * 16), z2 + (shiftSouth1 ? offset2 : -offset2))
                .color(red, green, blue, 0.3F)
                .next();
        buffer.vertex(matrix, x2 + (shiftEast2 ? offset2 : -offset2), (float)((y + 1) * 16), z2 + (shiftSouth2 ? offset2 : -offset2))
                .color(red, green, blue, 0.3F)
                .next();
        buffer.vertex(matrix, x1 + (shiftEast2 ? offset1 : -offset1), (float)(y * 16), z1 + (shiftSouth2 ? offset1 : -offset1)).color(red, green, blue, 0.3F).next();
    }

    public Identifier getTexture(ArchingBoltEntity lightningEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
