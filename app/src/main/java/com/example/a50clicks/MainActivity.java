package com.example.a50clicks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    MediaPlayer heheheha, cry, button1, button2, button3, button4, button5, button6, button7, button8;
    Button start,tryAgain;
    TextView txtClicks,txtTimer,txtCords,txtVictoryTime,txtBestTime,txtPB;
    Thread timer;
    Boolean threadAlive;
    FloatingActionButton ball;
    float time,bestTime;
    DisplayMetrics displayMetrics;
    RelativeLayout layout1,layoutVictory;
    String[] colorList;
    MediaPlayer[] sounds;
    private static final String PREFS_NAME = "MyPrefsFile";
    int clicksInt, maxX, maxY, initialClicksInt, colorIndex, layoutLeft,layoutRight,layoutTop,layoutBottom, ballLeft,ballRight,ballTop,ballBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        maxX = displayMetrics.widthPixels;
        maxY = displayMetrics.heightPixels;

        //Save besttime
        SharedPreferences saveTime = getSharedPreferences(PREFS_NAME,0);
        bestTime = saveTime.getFloat("bestTime", bestTime);
        if(bestTime==0){bestTime=Integer.MAX_VALUE;}
        //sounds
        heheheha = MediaPlayer.create(this,R.raw.heheheha);
        cry = MediaPlayer.create(this,R.raw.cry);
        button1 = MediaPlayer.create(this,R.raw.button1);
        button2 = MediaPlayer.create(this,R.raw.button2);
        button3 = MediaPlayer.create(this,R.raw.button3);
        button4 = MediaPlayer.create(this,R.raw.button4);
        button5 = MediaPlayer.create(this,R.raw.button5);
        button6 = MediaPlayer.create(this,R.raw.button6);
        sounds = new MediaPlayer[]{button1, button2, button3, button4, button5, button6};


        //start button
        start = findViewById(R.id.start);
        start.setOnClickListener(this);

        //Relative layout
        layout1 = findViewById(R.id.layout1);
        txtCords = findViewById(R.id.cords);
        //clicks
        initialClicksInt = 50;
        clicksInt = initialClicksInt;
        txtClicks = findViewById(R.id.clicks);

        //victory
        layoutVictory = findViewById(R.id.layoutVictory);
        txtVictoryTime = findViewById(R.id.victoryTime);
        txtBestTime = findViewById(R.id.bestTime);
        tryAgain = findViewById(R.id.tryAgain);
        tryAgain.setOnClickListener(this);
        txtPB = findViewById(R.id.pb);

        //ball
        ball = findViewById(R.id.ball);
        ball.setOnClickListener(this);
        //roy g biv
        colorList = new String[]{"#FF0000","#FFa500", "#FFFF00", "#00FF00", "#0000FF", "#4B0082", "#7f00FF"};
        colorIndex = 0;
        //time
        time = 0;
        txtTimer = findViewById(R.id.timer);
        threadAlive = true;
        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    while (threadAlive) {
                        SystemClock.sleep(10);
                        time += 0.01;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTimer();
                            }

                        });
                    }
                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        int v = view.getId();
        if(v == R.id.start){
            start.setVisibility(View.GONE);
            layout1.setVisibility(View.VISIBLE);
            ball.setVisibility(View.VISIBLE);


            randomizeBall();
            timer.start();

        }
        if(v == R.id.ball){
            clicksInt-=1;
            updateScore();
            randomizeBall();
            if(clicksInt<=0){
                threadAlive = false;
                victory();
            }
        }
        if(v == R.id.tryAgain){
            restart();
        }
    }

    public void randomizeBall(){
        button6.start();
        ball.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorList[colorIndex])));
        colorIndex+=1;
        if(colorIndex>=7){colorIndex=0;}
        int setX, setY;
        //Do not overlap timer and clicks


        //ball boundaries
        setX = (int) (Math.random() * (maxX - 288));
        setY = (int) (Math.random() * (maxY - 288));


        boolean right = (setX+299)>365&&(setX+299)<714;
        boolean left = setX<714&&setX>365;
        boolean top = (setY+299)>812&&(setY+299)<1076;
        boolean bottom = setY<1076&&setY>832;
        while ((right&&top)||(right&&bottom)||(left&&top)||(left&&bottom)) {
            //Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
            setX = (int) (Math.random() * (maxX - 288));
            setY = (int) (Math.random() * (maxY - 288));

            right = (setX+299)>365&&(setX+299)<714;
            left = setX<714&&setX>365;
            top = (setY+299)>832&&(setY+299)<1200;
            bottom = setY<1076&&setY>832;

        }
        ball.setX(setX);
        ball.setY(setY);

        //DEVELOPER TOOL
        //int[] location = new int[2];
        //ball.getLocationOnScreen(location);
        //txtCords.setText(location[0]+" "+location[1]);
        //txtCords.setText(setX+" "+setY);
        //DEVELOPER TOOL

    }
    public void updateTimer(){
        txtTimer.setText(changeToTimer(time));
    }
    public void updateScore(){
        txtClicks.setText("Clicks remaining: "+clicksInt);
    }

    public void victory(){
        ball.setVisibility(View.GONE);
        layout1.setVisibility(View.GONE);
        layoutVictory.setVisibility(View.VISIBLE);
        if(time< bestTime)
        {
            //Save bestTime
            bestTime = time;
            SharedPreferences saveTime = getSharedPreferences(PREFS_NAME,0);
            SharedPreferences.Editor editor = saveTime.edit();
            editor.putFloat("bestTime",time);
            editor.commit();

            txtPB.setVisibility(View.VISIBLE);
            txtPB.setPaintFlags(txtPB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            heheheha.start();
        }
        else{cry.start();}
        txtVictoryTime.setText("TIME: "+changeToTimer(time));
        txtBestTime.setText("BEST TIME: "+changeToTimer(bestTime));
    }

    public void restart(){
        clicksInt = initialClicksInt;
        time = 0;
        threadAlive = true;
        layout1.setVisibility(View.VISIBLE);
        ball.setVisibility(View.VISIBLE);
        layoutVictory.setVisibility(View.GONE);
        txtPB.setVisibility(View.GONE);

        updateScore();
        randomizeBall();


    }
    public String changeToTimer(float t){
        return (int)t/60 + ":"+ (((int)(t%60)/10==0) ? "0":"") + (int)(t%60)+"."+(int)((t*100)%100);
    }


}