package net.astropvp.core.utils;

import java.net.URI;
import java.net.URLDecoder;
import java.util.Locale;

/** Extracts video IDs only from actual YouTube hosts, never substring lookalikes. */
public final class YouTubeVideoUrl {
    private YouTubeVideoUrl() {}

    /** Older claims saved the raw ID together with query parameters or a fragment. */
    public static String normalizeStoredId(String stored) {
        if (stored == null) return null;
        String id = stored.trim().split("[&#?]", 2)[0];
        return id.matches("[A-Za-z0-9_-]{11}") ? id : null;
    }

    public static String parse(String input) {
        if (input == null) {
            return null;
        }
        try {
            URI uri = new URI(input.trim());
            String scheme = uri.getScheme();
            if (!("https".equalsIgnoreCase(scheme) || "http".equalsIgnoreCase(scheme))
                    || uri.getHost() == null || uri.getUserInfo() != null
                    || (uri.getPort() != -1 && uri.getPort() != 80 && uri.getPort() != 443)) {
                return null;
            }
            String host = uri.getHost().toLowerCase(Locale.ROOT);
            String id = null;
            if ("youtu.be".equals(host)) {
                id = uri.getPath().substring(1);
            } else if ("youtube.com".equals(host) || "www.youtube.com".equals(host) || "m.youtube.com".equals(host)) {
                if ("/watch".equals(uri.getPath()) && uri.getRawQuery() != null) {
                    for (String part : uri.getRawQuery().split("&")) {
                        String[] pair = part.split("=", 2);
                        if (pair.length == 2 && "v".equals(URLDecoder.decode(pair[0], "UTF-8"))) {
                            // Duplicate IDs are ambiguous and must not identify different rewards.
                            if (id != null) return null;
                            id = URLDecoder.decode(pair[1], "UTF-8");
                        }
                    }
                } else if (uri.getPath().startsWith("/shorts/") || uri.getPath().startsWith("/embed/")) {
                    id = uri.getPath().substring(uri.getPath().startsWith("/shorts/") ? 8 : 7);
                }
            }
            return id != null && id.matches("[A-Za-z0-9_-]{11}") ? id : null;
        } catch (Exception invalidUrl) {
            return null;
        }
    }
}
