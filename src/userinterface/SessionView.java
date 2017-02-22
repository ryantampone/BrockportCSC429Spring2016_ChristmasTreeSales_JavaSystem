package userinterface;

//The main scoutView 
import impresario.IModel;
import impresario.IView;
import model.Scout;
import model.ScoutCollection;
import model.Session;
import model.Shift;
import model.Tree;
import database.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;
import java.lang.Integer;

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

public class SessionView extends View {
	private Label		 		statusLog;
	private Alert				alert;
	private TextField 			startDateTextbox;
	private TextField 			startTimeTextbox;
	private TextField 			endTimeTextbox;
	private TextField 			startCashTextbox;
	private TextField			endCashTextbox;
	private TextField			totalChecksTextbox;
	private TextField 			notesTextbox;
	private ComboBox 			scout1;
	private ComboBox 			scout2;
	private ComboBox 			scout3;
	private ComboBox			scout4;
	private TextField			companion1Textbox;
	private TextField			companion2Textbox;
	private TextField			companion3Textbox;
	private TextField			companion4Textbox;
	private ComboBox			companion1Hours;
	private ComboBox			companion2Hours;
	private ComboBox			companion3Hours;
	private ComboBox			companion4Hours;
	private Button 				submitButton;
	private Button 				backButton;
	protected ResourceBundle	rb;
	
	VBox container = new VBox(10);
	Text titleText = new Text("title");
	protected String sessionStatus;
	protected Session mySession;
	protected Shift myShift;
	protected Scout myScout;
	protected ScoutCollection myScoutCollection = new ScoutCollection();
	
	Calendar today = Calendar.getInstance();
	String year = String.valueOf(today.get(Calendar.YEAR));
	String month = String.valueOf((today.get(Calendar.MONTH))+1);
	String day = String.valueOf(today.get(Calendar.DAY_OF_MONTH));
	String hour = String.valueOf(today.get(Calendar.HOUR));
	String minute = String.valueOf(today.get(Calendar.MINUTE));
	String startAMPM = "";
	String endAMPM = "";
	String todayDate = month + "/" + day + "/" + year;
	String currentTime = hour + ":" + minute;
	String endTime = String.valueOf(today.get(Calendar.HOUR)+3) + ":" + minute;

	public SessionView(IModel session, String key) 
	{
		super(session, "SessionView");
		mySession = (Session) session;
		rb = ResourceBundle.getBundle("SessionViewBundle", MainScreenView.language);
		if(hour.equals("0")) { hour = "12"; }
		System.out.println(hour);
		if(today.get(Calendar.MINUTE) < 10)
		{
			minute = "0" + String.valueOf(today.get(Calendar.MINUTE));
			currentTime = hour + ":" + minute;
			endTime = String.valueOf(today.get(Calendar.HOUR)+3) + ":" + minute;
		}
		if(today.get(Calendar.HOUR)+3 > 12)
		{
			//hour = String.valueOf(today.get(Calendar.HOUR)-12+3);
			endTime = String.valueOf(today.get(Calendar.HOUR)-12+3) + ":" + minute;
		}
		if(today.get(Calendar.HOUR) == 0)
			hour = "12";
		if((today.get(Calendar.AM_PM) == Calendar.AM) && (today.get(Calendar.HOUR) < 9 || today.get(Calendar.HOUR) == 12))
		{
			startAMPM = " AM";
			endAMPM = " AM";
		}
		else if((today.get(Calendar.AM_PM) == Calendar.AM) && (today.get(Calendar.HOUR) >= 9 || today.get(Calendar.HOUR) < 12))
		{
			startAMPM = " AM";
			endAMPM = " PM";
		}
		else if((today.get(Calendar.AM_PM) == Calendar.PM) && (today.get(Calendar.HOUR) < 9 || today.get(Calendar.HOUR) == 12))
		{
			startAMPM = " PM";
			endAMPM = " PM";
		}
		else
		{
			startAMPM = " PM";
			endAMPM = " AM";
		}
		currentTime = hour + ":" + minute + " " + startAMPM;
		endTime += endAMPM;
		//VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));
		if (key.equals("open"))
			openView();
		else if (key.equals("close"))
			closeView();
		myModel.subscribe("LoginError", this);
		//container.getChildren().add(createStatusLog("                          "));
		getChildren().add(container);
	}

