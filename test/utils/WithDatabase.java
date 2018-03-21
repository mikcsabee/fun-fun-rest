package utils;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import org.junit.Before;
import play.Application;
import play.db.ebean.EbeanConfig;
import play.test.Helpers;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;


public class WithDatabase  extends WithApplication {
    protected EbeanServer ebeanServer;

    @Override
    protected Application provideApplication() {
        Map<String, String > settings = new HashMap<>(Helpers.inMemoryDatabase());
        settings.put("app.pageSize", "3");
        return Helpers.fakeApplication(settings);
    }

    @Before
    public void getDatabase() {
        EbeanConfig ebeanConfig = app.injector().instanceOf(EbeanConfig.class);
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
    }
}
