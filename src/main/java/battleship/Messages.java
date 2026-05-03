package battleship;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Messages {

    // Lazy cache of loaded property sets (language -> properties)
    private static final java.util.Map<String, java.util.Properties> cache = new java.util.concurrent.ConcurrentHashMap<>();
    private static final String DEFAULT_LANG = "pt";
    // current language used by get(key)
    private static volatile String currentLang = DEFAULT_LANG;


    public static void load(String lang) throws Exception {
        if (lang == null) throw new IllegalArgumentException("lang is null");
        final String l = lang.toLowerCase();
        cache.computeIfAbsent(l, k -> {
            java.util.Properties props = new java.util.Properties();
            String resource = "messages_" + k + ".properties";
            try (InputStream in = Messages.class.getClassLoader().getResourceAsStream(resource)) {
                if (in == null) throw new RuntimeException("Resource not found: " + resource);
                try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                    props.load(reader);
                }
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return props;
        });
        // Update the current language so subsequent get(key) calls use it
        currentLang = l;
    }

    public static String get(String key) {
        if (key == null) return null;
        try {
            if (!cache.containsKey(currentLang)) load(currentLang);
        } catch (Exception e) {
            throw new RuntimeException("Failed loading messages for " + currentLang, e);
        }
        java.util.Properties p = cache.get(currentLang);
        return p == null ? null : p.getProperty(key);
    }

    public static String get(String key, String lang) {
        if (key == null) return null;
        String l = lang == null ? DEFAULT_LANG : lang;
        try {
            load(l);
        } catch (Exception e) {
            throw new RuntimeException("Failed loading messages for " + l, e);
        }
        java.util.Properties p = cache.get(l);
        return p == null ? null : p.getProperty(key);
    }
}