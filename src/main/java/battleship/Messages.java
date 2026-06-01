package battleship;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for managing localized messages.
 */
public class Messages {

    private static final Logger LOGGER = LogManager.getLogger(Messages.class);
    private static PropertiesConfiguration config;

    static {
        try {
            load("pt");
        } catch (Exception e) {
            LOGGER.error("Failed to load messages", e);
        }
    }

    private Messages() {
        // Utility class
    }

    /**
     * Loads messages for the specified language.
     *
     * @param lang the language code (e.g., "pt", "en")
     * @throws Exception if an error occurs while loading the configuration
     */
    public static void load(String lang) throws Exception {
        lang = lang.toLowerCase();
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFile(new File("src/main/resources/messages_" + lang + ".properties"))
                                .setEncoding(StandardCharsets.UTF_8.name()));

        config = builder.getConfiguration();
    }

    /**
     * Retrieves a message for the specified key.
     *
     * @param key the message key
     * @return the localized message, or an error string if not found
     */
    public static String get(String key) {
        if (config == null) {
            return "ERR: " + key;
        }
        return config.getString(key, "ERR: " + key);
    }

    /**
     * Retrieves a formatted message for the specified key and arguments.
     *
     * @param key  the message key
     * @param args the formatting arguments
     * @return the formatted localized message
     */
    public static String get(String key, Object... args) {
        String msg = get(key);
        if (msg != null && msg.contains("{")) {
            for (int i = 0; i < args.length; i++) {
                msg = msg.replace("{" + i + "}", args[i].toString());
            }
        }
        return msg;
    }
}
