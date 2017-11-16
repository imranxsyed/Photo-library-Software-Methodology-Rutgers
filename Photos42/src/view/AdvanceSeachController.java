package view;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

public class AdvanceSeachController {
	
	@FXML ListView<ImageView> lView;
	@FXML TextField tag1,tag2,tag3,tag4,tag5, dateTo,dateFrom;
	@FXML Button searchButton,resetButton, createButton,logoutButton, backButton;
	@FXML MenuButton selectionButton;
	
	
	private Scene scene;
	private boolean madeChanges;
	private Stage window;
	private MenuItem  Date;
	private MenuItem  Tags;
	ObservableList<User> users;
	ObservableList<ImageView> items;
	
	
	
	public void start(Stage window , Scene prev , Parent root, ObservableList<Album> albums, User user, ObservableList<User> users,boolean madeChanges){
		
		//initializing global variables
		this.madeChanges=madeChanges;
		this.window = window;
		this.users=users;
		items = FXCollections.observableArrayList();
		
		//setting up Menu button
		selectionButton.getItems().clear();
		selectionButton.getItems().addAll((Date= new MenuItem("By Date")), (Tags = new MenuItem("By Tags")));
		
		
		//setting a new Scene
		scene = new Scene(root);
		window.setScene(scene);
		
		//close request and logout button
		window.setOnCloseRequest(e->saveUsers(albums,user, users));
		logoutButton.setOnAction(e->{
			saveUsers(albums,user,users);
			this.window.close();
			});
		
		
		//takes back to the previous window
		backButton.setOnAction(e->this.window.setScene(prev));
		
		//resetting the fields
		resetButton.setOnAction(e->resetFields());
		
		//Menuitems Search criteria
		Date.setOnAction(e->menuItemPressed(e));
		Tags.setOnAction(e->menuItemPressed(e));
		
		//serching for an item
		searchButton.setOnAction(e->searchButtonPressed());
		
		
		
	}
	
	private void searchButtonPressed(){
		
		ArrayList<String> tags = new  ArrayList<String>(5);
		int[] dates = new int[6];
		
		
		//if it is by Tags
		
		if (!tag1.isDisable()){
				
			//getting all the tags
			
				if (!tag1.getText().equals("")){
					
					tags.add(tag1.getText());
				}
				if (!tag2.getText().equals("")){
					
					tags.add(tag2.getText());
				}
				if (!tag3.getText().equals("")){
					
					tags.add(tag3.getText());
				}
				if (!tag4.getText().equals("")){
	
					tags.add(tag4.getText());
				}
				if (!tag5.getText().equals("")){
	
					tags.add(tag5.getText());
				}
				
				
			searchByTags(tags);
		}
		else{
			
			if (dateTo.equals("")|| dateFrom.equals("")){
				
				LoginController.Alerts("Invalid Input", "you specify date range");
				return;
			}
			
			//parsing dates
			
			try{
				
				StringTokenizer st1 = new StringTokenizer(dateFrom.getText(), "/");
				
				int month = Integer.parseInt(st1.nextToken());
				int day =  Integer.parseInt(st1.nextToken());
				int year =  Integer.parseInt(st1.nextToken());
				
				Calendar calendar = Calendar.getInstance();
				
				//checking for the validity of the month day and year
				
				if((month<= 00 || (month >12 || (calendar.get(Calendar.YEAR)==year && month> calendar.get(Calendar.MONTH)+1)))
						||(day <=00 || (day>31) || (calendar.get(Calendar.YEAR)==year && month== calendar.get(Calendar.MONTH)+1 && day> calendar.get(Calendar.DATE)))
						|| (year> calendar.get(Calendar.YEAR) || year <1000)){
					
					System.out.println("Year: "+ calendar.get(Calendar.YEAR)+"\nDay: "+ calendar.get(Calendar.DATE)+"\nMonth: "+ calendar.get(Calendar.MONTH));
					
					LoginController.Alerts("Invalid Input", "You Must Enter valid dates");
					return;
				} 
				dates[0] = month; 
				dates[1]= day;
				dates[2]=year;
				
				
				/*
				if (day <=1 || day>31){
					
					return;
				}
				if (year> 2017 || year <1000){
					
					return;
				}*/
				//adding to the arrays
				
				 st1 = new StringTokenizer(dateTo.getText(), "/");
				 
				  month = Integer.parseInt(st1.nextToken());
				  day =  Integer.parseInt(st1.nextToken());
				  year =  Integer.parseInt(st1.nextToken());
				
				  
				//checking for the validity of the month day and year
					
				  if((month<= 00 || (month >12 || (calendar.get(Calendar.YEAR)==year && month> calendar.get(Calendar.MONTH)+1)))
							||(day <=00 || (day>31) || (calendar.get(Calendar.YEAR)==year && month== calendar.get(Calendar.MONTH)+1 && day> calendar.get(Calendar.DATE)))
							|| (year> calendar.get(Calendar.YEAR) || year <1000)){
						LoginController.Alerts("Invalid Input", "You Must Enter valid dates");
						return;
					}
					
					dates[3]= month;
					dates[4]= day;
					dates[5] = year;
					
				/*	for (Integer i: dates){
						
						System.out.println(i);
					}
					*/ 
					dateTo.clear();
					dateFrom.clear();
					searchByRange(dates);
					
					return;
					
					
				
			}catch(Exception e){
				
				LoginController.Alerts("Invalid Input", "You Must input the correct integers date in the following format\nMM/DD/YYYY");
				dateTo.clear();
				dateFrom.clear();
				return;
			}
			
			
		}
	}
	
