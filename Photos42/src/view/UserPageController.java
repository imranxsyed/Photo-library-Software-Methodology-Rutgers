package view;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;
import model.Album;
import model.User;

public class UserPageController {
	
	@FXML TableView<Album> albumList;
	@FXML Button renameButton,logoutButton,deleteButton,createAlbumButton,openButton;
	@FXML TextField albumTF,renTF;
	private User user; // current user
	private ObservableList<User> items; //list for users
	private ObservableList<Album> albums; // list for albums
	private Stage window;// the main window
	private Scene scene; // its scene
	private  boolean madeChanges = false; // item tells weather any changes were made in this window or not
	
	
	public void start(Stage window , Scene previous, Parent root, User user, ObservableList<User> items){
		
		this.window = window;
		this.items = items;
		this.user = user;
		this.scene = new Scene(root);
		
		
		//getting the users album
		albums = user.getAlbums();
		
		
		//filling list view
		fillListView();
		
		//setting the scene
		window.setScene(scene);
		
		
		//logout button action
		logoutButton.setOnAction(e->{	
		saveUsers();
		window.setScene(previous);
		});
		
		//close action
		window.setOnCloseRequest(e-> saveUsers());
		
		//creating an album
		createAlbumButton.setOnAction(e-> createAlbum());
		
		
		//deleting an album
		deleteButton.setOnAction(e-> deleteAlbum());
		
		
		//renaming an Album
		renameButton.setOnAction(e->renameAlbum());
		
		
		//Open an album
		openButton.setOnAction(e->openAlbum());
		
		/*
		//seach page
		
		searchButton.setOnAction(e->searchButtonPressed());
		*/
		
	}
	
	/*
	   private void searchButtonPressed(){
		
		load("AdvanceSearch.fxml", null);
	}
	*/
	
	private void openAlbum(){
		
		//if the direcory is empty
		if (albums.isEmpty()){
			
			LoginController.Alerts("Empty Directory", "No Album Found in User's Directory");
			return;
		}
		
		//if no item was selected
		Album item = (Album)albumList.getSelectionModel().getSelectedItem();
		
		if (item==null){
			
			LoginController.Alerts("Selection Error", "You Must select an album");
			return;
		}
	//	System.out.println(item.getName());
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("PhotosDirectory.fxml"));
		window.setTitle("PhotoGallary");
		
		try{
			Parent root = loader.load();
			PhotoDirectController songController = loader.getController();
		
			try{
				songController.startPhoto(window, scene, root,user, item,this.items, this.albums);
		
			}catch(Exception e){}
		
			
		}catch(Exception e){}
		//load("PhotosDirectory.fxml", item);
	}
	
	
	/*private void load(String name, Album item) {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(name));
		
		
		try{
			Parent root =loader.load();
			
			//sending the photo directory screen
			if (item!=null){
				
				PhotoDirectController controller =loader.getController();
				//sending the current album
				controller.startPhoto(this.window, scene, root, this.user);
			}
			//sending the search screen
			else{
				
				AdvanceSeachController controller  = loader.getController();
				
				
				controller.start(this.window, scene, root, albums, this.user, this.items, this.madeChanges );
			}
			
			
			
			
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println("Exception Caught loading the PhotoDirectory Controller");
			
		}
		
	}
	*/
	
	private void renameAlbum(){
		
		String new_Name = renTF.getText();
		renTF.clear();
		
		//no name provided
		if (new_Name.isEmpty()){
			
			LoginController.Alerts("Invalid Input", "You must enter the new name");
			return;
		}
		
		Album item = albumList.getSelectionModel().getSelectedItem();
		
		//if no item is selected
		
		if (item==null){
			
			LoginController.Alerts("Selection Error", "You Must Select An Album");
			return;
		}
		
		//if the new name already exists
		if (albums.contains(new Album(new_Name,""))){
			
			LoginController.Alerts("Duplicate Error", "Album with this name already exists");
			return;
		}
		
		//removing item
		albums.remove(item);
		
		//putting the item with the new name
		item.setName(new_Name);
		albums.add(item);
		
		//selecting the modified item
		albumList.getSelectionModel().select(item);
		madeChanges = true;
		return;
	}
	
	private void deleteAlbum(){
		
		
		//if the list is empty
		
		if (albums.isEmpty()){
			
			LoginController.Alerts("Empty Directory", "No item found in the user's directory");
			return;
		}
		
		
		Album item=	albumList.getSelectionModel().getSelectedItem();
		
		//if no item was selected
		
		if (item==null){
			
			LoginController.Alerts("Selection Error", "You must select an Album");
			return;
		}
		
		//delete the item
		
		albums.remove(item);
		madeChanges = true;
		return;
	}
	
	private void createAlbum(){
		
		String albumName = albumTF.getText();
		albumTF.clear();
		
		//if no input was detected 
		if (albumName.isEmpty()){
			
			LoginController.Alerts("Invalid Input", "No Input was detected!");
			return;			
		}
		
		//get the calender date
		Calendar date = Calendar.getInstance();
		String month = Integer.toString(date.get(Calendar.MONTH)+1);
		String day = Integer.toString(date.get(Calendar.DATE));
		String year = Integer.toString(date.get(Calendar.YEAR));
		
		//creating a new album 
		
		Album item = new Album (albumName,month+"/"+day+"/"+ year);
		
		//adding the album into the current list user
		
		if (!albums.contains(item)){
			
			this.albums.add(item);
			albumList.getSelectionModel().select(item);
			madeChanges = true;
			this.user.setAlbums(this.albums);
			return;
			
		}
		
		//the item already exists
		
		LoginController.Alerts("Duplicate", "The Album already exists");
		
		
		
		
	
		
	}
	
	private void fillListView(){
		
		
		
		TableColumn<Album, String> nameColumn =  new TableColumn<>("Name");
		nameColumn.setMinWidth(150);
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<Album, String> dataColumn =  new TableColumn<>("From - To (Date)");
		dataColumn.setMinWidth(230);
		dataColumn.setCellValueFactory(new PropertyValueFactory<>("comDate"));
		
		TableColumn<Album, Integer> numColumn =  new TableColumn<>("#Pics");
		numColumn.setMinWidth(150);
		numColumn.setCellValueFactory(new PropertyValueFactory<>("number_of_pics"));
		
		albumList.setItems(this.albums);
		albumList.getColumns().addAll(nameColumn,dataColumn,numColumn);
		
	
		
	
	}

	public void saveUsers(){
		
		//no changes were made meaning does not need to save all over again
		if (!madeChanges){
			
			//System.out.println("Did not save");
			return;
		}
		
		try{
			
			//adding the new data
			this.user.setAlbums(this.albums);
			items.remove(this.user);
			items.add(this.user);
			//taking out the previous user
			
			ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream("AdminData.dat"));
			User item = null;
			
			int length = items.size();
			
			for (int i=0 ; i<length;i++){
				
				item = items.get(i);
				ois.writeObject(item);
			}
			ois.close();
			
			
			
			
		}catch(Exception e){
			
			
			System.out.println("Exception caught\nClass: UserPageController\nMethod: saveUsers\nExpected: false");
			System.out.println(e.getMessage());
		}
		
	}
}
