package Dao;


import Utils.Dialogs.Dialogs;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import models.Company;
import models.DbModels.DeliveryJob;
import models.DbModels.DeliveryService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryJobDao extends CommonDao {
    public DeliveryJobDao(ConnectionSource connection) {
        super(connection);
    }

    public List<DeliveryJob> whereDriver(int driver_id){
        QueryBuilder<DeliveryJob, Integer> queryBuilder = getQueryBuilder(DeliveryJob.class);
        try {
            queryBuilder
                    .where()
                    .eq("staff_id", driver_id);
            PreparedQuery<DeliveryJob> prepare = queryBuilder.prepare();
            List<DeliveryJob> results = getDao(DeliveryJob.class).query(prepare);
            return results;
        } catch (SQLException e) {
            Dialogs.exception("Database error.", e);
        }
        return null;
    }

    public List<DeliveryJob> whereDriverAndToday(int driver_id){
        QueryBuilder<DeliveryJob, Integer> queryBuilder = getQueryBuilder(DeliveryJob.class);
        try {
            queryBuilder
                    .where()
                    .eq("staff_id", driver_id)
                    .and()
                    .eq("assigned_on", Company.getTodaysDate());
            PreparedQuery<DeliveryJob> prepare = queryBuilder.prepare();
            List<DeliveryJob> results = getDao(DeliveryJob.class).query(prepare);
            return results;
        } catch (SQLException e) {
            Dialogs.exception("Database error.", e);
        }
        return null;
    }

    public List<DeliveryJob> whereDriverAndDelivered(int driver_id){
        QueryBuilder<DeliveryJob, Integer> queryBuilder = getQueryBuilder(DeliveryJob.class);
        try {
            queryBuilder
                    .where()
                    .eq("staff_id", driver_id)
                    .and()
                    .eq("completed", true);
            PreparedQuery<DeliveryJob> prepare = queryBuilder.prepare();
            List<DeliveryJob> results = getDao(DeliveryJob.class).query(prepare);
            return results;
        } catch (SQLException e) {
            Dialogs.exception("Database error.", e);
        }
        return null;
    }
}
