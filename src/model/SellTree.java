/*package model;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import userinterface.View;
import userinterface.ViewFactory;
import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IModel;s
import impresario.IView;
import impresario.ModelRegistry;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import userinterface.MainStageContainer;
import userinterface.ScoutTableModel;
import userinterface.MainScreenView;
import userinterface.ScoutView;
import userinterface.TreeTableModel;
import userinterface.TreeView;
import userinterface.TreeTypeView;

public class SellTree extends EntityBase implements IView, IModel 
{
	//Variables
	private static final String 		myTableName = "Tree";
	protected Properties 				dependencies;
	private String 						updateStatusMessage = "";
	private ModelRegistry 				myRegistry;
	private MainScreen					myScreen;
	private Hashtable<String, Scene>	myViews;
	private Stage						myStage;


	public SellTree(MainScreen mainScreen)
	{
		super(myTableName);
		myScreen = mainScreen;
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();
		if ((myRegistry = new ModelRegistry("SellTree")) == null)
			new Event(Event.getLeafLevelClassName(this), "SellTree", "Could not instantiate Registry", Event.ERROR);
		setDependencies();
		createAndShowSellTreeView();
		
		
	}
	
	public SellTree(String barCode) throws InvalidPrimaryKeyException
	{
		super(myTableName);
		setDependencies();
		
		String query = "SELECT * FROM " + myTableName + " WHERE (Barcode = " + barCode + ")";
		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null)
		{
			if (allDataRetrieved.size() != 1)
			{
				throw new InvalidPrimaryKeyException("Mutiple trees matching barcode: " + barCode + " found.");
			}
			else 
			{
				Properties retrievedTreeData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();
				Enumeration allKeys = retrievedTreeData.propertyNames();
				while (allKeys.hasMoreElements() == true) 
				{
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedTreeData.getProperty(nextKey);
					if (nextValue != null)
						persistentState.setProperty(nextKey, nextValue);
				}
			}
		}
	}

	public void createNewTransaction()
	{
		Transaction t = new Transaction(this);
	}
	
	public void createAndShowSellTreeView()
	{
		Scene currentScene = (Scene)myViews.get("SellTreeView");
		if (currentScene == null) {
			View newView = ViewFactory.createView("SellTreeView", this);
			currentScene = new Scene(newView);
			myViews.put("SellTreeView", currentScene);
		}
		swapToView(currentScene);
	}
	private void createAndShowTransactionView() {
		Scene currentScene = (Scene)myViews.get("TransactionView");
		if (currentScene == null) {
			View newView = ViewFactory.createView("TranscationView", this);
			currentScene = new Scene(newView);
			myViews.put("TransactionView", currentScene);
		}
		swapToView(currentScene);
	}	

	
	
	public boolean checkBarcode(String barcode)
	{
		
		String query = "SELECT * FROM " + myTableName + " WHERE (Barcode = " + barcode + ")";
		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null)
		{
			if (allDataRetrieved.size() != 1)
			{
				return false;
				
			}
			else 
			{
				return true;
			}
			
		}
		return false;
	}
	public boolean checkTreeStatus(String barcode)
	{
		return true;
	}

	public SellTree(Properties props) 
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

	public SellTree(MainScreen mainScreen, String key)
	{
		super(myTableName);
		myScreen = mainScreen;
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();
		if ((myRegistry = new ModelRegistry("Tree")) == null)
			new Event(Event.getLeafLevelClassName(this), "Tree", "Could not instantiate Registry", Event.ERROR);
		setDependencies();
		createAndShowSellTreeView(key);
	}
	
	public void update()
	{
		updateStateInDatabase("");
	}
	
	public void update(String key)
	{
		updateStateInDatabase(key);
	}
	
	private void updateStateInDatabase(String key) 
	{
		try
		{
			if (persistentState.getProperty("Barcode") != null && key.equals("add"))
			{
				insertPersistentState(mySchema, persistentState);
				updateStatusMessage = "Tree data for new Tree : " +  persistentState.getProperty("Barcode")
					+ "installed successfully in database!";
			}
			else if (persistentState.getProperty("Barcode") != null && key.equals("modify"))
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("Barcode", persistentState.getProperty("Barcode"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Tree data for Barcode : " + persistentState.getProperty("Barcode") + " updated successfully in database!";
			}
			else if (persistentState.getProperty("Barcode") != null && key.equals("remove"))
			{
				deletePersistentState(mySchema, persistentState);
				updateStatusMessage = "Tree data for new Tree : " +  persistentState.getProperty("Barcode")
					+ "deleted successfully in database!";
			}
			else
			{
				Integer barcode = insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("Barcode", "" + barcode.intValue());
				updateStatusMessage = "Tree data for new Tree : " +  persistentState.getProperty("Barcode")
					+ "installed successfully in database!";
			}
		}
		catch (SQLException ex)
		{
			//System.out.println("Crash! Because: " + ex);
			//updateStatusMessage = "Error in installing tree data in database!";
		}
		
		DEBUG System.out.println("updateStateInDatabase = " + updateStatusMessage);
	}
	
	public void removeTree()
	{
		System.out.println("Tree removeTree Method!");
	}
	
	public static int compare(Tree a, Tree b)
	{
		String aNum = (String)a.getState("Barcode");
		String bNum = (String)b.getState("Barcode");

		return aNum.compareTo(bNum);
	}
	
	
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("Barcode"));
		v.addElement(persistentState.getProperty("TreeType"));
		v.addElement(persistentState.getProperty("Notes"));
		v.addElement(persistentState.getProperty("Status"));
		v.addElement(persistentState.getProperty("DateStatusUpdated"));

		return v;
	}

	private void createAndShowSellTreeView(String key) {
		Scene currentScene = (Scene)myViews.get("SellTreeView");
		if (currentScene == null) {
			View newView = ViewFactory.createView("SellTreeView", this, key);
			currentScene = new Scene(newView);
			myViews.put("SellTreeView", currentScene);
		}
		swapToView(currentScene);
	}
	
	//Initialize Scheme and work with DB
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}

	public Object getState(String key)
	{
		return (null);
	}
	
	public void stateChangeRequest(String key, Object value)
	{
		if (key.equals("Back") == true)
		{
			myScreen.createAndShowMainScreenView();
		}
		else if (key.equals("add") == true && value != null)
		{
			persistentState = (Properties)value;
			myRegistry.setDependencies((Properties)value);
			updateStateInDatabase("add");
		}
		else if (key.equals("modify") == true && value != null)
		{
			persistentState = (Properties)value;
			myRegistry.setDependencies((Properties)value);
			updateStateInDatabase("modify");
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
	
	public void done()
	{
		myScreen.createAndShowMainScreenView();
	}
}
*/