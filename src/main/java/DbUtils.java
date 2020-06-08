import com.smattme.MysqlExportService;
import com.smattme.MysqlImportService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtils {

    //required properties for exporting of db
    Properties properties = new Properties();

    public void setUp() {
        properties.setProperty(MysqlExportService.DB_NAME, "mysql");
        properties.setProperty(MysqlExportService.DB_USERNAME, "mysql");
        properties.setProperty(MysqlExportService.DB_PASSWORD, "mysql");
        properties.setProperty(MysqlExportService.JDBC_CONNECTION_STRING, "jdbc:mysql://localhost:3306/mysql?useSSL=false&zeroDateTimeBehavior=CONVERT_TO_NULL");
        properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");
        //set the outputs temp dir
        properties.setProperty(MysqlExportService.TEMP_DIR, new File("src\\main\\resources\\db-dump").getPath());

    }

    public void dbExport() {
        final MysqlExportService mysqlExportService = new MysqlExportService(properties);
        try {
            mysqlExportService.export();
        } catch (final IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean dbImport() {
        try {
            final String sql = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\db-dump\\db_dump.sql")));
            return MysqlImportService.builder()
                    .setJdbcConnString("jdbc:mysql://localhost:3306/mysql?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false")
                    .setDatabase("mysql")
                    .setSqlString(sql)
                    .setUsername("mysql")
                    .setPassword("mysql")
                    .setDeleteExisting(true)
                    .setDropExisting(true)
                    .importDatabase();
        } catch (final IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
