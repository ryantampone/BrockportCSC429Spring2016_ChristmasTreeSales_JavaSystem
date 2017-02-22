// specify the package
package model;

// system imports
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;
import impresario.IModel;

import userinterface.View;
import userinterface.ViewFactory;
import java.util.Hashtable;
import userinterface.WindowPosition;
import javafx.scene.control.TableView;

/** The class containing the TreeTypeCollection for the ATM application */
//==============================================================
public class TreeTypeCollection  extends EntityBase implements IView, IModel
{
    private static final String myTableName = "TreeType";
    private Vector<TreeType> 	treeTypes;
    private MainScreen 			mainScreen;

    public TreeTypeCollection()
    {
		super(myTableName);
		treeTypes = new Vector<TreeType>();
    }

    public TreeTypeCollection(MainScreen ms)
    {
		super(myTableName);
		treeTypes = new Vector<TreeType>();
		mainScreen = ms;
		retriveAllTreeType();
		createAndShowTreeTypeCollectionView();
    }
	
    public void retriveAllTreeType()
	{
		try {
			String query = "SELECT * FROM " + myTableName + ";";
			Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
			if (allDataRetrieved != null)
				for (Properties p : allDataRetrieved)
			    	treeTypes.addElement(new TreeType(p));
		    else
				throw new InvalidPrimaryKeyException("No tree find.");
		}
		catch (InvalidPrimaryKeyException e) {
						System.err.println(e);
			}
	}

    public void displayTreeTypeInfo() {
		for (TreeType b : treeTypes)
			b.displayInfo();
	}
    
	public Vector<String> treePrefixes()
	{
		Vector<String> prefixes = new Vector<String>();
		for (int cnt = 0; cnt < treeTypes.size(); cnt++)
		{
			TreeType nextTreeType = treeTypes.elementAt(cnt);
			String nextPrefix = (String)nextTreeType.getState("BarcodePrefix");
			prefixes.add(nextPrefix);
		}
		return prefixes;
	}

	public String getIdFromBarcodePrefix(String barcodePre)
	{
		for (TreeType t : treeTypes)
		{
			if (t.getBarcodePrefix().equals(barcodePre))
				return t.getId();
		}
		return null;
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

	public void createAndShowTreeTypeCollectionView()
	{
		Scene currentScene = (Scene)myViews.get("TreeTypeCollectionView");
		if (currentScene == null)
		    {
			// create our initial view
			View newView = ViewFactory.createView("TreeTypeCollectionView", this);
			currentScene = new Scene(newView);
			myViews.put("TreeTypeCollectionView", currentScene);
	    }
		swapToView(currentScene);
	}
	
	public void stateChangeRequest(String key, Object value)
	{	
	    if (key.equals("Done") == true)
		{
		    mainScreen.createAndShowMainScreenView();
		}
		else if (key.equals("Submit") == true)
		{
			TableView<TreeTypeVector> tv = (TableView<TreeTypeVector>)value;
			if (tv == null)
				System.out.println("owned bro...!");
			for (TreeTypeVector ttv : tv.getItems())
			{
				TreeType tt = new TreeType(ttv.getProperty());
			}
			// System.out.println("To do : check data and push them to the DB");
		    mainScreen.createAndShowMainScreenView();
		}
	    myRegistry.updateSubscribers(key, this);
	}

	public Object getState(String key)
	{
		if (key.equals("TreeTypes"))
			return treeTypes;
		else
		if (key.equals("TreeTypelist"))
			return this;
		if (key.equals("TreeTypeList"))
			return this;
		return null;
	}

    public void swapToView(Scene newScene)
    {
        if (newScene == null)
            {
				new Event(Event.getLeafLevelClassName(this), "swapToView", "Missing view for display ", Event.ERROR);
				return;
            }
		myStage.setScene(newScene);
        myStage.sizeToScene();
        WindowPosition.placeCenter(myStage);
    }

}