package userinterface;

// system imports
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Vector;
import java.util.Enumeration;
import java.util.ResourceBundle;

// project imports
import impresario.IModel;
import model.MainScreen;
import model.Scout;
import model.ScoutCollection;

//==============================================================================
public class ScoutCollectionView extends View
{
	protected TableView<ScoutTableModel> tableOfScouts;
	
	protected Button doneButton;
	protected Button submitButton;
	VBox container = new VBox(10);
	protected Text titleText;
	protected ResourceBundle	rb;
	
	protected ScoutCollection myScoutCollection;

	protected MessageView statusLog;
	private Alert alert;
	private Alert confirm;
	protected TableView<ScoutTableModel> tableOfScout;


	//--------------------------------------------------------------------------
	
	public ScoutCollectionView(IModel sc, String key)
	{
		super(sc, "ScoutCollectionView");
		myScoutCollection = (ScoutCollection) sc;
		rb = ResourceBundle.getBundle("ScoutViewBundle", MainScreenView.language);
		//VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));
		if (key.equals("modify"))
			modifyView();
		else if (key.equals("remove"))
			removeView();
		myModel.subscribe("LoginError", this);
		container.getChildren().add(createStatusLog("                          "));
		getChildren().add(container);
		getEntryTableModelValues();
	}
	
	private void modifyView()
	{
		HBox titleContainer = new HBox();
		titleContainer.setAlignment(Pos.CENTER);
		
		
		Text titleText = new Text(rb.getString("TitleModifyScout"));
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(800);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
		container.getChildren().add(titleText);
		
		GridPane modifyContainer = new GridPane();
		modifyContainer.getChildren().add(createFormContentModify());
		getChildren().add(modifyContainer);
	}
	
	private void removeView() {
		HBox titleContainer = new HBox();
		titleContainer.setAlignment(Pos.CENTER);
		
		titleText = new Text(rb.getString("TitleRemoveScout"));
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(800);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
		container.getChildren().add(titleText);
		
		GridPane removeContainer = new GridPane();
		removeContainer.getChildren().add(createFormContent());
		getChildren().add(removeContainer);
	}

	//--------------------------------------------------------------------------
	protected void getEntryTableModelValues()
	{
		
		ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();
		try
		{
			ScoutCollection scoutCollection = (ScoutCollection)myModel.getState("ScoutList");

	 		Vector entryList = (Vector)scoutCollection.getState("Scouts");
			Enumeration entries = entryList.elements();
			while (entries.hasMoreElements() == true)
			{
				//entries does not have any elements in it
				Scout nextScout = (Scout)entries.nextElement();
				Vector<String> view = nextScout.getEntryListView();

				// add this list entry to the list
				ScoutTableModel nextTableRowData = new ScoutTableModel(view);
				//System.out.println(nextTableRowData.getScoutId());
				tableData.add(nextTableRowData);
				
			}
			
			tableOfScouts.setItems(tableData);
		}
		catch (Exception e) {//SQLException e) {
			System.out.println("ERROR: Could not populate scout table");
		}
	}

	// Create the main form content
	//-----------------------------------------------------------
	public VBox createFormContent()	// Remove method
	{
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(50, 50, 50, 50));
		vbox.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");

