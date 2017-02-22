package userinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
//Import Statements
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import database.*;
import java.util.Calendar;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import userinterface.MessageView;
import impresario.IModel;
import userinterface.View;
import model.Session;
import model.Transaction;
//import model.Transaction;
import model.Tree;
import model.TreeType;
public class TransactionView extends View
{
	//GUI Components
	protected Text titleText;
	
	//AutoPopulated Fields (Non-editable)
	private TextField treeType;
	private TextField date;
	private TextField price;
	
	//Fields for user information
	private TextField barcode;
	protected TextField firstName;
	protected TextField lastName;
	protected TextField email;
	protected TextField phoneNumber;
	protected ComboBox paymentType;
	
	//Other components
	protected Button submitButton;
	protected Button backButton;
	protected Button searchButton;
	private Label statusLog;
	private Alert alert;
	
	//Objects
	protected Transaction myTransaction;
	protected Tree myTree;
	protected Session mySession;
	protected String sessionID;
	protected TreeType myTreeType;
	protected ResourceBundle rb;
	private static final String myTableName = "Tree";
	protected String query;
	
	//Calendar method that will auto fill date box. 
	Calendar today = Calendar.getInstance();
	String year = String.valueOf(today.get(Calendar.YEAR));
	String month = String.valueOf((today.get(Calendar.MONTH))+1);
	String day = String.valueOf(today.get(Calendar.DAY_OF_MONTH));
	String todayDate = month + "/" + day + "/" + year;
	
	
	public TransactionView(IModel transaction)
	{
		super(transaction, "TransactionView");
		myTransaction = (Transaction) transaction;
		
		rb = ResourceBundle.getBundle("TransactionViewBundle", MainScreenView.language);
		
		//Creates the container for showing GUI stuffs
		VBox container = new VBox(10);
		container.setPadding(new Insets(15,5,5,5));
    	container.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
    	container.setAlignment(Pos.CENTER);
    	
    	//Container for holding the vertical vBoxes
    	HBox formContainer = new HBox(10);
    	formContainer.setPadding(new Insets(15, 5, 5, 5));
    	formContainer.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
    	formContainer.setAlignment(Pos.CENTER);
		
    	createTitleFormat();
    	
		//Create GUI Components, and add them to this container
		container.getChildren().add(titleText);
		
		formContainer.getChildren().add(createContentsLeft());
		formContainer.getChildren().add(createContentsRight());
		container.getChildren().add(formContainer);
		
		container.getChildren().add(createContentsButtons());
		container.getChildren().add(createStatusLog("             "));
		getChildren().add(container);
	}
	

