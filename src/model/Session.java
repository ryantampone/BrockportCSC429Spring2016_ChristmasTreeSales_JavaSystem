package model;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import userinterface.MainScreenView;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;

public class Session extends EntityBase implements IView {
	private static final String 	myTableName = "Session";
	protected Properties 			dependencies;
	private String 					updateStatusMessage = "";
	private Alert					alert;
	//private ModelRegistry 			myRegistry;

	public int 					id;
	private String 					startDate;
	private String 					startTime;
	private String 					endTime;
	private String 					startingCash;
	private String 					endingCash;
	private String 					totalCheckTransactionsAmount;
	private String					status; //'Open' or 'Closed'
	private String 					notes;
	private Vector<Session>			sessions;
	protected ResourceBundle		rb;
	
	private MainScreen				myScreen;
	
	public Session(int sessionId) throws InvalidPrimaryKeyException 
	{
		super(myTableName);
		
		setDependencies();
		rb = ResourceBundle.getBundle("SessionViewBundle", MainScreenView.language);
		String query = "SELECT * FROM " + myTableName + " WHERE (sessionId = " + sessionId + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one session at least
		if (allDataRetrieved != null)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one session. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple sessions matching id : "
					+ sessionId + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedSessionData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedSessionData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedSessionData.getProperty(nextKey);

					if (nextValue != null)
					{
						persistentState.setProperty(nextKey, nextValue);
						System.out.println("Key = " + nextKey + ", Value = " + nextValue);
					}
				}

			}
		}
		// If no session found for this Id, throw an exception
		else
		{
			throw new InvalidPrimaryKeyException("No session matching id : "
				+ sessionId + " found.");
		}

	}
	
	public Session(Properties props)
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
	
	public Session(MainScreen screen, String key)
	{
		super(myTableName);
		rb = ResourceBundle.getBundle("SessionViewBundle", MainScreenView.language);
		myScreen = screen;
		Session s = getOpenSession();
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();
		if ((myRegistry = new ModelRegistry("Session")) == null)
			new Event(Event.getLeafLevelClassName(this), "Session", "Could not instantiate Registry", Event.ERROR);
		setDependencies();
		if(s == null && key.equals("open"))
			createAndShowSessionView(key);
		else if(s == null && key.equals("close"))
		{
			alert = new Alert(AlertType.ERROR);
			alert.setTitle(rb.getString("TitleMsg"));
			alert.setHeaderText(rb.getString("CloseFailMsg"));
			//alert.setContentText("There is a session that is currently open.\nPlease close the session and try again.");
			alert.show();
		}
		else if(s != null && key.equals("open"))
		{
			alert = new Alert(AlertType.ERROR);
			alert.setTitle(rb.getString("TitleMsg"));
			alert.setHeaderText(rb.getString("OpenFailMsg"));
			//alert.setContentText("There is a session that is currently open.\nPlease close the session and try again.");
			alert.show();
		}
		else if(s == null && key.equals("sell"))
		{
			alert = new Alert(AlertType.ERROR);
			alert.setTitle(rb.getString("TitleMsg"));
			alert.setHeaderText(rb.getString("SellFailMsg"));
			//alert.setContentText("There is a session that is currently open.\nPlease close the session and try again.");
			alert.show();
		}
		else createAndShowSessionView(key);
	}
	
	public Session openSession(String sd, String st, String et,
			String sc, String n)
	{
		Properties p = new Properties();
		p.setProperty("StartDate", sd);
		p.setProperty("StartTime", st);
		p.setProperty("EndTime", et);
		p.setProperty("StartingCash", sc);
		p.setProperty("EndingCash", "---");
		p.setProperty("TotalChecksTransactionsAmount", "---");
		p.setProperty("Status", "Open");
		p.setProperty("Notes", n);
		Session s = new Session(p);
		s.update();
		return s;
	}
	
	public void closeSession (String ec, String tc)
	{
		persistentState.setProperty("StartDate", (String)this.getState("StartDate"));
		persistentState.setProperty("StartTime", (String)this.getState("StartTime"));
		persistentState.setProperty("EndTime", (String)this.getState("EndTime"));
		persistentState.setProperty("StartingCash", (String)this.getState("StartingCash"));
		persistentState.setProperty("EndingCash", ec);
		persistentState.setProperty("TotalChecksTransactionsAmount", tc);
		persistentState.setProperty("Status", "Closed");
		persistentState.setProperty("Notes", (String)this.getState("Notes"));
		update();
	}
	
	public Vector<Session> getSessions()
	{
		sessions = new Vector<Session>();
		String query = "SELECT * FROM Session";
		Vector allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null)
		{
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextSessionData = (Properties)allDataRetrieved.elementAt(cnt);
				//System.out.println("nextScoutData: " + nextScoutData.toString());
				Session s = new Session(nextSessionData);
				if (s != null)
					sessions.add(s);
			}
		}
		else
			System.out.println("ERROR: retrieveScout()");
		return sessions;
	}
	
	public Session getOpenSession()
	{
		String query = "SELECT * FROM Session";
		Vector allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null)
		{
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextSessionData = (Properties)allDataRetrieved.elementAt(cnt);
				Session s = new Session(nextSessionData);
				if(s.getState("Status").equals("Open"))
					return s;
			}
		}
		else
			System.out.println("ERROR: getOpenSession()");
		return null;
	}
	
	public void update()
	{
		updateStateInDatabase("");
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
				updateStatusMessage = "Session data for sessionId : " + persistentState.getProperty("Id") + " updated successfully in database!";
			}		
			else
			{
				Integer sessionId =	insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("Id", "" + sessionId.intValue());
				updateStatusMessage = "Session data for new session : " +  persistentState.getProperty("Id")
					+ " installed successfully in database!";
			}
		}
		catch (SQLException ex)
		{
			System.out.println("Crash! Because: " + ex);
			updateStatusMessage = "Error in installing session data in database!";
		}
		
		/*DEBUG */System.out.println("updateStateInDatabase = " + updateStatusMessage);
	}

	public String getEndCash()
	{
		String query = "SELECT SUM(  `TransactionAmount` ) FROM  `Transaction` WHERE  `SessionId` = "+ getOpenSession().persistentState.getProperty("Id") +" AND `PaymentMethod`  LIKE 'Cash';";
		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
		// You must get one session at least
		if (allDataRetrieved != null)
		{
			Properties retrievedSessionData = allDataRetrieved.elementAt(0);
			Enumeration allKeys = retrievedSessionData.propertyNames();
			while (allKeys.hasMoreElements() == true)
			{
				String nextKey = (String)allKeys.nextElement();
				String nextValue = retrievedSessionData.getProperty(nextKey);
				if (nextValue != null)
					return nextValue;
			}
		}
		else
			System.out.println("data NOT retrived successfully");
		return "";
	}

	public String getEndChecks()
	{
		String query = "SELECT SUM(  `TransactionAmount` ) FROM  `Transaction` WHERE  `SessionId` = "+ getOpenSession().persistentState.getProperty("Id") +" AND `PaymentMethod` NOT LIKE 'Cash';";
		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
		// You must get one session at least
		if (allDataRetrieved != null)
		{
			Properties retrievedSessionData = allDataRetrieved.elementAt(0);
			Enumeration allKeys = retrievedSessionData.propertyNames();
			while (allKeys.hasMoreElements() == true)
			{
				String nextKey = (String)allKeys.nextElement();
				String nextValue = retrievedSessionData.getProperty(nextKey);
				if (nextValue != null)
					return nextValue;
			}
		}
		else
			System.out.println("data NOT retrived successfully");
		return "";
	}
	
	public void done()
	{
		myScreen.createAndShowMainScreenView();
	}
	
	public void createAndShowSessionView(String key)
	{
		Scene currentScene = (Scene)myViews.get("SessionView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("SessionView", this, key);
			currentScene = new Scene(newView);
			myViews.put("SessionView", currentScene);
		}
		
		swapToView(currentScene);
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

	@Override
	public Object getState(String key) 
	{
		if (key.equals("UpdateStatusMessage") == true)
			return updateStatusMessage;

		return persistentState.getProperty(key);
	}

	@Override
	public void stateChangeRequest(String key, Object value) 
	{
		myRegistry.updateSubscribers(key, this);
	}

	@Override
	protected void initializeSchema(String tableName) 
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}
}