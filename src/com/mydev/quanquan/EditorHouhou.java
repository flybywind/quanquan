package com.mydev.quanquan;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.R.drawable;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public final class EditorHouhou extends Activity{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.texteditor);
		mLocationManager = new MyLocationManager(editor_activity);
		TelephonyManager phoneMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		mThisPhoneNum = phoneMgr.getLine1Number(); 
	}
	
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		((Button)findViewById(R.id.del_btn)).setVisibility(View.INVISIBLE);
		((Button)findViewById(R.id.share_btn)).setVisibility(View.INVISIBLE);
		((Button)findViewById(R.id.save_btn)).setText(R.string.submit);
		mEditorBody = (EditText)findViewById(R.id.editor_note);
		mEditorHeader = (EditText)findViewById(R.id.real_world_location);
	}
	public void clickSave(View v) 
			throws ClientProtocolException, IOException
	{
//		Toast.makeText(editor_activity, "发布", Toast.LENGTH_SHORT).show();
		String text_body;
		String location;
//		if( mEditorBody.getText() == null ){
//			Toast.makeText(editor_activity, "please type body", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if( mEditorHeader.getText() == null ){
//			location = "unknown";
//		}else{
//			
//		}
		location = mEditorHeader.getText().toString();
		text_body = mEditorBody.getText().toString();
		if(text_body.isEmpty()){
			Toast.makeText(editor_activity, "please type body", Toast.LENGTH_SHORT).show();
			return;
		}
		if(location.isEmpty()){
			location = "unknown";
		}
		new Thread(new PostTalk(text_body, location)).start();
	}
	public void clickAddEmotion(View v){
//		Toast.makeText(editor_activity, "添加表情", Toast.LENGTH_SHORT).show();
	}
	
	class PostTalk implements Runnable{
		String text_body;
		String location;
		HttpResponse response;
		public PostTalk(String text_body, String loc){
			this.text_body = text_body;
			this.location = loc;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			LatLng bestLatLng = mLocationManager.getBestLocation();
			SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String time = date_format.format(new java.util.Date());
			HttpClient client = new DefaultHttpClient();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			HttpPost post_req = new HttpPost(SERVER);
			nameValuePairs.add(new BasicNameValuePair("user_id", mThisPhoneNum));
			nameValuePairs.add(new BasicNameValuePair("latitude", 
						Double.toString(bestLatLng.latitude)));
			nameValuePairs.add(new BasicNameValuePair("longitude", 
						Double.toString(bestLatLng.longitude)));
			nameValuePairs.add(new BasicNameValuePair("content", text_body));
			nameValuePairs.add(new BasicNameValuePair("location", location));
			nameValuePairs.add(new BasicNameValuePair("time", time));
			
			try {
				post_req.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
				response = client.execute(post_req);
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if( HttpStatus.SC_OK != response.getStatusLine().getStatusCode() )
						Toast.makeText(editor_activity, "发送失败, RET: "+response.getStatusLine().getStatusCode(),
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(editor_activity, "发送成功", Toast.LENGTH_SHORT).show();
				}
			});			
		}
		
	}
	Handler mHandler = new Handler();
	private String mThisPhoneNum;
	private EditText mEditorBody;
	private EditText mEditorHeader;
	private MyLocationManager mLocationManager;
	private EditorHouhou editor_activity = this;
	static String SERVER = "http://oohouhou.duapp.com/post.php";
//	static String SERVER = "http://fly.allalla.com/post.php";
}
