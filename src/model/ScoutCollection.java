// specify the package
package model;
//
// system imports
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;
import impresario.IModel;
import userinterface.ScoutTableModel;
import userinterface.TreeTableModel;
import userinterface.View;
import userinterface.ViewFactory;
import java.util.Hashtable;
import userinterface.WindowPosition;

/** The class containing the TreeCollection for the ATM application */
//==============================================================
public class ScoutCollection  extends EntityBase implements IView, IModel
{
    private static final String myTableName = "Scout";
    private Vector<Scout> scouts;
    private MainScreen myScreen;
    // GUI Components
    
    // constructor for this class
    //-----------------------------------------------------------
    public ScoutCollection()
    {
    	super(myTableName);
		scouts = new Vector<Scout>();
    }

    public ScoutCollection(MainScreen mainScreen, String key)
    {
    	super(myTableName);
		scouts = new Vector<Scout>();
		myScreen = mainScreen;
		displayScouts();
		createAndShowScoutCollectionView(key);
    }
    
    /*public void displayTreeInfo() {
	for (Tree b : trees)
	    b.displayInfo();
    }*/
    
    private void addScout(Scout s)
	{
		int index = findIndexToAdd(s);
		scouts.insertElementAt(s,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Scout a)
	{
		//users.add(u);
		int low=0;
		int high = scouts.size()-1;
		int middle;
		
		while (low <= high)
		{
			middle = (low+high)/2;
			Scout midSession = scouts.elementAt(middle);
			int result = Scout.compare(a,midSession);

			if (result == 0)
				return middle;
			else if (result < 0)
				high = middle - 1;
			else
				low=middle+1;
		}
		return low;
	}
	
	public Scout retrieve(String scoutId)
	{
		Scout retValue = null;
		for (int cnt = 0; cnt < scouts.size(); cnt++)
		{
			Scout nextScout = scouts.elementAt(cnt);
			String nextScoutId = (String)nextScout.getState("Id");
			if (nextScoutId.equals(scoutId) == true)
			{
				retValue = nextScout;
				return retValue; // we should say 'break;' here
			}
		}

		return retValue;
	}
	
	public Vector<String> getScoutFirstName()
	{
		Vector<String> ScoutFirstName = new Vector<String>();
		for (int cnt = 0; cnt < scouts.size(); cnt++)
		{
			Scout nextScout = scouts.elementAt(cnt);
			String nextScoutFirstName = (String)nextScout.getState("FirstName");
			ScoutFirstName.add(nextScoutFirstName);
		}
		return ScoutFirstName;
	}
	
	public Vector<String> getScoutLastName()
	{
		Vector<String> ScoutLastName = new Vector<String>();
		for (int cnt = 0; cnt < scouts.size(); cnt++)
		{
			Scout nextScout = scouts.elementAt(cnt);
			String nextScoutLastName = (String)nextScout.getState("LastName");
			ScoutLastName.add(nextScoutLastName);
		}
		return ScoutLastName;
	}
	
	public Vector<String> getScoutIds()
	{
		Vector<String> scoutIds = new Vector<String>();
		for (int cnt = 0; cnt < scouts.size(); cnt++)
		{
			Scout nextScout = scouts.elementAt(cnt);
			String nextScoutId = (String)nextScout.getState("Id");
			scoutIds.add(nextScoutId);
		}
		return scoutIds;
	}
	
	public void done()
	{
		myScreen.createAndShowMainScreenView();
	}
	
	public void displayScouts()
	{
		scouts = new Vector<Scout>();
		String query = "SELECT * FROM " + myTableName;
		Vector allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null)
		{
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextScoutData = (Properties)allDataRetrieved.elementAt(cnt);
				//System.out.println("nextScoutData: " + nextScoutData.toString());
				Scout s = new Scout(nextScoutData);
				if (s != null)
					addScout(s);
			}
		}
		else
			System.out.println("ERROR: displayScouts()");
	}
	
	public void retrieveScout(String id)
	{
		scouts = new Vector<Scout>();
		String query = "SELECT * FROM Scout WHERE (Id = " + id + ")";
		Vector allDataRetrieved = getSelectQueryResult(query);
		if (allDataRetrieved != null)
		{
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextScoutData = (Properties)allDataRetrieved.elementAt(cnt);
				//System.out.println("nextScoutData: " + nextScoutData.toString());
				Scout s = new Scout(nextScoutData);
				if (s != null)
					addScout(s);
			}
		}
		else
			System.out.println("ERROR: retrieveScout()");
	}
	
    public void createAndShowScoutCollectionView(String key)
    {
    	Scene currentScene = (Scene)myViews.get("ScoutCollectionView");
	
        if (currentScene == null)
        {
            View newView = ViewFactory.createView("ScoutCollectionView", this, key);
            currentScene = new Scene(newView);
            myViews.put("ScoutCollectionView", currentScene);
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

    public void updateState(String key, Object value)
    {
    	stateChangeRequest(key, value);
    }

    public void stateChangeRequest(String key, Object value)
    {
    	// Possibly not done?
		if (key.equals("Done") == true)
		{
			myScreen.createAndShowMainScreenView();
		}
		else if (key.equals("SearchScout") == true)
		{
			if (value != null)
			{
				persistentState = (Properties) value;
			}
		}
		
		else if (key.equals("modify")) 
		{
			if (value != null)
			{
				Scout s = new Scout(((ScoutTableModel)value).getProperty());
				s.update("modify");
				myScreen.createAndShowMainScreenView();
		    }
	    }
		
		/*else if (key.equals("Submit") == true)
		{
			System.out.println("Submit Updated Scout");
			TableView<ScoutTableModel> tv = (TableView<ScoutTableModel>)value;
			if (tv == null)
				System.out.println("owned bro...!");
			System.out.println(tv.getItems());
			for (ScoutTableModel stm : tv.getItems())
			{
				Scout s = new Scout(stm.getProperty());
			}
			// System.out.println("To do : check data and push them to the DB");
		    myScreen.createAndShowMainScreenView();
		}*/
		myRegistry.updateSubscribers(key, this);
    }

    public Object getState(String key)
    {
		if (key.equals("Scouts"))
		    return scouts;
		else
		    if (key.equals("scoutlist"))
			return this;
		if (key.equals("ScoutList"))
		    return this;
		return null;
    }
    
}
