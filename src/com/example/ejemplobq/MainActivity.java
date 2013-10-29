package com.example.ejemplobq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;
import com.example.ejemplobq.adapter.GridViewAdapter;
import com.example.ejemplobq.data.EpubItem;
import com.example.ejemplobq.data.EpubList;

public class MainActivity extends Activity {

	private static final String TAG = "DBRoulette";

    ///////////////////////////////////////////////////////////////////////////
    //                      Your app-specific settings.                      //
    ///////////////////////////////////////////////////////////////////////////
    
    final static private String APP_KEY = "tah7upgdqwmq8sm"; 
    final static private String APP_SECRET = ""; 
    final static private AccessType ACCESS_TYPE = AccessType.DROPBOX;

    ///////////////////////////////////////////////////////////////////////////
    //                      End app-specific settings.                       //
    ///////////////////////////////////////////////////////////////////////////

    // You don't need to change these, leave them alone.
    final static private String ACCOUNT_PREFS_NAME = "prefs";
    final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
    final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
    final static private String EPUB_MIME_TYPE = "application/epub+zip";


    DropboxAPI<AndroidAuthSession> mApi;

    private boolean mLoggedIn;
   

    // Android widgets
    private Button mSubmit; 
    private GridViewAdapter mAdapter;

    
    //private String mCameraFileName;
    private Spinner spnOrder;
    private GridView gvData;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (savedInstanceState != null) {
            mCameraFileName = savedInstanceState.getString("mCameraFileName");
        }*/

        // We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);

        // Basic Android widgets
        setContentView(R.layout.activity_main);

        checkAppKeySetup();

        mSubmit = (Button)findViewById(R.id.auth_button);

       mSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // This logs you out if you're logged in, or vice versa
                if (mLoggedIn) {
                    logOut();
                } else {
                    // Start the remote authentication
                   // mApi.getSession().startAuthentication(DBRoulette.this);
                }
            }
        });

        
       
       
        
        ArrayAdapter<CharSequence> adapter =
        	    ArrayAdapter.createFromResource(this,
        	        R.array.valores_orden,
        	        android.R.layout.simple_spinner_item);
        
        spnOrder = (Spinner)findViewById(R.id.spnOrden);
        
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
         
        spnOrder.setAdapter(adapter);
        
       	addListenerOnSpinnerItemSelection();  
        
        mApi.getSession().startAuthentication(MainActivity.this);
        
        com.dropbox.client2.DropboxAPI.Entry contact;
		try {
			contact = mApi.metadata("/Epubs", 0, null, true, null);
		
	        List<Entry> CFolder = contact.contents;
	        
	        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	       
	        Date date;
	        String strDate;
	        EpubItem item;
	        EpubList list;
	      
	        
	        list = new EpubList();
	        
	        for (Entry entry : CFolder) {
	        	if(entry.mimeType != null && entry.mimeType.equals(EPUB_MIME_TYPE)){
	        	
		        	item = new EpubItem();
		        		        	
		        	strDate = entry.clientMtime;
		        	
		        	date = df.parse(strDate);	        	      	
		        	item.setName(entry.fileName());
		        	item.setDate(date);
		        	list.addItem(item);
		        	
		        	Log.i("DbExampleLog", "Filename: " + entry.fileName());	        	
		        	Log.i("DbExampleLog", "Date orig: " + String.valueOf(entry.clientMtime));
		        	Log.i("DbExampleLog", "Date: " +  String.valueOf(date));
		        	Log.i("DbExampleLog", "Filename: " + String.valueOf(entry.thumbExists));        	
		        	Log.i("DbExampleLog", "Icon: " + String.valueOf(entry.icon));
	        	}
	        }
	        
	        mAdapter = new GridViewAdapter(this,list);
	        
	        // Set custom adapter to gridview
	        gvData = (GridView) findViewById(R.id.gvData);
	        gvData.setAdapter(mAdapter);       
	       
	        
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("fin ", "todo");        


    }
    
    public void addListenerOnSpinnerItemSelection()
    {
	 spnOrder = (Spinner)findViewById(R.id.spnOrden);
	 //spnOrder.setOnItemSelectedListener(new CustomOnItemSelected());
    	//int order;
    	/*if(spnOrder.getSelectedItemId() == GridviewAdapter.ORDER_BY_NAME)
    		order 
    */
    	
    	spnOrder.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				gvData = (GridView)findViewById(R.id.gvData);
				int order = (int)spnOrder.getSelectedItemId();
				if(gvData.getAdapter() != null)
				{
					((GridViewAdapter)gvData.getAdapter()).orderList(order);
					gvData.setAdapter(gvData.getAdapter());
				}
				
				
				//ArrayAdapter<Entry> ad = new ArrayAdapter<Entry>(this,(List)dataArray,android.R.layout.simple_list_item_1);
				//gvData.setBackgroundColor(Color.BLACK);
				//gvData.setNumColumns(3);
				//gvData.setGravity(Gravity.CENTER);
				//gvData.setAdapter(ad);
				//gvData.setBackgroundResource(R.drawable.black_cloud1);
				/*gvData.setOnItemClickListener(new OnItemClickListener() {
				                    public void onItemClick(AdapterView<?> arg0, View arg1,
				                            int arg2, long arg3) {
				                        // TODO Auto-generated method stub
				                        Toast.makeText(mContext,gv.getItemAtPosition(arg2).toString(),Toast.LENGTH_SHORT).show();

				                        temp.setData(fnames,gv.getItemAtPosition(arg2).toString());

				                        return;
				                    }

				                    });*/
				
				}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
				
				
			

			
		});
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putString("mCameraFileName", mCameraFileName);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = mApi.getSession();

        // The next part must be inserted in the onResume() method of the
        // activity from which session.startAuthentication() was called, so
        // that Dropbox authentication completes properly.
        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                TokenPair tokens = session.getAccessTokenPair();
                storeKeys(tokens.key, tokens.secret);
                //setLoggedIn(true);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                Log.i(TAG, "Error authenticating", e);
            }
        }
    }

    // This is what gets called on finishing a media piece to import
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == NEW_PICTURE) {
            // return from file upload
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                if (uri == null && mCameraFileName != null) {
                    uri = Uri.fromFile(new File(mCameraFileName));
                }
                File file = new File(mCameraFileName);

                if (uri != null) {
                    UploadPicture upload = new UploadPicture(this, mApi, PHOTO_DIR, file);
                    upload.execute();
                }
            } else {
                Log.w(TAG, "Unknown Activity Result from mediaImport: "
                        + resultCode);
            }
        }*/
    }

    private void logOut() {
        // Remove credentials from the session
        mApi.getSession().unlink();

        // Clear our stored keys
        clearKeys();
        // Change UI state to display logged out version
        //setLoggedIn(false);
    }

    /**
     * Convenience function to change UI state based on being logged in
     */
    /*private void setLoggedIn(boolean loggedIn) {
    	mLoggedIn = loggedIn;
    	if (loggedIn) {
    		mSubmit.setText("Unlink from Dropbox");
            mDisplay.setVisibility(View.VISIBLE);
    	} else {
    		mSubmit.setText("Link with Dropbox");
            mDisplay.setVisibility(View.GONE);
            mImage.setImageDrawable(null);
    	}
    }*/

    private void checkAppKeySetup() {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") ||
                APP_SECRET.startsWith("CHANGE")) {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            finish();
            return;
        }

        // Check if the app has set up its manifest properly.
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            showToast("URL scheme in your app's " +
                    "manifest is not set up correctly. You should have a " +
                    "com.dropbox.client2.android.AuthActivity with the " +
                    "scheme: " + scheme);
            finish();
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     *
     * @return Array of [access_key, access_secret], or null if none stored
     */
    private String[] getKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key != null && secret != null) {
        	String[] ret = new String[2];
        	ret[0] = key;
        	ret[1] = secret;
        	return ret;
        } else {
        	return null;
        }
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void storeKeys(String key, String secret) {
        // Save the access key for later
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.putString(ACCESS_KEY_NAME, key);
        edit.putString(ACCESS_SECRET_NAME, secret);
        edit.commit();
    }

    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session;

        String[] stored = getKeys();
        if (stored != null) {
            AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
        } else {
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
        }

        return session;
    }

}