		tableOfScouts = new TableView<ScoutTableModel>();
		tableOfScouts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tableOfScouts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	
		TableColumn scoutIdColumn = new TableColumn(rb.getString("ScoutId"));
		//scoutIdColumn.setMinWidth(11);
		scoutIdColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, Integer>("Id"));
					scoutIdColumn.setVisible(false);
		
		TableColumn firstNameColumn = new TableColumn(rb.getString("FirstName"));
		//firstNameColumn.setMinWidth(100);
		firstNameColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("FirstName"));
		  
		TableColumn lastNameColumn = new TableColumn(rb.getString("LastName"));
		//lastNameColumn.setMinWidth(100);
		lastNameColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("LastName"));
		
		TableColumn middleNameColumn = new TableColumn(rb.getString("MiddleName"));
		//middleNameColumn.setMinWidth(100);
		middleNameColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("MiddleName"));

		TableColumn dateOfBirthColumn = new TableColumn(rb.getString("DateOfBirth"));
		//dateOfBirthColumn.setMinWidth(10);
		dateOfBirthColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("DateOfBirth"));
		
		TableColumn phoneColumn = new TableColumn(rb.getString("PhoneNumber"));
		//phoneColumn.setMinWidth(50);
		phoneColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("PhoneNumber"));
		
		TableColumn emailColumn = new TableColumn(rb.getString("Email"));
		//emailColumn.setMinWidth(10);
		emailColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("Email"));
		  
		TableColumn troopIdColumn = new TableColumn(rb.getString("TroopId"));
		//troopIdColumn.setMinWidth(25);
		troopIdColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("TroopId"));
		
		TableColumn statusColumn = new TableColumn(rb.getString("Status"));
		//statusColumn.setMinWidth(50);
		statusColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("Status"));

		TableColumn dateStatusUpdatedColumn = new TableColumn(rb.getString("DateStatusUpdated"));
		//dateStatusUpdatedColumn.setMinWidth(25);
		dateStatusUpdatedColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("DateStatusUpdated"));

		tableOfScouts.getColumns().addAll(scoutIdColumn, 
				firstNameColumn, lastNameColumn, middleNameColumn, 
				dateOfBirthColumn, phoneColumn, emailColumn,
				troopIdColumn, statusColumn, dateStatusUpdatedColumn);

		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(730, 350);
		scrollPane.setContent(tableOfScouts);
		
		submitButton = new Button(rb.getString("SubmitButton"));
		submitButton.setOnAction(e -> {
			try {
				TablePosition pos = tableOfScouts.getSelectionModel().getSelectedCells().get(0);
				int row = pos.getRow();
				ScoutTableModel item = tableOfScouts.getItems().get(row);
				String selectedScoutId = item.getFirstName();
				confirm = new Alert(AlertType.CONFIRMATION, rb.getString("ConfirmMsg") + selectedScoutId + "?", ButtonType.YES, ButtonType.NO);
				confirm.showAndWait();
				if(confirm.getResult() == ButtonType.YES)
				{					
					String sID = tableOfScouts.getSelectionModel().getSelectedItem().getScoutId();
					Scout selectedScout = myScoutCollection.retrieve(sID);
					selectedScout.removeScout();
					//tableOfScouts;
					//statusLog.displayMessage("Scout ID " + sID + " deleted");
					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle(rb.getString("RemoveMsgTitle"));
					alert.setHeaderText(rb.getString("RemoveMsgHeader"));
					//alert.setContentText("There is a session that is currently open.\nPlease close the session and try again.");
					alert.show();
					myScoutCollection.done();
				}
				else confirm.close();
			}
			catch (Exception NullPointerException){
				System.out.println("ERROR: Scout not selected!");
			}
			
		});
		
		doneButton = new Button(rb.getString("DoneButton"));
 		doneButton.setOnAction(e -> {
 			myScoutCollection.done();
        });

		HBox btnContainer = new HBox(50);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().add(submitButton);
		btnContainer.getChildren().add(doneButton);
		
		vbox.getChildren().add(scrollPane);
		vbox.getChildren().add(btnContainer);
	
		return vbox;
	}

	
	
	//********************************************************************************
	public GridPane createFormContentModify()
    {
		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(50, 50, 50, 50));
		grid.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");

		tableOfScouts = new TableView<ScoutTableModel>();
		tableOfScouts.setEditable(true);
		tableOfScouts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn scoutIDColumn = new TableColumn(rb.getString("ScoutId"));
		//scoutIDColumn.setMinWidth(200);
		scoutIDColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("Id"));
		scoutIDColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		scoutIDColumn.setVisible(false);
		
		
		TableColumn firstNameColumn = new TableColumn(rb.getString("FirstName"));
		//firstNameColumn.setMinWidth(200);
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("FirstName"));
		firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		firstNameColumn.setOnEditCommit(
			new EventHandler<CellEditEvent<ScoutTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<ScoutTableModel, String> s) {
					((ScoutTableModel) s.getTableView().getItems().get(
						s.getTablePosition().getRow())
						).setFirstName(s.getNewValue());
				}
			}
		);
		
		
		TableColumn lastNameColumn = new TableColumn(rb.getString("LastName"));
		//lastNameColumn.setMinWidth(200);
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("LastName"));
		lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		lastNameColumn.setOnEditCommit(
			new EventHandler<CellEditEvent<ScoutTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<ScoutTableModel, String> s) {
					((ScoutTableModel) s.getTableView().getItems().get(
						s.getTablePosition().getRow())
						).setLastName(s.getNewValue());
				}
			}
		);
		
		
		TableColumn middleNameColumn = new TableColumn(rb.getString("MiddleName"));
		//middleNameColumn.setMinWidth(200);
		middleNameColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("MiddleName"));
		middleNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		middleNameColumn.setOnEditCommit(
			new EventHandler<CellEditEvent<ScoutTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<ScoutTableModel, String> s) {
					((ScoutTableModel) s.getTableView().getItems().get(
						s.getTablePosition().getRow())
						).setMiddleName(s.getNewValue());
				}
			}
		);


		TableColumn dobColumn = new TableColumn(rb.getString("DateOfBirth"));
		//dobColumn.setMinWidth(200);
		dobColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("DateOfBirth"));
		dobColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		dobColumn.setOnEditCommit(
			new EventHandler<CellEditEvent<ScoutTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<ScoutTableModel, String> s) {
					((ScoutTableModel) s.getTableView().getItems().get(
						s.getTablePosition().getRow())
						).setDateOfBirth(s.getNewValue());
				}
			}
		);
		
		
		TableColumn phoneNumberColumn = new TableColumn(rb.getString("PhoneNumber"));
		//phoneNumberColumn.setMinWidth(200);
		phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("PhoneNumber"));
		phoneNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		phoneNumberColumn.setOnEditCommit(
			new EventHandler<CellEditEvent<ScoutTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<ScoutTableModel, String> s) {
					((ScoutTableModel) s.getTableView().getItems().get(
						s.getTablePosition().getRow())
						).setPhoneNumber(s.getNewValue());
				}
			}
		);
		
		
		TableColumn emailColumn = new TableColumn(rb.getString("Email"));
		//emailColumn.setMinWidth(200);
		emailColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("Email"));
		emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		emailColumn.setOnEditCommit(
			new EventHandler<CellEditEvent<ScoutTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<ScoutTableModel, String> s) {
					((ScoutTableModel) s.getTableView().getItems().get(
						s.getTablePosition().getRow())
						).setEmail(s.getNewValue());
				}
			}
		);
		
		
		TableColumn troopColumn = new TableColumn(rb.getString("TroopId"));
		//troopColumn.setMinWidth(200);
		troopColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("TroopId"));
		troopColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		troopColumn.setOnEditCommit(
			new EventHandler<CellEditEvent<ScoutTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<ScoutTableModel, String> s) {
					((ScoutTableModel) s.getTableView().getItems().get(
						s.getTablePosition().getRow())
						).setTroopId(s.getNewValue());
				}
			}
		);
		
		TableColumn statusColumn = new TableColumn(rb.getString("Status"));
		//statusColumn.setMinWidth(200);
		statusColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("Status"));
		statusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		statusColumn.setOnEditCommit(
			new EventHandler<CellEditEvent<ScoutTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<ScoutTableModel, String> s) {
					((ScoutTableModel) s.getTableView().getItems().get(
						s.getTablePosition().getRow())
						).setStatus(s.getNewValue());
				}
			}
		);
		
		
		TableColumn dateUpdatedColumn = new TableColumn(rb.getString("DateStatusUpdated"));
		//dateUpdatedColumn.setMinWidth(200);
		dateUpdatedColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("DateStatusUpdated"));
		dateUpdatedColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		
		tableOfScouts.getColumns().addAll(scoutIDColumn, firstNameColumn, lastNameColumn, middleNameColumn, dobColumn, phoneNumberColumn, emailColumn, troopColumn, statusColumn, dateUpdatedColumn);
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(730, 350);
		scrollPane.setContent(tableOfScouts);
		grid.add(scrollPane, 0, 1);
		//createButton(grid, doneButton, "Done", 3);
		//createButton(grid, submitButton, "Submit", 2);
		doneButton = new Button(rb.getString("DoneButton"));
		doneButton.setOnAction(e -> {
			myScoutCollection.done();
		});
		submitButton = new Button(rb.getString("SubmitButton"));
		submitButton.setOnAction(e -> {
			try{
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(rb.getString("ModifyMsgTitle"));
				alert.setHeaderText(rb.getString("ModifyMsgHeader"));
				alert.show();
				myModel.stateChangeRequest("modify", tableOfScouts.getSelectionModel().getSelectedItem());
				//statusLog.displayMessage(rb.getString("SuccessMsg"));
				//alert.setContentText("There is a session that is currently open.\nPlease close the session and try again.");
			}
			catch (Exception ex){
				System.out.println("Update failed..." + ex.getCause());
				//displayErrorMessage("Error in adding the Tree in the database!");
			}
		});
		
		HBox btnContainer = new HBox(10);
		btnContainer.setAlignment(Pos.BOTTOM_CENTER);
		btnContainer.getChildren().addAll(submitButton, doneButton);
		grid.add(btnContainer, 0, 2);
		
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
		btnContainer.setAlignment(Pos.BOTTOM_RIGHT);
		btnContainer.getChildren().add(button);
		grid.add(btnContainer, 1, pos);
    }
	

	//--------------------------------------------------------------------------
	public void updateState(String key, Object value)
	{
		
	}

	//--------------------------------------------------------------------------
	protected void processAccountSelected()
	{
		/*AccountTableModel selectedItem = tableOfAccounts.getSelectionModel().getSelectedItem();
		
		if(selectedItem != null)
		{
			String selectedAcctNumber = selectedItem.getAccountNumber();

			myModel.stateChangeRequest("AccountSelected", selectedAcctNumber);
		}*/
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
			myModel.stateChangeRequest("Submit", tableOfScouts);
		}
		clearErrorMessage();
 	}
    
	//--------------------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
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
	/*
	//--------------------------------------------------------------------------
	public void mouseClicked(MouseEvent click)
	{
		if(click.getClickCount() >= 2)
		{
			processAccountSelected();
		}
	}
   */
	
}
