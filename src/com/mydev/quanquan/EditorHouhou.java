package com.mydev.quanquan;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

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
		String location = mEditorHeader.getText().toString();
		String text_body = mEditorBody.getText().toString();
		SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String time = date_format.format(new java.util.Date());
		HttpClient client = new DefaultHttpClient();
		
		String post_str = SERVER+"?content="+text_body+
								 "&location="+location+
								 "&time="+time;
		HttpPost post_req = new HttpPost(post_str);
		if( HttpStatus.SC_OK != client.execute(post_req).getStatusLine().getStatusCode() )
			Toast.makeText(editor_activity, "发送失败", Toast.LENGTH_SHORT).show();
	}
	public void clickAddEmotion(View v){
//		Toast.makeText(editor_activity, "添加表情", Toast.LENGTH_SHORT).show();
	}
	private EditText mEditorBody;
	private EditText mEditorHeader;
	private LatLng mCurGeoPoint;
	private EditorHouhou editor_activity = this;
	static String SERVER = "http://oohouhou.duapp.com/post.php";
	
}
