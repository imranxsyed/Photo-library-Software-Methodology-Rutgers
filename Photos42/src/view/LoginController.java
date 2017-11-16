package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;

public class LoginController {
	
	
	@FXML Button LButton,closeButton;
	@FXML TextField TField;
	@FXML Text TName;
	
	Stage window;
	Scene current;
	
	ObservableList<User> items = FXCollections.observableArrayList();
	ListView<User> usersList  = new ListView<User>();
	
	
	public void start(Stage window, Scene current){
		
		this.window = window;
		this.current = current;
		
		getUsers();
		
		LButton.setOnAction(e->LoginButtonPressed());
		
		closeButton.setOnAction(e-> System.exit(0));
		
		
	}
	private void getUsers(){
		
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
			//System.out.println("Exception caught\nClass: AdminController\nMethod: getUsers\nExcepted: true");
		}
		
		
	}
	
	private void LoginButtonPressed() {
		
		String userName = TField.getText();
		TField.clear();
		
		//if the textField is Empty
		if (userName.equals("")){
			
			
			return;
		}
		//admins profile should open
		if (userName .equalsIgnoreCase("Admin")){
			
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("AdminPage.fxml"));
			
			
			//sending it to the admin page
			try{
			Parent root = loader.load();
			AdminController controller = loader.getController();
			
			controller.start(window,current, root, items,usersList );
			
			}catch(Exception e){
				System.out.println("Exception caught\nClass LoginController, Line 58");
				
			}
			
			
		}else{
			
			//file name is same as users
			
			//File file = new File(userName);//
			
			int index = items.indexOf(new User(userName.toLowerCase()));
			//checking file means checking if user exists
			if (index<0){
				
				Alerts("Error", "The Specified User Not Found");
				return;
			}
			
			//meaning file exists
			
			try{
				

				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("UserPage.fxml"));
				
				
				//sending it to the user page
				try{
				Parent root = loader.load();
				
				User user =items.get(index);
				
				UserPageController controller = loader.getController();
				controller.start(window, current, root, user,items);
				
				
				
				}catch(Exception e){
					System.out.println("Exception caught\nClass LoginController, Line 58");
					
				}
				
			}catch(Exception e){
				
				
				
			}
		}
		
	} 
	public static  void Alerts(String title, String message){
		
		
		Alert userNotFound;
		
		userNotFound = new Alert(AlertType.ERROR);
		userNotFound.setHeaderText(null);
		userNotFound.setContentText(message);
		userNotFound.setResizable(false);
		userNotFound.setTitle(title);
		userNotFound.showAndWait();
		
	}

}
