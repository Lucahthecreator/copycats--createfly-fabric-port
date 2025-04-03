package com.copycatsplus.copycats.utility.neoforge;

import com.copycatsplus.copycats.utility.Platform;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

public class PlatformEnvironmentImpl {

    public static Platform.Environment getCurrent() {
        return FMLEnvironment.dist == Dist.CLIENT ? Platform.Environment.CLIENT : Platform.Environment.SERVER;
    }
}
