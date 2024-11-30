package net.beelabs.sos.common.init;

import net.beelabs.sos.common.SweetsOSpirits;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SoSItemGroup {
    public static final ItemGroup PINK_GARNET_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(SweetsOSpirits.MOD_ID, "sweets_o_spirits_group"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.sweets_o_spirits_group"))
                    .icon(() -> new ItemStack(SoSItems.MARSHMALLOW)).entries((displayContext, entries) -> {
                        entries.add(SoSItems.MARSHMALLOW);
                        entries.add(SoSItems.MARSHMALLOW_ON_A_STICK);
                        entries.add(SoSItems.MALLOWPAD_JR);
                        entries.add(SoSItems.BUDDING_BOMB);
                        entries.add(SoSItems.AMETHYST_TRIDENT);
                        entries.add(SoSItems.PIGSTEEL_SHIELD);
                    }).build());


    public static void registerItemGroups(){
        SweetsOSpirits.LOGGER.info("Registering Item Groups for " + SweetsOSpirits.MOD_ID);
    }
}
