package net.beelabs.sos.mixin;

import net.beelabs.sos.common.SweetsOSpirits;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.HashMap;
import java.util.Map;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin {

    @ModifyConstant(method = "onStoppedUsing", constant = @Constant(intValue = 10))
    private int modifyRequiredTicks(int original) {
        return 0;
    }

    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
    private void MoveNormal(TridentEntity instance, Entity entity, float pitch, float yaw, float roll, float speed, float divergence) {
        instance.setVelocity(entity, pitch, yaw, roll, speed, divergence);
        instance.setPierceLevel((byte) 4);
    }
}
