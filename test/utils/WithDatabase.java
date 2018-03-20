package utils;

import com.google.common.collect.ImmutableMap;
import org.junit.After;
import org.junit.Before;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;


public class WithDatabase {
    protected Database database;

    @Before
    public void createDatabase() {
        database = Databases.inMemory(
                "testDB",
                ImmutableMap.of("MODE", "MYSQL"),
                ImmutableMap.of("logStatements", true)
        );
        Evolutions.applyEvolutions(database);
    }

    @After
    public void shutdownDatabase() {
        database.shutdown();
    }
}
