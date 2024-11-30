package net.beelabs.sos.common.networking;

import io.netty.buffer.Unpooled;
import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.init.SoSStatusEffects;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class UnleashedDashPacket {
    public static final Identifier ID = new Identifier(SweetsOSpirits.MOD_ID, "unleashed_dash");

    public static void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                if (player != null) {
                    preformDash(player);
                }
            });
        });
    }

    public static void preformDash(PlayerEntity user) {
        Vec3d velocity = user.getRotationVec(1.0F).normalize();
        user.setVelocity(velocity.getX() , velocity.getY(), velocity.getZ());
        user.velocityModified = true;
        user.fallDistance = 0;
        if (user.getWorld() instanceof ServerWorld server) {
            server.spawnParticles(ParticleTypes.EXPLOSION, user.getX(), user.getY(), user.getZ(), 1, 0, 0, 0, 0);
            server.playSound(null, user.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.NEUTRAL, 7f, 1f);
            server.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 0.1f, 1f);
            for (int i = 0; i < 8; i++) {
                server.spawnParticles(ParticleTypes.SCULK_SOUL, user.getParticleX(1), user.getRandomBodyY(), user.getParticleZ(1), 1, 0, 0, 0, 0);
            }
        }
    }

    public static void send() {
        ClientPlayNetworking.send(ID, new PacketByteBuf(Unpooled.buffer()));
    }
}
