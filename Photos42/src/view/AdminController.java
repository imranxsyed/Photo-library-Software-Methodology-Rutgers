package view;

import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

public class AdminController {

	@FXML Button logoutButton, createButton, deleteButton;
	@FXML TextField nameField;
	@FXML ListView<User> usersList;
	
	ObservableList<User> items = FXCollections.observableArrayList();
	
	
	public void start(Stage window, Scene previous , Parent root, ObservableList<User> items, ListView<User> usersList){
		
		
		
		File file = new File("AdminData.dat");
		
		//if no users does not exist
		
		if (!file.exists()){
			
			window.setScene(new Scene(root));
			
		}else{ //getting all the user
			
			this.items = items;
			this.usersList.setItems(this.items);
			window.setScene(new Scene(root));
		
		}
		
		window.setOnCloseRequest(e->saveUsers()); // close requent
		
		createButton.setOnAction(e->createUser()); // creating a user
		
		deleteButton.setOnAction(e->deleteUser());
			
			
		logoutButton.setOnAction(e->{//going back to Login screen
			
			saveUsers();
			window.setScene(previous);
		});
		
	}
	
	
	private void deleteUser(){
		
		
		User item = usersList.getSelectionModel().getSelectedItem();
		
		if (item == null){ // no item is selected
			
			LoginController.Alerts("Error", "No User Is selected");
			return;
		}
		File file = new File(item.getName().toLowerCase());//deleting the file
		//file.delete();//deleting the file
		items.remove(item);
		usersList.setItems(items);
	}
	
	
	private void createUser(){
		
		String input = nameField.getText();
		nameField.clear();
		
		if (input.equals("")){ // no input provided
			
			LoginController.Alerts("Error", "You must enter the name of the new User");
			return;
		}
		input = input.toLowerCase();
	//	File file = new File(input);
		
		int index  = items.indexOf(new User(input));
		
		if (index<0){
			try{
				
				//file.createNewFile();
				User item = new User(input);
				items.add(item);
				usersList.setItems(items);
				
				return;
			}
			catch(Exception e){
				
				System.out.println("Problem making a file");
			}
			
		}
		LoginController.Alerts("Error", "The User Already Exists");
	}

	
	/*private void getUsers(){
		
		try{
			
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("AdminData.dat"));
			User item = null;
			
			while ((item=(User)ois.readObject())!=null){
				
				items.add(item);
				usersList.setItems(items);
			}
			
			ois.close();
			
			
		}catch(Exception e){
			
			usersList.setItems(items);
			System.out.println("Exception caught\nClass: AdminController\nMethod: getUsers\nExcepted: true");
		}
		
		
	}*/
	
	
	private void saveUsers(){
		
		try{
			
			
			ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream("AdminData.dat"));
			User item = null;
			
			int length = items.size();
			
			for (int i=0 ; i<length;i++){
				
				item = items.get(i);
				ois.writeObject(item);
			}
			ois.close();
			
			
			
			
		}catch(Exception e){
			
			
			System.out.println("Exception caught\nClass: AdminController\nMethod: saveUsers\nExpected: false");
			System.out.println(e.getMessage());
		}
		
	}
	
}
