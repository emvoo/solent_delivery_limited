package Dao;

import Utils.Dialogs.Dialogs;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import models.DbModels.Account;

import java.sql.SQLException;
import java.util.List;


public class AccountDao extends CommonDao {

    public AccountDao(ConnectionSource connection) {
        super(connection);
    }

    public boolean whereUsername(String username){
        QueryBuilder<Account, Integer> queryBuilder = getQueryBuilder(Account.class);
        queryBuilder.setCountOf(true);
        try {
            queryBuilder
                    .where()
                    .eq("username", username)
                    .countOf();
            PreparedQuery<Account> prepare = queryBuilder.prepare();
            long result = getDao(Account.class).countOf(prepare);
            if(result > 0){
                return true;
            }
        } catch (SQLException e) {
            Dialogs.exception("Database error.", e);
        }
        return false;
    }
}
