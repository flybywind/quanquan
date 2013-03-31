package com.mydev.quanquan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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
	}
	void setSeekbar(SeekBar sb)
	{
		final double MAX_RANGE=10000;
		sb.setMax((int)MAX_RANGE);
		
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			TextView range_text = (TextView)context.findViewById(R.id.range_text);
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int range = (int)(seekBar.getProgress()/MAX_RANGE * (max_range - min_range) + min_range);
				context.range_select = range;
				// TODO  ¸üÐÂ²éÑ¯
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int range = (int)(seekBar.getProgress()/MAX_RANGE * (max_range - min_range) + min_range);
				range_text.setText(Integer.toString(range) + 
						context.getString(R.string.range_unit));
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				int range = (int)(seekBar.getProgress()/MAX_RANGE * (max_range - min_range) + min_range);
				range_text.setText(Integer.toString(range) + 
						context.getString(R.string.range_unit));
			}
		});
	}
	
	public void squareTalk(View v){
		
	}
	Square context = this;
	int range_select = min_range;
}
