package model;
/**
 * 
 * The user class has albums
 * name ()
 * toString()
 * savePhotos()
 * getPhotos()
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User implements Comparable<User>, Serializable   {

	
	private ArrayList<Album> Albums;// all photos are in here
	String name; // name of the user
	Photo copy;
	public User(String name){
		Albums = new ArrayList<Album>();
		this.name = name;
	}
	public void addPhotoCopy(Photo photo){
		copy = photo;
	}
	
	
	public Photo getPhotoCopy(){
		return copy;
	}
	

	public boolean equals(Object o){
		
		if (o == null || !( o instanceof User)){
			
			return false;
		}
		User object2 = (User)(o);
		
		return object2.name.equalsIgnoreCase(this.name);
	}
	
	public String getName() {
		return name;
	}
	
	public ObservableList<Album> getAlbums(){
		
		ObservableList<Album> items = FXCollections.observableArrayList();
		
		for (int i=0 ; i<Albums.size(); i++){
			
			items.add(Albums.get(i));
		}
		
		return items;
	}
	public ArrayList<Album> getAlbumList(){
		return Albums;
	}
	public void setAlbums(ObservableList<Album> arr){
		
		Albums.clear();
		for (int i = 0 ; i< arr.size(); i++){
			
			Albums.add(arr.get(i));
		}
		
	}
	public void setAlbums(ArrayList<Album> arr){
		
		
		Albums = arr;
		return;
	}
	
	public String toString(){
		
		return name;
	}
	
	
	
	public void savePhotos(){
		
		try{
		
			ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(name)); // getting the file
			
			int length = Albums.size();
			
			for (int i=0; i<length; i++){
				
				Album item = Albums.get(i);
				ois.writeObject(item);
			}
			ois.close();
		}catch(Exception e){
			
			System.out.println("Exception is Caught\nClass:User\nMethod: savePhotos");
		}
		
	}
	
	public void getPhotos(){
		
		try{
			
			
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(name)); // getting the file
		
		Album item = null; 
		
		while ((item=(Album)ois.readObject())!=null){
			
			Albums.add(item);
		}
		ois.close();
		
		}catch(Exception e){
			
			
			//System.out.println("Exception is Caught\nClass:User\nMethod: getPhotos\n");
		}
	}

	@Override
	public int compareTo(User arg0) {
		
		if (this.name.equalsIgnoreCase(arg0.name)){
			
			return 0;
		}
		return this.name.compareTo(arg0.name);
	}
	public ArrayList<Album> getUserAlbum(){
		return Albums;
	}
	public void addAlbum(Album albumToAdd){
		Albums.add(albumToAdd);
	}
}
