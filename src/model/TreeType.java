package model;

//DEBUG
import java.util.Set;

import event.Event;

import impresario.ModelRegistry;
import impresario.IView;
import impresario.IModel;

import javafx.stage.Stage;
import javafx.scene.Scene;

import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.SQLException;
import java.util.Hashtable;

import database.*;
import exception.InvalidPrimaryKeyException;

public class TreeType extends EntityBase {
	private static final String 	myTableName = "TreeType";
	protected Properties 			dependencies;
	private String 					updateStatusMessage = "";
	private ModelRegistry 			myRegistry;
	private MainScreen 				ms;
	// private int 					id;
	// private String 					typeDescription;
	// private String 					cost;
	// private String 					barcodePrefix;

	public TreeType(int id, MainScreen mainScreen)  throws InvalidPrimaryKeyException
	{
		super(myTableName);
		setDependencies();
		ms = mainScreen;
		String query = "SELECT * FROM " + myTableName + " WHERE (TreeId = " + id + ")";
		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null)
	    {
			int size = allDataRetrieved.size();
			if (size != 1)
			    {
					throw new InvalidPrimaryKeyException("Multiple TreeType matching id : "
							     + id + " found.");
		    	}
			else
		    	{
					Properties retrievedtreeTypeData = allDataRetrieved.elementAt(0);
					persistentState = new Properties();
					Enumeration allKeys = retrievedtreeTypeData.propertyNames();
					while (allKeys.hasMoreElements() == true)
			    	{
				    	String nextKey = (String)allKeys.nextElement();
				    	String nextValue = retrievedtreeTypeData.getProperty(nextKey);
					    if (nextValue != null)
						{
						    persistentState.setProperty(nextKey, nextValue);
						}
			    	}
			    }
	    }
	// If no treeType found for this id, throw an exception
	else
	    {
			throw new InvalidPrimaryKeyException("No TreeType matching id : "
						     + id + " found.");
	    }
	}
	
	public TreeType(Properties prop) 
	{
		super(myTableName);
		persistentState = new Properties();
		Enumeration allKeys = prop.propertyNames();
		while (allKeys.hasMoreElements() == true)
	    	{
				String nextKey = (String)allKeys.nextElement();
				String nextValue = prop.getProperty(nextKey);
				if (nextValue != null)
						persistentState.setProperty(nextKey, nextValue);
				//System.out.println("Set persistentState with : " + nextKey + nextValue);
			}
		// setDependencies();
		updateStateInDatabase();
	}

	public TreeType(MainScreen mainScreen)
	{
		super(myTableName);
		ms = mainScreen;
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();
		myRegistry = new ModelRegistry("TreeType");
		if (myRegistry == null)
		{
			new Event(Event.getLeafLevelClassName(this), "TreeType", "Could not instantiate Registry", Event.ERROR);
		}
		setDependencies();
		createAndShowTreeTypeView();
	}

	public String getId()
	{
		if (persistentState.getProperty("Id") != null)
			return persistentState.getProperty("Id");
		return null;
	}

	public String getBarcodePrefix()
	{
		if (persistentState.getProperty("BarcodePrefix") != null)
			return persistentState.getProperty("BarcodePrefix");
		return null;
	}

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

	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}

	public void stateChangeRequest(String key, Object value)
	{
		if (key.equals("Done") == true)
		{
			ms.createAndShowMainScreenView();
		}
		if (key.equals("add") == true && value != null)
		{
			persistentState = (Properties)value;
			updateStateInDatabase();
		}
	}

	public void createAndShowTreeTypeView()
	{
		Scene currentScene = (Scene)myViews.get("TreeTypeView");
		if (currentScene == null)
		    {
			// create our initial view
			View newView = ViewFactory.createView("TreeTypeView", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("TreeTypeView", currentScene);
	    }
		swapToView(currentScene);
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
				updateStatusMessage = "treeType data for treeType number : " + persistentState.getProperty("Id") + " updated successfully in database!";
			}
			else
			{
				Integer iD =
					insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("Id", "" + iD.intValue());
				updateStatusMessage = "treeType data for new treeType : " +  persistentState.getProperty("Id")
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

	public void swapToView(Scene newScene)
	{		
		if (newScene == null)
		{
			new Event(Event.getLeafLevelClassName(this), "swapToView",
				"Missing view for display ", Event.ERROR);
			return;
		}
		myStage.setScene(newScene);
		myStage.sizeToScene();

		WindowPosition.placeCenter(myStage);

	}

	public void displayInfo() {
		Enumeration keys = persistentState.keys();
		while (keys.hasMoreElements()) {
		  String key = (String)keys.nextElement();
		  String value = (String)persistentState.get(key);
		  System.out.println(key + ": " + value);
		}
	}
    
    public Vector<String> getVector()
    {
		Vector<String> v = new Vector<String>();
		v.addElement(persistentState.getProperty("Id"));
		v.addElement(persistentState.getProperty("BarcodePrefix"));
		v.addElement(persistentState.getProperty("Cost"));
		v.addElement(persistentState.getProperty("Description"));
		return v;
    }

	public void subscribe(String key, IView subscriber) {}
	public void unSubscribe(String key, IView subscriber) {}

 }
