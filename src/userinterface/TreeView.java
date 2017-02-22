package userinterface;

import model.*;
import impresario.IModel;
import impresario.IView;

import java.util.Calendar;
import java.util.Properties;
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
import java.util.ResourceBundle;
import java.util.Vector;

public class TreeView extends View {
	private Button 				submitButton;
	private Button 				backButton;
	private MessageView 		statusLog;
	private Alert				alert;
	private ComboBox 			statusBox;
	private TextField			notes;
	private TextField			barcode;
	private String				dateStatusUpdated;
	protected Tree				myTree;
	private Text				titleText;
	private ComboBox			barcodePrefix;
	protected ResourceBundle	rb;
	protected TreeTypeCollection myTreeTypeCollection = 
			new TreeTypeCollection();
	
	public TreeView(IModel tree, String key) 
	{
		super(tree, "TreeView");
		myTree = (Tree)tree;
		//Locale lang = new Locale("en", "US");
		rb = ResourceBundle.getBundle("TreeViewBundle", MainScreenView.language);
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));
		container.setAlignment(Pos.CENTER);
		createTitleFormat(key);
		container.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
		
		container.getChildren().add(titleText);
		container.getChildren().add(createSelectContents(key));
		getChildren().add(container);
		myModel.subscribe("LoginError", this);
		container.getChildren().add(createStatusLog("                          "));
	}
	
	private void setDateModified()
	{
		Calendar today = Calendar.getInstance();
		String year = String.valueOf(today.get(Calendar.YEAR));
		String month = String.valueOf((today.get(Calendar.MONTH))+1);
		String day = String.valueOf(today.get(Calendar.DAY_OF_MONTH));

		dateStatusUpdated = new String(month + "/" + day + "/" + year);
	}
	
	private void createTitleFormat(String key)
	{
		
		
		if (key.equals("add"))
			titleText = new Text(rb.getString("titleAddTree"));
		else if (key.equals("modify"))
			titleText = new Text(rb.getString("titleModifyTree"));
		
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		titleText.setWrappingWidth(700);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
	}
	
    @SuppressWarnings("unchecked")
	private GridPane createSelectContents(String key) 
    {
		GridPane grid = new GridPane();
		createBody(grid);
		submitButton = new Button(rb.getString("submit"));
		grid.add(submitButton, 1, 6);
		submitButton.setOnAction(e -> {
			String bc = barcode.getText();
			String pf = myTreeTypeCollection.getIdFromBarcodePrefix((String)barcodePrefix.getValue());
			String nts = notes.getText();
			String stat = (String)statusBox.getValue();
			setDateModified();
			String dsu = dateStatusUpdated;
			updateTree(bc, pf, nts, stat, dsu, key);
			myTree.done();
		});
		backButton = new Button(rb.getString("back"));
		grid.add(backButton, 2, 6);
		backButton.setOnAction(e -> {
			myTree.done();
		});
		return (grid);
    }
	
	private void updateTree(String bc, String pf, String nts, String stat, String dsu, String key) {
		Properties props = new Properties();
		
		if (stat == null)
			System.out.println("stats == null");
		props.setProperty("Barcode", bc);
		props.setProperty("TreeType", pf);
		props.setProperty("Notes", nts);
		props.setProperty("Status", stat);
		props.setProperty("DateStatusUpdated", dsu);
		try{
			myModel.stateChangeRequest(key, props);
			//statusLog.displayMessage("Tree added successfully!");
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(rb.getString("AddMsgTitle"));
			alert.setHeaderText(rb.getString("AddMsgHeader"));
			//alert.setContentText("There is a session that is currently open.\nPlease close the session and try again.");
			alert.show();
			clearFields();
		}
		catch (Exception ex){
			System.out.println("Update failed..." + ex.getCause());
			displayErrorMessage("Error in adding the Tree in the database!");
		}
	}
	
	private void createBody(GridPane grid) {
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		//TextFields
		barcode = new TextField();
		notes = new TextField();
		
		//Creates an HBox for the label and TextFields
		createHBox(grid, rb.getString("barcode"), barcode, 1);
		createHBox(grid, rb.getString("notes"), notes, 2);
		
		ObservableList<String> options = FXCollections.observableArrayList("Available", "Sold", "Damaged");
		statusBox = new ComboBox(options);
		
		createComboHBox(grid, "Status", statusBox, 3);
		
		//Populates ComboBox for Tree Type Barcode Prefixes
		myTreeTypeCollection.retriveAllTreeType();
		Vector<String> prefixOptions = myTreeTypeCollection.treePrefixes();
		ObservableList<String> options2 = FXCollections.observableArrayList(prefixOptions);
		barcodePrefix = new ComboBox(options2);
		
		createComboHBox(grid, rb.getString("barCodePrefix"), barcodePrefix, 4);
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
	
	private void createComboHBox(GridPane grid, String str, ComboBox cb, int i) {
		HBox labelContainer = new HBox(10);
		HBox comboBoxContainer = new HBox(10);
		labelContainer.setAlignment(Pos.CENTER_RIGHT);
		Label l = new Label(str);
		l.setTextFill(Color.WHITE);
		labelContainer.getChildren().add(l);
		comboBoxContainer.getChildren().add(cb);
		grid.add(labelContainer, 0, i);
		grid.add(comboBoxContainer, 1, i);
	}

	private MessageView createStatusLog(String initialMessage) {
		statusLog = new MessageView(initialMessage);
		return statusLog;
	}

	public void updateState(String key, Object value) {
		if (key.equals("LoginError") == true)
			displayErrorMessage((String)value);
	}

	private void clearFields() {
		notes.setText("");
		barcode.setText("");
		statusBox.setPromptText("");
		barcodePrefix.setPromptText("");
	}

	public void displayErrorMessage(String message) {
		statusLog.displayErrorMessage(message);
	}

	public void clearErrorMessage() {
		statusLog.clearErrorMessage();
	}
}