	private void searchByTags(ArrayList<String> tags){
		
		int num_of_users = users.size();
		Album album=null;
		Photo photo= null;
		ArrayList<Photo> photos = new ArrayList<Photo>(); // for the new album
		
		//getting the users
		for (int i=0 ;i< num_of_users; i++){
			
			
			ObservableList<Album> albums = users.get(i).getAlbums();
			int num_of_albums = albums.size();
			
			//getting the albums
			for (int k=0 ; k< num_of_albums; k++){
				
				
				//getting the photos
				album = albums.get(k);
				int num_of_photos = album.getPhotos().size();
				
				for (int l=0 ; l< num_of_photos; l++){
					
					//each photo
					photo =  album.getPhotos().get(l);
					
					
					/**
					 * just need to figure out if the photo contains gives tags					
					 */
					if (true){
						
						//at this time photos is in the range
						
						photos.add(photo);
						
						//adding to the imageview
						ImageView imgView = new ImageView();
						imgView.setImage(new Image(photo.getUrl()));
						
						//adding to the listView
						items.add(imgView);
						lView.setItems(items);
					}
					
					
				}//arraylist of photos loop
				
			} // list of albums loop
			
		} //list of user loop
		
		//no picture found
		if (items.isEmpty()){
			
			LoginController.Alerts("Empty", "No Pics In the given ranges were found in Albums");
			return;
		}
	}
	
	private void searchByRange(int[] Dates){
		
		int num_of_users = users.size();
		Album album=null;
		Photo photo= null;
		ArrayList<Photo> photos = new ArrayList<Photo>(); // for the new album
		
		//getting the users
		for (int i=0 ;i< num_of_users; i++){
			
			
			ObservableList<Album> albums = users.get(i).getAlbums();
			int num_of_albums = albums.size();
			
			//getting the albums
			for (int k=0 ; k< num_of_albums; k++){
				
				
				//getting the photos
				album = albums.get(k);
				int num_of_photos = album.getPhotos().size();
				
				for (int l=0 ; l< num_of_photos; l++){
					
					//each photo
					photo =  album.getPhotos().get(l);
					
					//checking for the ranges now
					
					String date = photo.getDate();
					
					//tokenizing the photo date
					
					StringTokenizer st = new StringTokenizer(date,"/");
					
					int year = Integer.parseInt(st.nextToken());
					int month = Integer.parseInt(st.nextToken());
					int day = Integer.parseInt(st.nextToken());
					
					
					
					//checking the constrains
					if ((year>= Dates[2] 	//checking year
						&& month>= Dates[0]	//cheking month
							&& day>= Dates[1])//checking day
							 //for to date
						&& year<= Dates[5] 	//checking year
								&& month<=Dates[3]	//cheking month
										&& day<= Dates[4]){ // checking day
						
						//at this time photos is in the range
						
						photos.add(photo);
						
						//adding to the imageview
						ImageView imgView = new ImageView();
						imgView.setImage(new Image(photo.getUrl()));
						
						//adding to the listView
						items.add(imgView);
						lView.setItems(items);
					}
					
					
				}//arraylist of photos loop
				
			} // list of albums loop
			
		} //list of user loop
		
		//no picture found
		if (items.isEmpty()){
			
			LoginController.Alerts("Empty", "No Pics In the given ranges were found in Albums");
			return;
		}
	}
	
	private void menuItemPressed(ActionEvent e){
		
		if ((MenuItem)e.getSource()== Date){
			
			selectionButton.setText("By Date");
			
			//disabling all the tags firs
			tag1.setDisable(true);
			tag2.setDisable(true);
			tag3.setDisable(true);
			tag4.setDisable(true);
			tag5.setDisable(true);
			
			//enabling dates fields
			dateTo.setDisable(false);
			dateFrom.setDisable(false);
		}else{
			
			selectionButton.setText("By Tags");
			
			//disabling dates fields
			dateTo.setDisable(true);
			dateFrom.setDisable(true);
			
			//enabling the tags firs
			tag1.setDisable(false);
			tag2.setDisable(false);
			tag3.setDisable(false);
			tag4.setDisable(false);
			tag5.setDisable(false);
		}
		
		//search and reset button gets enabled in either cases
		searchButton.setDisable(false);
		resetButton.setDisable(false);
	}
	
	private void resetFields(){
		
		tag1.clear();
		tag2.clear();
		tag3.clear();
		tag4.clear();
		tag5.clear();
		dateTo.clear();
		dateFrom.clear();
		
		return;
	}
	
	private void saveUsers(ObservableList<Album> albums, User user, ObservableList<User> items){
		
		//no changes were made meaning does not need to save all over again
		if (!madeChanges){
			
			System.out.println("Did not save");
			return;
		}
		
		try{
			
			//adding the new data
			user.setAlbums(albums);
			items.remove(user);
			items.add(user);
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
			
			
			System.out.println("Exception caught\nClass: AdvanceSearchController\nMethod: saveUsers\nExpected: false");
			System.out.println(e.getMessage());
		}
	}
}
