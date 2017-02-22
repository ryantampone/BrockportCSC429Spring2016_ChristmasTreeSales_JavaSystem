package userinterface;

import java.util.Calendar;
import java.util.Properties;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class ScoutTableModel extends AbstractTableModel
{
	private final SimpleStringProperty scoutId;
	private final SimpleStringProperty firstName;
	private final SimpleStringProperty lastName;
	private final SimpleStringProperty middleName;
	private final SimpleStringProperty dateOfBirth;
	private final SimpleStringProperty phoneNumber;
	private final SimpleStringProperty email;
	private final SimpleStringProperty troopId;
	private final SimpleStringProperty status;
	private final SimpleStringProperty dateStatusUpdated;

	//----------------------------------------------------------------------------
	public ScoutTableModel(Vector<String> scoutData)
	{
		scoutId = new SimpleStringProperty(scoutData.elementAt(0));
		firstName = new SimpleStringProperty(scoutData.elementAt(1));
		lastName = new SimpleStringProperty(scoutData.elementAt(2));
		middleName = new SimpleStringProperty(scoutData.elementAt(3));
		dateOfBirth = new SimpleStringProperty(scoutData.elementAt(4));
		phoneNumber = new SimpleStringProperty(scoutData.elementAt(5));
		email = new SimpleStringProperty(scoutData.elementAt(6));
		troopId = new SimpleStringProperty(scoutData.elementAt(7));
		status = new SimpleStringProperty(scoutData.elementAt(8));
		dateStatusUpdated = new SimpleStringProperty(scoutData.elementAt(9));
	}
	
    public Properties getProperty()
    {
    	
        Properties p = new Properties();
        p.put("Id", scoutId.getValue());
        p.put("FirstName", firstName.getValue());
        p.put("LastName", lastName.getValue());
        p.put("MiddleName", middleName.getValue());
        p.put("DateOfBirth", dateOfBirth.getValue());
        p.put("PhoneNumber", phoneNumber.getValue());
        p.put("Email", email.getValue());
        p.put("TroopId", troopId.getValue());
        p.put("Status", status.getValue());
        p.put("DateStatusUpdated", getDateStatusUpdated());
        return p;
    }
	
	public String getScoutId() {
		return scoutId.get();
	}

	public String getFirstName() {
		return firstName.get();
	}
	
	public void setFirstName(String fn) {
		firstName.set(fn);
	}

	public String getLastName() {
		return lastName.get();
	}
	
	public void setLastName(String ln) {
		lastName.set(ln);
	}

	public String getMiddleName() {
		return middleName.get();
	}
	
	public void setMiddleName(String mn) {
		middleName.set(mn);
	}

	public String getDateOfBirth() {
		return dateOfBirth.get();
	}
	
	public void setDateOfBirth(String dob) {
		dateOfBirth.set(dob);
	}

	public String getPhoneNumber() {
		return phoneNumber.get();
	}
	
	public void setPhoneNumber(String phone) {
		phoneNumber.set(phone);
	}

	public String getEmail() {
		return email.get();
	}
	
	public void setEmail(String em) {
		email.set(em);
	}

	public String getTroopId() {
		return troopId.get();
	}
	
	public void setTroopId(String tID) {
		troopId.set(tID);
	}

	public String getStatus() {
		return status.get();
	}
	
	public void setStatus(String stat) {
		status.set(stat);
	}

	public String getDateStatusUpdated() {
		return dateStatusUpdated.get();
	}
	
	public void setDateStatusUpdated(String dsu) {
		dateStatusUpdated.set(dsu);
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}	
}