	//View to Open a new Session
	private void openView() 
	{
		VBox superContainer = new VBox();
		superContainer.setAlignment(Pos.CENTER);
		superContainer.setPadding(new Insets(15, 5, 5, 5));
	   	superContainer.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
		
		HBox titleContainer = new HBox();
		titleContainer.setAlignment(Pos.CENTER);
		titleContainer.setPadding(new Insets(15, 5, 5, 5));
	   	titleContainer.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
		
		Text titleText = new Text(rb.getString("TitleOpenSession"));
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
		titleContainer.getChildren().add(titleText);
		
		GridPane addContainer = new GridPane();
		superContainer.getChildren().add(titleContainer);
		addContainer.getChildren().add(createSelectContentsForOpen());
		superContainer.getChildren().add(addContainer);
		
		getChildren().add(superContainer);
	}
	
	//View to Close an existing Session
	private void closeView() 
	{
		VBox superContainer = new VBox();
		superContainer.setAlignment(Pos.CENTER);
		superContainer.setPadding(new Insets(15, 5, 5, 5));
	   	superContainer.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
		
		HBox titleContainer = new HBox();
		titleContainer.setAlignment(Pos.CENTER);
		titleContainer.setPadding(new Insets(15, 5, 5, 5));
	   	titleContainer.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");
		
		Text titleText = new Text(rb.getString("TitleCloseSession"));
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.GOLD);
		titleContainer.getChildren().add(titleText);

/*		GridPane addContainer = new GridPane();
		try {
			addContainer.getChildren().add(createSelectContentsForClose());
			getChildren().add(addContainer);
		} catch (NullPointerException ex) {
			System.out.println("Could not switch to Open Session View");
		}*/
		
		GridPane addContainer = new GridPane();
		superContainer.getChildren().add(titleContainer);
		addContainer.getChildren().add(createSelectContentsForClose());
		superContainer.getChildren().add(addContainer);
		
