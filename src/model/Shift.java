package model;
import java.util.Properties;
import impresario.ModelRegistry;
import java.sql.SQLException;
//Simon

public class Shift extends EntityBase {

	private static final String myTableName = "Shift";
	protected Properties 		dependencies;
	private String 				updateStatusMessage = "";
	private ModelRegistry 		myRegistry;


	public Shift(Scout sc, Session se, String companionName, String companionHours) 
	{
		super(myTableName);
		//setDependencies();
		persistentState = new Properties();
		persistentState.setProperty("SessionId", (String)se.getState("Id"));
		persistentState.setProperty("ScoutId", (String)sc.getState("Id"));
		persistentState.setProperty("CompanionName", companionName);
		persistentState.setProperty("StartTime", (String)se.getState("StartTime"));
		persistentState.setProperty("EndTime", (String)se.getState("EndTime"));
		persistentState.setProperty("CompanionHours", companionHours);
		System.out.println("persistentState values: " + persistentState.toString());
		updateStateInDatabase();
	}

	private void updateStateInDatabase() 
	{
		try
		{
			if (persistentState.getProperty("Id") != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("Id",
				persistentState.getProperty("Id"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Shift data for Shift number : " + persistentState.getProperty("Id") + " updated successfully in database!";
			}
			else
			{
				Integer iD =
					insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("Id", "" + iD.intValue());
				updateStatusMessage = "Shift data for new Shift : " +  persistentState.getProperty("Id")
					+ "installed successfully in database!";
			}
		}
		catch (SQLException ex)
		{
			System.out.println("Update failed..." + ex.getCause());
			updateStatusMessage = "Error in installing account data in database!";
		}
		//DEBUG	System.out.println("updateStateInDatabase " + updateStatusMessage);
	}

	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}

	public void stateChangeRequest(String key, Object value)
	{
		if (key.equals("add") == true && value != null)
		{
			persistentState = (Properties)value;
			updateStateInDatabase();
		}
	}

	@Override
	public Object getState(String key) 
	{
		if (key.equals("UpdateStatusMessage") == true)
			return updateStatusMessage;
		return persistentState.getProperty(key);
	}

	private void setDependencies()
	{
		dependencies = new Properties();
		myRegistry.setDependencies(dependencies);
	}
}