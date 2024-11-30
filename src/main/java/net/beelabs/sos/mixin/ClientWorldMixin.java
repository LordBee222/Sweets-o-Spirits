package net.beelabs.sos.mixin;

import net.beelabs.sos.mixin.accessor.ClientWorldAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "addFireworkParticle", at = @At("HEAD"))
    private void addFireworkParticle(double x, double y, double z, double velocityX, double velocityY, double velocityZ, NbtCompound nbt, CallbackInfo ci){
        ClientWorld clientWorld = (ClientWorld) (Object) this;
        ClientWorldAccessor accessor = (ClientWorldAccessor) clientWorld;
       // accessor.getClient().particleManager.addParticle(new EatGappleParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ, accessor.getClient().particleManager.getS));

    }
}
