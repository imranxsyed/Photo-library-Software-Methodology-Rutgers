package app;

import java.io.IOException;
/*
import application.GallaryController;
import view.Controller;
import view.PhotoDirectController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;*/


import javafx.application.Application;
import view.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class PhotosApp extends Application {
	
	Stage mainStage;
	
	@Override
	public void start(Stage primaryStage) throws IOException{
	/*	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/PhotosDirectory.fxml"));
		primaryStage.setTitle("Gallary library");
		AnchorPane root = (AnchorPane)loader.load();
		PhotoDirectController galController = loader.getController();
		Scene scene = new Scene(root,800,500);
		
		try{
		galController.startPhoto(primaryStage, scene, root, new User("imran"), new Album("album1","000000"));
		
		}catch(Exception e){}
		
		
		//scene.getStylesheets().add("application.css");
		primaryStage.setScene(scene);
		primaryStage.show();
		*/
		
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/LoginPage.fxml"));
			AnchorPane root = loader.load();
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add("software.css");
			// start up the calculator
			LoginController Controller = loader.getController();
			Controller.start(primaryStage, scene);
			
			
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}