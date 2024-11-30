package net.beelabs.sos.client.entity.renderer;

import net.beelabs.sos.client.entity.model.MallowpadJrModel;
import net.beelabs.sos.client.entity.model.MallowpadJrProjectileModel;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.entity.MallowpadJrEntity;
import net.beelabs.sos.common.entity.MallowpadJrProjectileEntity;
import net.beelabs.sos.common.init.SosModelLayers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MallowpadJrProjectileRenderer extends EntityRenderer<MallowpadJrProjectileEntity> {
    private static final Identifier TEXTURE = new Identifier(SweetsOSpirits.MOD_ID, "textures/entity/mallowpad_jr_projectile.png");
    private final MallowpadJrProjectileModel model;


    public MallowpadJrProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new MallowpadJrProjectileModel(ctx.getPart(SosModelLayers.MALLOWPAD_JR_PROJECTILE));
    }

    @Override
    public Identifier getTexture(MallowpadJrProjectileEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(MallowpadJrProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Render the entity model
        matrices.push();

        // Render the model
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(TEXTURE));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        matrices.pop();

        // Draw a connecting line to the entity's owner (if applicable)
        Entity owner = entity.getOwner(); // Assume the entity has a method to get its owner
        if (owner != null) {
            // Get positions
            double ownerX = MathHelper.lerp(tickDelta, owner.prevX, owner.getX());
            double ownerY = MathHelper.lerp(tickDelta, owner.prevY, owner.getY()) + owner.getStandingEyeHeight();
            double ownerZ = MathHelper.lerp(tickDelta, owner.prevZ, owner.getZ());

            double entityX = MathHelper.lerp(tickDelta, entity.prevX, entity.getX());
            double entityY = MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + 0.25; // Adjust based on entity model
            double entityZ = MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ());

            // Calculate deltas
            float deltaX = (float) (ownerX - entityX);
            float deltaY = (float) (ownerY - entityY);
            float deltaZ = (float) (ownerZ - entityZ);

            // Get the buffer for line rendering
            VertexConsumer lineBuffer = vertexConsumers.getBuffer(RenderLayer.getLineStrip());

            // Push matrix for line rendering
            matrices.push();
            MatrixStack.Entry matrixEntry = matrices.peek();

            // Draw segments of the line
            for (int i = 0; i <= 16; ++i) {
                renderFishingLine(deltaX, deltaY, deltaZ, lineBuffer, matrixEntry, percentage(i, 16), percentage(i + 1, 16));
            }

            matrices.pop();
        }

        // Call the super render method
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }


    private static float percentage(int value, int max) {
        return (float) value / (float) max;
    }

    // Render a single segment of the line
    private static void renderFishingLine(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry matrices, float segmentStart, float segmentEnd) {
        float startX = x * segmentStart;
        float startY = y * (segmentStart * segmentStart + segmentStart) * 0.5F + 0.25F;
        float startZ = z * segmentStart;

        float endX = x * segmentEnd - startX;
        float endY = y * (segmentEnd * segmentEnd + segmentEnd) * 0.5F + 0.25F - startY;
        float endZ = z * segmentEnd - startZ;

        float length = MathHelper.sqrt(endX * endX + endY * endY + endZ * endZ);
        endX /= length;
        endY /= length;
        endZ /= length;

        buffer.vertex(matrices.getPositionMatrix(), startX, startY, startZ)
                .color(0, 0, 0, 255)
                .normal(matrices.getNormalMatrix(), endX, endY, endZ)
                .next();
    }}
