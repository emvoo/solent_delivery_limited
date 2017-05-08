package Dao;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import models.Company;
import models.DbModels.DeliveryService;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DeliveryServiceDao extends CommonDao {
    public DeliveryServiceDao(ConnectionSource connection) {
        super(connection);
    }

    public List<DeliveryService> whereSeller(int seller_id){
        QueryBuilder<DeliveryService, Integer> queryBuilder = getQueryBuilder(DeliveryService.class);
        try {
            queryBuilder
                    .where()
                    .eq("seller_id", seller_id);
            PreparedQuery<DeliveryService> prepare = queryBuilder.prepare();
            List<DeliveryService> results = getDao(DeliveryService.class).query(prepare);
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DeliveryService> whereToday(){
        QueryBuilder<DeliveryService, Integer> queryBuilder = getQueryBuilder(DeliveryService.class);
        try {
            queryBuilder
                    .where()
                    .eq("date_purchased", Company.getTodaysDate());
            PreparedQuery<DeliveryService> prepare = queryBuilder.prepare();
            List<DeliveryService> results = getDao(DeliveryService.class).query(prepare);
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DeliveryService> whereDeliveredStatus(boolean value){
        QueryBuilder<DeliveryService, Integer> queryBuilder = getQueryBuilder(DeliveryService.class);
        try {
            queryBuilder
                    .where()
                    .eq("delivered", value);
            PreparedQuery<DeliveryService> prepare = queryBuilder.prepare();
            List<DeliveryService> results = getDao(DeliveryService.class).query(prepare);
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DeliveryService> whereCancelled(){
        QueryBuilder<DeliveryService, Integer> queryBuilder = getQueryBuilder(DeliveryService.class);
        try {
            queryBuilder
                    .where()
                    .eq("cancelled", true);
            PreparedQuery<DeliveryService> prepare = queryBuilder.prepare();
            List<DeliveryService> results = getDao(DeliveryService.class).query(prepare);
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
