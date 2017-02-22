package userinterface;

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
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import impresario.IModel;
import model.Tree;
import model.TreeCollection;
import java.util.ResourceBundle;

public class TreeCollectionView extends View
{
	protected TableView<TreeTableModel> tableOfTrees;
	
	protected Button selectButton;
	protected Button doneButton;
	protected Text	titleText;
	protected TreeCollection myTreeCollection;
	protected MessageView statusLog;
	private Alert				alert;
	private Alert				confirm;
	protected ResourceBundle	rb;


	public TreeCollectionView(IModel tc, String key)
	{
		super(tc, "TreeCollectionView");
		myTreeCollection = (TreeCollection)tc;
		rb = ResourceBundle.getBundle("TreeViewBundle", MainScreenView.language);

		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));
		container.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
		container.getChildren().add(createTitle(key));
		container.getChildren().add(createFormContent(key));
		container.getChildren().add(createStatusLog("                        "));
		getChildren().add(container);
		myModel.stateChangeRequest("search", null);
		populateFields(key);
	}

	protected void populateFields(String key)
	{
		getEntryTableModelValues(key);
	}

	protected void getEntryTableModelValues(String key)
	{
		ObservableList<TreeTableModel> tableData = FXCollections.observableArrayList();
		try
		{
			TreeCollection TreeCollection = (TreeCollection)myModel.getState("TreeList");

	 		Vector entryList = (Vector)TreeCollection.getState("Trees");
			Enumeration entries = entryList.elements();

			while (entries.hasMoreElements() == true)
			{
				Tree nextTree = (Tree)entries.nextElement();
				Vector<String> view = nextTree.getEntryListView();
				TreeTableModel nextTableRowData = new TreeTableModel(view);
				if (key.equals("remove") && nextTableRowData.getStatus().equals("Sold") == false) {
					tableData.add(nextTableRowData);
				}
				else if (key.equals("modify")){
					tableData.add(nextTableRowData);
				}
			}
			tableOfTrees.setItems(tableData);
		}
		catch (Exception e) {//SQLException e) {
			// Need to handle this exception
		}
	}

	private Node createTitle(String key)
	{
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);
		if (key.equals("modify"))
			titleText = new Text(rb.getString("titleModifyTree"));
		else if (key.equals("remove"))
			titleText = new Text(rb.getString("titleRemoveTree"));
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
		container.getChildren().add(titleText);
		return (container);
	}

	private GridPane createFormContent(String key)
	{
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

		
		tableOfTrees = new TableView<TreeTableModel>();
		tableOfTrees.setEditable(true);
		TableColumn barCodeColumn = new TableColumn(rb.getString("barcode"));
		barCodeColumn.setMinWidth(100);
		barCodeColumn.setCellValueFactory(new PropertyValueFactory<TreeTableModel, String>("Barcode"));
		barCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		barCodeColumn.setOnEditCommit(
		new EventHandler<CellEditEvent<TreeTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<TreeTableModel, String> t) {
					((TreeTableModel) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setBarcode(t.getNewValue());
				}
			}
		);
		TableColumn treeTypeColumn = new TableColumn(rb.getString("treeType"));
		treeTypeColumn.setMinWidth(100);
		treeTypeColumn.setCellValueFactory(new PropertyValueFactory<TreeTableModel, String>("TreeType"));
		treeTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		treeTypeColumn.setOnEditCommit(
		new EventHandler<CellEditEvent<TreeTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<TreeTableModel, String> t) {
					((TreeTableModel) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setTreeType(t.getNewValue());
				}
			}
		);
		TableColumn notesColumn = new TableColumn("Notes");
		notesColumn.setMinWidth(100);
		notesColumn.setCellValueFactory(new PropertyValueFactory<TreeTableModel, String>("Notes"));
		notesColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		notesColumn.setOnEditCommit(
		new EventHandler<CellEditEvent<TreeTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<TreeTableModel, String> t) {
					((TreeTableModel) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setNotes(t.getNewValue());
				}
			}
		);
		TableColumn statusColumn = new TableColumn("Status");
		statusColumn.setMinWidth(100);
		statusColumn.setCellValueFactory(new PropertyValueFactory<TreeTableModel, String>("Status"));
		statusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		statusColumn.setOnEditCommit(
		new EventHandler<CellEditEvent<TreeTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<TreeTableModel, String> t) {
					((TreeTableModel) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setStatus(t.getNewValue());
				}
			}
		);
		TableColumn dateStatusUpdatedColumn = new TableColumn(rb.getString("dateStatusUpdated"));
		dateStatusUpdatedColumn.setMinWidth(100);
		dateStatusUpdatedColumn.setCellValueFactory(new PropertyValueFactory<TreeTableModel, String>("DateStatusUpdated"));
		dateStatusUpdatedColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		dateStatusUpdatedColumn.setOnEditCommit(
		new EventHandler<CellEditEvent<TreeTableModel, String>>() {
				@Override
				public void handle(CellEditEvent<TreeTableModel, String> t) {
					((TreeTableModel) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
						).setDateStatusUpdated(t.getNewValue());
				}
			}
		);
		tableOfTrees.getColumns().addAll(barCodeColumn, treeTypeColumn, notesColumn, statusColumn, dateStatusUpdatedColumn);
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(520, 250);
		scrollPane.setContent(tableOfTrees);

		grid.add(scrollPane, 1, 0);

		grid.add(createButton(selectButton, key), 1, 1);
		return (grid);
	}

	private GridPane createButton(Button button, String key) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
        grid.setVgap(10);
		
		grid.setAlignment(Pos.CENTER);
		if (key.equals("modify")){
			selectButton = new Button(rb.getString("submit"));
		}
		else if (key.equals("remove")) {
			selectButton = new Button(rb.getString("submit"));
		}
		grid.add(selectButton, 1, 1);
		selectButton.setOnAction(e -> {
			updateTreeCollection(tableOfTrees.getSelectionModel().getSelectedItem(), key);
		});
		doneButton = new Button(rb.getString("back"));
		grid.add(doneButton, 2, 1);
		doneButton.setOnAction(e -> {
			myModel.stateChangeRequest("done", null);
		});
		return grid;
	}
	
	private void updateTreeCollection(TreeTableModel ttm, String key) {
		try{
			if(key.equals("remove"))
			{
				TablePosition pos = tableOfTrees.getSelectionModel().getSelectedCells().get(0);
				int row = pos.getRow();
				TreeTableModel item = tableOfTrees.getItems().get(row);
				String selectedTreeBarcode = item.getBarcode();
				confirm = new Alert(AlertType.CONFIRMATION, rb.getString("ConfirmMsg") + selectedTreeBarcode + "?", ButtonType.YES, ButtonType.NO);
				confirm.showAndWait();
				if(confirm.getResult() == ButtonType.YES)
				{
					myModel.stateChangeRequest(key, ttm);
					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle(rb.getString("RemoveMsgTitle"));
					alert.setHeaderText(rb.getString("RemoveMsgHeader"));
					alert.show();
				}
				else confirm.close();
			}
			else
			{
				myModel.stateChangeRequest(key, ttm);
				//statusLog.displayMessage("Tree update successfully!");
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(rb.getString("ModifyMsgTitle"));
				alert.setHeaderText(rb.getString("ModifyMsgHeader"));
				alert.show();
			}
		}
		catch (Exception ex){
			System.out.println("Update failed..." + ex.getCause());
			//displayErrorMessage("Error in adding the Tree in the database!");
		}
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