	//----------------------------------------------------------
	//Creates the title of our GUI
	//----------------------------------------------------------
	private void createTitleFormat() 
	{
		titleText = new Text(rb.getString("Title"));
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		titleText.setWrappingWidth(700);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
	}
	
	
	//-------------------------------------
	//Creates the vBox the left side of our column 
	//Barcode TreeType,price, Date
	//--------------------------------------
	private VBox createContentsLeft() 
	{
    	VBox vboxLeft = new VBox(2);
    	vboxLeft.setAlignment(Pos.BASELINE_LEFT);
    	vboxLeft.setPadding(new Insets(25, 25, 25, 25));
    	vboxLeft.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
	
    	
    	Label barcodeLabel = new Label(rb.getString("EnterBarcode"));
    	//Label barcodeLabel = new Label("EnterBarcode");
    	barcodeLabel.setTextFill(Color.WHITE);
    	barcodeLabel.setAlignment(Pos.CENTER);
    	barcodeLabel.setPadding(new Insets(0, 0, 10, 0));
    	barcode = new TextField();
    	
    	barcode.setOnKeyPressed(new EventHandler<KeyEvent>() {
    	    @Override
    	    public void handle(KeyEvent keyEvent) {
    	        if (keyEvent.getCode() == KeyCode.ENTER)  {
    	            String text = barcode.getText();

    	            try {
    		    		Properties myProps = new Properties();
    		    		String bc = (String)barcode.getText();
    		    		myTransaction.addTrans(bc);
    		    		treeType.setText(myTransaction.getTreeInfo().getProperty("Description"));
    		    		price.setText(myTransaction.getTreeInfo().getProperty("Cost"));
    		    		System.out.println(myTransaction.getTreeInfo()); 
    		    		String barCode = barcode.getText();
    	    		} catch (NullPointerException exc) {
    	    			alert = new Alert(AlertType.INFORMATION);
    	    			alert.setTitle(rb.getString("SellTreeBarcodeTitleMsg"));
    	    			alert.setHeaderText(rb.getString("SellTreeBarcodeHeaderMsg"));
    	    			alert.setContentText(rb.getString("SellTreeBarcodeContentMsg"));
    	    			alert.show();
    	    		}
    	        }
    	    }
    	});
    	
    	
		searchButton = new Button(rb.getString("Search"));
    	//searchButton = new Button("Search");
    	searchButton.setOnAction(e -> 
    	{
    		try {
	    		Properties myProps = new Properties();
	    		String bc = (String)barcode.getText();
	    		myTransaction.addTrans(bc);
	    		treeType.setText(myTransaction.getTreeInfo().getProperty("Description"));
	    		price.setText(myTransaction.getTreeInfo().getProperty("Cost"));
	    		System.out.println(myTransaction.getTreeInfo()); 
	    		String barCode = barcode.getText();
    		} catch (NullPointerException exc) {
    			alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle(rb.getString("SellTreeBarcodeTitleMsg"));
    			alert.setHeaderText(rb.getString("SellTreeBarcodeHeaderMsg"));
    			alert.setContentText(rb.getString("SellTreeBarcodeContentMsg"));
    			alert.show();
    		}
		});

    	Label treeTypeLabel = new Label(rb.getString("TreeType"));
    	//Label treeTypeLabel = new Label("TreeType");
    	treeTypeLabel.setTextFill(Color.WHITE);
    	treeTypeLabel.setAlignment(Pos.CENTER);
    	treeTypeLabel.setPadding(new Insets(0, 0, 10, 0));
		treeType = new TextField();
		treeType.setDisable(true);
    	
    	
    	Label priceLabel = new Label(rb.getString("Price"));
    	//Label priceLabel = new Label("Price");
    	priceLabel.setTextFill(Color.WHITE);
    	priceLabel.setAlignment(Pos.CENTER);
    	priceLabel.setPadding(new Insets(0, 0, 10, 0));
    	price = new TextField();
		price.setDisable(true);
		
		Label dateLabel = new Label(rb.getString("Date"));
    	//Label dateLabel = new Label("Date");
    	dateLabel.setTextFill(Color.WHITE);
    	dateLabel.setAlignment(Pos.CENTER);
    	dateLabel.setPadding(new Insets(0, 0, 10, 0));
		date = new TextField();
		date.setText(todayDate);
		date.setDisable(true);
	
		
		vboxLeft.getChildren().addAll(barcodeLabel, barcode, searchButton, treeTypeLabel, treeType, priceLabel, price, dateLabel, date);	
		return vboxLeft;
    }
	
	
	//----------------------------------------------------------
	//Creates the vBox for our right column
	//Name, Email, Phone, PaymentType
	//----------------------------------------------------------
	private VBox createContentsRight() 
	{
    	VBox vboxRight = new VBox(2);
    	vboxRight.setAlignment(Pos.BASELINE_LEFT);
    	vboxRight.setPadding(new Insets(25, 25, 25, 25));
    	vboxRight.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
    	
    	Label firstNameLabel = new Label(rb.getString("FirstName"));
    	//Label firstNameLabel = new Label("FirstName");
    	firstNameLabel.setTextFill(Color.WHITE);
    	firstNameLabel.setAlignment(Pos.CENTER);
    	firstNameLabel.setPadding(new Insets(0, 0, 10, 0));
    	firstName = new TextField();
    	
    	/*Label lastNameLabel = new Label(rb.getString("LastName"));
    	//Label lastNameLabel = new Label("LastName");
    	lastNameLabel.setTextFill(Color.WHITE);
    	lastNameLabel.setAlignment(Pos.CENTER);
    	lastNameLabel.setPadding(new Insets(0, 0, 10, 0));
    	lastName = new TextField();*/
    	
    	Label emailLabel = new Label(rb.getString("Email"));
    	//Label emailLabel = new Label("Email");
    	emailLabel.setTextFill(Color.WHITE);
    	emailLabel.setAlignment(Pos.CENTER);
    	emailLabel.setPadding(new Insets(0, 0, 10, 0));
    	email = new TextField();
    	
    	Label phoneLabel = new Label(rb.getString("Phone"));
    	//Label phoneLabel = new Label("Phone");
    	phoneLabel.setTextFill(Color.WHITE);
    	phoneLabel.setAlignment(Pos.CENTER);
    	phoneLabel.setPadding(new Insets(0, 0, 10, 0));
    	phoneNumber = new TextField();
    	
    	Label paymentLabel = new Label(rb.getString("SelectPaymentType"));
    	//Label paymentLabel = new Label("SelectPaymentType");
    	paymentLabel.setTextFill(Color.WHITE);
    	paymentLabel.setAlignment(Pos.CENTER);
    	paymentLabel.setPadding(new Insets(0, 0, 10, 0));
    	ObservableList<String> payments = FXCollections.observableArrayList(rb.getString("Cash"), rb.getString("Check"));
    	paymentType = new ComboBox(payments);
    	
	
    	vboxRight.getChildren().addAll(firstNameLabel, firstName, emailLabel, email, phoneLabel, phoneNumber, paymentLabel, paymentType);	
		return vboxRight;
    }	
	
	
	
