package net.beelabs.sos.common.init;

import net.beelabs.sos.common.SweetsOSpirits;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class SosModelLayers {
    public static final EntityModelLayer SOULMALLOW =
            new EntityModelLayer(new Identifier(SweetsOSpirits.MOD_ID, "soulmallow"), "main");

    public static final EntityModelLayer MALLOWPAD_JR =
            new EntityModelLayer(new Identifier(SweetsOSpirits.MOD_ID, "mallowpad_jr"), "main");

    public static final EntityModelLayer MALLOWPAD_JR_PROJECTILE =
            new EntityModelLayer(new Identifier(SweetsOSpirits.MOD_ID, "mallowpad_jr_projectile"), "main");

    public static final EntityModelLayer AMETHYST_TRIDENT =
            new EntityModelLayer(new Identifier(SweetsOSpirits.MOD_ID, "amethyst_trident"), "main");

    public static final EntityModelLayer PIGSTEEL_SHIELD =
            new EntityModelLayer(new Identifier(SweetsOSpirits.MOD_ID, "pigsteel_shield"), "main");
}
