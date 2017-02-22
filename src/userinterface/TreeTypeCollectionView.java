package userinterface;

// project imports
import impresario.IModel;
import impresario.IView;
import model.TreeType;
import model.TreeTypeCollection;
import model.TreeTypeVector;

import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;

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
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ResourceBundle;

public class TreeTypeCollectionView extends View
{
    private TextField 					id;
    private Button 						barcodePrefix;
    private Button 						doneButton;
    private Button 						submitButton;
    private MessageView 				statusLog;
    private Alert						alert;
    private TableView<TreeTypeVector> 	tableOfTreeType;
	private ResourceBundle				rb;

    public TreeTypeCollectionView(IModel treeTypecollection)
    {

	super(treeTypecollection, "TreeTypeCollectionView");

	VBox container = new VBox(10);
	rb = ResourceBundle.getBundle("TreeTypeView", MainScreenView.language);
	container.setPadding(new Insets(15, 5, 5, 5));
			container.setStyle("-fx-background-style: solid;"
	           + "-fx-background-color: #2B2B2B");
	container.getChildren().add(createTitle());
	container.getChildren().add(createFormContents());
	container.getChildren().add(createStatusLog("                          "));
	getChildren().add(container);
	getEntryTableModelValues();
	myModel.subscribe("LoginError", this);
    }

    private Node createTitle()
    {
    	HBox titleContainer = new HBox();
		titleContainer.setAlignment(Pos.CENTER);
		
		
		Text titleText = new Text(rb.getString("modifyTreeType"));
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(800);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
		titleContainer.getChildren().add(titleText);
		return titleContainer;
    }

    @SuppressWarnings("unchecked")
    private GridPane createFormContents()
    {
		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

		tableOfTreeType = new TableView<TreeTypeVector>();
		tableOfTreeType.setEditable(true);

		TableColumn descriptionColumn = new TableColumn(rb.getString("barcodePrefix"));
		descriptionColumn.setMinWidth(200);
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<TreeTypeVector, String>("description"));
		descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		descriptionColumn.setOnEditCommit(
			new EventHandler<CellEditEvent<TreeTypeVector, String>>() {
				@Override
				public void handle(CellEditEvent<TreeTypeVector, String> t) {
					((TreeTypeVector) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setDescription(t.getNewValue());
				}
			}
		);

		TableColumn costColumn = new TableColumn(rb.getString("cost"));
		costColumn.setMinWidth(200);
		costColumn.setCellValueFactory(new PropertyValueFactory<TreeTypeVector, String>("cost"));
		costColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		costColumn.setOnEditCommit(
			new EventHandler<CellEditEvent<TreeTypeVector, String>>() {
				@Override
				public void handle(CellEditEvent<TreeTypeVector, String> t) {
					((TreeTypeVector) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setCost(t.getNewValue());
				}
			}
		);

		TableColumn barcodePrefixColumn = new TableColumn(rb.getString("description"));
		barcodePrefixColumn.setMinWidth(200);
		barcodePrefixColumn.setCellValueFactory(new PropertyValueFactory<TreeTypeVector, String>("barcodePrefix"));
		barcodePrefixColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		barcodePrefixColumn.setOnEditCommit(
			new EventHandler<CellEditEvent<TreeTypeVector, String>>() {
				@Override
				public void handle(CellEditEvent<TreeTypeVector, String> t) {
					((TreeTypeVector) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setBarcodePrefix(t.getNewValue());
				}
			}
		);

		tableOfTreeType.getColumns().addAll(descriptionColumn, costColumn, barcodePrefixColumn);
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(600, 250);
		scrollPane.setContent(tableOfTreeType);
		grid.add(scrollPane, 1, 1);
		createButton(grid, doneButton, rb.getString("done"), 3);
		createButton(grid, submitButton, rb.getString("submit"), 2);
		return grid;
	}

    private void createButton(GridPane grid, Button button, String nameButton, Integer pos)
    {
		button = new Button(nameButton);
		button.setId(Integer.toString(pos));
		button.setOnAction(new EventHandler<ActionEvent>() {
		     @Override
			 public void handle(ActionEvent e) {
			 processAction(e);
		     }
	    });
		HBox btnContainer = new HBox(10);
		btnContainer.setAlignment(Pos.BOTTOM_CENTER);
		btnContainer.getChildren().add(button);
		grid.add(btnContainer, 1, pos);
    }

    private MessageView createStatusLog(String initialMessage)
    {
		statusLog = new MessageView(initialMessage);
		return statusLog;
    }

    private void getEntryTableModelValues()
    {
		ObservableList<TreeTypeVector> tableData = FXCollections.observableArrayList();
		try
	    	{
			TreeTypeCollection TreeTypeSearch = (TreeTypeCollection)myModel.getState("TreeTypeList");
			Vector entryList = (Vector)TreeTypeSearch.getState("TreeTypes");
			Enumeration entries = entryList.elements();

		while (entries.hasMoreElements() == true)
		    {
				TreeType nextTreeType = (TreeType)entries.nextElement();
				Vector<String> view = nextTreeType.getVector();
				TreeTypeVector nextTableRowData = new TreeTypeVector(view);
				tableData.add(nextTableRowData);
		    }
			tableOfTreeType.setItems(tableData);
	    }
		catch (Exception e) {
			System.out.println(e);
		}
    }

    public void processAction(Event evt)
    {
		Object source = evt.getSource();
		Button clickedBtn = (Button) source;
		if (clickedBtn.getId().equals("3") == true){
			myModel.stateChangeRequest("Done", null);
		}
		clearErrorMessage();
		if (clickedBtn.getId().equals("2") == true){
			myModel.stateChangeRequest("Submit", tableOfTreeType);
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(rb.getString("ModifyMsgTitle"));
			alert.setHeaderText(rb.getString("ModifyMsgHeader"));
			//alert.setContentText("There is a session that is currently open.\nPlease close the session and try again.");
			alert.show();
		}
		clearErrorMessage();
 	}

    private void processSearchTreeType(String idEntry)
    {
	Properties props = new Properties();

	props.setProperty("Id", idEntry);
	try{
	    myModel.stateChangeRequest("SearchTreeType", props);
	    getEntryTableModelValues();
	}
	catch (Exception ex)
	    {
		displayErrorMessage("Error to searching TreeType in database!");
	    }
    }

    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
	// STEP 6: Be sure to finish the end of the 'perturbation'
	// by indicating how the view state gets updated.
	if (key.equals("LoginError") == true)
	    {
		// display the passed text
		displayErrorMessage((String)value);
	    }

    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message)
    {
    	statusLog.displayErrorMessage(message);
    }

    /**
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message)
    {
    	statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
    	statusLog.clearErrorMessage();
    }

}