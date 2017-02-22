// package userinterface;
// import model.TreeType;
// import model.MainScreen;

// import impresario.IModel;
// import impresario.IView;

// import java.util.Properties;

// import javafx.event.Event;
// import javafx.event.ActionEvent;
// import javafx.event.EventHandler;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.control.ComboBox;
// import javafx.collections.ObservableList;
// import javafx.collections.FXCollections;
// import javafx.scene.Node;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.TextField;
// import javafx.scene.layout.GridPane;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.VBox;
// import javafx.scene.paint.Color;
// import javafx.scene.text.Font;
// import javafx.scene.text.FontWeight;
// import javafx.scene.text.Text;
// import javafx.scene.text.TextAlignment;
// import javafx.stage.Stage;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView;
// import javafx.scene.control.SelectionMode;
// import javafx.scene.control.cell.PropertyValueFactory;

// public class TreeTypeView extends View 
// {	
// 	private MessageView 		statusLog;
// 	private ComboBox 			statusBox;
	
// 	public TreeTypeView(IModel treeType) {
// 		super(treeType, "TreeTypeView");
// 		VBox container = new VBox(10);
		
// 		//Sets background color of vbox
// 		container.setStyle("-fx-background-style: solid;"
//                 + "-fx-background-color: navy");
		
// 		//TODO interface stuff here
// 		container.setPadding(new Insets(15, 5, 5, 5));
// 		container.getChildren().add(createTitle());
// 		getChildren().add(container);
		
// 		myModel.subscribe("LoginError", this);
// 		container.getChildren().add(createStatusLog("                          "));
// 	}

// 	private Node createTitle()
// 	{
// 		Text titleText = new Text("New TreeType");

// 		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
// 		titleText.setTextAlignment(TextAlignment.CENTER);
// 		titleText.setFill(Color.DARKGREEN);
// 		return titleText;
// 	}


// 	private MessageView createStatusLog(String initialMessage)
// 	{
// 		statusLog = new MessageView(initialMessage);
// 		return statusLog;
// 	}

// 	public void processAction(Event evt)
// 	{
// 		// Object source = evt.getSource();
// 		// Button clickedBtn = (Button) source;

// 		// if (clickedBtn.getId().equals("5") == true){
// 		// 	myModel.stateChangeRequest("Done", null);
// 		// }

// 		// // DEBUG: System.out.println("TellerView.actionPerformed()");

// 		// clearErrorMessage();

// 		// String authorField = author.getText();
// 		// String titleField = title.getText();
// 		// String publicationField = pubyear.getText();
// 		// int year = 0;

// 		// if ((!(publicationField == null) && !(publicationField.length() == 0)))
// 		// {
// 		//    try
// 		//    {
// 		//       year = Integer.parseInt(publicationField);
// 		//    }
// 		//    catch( Exception e)
// 		//    {
// 		// 		pubyear.requestFocus();		      
// 		//    }
			
// 		// }
// 		// else {
// 		// 	pubyear.requestFocus();
// 		// }

// 		// if ((authorField == null) || (authorField.length() == 0))
// 		// {
// 		// 	displayErrorMessage("Name requested.");
// 		// 	author.requestFocus();
// 		// }
// 		// else if ((titleField == null) || (titleField.length() == 0))
// 		// {
// 		// 	displayErrorMessage("Title requested.");
// 		// 	title.requestFocus();
// 		// }
// 		// else if (year < 1800 || year > 2016)
// 		// {
// 		// 	displayErrorMessage("Please an year between 1800 and 2016!");
// 		// 	pubyear.requestFocus();
// 		// }

// 		// else
// 		// {
// 		// 		addBook(authorField, titleField, publicationField);
// 		// }
// 	}
// 	public void updateState(String key, Object value)
// 	{
// 		// STEP 6: Be sure to finish the end of the 'perturbation'
// 		// by indicating how the view state gets updated.
// 		if (key.equals("LoginError") == true)
// 		{
// 			// display the passed text
// 			displayErrorMessage((String)value);
// 		}
// 	}

// 	public void displayErrorMessage(String message)
// 	{
// 		statusLog.displayErrorMessage(message);
// 	}
// }