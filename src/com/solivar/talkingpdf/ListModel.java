package com.solivar.talkingpdf;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;

public class ListModel {
    
    private  String CompanyName="";
    private  Bitmap Image=null;
    private  String Url="";
     
    /*********** Set Methods ******************/
     
    public void setCompanyName(String CompanyName)
    {
        this.CompanyName = CompanyName;
    }
     
    public void setImage(Bitmap Image)
    {
        this.Image = Image;
    }
     
    public void setUrl(String Url)
    {
        this.Url = Url;
    }
     
    /*********** Get Methods ****************/
     
    public String getCompanyName()
    {
        return this.CompanyName;
    }
     
    public Bitmap getImage()
    {
        return this.Image;
    }
 
    public String getUrl()
    {
        return this.Url;
    }    
}