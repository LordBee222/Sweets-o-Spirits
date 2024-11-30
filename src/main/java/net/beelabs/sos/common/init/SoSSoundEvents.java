package net.beelabs.sos.common.init;

import net.beelabs.sos.common.SweetsOSpirits;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoSSoundEvents {
    public static final SoundEvent ITEM_AMETHYST_TRIDENT_HIT = register("item_amethyst_trident_hit");
    public static final SoundEvent ITEM_AMETHYST_TRIDENT_THROW = register("item_amethyst_trident_throw");

    public static final SoundEvent MISC_AMETHYST_SHARD_BURST = register("misc_amethyst_shard_burst");


    private static SoundEvent register(String name){
        Identifier identifier = new Identifier(SweetsOSpirits.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void registerSounds(){
        SweetsOSpirits.LOGGER.info("Registering Sounds For " + SweetsOSpirits.MOD_ID);
    }
}
