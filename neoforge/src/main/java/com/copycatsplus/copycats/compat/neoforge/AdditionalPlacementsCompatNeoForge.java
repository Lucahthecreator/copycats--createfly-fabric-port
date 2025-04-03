package com.copycatsplus.copycats.compat.neoforge;

import com.copycatsplus.copycats.compat.AdditionalPlacementsCompat;
import com.firemerald.additionalplacements.generation.Registration;

public class AdditionalPlacementsCompatNeoForge {
    public static void register() {
        Registration.addRegistration(new AdditionalPlacementsCompat());
    }
}
