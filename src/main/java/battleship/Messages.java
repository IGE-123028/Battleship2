package battleship;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;

public class Messages {

    private static PropertiesConfiguration config;

    public static void load(String lang) throws Exception {

        Configurations configs = new Configurations();

        config = configs.properties(
                new File("src/main/resources/messages_" + lang + ".properties")
        );
    }

    public static String get(String key) {
        return config.getString(key);
    }
}