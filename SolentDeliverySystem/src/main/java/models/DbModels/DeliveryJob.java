package models.DbModels;

import Dao.AccountDao;
import Dao.AddressDao;
import Utils.DbManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "delivery_jobs")
public class DeliveryJob implements BaseModel {
    public DeliveryJob(){}

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private DeliveryService delivery_service;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private Staff staff;

    @DatabaseField(defaultValue = "false")
    private Boolean completed;

    @DatabaseField
    private String assigned_on;

    /*
        Foreign fields
     */
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private DeliveryArea delivery_area;

    /*
        Getters
     */
    public int getId() { return id; }
    public DeliveryService getDeliveryService() { return delivery_service; }
    public Staff getStaff() { return staff; }
    public DeliveryArea getDeliveryArea() { return delivery_area; }
    public Boolean isCompleted() { return completed; }
    public String getAssignedOn() { return assigned_on; }

    /*
        Setters
     */
    public void setId(int id) { this.id = id; }
    public void setDeliveryService(DeliveryService delivery_service) { this.delivery_service = delivery_service; }
    public void setStaff(Staff staff) { this.staff = staff; }
    public void setDeliveryArea(DeliveryArea delivery_area) { this.delivery_area = delivery_area; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
    public void setAssignedOn(String assigned_on) { this.assigned_on = assigned_on; }

    /**
     *  Methods and properties to be used for TableView only.
     */
    private int clientID;
    public int getClientID(){ return delivery_service.getSeller().getId(); }

    private String deliveryAreaPostCode;
    public String getDeliveryAreaPostCode(){ return delivery_area.getAreaPostCode(); }

    private String streetFrom;
    public String getStreetFrom(){
        refreshAccountDao(delivery_service.getSeller().getAccount());
        return delivery_service.getSeller().getAccount().getAddress().getStreetName();
    }

    private String postCodeFrom;
    public String getPostCodeFrom(){
        refreshAccountDao(delivery_service.getSeller().getAccount());
        return delivery_service.getSeller().getAccount().getAddress().getPostCode();
    }

    private void refreshAccountDao(Account account){
        AccountDao accountDao = new AccountDao(DbManager.getConnection());
        accountDao.refresh(account);
    }


    private String streetTo;
    public String getStreetTo(){
        Address address = delivery_service.getDeliveryAddress().getAddress();
        refreshAddressDao(address);
        return address.getStreetName();
    }

    private String postCode;
    public String getPostCode(){
        Address address = delivery_service.getDeliveryAddress().getAddress();
        refreshAddressDao(address);
        return address.getPostCode();
    }

    private void refreshAddressDao(Address address){
        AddressDao addressDao = new AddressDao(DbManager.getConnection());
        addressDao.refresh(address);
    }

    private String expectedDate;
    public String getExpectedDate(){ return delivery_service.getPredictedDeliveryDate(); }

    private String deliveredDate;
    public String getDeliveredDate(){ return delivery_service.getDateDelivered(); }

    private String receiverNameSurname;
    public String getReceiverNameSurname(){ return delivery_service.getDeliveryAddress().getReceiverName() + " " + delivery_service.getDeliveryAddress().getReceiverSurname(); }

    private String deliveryType;
    public String getDeliveryType(){ return delivery_service.getDeliveryType().getType(); }

    private String cancelled;
    public String getCancelled(){
        if(delivery_service.isCancelled())
            return "yes";
        else
            return "no";
    }
}
