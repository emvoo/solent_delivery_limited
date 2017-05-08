package Utils;

import Utils.Dialogs.Dialogs;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import models.DbModels.*;

import java.io.IOException;
import java.sql.SQLException;

/*
    db driver, create tables, delete tables and so on
 */
public class DbManager {


    private static final String JDBC_DRIVER_SQLITE = "jdbc:sqlite:database.db";
    private static ConnectionSource connection;

    public static void initDatabase(){
        createConnection();
        createTables();
        closeConnection();
    }

    private static void createConnection(){
        try {
            /**
             * create single connection
             * normally pooled connection should be created but for this scenario single is fine
             * no username/password is needed to connect to sqlite database
             */
            connection = new JdbcConnectionSource(JDBC_DRIVER_SQLITE);
        } catch (SQLException|IllegalArgumentException e) {
            Dialogs.exception("Connection to database failed.", e);
        }
    }


    public static ConnectionSource getConnection(){
        if(connection == null){
            createConnection();
        }
        return connection;
    }

    public static void closeConnection(){
        if(connection != null){
            try {
                connection.close();
            } catch (IOException e) {
                Dialogs.exception("Unexpected has just happened. You may contact system creators about this error.", e);
            }
        }
    }

    private static void createTables(){
        try {
            TableUtils.createTableIfNotExists(connection, AccountType.class);
            TableUtils.createTableIfNotExists(connection, Account.class);
            TableUtils.createTableIfNotExists(connection, Address.class);
            TableUtils.createTableIfNotExists(connection, Client.class);
            TableUtils.createTableIfNotExists(connection, Staff.class);
            TableUtils.createTableIfNotExists(connection, DeliveryService.class);
            TableUtils.createTableIfNotExists(connection, DeliveryJob.class);
            TableUtils.createTableIfNotExists(connection, DeliveryType.class);
            TableUtils.createTableIfNotExists(connection, DeliveryArea.class);
            TableUtils.createTableIfNotExists(connection, DeliveryAddress.class);
            TableUtils.createTableIfNotExists(connection, Migrate.class);

        } catch (SQLException e) {
            Dialogs.exception("Error occurred during creating database tables.", e);
        }
    }

    private static void dropTables(){
        try {
            TableUtils.dropTable(connection, Account.class, true);
            TableUtils.dropTable(connection, Address.class, true);
            TableUtils.dropTable(connection, Client.class, true);
            TableUtils.dropTable(connection, DeliveryJob.class, true);
            TableUtils.dropTable(connection, Staff.class, true);
            TableUtils.dropTable(connection, DeliveryService.class, true);
            TableUtils.dropTable(connection, DeliveryArea.class, true);
            TableUtils.dropTable(connection, DeliveryAddress.class, true);
            TableUtils.dropTable(connection, DeliveryType.class, true);
            TableUtils.dropTable(connection, Migrate.class, true);
            TableUtils.dropTable(connection, AccountType.class, true);
        } catch (SQLException e) {
            Dialogs.exception("Error occurred during dropping database tables.", e);
        }
    }
}
