package net.beelabs.sos.mixin;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.util.TridentUtil;
import net.beelabs.sos.mixin.accessor.PersistentProjectileEntityAccessor;
import net.beelabs.sos.mixin.accessor.TridentEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin {

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void MoveNormal(TridentEntity instance, Vec3d vec3d) {
        TridentEntityAccessor accessor = (TridentEntityAccessor) instance;
        if (instance.getPierceLevel() <= 0) instance.setVelocity(vec3d);
        if (instance.getPierceLevel() > 0) accessor.setDealtDamage(false);
    }

    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void injectPierce(EntityHitResult entityHitResult, CallbackInfo ci) {
        TridentEntity trident = (TridentEntity) (Object) this;
        TridentEntityAccessor accessor = (TridentEntityAccessor) trident;
        PersistentProjectileEntityAccessor accessor2 = (PersistentProjectileEntityAccessor) trident;
        Entity entity = entityHitResult.getEntity();

        if (accessor2.getPiercedEntities() != null && !accessor2.getPiercedEntities().isEmpty()) {
            if (accessor2.getPiercedEntities().contains(entity.getId())) {
                ci.cancel();
            }
        }

        if (trident.getPierceLevel() > 0) {
            if (accessor2.getPiercedEntities() == null) accessor2.setPiercedEntities(new IntOpenHashSet(trident.getPierceLevel()));

            if (accessor2.getPiercedEntities().size() >= trident.getPierceLevel() + 1) {
                accessor.setDealtDamage(true);
                ci.cancel();
            }
            if (accessor2.getPiercedEntities().contains(entity.getId())) ci.cancel();
            accessor2.getPiercedEntities().add(entity.getId());
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initTracker(CallbackInfo ci){
        TridentEntity trident = (TridentEntity) (Object) this;
        trident.getDataTracker().startTracking(TridentUtil.TRACKED_DATA, true);
    }
}
