package models.DbModels;

import Utils.Dialogs.Dialogs;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import models.Company;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@DatabaseTable(tableName = "delivery_services")
public class DeliveryService implements BaseModel{
    public DeliveryService(){}

    /*
        Table columns
     */
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private int length;

    @DatabaseField(canBeNull = false)
    private int width;

    @DatabaseField(canBeNull = false)
    private int depth;

    @DatabaseField(canBeNull = false)
    private int weight;

    @DatabaseField(canBeNull = false, dataType = DataType.LONG_STRING)
    private String contents;

    @DatabaseField(canBeNull = false)
    private double price;

    @DatabaseField(canBeNull = false)
    private String date_purchased;

    @DatabaseField(canBeNull = true)
    private String date_delivered;

    @DatabaseField
    private String predicted_delivery_date;

    @DatabaseField(canBeNull = false, defaultValue = "false")
    private Boolean delivered;

    @DatabaseField(defaultValue = "false")
    private Boolean cancelled;

    @DatabaseField(defaultValue = "false")
    private Boolean assigned;

    @DatabaseField(defaultValue = "false")
    private Boolean paid;

    /*
        Foreign keys
     */
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Client seller;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private DeliveryType delivery_type;

    @DatabaseField(columnName = "delivery_address_id", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private DeliveryAddress deliveryAddress;

    @ForeignCollectionField
    private ForeignCollection<DeliveryJob> delivery_jobs;

    /*
        Getters
     */
    public int getId() { return id; }
    public int getLength() { return length; }
    public int getWidth() { return width; }
    public int getDepth() { return depth; }
    public int getWeight() { return weight; }
    public String getContents() { return contents; }
    public double getPrice() { return price; }
    public String getDatePurchased() {
        return date_purchased;
    }
    public String getDateDelivered() {
        return date_delivered;
    }
    public boolean isDelivered() { return delivered; }
    public Client getSeller() { return seller; }
    public DeliveryType getDeliveryType() { return delivery_type; }
    public DeliveryAddress getDeliveryAddress() { return deliveryAddress; }
    public String getPredictedDeliveryDate() { return predicted_delivery_date; }
    public Boolean isCancelled() { return cancelled; }
    public ForeignCollection<DeliveryJob> getDeliveryJobs() { return delivery_jobs; }
    public Boolean isAssigned() { return assigned; }
    public Boolean isPaid(){ return  this.paid; }

    /*
        Setters
     */
    public void setId(int id) { this.id = id; }
    public void setLength(int length) { this.length = length; }
    public void setWidth(int width) { this.width = width; }
    public void setDepth(int depth) { this.depth = depth; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setContents(String contents) { this.contents = contents; }
    public void setPrice(double price) { this.price = price; }
    public void setDatePurchased() { this.date_purchased = Company.getTodaysDate(); }
    public void setDelivered() {
        this.delivered = true;
        setDateDelivered();
    }
    public void setDateDelivered() { this.date_delivered = Company.getTodaysDate(); }
    public void setSeller(Client seller) { this.seller = seller; }
    public void setDeliveryType(DeliveryType delivery_type) { this.delivery_type = delivery_type; }
    public void setDeliveryAddress(DeliveryAddress address) { this.deliveryAddress = address; }
    public void setPredictedDeliveryDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int duration = getDeliveryType().getDuration();
        String purchase_date = getDatePurchased();
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(purchase_date));
        } catch (ParseException e) {
            Dialogs.exception("Converting to date failed.", e);
        }
        c.add(Calendar.DATE, duration);
        this.predicted_delivery_date = sdf.format(c.getTime());
    }
    public void setCancelled(Boolean cancelled) { this.cancelled = cancelled; }
    public void setDeliveryJobs(ForeignCollection<DeliveryJob> delivery_jobs) { this.delivery_jobs = delivery_jobs; }
    public void setAssigned(Boolean assigned) { this.assigned = assigned; }
    public void setPaid(Boolean paid) { this.paid = true; }

    /*
        Helper attributes and methods
        only used in particular table view
        (clients deliveries view)
     */
    private String receiverNameSurname;
    public String getReceiverNameSurname() {
        String name = getDeliveryAddress().getReceiverName();
        String surname = getDeliveryAddress().getReceiverSurname();
        receiverNameSurname = name + " " + surname;
        return receiverNameSurname;
    }

    private String deliveryTypes;
    public String getDeliveryTypes(){
        deliveryTypes = getDeliveryType().getType();
        return deliveryTypes;
    }

    private String status;
    public String getStatus(){
        if(this.cancelled)
            status = "yes";
        else
            status = "no";
        return status;
    }

    private String hasBeenAssigned;
    public String getHasBeenAssigned(){
        if(assigned)
            return "yes";
        else
            return "no";
    }

    private String hasBennDelivered;
    public String getHasBeenDelivered(){
        if(delivered)
            return "yes";
        else
            return "no";
    }
}
