package net.beelabs.sos.client.item;

import net.beelabs.sos.client.entity.model.AmethystTridentModel;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.init.SoSItems;
import net.beelabs.sos.common.init.SosModelLayers;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Collections;

public class PigsteelShieldRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, SimpleSynchronousResourceReloadListener {
    private PigsteelShieldModel model;

    private static final Identifier RELOAD_ID = new Identifier(SweetsOSpirits.MOD_ID, "pigsteel_shield_renderer");

    @Override
    public void reload(ResourceManager manager) {
        // Initialize the model during resource reload
        this.model = new PigsteelShieldModel(MinecraftClient.getInstance().getEntityModelLoader()
                .getModelPart(SosModelLayers.PIGSTEEL_SHIELD));
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.getItem() == SoSItems.PIGSTEEL_SHIELD.asItem()) {
            if (this.model == null) {
                // Ensure the model is initialized in case reload wasn't called
                reload(null); // Reload manually if needed (null is safe for ResourceManager here)
            }

            matrices.push();
            matrices.scale(1.0F, -1.0F, -1.0F); // Invert to match shield rendering
            Identifier texture = new Identifier(SweetsOSpirits.MOD_ID, "textures/entity/pigsteel_shield.png");
            this.model.render(matrices, vertexConsumers.getBuffer(this.model.getLayer(texture)), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            matrices.pop();
        }
    }

    @Override
    public Identifier getFabricId() {
        return RELOAD_ID;
    }
}
