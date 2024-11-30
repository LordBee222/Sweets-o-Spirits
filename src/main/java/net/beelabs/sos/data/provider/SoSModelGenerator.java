package net.beelabs.sos.data.provider;

import net.beelabs.sos.common.init.SoSItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class SoSModelGenerator extends FabricModelProvider {
    public SoSModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(SoSItems.MARSHMALLOW, Models.GENERATED);
        itemModelGenerator.register(SoSItems.MALLOWPAD_JR, Models.GENERATED);
        itemModelGenerator.register(SoSItems.BUDDING_BOMB, Models.GENERATED);
        itemModelGenerator.register(SoSItems.AMETHYST_TRIDENT, Models.GENERATED);
        itemModelGenerator.register(SoSItems.MARSHMALLOW_ON_A_STICK, Models.HANDHELD);
    }
}
