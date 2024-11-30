package net.beelabs.sos.mixin;

import net.beelabs.sos.common.SweetsOSpirits;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.TridentItem;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Redirect(
            method = "canStartSprinting",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
            )
    )
    private boolean allowSprintingWhileUsingTrident(ClientPlayerEntity instance) {
        // Allow sprinting if using a trident
        if (instance.getActiveItem().getItem() instanceof TridentItem) {
            return false; // Pretend the player is not "using" an item
        }
        return instance.isUsingItem(); // Fall back to default behavior
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean MoveNormal(ClientPlayerEntity instance) {
        // Allow sprinting if using a trident
        if (instance.getActiveItem().getItem() instanceof TridentItem) {
            return false; // Pretend the player is not "using" an item
        }
        return instance.isUsingItem(); // Fall back to default behavior
    }
}
