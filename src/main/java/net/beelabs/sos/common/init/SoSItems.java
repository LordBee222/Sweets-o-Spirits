package net.beelabs.sos.common.init;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SoSItems {
    public static final Item MARSHMALLOW = register("marshmallow",
            new Item(new FabricItemSettings().food(SoSFoodComponents.MARSHMALLOW)));

    public static final Item MARSHMALLOW_ON_A_STICK = register("marshmallow_on_a_stick",
            new MarshmallowOnAStickItem(new FabricItemSettings().maxCount(1)));

    public static final Item MALLOWPAD_JR = register("mallowpad_jr",
            new MallowpadJrItem(new FabricItemSettings().maxCount(16)));

    public static final Item BUDDING_BOMB = register("budding_bomb",
            new BuddingBombItem(new FabricItemSettings().maxCount(16)));

    public static final Item AMETHYST_TRIDENT = register("amethyst_trident",
            new AmethystTridentItem(new FabricItemSettings().maxDamage(250)));

    public static final Item PIGSTEEL_SHIELD = register("pigsteel_shield",
            new PigsteelShieldItem(new FabricItemSettings()));

    private static Item register(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(SweetsOSpirits.MOD_ID, name), item);
    }

    public static void registerItems(){
        SweetsOSpirits.LOGGER.info("Registering Mod Items for " + SweetsOSpirits.MOD_ID);
    }
}
