package com.solivar.talkingpdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import com.solivar.talkingpdf.R;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.codec.Base64.InputStream;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PdfViewer extends ActionBarActivity implements
TextToSpeech.OnInitListener{

	private TextToSpeech tts;
	Button btnSpeak;
	TextView tv;
	String response = null;
	ProgressDialog pDialog;
	String path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdf_viewer);
		ActionBar actionBar=getSupportActionBar();
		actionBar.setIcon(R.drawable.talking_pdf);
		tts = new TextToSpeech(this, this);
        tv= (TextView) this.findViewById(R.id.textView);
        Bundle bundle = getIntent().getExtras();
        path = bundle.getString("filePath");
        pDialog = new ProgressDialog(this);
        new LoadFileContent().execute("");
        
	}
    
	
	@Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
 
    //@Override
    public void onInit(int status) {
 
        if (status == TextToSpeech.SUCCESS) {
 
            int result = tts.setLanguage(Locale.US);
 
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
         //       btnSpeak.setEnabled(true);
                speakOut();
            }
 
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
 
    }
 
    @SuppressWarnings("deprecation")
	private void speakOut() {
 
    	if(response.equals(""))
    		tts.speak("Sorry. No proper text in the pdf", TextToSpeech.QUEUE_FLUSH, null);
    	else
    		tts.speak(response, TextToSpeech.QUEUE_FLUSH, null);
    
    }
    
    private class LoadFileContent extends AsyncTask<String, Void, String>
	{
		protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		setContent();
		return null;
	}
		
		public void setContent()
		{
			AssetManager assetManager =getAssets();
	        InputStream istr = null;
	        String str=null;
	        int n=0;
	        BufferedReader br = null;
	        
	        
	        try {
	            StringBuffer output = new StringBuffer();
	            
	        PdfReader reader = new PdfReader(path);
	        PdfReaderContentParser parser = new PdfReaderContentParser(reader);

	        
	        StringWriter strW = new StringWriter();
	        response="";
	        TextExtractionStrategy strategy;
	        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
	        	
	            strategy = parser.processContent(i,
	                    new SimpleTextExtractionStrategy());

	            strW.write(strategy.getResultantText());
	            response = response+strW.toString();
	        }

	        

	    } catch (IOException e) {
	        e.printStackTrace();
	        
	    }
	        
		}
		
		protected void onPostExecute(String result) {
			tv.setText(response);
			
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pdf_viewer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		
		if(id == R.id.action_speak)
		{
			speakOut();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
