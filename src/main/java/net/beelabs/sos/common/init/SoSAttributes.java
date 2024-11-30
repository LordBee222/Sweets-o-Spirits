package net.beelabs.sos.common.init;

import net.beelabs.sos.common.SweetsOSpirits;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SoSAttributes {
    public static final EntityAttribute TURNING_SPEED = register(
            "turning_speed", new ClampedEntityAttribute("attribute.sos.name.generic.turning_speed", 1F, 0.0, 10.0).setTracked(true));

    private static EntityAttribute register(String name, EntityAttribute attribute){
        return Registry.register(Registries.ATTRIBUTE, new Identifier(SweetsOSpirits.MOD_ID, name), attribute);
    }

    public static void registerAttribute(){
        SweetsOSpirits.LOGGER.info("Registering Mod Items for " + SweetsOSpirits.MOD_ID);
    }
}
