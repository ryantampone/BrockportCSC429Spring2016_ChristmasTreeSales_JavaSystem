package model;

import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;
import impresario.IView;
import impresario.IModel;
import userinterface.View;
import userinterface.ViewFactory;
import javafx.scene.control.TableView;
import java.util.Hashtable;
import userinterface.WindowPosition;
import userinterface.TreeTableModel;

public class TreeCollection  extends EntityBase implements IView, IModel
{
    private static final String myTableName = "Tree";
    private Vector<Tree> trees;
    private MainScreen myScreen;

    public TreeCollection()
    {
		super(myTableName);
		trees = new Vector<Tree>();
    }

    public TreeCollection(MainScreen myS, String key)
    {
		super(myTableName);
		trees = new Vector<Tree>();
		myScreen = myS;
		createAndShowTreeCollectionView(key);
    }
	
	public void findAllTrees()
	{
		try {
		    String query = "SELECT * FROM " + myTableName + ";";
		    Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
		    
		    if (allDataRetrieved != null)
			for (Properties p : allDataRetrieved)
			    trees.addElement(new Tree(p));
		    else
			throw new InvalidPrimaryKeyException("No Tree.");
		} 
		catch (InvalidPrimaryKeyException e) {
						System.err.println(e);
		}
		
	}
	
    public void findTreesOlderThanDate(String year)
    {
	try {
	    String query = "SELECT * FROM " + myTableName + " WHERE (pubYear < " + year + ");";
	    Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
	    
	    if (allDataRetrieved != null)
		for (Properties p : allDataRetrieved)
		    trees.addElement(new Tree(p));
	    else
		throw new InvalidPrimaryKeyException("No older Tree matching for year "
						     + year + ".");
	} 
	catch (InvalidPrimaryKeyException e) {
					System.err.println(e);
	}
    }
    
    public void findTreesNewerThanDate(String year)
    {
	String query = "SELECT * FROM " + myTableName + " WHERE (pubYear > " + year + ");";
	Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
	try {	
	    if (allDataRetrieved != null)
		for (Properties p : allDataRetrieved)
		    trees.addElement(new Tree(p));
	    else
		{
		    throw new InvalidPrimaryKeyException("No newer Tree matching for year "
							 + year + ".");
		}
	} catch (InvalidPrimaryKeyException e) {
	    System.err.println(e);
	}
    }

    public void findTreesLike(String title, String author)
    {
	trees.clear();
	String query = "SELECT * FROM " + myTableName + " WHERE title like '%" + title + "%' && author like '%" + author + "%';";
	Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
	try {
	    if (allDataRetrieved != null)
		{
		    for (Properties p : allDataRetrieved)
			{
			    Tree b = new Tree(p);
			    trees.add(b);
			}
		}
	    else
		{
		    throw new InvalidPrimaryKeyException("No Tree matching for title "
							 + title + ".");
		}
	}catch (InvalidPrimaryKeyException e) {
	    System.err.println(e);
	}
    }
    
    public void findTreesWithTitleLike(String title)
    {
	trees.clear();
	String query = "SELECT * FROM " + myTableName + " WHERE title like '%" + title + "%';";
	Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
	try {
	    if (allDataRetrieved != null)
		{
		    for (Properties p : allDataRetrieved)
			{
			    Tree b = new Tree(p);
			    trees.add(b);
			}
		}
	    else
		{
		    throw new InvalidPrimaryKeyException("No Tree matching for title "
							     + title + ".");
		}
	}catch (InvalidPrimaryKeyException e) {
	    System.err.println(e);
	}
    }
    
    public void findTreesWithAuthorLike(String author)
    {
	String query = "SELECT * FROM " + myTableName + " WHERE  (author like '" + author + "');";
	Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
	try {
	    if (allDataRetrieved != null)
		for (Properties p : allDataRetrieved) {
		    trees.addElement(new Tree(p));
		}
	    else
		{
		    throw new InvalidPrimaryKeyException("No Tree matching for author "
							 + author + ".");
		}
	}catch (InvalidPrimaryKeyException e) {
	    System.err.println(e);
	}
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
	if (key.equals("done"))
		myScreen.createAndShowMainScreenView();
	else if (key.equals("search")) {
		persistentState = (Properties) value;
		findAllTrees();
	}
	else if (key.equals("modify")) {
		if (value != null)
		{
			Tree t = new Tree(((TreeTableModel)value).getProperty());
			t.update("modify");
			myScreen.createAndShowMainScreenView();
	    }
    }
	else if (key.equals("remove")) {
		if (value != null)
		{
			Tree t = new Tree(((TreeTableModel)value).getProperty());
			t.update("remove");
			myScreen.createAndShowMainScreenView();
	    }
	}
	myRegistry.updateSubscribers(key, this);
    }

    public Object getState(String key)
    {
		if (key.equals("Trees"))
			return trees;
		else if (key.equals("treelist"))
			return this;
		if (key.equals("TreeList"))
			return this;
		return null;
    }
    
    public void createAndShowTreeCollectionView(String key)
    {
	Scene currentScene = (Scene)myViews.get("TreeCollectionView");
	
        if (currentScene == null)
            {
                View newView = ViewFactory.createView("TreeCollectionView", this, key);
                currentScene = new Scene(newView);
                myViews.put("TreeCollectionView", currentScene);
            }
	swapToView(currentScene);
    }

    public void done()
	{
		myScreen.createAndShowMainScreenView();
	}
	
    public void swapToView(Scene newScene)
    {
        if (newScene == null)
            {
                System.out.println("TreeCollection.swapToView(): Missing view for display");
		new Event(Event.getLeafLevelClassName(this), "swapToView", "Missing view for display ", Event.ERROR);
		return;
            }
	myStage.setScene(newScene);
        myStage.sizeToScene();
        WindowPosition.placeCenter(myStage);
    }
    
}
