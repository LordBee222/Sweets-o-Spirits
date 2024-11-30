package net.beelabs.sos.common.init;

import net.beelabs.sos.common.SweetsOSpirits;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SoSDamageTypes {
        //public static final RegistryKey<DamageType> REDIRECT_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Blast.id("amethyst_shard"));
       // public static final RegistryKey<DamageType> DEATH_BARTER = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Blast.id("amethyst_shard"));

   /* public static DamageSource icicle(PersistentProjectileEntity projectile, @Nullable Entity attacker) {
        return create(ICICLE, projectile.getWorld(), attacker);
    }

    */

    private static Item register(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(SweetsOSpirits.MOD_ID, name), item);
    }

    private static DamageSource create(RegistryKey<DamageType> key, World world, @Nullable Entity attacker) {
        return world.getRegistryManager()
                .get(RegistryKeys.DAMAGE_TYPE)
                .getEntry(key)
                .map((type) -> new DamageSource(type, null, attacker))
                .orElse(world.getDamageSources().genericKill()); // Fallback, should never reach this
    }
}
