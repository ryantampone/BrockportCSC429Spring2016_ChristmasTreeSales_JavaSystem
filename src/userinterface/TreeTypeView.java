package userinterface;
import model.TreeType;
import model.MainScreen;

import impresario.IModel;
import impresario.IView;

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
import java.util.ResourceBundle;

//class to created our TreeTypeView gui for the user. 
public class TreeTypeView extends View 
{	
	private Button 				submitButton;
	private Button 				doneButton;
	private MessageView 		statusLog;
	private Alert				alert;
	private ComboBox 			statusBox;
	private TextField 			treeTypeDescription;
	private TextField 			cost;
	private TextField 			barcodePrefix;
	private MainScreen			ms;
	protected ResourceBundle	rb;
	
	public TreeTypeView(IModel treeType) 
	{
		super(treeType, "TreeTypeView");
		VBox container = new VBox(10);
		rb = ResourceBundle.getBundle("TreeTypeView", MainScreenView.language);
		//Sets background color of vbox
		container.setStyle("-fx-background-style: solid;"
	           + "-fx-background-color: #2B2B2B");
		
		//TODO interface stuff here
		container.setPadding(new Insets(15, 5, 5, 5));
		container.setAlignment(Pos.CENTER);
		container.getChildren().add(createTitle());
		container.getChildren().add(createForm());
		getChildren().add(container);
		
		myModel.subscribe("LoginError", this);
		submitButton = createButton(submitButton, rb.getString("submit"), 0);
		doneButton = createButton(doneButton, rb.getString("done"), 1);
		
		HBox btnContainer = new HBox(50);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().add(submitButton);
		btnContainer.getChildren().add(doneButton);
		container.getChildren().add(btnContainer);
		container.getChildren().add(createStatusLog("                          "));
	}

	private Node createForm() 
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		treeTypeDescription = new TextField();
		cost = new TextField();
		barcodePrefix = new TextField();
		
		createHBox(grid, rb.getString("treeTypeDescription"), treeTypeDescription, 0);
		createHBox(grid, rb.getString("cost"), cost, 1);
		createHBox(grid, rb.getString("barcodePrefix"), barcodePrefix, 2);

		/* Add Button */
		return grid;
	}
    
    private Button createButton(Button button, String nameButton, Integer pos)
	{
		button = new Button(nameButton);
		button.setId(Integer.toString(pos));
 		button.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
								 	     	processAction(e);
            	     }
        	});

		return button;
	}

	public void createHBox(GridPane grid, String str, TextField tf, int i)
    {
		HBox formContainer = new HBox(10);
		formContainer.setAlignment(Pos.CENTER_RIGHT);
		Label s = new Label(str);
		s.setTextFill(Color.WHITE);
		formContainer.getChildren().add(s);
		formContainer.getChildren().add(tf);
		grid.add(formContainer, 1, i);
    }

	private Node createTitle()
	{
		Text titleText = new Text(rb.getString("newTreeType"));

		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
		return titleText;
	}


	private MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);
		return statusLog;
	}

	private boolean formIsCorrect()
	{
		String tTypeDescription = treeTypeDescription.getText();
		String tcost = cost.getText();
		String tbarcodePrefix = barcodePrefix.getText();

		if (tTypeDescription.length() > 25)
		{
			displayErrorMessage("Type description is too long. (25 character maximum)");
			treeTypeDescription.requestFocus();
		}
		else if (tTypeDescription.length() == 0)
		{
			displayErrorMessage("Type description is required");
			treeTypeDescription.requestFocus();
		}
		else if (tcost.length() > 20)
		{
			displayErrorMessage("The cost must be less than 20 numbers");
			cost.requestFocus();
		}
		else if (tcost.length() == 0)
		{
			displayErrorMessage("The cost is required");
			cost.requestFocus();
		}
		else if(!isNumeric(tcost))
		{
			displayErrorMessage("the cost must be numeric only");
			cost.requestFocus();
		}
		else if (tbarcodePrefix.length() != 2)
		{
			displayErrorMessage("The bar code prefix must contain two character only");
			barcodePrefix.requestFocus();
		}
		else
			return true;
		return false;
	}

	public void processAction(Event evt)
	{
		Object source = evt.getSource();
		Button clickedBtn = (Button) source;

		if (clickedBtn.getId().equals("1") == true){
			myModel.stateChangeRequest("Done", null);
		}
		if (clickedBtn.getId().equals("0") == true && formIsCorrect())
		{
			addTreeType(treeTypeDescription.getText(), cost.getText(), barcodePrefix.getText());
			myModel.stateChangeRequest("Done", null);
		}
	}

	private boolean addTreeType(String treeTypeDescription, String cost, String barcodePrefix)
	{
			Properties props = new Properties();

			props.setProperty("Description", treeTypeDescription);
			props.setProperty("Cost", cost);
			props.setProperty("BarcodePrefix", barcodePrefix);
			try{
				myModel.stateChangeRequest("add", props);
				//statusLog.displayMessage("Tree Type added successfully!");
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(rb.getString("AddMsgTitle"));
				alert.setHeaderText(rb.getString("AddMsgHeader"));
				//alert.setContentText("There is a session that is currently open.\nPlease close the session and try again.");
				alert.show();
				clearFields();
			}
			catch (Exception ex)
				{
					System.out.println("Update failed..." + ex.getCause());
					displayErrorMessage("Error in adding the TreeType in the database!");
				}
		return false;
	}
	
	private void clearFields()
	{
		treeTypeDescription.setText("");
		cost.setText("");
		barcodePrefix.setText("");
	}

	public void updateState(String key, Object value)
	{

		// STEP 6: Be sure to finish the end of the 'perturbation'
		// by indicating how the view state gets updated.
		// if (key.equals("LoginError") == true)
		// {
		// 	// display the passed text
		// 	displayErrorMessage((String)value);
		// }
	}

	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	public static boolean isNumeric(String str)  
	{	
 	 try	
		{	
			double d = Double.parseDouble(str);  
 	 	}  
  	catch(NumberFormatException nfe)  
  		{  
			return false;  
  		}
  	return true;  
	}
}