package Dao;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import models.DbModels.Staff;

import java.sql.SQLException;
import java.util.List;

public class StaffDao extends CommonDao {
    public StaffDao(ConnectionSource connection) {
        super(connection);
    }

    public List<Staff> allButAdmin(){
        QueryBuilder<Staff, Integer> queryBuilder = getQueryBuilder(Staff.class);
        try {
            queryBuilder
                    .where()
                    .eq("is_admin", false);
            PreparedQuery<Staff> prepare = queryBuilder.prepare();
            List<Staff> results = getDao(Staff.class).query(prepare);
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
