package utils;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * Singleton class storing application settings
 */
public class AppSettings {
    @Getter @Setter
    private boolean isProduction;

    @Getter @Setter
    private String productionDbName;

    @Getter @Setter
    private String qaDbName;

    /**
     * Gets DB connection string depending on current environment - QA or Production
     */
    public String getConnectionString() {
        return getConnectionString(isProduction);
    }

    /**
     * Gets DB connection string depending environment type passed as an argument QA or Production
     * @param isProduction Set true for Production; false for QA
     */
    public String getConnectionString(boolean isProduction) {
        var dbName = getDbName(isProduction);
        return "jdbc:sqlite:" + dbName;
    }

    private String getDbName(boolean isProduction) {
        return isProduction ? productionDbName : qaDbName;
    }

    private static final AppSettings instance = new AppSettings();

    public static AppSettings getInstance() {
        return instance;
    }

    static final String CONFIG_FILE = "app.config";

    /**
     * Loads current instance from the configuration file
     */
    public void load() {
        try {
            var configFile = new File(CONFIG_FILE);

            FileReader reader = new FileReader(configFile);
            var properties = new Properties();
            properties.load(reader);

            isProduction = Boolean.parseBoolean((String) properties.get("isProduction"));
            productionDbName = (String) properties.get("productionDbName");
            qaDbName = (String) properties.get("qaDbName");
        } catch (Exception e) {
            System.out.println("Error loading app settings: " + e.getMessage());
        }
    }
}
