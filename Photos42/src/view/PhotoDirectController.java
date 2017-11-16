package view;


	import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.event.ActionEvent;
	import javafx.event.EventHandler;
	import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
	import javafx.geometry.Insets;
	import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
	import javafx.scene.control.ChoiceBox;
	import javafx.scene.control.Label;
	import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
	import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
	import javafx.scene.layout.AnchorPane;
	import javafx.scene.layout.BorderPane;
	import javafx.scene.layout.ColumnConstraints;
	import javafx.scene.layout.GridPane;
	import javafx.scene.layout.HBox;
	import javafx.scene.layout.Pane;
	import javafx.scene.layout.RowConstraints;
	import javafx.scene.layout.TilePane;
	import javafx.scene.layout.VBox;
	import javafx.scene.text.Font;
	import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Identity;
import model.Key;
import model.Photo;
import model.User;
	/**
	 * When an album is other all photos in the album will be shown here. All photos will be displayed in thumbnail form type ImageView. Each
	 * ImageView is assigned an id generated from the the url of the image and the id is linked to a Photo object of the image containing all 
	 * information associated with the image. 
	 * @author Pedro Cruz
	 *
	 */
	public class PhotoDirectController {
		
		//label at the top of the page
		@FXML Label albumName;
		@FXML Button prevPage;
		
		//when thumbnail representation of photos will be
		@FXML ScrollPane photoPane;
		
		//Controls for Photo Slide show
		@FXML ImageView photoDisplay;
		@FXML Button nextPhoto;
		@FXML Button prevPhoto;
		
		//caption/re-caption control
		@FXML Button captionButton;
		@FXML TextArea captionDisplay;
		@FXML Label dateOfPhoto;
		
		//General Photo Control 
		@FXML Button addPhoto;
		@FXML Button copyPhoto;
		@FXML Button deletePhoto;
		@FXML Button pastePhoto;
		@FXML Button movePhotoButton;
		@FXML TextField moveToField;
		
		//search control
		@FXML TextField fromDate;
		@FXML TextField toDate;
		@FXML Button searchButton;
		@FXML Button clearSearch;
		@FXML ChoiceBox<String> allTags;
		@FXML ChoiceBox<String> allKeys;
		
		//tags and keys control in display
		@FXML Button addKeyToTag;
		@FXML Button deleteKeyFromTag;
		@FXML Button deleteTags;
		@FXML ChoiceBox<String> tagsSelectionList;
		@FXML ListView<String> tagsKeyList;
		@FXML TextField keyToAdd;
		@FXML Button addTag;
		@FXML TextField tagToAdd;
		@FXML ChoiceBox<String> albumSelection;
		
		@FXML TextField newAlbum;
		@FXML Button createAlbum;
		Stage stage;
		Scene scene;
		private ArrayList<ImageView> imgs;
		private ArrayList<Photo> allPhotos;
		private ArrayList<Photo> subPhotos; 
		private ArrayList<Identity> tags;
		private int photoIndex;
		int size;
		User user;
		Album userAlbum;
		protected Album albumSelected;
		Photo currPhoto;
		Identity currLabel;
		Key currKey;
		ArrayList<Album> albums;
		Photo photoCopy;
		ArrayList<Album> userAlbumList;
		ObservableList<Album> oslAlbums;

		

		ObservableList<String> albumList = FXCollections.observableArrayList();
		ObservableList<String> allTagsList = FXCollections.observableArrayList();
		ObservableList<User> users;
		
		public void startPhoto(Stage stage, Scene previous , Parent root, User user, Album album,ObservableList<User> users, ObservableList<Album> obListAlbums) {
			
			oslAlbums = obListAlbums; 
			this.stage = stage;
			this.user = user;
			this.users = users;
			albums = user.getUserAlbum();
			userAlbum = album;
			userAlbumList = user.getUserAlbum();
			this.scene = new Scene(root);
			stage.setScene(scene);
			albumName.setText(userAlbum.getName());
			prevPage.setOnAction(e -> {
				saveUsers();
				stage.setScene(previous);
			});
			
			this.createAlbum.setOnAction(e->createAlbum(e));
			stage.setOnCloseRequest(e->saveUsers());
			initialize();
			
			updateTagSelection();
			updateAlbumSelection();
			allTags.setOnAction(e -> {
						int t = allTags.getSelectionModel().getSelectedIndex();
						if(t <0 ||tags==null ||tags.size()==0) return;
						
						 Identity searchTag = tags.get(t);
						 showKeyList(searchTag);
					});
			albumSelection.setOnAction(e->{
					int i = albumSelection.getSelectionModel().getSelectedIndex();
					if(i<0 || userAlbumList==null || userAlbumList.size() ==0){ return;}
					albumSelected = user.getAlbumList().get(i);
			});
		}
		
		
	
		/**
		 * Any changes made by the user will be update and save upon closer of the application or return to album selection.
		 */
		
		private void saveUsers(){
			
			try{
				
				//setting the new photos and observableList
				userAlbum.setAlbum(allPhotos);
				userAlbum.setNumber_of_pics(allPhotos.size());
				userAlbum.upDateTags(tags);
				
				
				//setting new Dates if there is any
				
				//if no pics was added or nothing is in the album
				if (allPhotos.size()==0){
					
					//System.out.println("Size 0 statement ran");
					//do nothing
				}
				else{
					
					//date of the last pic that was added
					String lastAlbumDate = allPhotos.get(allPhotos.size()-1).getDate();
				
				
				//setting the right date of the album in the right format
					StringTokenizer st = new StringTokenizer(lastAlbumDate, " /");
					
					String year = st.nextToken();
					String month = st.nextToken();
					String day = st.nextToken();
					
					//setting up date
					userAlbum.setDate(month+"/"+ day+"/" + year);
					
				}
				
				
				//deleting the previous album and ObservableList
				albums.remove(userAlbum);
				oslAlbums.remove(userAlbum);
				
				
				
				//adding the album 
				albums.add(userAlbum);
				oslAlbums.add(userAlbum);
				
				this.user.setAlbums(albums);
				
				users.remove(this.user);
				users.add(this.user);
				//taking out the previous user
				
				ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream("AdminData.dat"));
				User item = null;
				
				int length = users.size();
				
				for (int i=0 ; i<length;i++){
					
					item = users.get(i);
					ois.writeObject(item);
				}
				ois.close();
				
				//System.out.println("Saved Sucessfully");
				
				
			}catch(Exception e){
				
				
				System.out.println("Exception caught\nClass: UserPageController\nMethod: saveUsers\nExpected: false");
				System.out.println(e.getMessage());
			}

			
		}
		
		/**
		 * Upon running the application all variables: imgs (ArrayList), allPhotos, tags, and subPhotos are generated. Once conpleted,
		 * a display of all the photos will be shown as thumbnails in the display
		 */
		private void initialize(){
			
			imgs = new ArrayList<ImageView>();
			allPhotos = new ArrayList<Photo>();
			tags = new ArrayList<Identity>();
			subPhotos = new ArrayList<Photo>();
			
			
			if(user.getName().compareTo("stock")==0 && (userAlbum.getName().compareTo("stock")==0 && userAlbum.getPhotos().size()==0)){
				photoIndex = 0;
				size = 3;
				Photo photo1 = new Photo("http://platowebdesign.com/articles/wp-content/uploads/2014/10/public-domain-images-free-stock-photos-light-sky-silo-windows-lillyphotographer-1024x684.jpg");
				Photo photo2 = new Photo("http://media02.hongkiat.com/fruits-vege-stock-photos/highres/fruitsvege-stock04.jpg");
				Photo photo3 = new Photo("http://img.timeinc.net/time/photoessays/2010/dennis_stock/dennis_stock_01.jpg");
			
				allPhotos.add(photo1);
				allPhotos.add(photo2);
				allPhotos.add(photo3);
				subPhotos.addAll(allPhotos);
				showThumbNails();
				return;
			}
			
			if(userAlbum ==null || userAlbum.getPhotos().size()==0){
				photoIndex = -1;
				size = 0;
				return;
			}
			allPhotos.addAll(userAlbum.getPhotos());
			subPhotos.addAll(allPhotos);
			setTags();
			
			size = allPhotos.size();
			photoIndex = 0;
			showThumbNails();
		}
		/**
		 * Tags are generated from collection information from all photos in the Album object arraylist of Photo(s). Should tags be duplicated,
		 * then keys are looked to see if they are the same. Should they be the same, the Photos url is added to the key's array list
		 * 
		 */
		private void setTags(){
			int i = 0;
			while(i<allPhotos.size()){
				int j=0;
				ArrayList<Identity> holdTags = allPhotos.get(i).getTags();
				while(j<holdTags.size()){
					if(!containsTag(tags,holdTags.get(j))){
						tags.add(holdTags.get(j));
						sortTags(tags, 0, tags.size()-1);
					}else{
						int index = indexOfTag(tags, holdTags.get(j));
						Identity currTag = tags.get(index);
						
						ArrayList<Key> holdKeys = holdTags.get(j).getKeys();
						
						int k = 0;
						while(k<holdKeys.size()){
							Key key = holdKeys.get(k);
							if(!currTag.contains(key)){
								currTag.addKey(key.getKey(), allPhotos.get(i));
							}else{
								Key otherKey = currTag.getKeyContaining(key.getKey());
								otherKey.addPhoto(allPhotos.get(i));
							}
							k++;
						}
						
					}
					j++;
				}
				i++;
			}
		}
		
		/**
		 * ChoiceBox of album is updated when first initialized and when a user creates a new album out of search results
		 */
		private void updateAlbumSelection(){
			ArrayList<String> albumNames = new ArrayList<String>();
			albumList.clear();
			int i = 0;
			while(i < userAlbumList.size()){
				albumNames.add(userAlbumList.get(i).getName());
				i++;
			}
			albumList.addAll(albumNames);
			albumSelection.setItems(albumList);
			return;
		}
		/**
		 * Tag selection is used to generate a search criteria, should a user add  a new tag then tags have to be updated and hence 
		 * the user's tag selection is updated to the new list of tags
		 */
		private void updateTagSelection(){
			ArrayList<String> tagsNames = new ArrayList<String>();
			allTagsList.clear();
			if(tags==null || tags.size()==0){
				allTagsList.add(" ");
				tagsSelectionList.setItems(allTagsList);
				return;
			}
			int i = 0;
			while(i<tags.size()){
				tagsNames.add(tags.get(i).getTagName());
				i++;
			}
			allTagsList.addAll(tagsNames);
			
			allTags.setItems(allTagsList);
			return;
		}
		
		/**
		 * Shows the KeyList of when a selected tag is selected from Choice box
		 * @param searchMe
		 */
		
		
		private void showKeyList(Identity searchMe){
			if(searchMe==null){

				ObservableList<String> currkeys=FXCollections.observableArrayList();
				currkeys.add("");
				allKeys.setItems(currkeys);
				return;
			}
			
			Identity hold = searchMe;
			
			if(hold.getKeys()==null) return;
			ObservableList<String> currKeys=FXCollections.observableArrayList();
			ArrayList<Key> photosKeys = hold.getKeys();
			int i = 0;
			while(i<photosKeys.size()){
				currKeys.add(photosKeys.get(i).getKey());
				i++;
			}
			
			allKeys.setItems(currKeys);
			if(searchMe.getKeys()==null || searchMe.getKeys().size()==0){
				return;
			}
				
				allKeys.setOnAction( e -> {
					String t = allKeys.getSelectionModel().getSelectedItem();
					if(t==null) return;
					Key searchKey = searchMe.getKeyContaining(t); 
						if(searchKey==null){
							
							return;
						}
					searchByKey(searchKey);
				});
		}
		/**
		 * Once a tag and a key are selected from their ChoiceBoxes then the search result is displayed in the same display where
		 * all thumbnails are shown
		 * @param searchKey
		 */
		private void searchByKey(Key searchKey){
			if(searchKey==null){return;}
			
			ArrayList<String> phts = searchKey.getPhotos();
			subPhotos.clear();
			
			int j = 0;
			
			while(j<allPhotos.size()){
				int i = 0;
				while(i<phts.size()){
					if(allPhotos.get(j).getUrl().compareTo(phts.get(i))==0){
						subPhotos.add(allPhotos.get(j));
						i = phts.size();
					}
					i++;
				}
				j++;
			}
			
			//Removed this as it might become confusing to user
			/*
			allKeys.getSelectionModel().clearSelection();
			allTags.getSelectionModel().clearSelection();*/
			showThumbNails();
		}
		/**
		 * Adding a Photo is activated when the user clicks the "add" button. Users dictory is opened and the user must select an image,
		 * should the image not be of type acceptable, then an alert will be displayed. Should the photo already be in the current album,
		 * then another alert is shown. Stating that the album already contains the photo. If successful, the the photo is added and it is
		 * displayed on the displayed side; there the user can add caption, tag(s) and key(s).
		 * @param e
		 */
		public void addImage(ActionEvent e){
			if((Button)e.getSource()!= addPhoto){
				return;
			}
			
			FileChooser fileChoose = new FileChooser();
			File file = fileChoose.showOpenDialog(stage);
			
			if (file == null){ // no files selected
				Alerts("Invalid Add", "No photo was selected");
				return;
			}
			String name = file.getName();


			if(!(name.contains("jpg")||name.contains("pnj")||name.contains("jpeg") || name.contains("PNG") ||name.contains("png")|| name.contains("JPG")|| name.contains("PNG"))){
				Alerts("Invalid input", "FIle is not a valid photo");

				return;
			}
			URI path = file.toURI();
			URL url = null;
			try {
				url = path.toURL();
			} catch (MalformedURLException e1) {
				
				e1.printStackTrace();
			}
			if(url==null){
				return;
			}
			String channel = url.toString();
			
			Photo photoToAdd = new Photo(channel);
			
			if(allPhotos!=null && allPhotos.size()!=0){
				if(indexOfPhoto(allPhotos, photoToAdd)!=-1){
					Alerts("Image Add Error", "Photo is currently in this album");
					return;
				}
			}
			
			allPhotos.add(new Photo(channel));
			size++;
			photoIndex = size-1;
			currPhoto = allPhotos.get(size-1);
			
			subPhotos.clear();
			subPhotos.addAll(allPhotos);
			showThumbNails();
		}
		/**
		 * Always active, allows the user to go to previous photo in the thumbnail display panel. The slide show pigbacks from allPhotos and subphotos;
		 * subphotos are the current search results in display. This is done when the user clicks on the left button of the display image.
		 * @param e
		 */
		public void previous(ActionEvent e){
			
			if(subPhotos ==null || subPhotos.size() <1 || indexOfPhoto(subPhotos, currPhoto)==0){
				return;
			}
			int prevIndex = indexOfPhoto(subPhotos, currPhoto)-1;
			currPhoto = subPhotos.get(prevIndex);
			photoIndex =indexOfPhoto(allPhotos, currPhoto);
			DisplayPhoto(photoIndex);
		}
		/**
		 * Same as the previos button, allows the user to go to the next photo in sequential order by clicking on the button to the right of the 
		 * image display view
		 * @param e
		 */
		public void advance(ActionEvent e){
			if(subPhotos ==null || subPhotos.size()<1 || indexOfPhoto(subPhotos, currPhoto)==subPhotos.size()-1){
				return;
			}
			int nextIndex = indexOfPhoto(subPhotos, currPhoto)+1;
			currPhoto = subPhotos.get(nextIndex);
			photoIndex = indexOfPhoto(allPhotos, currPhoto);
			DisplayPhoto(photoIndex);
		}
		
		public void save(){
			userAlbum.setAlbum(allPhotos);
			
		}
		/**
		 * When a photo is selected its display contents are shown.
		 * @param photoIndex
		 */
		private void DisplayPhoto(int photoIndex){
			
			

			ObservableList<String> keyList = FXCollections.observableArrayList();
			keyList.add(" ");
			tagsKeyList.setItems(keyList);
			
			photoDisplay.setImage(allPhotos.get(photoIndex).getImage());
			captionDisplay.setText(currPhoto.getCaption());
			dateOfPhoto.setText(currPhoto.getDate());
			keyToAdd.clear();
			ArrayList<String> tagsNames = new ArrayList<String>();
			ObservableList<String> tagsList = FXCollections.observableArrayList();
			ArrayList<Identity> hold = currPhoto.getTags();
			if(hold==null || hold.size()<1){
				tagsList.add(" ");
				tagsSelectionList.setItems(tagsList);
				return;
			}
			int i = 0;
			while(i<hold.size()){
				tagsNames.add(hold.get(i).getTagName());
				i++;
			}
			tagsList.addAll(tagsNames);
			
			tagsSelectionList.setItems(tagsList);
			
			tagsSelectionList
	     		.getSelectionModel()
	     		.selectedIndexProperty()
	     			.addListener(
						(obs, oldVal, newVal) -> {
							int t = tagsSelectionList.getSelectionModel().getSelectedIndex();
							if(t <0 || hold.size()==0 || t>=hold.size()){ return;}
							currLabel = hold.get(t); 
							showKeys();
							return;
						});
		    
		    
			return;
		}
		
		/**
		 * When the caption/recaption button is clicked the caption field is enabled allowing the user to make 
		 * changes to the caption, initially the caption is empty. Once the recaption/recaption button is clicked
		 * changes are updated to the Photo object
		 * 
		 * @param e
		 */
		public void recaption(ActionEvent e){
			if(!captionDisplay.isDisable()){
				String str = captionDisplay.getText();
				if(!str.equals("")){
					currPhoto.recaption(str);
				}
				captionDisplay.setDisable(true);
				return;
			}
			captionDisplay.setDisable(false);
		}
		/**
		 * When the user clicked on the add Tag button, the String entered in the textBox is read and should the tag name already exists in the 
		 * current Photo, an alert will be shown. Otherwise the tag selection will be updated to include the new tag. Tags are in decending order. For 
		 * user convenince.
		 * @param e
		 */
		public void addTag(ActionEvent e){
			if(currPhoto ==null) return;
			
			String strn = tagToAdd.getText();
			
			if(strn ==null ||strn.equals("")){
				Alerts("Tag Error", "Tag cannot be empty");
				return;
			}
			strn = strn.trim();
			Identity temp = new Identity(strn);
			
			if(currPhoto.contains(temp)){
				Alerts("Tag error", "Tag already exists, enter a new tag");
				return;
			}
			
			if(!containsTag(tags, temp)){
				tags.add(temp);
				sortTags(tags, 0, tags.size()-1);
			}
			currPhoto.addTag(strn);
			int index = indexOfTag(currPhoto.getTags(), temp);
			currLabel = currPhoto.getTags().get(index);
			tagToAdd.clear();
			updateTagSelection();
			showKeys();
			DisplayPhoto(photoIndex);
		}
		
		/**
		 * Upon clicking on the addKey button the information entered by the user in the textbox to the left of the button will be read,
		 * should the key already exists in the selected tag, an alert will be displayed, otherwise the key will be added and the list will
		 * be updated
		 * @param e
		 */
		public void addKey(ActionEvent e){
			if(currLabel == null ||imgs.size()==0 || tags.size()==0) return;
			
			String strn = keyToAdd.getText();
			
			if(strn == null || strn.equals("")){
				Alerts("Invalid input", "No key entered");
				return;
			}
			
			strn = strn.trim();
			Key temp = new Key(strn, currPhoto);
			keyToAdd.clear();
			if( currLabel.contains(temp)) {
					Alerts("Error", "Tag - Key relation already exists in this photo, please try again");
					return;
			}
			
			int i = indexOfTag(tags, currLabel);
			Identity holdTag = tags.get(i);
			
			if(!holdTag.contains(temp)){
				tags.get(i).addKey(temp.getKey(), currPhoto);
			}else{
				Key tagsKey = holdTag.getKeyContaining(temp.getKey());
				if(tagsKey.indexOfPhoto(currPhoto) == -1){
					tagsKey.addPhoto(currPhoto);
				}
			}
			updateTagSelection();
			currLabel.addKey(strn, currPhoto);
			currPhoto.dateOfChange();
			dateOfPhoto.setText(currPhoto.getDate());
			showKeys();
		}
		/***
		 * For user convenience a choicebox of all album associated to the user are displated. Once a user has selected a photo, an album in choice box,
		 * and clicked on the move button, selected photo will be move to the selected destination. Should the user fail to follow that sequence an alert
		 * will pop stating the corresponding error. 
		 * @param e
		 */
		public void moveTo(ActionEvent e){
			
			if(currPhoto == null){
				Alerts("Move to Error", "Please select a photo to Move");
				return;
			}
	
			if(albumSelection.getSelectionModel().getSelectedItem()==null || albumSelected ==null){
				Alerts("Move to ERROR", "No album was selected to move photo to");
				return;
			}
			
			if(albumSelected.equals(userAlbum)){
				Alerts("Move to ERROR", "Can not move to CURRENT album");
				return;
			}
			if(albumSelected.hasPhoto(currPhoto)){
				Alerts("Move to ERROR", "Photo already exists in the selected album");
				return;
			}
			
			Photo tempPhoto = currPhoto.getCopy();
			albumSelected.addPhoto(tempPhoto);
			ArrayList<Identity> selectAlbumTag = albumSelected.getTags();
			int i=  0;
			while(i < tempPhoto.getTags().size()){
				if(!containsTag(selectAlbumTag, tempPhoto.getTags().get(i))){
					selectAlbumTag.add(tempPhoto.getTags().get(i));
					sortTags(selectAlbumTag, 0, selectAlbumTag.size()-1);
				}else{
					Identity holdLabel = tempPhoto.getTags().get(i);
					ArrayList<Key> labelKeys = holdLabel.getKeys();
					int p =0;
					
					int t = indexOfTag(selectAlbumTag, holdLabel);
					Identity holdTag = selectAlbumTag.get(t);
					
					while(p<labelKeys.size()){
						if(!holdTag.contains(labelKeys.get(p))){
							selectAlbumTag.get(t).addKey(labelKeys.get(p).getKey(), tempPhoto);
						}else{
							Key tagsKey = holdTag.getKeyContaining(labelKeys.get(p).getKey());
							if(tagsKey.indexOfPhoto(tempPhoto) == -1){
								tagsKey.addPhoto(tempPhoto);
							}
						}
						p++;
					}
				}
				i++;
			}
			
			removePhoto();
			clearAll();
			return;
		}
		
	
		/**
		 * Upon clicking on the search button, the user input for dateSearch will be pulled, should it be in inproper order the search will fail.
		 * An alert will be popped. Otherwise photo's corresponding to the date search will be shown in the the thumbnaiil panel
		 * @param e
		 */
		public void search(ActionEvent e){
			if(fromDate == null || toDate == null) return;
			String from = fromDate.getText();
			from = from.trim();
			String to = toDate.getText();
			to = to.trim();
			
			if(to.compareTo(from)<0 || to.length()!=10 || from.length()!=10){
				Alerts("Search Date error", "Date input is invalid");
				return;
			}
			
			fromDate.clear();
			toDate.clear();
			
			subPhotos.clear();
			int i =  0;
			while(i<allPhotos.size()){
				String strn = allPhotos.get(i).getDate();
				strn = strn.substring(0,10);
				if(strn.compareTo(from)>=0 && strn.compareTo(to)<=0){
					subPhotos.add(allPhotos.get(i));
				}
				i++;
			}
			showThumbNails();
		}
		/**
		 * Generated an array of imageview, sizing them to thumbnail size
		 */
		public void showThumbNails(){
			if(subPhotos==null ||subPhotos.size()==0){ 
				Pane clean = new Pane();
				photoPane.setContent(clean);
				return;
			}
			imgs.clear();
			int i = 0;
			
			while(i < subPhotos.size()){
				ImageView temp = new ImageView(subPhotos.get(i).getImage());
				//ImageView temp = new ImageView(new Image(subPhotos.get(i).getUrl()));
				temp.setPreserveRatio(true);
				temp.setFitWidth(100.0);
				temp.setFitHeight(100.0);
				temp.setId(subPhotos.get(i).getUrl());
				temp.setOnMousePressed(e->{
					ImageView hold  = (ImageView) e.getSource();
					getImgWithID(hold.getId());
					//DisplayPhoto(photoIndex);
					
				});
				imgs.add(temp);
				i++;
			}
			i = 0;
			HBox hBox = new HBox(10);
			VBox vBox1 = new VBox(5);
			VBox vBox2 = new VBox(5);
			VBox vBox3 = new VBox(5);
			VBox vBox4 = new VBox(5);
			VBox vBox5 = new VBox(5);
			
			hBox.getChildren().addAll(vBox1, vBox2, vBox3, vBox4, vBox5);
			
			
			for(i= 0; i<subPhotos.size(); i++){
				int mono = i%5;
				switch(mono){
					case 0:
						vBox1.getChildren().add(imgs.get(i));
						continue;
					case 1:
						vBox2.getChildren().add(imgs.get(i));
						continue;
					case 2:
						vBox3.getChildren().add(imgs.get(i));
						continue;
					case 3:
						vBox4.getChildren().add(imgs.get(i));
						continue;
					case 4:
						vBox5.getChildren().add(imgs.get(i));
						continue;
				}
				
			}
			photoPane.setContent(hBox);
			
		}
		/**
		 * photos are identified by their url, essentially their id. Hence each image in imaveview array created in showthumbnails is given 
		 * this id. 
		 * @param id
		 */
		private void getImgWithID(String id){
			if(allPhotos==null||allPhotos.size()==0)return;
			int i = 0;
			while(i<allPhotos.size()){
				if(allPhotos.get(i).getUrl().compareTo(id)==0){
					currPhoto = allPhotos.get(i);
					photoIndex = i;
					DisplayPhoto(photoIndex);
					return;
				}
				i++;
			}
		}
		
		/**
		 * shows the observble list of keys used for search
		 */
		public void showKeys(){
			
			if(currLabel==null || currLabel.getKeys()==null || currLabel.getKeys().size()<1){

				ObservableList<String> currkeys=FXCollections.observableArrayList();
				currkeys.add("");
				tagsKeyList.setItems(currkeys);
				return;
			}
			Identity hold = currLabel;
			
			ObservableList<String> currKeys=FXCollections.observableArrayList();
			ArrayList<Key> photosKeys = hold.getKeys();
			int i = 0;
			while(i<photosKeys.size()){
				currKeys.add(photosKeys.get(i).getKey());
				i++;
			}
			
			tagsKeyList.setItems(currKeys);
			if(currLabel.getKeys()!=null && currLabel.getKeys().size()!=0){
			tagsKeyList
			.getSelectionModel()
     		.selectedIndexProperty()
     			.addListener(
					(obs, oldVal, newVal) -> {
						String t = tagsKeyList.getSelectionModel().getSelectedItem();
						
						if(t ==null || t.compareTo("")==0 || t.contains(" ")){ return;}
							currKey = currLabel.getKeyContaining(t); 
								if(currKey==null){
									return;
								}
					});
			}
		}
		/**
		 * Upon selecting a photo and clicking on the delete button, the user will delete the selected photo. With it the user will
		 * also deleted all tags and key relation associated with the photo.
		 * @param e
		 * @throws Exception
		 */
		public void deleteKey(ActionEvent e) throws Exception{
			
				if(currKey ==null || currLabel ==null) return;
				////some alert should be added
				
				Key holdKey = new Key(currKey.getKey(), currPhoto);
				currLabel.removeKey(currKey);
				currKey = null;
				int i = indexOfTag(tags, currLabel);
				if(i==-1) return;
				Identity tagInList = tags.get(i);
				if(tagInList.contains(holdKey)){
					
					Key removePhotoRelation  =tagInList.getKeyContaining(holdKey.getKey());
					if(removePhotoRelation.indexOfPhoto(currPhoto)==-1) return;
					if(removePhotoRelation.getNumKeyToPhoto()==1){
						if(tagInList.getKeys().size()==1){
							tags.remove(i);
							currLabel = null;
						}else{
							tagInList.removeKey(removePhotoRelation);
						}
					}else{
						removePhotoRelation.removePhoto(currPhoto);
					}
				}
			updateTagSelection();
			showKeys();
			return;
		}
		/**
		 * Upon clicking on delete photo tag button, the user will delete the selected tag in the listview of tags
		 * @param e
		 * @throws Exception
		 */
		public void removeTag(ActionEvent e) throws Exception{
			int i = indexOfTag(tags, currLabel);
			if(i != -1){
				Identity holdTag = tags.get(i);
				ArrayList<Key> labelKeys = currLabel.getKeys();
				int k = 0;
				while(k < labelKeys.size()){
					if(holdTag.contains(labelKeys.get(k))){
						Key tagKey = holdTag.getKeyContaining(labelKeys.get(k).getKey());
						if(tagKey.indexOfPhoto(currPhoto)!=-1){
							tagKey.removePhoto(currPhoto);
						}
						if(tagKey.getNumKeyToPhoto()==0){
							holdTag.removeKey(tagKey);
						}
					}
					k++;
				}
				if(holdTag.getKeys().size()==0){
					tags.remove(i);
				}
			}
			currPhoto.removeTag(currLabel);
			updateTagSelection();
			currLabel = null;
			DisplayPhoto(photoIndex);
		}
		/**
		 * User will hold a copy of a photo, upon clicking on the copy button
		 * @param e
		 */
		public void copyPhoto(ActionEvent e){
			photoCopy = currPhoto;
			user.addPhotoCopy(photoCopy);
			return;
		}
		/**
		 * Upon clicking on the paste button, the copy (Photo object) will be pulled from the user. Should there be nothing, or the photo already
		 * exists in the album an alert will be posted. Otherwise, tags and keys will be updated to include the photo.
		 * @param e
		 */
		public void pastePhoto(ActionEvent e){
			
			photoCopy = user.getPhotoCopy();
			if(photoCopy == null){
				Alerts("Paste Invalid", "There is nothig to paste");
				return;
			}
			if(indexOfPhoto(allPhotos, photoCopy) !=-1){
				Alerts("Paste Invalid", "Photo is already in collection");
				return;
			}
			int i=  0;
			while(i < photoCopy.getTags().size()){
				if(!containsTag(tags, photoCopy.getTags().get(i))){
					tags.add(photoCopy.getTags().get(i));
					sortTags(tags, 0, tags.size()-1);
				}else{
					Identity holdLabel = photoCopy.getTags().get(i);
					ArrayList<Key> labelKeys = holdLabel.getKeys();
					int p =0;
					
					int t = indexOfTag(tags, holdLabel);
					Identity holdTag = tags.get(t);
					
					while(p<labelKeys.size()){
						if(!holdTag.contains(labelKeys.get(p))){
							tags.get(t).addKey(labelKeys.get(p).getKey(), photoCopy);
						}else{
							Key tagsKey = holdTag.getKeyContaining(labelKeys.get(p).getKey());
							if(tagsKey.indexOfPhoto(photoCopy) == -1){
								tagsKey.addPhoto(photoCopy);
							}
						}
						p++;
					}
				}
				i++;
			}
			//Photo tempPhoto = photoCopy.getCopy();
			//tempPhoto.dateOfChange();
			allPhotos.add(photoCopy);
			updateTagSelection();
			clearAll();
			photoCopy = null;
		}
		private int indexOfPhoto(ArrayList<Photo> list, Photo p){
			int i= 0;
			while(i<list.size()){
				if(list.get(i).comparedTo(p)==0){return i;}
				i++;
			}
			return -1;
		}
		
		/**
		 * Activated upon clicking delete button
		 * @param e
		 */
		public void deletePhoto(ActionEvent e){
			removePhoto();
		}
		/**
		 * removes a photo from the users arraylist of photos
		 */
		private void removePhoto(){
			if(currPhoto==null ||size ==0){
				Alerts("Invalid Delete", "A photo needs to be selected in order to be deleted");
			}
			
			int i = 0;
			ArrayList<Identity> tmpTags = currPhoto.getTags();
			
			while( i < tmpTags.size()){
				if(containsTag(tags, tmpTags.get(i))){
					int j = indexOfTag(tags, tmpTags.get(i));
					Identity currTagInList = tags.get(j);
					ArrayList<Key> tagsKeys = currTagInList.getKeys();
					ArrayList<Key> photosKeys = tmpTags.get(i).getKeys();
					int k = 0;
					while(k < photosKeys.size()){
						if(containsKey(tagsKeys, photosKeys.get(k))){
							
							Key currKey = currTagInList.getKeyContaining(photosKeys.get(k).getKey());
							int index = currKey.indexOfPhoto(currPhoto);
							if(index == -1) continue;
							currKey.removePhoto(currPhoto);
							if(currKey.getPhotos().size()==0){
								currTagInList.removeKey(currKey);
							}
						}
						k++;
					}
					if(currTagInList.getKeys().size()==0){
						tags.remove(i);
						if(tags.size()==0){break;}
					}
				}
				i++;
			}
			
			allPhotos.remove(currPhoto);
			size--;
			updateTagSelection();
			
			if(allPhotos.size()==0){
				currPhoto = null;
			}
			clearAll();
		}
		/**
		 * Upon clicking clear search button clearAll method is called
		 * @param e
		 */
		public void clearSearch(ActionEvent e){
			clearAll();
		}
		/**
		 * The photos displayed in the thumbnail will be updated to include all photos.
		 */
		private void clearAll(){
			currPhoto = null;
			subPhotos.clear();
			subPhotos.addAll(allPhotos);
			allTags.getSelectionModel().clearSelection();
			allKeys.getSelectionModel().clearSelection();
			defaultSheet();
			showThumbNails();
		}
		/**
		 * Displayed nothing in the display area
		 */
		private void defaultSheet(){
			photoDisplay.setImage(null);
			captionDisplay.setText("");
			dateOfPhoto.setText("");

			ObservableList<String> tagsList = FXCollections.observableArrayList();
			tagsList.add(null);
			tagsSelectionList.setItems(tagsList);
			ObservableList<String> keysList = FXCollections.observableArrayList();
			keysList.add(" ");
			tagsKeyList.setItems(keysList);
		}
		/**
		 * Searches to see if the list of Key already contains a key through binary search
		 * @param list
		 * @param key
		 * @return
		 */
		private boolean containsKey(ArrayList<Key> list, Key key){
			if(list==null|| key==null){return false;}
			int low = 0;
			int high = list.size()-1;
			Key temp = null;

			while(low<=high){
				int middle = (low+high)/2;
				temp = list.get(middle);
				int com = temp.getKey().compareToIgnoreCase(key.getKey());
				if(com ==0){
					return true;
				}else if(com <0){
					low = middle+1; 
				}else{
					high = middle-1;
				}
			}
			
			return false;
		}
		/**
		 * Searches a tag list to see if a tag already exists in the tag through binary search
		 * @param list
		 * @param tag
		 * @return
		 */
		private boolean containsTag(ArrayList<Identity> list, Identity tag ){
			if(list==null|| tag==null || list.size()==0){return false;}
			int low = 0;
			int high = list.size()-1;
			Identity temp = null;

			while(low<=high){
				int middle = (low+high)/2;
				temp = list.get(middle);
				int com = temp.comparedTo(tag);
				if(com ==0){
					return true;
				}else if(com <0){
					low = middle+1; 
				}else{
					high = middle-1;
				}
			}
			
			return false;
		}
		/**
		 * Returns the index at which a tag is located, used for pulling a tag from a taglist 
		 * @param list
		 * @param tag
		 * @return
		 */
		private int indexOfTag(ArrayList<Identity> list, Identity tag ){
			if(list==null|| tag==null || list.size()==0){return -1;}
			int low = 0;
			int high = list.size()-1;
			Identity temp = null;

			while(low<=high){
				int middle = (low+high)/2;
				temp = list.get(middle);
				int com = temp.comparedTo(tag);
				if(com ==0){
					return middle;
				}else if(com <0){
					low = middle+1; 
				}else{
					high = middle-1;
				}
			}
			
			return -1;
		}
		/**
		 * sorts a tag
		 * @param arr
		 * @param low
		 * @param high
		 */
		private void sortTags(ArrayList<Identity> arr, int low, int high){
			if(arr==null || arr.size()==0){return;}
			if(low>=high){ return;}
			
			int middle = low + (high - low) / 2;
			Identity pivot = arr.get(middle);
			
			int i = low, j = high;
			while (i <= j) {
				while (arr.get(i).comparedTo(pivot)<0) {
					i++;
				}
				while (arr.get(j).comparedTo(pivot)>0){
					j--;
				}
				if (i <= j) {
					Identity temp = arr.get(i);
					arr.set(i, arr.get(j));
					arr.set(j, temp);
					i++;
					j--;
				}
			}
			if (low < j)
				sortTags(arr, low, j);
			if (high > i)
				sortTags(arr, i, high);
		
	}
	
		/**
		 * Created a new album of search context and searches to see if the user already has an album named this. This is activated upon the 
		 * user clicking the create button
		 * @param e
		 */
		public void createAlbum(ActionEvent e){
			
			String albumName = newAlbum.getText();
			albumName = albumName.trim();
			newAlbum.clear();
			
			//if no input was detected 
			if (albumName.isEmpty()){
				
				LoginController.Alerts("Invalid Input", "No Input was detected!");
				return;			
			}
			if(subPhotos ==null || subPhotos.size()==0){
				Alerts("New ALbum Error", "Invalid input, search result is empty");
				return;
			}
			//get the calender date
			Calendar date = Calendar.getInstance();
			String month = Integer.toString(date.get(Calendar.MONTH)+1);
			String day = Integer.toString(date.get(Calendar.DATE));
			String year = Integer.toString(date.get(Calendar.YEAR));
			
			//creating a new album 
			
			Album item = new Album (albumName,month+"/"+day+"/"+ year);
			ArrayList<Identity> tagsToAdd = new ArrayList<Identity>();
			int i = 0;
			while(i<subPhotos.size()){
				item.addPhoto(subPhotos.get(i));
				int j=0;
				ArrayList<Identity> holdTags = subPhotos.get(i).getTags();
				while(j<holdTags.size()){
					if(!containsTag(tagsToAdd,holdTags.get(j))){
						tagsToAdd.add(holdTags.get(j));
						sortTags(tagsToAdd, 0, tagsToAdd.size()-1);
					}else{
						int index = indexOfTag(tagsToAdd, holdTags.get(j));
						Identity currTag = tagsToAdd.get(index);
						
						ArrayList<Key> holdKeys = holdTags.get(j).getKeys();
						
						int k = 0;
						while(k<holdKeys.size()){
							Key key = holdKeys.get(k);
							if(!currTag.contains(key)){
								currTag.addKey(key.getKey(), subPhotos.get(i));
							}else{
								Key otherKey = currTag.getKeyContaining(key.getKey());
								otherKey.addPhoto(subPhotos.get(i));
							}
							k++;
						}
						
					}
					j++;
				}
				i++;
			}
			//adding the album into the current list user
			item.upDateTags(tagsToAdd);
			
			if (!this.albums.contains(item)){
					user.addAlbum(item);
					oslAlbums.add(item);
					updateAlbumSelection();
					return;
					
				}
			//the item already exists
			
			LoginController.Alerts("Duplicate", "The Album already exists");	
		
			
		}
		
		/**
		 * Generic alert
		 * @param title
		 * @param message
		 */
		
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
