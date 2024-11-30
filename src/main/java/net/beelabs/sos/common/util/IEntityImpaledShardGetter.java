package net.beelabs.sos.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IEntityImpaledShardGetter {
    void setLodgedTridentStack(ItemStack stack);
    ItemStack getLodgedTridentStack();

    void setImpaledShardCount(int count);
    int getImpaledShardCount();

    void setImpaledShardTimer(int timer);
    int getImpaledShardTimer();

    void explodeAmethystCluster();
}
