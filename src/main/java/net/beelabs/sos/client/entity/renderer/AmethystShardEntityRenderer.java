package net.beelabs.sos.client.entity.renderer;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.entity.AmethystShardEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class AmethystShardEntityRenderer extends ProjectileEntityRenderer<AmethystShardEntity> {
    public static final Identifier TEXTURE = new Identifier(SweetsOSpirits.MOD_ID, "textures/entity/amethyst_shard.png");

    public AmethystShardEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }


    public Identifier getTexture(AmethystShardEntity amethystShardEntity) {
        return TEXTURE;
    }
}