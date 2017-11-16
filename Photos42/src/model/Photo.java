
package model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.scene.image.Image;


/**
 * Photo is an object used the hold unique identities associated to a image. A Photo in an album is identified by it's caption, which may be
 * nothing, its URL (type String), date (dateOfChange), and a list of assigned tags used for searching photos with identical tag to key association
 * @param String url
 * @param String caption
 * @param String data
 * @param ArrayList<Identity> tags
 * @author Pedro Cruz
 *
 */
public class Photo implements Serializable {
	private String url;
	private String caption;
	private String date;
	private ArrayList<Identity> tags;
	
	/**
	 * Photo constructor created a photo object by taking a URL of type String
	 * @param String url
	 */
	public Photo(String url){
		this.url = url;
		caption = "";
		tags = new ArrayList<Identity>();
		dateOfChange();
	}
	/**
	 * dateOfChange generate an instance of when any changes to Photo object where made
	 * @return void
	 */
	public void dateOfChange(){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		date = dtf.format(now);
	}
	/**
	 * Returns an ArrayList<Identity>, (Tags) associated with Photo object
	 * @return ArrayList<Indetity>: tags
	 */
	public ArrayList<Identity> getTags(){
		return tags;
	}
	/**
	 * Returns a copy of Photo object by creating a new object and initializing all parameters from current Photo object
	 * @return Photo
	 */
	public Photo getCopy(){
		Photo tempPhoto = new Photo(this.getUrl());
		tempPhoto.recaption(caption);
		tempPhoto.setTags(tags);
		return tempPhoto;
	}
	/**
	 * Used to initially set tag (ArrayList<Identity>) or to add additional Identity objects to tags list.
	 * @param ids
	 */
	public void setTags(ArrayList<Identity> ids){
		tags.addAll(ids);
	}
	
	/**
	 * Used to add a single tag giving only a String name of new Identity to be added to Photo's tags
	 * @param String str
	 */
	public void addTag(String str){
		Identity tmp = new Identity(str);
		if(contains(tmp)){
			return;
		}
		tags.add(tmp);
		sort(tags,0,tags.size()-1);
		dateOfChange();
	}
	
	/**
	 * Removes a tag from Photo's tag's list when given an Identity object to remove. Returns true or false if successful or fail.
	 * @param tag
	 * @return boolean
	 */
	public boolean removeTag(Identity tag){
		if(tags==null ||tags.size()==0){return false;}
		int low = 0;
		int high = tags.size()-1;
		Identity temp = null;

		while(low<=high){
			int middle = (low+high)/2;
			temp = tags.get(middle);
			int com = temp.comparedTo(tag);
			if(com ==0){
				
				Identity hold = tags.remove(middle);
				hold.removeKeys();
				sort(tags,0,tags.size()-1);
				dateOfChange();
				return true;
			}else if(com <0){
				low = middle+1; 
			}else{
				high = middle-1;
			}
		}
		
		return false;
	}
	public boolean contains(Identity label){
		if(label==null|| tags==null || tags.size()==0){return false;}
		int low = 0;
		int high = tags.size()-1;
		Identity temp = null;

		while(low<=high){
			int middle = (low+high)/2;
			temp = tags.get(middle);
			int com = temp.comparedTo(label);
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
	 * Used to get the caption of  a photo, type String
	 * @return String
	 */
	public String getCaption(){
		return caption;
	}
	/**
	 * Allows to change the Photo object's caption to a new given caption
	 */
	public void recaption(String caption){
		
		this.caption = caption;
		dateOfChange();
	}
	/**
	 * Returns the date of the Photo's last update
	 * @return String
	 */
	public String getDate(){
		return date;
	}
	/**
	 * Returns a Image object generated from the Photo's object URL 
	 * @return Image
	 */
	public Image getImage(){
		return  new Image(url);
	}

	private void sort(ArrayList<Identity> arr, int low, int high){
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
				sort(arr, low, j);
			if (high > i)
				sort(arr, i, high);
		
	}
	/**
	 * Returns the Photo's object URL of type String
	 * @return String URL
	 */
	public String getUrl(){
		return url;
	}
	/**
	 * Used to compare Photos, photos are identified by their URL.
	 * @param Object o
	 * @return int
	 */
	public int comparedTo(Object o){
		if(o==null || ! (o instanceof Photo)){return -1;}
		Photo p = (Photo)o;
		return this.getUrl().compareTo(p.getUrl());
	}
	/**
	 * Returns boolean, telling whether a Photo object contains a photo's URL or not
	 * @return boolean
	 */
	public boolean isEmpty(){
		return url.length()==0;
	}
	public String toString(){
		
		return this.url;
	}
}
