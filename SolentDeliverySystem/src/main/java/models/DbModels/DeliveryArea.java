package models.DbModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "delivery_areas")
public class DeliveryArea implements BaseModel {

    public DeliveryArea(){}

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String area_post_code;

    @DatabaseField
    private int area_code;

    /*
        Getters
     */
    public int getId() { return id; }
    public String getAreaPostCode() { return area_post_code; }
    public int getAreaCode() { return area_code; }

    /*
        Setters
     */
    public void setId(int id) { this.id = id; }
    public void setAreaPostCode(String area_post_code) { this.area_post_code = area_post_code; }
    public void setAreaCode(int area_code) { this.area_code = area_code; }

    /*
        Other methods
     */
    @Override
    public String toString(){
        return id + " " + area_post_code + " " + area_code;
    }
}
