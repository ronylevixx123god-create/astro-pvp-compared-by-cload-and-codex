package com.cosmicpvp.cosmicutils.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Supports both the original Banana/TacoSpigot and WineSpigot region-file APIs. */
public final class RegionFileCloser {
    private RegionFileCloser() {}

    public static void close(Object regionFile) throws IOException {
        try {
            Method close;
            try {
                close = regionFile.getClass().getMethod("closeFile");
            } catch (NoSuchMethodException originalFork) {
                close = regionFile.getClass().getMethod("c");
            }
            close.invoke(regionFile);
        } catch (InvocationTargetException failure) {
            if (failure.getCause() instanceof IOException) {
                throw (IOException) failure.getCause();
            }
            throw new IOException("Could not close region file", failure.getCause());
        } catch (ReflectiveOperationException failure) {
            throw new IOException("Unsupported region-file close API", failure);
        }
    }
}
