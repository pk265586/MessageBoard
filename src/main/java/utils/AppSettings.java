package utils;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class AppSettings {
    @Getter @Setter
    private boolean isProduction;

    @Getter @Setter
    private String productionDbName;

    @Getter @Setter
    private String qaDbName;

    @Getter
    private MessageCache messageCache;

    public String getConnectionString(){
        return getConnectionString(isProduction);
    }

    public String getConnectionString(boolean isProduction){
        var dbName = getDbName(isProduction);
        return  "jdbc:sqlite:" + dbName;
    }

    private String getDbName(boolean isProduction){
        return isProduction ? productionDbName : qaDbName;
    }

    private static AppSettings instance = new AppSettings();
    public static AppSettings getInstance(){
        return instance;
    }

    static final String CONFIG_FILE = "app.config";

    public void load(){
        try {
            var configFile = new File(CONFIG_FILE);

            FileReader reader = new FileReader(configFile);
            var properties = new Properties();
            properties.load(reader);

            isProduction = Boolean.parseBoolean((String)properties.get("isProduction"));
            productionDbName = (String) properties.get("productionDbName");
            qaDbName = (String)properties.get("qaDbName");

            boolean useCache = Boolean.parseBoolean((String)properties.get("useCache"));
            if (useCache) {
                messageCache = new MessageCache();
            }
        }
        catch (Exception e){
            System.out.println("Error loading app settings: " + e.getMessage());
        }
    }
}
