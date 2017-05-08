package models.DbModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "delivery_types")
public class DeliveryType implements BaseModel{
    public DeliveryType(){}

    /*
        Table columns
     */
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, unique = true)
    private String type;

    @DatabaseField(canBeNull = false)
    private int duration;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseField(canBeNull = false)
    private double base_price;

    /*
        Getters
     */
    public int getId() { return id; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public double getBasePrice() { return base_price; }

    /*
        Setters
     */
    public void setId(int id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setBasePrice(double base_price) { this.base_price = base_price; }
}
