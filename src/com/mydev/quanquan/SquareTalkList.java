package com.mydev.quanquan;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.zip.Inflater;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SquareTalkList extends BaseAdapter {
	
	public SquareTalkList(Square parent){
		square = parent;
		initArrayFirst();
		load_cnt = 0;
	}
	public void load_more()
	{
		++ load_cnt;
		String msgs[] = new String[] { "haha", 
				"你好\n北京天气好差！", 
				"天气不错啊！\njust kidding....", 
				"have a good day",
				"lalalalalalalla",
				"llalalalalalalalalal",
				"lalalalaalalalalal",
				"lalalalalalaalalallaal"};
		
		SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String time = date_format.format(new java.util.Date());
		for( String msg : msgs){
			msg += ("\n load " + load_cnt + " times. "); 
			square_talk_list.add(new SquareTalkItem(msg, time));
		}
		
	}
	public void update(ArrayList<SquareTalkItem> new_talks)
	{
		for( SquareTalkItem talk : new_talks){
			square_talk_list.add(talk);
		}
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return square_talk_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return square_talk_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView,
			android.view.ViewGroup parent) {
		// TODO Auto-generated method stub
		if( null == convertView ){
			vg = new ViewHolder();
			convertView = LayoutInflater.from(square)
					.inflate(R.layout.square_talk_item, null);
			convertView.setTag(vg);
			vg.some_one_talk = (TextView) convertView.findViewById(R.id.some_one_talk);
			vg.square_talk_time = (TextView) convertView.findViewById(R.id.square_talk_time);
			vg.square_msg_op = (Button) convertView.findViewById(R.id.square_msg_op);
		}else
		{
			vg = (ViewHolder) convertView.getTag();
		}
		vg.some_one_talk.setText(square_talk_list.get(position).some_one_talk);
		vg.square_talk_time.setText(square_talk_list.get(position).publish_time);
		vg.square_msg_op.setOnClickListener(pay_attention);		
		return convertView;
	}
	void initArrayFirst()
	{
//		String msgs[] = new String[] { "haha", 
//				"你好\n北京天气好差！", 
//				"天气不错啊！\njust kidding....", 
//				"have a good day",
//				"lalalalalalalla",
//				"llalalalalalalalalal",
//				"lalalalaalalalalal",
//				"lalalalalalaalalallaal"};
//		SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		String time = date_format.format(new java.util.Date());
		square_talk_list = new ArrayList<SquareTalkItem>();
//		for( String msg : msgs){
//			square_talk_list.add(new SquareTalkItem(msg, time));
//		}
	}
	
	final static public class ViewHolder{
		TextView some_one_talk;
		TextView square_talk_time;
		Button square_msg_op;
	}
	final static public class SquareTalkItem{
		String some_one_talk;
		String publish_time;
		public SquareTalkItem(String talk, String time) {
			// TODO Auto-generated constructor stub
			some_one_talk = talk;
			publish_time = time;
		}
	}
	final static public class Payattention implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO 加关注：
			
		}
		
	}
	Square square;
	ViewHolder vg;
	ArrayList<SquareTalkItem> square_talk_list;
	Payattention pay_attention = new Payattention();
	static int load_cnt = 0;
}
