package models.DbModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*
    The purpose of this database table
    is purely to store details of the fact
    if database has been populated with data
 */

@DatabaseTable(tableName = "migrate")
public class Migrate implements BaseModel{


    public Migrate(){}

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(defaultValue = "false")
    private Boolean migrated;

    /*
        Getters
     */
    public boolean isMigrated() { return migrated; }
    public int getId() { return id; }

    /*
            Setters
     */
    public void setMigrated(boolean migrated) { this.migrated = migrated; }
    public void setId(int id) { this.id = id; }

}
