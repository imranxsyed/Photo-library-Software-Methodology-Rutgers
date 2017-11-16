package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Photo> photos;
	private String name, date, lastDate, comDate;

	private int number_of_pics;
	private ArrayList<Identity> tags;
	

	
		
	
	public Album(String name, String date){
		photos = new ArrayList<Photo>();
		tags = new ArrayList<Identity>();

		this.name = name;
		this.date = date;
		this.lastDate = date;
		this.number_of_pics =0; 
		comDate =  this.date+"-" + this.lastDate;
	}
	
	public void upDateTags(ArrayList<Identity> newTags){
		tags.clear();
		tags.addAll(newTags);
		return;
	}
	
	
	public void addPhoto(Photo object){
		
		if(object==null) return;
		
		ArrayList<Photo> tmp = new ArrayList<Photo>();
		tmp.addAll(photos);
		photos.clear();
		tmp.add(object);
		photos = tmp;
		this.number_of_pics++;
		//this.number_of_pics++;
		
		lastDate = object.getDate();
		int i =0;
		/*
		if(object.getTags()==null) return;
		ArrayList<Identity> obTags = object.getTags();
		while(i < obTags.size()){
			if(!this.hasTag(obTags.get(i))){
				tags.add(obTags.get(i));
			}
		}
		*/
	}


	
	public String getName() {
		return name;
	}


	public String getDate() {
		return date;
	}
	
	
	public void setDate(String date){
		
		this.lastDate = date;
	//	System.out.println("First date: "+ this.date+"\n last date: "+ this.lastDate);
	}
	
	public String getComDate(){
		
		return this.comDate = this.date+" -- " + this.lastDate;
	}
	
	
	public int getNumber_of_pics() {
		return number_of_pics;
	}
	
	public void setNumber_of_pics(int size){
		
		this.number_of_pics = photos.size();
		return;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public  String toString(){
		
		return this.name;
	}
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null || !(obj instanceof Album)){
			
			return false;
		}
		
		Album object = (Album)obj;
		
		return this.name.equalsIgnoreCase(object.name);
	}
	

	public ArrayList<Photo> getPhotos(){
		return photos;
	}
	public void setAlbum(ArrayList<Photo> photos){
		this.photos = photos;
		return;
	}

	
	public void addTag(Identity label){
		tags.add(label);
	}
	public ArrayList<Identity> getTags(){
		return tags;
	}
	
	public boolean hasPhoto(Photo p){
		if(this.photos ==null || this.photos.size()==0){ return false;}
		int i = 0;
		while(i< this.photos.size()){
			if(p.comparedTo(this.photos.get(i))==0){return true;
			}
			i++;
		}
		return false;
	}
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
				sort(tags,0,tags.size());
				return true;
			}else if(com <0){
				low = middle+1; 
			}else{
				high = middle-1;
			}
		}
		
		return false;
	}
	public boolean hasTag(Identity tag){
		if(tags==null ||tags.size()==0){return false;}
		int low = 0;
		int high = tags.size()-1;
		Identity temp = null;

		while(low<=high){
			int middle = (low+high)/2;
			temp = tags.get(middle);
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
}
