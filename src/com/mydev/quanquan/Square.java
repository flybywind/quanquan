package com.mydev.quanquan;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_panel);
		
		setSeekbar((SeekBar)findViewById(R.id.square_range_select));
		
		mList_view_talk = (ListView) findViewById(R.id.square_talk_list);
		mSquare_talk_adapter = new SquareTalkList(this);
		
		mList_view_talk.setAdapter(mSquare_talk_adapter);
		
		
		mCircle_progress = (ProgressBar) findViewById(R.id.load_more_prog);
//    	mList_view_talk.addFooterView(mCircle_progress);
//    	mCircle_progress.setVisibility(View.INVISIBLE);
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
				// TODO Auto-generated method stub
				int range = (int)(seekBar.getProgress()/MAX_RANGE * (max_range - min_range) + min_range);
				range_text.setText(Integer.toString(range) + 
						mContext.getString(R.string.range_unit));
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				int range = (int)(seekBar.getProgress()/MAX_RANGE * (max_range - min_range) + min_range);
				range_text.setText(Integer.toString(range) + 
						mContext.getString(R.string.range_unit));
			}
		});
	}
	
	public void squareTalk(View v){
		
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
	        	
	        	Toast.makeText(mContext, "loadmore", Toast.LENGTH_SHORT).show();
	        	Log.i("LoadMore", "firstVisible = "+firstVisible+
	        			", visibleCount = "+visibleCount+", totalCount = "+totalCount);
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
		@Override
		public void run() {
			// 这里是在子线程：模拟网络通信
			SystemClock.sleep(2000);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// 这里就回到主线程了。
					//  Only the original thread that created a view hierarchy 
					// can touch its views.
					mSquare_talk_adapter.load_more();
					mCircle_progress.setVisibility(View.INVISIBLE);
					mSquare_talk_adapter.notifyDataSetChanged();
				}
			});
		}
	}
	public LoadMore mLoad_more = new LoadMore();
	Square mContext = this;
	SquareTalkList mSquare_talk_adapter;
	ScrollUpdate mScroll_update = new ScrollUpdate();
	int mRange_select = min_range;
	Handler mHandler = new Handler();
//	Thread mLoad_thread = new Thread(mLoad_more);
	ListView mList_view_talk;
	ProgressBar mCircle_progress;
}
