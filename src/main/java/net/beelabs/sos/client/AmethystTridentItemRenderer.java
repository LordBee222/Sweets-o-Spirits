package net.beelabs.sos.client;

import net.beelabs.sos.client.entity.model.AmethystTridentModel;
import net.beelabs.sos.client.entity.renderer.AmethystTridentEntityRenderer;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.init.SoSEntities;
import net.beelabs.sos.common.init.SosModelLayers;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Collections;

public class AmethystTridentItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, SimpleSynchronousResourceReloadListener {
    private static final Identifier ID = new Identifier(SweetsOSpirits.MOD_ID, "amethyst_trident_renderer");

    private ItemRenderer itemRenderer;
    private AmethystTridentModel tridentModel;
    private BakedModel inventoryTridentModel;

    public AmethystTridentItemRenderer() {

    }

    @Override
    public Identifier getFabricId() {
        return this.ID;
    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return Collections.singletonList(ResourceReloadListenerKeys.MODELS);
    }

    @Override
    public void reload(ResourceManager manager) {
        MinecraftClient mc = MinecraftClient.getInstance();
        this.itemRenderer = mc.getItemRenderer();
        this.tridentModel = new AmethystTridentModel(mc.getEntityModelLoader().getModelPart(SosModelLayers.AMETHYST_TRIDENT));
        this.inventoryTridentModel = mc.getBakedModelManager().getModel(new ModelIdentifier(SweetsOSpirits.MOD_ID, "amethyst_trident_in_inventory", "inventory"));
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (mode == ModelTransformationMode.GUI || mode == ModelTransformationMode.GROUND || mode == ModelTransformationMode.FIXED) {
            matrices.pop(); // Cancel the previous transformation
            matrices.push();
            itemRenderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, this.inventoryTridentModel);
        } else {
            matrices.push();
            matrices.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.tridentModel.getLayer(AmethystTridentEntityRenderer.TEXTURE), false, stack.hasGlint());
            this.tridentModel.render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            matrices.pop();
        }
    }
}
