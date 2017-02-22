package model;
import java.util.Properties;
import impresario.ModelRegistry;
import database.*;
import event.Event;

//Group Project.
//system imports
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.JFrame;



//project imports
import exception.InvalidPrimaryKeyException;
import database.*;
import impresario.IView;
import userinterface.*;



public class Scout extends EntityBase implements IView {
	private static final String 	myTableName = "Scout";
	protected Properties 			dependencies;
	private String 					updateStatusMessage = "";
	//private ModelRegistry 			myRegistry;
	
	private int						scoutId;
	private String 					lastName;
	private String 					firstName;
	private String 					middleName;
	private String 					dateOfBirth;
	private String 					phoneNumber;
	private String 					email;
	private String 					troopID;
	//'Active' and 'Inactive'
	private String 					status;
	private String 					dateStatusUpdated;
	
	protected MainScreen 			myScreen;

	//constructor for Scout. 
	public Scout(int scoutId)
		throws InvalidPrimaryKeyException
	{
		super(myTableName);
		String query = "SELECT * FROM " + myTableName + " WHERE (Id = " + scoutId + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
		// You must get at least one scout. 
				if (allDataRetrieved != null)
				{
					int size = allDataRetrieved.size();

					// There should be EXACTLY one scout.. More than that is an error
					if (size != 1)
					{
						throw new InvalidPrimaryKeyException("Multiple scoutId's matching : "
							+ scoutId + " found.");
					}
					else
					{
						// copy all the retrieved data into persistent state
						Properties retrievedscoutData = allDataRetrieved.elementAt(0);
						persistentState = new Properties();

						Enumeration allKeys = retrievedscoutData.propertyNames();
						while (allKeys.hasMoreElements() == true)
						{
							String nextKey = (String)allKeys.nextElement();
							String nextValue = retrievedscoutData.getProperty(nextKey);

							if (nextValue != null)
							{
								persistentState.setProperty(nextKey, nextValue);
							}
						}

					}
				}
				// If no scout was found for this user name, throw an exception
				else
				{
					throw new InvalidPrimaryKeyException("No scout matching id : "
						+ scoutId + " found.");
				}
	}

	//----------------------------------------------------------
	public Scout(Properties props)
	{
		super(myTableName);
		setDependencies();
		persistentState = new Properties();
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true)
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);
			if (nextValue != null)
			{
				persistentState.setProperty(nextKey, nextValue);
			}
		}
		
	}
	
	public Scout(MainScreen mainScreen, String key)
	{
		super(myTableName);
		myScreen = mainScreen;
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();
		if ((myRegistry = new ModelRegistry("Scout")) == null)
			new Event(Event.getLeafLevelClassName(this), "Scout", "Could not instantiate Registry", Event.ERROR);
		setDependencies();
		createAndShowScoutView(key);
	}
	
	public void update()
	{
		updateStateInDatabase("");
	}
	
	public void update(String key)
	{
		updateStateInDatabase(key);
	}
	
	//-----------------------------------------------------------------------------------
	private void updateStateInDatabase(String key) //added string key
	{
		try
		{
			if (persistentState.getProperty("Id") != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("Id", persistentState.getProperty("Id"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Scout data for scoutId : " + persistentState.getProperty("Id") + " updated successfully in database!";
			}
			else if (persistentState.getProperty("Id") != null && key.equals("modify"))
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("Id", persistentState.getProperty("Id"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Scout data for Id : " + persistentState.getProperty("Id") + " updated successfully in database!";
			}			
			else
			{
				Integer scoutId =
					insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("Id", " " + scoutId.intValue());
				updateStatusMessage = "Scout data for new scout : " +  persistentState.getProperty("Id")
					+ "installed successfully in database!";
			}
		}
		catch (SQLException ex)
		{
			System.out.println("Crash! Because: " + ex);
			updateStatusMessage = "Error in installing scout data in database!";
		}
		
		/*DEBUG */System.out.println("updateStateInDatabase = " + updateStatusMessage);
	}
	
	//------------------------------------------------------------
	//Method to add a scout to the database
	public void addScout(String fn, String ln, String mn,
					String dob, String phone, String em, String tID,
					String stat, String dsu)
	{
		Properties p = new Properties();
		p.setProperty("FirstName", fn);
		p.setProperty("LastName", ln);
		p.setProperty("MiddleName", mn);
		p.setProperty("DateOfBirth", dob);
		p.setProperty("PhoneNumber", phone);
		p.setProperty("Email", em);
		p.setProperty("TroopId", tID);
		p.setProperty("Status", stat);
		p.setProperty("DateStatusUpdated", dsu);
		Scout s = new Scout(p);
		s.update();
	}

	//Method to change a scouts information. 
	public void modifyScout(String scoutName)
	{
		System.out.println("Scout modifyScout TO_DO!");
	}

	//Method to remove Scout from the Database (set as inactive) 
	public void removeScout()
	{
		/*try {
			this.deletePersistentState(mySchema, persistentState);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		persistentState.setProperty("Status", "Inactive");
		update();
	}
	
	public static int compare(Scout a, Scout b)
	{
		String aNum = (String)a.getState("Id");
		String bNum = (String)b.getState("Id");
		
		return aNum.compareTo(bNum);
	}
	
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("Id"));
		v.addElement(persistentState.getProperty("FirstName"));
		v.addElement(persistentState.getProperty("LastName"));
		v.addElement(persistentState.getProperty("MiddleName"));
		v.addElement(persistentState.getProperty("DateOfBirth"));
		v.addElement(persistentState.getProperty("PhoneNumber"));
		v.addElement(persistentState.getProperty("Email"));
		v.addElement(persistentState.getProperty("TroopId"));
		v.addElement(persistentState.getProperty("Status"));
		v.addElement(persistentState.getProperty("DateStatusUpdated"));

		return v;
	}
	
	public void done()
	{
		myScreen.createAndShowMainScreenView();
	}
	
	public void createAndShowScoutView(String key) {
		Scene currentScene = (Scene)myViews.get("ScoutView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("ScoutView", this, key);
			currentScene = new Scene(newView);
			myViews.put("ScoutView", currentScene);
		}
		
		swapToView(currentScene);
	}

	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}

	public Object getState(String key)
	{
		if (key.equals("UpdateStatusMessage") == true)
			return updateStatusMessage;

		return persistentState.getProperty(key);
	}
	
	public void stateChangeRequest(String key, Object value)
	{
		myRegistry.updateSubscribers(key, this);
	}

	private void setDependencies()
	{
		dependencies = new Properties();
	
		myRegistry.setDependencies(dependencies);
	}

	@Override
	public void updateState(String key, Object value) 
	{
		stateChangeRequest(key, value);	
	}
	
	
	
}