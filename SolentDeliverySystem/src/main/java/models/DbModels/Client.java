package models.DbModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "clients")
public class Client implements BaseModel{

    public Client(){}

    /*
        Table columns
     */
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(defaultValue = "true")
    private Boolean active;

    /*
        Foreign keys
     */
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Account account;

    @ForeignCollectionField
    private ForeignCollection<DeliveryAddress> deliveryAddresses;

    @ForeignCollectionField
    private ForeignCollection<DeliveryService> deliveryServices;

    /*
        Getters
     */
    public int getId() { return id; }
    public Account getAccount() { return account; }
    public ForeignCollection<DeliveryAddress> getDeliveryAddresses() { return deliveryAddresses; }
    public ForeignCollection<DeliveryService> getDeliveryServices() { return deliveryServices; }
    public boolean isActive() { return active; }
    public void changeActive(){
        if(this.active)
            this.active = false;
        else
            this.active = true;
    }

    /*
        Setters
     */
    public void setId(int id) { this.id = id; }
    public void setAccount(Account account) { this.account = account; }
    public void setDeliveryAddresses(ForeignCollection<DeliveryAddress> deliveryAddresses) { this.deliveryAddresses = deliveryAddresses; }
    public void setDeliveryServices(ForeignCollection<DeliveryService> deliveryServices) { this.deliveryServices = deliveryServices; }
    public void setActive(boolean active) { this.active = active; }
    public void deactivate() { this.active = false; }

    /*
        Properties and methods for table view
        in 'View Clients' tab in Staff section
     */
    private String name;
    public String getName() { return account.getName(); }

    private String surname;
    public String getSurname() { return account.getSurname(); }

    private String street;
    public String getStreet() {
        String street = "";
        street += account.getAddress().getHouseNo();
        street += " " + account.getAddress().getStreetName();
        return street;
    }

    private String postcode;
    public String getPostcode() { return account.getAddress().getPostCode(); }

    private String city;
    public String getCity() { return account.getAddress().getTown(); }

    private String activeClient;
    public String getActiveClient() {
        if(this.active){
            return "yes";
        }
        return "no";
    }
}
