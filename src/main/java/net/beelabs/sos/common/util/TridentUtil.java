package net.beelabs.sos.common.util;

import net.beelabs.sos.common.SweetsOSpirits;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.TridentEntity;

public class TridentUtil {
    public static final TrackedData<Boolean> TRACKED_DATA = DataTracker.registerData(TridentEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
}
