import model.*; 
import userinterface.*;
import userinterface.MainStageContainer; 
import userinterface.WindowPosition;

import java.util.Locale;

import event.Event; 
import javafx.event.EventHandler; 
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Assign3 extends Application
{
	private MainScreen	myMainScreen;
	private Stage		mainStage;

	public void start(Stage primaryStage) {
		System.out.println("Tree Sales Program Prototype Version");
		System.out.println("Copyright 2015/2016 Better Than Them Team");
		MainStageContainer.setStage(primaryStage, "Tree Sales Program");
		mainStage = MainStageContainer.getInstance();
		mainStage.setOnCloseRequest(new EventHandler <javafx.stage.WindowEvent>() {
			@Override
			public void handle(javafx.stage.WindowEvent event) {
				System.exit(0);
			}
		});
		try {
			myMainScreen = new MainScreen();
		}
		catch (Exception e) {
			System.err.println("The program cannot be executed");
			new Event(Event.getLeafLevelClassName(this), "Assign3.<init>", "Unable to create MainScreen object", Event.ERROR);
			e.printStackTrace();
		}
		WindowPosition.placeCenter(mainStage); 
		mainStage.show();
		
		//Locale language = new Locale("en", "US");
		//set locale here (use singleton) to set global like DBconfig.ini but contains the local for all GUIs to see. (locale updated when user selects different language)

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}