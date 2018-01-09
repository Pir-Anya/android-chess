package com.example.achess;


import java.io.IOException;
import java.sql.SQLException;


import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Display;
import android.graphics.Point;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DrawTest extends ImageView {
	Paint p;
	Desc desc1;
	Display disp;

	public DrawTest(Context context, AttributeSet attributeSet) throws SQLException {

		super(context, attributeSet);
		p = new Paint();
		try {
			DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
			int width = (int)Math.ceil(displaymetrics.widthPixels/8);
			desc1 = new Desc(width,width,context);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void Refresh_Desc(Context context){
		desc1 = null;

		DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
		int width = (int)Math.ceil(displaymetrics.widthPixels/8);

		try {
			desc1 = new Desc(width,width,context);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		desc1.Draw_desc(p, canvas);
		desc1.Draw_Place(p, canvas);
		desc1.Draw_Figures(p, canvas);
	//	String status = desc1.get_game_status();
	//	TextView hodTextView = (TextView) findViewById(com.example.achess.R.id.hod);
	//	hodTextView.setText(status);
	//	desc1.Play(Math.round(77), Math.round(220));
	//	desc1.Play(Math.round(62), Math.round(359));

	//	desc1.Play(Math.round(82), Math.round(876));
	//	desc1.Play(Math.round(82), Math.round(754));
	}

	public void clear_desc() {
		desc1.clear_desc();
	}

}
