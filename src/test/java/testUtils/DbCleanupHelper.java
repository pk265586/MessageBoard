package testUtils;

import dataLayer.SqlHelper;
import utils.AppSettings;

public class DbCleanupHelper {
    public void cleanupDatabase() {
        // make sure we have non-production connection
        var connectionString = AppSettings.getInstance().getConnectionString(false);

        var helper = new SqlHelper(connectionString);
        helper.execSql("Delete From UserVote");
        helper.execSql("Delete From Message");
        helper.execSql("Delete From User");
    }
}
