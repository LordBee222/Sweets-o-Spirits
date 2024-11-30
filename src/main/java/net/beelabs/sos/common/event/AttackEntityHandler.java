package net.beelabs.sos.common.event;

import net.beelabs.sos.common.SweetsOSpirits;
import net.beelabs.sos.common.entity.ArchingBoltEntity;
import net.beelabs.sos.common.init.SoSEntities;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class  AttackEntityHandler implements AttackEntityCallback {
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (!world.isClient() && !player.isSpectator() && entity instanceof LivingEntity) {
            ArchingBoltEntity arc = SoSEntities.ARCHING_BOLT.create(world);
            arc.setPosition(player.getEyePos());
            arc.setStartPosition(player.getEyePos());
            arc.setTarget((LivingEntity) entity);
            world.spawnEntity(arc);
        }
        return ActionResult.PASS;
    }
}
