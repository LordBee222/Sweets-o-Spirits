package net.beelabs.sos.mixin.accessor;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PersistentProjectileEntity.class)
public interface PersistentProjectileEntityAccessor {

    @Accessor("piercedEntities")
    IntOpenHashSet getPiercedEntities();

    @Accessor("piercedEntities")
    void setPiercedEntities(IntOpenHashSet dealtDamage);

}
