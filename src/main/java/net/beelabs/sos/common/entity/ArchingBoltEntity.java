package net.beelabs.sos.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ArchingBoltEntity extends Entity implements Ownable {
    private LivingEntity target;
    private LivingEntity source;
    private Vec3d startPosition;
    private Vec3d endPosition;
    public long seed;
    @Nullable
    private UUID ownerUuid;
    @Nullable
    private Entity owner;


    public ArchingBoltEntity(EntityType<? extends ArchingBoltEntity> entityType, World world) {
        super(entityType, world);
        this.ignoreCameraFrustum = true; // Always render the entity
        this.seed = this.random.nextLong();

    }

    public ArchingBoltEntity(EntityType<? extends ArchingBoltEntity> entityType, World world, Vec3d startPosition, LivingEntity target) {
        this(entityType, world);

        this.startPosition = startPosition;
        this.endPosition = target.getPos();
        this.target = target;
        this.refreshPositionAndAngles(startPosition.x, startPosition.y, startPosition.z, this.getYaw(), this.getPitch());
    }

    public LivingEntity getTarget() {
        return target;
    }

    public LivingEntity getSource() {
        return source;
    }

    public void setSource(LivingEntity source) {
        this.source = source;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            return;
        }

        /*
        if (target == null || !target.isAlive()) {
            this.discard(); // Remove the bolt if the target is invalid
            return;
        }

         */

         if (target != null) {

             // Update the end position to the target's current position
             this.endPosition = target.getPos();

             // Optionally: Deal damage or apply effects when the bolt "strikes"
             if (this.age > 20) { // Example lifespan of 10 ticks
                 target.damage(this.getDamageSources().lightningBolt(), 6.0F);
                 this.discard();
             }
         }
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public void setStartPosition(Vec3d startPosition) {
        this.startPosition = startPosition;
    }

    public void setEndPosition(Vec3d endPosition) {
        this.endPosition = endPosition;
    }

    public Vec3d getStartPosition() {
        return startPosition;
    }

    public Vec3d getEndPosition() {
        return endPosition;
    }

    @Override
    protected void initDataTracker() {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        // Restore saved state
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        // Save state
    }

    @Override
    public @Nullable Entity getOwner() {
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        } else if (this.ownerUuid != null && this.getWorld() instanceof ServerWorld) {
            this.owner = ((ServerWorld)this.getWorld()).getEntity(this.ownerUuid);
            return this.owner;
        } else {
            return null;
        }
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUuid = entity.getUuid();
            this.owner = entity;
        }
    }
}
