package com.mydev.quanquan;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Square  extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_panel);
		setSpinner((Spinner)findViewById(R.id.square_range_select));
	}
	void setSpinner(Spinner v){
		ArrayAdapter<CharSequence> range_adapter = 
				ArrayAdapter.createFromResource(this, R.array.range_list,
						R.layout.range_spinner_item);
//						android.R.layout.simple_selectable_list_item);
		range_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		v.setAdapter(range_adapter);
		v.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "pos = "+pos, Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		}); 
	}
	public void squareTalk(View v){
		
	}
	Context context = this;
}
