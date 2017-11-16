package model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.image.Image;
/**
 * Identity class is essentially the tag associated to any Photo object. An Identity is used, in connection to a Key to search for Photo object that 
 * contains identical connections. A tag is identified by its name, type String, and each tag hold an ArrayList of Key objects associated to the tag.
 * @author Pedro Cruz
 */
public class Identity implements Serializable{
	
	protected String tagName;
	protected ArrayList<Key> keys;
	/**
	 * Identity constructor generated a new Identity object given a String parameter used to identify the Identity (tag).
	 * @param String tag
	 */
	public Identity(String tag){
		this.tagName = tag;
		keys = new ArrayList<Key>();
	}
	/**
	 * Adds a Key, a new Key object is created here given a String for the key name, and a Photo used the associate the photo. Once the Key 
	 * object is generated it is checked to see if such Key relation already exists, if not, the new Key is added
	 * @param String key
	 * @param Photo photo
	 */
	public void addKey(String key, Photo photo){
		Key tmp = new Key(key, photo);
		if(contains(tmp)){ return;}
		keys.add(tmp);
		sort(this.keys, 0, this.keys.size()-1);
	}
	/**
	 * Returns the name of the tag
	 * @return String
	 */
	public String getTagName(){
		return tagName;
	} 
	/**
	 * Removes a Key from the Identity's key list
	 * @param Key key
	 * @return void
	 */
	
	public void removeKey(Key key){
		if(contains(key)==false){return;}
		keys.remove(indexOfKey(key));
		return;
	}
	/**
	 * Returns an int, identified where the Key is located in the array. A key is identified by it's assigned name.
	 * @param key
	 * @return int
	 */
	public int indexOfKey(Key key){
		if(keys==null || keys.size()==0){return -1;}
		int low = 0;
		int high = keys.size()-1;
		Key temp = null;
		String strn = key.getKey();
		while(low<=high){
			int middle = (low+high)/2;
			temp = keys.get(middle);
			int com = (temp.getKey()).compareToIgnoreCase(strn);
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
	 * Returns the Key object in the Identity's ArraList of Key objects when given a Key to finds name, type String
	 * @param String strn
	 * @return Key 
	 */
	public Key getKeyContaining(String strn){
		if(keys==null || keys.size()==0){return null;}
		int low = 0;
		int high = keys.size()-1;
		Key temp = null;

		while(low<=high){
			int middle = (low+high)/2;
			temp = keys.get(middle);
			int com = (temp.getKey()).compareTo(strn);
			if(com ==0){
				return temp;
			}else if(com <0){
				low = middle+1; 
			}else{
				high = middle-1;
			}
		}
		return null;
	}
	/**
	 * Returns the Identity's ArrayList of Key objects
	 * @return ArrayList
	 */
	public ArrayList<Key> getKeys(){
		return keys;
	}
	
	
	
	
	
	
	private void sort(ArrayList<Key> arr, int low, int high){
		if(arr==null || arr.size()==0){return;}
		if(low>=high){ return;}
		
		int middle = low + (high - low) / 2;
		Key pivot = arr.get(middle);
		
		int i = low, j = high;
		while (i <= j) {
			while ((arr.get(i).getKey()).compareToIgnoreCase(pivot.getKey())<0) {
				i++;
			}
			while ((arr.get(j).getKey()).compareToIgnoreCase(pivot.getKey())>0){
				j--;
			}
			if (i <= j) {
				Key temp = arr.get(i);
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
	 * Used to identify is Key object exists in the this Identity
	 * @param key
	 * @return boolean
	 */
	public boolean contains(Key key){
		if(key==null|| keys==null || keys.size()==0){return false;}
		int low = 0;
		int high = keys.size()-1;
		Key temp = null;

		while(low<=high){
			int middle = (low+high)/2;
			temp = keys.get(middle);
			int com = (temp.getKey()).compareTo(key.getKey());
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
	
	public boolean isEmpty(){
		return keys==null;
	}
	/***
	 * Compared two Identity object. Each object is identified by their assigned name.
	 * @param Identity other
	 * @return
	 */
	public int comparedTo(Identity other){
		return this.getTagName().compareToIgnoreCase(other.getTagName());
	}
	/**
	 * Removes all keys in Tag
	 * @return void
	 */
	public void removeKeys(){
		keys.clear();
	}
	
}
