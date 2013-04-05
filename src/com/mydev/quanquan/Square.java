package com.mydev.quanquan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.HttpConnectionMetricsImpl;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mydev.quanquan.SquareTalkList.SquareTalkItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Square  extends Activity {
	final static int max_range = 3000;
	final static int min_range = 500;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_panel);
		
		setSeekbar((SeekBar)findViewById(R.id.square_range_select));
//		setHouhou((Button)findViewById(R.id.square_talk_publish));
		
		mList_view_talk = (ListView) findViewById(R.id.square_talk_list);
		mSquare_talk_adapter = new SquareTalkList(this);
		
		mList_view_talk.setAdapter(mSquare_talk_adapter);
		mCircle_progress = (ProgressBar) findViewById(R.id.load_more_prog);
		mList_view_talk.setOnScrollListener(mScroll_update);
	}
	void setSeekbar(SeekBar sb)
	{
		final double MAX_RANGE=10000;
		sb.setMax((int)MAX_RANGE);
		
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			TextView range_text = (TextView)mContext.findViewById(R.id.range_text);
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int range = (int)(seekBar.getProgress()/MAX_RANGE * (max_range - min_range) + min_range);
				mContext.mRange_select = range;
				// TODO  更新查询
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				int range = (int)(seekBar.getProgress()/MAX_RANGE * (max_range - min_range) + min_range);
				range_text.setText(Integer.toString(range) + 
						mContext.getString(R.string.range_unit));
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int range = (int)(seekBar.getProgress()/MAX_RANGE * (max_range - min_range) + min_range);
				range_text.setText(Integer.toString(range) + 
						mContext.getString(R.string.range_unit));
			}
		});
	}
	
	public void squareTalkPublish(View v){
//		Toast.makeText(this, "发表言论一下下", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.setClass(this, com.mydev.quanquan.EditorHouhou.class);
		startActivity(intent);
		
	}
	public final class ScrollUpdate implements OnScrollListener{
		@Override
		public void onScroll(AbsListView view, int firstVisible,
				int visibleCount, int totalCount) {

	        boolean loadMore = (
	            firstVisible + visibleCount >= totalCount);
	        // mIs_need_load 只有在初始化和某个thread运行完之后才会被激活
	        // 这样可以防止一个thread没运行完就启动下一个thread
	        if(loadMore && (mCircle_progress.getVisibility() == View.INVISIBLE)) {
//	        	mSquare_talk_adapter.load_more();
//	        	mSquare_talk_adapter.notifyDataSetChanged();
//	        	
	        	
//	        	Toast.makeText(mContext, "loadmore", Toast.LENGTH_SHORT).show();
//	        	Log.i("LoadMore", "firstVisible = "+firstVisible+
//	        			", visibleCount = "+visibleCount+", totalCount = "+totalCount);
	        	mCircle_progress.setVisibility(View.VISIBLE);
	        	new Thread(mLoad_more).start();
	        }
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			
		}
		
	}
	public class LoadMore implements Runnable
	{
		ArrayList<SquareTalkItem> square_talks;
		String url;
		LoadMore(String Url){
			url = Url;
			square_talks = new ArrayList<SquareTalkItem>();
		}
		@Override
		public void run() {
			// 这里是在子线程：模拟网络通信
//			SystemClock.sleep(2000);
			getNewestSquareTalks();
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// 这里就回到主线程了。
					//  Only the original thread that created a view hierarchy 
					// can touch its views.
//					mSquare_talk_adapter.load_more();
					mSquare_talk_adapter.update(square_talks);
					mCircle_progress.setVisibility(View.INVISIBLE);
					mSquare_talk_adapter.notifyDataSetChanged();
				}
			});
		}
		void getNewestSquareTalks()
		{
			HttpGet get = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			try{
				HttpResponse response = client.execute(get);
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity().getContent()));
					String json_str = reader.readLine();
					JSONArray ja = new JSONArray(json_str);
					int len = ja.length();
					SquareTalkItem item; 
					for( int i = 0; i < len; ++i ){
						JSONObject obj = ja.getJSONObject(i);
						item = new SquareTalkItem(obj.getString("content"),
								obj.getString("time"));
						square_talks.add(item);
					}
						
				}
			}catch(ClientProtocolException e) {
		           e.printStackTrace();
	       } catch (IOException e) {
		           e.printStackTrace();
	       }catch (JSONException e) {
			// TODO: handle exception
	    	  e.printStackTrace();
	       }
		}	
	}
	String server_url = "http://oohouhou.duapp.com/";
	public LoadMore mLoad_more = new LoadMore(server_url);
	Square mContext = this;
	SquareTalkList mSquare_talk_adapter;
	ScrollUpdate mScroll_update = new ScrollUpdate();
	int mRange_select = min_range;
	Handler mHandler = new Handler();
//	Thread mLoad_thread = new Thread(mLoad_more);
	ListView mList_view_talk;
	ProgressBar mCircle_progress;
}
