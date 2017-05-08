package models.DbModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "addresses")
public class Address implements BaseModel {

    public Address(){}

    /*
        Database fields
     */
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private int house_no;

    @DatabaseField(canBeNull = false)
    private String street_name;

    @DatabaseField(canBeNull = false)
    private String post_code;

    @DatabaseField(canBeNull = false)
    private String town;

    /*
        Getters
     */
    public int getId() { return id; }
    public int getHouseNo() { return house_no; }
    public String getStreetName() { return street_name; }
    public String getPostCode() { return post_code; }
    public String getTown() { return town; }

    /*
        Setters
     */
    public void setId(int id) { this.id = id; }
    public void setHouseNo(int house_no) { this.house_no = house_no; }
    public void setStreetName(String street_name) { this.street_name = street_name; }
    public void setPostCode(String post_code) { this.post_code = post_code; }
    public void setTown(String town) { this.town = town; }

    /*
        Other methods
     */
    @Override
    public String toString(){
        return id + ", " +
                house_no + ", " +
                street_name + ", " +
                post_code + ", " +
                town;
    }

}
