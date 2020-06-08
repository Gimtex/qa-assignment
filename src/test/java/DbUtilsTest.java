import org.junit.Test;

public class DbUtilsTest {

    final DbUtils dbUtils = new DbUtils();

    @Test
    public void dbExport() {
        dbUtils.setUp();
        dbUtils.dbExport();
    }
}
