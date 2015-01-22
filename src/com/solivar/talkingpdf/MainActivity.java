package com.solivar.talkingpdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.solivar.talkingpdf.R;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.solivar.talkingpdf.ListModel;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity{
	
	String path;
	 ArrayList<String> allPdfs=new ArrayList<String>();
	 static int totalCount=0;
	ListView list;
    CustomAdapter adapter;
    public  MainActivity CustomListView = null;
    public  ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();
    ProgressDialog pDialog;
 
    @TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setIcon(R.drawable.talking_pdf);
        actionBar.setTitle("Talking Pdf");
        CustomListView = this;
        pDialog = new ProgressDialog(this);
        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        new LoadFiles().execute("");
        
                
    }   
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                
       /*         case R.id.change_account:
                {
                	ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                	  File directory = contextWrapper.getDir(filepath, Context.MODE_APPEND);
                	  myInternalFile = new File(directory , filename);
                	  boolean deleted = myInternalFile.delete();
                    Intent i=new Intent(getApplicationContext(), Registration.class);
              	  startActivity(i);
                                     
                }*/
            }
			return true;
        
         
    }
 
    /****** Function to set data in ArrayList *************/
    

   /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        ListModel tempValues = ( ListModel ) CustomListViewValuesArr.get(mPosition);
    
        Intent i=new Intent(getApplicationContext(), PdfViewer.class);
        i.putExtra("filePath", allPdfs.get(mPosition).toString());
        
        startActivity(i);
        
       
       // SHOW ALERT                  

        
    }
    

    
    private class LoadFiles extends AsyncTask<String, Void, String>
	{
		protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		setListData(Environment.getExternalStorageDirectory());
		return null;
	}
		
		protected void onPostExecute(String result) {
			
			Resources res =getResources();
	        list= ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )
	         
	        /**************** Create Custom Adapter *********/
	        adapter=new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
	        list.setAdapter( adapter );
	        
			pDialog.dismiss();
				//		finish();
        }

        protected void onPreExecute() {
    //       progressBar.setVisibility(View.VISIBLE);
        	
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.show();
     
        }

        protected void onProgressUpdate(Void... values) {

        }
    }
    
    public void setListData(File dir)
    {
    	String pdfPattern = ".pdf";

	    File[] listFile = dir.listFiles();

	    if (listFile != null) {
	        for (int i = 0; i < listFile.length; i++) {

	            if (listFile[i].isDirectory()) {
	                setListData(listFile[i]);
	            } else {
	              if (listFile[i].getName().endsWith(pdfPattern)){
	                              //Do what ever u want
	            	  allPdfs.add(listFile[i].getAbsolutePath());
	            	  totalCount+=1;
	            	  final ListModel sched = new ListModel();
	            	  String fileTitle=listFile[i].getName().toString();
	            	  String firstHalf="";
	            	  String lastHalf="";
	            	  if(fileTitle.length()>15)
	            	  {
	            		  firstHalf=fileTitle.substring(0,10);
	            		  lastHalf=fileTitle.substring(fileTitle.length()-10);
	            		  sched.setCompanyName(firstHalf+"..."+lastHalf);
	            	  }
	            	  else
	            		  sched.setCompanyName(fileTitle);
	            	  
	           			CustomListViewValuesArr.add( sched );

	              }
	            }
	        }
	    }
      /******** Take Model Object in ArrayList **********/
 //     CustomListViewValuesArr.addAll( allPdfs );
 	}
    
    
   
}


