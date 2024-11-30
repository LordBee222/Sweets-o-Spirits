package net.beelabs.sos.client.entity.renderer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.UUID;

public class DungeonDoorEntity extends Entity {
    private UUID uuid;
    private boolean isOnCooldown;
    private boolean isDungeonActive;    

    public DungeonDoorEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
