package com.example.ejemplobq.data;

import java.util.Date;

import android.widget.ImageView;

public class EpubItem {

	private ImageView Image;
	private String Name;
    private Date Date;
    
    public EpubItem(){}
    
    public EpubItem(ImageView image, String name, Date date)
    {
    	this.Image = image;
    	this.Name = name;
    	this.Date = date;    			
    }
    
    
	public ImageView getImage() {
		return Image;
	}
	public void setImage(ImageView image) {
		Image = image;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Date getDate() {
		return Date;
	}
	public void setDate(Date date) {
		Date = date;
	}
    
	
}
