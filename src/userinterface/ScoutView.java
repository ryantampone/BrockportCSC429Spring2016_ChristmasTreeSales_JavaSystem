package userinterface;

//The main scoutView 
import impresario.IModel;
import impresario.IView;
import model.Scout;
import model.ScoutCollection;
import model.Tree;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ScoutView extends View {
	private Label		 		statusLog;
	private Alert				alert;
	private TableView<Scout>	tableOfStudents;
	private TextField 			firstNameTextbox;
	private TextField 			lastNameTextbox;
	private TextField 			middleNameTextbox;
	private TextField 			dateOfBirthTextbox;
	private TextField 			phoneNumberTextbox;
	private TextField 			emailTextbox;
	private TextField 			troopIdTextbox;
	private ComboBox 			statusCombobox;
	private Button 				submitButton;
	private Button 				doneButton;
	protected ResourceBundle	rb;
	
	VBox container = new VBox(10);
	Text titleText = new Text("title");
	//Stuff
	protected Scout myScout;
	protected TableView<ScoutTableModel> tableOfScouts;
	
	Calendar today = Calendar.getInstance();
	String year = String.valueOf(today.get(Calendar.YEAR));
	String month = String.valueOf((today.get(Calendar.MONTH))+1);
	String day = String.valueOf(today.get(Calendar.DAY_OF_MONTH));

	String todayDate = month + "/" + day + "/" + year;

	public ScoutView(IModel scout, String key) 
	{
		super(scout, "ScoutView");
		myScout = (Scout) scout;
		rb = ResourceBundle.getBundle("ScoutViewBundle", MainScreenView.language);
		
		//VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));
		if (key.equals("add"))
			addView();
		/*else if (key.equals("modify"))
			modifyView();
		else if (key.equals("remove"))
			removeView();*/
		myModel.subscribe("LoginError", this);
		container.getChildren().add(createStatusLog("                          "));
		getChildren().add(container);
		getEntryTableModelValues();
	}

//View to add a Scout to the database. 
	private void addView() {
		HBox titleContainer = new HBox();
		
		
		Text titleText = new Text(rb.getString("TitleAddScout"));
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
		
		titleContainer.getChildren().add(titleText);
		
		//titleContainer.setMinWidth(500);
		titleText.setWrappingWidth(500);
		
		
		//titleContainer.setPadding(new Insets(20,200,20,200));
		container.getChildren().add(titleContainer);
		//container.setAlignment(Pos.CENTER_RIGHT);

		GridPane addContainer = new GridPane();
		addContainer.getChildren().add(createSelectContentsForAdd());
		getChildren().add(addContainer);
	}
		
    @SuppressWarnings("unchecked")
	private GridPane createSelectContentsForAdd() {
    	GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
		
		grid.getColumnConstraints().add(new ColumnConstraints(200));
		grid.getColumnConstraints().add(new ColumnConstraints(200));
		grid.getColumnConstraints().add(new ColumnConstraints(55));
		
		
		

		firstNameTextbox = new TextField();
		lastNameTextbox = new TextField();
		middleNameTextbox = new TextField();
		dateOfBirthTextbox = new TextField();
		phoneNumberTextbox = new TextField();
		emailTextbox = new TextField();
		troopIdTextbox = new TextField();

		createHBox(grid, rb.getString("FirstName"), firstNameTextbox, 2);
		createHBox(grid, rb.getString("LastName"), lastNameTextbox, 3);
		createHBox(grid, rb.getString("MiddleName"), middleNameTextbox,4);
		createHBox(grid, rb.getString("DateOfBirth"), dateOfBirthTextbox, 5);
		createHBox(grid, rb.getString("PhoneNumber"), phoneNumberTextbox, 6);
		createHBox(grid, rb.getString("Email"), emailTextbox, 7);
		createHBox(grid, rb.getString("TroopId"), troopIdTextbox, 8);
		
		HBox statusContainer = new HBox(10);
		Label Status = new Label(rb.getString("Status"));
		Status.setTextFill(Color.WHITE);
		statusContainer.getChildren().add(Status);
		statusContainer.setAlignment(Pos.CENTER_RIGHT);
		grid.add(statusContainer, 0, 9);

		ObservableList<String> options = FXCollections.observableArrayList("Active", "Inactive");
		statusCombobox = new ComboBox(options);
		grid.add(statusCombobox, 1, 9);

	  	/* Add Button */
		//createButton(grid, submitButton, "Submit", 9);
		//createButton(grid, doneButton, "Done", 10);
		
		submitButton = new Button(rb.getString("SubmitButton"));
		submitButton.setOnAction(e -> {
			System.out.println(todayDate);
			String fn = firstNameTextbox.getText();
			String ln = lastNameTextbox.getText();
			String mn = middleNameTextbox.getText();
			String dob = dateOfBirthTextbox.getText();
			String phone = phoneNumberTextbox.getText();
			String em = emailTextbox.getText();
			String tID = troopIdTextbox.getText();
			String stat = (String)statusCombobox.getValue();
			String dsu = todayDate;
			myScout.addScout(fn, ln, mn, dob, phone, em, tID, stat, dsu);
			
			//statusLog.setText("Scout inserted successfully");
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(rb.getString("AddMsgTitle"));
			alert.setHeaderText(rb.getString("AddMsgHeader"));
			//alert.setContentText("There is a session that is currently open.\nPlease close the session and try again.");
			alert.show();
			clearFields();
			myScout.done();
		});
		
		doneButton = new Button(rb.getString("DoneButton"));
		doneButton.setOnAction(e -> {
			myScout.done();
		});
		
		grid.add(submitButton, 1, 10);
		grid.add(doneButton, 2, 10);

		return grid;
    }
    
	protected void getEntryTableModelValues()
	{
		
		ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();
		try
		{
			ScoutCollection scoutCollection = (ScoutCollection)myModel.getState("ScoutList");

	 		Vector entryList = (Vector)scoutCollection.getState("Scouts");
			Enumeration entries = entryList.elements();
			System.out.println("Got into 'try'");
			while (entries.hasMoreElements() == true)
			{
				Scout nextScout = (Scout)entries.nextElement();
				Vector<String> view = nextScout.getEntryListView();

				// add this list entry to the list
				ScoutTableModel nextTableRowData = new ScoutTableModel(view);
				tableData.add(nextTableRowData);
				
			}
			
			tableOfScouts.setItems(tableData);
		}
		catch (Exception e) {//SQLException e) {
			//System.out.println("Could not create ScoutCollectionTable");
		}
	}
    
    public void createHBox(GridPane grid, String str, TextField tf, int i) {
		HBox labelContainer = new HBox(10);
		HBox textContainer = new HBox(10);
		labelContainer.setAlignment(Pos.CENTER_RIGHT);
		Label l = new Label(str);
		l.setTextFill(Color.WHITE);
		labelContainer.getChildren().add(l);
		textContainer.getChildren().add(tf);
		grid.add(labelContainer, 0, i);
		grid.add(textContainer, 1, i);
    }

	private Label createStatusLog(String initialMessage) {
		statusLog = new Label(initialMessage);
		statusLog.setTextFill(Color.GREEN);
		statusLog.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		return statusLog;
	}

	//-------------------------------------------------------------
//Method to add a Scout to the database. 
	private void addScout(String fn, String ln, String mn,
			String dob, String phone, String em, String tID,
			String stat, String dsu) {
		Properties props = new Properties();

			props.setProperty("FirstName", fn);
			props.setProperty("LastName", ln);
			props.setProperty("MiddleName", mn);
			props.setProperty("DateOfBirth", dob);
			props.setProperty("PhoneNumber", phone);
			props.setProperty("Email", em);
			props.setProperty("TroopId", tID);
			props.setProperty("Status", stat);
			props.setProperty("DateStatusUpdated", dsu);
			try {
				myModel.stateChangeRequest("add", props);
				statusLog.setText("Scout added successfully!");
				clearFields();
			}
			catch (Exception ex) {
				displayErrorMessage("Error in adding the Scout in the database!");
			}
	}

	public void updateState(String key, Object value) {
		if (key.equals("LoginError") == true)
			displayErrorMessage((String)value);

	}

	private void clearFields() {
		firstNameTextbox.setText("");
		lastNameTextbox.setText("");
		middleNameTextbox.setText("");
		dateOfBirthTextbox.setText("");
		phoneNumberTextbox.setText("");
		emailTextbox.setText("");
		troopIdTextbox.setText("");
		statusCombobox.setPromptText("");
	}
//Error message 
	public void displayErrorMessage(String message) {
		//statusLog.displayErrorMessage(message);
	}

	public void clearErrorMessage() {
		//statusLog.clearErrorMessage();
	}
}

