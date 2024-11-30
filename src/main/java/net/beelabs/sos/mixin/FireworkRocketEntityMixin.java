package net.beelabs.sos.mixin;

import net.beelabs.sos.common.util.IRocketJumpVelocityGetter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {
    private static final double radius = 3;
    private static final double verticalVelocity = 1.5;
    private static final double horizontalVelocity = 3.75;

    @Inject(method = "explode", at = @At("HEAD"))
    private void explode(CallbackInfo info) {
        FireworkRocketEntity fireworkEntity = (FireworkRocketEntity) (Object) this;
        if (!fireworkEntity.getWorld().isClient && fireworkEntity.wasShotAtAngle()) {
            List<Entity> entities = fireworkEntity.getWorld().getOtherEntities(fireworkEntity, new Box(fireworkEntity.getX() - radius, fireworkEntity.getY() - radius, fireworkEntity.getZ() - radius, fireworkEntity.getX() + radius, fireworkEntity.getY() + radius, fireworkEntity.getZ() + radius));
            for (Entity entity : entities) {
                Vec3d knockbackDirection = entity.getPos().subtract(fireworkEntity.getPos()).normalize();
                Vec3d addedVelocity = new Vec3d(knockbackDirection.x * horizontalVelocity, verticalVelocity, knockbackDirection.z * horizontalVelocity);
                entity.addVelocity(addedVelocity.x, addedVelocity.y, addedVelocity.z);
                entity.velocityModified = true;
                if (entity instanceof LivingEntity livingEntity){
                    ((IRocketJumpVelocityGetter)livingEntity).setRocketJumping(true);
                }
            }
        }
    }
}
