package testUtils;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import utils.AppSettings;

public class BeforeAllTests implements BeforeAllCallback, AfterAllCallback {
    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        System.out.println("Run tests started - database cleanup...");

        initAppSettings();
        cleanupDatabase();
    }

    private void initAppSettings() {
        var settings = AppSettings.getInstance();
        settings.load();
    }

    private void cleanupDatabase() {
        new DbCleanupHelper().cleanupDatabase();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        cleanupDatabase();
    }
}
