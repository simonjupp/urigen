package uk.ac.ebi.fgpt.urigen.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import uk.ac.ebi.fgpt.urigen.impl.GeneratorTypes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Simon Jupp
 * @date 13/12/2011
 * Functional Genomics Group EMBL-EBI
 *
 */
public class HSQLDBInitializer {

    public static final String USER =
            "create table if not exists USER (" +
                    "id INTEGER NOT NULL IDENTITY," +
                    "user_name VARCHAR (100)," +
                    "email VARCHAR (100)," +
                    "restapikey VARCHAR (150)," +
                    "admin BOOLEAN DEFAULT FALSE NOT NULL," +
                    "constraint EMAIL_U UNIQUE (email), " +
                    "constraint APIKEY_U UNIQUE (restapikey), " +
                    "constraint USERNAME UNIQUE (user_name) )";

    public static final String ID_GENERATOR_TYPE =
            "create table if not exists ID_GENERATOR_TYPE (" +
                    "id INTEGER NOT NULL IDENTITY, " +
                    "class_name VARCHAR (50), " +
                    "constraint CLASS_NAME UNIQUE (class_name)" +
                    ")";

    public static final String ONTOLOGY_PREFS =
            "create table if not exists PREFERENCE (" +
                    "id INTEGER NOT NULL IDENTITY, " +
                    "ontology_uri VARCHAR (150), " +
                    "ontology_physical_uri VARCHAR(150), " +
                    "ontology_name VARCHAR (50), " +
                    "base_uri VARCHAR (150), " +
                    "separator VARCHAR (1), " +
                    "prefix VARCHAR (50), " +
                    "suffix VARCHAR (50), " +
                    "digit_count INTEGER, " +
                    "digit_start INTEGER, " +
                    "digit_end INTEGER, " +
                    "last_digit BIGINT, " +
                    "generator_type_id INTEGER, " +
                    "check_source BOOLEAN DEFAULT FALSE NOT NULL, " +
                    "last_update TIME, " +
                    "ontology_md5 VARCHAR (32), " +
                    "status_ok BOOLEAN DEFAULT TRUE NOT NULL, " +
                    "status_message VARCHAR (200), " +
                    "constraint ONTOLOGY_NAME UNIQUE (ontology_name), " +
                    "constraint GEN_TYPE_FK foreign key (generator_type_id) references ID_GENERATOR_TYPE (id) )";

    public static final String GENERATED_URI =
            "create table if not exists URI (" +
                    "id INTEGER NOT NULL IDENTITY, " +
                    "preference_id INTEGER NOT NULL, " +
                    "user_id INTEGER NOT NULL," +
                    "original_uri VARCHAR(200), " +
                    "generated_uri VARCHAR(200) NOT NULL, " +
                    "uri_label VARCHAR(200), " +
                    "uri_comment VARCHAR (255), " +
                    "creation_date TIMESTAMP DEFAULT NOW," +
                    "constraint URI_PREF_FK foreign key (preference_id) references PREFERENCE (id) ON DELETE CASCADE," +
                    "constraint UNIQUE_URI UNIQUE (generated_uri), " +
                    "constraint USER_FK foreign key (user_id) references USER (id) ON DELETE CASCADE )";

    public static final String ONTOLOGY_RANGES =
            "create table if not exists USER_RANGE (" +
                    "id INTEGER NOT NULL IDENTITY, " +
                    "user_id INTEGER NOT NULL," +
                    "preferences_id INTEGER NOT NULL," +
                    "digit_start INTEGER NOT NULL," +
                    "digit_end INTEGER NOT NULL," +
                    "last_digit BIGINT NOT NULL," +
//                    "constraint DIGITSTART CHECK (digit_end > digit_start), " +
                    "constraint PREFS_RANGE_FK foreign key (preferences_id) references PREFERENCE (id) ON DELETE CASCADE," +
                    "constraint USER_RANEG_FK foreign key (user_id) references USER (id) ON DELETE CASCADE )";


    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private Logger log = LoggerFactory.getLogger(getClass());

    protected Logger getLog() {
        return log;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void init() {
        // create tables in the HSQL database if they do not already exist
        try {
            // load driver
            Class.forName(getDriverClassName());

            // get connection and create tables
            getLog().debug("Initializing database...");
            Connection connection = DriverManager.getConnection(getUrl() + ";hsqldb.write_delay=false");

            // create user table
            getLog().debug("Creating tables...");
            connection.prepareStatement(USER).execute();
            connection.prepareStatement(ID_GENERATOR_TYPE).execute();
            connection.prepareStatement(ONTOLOGY_PREFS).execute();
            connection.prepareStatement(GENERATED_URI).execute();
            connection.prepareStatement(ONTOLOGY_RANGES).execute();

            for (String id : GeneratorTypes.ALL_TYPES_ID) {
                getLog().debug("populating generator types table with id : " +id);
                PreparedStatement p  = connection.prepareStatement(
                        "insert into ID_GENERATOR_TYPE (class_name) values(?)");
                p.setString(1, id);
                try {
                    p.execute();
                } catch (Exception e) {
                    getLog().debug("ignoring duplicate class_name:" +e.getLocalizedMessage());
                }

            }

            getLog().debug("...database initialization complete");
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot load HSQL driver", e);
        }
        catch (SQLException e) {
            throw new CannotGetJdbcConnectionException("Unable to connect to " + getUrl(), e);
        }
    }

    public void destroy() {
        // send shutdown signal to HSQL database
        try {
            Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            connection.prepareStatement("SHUTDOWN;").execute();
        }
        catch (SQLException e) {
            throw new CannotGetJdbcConnectionException("Unable to connect to " + getUrl(), e);
        }
    }

}
