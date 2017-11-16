package model;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Key class is used to identity characteristics shared between photos. Keys holds a connection to URLs in order to generate and confirm the connection associated to a key and multiple photos.
 * @author Pedro Cruz
 *
 */
public class Key implements Serializable{
		private String key;
		private ArrayList<String> photosUrl;
		private int size;
		
		/**
		 * Key constructor
		 * @param key String
		 * @param photo Photo object
		 */
		public Key(String key, Photo photo){
			this.key = key;
			photosUrl= new ArrayList<String>();
			photosUrl.add(photo.getUrl());
			size = 1;
		}
		/**
		 * addPhoto allows used to add a Photo's objects url to a key, useful for tag-key match to a Photo object
		 * @param Photo
		 */
		public void addPhoto(Photo p){
			photosUrl.add(p.getUrl());
			size++;
			return;
		}
		
		/**
		 * Returns Key's object name of type String
		 * @return String: Key name
		 */
		public String getKey(){
			return key;
		}
		/**
		 * Returns Key's ArrayList<String> of URLs associated with Key object
		 * @return ArayList<String>
		 */
		public ArrayList<String> getPhotos(){
			return photosUrl;
		}
		
		/**
		 * Returns the index at which the photo is located in the Key's ArrayList of photos URL, index is used to get a photo's URL from arrayList
		 * @param 
		 * @return int
		 */
		public int indexOfPhoto(Photo photo){
			if(photo==null|| photo.isEmpty()){return -1;}
			
			int i = 0;
			while(i< photosUrl.size()){
				
				if((photo.getUrl()).compareToIgnoreCase(photosUrl.get(i))==0){
					return i;
				}
				i++;
			}
			return -1;
		}
		/**
		 * Removes a photo from Key
		 * @param Photo photo
		 * @return void
		 */
		public void removePhoto(Photo photo){
			int i = this.indexOfPhoto(photo);
			photosUrl.remove(i);size--;
		}
		/**
		 * Returns the numbers of photos associated to Key object
		 * @return int
		 */
		public int getNumKeyToPhoto(){
			return size;
		}
}
