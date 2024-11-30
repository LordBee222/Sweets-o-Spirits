package net.beelabs.sos.common.init;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SoSEntities {
    public static EntityType<SoulmallowEntity> SOULMALLOW = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(SweetsOSpirits.MOD_ID, "soulmallow"),
            FabricEntityTypeBuilder.<SoulmallowEntity>create(SpawnGroup.MISC, SoulmallowEntity::new)
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static EntityType<WhispEntity> WHISP = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(SweetsOSpirits.MOD_ID, "whisp"),
            FabricEntityTypeBuilder.<WhispEntity>create(SpawnGroup.MISC, WhispEntity::new)
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static EntityType<MallowpadJrEntity> MALLOWPAD_JR = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(SweetsOSpirits.MOD_ID, "mallowpad_jr"),
            FabricEntityTypeBuilder.<MallowpadJrEntity>create(SpawnGroup.MISC, MallowpadJrEntity::new)
                    .dimensions(EntityDimensions.fixed(0.875f, 0.25f)).build());

    public static EntityType<MallowpadJrProjectileEntity> MALLOWPAD_JR_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(SweetsOSpirits.MOD_ID, "mallowpad_jr_projectile"),
            FabricEntityTypeBuilder.<MallowpadJrProjectileEntity>create(SpawnGroup.MISC, MallowpadJrProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

    public static EntityType<AmethystShardEntity> AMETHYST_SHARD = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(SweetsOSpirits.MOD_ID, "amethyst_shard"),
            FabricEntityTypeBuilder.<AmethystShardEntity>create(SpawnGroup.MISC, AmethystShardEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

    public static EntityType<BuddingBombEntity> BUDDING_BOMB = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(SweetsOSpirits.MOD_ID, "budding_bomb"),
            FabricEntityTypeBuilder.<BuddingBombEntity>create(SpawnGroup.MISC, BuddingBombEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

    public static EntityType<AmethystTridentEntity> AMETHYST_TRIDENT = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(SweetsOSpirits.MOD_ID, "amethyst_trident"),
            FabricEntityTypeBuilder.<AmethystTridentEntity>create(SpawnGroup.MISC, AmethystTridentEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

    public static EntityType<ArchingBoltEntity> ARCHING_BOLT = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(SweetsOSpirits.MOD_ID, "arching_bolt"),
            FabricEntityTypeBuilder.<ArchingBoltEntity>create(SpawnGroup.MISC, ArchingBoltEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

    public static void registerModEntities() {
        SweetsOSpirits.LOGGER.info("Registering Mod Entities for " + SweetsOSpirits.MOD_ID);

    }

}
