package org.arkhamnetwork.Arkkit;

import java.util.HashMap;

public class CustomPlayerVaults
        extends HashMap<Integer, CustomPV> {
    public boolean isCustomPV(int i) {
        return this.containsKey(i);
    }

    public void storePV(int pvNumber, CustomPV customPV) {
        this.put(pvNumber, customPV);
    }
}

