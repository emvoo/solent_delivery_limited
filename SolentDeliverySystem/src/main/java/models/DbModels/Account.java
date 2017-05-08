package models.DbModels;


import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "accounts")
public class Account implements BaseModel{

    public Account(){}

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, unique = true)
    private String username;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String surname;

    @DatabaseField(canBeNull = false)
    private String password;

    @DatabaseField(defaultValue = "false")
    private Boolean isLoggedIn;

    // foreign keys
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Address address; // column name will be 'address_id' by default, can be overriden by 'columnName' parameter

    @ForeignCollectionField
    private ForeignCollection<Client> clients;

    @ForeignCollectionField
    private ForeignCollection<Staff> staffs;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private AccountType account_type;

    /*
        Getters
     */
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getPassword() { return password; }
    public Address getAddress() { return address; }
    public boolean isLoggedIn() { return isLoggedIn; }
    public ForeignCollection<Staff> getStaffs(){ return this.staffs; }
    public ForeignCollection<Client> getClients(){ return this.clients; }
    public AccountType getAccountType() { return account_type; }

    /*
        Setters
     */
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setPassword(String password) { this.password = password; }
    public void setAddress(Address address) { this.address = address; }
    public void login() { isLoggedIn = true; }
    public void logout() { isLoggedIn = false; }
    public void setClients(ForeignCollection<Client> clients) { this.clients = clients; }
    public void setStaffs(ForeignCollection<Staff> staffs) { this.staffs = staffs; }
    public void setAccountType(AccountType account_type) { this.account_type = account_type; }
}
