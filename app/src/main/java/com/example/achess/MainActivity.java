package com.example.achess;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.achess.R.id.hod;

public class MainActivity extends AppCompatActivity implements OnTouchListener  {
    DrawTest drawView;
    private Button mButton;
    AttributeSet attributeSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            drawView=(DrawTest)findViewById(R.id.drawTest);
            drawView.setOnTouchListener(this);

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;

            String game_status = drawView.desc1.get_game_status();
            TextView textEl = (TextView) findViewById(hod);
            textEl.setText( game_status);


            drawView.setLayoutParams(new LinearLayout.LayoutParams(width, width));

            mButton=(Button)findViewById(R.id.button1);
            View.OnClickListener oclBtnClear = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast toast = Toast.makeText(getApplicationContext(),"начать сначала",Toast.LENGTH_LONG);
                   //toast.show();
                    drawView.clear_desc();
                    drawView.invalidate();
                    v.invalidate();
                }
            };
            mButton.setOnClickListener(oclBtnClear);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
       // Toast toast = Toast.makeText(getApplicationContext(),String.valueOf(Math.round(event.getX())) +"  " +String.valueOf(Math.round(event.getY())) ,Toast.LENGTH_SHORT);
       // toast.show();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                this.drawView.desc1.Play(Math.round(event.getX()), Math.round(event.getY()));

                String game_status = drawView.desc1.get_game_status();
                TextView textEl = (TextView) findViewById(hod);
                textEl.setText( game_status);

                v.invalidate();
                break;
        }

        return true;
    }

}
