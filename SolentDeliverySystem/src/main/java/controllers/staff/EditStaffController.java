package controllers.staff;

import Dao.AccountDao;
import Dao.AddressDao;
import Dao.StaffDao;
import Utils.DbManager;
import com.j256.ormlite.support.ConnectionSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import models.DbModels.Account;
import models.DbModels.Address;
import models.DbModels.Staff;

/**
 * Controller responsible for updating member of staff details.
 */
public class EditStaffController {
    private Staff staff;
    private Account account;
    private Address address;

    @FXML private TextField name, surname, houseNo, street, postcode, city, password;

    /**
     *  Retrives and sets staff his account and address based on admin selection.
     * @param staff Staff - passed from different controller.
     */
    public void initData(Staff staff){
        this.staff = staff;
        this.account = staff.getAccount();
        this.address = account.getAddress();
        populateFields();
    }

    /**
     * Populate text fields.
     */
    private void populateFields() {
        name.setText(account.getName());
        surname.setText(account.getSurname());
        houseNo.setText(String.valueOf(address.getHouseNo()));
        street.setText(address.getStreetName());
        postcode.setText(address.getPostCode());
        city.setText(address.getTown());
    }

    /**
     * Closes this window.
     * @param event
     */
    @FXML private void cancelAction(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    /**
     * Updates all associated with staff database column's tables
     * @param event
     */
    @FXML private void saveStaffAction(ActionEvent event){
        ConnectionSource conn = DbManager.getConnection();

        address.setHouseNo(Integer.parseInt(houseNo.getText()));
        address.setStreetName(street.getText());
        address.setPostCode(postcode.getText());
        address.setTown(city.getText());

        AddressDao addressDao = new AddressDao(conn);
        addressDao.createOrUpdate(address);

        account.setName(name.getText());
        account.setSurname(surname.getText());
        account.setAddress(address);
        if(!password.getText().equals(""))
            account.setPassword(password.getText());

        AccountDao accountDao = new AccountDao(conn);
        accountDao.createOrUpdate(account);

        staff.setAccount(account);

        StaffDao staffDao = new StaffDao(conn);
        staffDao.createOrUpdate(staff);

        ((Node)event.getSource()).getScene().getWindow().hide();

    }
}
