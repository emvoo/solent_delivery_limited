package models.DbModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "account_types")
public class AccountType implements BaseModel {
    public AccountType(){}

    /*
        Table columns
     */
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String account_type;

    /*
        Getters
     */
    public int getId() { return id; }
    public String getAccountType() { return account_type; }

    /*
        Setters
     */
    public void setId(int id) { this.id = id; }
    public void setAccountType(String account_type) { this.account_type = account_type; }

}
