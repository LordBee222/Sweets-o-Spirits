package net.beelabs.sos.client;

import net.beelabs.sos.client.entity.model.AmethystTridentModel;
import net.beelabs.sos.client.entity.model.MallowpadJrModel;
import net.beelabs.sos.client.entity.model.MallowpadJrProjectileModel;
import net.beelabs.sos.client.entity.model.SoulmallowModel;
import net.beelabs.sos.client.entity.renderer.*;
import net.beelabs.sos.client.entity.renderer.ArchingBoltEntityRenderer;
import net.beelabs.sos.client.item.PigsteelShieldModel;
import net.beelabs.sos.client.item.PigsteelShieldRenderer;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.init.SoSEntities;
import net.beelabs.sos.common.init.SoSItems;
import net.beelabs.sos.common.init.SosModelLayers;
import net.beelabs.sos.common.item.PigsteelShieldItem;
import net.beelabs.sos.common.keybind.UnleashedDashKeybind;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class SweetsOSpiritsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(SosModelLayers.SOULMALLOW, SoulmallowModel::getTexturedModelData);
        EntityRendererRegistry.register(SoSEntities.SOULMALLOW, SoulmallowRenderer::new);
        EntityRendererRegistry.register(SoSEntities.WHISP, FlyingItemEntityRenderer::new);

        UnleashedDashKeybind.registerBind();
        EntityModelLayerRegistry.registerModelLayer(SosModelLayers.MALLOWPAD_JR, MallowpadJrModel::getTexturedModelData);
        EntityRendererRegistry.register(SoSEntities.MALLOWPAD_JR, MallowpadJrRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(SosModelLayers.MALLOWPAD_JR_PROJECTILE, MallowpadJrProjectileModel::getTexturedModelData);
        EntityRendererRegistry.register(SoSEntities.MALLOWPAD_JR_PROJECTILE, MallowpadJrProjectileRenderer::new);

        EntityRendererRegistry.register(SoSEntities.AMETHYST_SHARD, AmethystShardEntityRenderer::new);
        EntityRendererRegistry.register(SoSEntities.BUDDING_BOMB, FlyingItemEntityRenderer::new);

        EntityRendererRegistry.register(SoSEntities.AMETHYST_TRIDENT, AmethystTridentEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(SosModelLayers.AMETHYST_TRIDENT, AmethystTridentModel::getTexturedModelData);

        EntityRendererRegistry.register(SoSEntities.ARCHING_BOLT, ArchingBoltEntityRenderer::new);

        AmethystTridentItemRenderer tridentItemRenderer = new AmethystTridentItemRenderer();
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(tridentItemRenderer);
        BuiltinItemRendererRegistry.INSTANCE.register(SoSItems.AMETHYST_TRIDENT, tridentItemRenderer);
        FabricModelPredicateProviderRegistry.register(SoSItems.AMETHYST_TRIDENT, new Identifier("throwing"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) ->
                out.accept(new ModelIdentifier(SweetsOSpirits.MOD_ID,  "amethyst_trident_in_inventory", "inventory")));

        EntityModelLayerRegistry.registerModelLayer(SosModelLayers.PIGSTEEL_SHIELD, PigsteelShieldModel::getTexturedModelData);
        PigsteelShieldRenderer shieldRenderer = new PigsteelShieldRenderer();
        BuiltinItemRendererRegistry.INSTANCE.register(SoSItems.PIGSTEEL_SHIELD, shieldRenderer);
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(shieldRenderer);


        ModelPredicateProviderRegistry.register(SoSItems.PIGSTEEL_SHIELD, new Identifier("blocking"), ((stack, world, entity, seed) -> {
            boolean active = entity != null && entity.isUsingItem()
                    && entity.getActiveItem() == stack;
            return entity != null && active ? 1.0F : 0.0F;
        }));

        PigsteelShieldItem.registerClientTickEvent();
    }
}
