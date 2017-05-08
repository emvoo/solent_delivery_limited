package models.DbModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "delivery_addresses")
public class DeliveryAddress implements BaseModel{
    public DeliveryAddress(){}
    /*
        Table columns
     */
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String receiver_name;

    @DatabaseField
    private String receiver_surname;

    @DatabaseField
    private String email;

    /*
        Foreign keys
     */
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Address address;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private Client client;



    /*
        Getters
     */
    public int getId() { return id; }
    public String getReceiverName() { return receiver_name; }
    public String getReceiverSurname() { return receiver_surname; }
    public Address getAddress() { return address; }
    public Client getClient() { return client; }
    public String getEmail(){ return this.email; }

    /*
        Setters
     */

    public void setId(int id) { this.id = id; }
    public void setReceiverName(String receiver_name) { this.receiver_name = receiver_name; }
    public void setReceiverSurname(String receiver_surname) { this.receiver_surname = receiver_surname; }
    public void setAddress(Address address) { this.address = address; }
    public void setClient(Client client) { this.client = client; }
    public void setEmail(String email){ this.email = email; }

    /*
        Other methods
     */
    @Override
    public String toString(){
        return getReceiverName() + " " +
                getReceiverSurname() + " " +
                getAddress().getHouseNo() + " " +
                getAddress().getStreetName() + " " +
                getAddress().getPostCode() + " " +
                getAddress().getTown();
    }
}
