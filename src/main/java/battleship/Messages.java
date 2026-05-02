package battleship;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class Messages {

    private static PropertiesConfiguration config;

    static {
        try {
            load("pt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load(String lang) throws Exception {
        lang = lang.toLowerCase();

        loadConfiguration(lang);
    }

    private static void loadConfiguration(String lang) throws ConfigurationException {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFile(new File("src/main/resources/messages_" + lang + ".properties"))
                                .setEncoding(StandardCharsets.UTF_8.name()));

        config = builder.getConfiguration();
    }

    public static String get(String key) {
        return config.getString(key);
    }
}