package net.beelabs.sos.common.util;

import net.minecraft.item.ItemStack;

public interface IEntityBashingGetter {
    void setBashing(Boolean value);
    boolean isBashing();

    void setPostBashTicks(int value);
    int getPostBashTicks();
    void decrementPostBashTicks();
}
