package userinterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.MainScreen;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import impresario.IModel;
import model.Scout;
import model.Session;
import model.Tree;
import model.TreeType;
import model.MainScreen;


public class MainScreenView extends View 
{
	//Declare internationalizing components here
	protected RadioButton languageSelect;
	public static Locale language = new Locale("en", "US");
	protected Preferences p;
	protected ResourceBundle header;
	protected ResourceBundle buttonText;
	
	//Declare GUI Components Here
	protected MainScreen mainScreen;
	
	protected Text titleText;

	protected Button done;
	
	protected Button addScoutButton;
	protected Button modifyScoutButton;
	protected Button removeScoutButton;
	
	protected Button addTreeTypeButton;
	protected Button modifyTreeTypeButton;

	protected Button addTreeButton;
	protected Button modifyTreeButton;
	protected Button removeTreeButton;
	
	protected Button openSessionButton;
	protected Button closeSessionButton;
	protected Button sellTreeButton;
	
	
	//----------------------------------------------------------
	//Constructor for MainScreenView -- takes a model object
	//----------------------------------------------------------
	public MainScreenView(MainScreen main) 
	{
		super(main, "MainScreenView");
		mainScreen = main;
				
		//retrieves the button bundle from the folder to print the correct
		//button label in the corresponding language. 
		buttonText = ResourceBundle.getBundle("ButtonsBundle",language);
        header = ResourceBundle.getBundle("HeaderBundle",language);

        p = Preferences.userNodeForPackage(MainScreenView.class);
        p.put("language", "en");
		
		// Create the container for showing contents
		VBox container = new VBox(10);					//VBox with a spacing of 10
		HBox buttonContainer = new HBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));	//Padding
		buttonContainer.setPadding(new Insets(15, 5, 5, 5));
		//Sets background color of vbox
		container.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
		container.setAlignment(Pos.CENTER);
		buttonContainer.setAlignment(Pos.CENTER);
		createTitleFormat();

		//We write methods below to create our things, when finished we must add them as seen below
		container.getChildren().add(titleText);	
		buttonContainer.getChildren().add(scoutButtons());
		buttonContainer.getChildren().add(treeTypeButtons());
		buttonContainer.getChildren().add(treeButtons());
		container.getChildren().add(buttonContainer);
		//container.getChildren().add(createFormContent());
		container.getChildren().add(sessionsAndSellButtons());
		container.getChildren().add(selectDone());
		container.getChildren().add(selectLanguage());


		getChildren().add(container);
     	reloadGUIWithLangChange();
	}
	
	
	//----------------------------------------------------------
	//Creates the title of our GUI
	//----------------------------------------------------------	
	private void createTitleFormat() 
	{
		titleText = new Text("Title");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		titleText.setWrappingWidth(700);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
	}
	
	
	//----------------------------------------------------------
	//Creates the Scout Buttons of our GUI
	//----------------------------------------------------------
	private GridPane scoutButtons()
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		addScoutButton = new Button("addS");
		modifyScoutButton = new Button("modifyS");
		removeScoutButton = new Button("removeS");
		
		addScoutButton.setOnAction(e -> {
			mainScreen.createNewScout();
		});
		
		modifyScoutButton.setOnAction(e -> {
			mainScreen.modifyScout();
		});
		
		removeScoutButton.setOnAction(e -> {
			mainScreen.removeScout();
		});
		
		createVBox(grid, addScoutButton, 0);
		createVBox(grid, modifyScoutButton, 1);
		createVBox(grid, removeScoutButton, 2);
		
		return (grid);
	}
	
	
	//----------------------------------------------------------
	//Creates the TreeButtons of our GUI
	//----------------------------------------------------------
	private GridPane treeButtons()
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		addTreeButton = new Button("addT");
		modifyTreeButton = new Button("modifyT");
		removeTreeButton = new Button("removeT");
		
		addTreeButton.setOnAction(e -> {
			mainScreen.createNewTree();
		});
		
		modifyTreeButton.setOnAction(e -> {
			mainScreen.modifyTree();
		});
		
		removeTreeButton.setOnAction(e -> {
			mainScreen.removeTree();
		});
		
		createVBox(grid, addTreeButton, 0);
		createVBox(grid, modifyTreeButton, 1);
		createVBox(grid, removeTreeButton, 2);
		
		return (grid);
	}
	
	
	//----------------------------------------------------------
	//Creates the TreeTypeButtons of our GUI
	//----------------------------------------------------------
	private GridPane treeTypeButtons()
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		addTreeTypeButton = new Button("addS");
		modifyTreeTypeButton = new Button("modifyS");
		
		addTreeTypeButton.setOnAction(e -> {
			mainScreen.createNewTreeType();
		});
		
		modifyTreeTypeButton.setOnAction(e -> {
			mainScreen.modifyTreeType();
		});
		
		createVBox(grid, addTreeTypeButton, 0);
		createVBox(grid, modifyTreeTypeButton, 1);
		
		return (grid);
	}
	
	//----------------------------------------------------------
	//Creates the Session Buttons and Sell Tree Button
	//----------------------------------------------------------
	private GridPane sessionsAndSellButtons()
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		//Buttons
		openSessionButton = new Button("openSession");
		closeSessionButton = new Button("closeSession");
		sellTreeButton = new Button("sellTree");
		
		openSessionButton.setOnAction(e -> {
			mainScreen.openNewSession();
		});
		
		closeSessionButton.setOnAction(e -> {
			mainScreen.closeSession();
		});
		
		sellTreeButton.setOnAction(e -> {
			mainScreen.transaction();
		});
		
		createHBox(grid, openSessionButton, 0);
		createHBox(grid, sellTreeButton, 1);
		createHBox(grid, closeSessionButton, 2);
		
		return (grid);
	}
	
	
	public void createHBox(GridPane grid, Button bt, int i)
    {
		HBox formContainer = new HBox(10);
		formContainer.setAlignment(Pos.CENTER);
		formContainer.getChildren().add(bt);
		grid.add(formContainer, i, 1);
    }
	
	public void createVBox(GridPane grid, Button bt, int i)
    {
		VBox formContainer = new VBox(10);
		formContainer.setAlignment(Pos.CENTER);
		formContainer.getChildren().add(bt);
		grid.add(formContainer, 1, i);
    }
	
	//----------------------------------------------------------
	//Creates the language select buttons and calls the method to swap text
	//----------------------------------------------------------	
	private Node selectLanguage() 
	{
		HBox containerLang = new HBox();
		containerLang.setPadding(new Insets(10, 0, 10, 0));
		containerLang.setAlignment(Pos.CENTER);
		
/*		//Done Button to Exit Program
		done = new Button("Exit");
		done.setOnAction(e -> {
			mainScreen.closeProgram();
		});*/
		
		RadioButton english = new RadioButton("English   ");
		RadioButton french = new RadioButton("Fran\u00E7ais   ");
		english.setStyle("-fx-text-fill: white;");
		french.setStyle("-fx-text-fill: white;");
		//Adds radio buttons to a group
		final ToggleGroup group = new ToggleGroup();
		english.setToggleGroup(group);
		english.setSelected(true);		
		english.setOnAction(new EventHandler<ActionEvent>() 
		{
 		     @Override
 		     public void handle(ActionEvent e) 
 		     {
 		    	language = new Locale("en", "US");
 		    	buttonText = ResourceBundle.getBundle("ButtonsBundle",language);
                header = ResourceBundle.getBundle("HeaderBundle",language);
                p = Preferences.userNodeForPackage(MainScreenView.class);
                p.put("language", "en");
 		    	reloadGUIWithLangChange();
      	     }
		});
		
		french.setToggleGroup(group);
		french.setSelected(false);
		french.setOnAction(new EventHandler<ActionEvent>() 
		{
 		     @Override
 		     public void handle(ActionEvent e) 
 		     {
 		    	language = new Locale("fr", "Fr");
 		    	buttonText = ResourceBundle.getBundle("ButtonsBundle",language);
                header = ResourceBundle.getBundle("HeaderBundle",language);
                p = Preferences.userNodeForPackage(MainScreenView.class);
                p.put("language", "fr");
 		    	reloadGUIWithLangChange();
      	     }
		});
		containerLang.getChildren().addAll(english, french);		
		return containerLang;
		
	}
	
	
	//----------------------------------------------------------
	//Creates the Exit  button
	//----------------------------------------------------------	
	private Node selectDone() 
	{
		HBox containerDone = new HBox();
		containerDone.setPadding(new Insets(10, 0, 10, 0));
		containerDone.setAlignment(Pos.CENTER);
		
		//Done Button to Exit Program
		done = new Button("Exit");
		done.setOnAction(e -> {
			mainScreen.closeProgram();
		});
		
		containerDone.getChildren().addAll(done);
		return containerDone;
	}
	
	//----------------------------------------------------------
	//Abstract Methods inherited from View - Leave Blank
	//----------------------------------------------------------	
	public void updateState(String arg0, Object arg1) 
	{
		
	}
	
	//----------------------------------------------------------
	//Method that updates the GUI with the correct language dependent text
	//----------------------------------------------------------
	private void reloadGUIWithLangChange()
	{
		//Sets the text of buttons and titles to the selected language

		addScoutButton.setText(buttonText.getString("addS"));
		modifyScoutButton.setText(buttonText.getString("modifyS"));
		removeScoutButton.setText(buttonText.getString("removeS"));
		
		addTreeButton.setText(buttonText.getString("addT"));
		modifyTreeButton.setText(buttonText.getString("modifyT"));
		removeTreeButton.setText(buttonText.getString("removeT"));
		
		addTreeTypeButton.setText(buttonText.getString("addTT"));
		modifyTreeTypeButton.setText(buttonText.getString("modifyTT"));
		
		openSessionButton.setText(buttonText.getString("openSession"));
		sellTreeButton.setText(buttonText.getString("sellTree"));
		closeSessionButton.setText(buttonText.getString("closeSession"));
		
		titleText.setText(header.getString("Title"));
		done.setText(buttonText.getString("Exit"));
	}
}