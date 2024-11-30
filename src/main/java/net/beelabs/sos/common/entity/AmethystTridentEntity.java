package net.beelabs.sos.common.entity;

import net.beelabs.sos.common.init.SoSEntities;
import net.beelabs.sos.common.init.SoSSoundEvents;
import net.beelabs.sos.common.util.IEntityImpaledShardGetter;
import net.beelabs.sos.mixin.accessor.TridentEntityAccessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import java.util.Optional;

public class AmethystTridentEntity extends TridentEntity {
    public AmethystTridentEntity(EntityType<? extends TridentEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity livingTarget) {
            this.playSound(SoSSoundEvents.ITEM_AMETHYST_TRIDENT_HIT, 1.0F, 1.0F);
            Optional<Integer> impaledShardsCount = Optional.of((((IEntityImpaledShardGetter) livingTarget).getImpaledShardCount()));
            ((IEntityImpaledShardGetter) livingTarget).setImpaledShardCount(impaledShardsCount.orElse(0) + 1);

        }
    }

    public void setTridentAttributes(ItemStack stack) {
        this.setTridentStack(stack.copy());
        this.dataTracker.set(TridentEntityAccessor.sos$getLoyalty(), (byte) EnchantmentHelper.getLoyalty(stack));
        this.dataTracker.set(TridentEntityAccessor.sos$getEnchanted(), stack.hasGlint());
    }

    public void setTridentAttributes(World world, LivingEntity owner, ItemStack stack) {
        this.setTridentStack(stack.copy());
        this.dataTracker.set(TridentEntityAccessor.sos$getLoyalty(), (byte) EnchantmentHelper.getLoyalty(stack));
        this.dataTracker.set(TridentEntityAccessor.sos$getEnchanted(), stack.hasGlint());
    }


    public ItemStack getTridentStack() {
        return ((TridentEntityAccessor) this).sos$getTridentStack();
    }

    public void setTridentStack(ItemStack tridentStack) {
        ((TridentEntityAccessor) this).sos$setTridentStack(tridentStack);
    }

    protected void setDealtDamage(boolean dealtDamage) {
        ((TridentEntityAccessor) this).sos$setDealtDamage(dealtDamage);
    }

    protected boolean hasDealtDamage() {
        return ((TridentEntityAccessor) this).sos$hasDealtDamage();
    }

}