//Old Depreciated Code
/*	//view to remove a scout from the database. 
private void removeView() {
	HBox titleContainer = new HBox();
	titleContainer.setAlignment(Pos.CENTER);
	
	Text titleText = new Text(rb.getString("Remove a Scout"));

	titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	titleText.setWrappingWidth(800);
	titleText.setTextAlignment(TextAlignment.CENTER);
	titleText.setFill(Color.YELLOW);
	container.getChildren().add(titleText);
	
	GridPane removeContainer = new GridPane();
	removeContainer.getChildren().add(createSelectContentsForRemove());
	getChildren().add(removeContainer);
}*/


/*

private void modifyView() {
	//System.out.println("modifyView : TO DO");
	HBox titleContainer = new HBox();
	titleContainer.setAlignment(Pos.CENTER);
	
	titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	titleText.setTextAlignment(TextAlignment.CENTER);
	titleText.setFill(Color.YELLOW);
	container.getChildren().add(titleText);
	
	// container.getChildren().add(createSelectContentsForModify());
	// getChildren().add(container);
}
*/
/*private Node createTitle() {
	HBox titleContainer = new HBox();
	titleContainer.setAlignment(Pos.CENTER);
	
	Text titleText = new Text("New Scout");
	titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	titleText.setTextAlignment(TextAlignment.CENTER);
	titleText.setFill(Color.YELLOW);
	return titleText;
}*/
