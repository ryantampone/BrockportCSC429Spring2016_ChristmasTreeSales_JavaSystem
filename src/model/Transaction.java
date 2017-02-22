package model;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Hashtable;
import java.util.Vector;
import event.Event;
import javafx.scene.Scene;
import javafx.stage.Stage;
import impresario.IModel;
import impresario.IView;
import exception.InvalidPrimaryKeyException;
import userinterface.View;
import userinterface.ViewFactory;
import impresario.ModelRegistry;
import userinterface.MainStageContainer;
import userinterface.TransactionView;
import userinterface.MainScreenView;

public class Transaction  extends EntityBase implements IView, IModel {
	private static final String 	myTableName = "Transaction";
	protected Properties 			dependencies;
	private String 					updateStatusMessage = "";
	private ModelRegistry 				myRegistry;
	private MainScreen					myScreen;
	private Hashtable<String, Scene>	myViews;
	private Stage						myStage;
	protected Properties			treeInfo;
	protected Properties 			sessionIdProp;
/*
	private int 					id;
	private int 					sessionID;
	private String 					transactionType;
	private String 					barcode;
	private String 					transactionAmount;
	private String 					paymentMethod;
	private String 					customerName;
	private String 					customerPhone;
	private String 					customerEmail;
	private String 					transactionDate;
	private String 					transactionTime;
	private String 					dateStatusUpdated;*/
	
	public Transaction(String id) throws InvalidPrimaryKeyException
	{
		super(myTableName);
		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (Id = " + id + ")";
		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null)
		{
			if (allDataRetrieved.size() != 1)
			{
				throw new InvalidPrimaryKeyException("Mutiple Transactions matching Id: " + id + " found.");
			}
			else 
			{
				Properties retrievedTransactionData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();
				Enumeration allKeys = retrievedTransactionData.propertyNames();
				while (allKeys.hasMoreElements() == true) 
				{
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedTransactionData.getProperty(nextKey);
					if (nextValue != null)
						persistentState.setProperty(nextKey, nextValue);
				}
			}
		}
	}

	public Transaction(Properties props) 
	{
		super(myTableName);
		//setDependencies();
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
	
	public Transaction(MainScreen mainScreen) 
	{
		super(myTableName);
		myScreen = mainScreen;
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();
		Session s = new Session(myScreen, "sell");
		if(s.getOpenSession() != null)
		{
			if ((myRegistry = new ModelRegistry("Transaction")) == null)
				new Event(Event.getLeafLevelClassName(this), "Transaction", "Could not instantiate Registry", Event.ERROR);
			setDependencies();
			createAndShowTransactionView();
		}
	}

	public Object getState(String key) {
		return (null);
	}

	public void stateChangeRequest(String key, Object value) {
		if (key.equals("Back") == true)
		{
			done();
		}
		else if (key.equals("Search") == true && value != null)
		{
			persistentState = (Properties)value;
			myRegistry.setDependencies((Properties)value);
			updateStateInDatabase(key);
		}
		else if (key.equals("Done") == true && value != null)
		{
			persistentState = (Properties)value;
			myRegistry.setDependencies((Properties)value);
			updateStateInDatabase(key);
			done();
		}
	}
	
	public void updateStateInDatabase(String key)
	{
		try
		{
			if (persistentState.getProperty("Id") == null && key.equals("Done")) {
				Integer transactionId = insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("Id", "" + transactionId.intValue());
				updateStatusMessage = "Tree data for new Tree : " +  persistentState.getProperty("Id")
					+ "installed successfully in database!";
			}
			else if (persistentState.getProperty("Barcode") != null && key.equals("Search")) {
				String query = "SELECT * FROM Tree, TreeType WHERE Tree.TreeType = TreeType.Id AND (Barcode = " + persistentState.getProperty("Barcode") + " AND Status = 'Available')";
				Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
				if (allDataRetrieved != null)
				{
					if (allDataRetrieved.size() != 1)
					{
						//throw new InvalidPrimaryKeyException("Mutiple Session matching Id: " + persistentState.getProperty("SessionId") + " found.");
					}
					else 
					{
						Properties retrievedTreeData = allDataRetrieved.elementAt(0);
						treeInfo = new Properties();
						Enumeration allKeys = retrievedTreeData.propertyNames();
						while (allKeys.hasMoreElements() == true) 
						{
							String nextKey = (String)allKeys.nextElement();
							String nextValue = retrievedTreeData.getProperty(nextKey);
							if (nextValue != null)
								treeInfo.setProperty(nextKey, nextValue);
						}
					}
				}
			}
		}
		catch (SQLException ex)
		{
			System.out.println("Crash! Because: " + ex);
			//updateStatusMessage = "Error in installing tree data in database!";
		}
		
		/*DEBUG */System.out.println("updateStateInDatabase = " + updateStatusMessage);
	}
	
	public void addTrans(String bc)
	{
		Properties p = new Properties();
		
		p.setProperty("SessionId", "-");
		p.setProperty("TransactionType", "-");
		p.setProperty("Barcode",bc);
		p.setProperty("TransactionAmount", "-");
		p.setProperty("PaymentMethod", "-");
		p.setProperty("CustomerName", "-");
		p.setProperty("CustomerPhone", "-");
		p.setProperty("CustomerEmail", "-");	
		p.setProperty("TransactionDate", "-");
		p.setProperty("TransactionTime", "-");
		p.setProperty("DateStatusUpdated", "-");
	
		System.out.println(this.getState("Barcode"));
		this.stateChangeRequest("Search", p);
				
		
	}
	
	public void addTransaction(String name, String eMail, String phone, String payment,String bc, String cost, String day,
			String sessionID)
	{
		Properties p = new Properties();
		
		p.setProperty("CustomerName", name);
		p.setProperty("CustomerEmail", eMail);
		p.setProperty("CustomerPhone", phone);
		p.setProperty("PaymentMethod", payment);
		p.setProperty("SessionId", sessionID);
		p.setProperty("Barcode", bc);
		p.setProperty("TransactionAmount", cost);
		p.setProperty("TransactionDate", day);
		p.setProperty("TransactionTime", "-");
		p.setProperty("TransactionType", "-");
		p.setProperty("DateStatusUpdated", "-");
		
		
		Transaction t = new Transaction(p);
		t.updateStateInDatabase("Done");		
	}
	
	public String getSessionId()
	{
		
		String query = "SELECT Id FROM Session WHERE (Status = 'Open')";
		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
		if(allDataRetrieved != null)
				sessionIdProp = allDataRetrieved.elementAt(0);
				return sessionIdProp.getProperty("Id");
	}
	
//	public void addSession(Session myS)
//	{
//		Properties p = new Properties(); 
//		
//		
//		
//		
//	}
	
	
	
	
	public Properties getTreeInfo()
	{
		return (treeInfo);
	}
	
	public void update()
	{
		updateStateInDatabase("");
	}

	protected void initializeSchema(String tableName) {
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
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
	
	private void createAndShowTransactionView() {
		Scene currentScene = (Scene)myViews.get("TransactionView");
		
		if (currentScene == null) 
		{
			//Creates our initial view here
			View newView = ViewFactory.createView("TransactionView", this);
			currentScene = new Scene(newView);
			myViews.put("TransactionView", currentScene);
		}
		swapToView(currentScene);
	}
	
	public void done()
	{
		myScreen.createAndShowMainScreenView();
	}

	
}