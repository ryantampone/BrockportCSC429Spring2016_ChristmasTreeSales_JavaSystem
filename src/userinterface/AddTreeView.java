package userinterface;

//Import Statements
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Properties;

import userinterface.MessageView;
import impresario.IModel;
import userinterface.View;
import model.Tree;

public class AddTreeView extends View
{
	//variables
	protected Tree myTree;
	
	//GUI Components
	protected Label addTreeTypeTitle;
	
	protected TextField notes;
	protected TextField status;
	protected TextField dateStatusUpdated;
	
	protected ComboBox treeTypeSelect;
	
	protected Button addTreeButton;
	protected Button doneButton;
	
	protected MessageView statusLog;
	
	
	public AddTreeView(Tree tree) 
	{
		super(tree, "AddTreeView");
		myTree = tree;
		
		//Create container for showing our GUI components
		VBox container = new VBox (10);
		container.setPadding(new Insets(15, 5, 5, 5));
		
		//Adds a title for the panel
		container.getChildren().add(createTitle());
		
		//Create GUI Components, and add them to this container
		container.getChildren().add(createFormContent());
		container.getChildren().add(createStatusLog("             "));
		getChildren().add(container);
	}


	private Node createStatusLog(String string) 
	{
		return null;
	}


	private Node createFormContent() 
	{
		VBox formContainer = new VBox(15);
		VBox vbox = new VBox(15);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(30,10, 20, 10));
		
		notes = new TextField("Enter Notes");
		status = new TextField("Enter Status");
		dateStatusUpdated = new TextField("Enter Date of Last Status Update");
		
		addTreeButton = new Button ("Add Tree");
		doneButton = new Button ("Done");
		
		//Continue from here ****************************************************
		
		return null;
	}


	private Node createTitle() 
	{
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);
		
		Text titleText = new Text("Book Information");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKOLIVEGREEN);
		container.getChildren().add(titleText);
			
		return container;
	}


	public void updateState(String key, Object value) 
	{
		// TODO Auto-generated method stub
	}

}
