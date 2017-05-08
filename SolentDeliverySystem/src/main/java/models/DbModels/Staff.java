package models.DbModels;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "staffs")
public class Staff implements BaseModel{
    public Staff(){}

    /*
        Table columns
     */
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private boolean is_admin;

    @DatabaseField(defaultValue = "false")
    private Boolean confirmed;

    @DatabaseField(defaultValue = "10")
    private Integer delivery_limit;

    /*
        Foreign keys
     */
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Account account;

    @ForeignCollectionField
    private ForeignCollection<DeliveryJob> delivery_jobs;

    /*
        Getters
     */
    public int getId() { return id; }
    public boolean isAdmin() { return is_admin; }
    public Account getAccount() { return account; }
    public boolean isConfirmed() { return confirmed; }
    public ForeignCollection<DeliveryJob> getDeliveryJobs() { return delivery_jobs; }
    public int getDeliveryLimit(){ return this.delivery_limit; }
    /*
        Setters
     */
    public void setId(int id) { this.id = id; }
    public void setAdmin(boolean is_admin) { this.is_admin = is_admin; }
    public void setAccount(Account account) { this.account = account; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }
    public void setDeliveryJobs(ForeignCollection<DeliveryJob> delivery_jobs) { this.delivery_jobs = delivery_jobs; }
    public void setDeliveryLimit(int limit){ this.delivery_limit = limit; }

    /*
        Other methods
     */
    @Override
    public String toString(){
        return getId() + ", " +
                this.getAccount().getName() + " " +
                this.getAccount().getSurname();
    }

    /*
        Properties and methods for staff TableView
        in 'View Staff' tab in Staff Panel
     */
    private String username;
    public String getUsername(){ return account.getUsername(); }

    private String name;
    public String getName() { return account.getName(); }

    private String surname;
    public String getSurname() { return account.getSurname(); }

    private String streetName;
    public String getStreetName(){
        String street = "";
        street += account.getAddress().getHouseNo();
        street += " " + account.getAddress().getStreetName();
        return street;
    }

    private String postcode;
    public String getPostcode(){ return account.getAddress().getPostCode(); }

    private String city;
    public String getCity() { return account.getAddress().getTown(); }

    private String active;
    public String getActive() {
        if(isConfirmed()){
            return "yes";
        }
        return "no";
    }
}
