package Dao;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import models.DbModels.DeliveryArea;

import java.sql.SQLException;
import java.util.List;

public class DeliveryAreaDao extends ClientDao {
    public DeliveryAreaDao(ConnectionSource connection) {
        super(connection);
    }

    public DeliveryArea wherePostCode(String postCode){
        QueryBuilder<DeliveryArea, Integer> queryBuilder = getQueryBuilder(DeliveryArea.class);
        try {
            queryBuilder
                    .where()
                    .eq("area_post_code", postCode);
            PreparedQuery<DeliveryArea> prepare = queryBuilder.prepare();
            List<DeliveryArea> results = getDao(DeliveryArea.class).query(prepare);
            return results.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
