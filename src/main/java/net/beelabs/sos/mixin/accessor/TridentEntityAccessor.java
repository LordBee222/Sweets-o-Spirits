package net.beelabs.sos.mixin.accessor;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TridentEntity.class)
public interface TridentEntityAccessor {

    @Accessor("dealtDamage")
    boolean getDealtDamage();

    @Accessor("dealtDamage")
    void setDealtDamage(boolean dealtDamage);

    @Accessor("LOYALTY")
    static TrackedData<Byte> sos$getLoyalty() {
        return null;
    }

    @Accessor("ENCHANTED")
    static TrackedData<Boolean> sos$getEnchanted() {
        return null;
    }

    @Accessor("tridentStack")
    ItemStack sos$getTridentStack();

    @Accessor("tridentStack")
    void sos$setTridentStack(ItemStack stack);

    @Accessor("dealtDamage")
    boolean sos$hasDealtDamage();

    @Accessor("dealtDamage")
    void sos$setDealtDamage(boolean dealtDamage);
}
