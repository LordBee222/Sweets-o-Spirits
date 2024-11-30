package net.beelabs.sos.mixin;

import net.beelabs.sos.client.entity.model.AmethystTridentModel;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.init.SoSItems;
import net.beelabs.sos.common.init.SosModelLayers;
import net.beelabs.sos.mixin.accessor.BuiltinModelItemRendererAccessor;
import net.beelabs.sos.mixin.accessor.ItemRendererAccessor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class AmethystTridentMixin {

    @Mixin(BuiltinModelItemRenderer.class)
    public static abstract class BuiltinModelItemRendererMixin{
        @Unique private AmethystTridentModel modelAmethystTrident;


        @Inject(method = "reload", at = @At("TAIL"))
        private void injectReload(ResourceManager manager, CallbackInfo ci){
            BuiltinModelItemRenderer renderer = (BuiltinModelItemRenderer) (Object) this;
            BuiltinModelItemRendererAccessor accessor = (BuiltinModelItemRendererAccessor)renderer;
            this.modelAmethystTrident = new AmethystTridentModel(accessor.getEntityModelLoader().getModelPart(SosModelLayers.AMETHYST_TRIDENT));
        }

        @Inject(method = "render", at = @At("TAIL"))
        private void injectRender(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci){
            if (stack.isOf(SoSItems.AMETHYST_TRIDENT)){
                matrices.push();
                matrices.scale(1.0F, -1.0F, -1.0F);
                VertexConsumer vertexConsumer2 = ItemRenderer.getDirectItemGlintConsumer(
                        vertexConsumers, this.modelAmethystTrident.getLayer(TridentEntityModel.TEXTURE), false, stack.hasGlint()
                );
                this.modelAmethystTrident.render(matrices, vertexConsumer2, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
                matrices.pop();
            }
        }
    }

    @Mixin(ItemRenderer.class)
    public static abstract class ItemRendererMixin{
        @Unique private static final ModelIdentifier AMETHYST_TRIDENT = new ModelIdentifier(SweetsOSpirits.MOD_ID, "amethyst_trident", "inventory");
        @Unique private static final ModelIdentifier AMETHYST_TRIDENT_IN_HAND = new ModelIdentifier(SweetsOSpirits.MOD_ID, "amethyst_trident_in_hand", "inventory");

        @Shadow
        private ItemModels models;

        @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
        public void getAmethystTridentModel(
                ItemStack stack,
                @Nullable World world,
                @Nullable LivingEntity entity,
                int seed,
                CallbackInfoReturnable<BakedModel> cir
        ) {
            // Check if the ItemStack is the custom trident
            if (stack.isOf(SoSItems.AMETHYST_TRIDENT)) {
                // Get the custom "in hand" model for the amethyst trident
                BakedModel amethystTridentModel = this.models.getModelManager().getModel(AMETHYST_TRIDENT_IN_HAND);

                // Get the client world (if applicable)
                ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld) world : null;

                // Apply overrides (e.g., enchantments or other dynamic behaviors)
                BakedModel overriddenModel = amethystTridentModel.getOverrides().apply(amethystTridentModel, stack, clientWorld, entity, seed);

                // Return the overridden model if it exists, otherwise the base model
                cir.setReturnValue(overriddenModel != null ? overriddenModel : amethystTridentModel);
            }
        }

        @Shadow
        protected abstract void renderBakedItemModel(
                BakedModel model,
                ItemStack stack,
                int light,
                int overlay,
                MatrixStack matrices,
                VertexConsumer vertexConsumer
        );

        @Inject(method = "renderItem*", at = @At("HEAD"), cancellable = true)
        public void renderCustomTrident(
                ItemStack stack,
                ModelTransformationMode renderMode,
                boolean leftHanded,
                MatrixStack matrices,
                VertexConsumerProvider vertexConsumers,
                int light,
                int overlay,
                BakedModel model,
                CallbackInfo ci
        ) {
            // Check if the stack is the custom trident
            if (stack.isOf(SoSItems.AMETHYST_TRIDENT)) {
                matrices.push();

                // Check if the rendering mode matches GUI, GROUND, or FIXED
                boolean isSpecialRenderMode = renderMode == ModelTransformationMode.GUI
                        || renderMode == ModelTransformationMode.GROUND
                        || renderMode == ModelTransformationMode.FIXED;

                if (isSpecialRenderMode) {
                    // Get the custom model for the amethyst trident
                    model = this.models.getModelManager().getModel(AMETHYST_TRIDENT);
                }

                // Apply the model's transformation
                model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);

                // Offset the model for proper rendering
                matrices.translate(-0.5F, -0.5F, -0.5F);

                // Set up the appropriate render layer
                RenderLayer renderLayer = RenderLayers.getItemLayer(stack, true);
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);

                // Render the custom trident model
                this.renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer);

                matrices.pop();
                ci.cancel(); // Cancel further rendering to prevent vanilla handling
            }
        }
    }
}