	//----------------------------------------------------------
	//Creates the hBox for our buttons
	//----------------------------------------------------------
	private GridPane createContentsButtons() 
	{
/*    	HBox hbox = new HBox();
    	hbox.setAlignment(Pos.CENTER);
    	hbox.setPadding(new Insets(25, 25, 25, 25));
    	hbox.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");*/
    	
    	GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
	
    	
    	submitButton = new Button(rb.getString("Submit"));
		//submitButton = new Button("Submit");
		submitButton.setOnAction(e -> {
		// Close the currently open session
		String name = firstName.getText();
		String eMail = email.getText();
		String phone = phoneNumber.getText();
		String payment = (String)paymentType.getValue();
		String bc = barcode.getText();
		String cost = price.getText();
		String day = date.getText();
		
		
		//persistantState.setProperty("SessionId", myTransaction.getSessionId());
		sessionID = myTransaction.getSessionId();
		myTransaction.stateChangeRequest("SessionId", sessionID);
		
		myTransaction.addTransaction(name, eMail, phone, payment, bc, cost, day, sessionID);
		
		statusLog.setText("Transaction Recorded");
		
			
			
		/*String barCode = barcode.getText();
		boolean unique = myTransaction.checkBarcode(barCode);
		
		if(unique == true)
		{
			boolean treeStatus = myTransaction.checkTreeStatus(barCode);
				if(treeStatus == true)
				{
					//Call Transaction View
					myTransaction.createNewTransaction();
					
				}
				else if(treeStatus == false)
				{
					statusLog.setText("Tree Already Sold, Enter Different Barcode");
				}
		}
		else 
		{
			statusLog.setText("Barcode Not found");
		}*/
			
			
			
			clearFields();
		});
		
		backButton = new Button(rb.getString("Back"));
		//backButton = new Button("Back");
		backButton.setOnAction(e -> {
			myTransaction.done();
		});
	
		grid.add(submitButton, 0, 0);
		grid.add(backButton, 2, 0);
		
		return grid;
    }	
	

	
	
	
	
	
	
	
	
	//----------------------------------------------------------
	//Clears the barcode textfield
	//----------------------------------------------------------
	private void clearFields()
	{
		barcode.setText("");
	}

	
	private Label createStatusLog(String initialMessage)
	{
		statusLog = new Label(initialMessage);
		statusLog.setTextFill(Color.GREEN);
		statusLog.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		return statusLog;
	}

	
	//Update State in database.. auto generated. 
	public void updateState(String key, Object value) 
	{
		// TODO Auto-generated method stub
	}



}
