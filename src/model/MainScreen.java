package model;

//Imports
import impresario.IModel;
import impresario.IView;

import java.util.Hashtable;

//import classes needed to show views
import model.Scout;
import model.Tree;
import model.TreeType;

import userinterface.MainScreenView;
import userinterface.TreeView;
import userinterface.TreeTypeView;

import exception.InvalidPrimaryKeyException;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainScreen implements IModel
{
	//GUI Components
	private Stage myStage;
	private Hashtable<String, Scene> myViews;
		
	public MainScreen() 
	{
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();
		createAndShowMainScreenView();
	}
		
	//Method called by constructor, creates/shows MainScreenView
	public void createAndShowMainScreenView() 
	{
		Scene currentScene = (Scene)myViews.get("MainScreenView");
		
		if (currentScene == null) 
		{
			//Creates initial GUI
			View newView = new MainScreenView(this);
			currentScene = new Scene(newView);
			myViews.put("MainScreenView", currentScene);
		}
		swapToView(currentScene);
	}
	
	
//----------------------------Scout----------------------------------------------
		
	//Create New Scout (Called when "Insert Scout" button is clicked)
	public void createNewScout()
	{
		Scout s = new Scout(this, "add");
	}
	
	
	//Modify Existing Scout (Called when "Modify Scout" button is clicked)
	public void modifyScout()
	{
		ScoutCollection sc = new ScoutCollection(this, "modify");
	}
	
	
	//Remove Existing Scout (Called when "Remove Scout" button is clicked)
	public void removeScout()
	{
		ScoutCollection sc = new ScoutCollection(this, "remove");
	}
	

//----------------------------Tree Type----------------------------------------------
	//Create New Tree Type (Called when "Insert Scout" button is clicked)
	public void createNewTreeType()
	{
		TreeType tt = new TreeType(this);
	}
	
	
	//Modify Existing Tree Type (Called when "Modify Scout" button is clicked)
	public void modifyTreeType()
	{
		TreeTypeCollection tt = new TreeTypeCollection(this);
	}
	
	
//----------------------------Tree----------------------------------------------

	//Create New Tree (Called when "Insert Tree" button is clicked)
	public void createNewTree()
	{
		Tree t = new Tree(this, "add");
	}
	
	//Modify Existing Scout (Called when "Modify Scout" button is clicked)
	public void modifyTree()
	{
		TreeCollection tc = new TreeCollection(this, "modify");
	}
	
	
	//Modify Existing Scout (Called when "Modify Scout" button is clicked)
	public void removeTree()
	{
		TreeCollection tc = new TreeCollection(this, "remove");
	}	
	
	
	//----------------------------Sessions and Sell Tree----------------------------------------------	
	//Create New Tree
	public void openNewSession()
	{
		Session s = new Session(this, "open");
	}
	
	//Modify Existing Scout
	public void closeSession()
	{
		Session s = new Session(this, "close");
	}
	
	
	//Transaction object created for sell Tree
	public void transaction()
	{
		Transaction t = new Transaction(this);
	}	
	
	
	//Changes the view called by a previous method
	public void swapToView(Scene newScene) 
	{		
		if (newScene == null) 
		{
			System.out.println("MainScreen.swapToView(): Missing view for display");
		}
		
		myStage.setScene(newScene);
		myStage.sizeToScene();
		
		//Center our window
		WindowPosition.placeCenter(myStage);
	}

	//Abstract methods from IView (Leave Blank)
	public Object getState(String arg0) 
	{
		return null;
	}
	public void stateChangeRequest(String arg0, Object arg1) 
	{
		
	}
	public void subscribe(String arg0, IView arg1) 
	{
		
	}
	public void unSubscribe(String arg0, IView arg1)
	{	
		
	}
	
	//Exits the program when called
	public void closeProgram() 
	{
		Platform.exit();
	}
	
	
}