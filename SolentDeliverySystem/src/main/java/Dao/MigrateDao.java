package Dao;

import Utils.Dialogs.Dialogs;
import models.DbModels.Migrate;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;


public class MigrateDao extends CommonDao {
    public MigrateDao(ConnectionSource connection) {
        super(connection);
    }

    public boolean migrated(){
        QueryBuilder<Migrate, Integer> queryBuilder = getQueryBuilder(Migrate.class);
        try {
            queryBuilder.where().eq("id", 1);
            PreparedQuery<Migrate> prepare = queryBuilder.prepare();
            List<Migrate> result = getDao(Migrate.class).query(prepare);
            if(result.size() > 0)
                return result.get(0).isMigrated();
        } catch (SQLException e) {
            Dialogs.exception("Database error", e);
        }
        return false;
    }
}