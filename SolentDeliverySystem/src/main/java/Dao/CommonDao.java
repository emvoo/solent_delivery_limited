package Dao;


import Utils.Dialogs.Dialogs;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import models.DbModels.Account;
import models.DbModels.BaseModel;
import models.DbModels.Client;
import models.DbModels.Staff;

import java.sql.SQLException;
import java.util.List;

/**
 * DAO - Data Access Object
 * Object for accessing Database tables and manipulating data within.
 */
public abstract class CommonDao {

    protected final ConnectionSource connection;

    public CommonDao(ConnectionSource connection) {
        this.connection = connection;
    }

    public <T extends BaseModel, I> Dao<T, I> getDao(Class<T> cls){
        try {
            return DaoManager.createDao(connection, cls);
        } catch (SQLException e) {
            Dialogs.exception("Database error.", e);
        }
        return null;
    }

    public <T extends BaseModel, I> void createOrUpdate(BaseModel baseModel){
        Dao<T, I> dao = getDao((Class<T>) baseModel.getClass());
        try {
            dao.createOrUpdate((T)baseModel);
        } catch (SQLException e) {
            Dialogs.exception("Database error.", e);
        }
    }

    public <T extends BaseModel, I> void refresh(BaseModel baseModel){
        Dao<T, I> dao = getDao((Class<T>) baseModel.getClass());
        try {
            dao.refresh((T)baseModel);
        } catch (SQLException e) {
            Dialogs.exception("Database error.", e);
        }
    }

    public <T extends BaseModel, I> void delete(BaseModel baseModel){
        Dao<T, I> dao = getDao((Class<T>) baseModel.getClass());
        try {
            dao.delete((T)baseModel);
        } catch (SQLException e) {
            Dialogs.exception("Database error.", e);
        }
    }

    public <T extends BaseModel, I> List<T> queryForAll(Class<T> cls){
        Dao<T, I> dao = getDao(cls);
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            Dialogs.exception("Database error.", e);
        }
        return null;
    }

    public <T extends BaseModel, I> QueryBuilder<T, I> getQueryBuilder(Class<T> cls){
        Dao<T, I> dao = getDao(cls);
        return dao.queryBuilder();
    }

    public Account login(String username, String pass){
        QueryBuilder<Account, Integer> queryBuilder = getQueryBuilder(Account.class);
        try {
            queryBuilder
                    .where()
                    .eq("username", username)
                    .and()
                    .eq("password", pass);
            PreparedQuery<Account> prepare = queryBuilder.prepare();
            List<Account> result = getDao(Account.class).query(prepare);
            if(result.size() > 0)
                return result.get(0);
        } catch (SQLException e) {
            Dialogs.exception("Database error.", e);
        }
        return null;
    }

    public Account getLoggedin(){
        QueryBuilder<Account, Integer> queryBuilder = getQueryBuilder(Account.class);
        try {
            queryBuilder
                    .where()
                    .eq("isLoggedIn", true);
            PreparedQuery<Account> prepare = queryBuilder.prepare();
            List<Account> result = getDao(Account.class).query(prepare);
            Account account = result.get(0);
            return account;
        } catch (SQLException e) {
            Dialogs.exception("Database error.", e);
        }
        return null;

    }



}