		getChildren().add(superContainer);
	}
		
    @SuppressWarnings("unchecked")
	private GridPane createSelectContentsForOpen() {
    	GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setStyle("-fx-background-style: solid;"
                + "-fx-background-color: #2B2B2B");

		startDateTextbox = new TextField();
		startDateTextbox.setText(todayDate);
		startDateTextbox.setDisable(true);
		startTimeTextbox = new TextField();
		startTimeTextbox.setText(currentTime);
		startTimeTextbox.setDisable(true);
		endTimeTextbox = new TextField();
		endTimeTextbox.setText(endTime);
		endTimeTextbox.setDisable(true);
		startCashTextbox = new TextField();
		notesTextbox = new TextField();
		companion1Textbox = new TextField();
		companion2Textbox = new TextField();
		companion3Textbox = new TextField();
		companion4Textbox = new TextField();

		createHBox(grid, rb.getString("StartDate"), startDateTextbox, 1);
		createHBox(grid, rb.getString("StartTime"), startTimeTextbox, 2);
		createHBox(grid, rb.getString("EndTime"), endTimeTextbox, 3);
		createHBox(grid, rb.getString("StartCash"), startCashTextbox, 4);
		createHBox(grid, rb.getString("Notes"), notesTextbox, 5);
		
		Label scoutLabel = new Label(rb.getString("ChooseScout"));
		scoutLabel.setTextFill(Color.WHITE);
		scoutLabel.setAlignment(Pos.CENTER);
		grid.add(scoutLabel, 0, 6);
		
		Label companionLabel = new Label(rb.getString("CompanionName"));
		companionLabel.setTextFill(Color.WHITE);
		companionLabel.setAlignment(Pos.CENTER);
		grid.add(companionLabel, 1, 6);
		
		Label hoursLabel = new Label(rb.getString("CompanionHours"));
		hoursLabel.setTextFill(Color.WHITE);
		hoursLabel.setAlignment(Pos.CENTER);
		grid.add(hoursLabel, 2, 6);
		
		myScoutCollection.displayScouts();
		Vector<String> scoutIdOptions = myScoutCollection.getScoutIds();
		Vector<String> scoutFNOptions = myScoutCollection.getScoutFirstName();
		Vector<String> scoutLNOptions = myScoutCollection.getScoutLastName();

		Vector<String> allScoutOptions = new Vector<String>();
		int numScouts = scoutIdOptions.size();
		for (int i=0; i< numScouts; i++)
		{
			String id = scoutIdOptions.elementAt(i);
			String fN = scoutFNOptions.elementAt(i);
			String lN = scoutLNOptions.elementAt(i);
			
			/*System.out.println(id);
			System.out.println(fN);
			System.out.println(lN);*/
			
			allScoutOptions.insertElementAt(id + ", " + fN + " " + lN, i);
			System.out.println(allScoutOptions.elementAt(i));
			
			/*allScoutOptions.set(i," " +  scoutIdOptions.elementAt(i));
			allScoutOptions.set(i," " +  scoutFNOptions.elementAt(i));
			allScoutOptions.set(i," " +  scoutLNOptions.elementAt(i));*/
		}

		
		ObservableList<String> allOptions = FXCollections.observableArrayList(allScoutOptions);
		scout1 = new ComboBox(allOptions);
		scout2 = new ComboBox(allOptions);
		scout3 = new ComboBox(allOptions);
		scout4 = new ComboBox(allOptions);
		
		ObservableList<String> hours = FXCollections.observableArrayList("0", "1", "2", "3", "4", "5", "6", "7", "8");
		companion1Hours = new ComboBox(hours);
		companion2Hours = new ComboBox(hours);
		companion3Hours = new ComboBox(hours);
		companion4Hours = new ComboBox(hours);
		
		grid.add(scout1, 0, 7);
		grid.add(companion1Textbox, 1, 7);
		grid.add(companion1Hours, 2, 7);
		grid.add(scout2, 0, 8);
		grid.add(companion2Textbox, 1, 8);
		grid.add(companion2Hours, 2, 8);
		grid.add(scout3, 0, 9);
		grid.add(companion3Textbox, 1, 9);
		grid.add(companion3Hours, 2, 9);
		grid.add(scout4, 0, 10);
		grid.add(companion4Textbox, 1, 10);
		grid.add(companion4Hours, 2, 10);
		
		submitButton = new Button(rb.getString("SubmitButton"));
		submitButton.setOnAction(e -> {
			String startDate = startDateTextbox.getText();
			String startTime = startTimeTextbox.getText();
			String endTime = endTimeTextbox.getText();
			String startCash = startCashTextbox.getText();
			String notes = notesTextbox.getText();
			Session openedSession = mySession.openSession(startDate, startTime, endTime, startCash, notes);
			System.out.println("Session ID: " + openedSession.getState("Id"));

			myScoutCollection = new ScoutCollection();
			if(scout1.getValue() != null)
			{
				String scoutInfo1 = (String)scout1.getValue();
				System.out.println("ScoutInfo1 is: " + scoutInfo1);	
				
				String str = scoutInfo1;
				List<String> scout1InfoList = Arrays.asList(str.split(","));
				System.out.println(scout1InfoList);
				
				String s1 = scout1InfoList.get(0);
				System.out.println("S1 is: " + scoutInfo1);	
				
				String c1 = companion1Textbox.getText();
				String ch1 = (String)companion1Hours.getValue();
				myScoutCollection.retrieveScout(s1);
				myScout = myScoutCollection.retrieve(s1);
				if(myScout == null)
					System.out.println("Scout is null!");
				else System.out.println("Scout NOT null");
				myShift = new Shift(myScout, openedSession, c1, ch1);
			}
			if(scout2.getValue() != null)
			{
				String scoutInfo2 = (String)scout2.getValue();
				System.out.println("ScoutInfo2 is: " + scoutInfo2);	
				
				String str = scoutInfo2;
				List<String> scout2InfoList = Arrays.asList(str.split(","));
				System.out.println(scout2InfoList);
				
				String s2 = scout2InfoList.get(0);
				System.out.println("S2 is: " + scoutInfo2);	

				
				String c2 = companion2Textbox.getText();
				String ch2 = (String)companion2Hours.getValue();
				myScoutCollection.retrieveScout(s2);
				myScout = myScoutCollection.retrieve(s2);
				myShift = new Shift(myScout, openedSession, c2, ch2);
			}
			if(scout3.getValue() != null)
			{
				String scoutInfo3 = (String)scout3.getValue();
				System.out.println("ScoutInfo3 is: " + scoutInfo3);	
				
				String str = scoutInfo3;
				List<String> scout3InfoList = Arrays.asList(str.split(","));
				System.out.println(scout3InfoList);
				
				String s3 = scout3InfoList.get(0);
				System.out.println("S3 is: " + scoutInfo3);	
				
				String c3 = companion3Textbox.getText();
				String ch3 = (String)companion3Hours.getValue();
				myScoutCollection.retrieveScout(s3);
				myScout = myScoutCollection.retrieve(s3);
				myShift = new Shift(myScout, openedSession, c3, ch3);
			}
			if(scout4.getValue() != null)
			{
				String scoutInfo4 = (String)scout4.getValue();
				System.out.println("ScoutInfo4 is: " + scoutInfo4);	
				
				String str = scoutInfo4;
				List<String> scout4InfoList = Arrays.asList(str.split(","));
				System.out.println(scout4InfoList);
				
				String s4 = scout4InfoList.get(0);
				System.out.println("S4 is: " + scoutInfo4);	
				
				String c4 = companion4Textbox.getText();
				String ch4 = (String)companion4Hours.getValue();
				myScoutCollection.retrieveScout(s4);
				myScout = myScoutCollection.retrieve(s4);
				myShift = new Shift(myScout, openedSession, c4, ch4);
			}

			//myScout = new Scout(s1);
			//myShift = new Shift(myScout, mySession, c1);
			//For each scout selected, update shift in database
			mySession.done();
			//statusLog.setText("Session opened successfully");
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(rb.getString("TitleMsg"));
			alert.setHeaderText(rb.getString("OpenSuccessMsg"));
			//alert.setContentText("There is a session that is currently open.\nPlease close the session and try again.");
			alert.show();
		});
		
		backButton = new Button(rb.getString("BackButton"));
		backButton.setOnAction(e -> {
			mySession.done();
		});
		
		container.getChildren().add(createStatusLog("                          "));
		grid.add(submitButton, 1, 12);
		grid.add(backButton, 2, 12);

		return grid;
    }

	private GridPane createSelectContentsForClose()
	{
		Session openSession = mySession.getOpenSession();
		if(openSession != null)
		{
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25, 25, 25, 25));
			grid.setStyle("-fx-background-style: solid;"
	                + "-fx-background-color: #2B2B2B");
			
			String startDate = (String)openSession.getState("StartDate");
			String startTime = (String)openSession.getState("StartTime");
			String endTime = (String)openSession.getState("EndTime");
			String startCash = (String)openSession.getState("StartingCash");
			String notes = (String)openSession.getState("Notes");
			
			startDateTextbox = new TextField();
			startDateTextbox.setText(startDate);
			startDateTextbox.setDisable(true);
			startTimeTextbox = new TextField();
			startTimeTextbox.setText(startTime);
			startTimeTextbox.setDisable(true);
			endTimeTextbox = new TextField();
			endTimeTextbox.setText(endTime);
			endTimeTextbox.setDisable(true);
			startCashTextbox = new TextField();
			startCashTextbox.setText(startCash);
			startCashTextbox.setDisable(true);
			notesTextbox = new TextField();
			notesTextbox.setText(notes);
			//notesTextbox.setDisable(true);
			endCashTextbox = new TextField();
			Integer tmp = Integer.parseInt(mySession.getEndCash()) + Integer.parseInt(startCash);
			String endCashSum = tmp.toString();
			endCashTextbox.setText(endCashSum);
			endCashTextbox.setDisable(true);

			String endchecksSum = mySession.getEndChecks();
			totalChecksTextbox = new TextField();
			totalChecksTextbox.setText(endchecksSum);
			totalChecksTextbox.setDisable(true);
			
			createHBox(grid, rb.getString("StartDate"), startDateTextbox, 1);
			createHBox(grid, rb.getString("StartTime"), startTimeTextbox, 2);
			createHBox(grid, rb.getString("EndTime"), endTimeTextbox, 3);
			createHBox(grid, rb.getString("StartCash"), startCashTextbox, 4);
			createHBox(grid, rb.getString("EndCash"), endCashTextbox, 5);
			createHBox(grid, rb.getString("TotalChecks"), totalChecksTextbox, 6);
			createHBox(grid, rb.getString("Notes"), notesTextbox, 7);
			
			submitButton = new Button(rb.getString("SubmitButton"));
			submitButton.setOnAction(e -> {
				// Close the currently open session
				String endCash = endCashTextbox.getText();
				String totalChecks = totalChecksTextbox.getText();
				openSession.closeSession(endCash, totalChecks);
				
				
				mySession.done();
				//statusLog.setText("Session closed successfully");
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(rb.getString("TitleMsg"));
				alert.setHeaderText(rb.getString("CloseSuccessMsg"));
				//alert.setContentText("There is a session that is currently open.\nPlease close the session and try again.");
				alert.show();
			});
			
			backButton = new Button(rb.getString("BackButton"));
	 		backButton.setOnAction(e -> {
	 			mySession.done();
	        });
	
	 		container.getChildren().add(createStatusLog("                          "));
			grid.add(submitButton, 1, 9);
			grid.add(backButton, 2, 9);
		
			return grid;
		}
		else
		{
			System.out.println("No open session! Please open one first.");
			return null;
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
		statusLog.setTextFill(Color.WHITE);
		statusLog.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		return statusLog;
	}

	//-------------------------------------------------------------


	public void updateState(String key, Object value) {
		if (key.equals("LoginError") == true)
			displayErrorMessage((String)value);

	}
	
//Error message 
	public void displayErrorMessage(String message) {
		//statusLog.displayErrorMessage(message);
	}

	public void clearErrorMessage() {
		//statusLog.clearErrorMessage();
	}
}
