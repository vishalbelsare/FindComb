package ch.ethz.tgumbschbsse.findcomb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Arrays;

/**
 * Created by tgumbsch on 8/10/17.
 */
public class GameView extends SurfaceView implements Runnable {

    volatile boolean playing;
    private Thread gameThread = null;

    // In case moving is an option
    private Point mPlayer;

    // Layout stuff
    private Rectangle mrectRed;
    private Rectangle mrectBlue;
    private Rectangle mrectGreen;
    private Rectangle mrectPurple;
    private Rectangle mrectYellow;
    private Picture mLevel;

    // Mechanics
    private boolean[] rbgpy;
    private int mLevelIndicator;
    private int mScore;
    private long mTimestamp;


    //These objects will be used for drawing
    //private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Context mContext;

    public GameView(Context context) {
        super(context);

        int[] red = { Color.rgb(212,0,0),Color.rgb(255,0,0)};
        int[] blue = { Color.rgb(0,90,154),Color.rgb(0,112,192)};
        int[] green = { Color.rgb(0,141,64),Color.rgb(0,176,80)};
        int[] purple = { Color.rgb(90,38,128),Color.rgb(112, 48, 160)};
        int[] yellow = { Color.rgb(212,212,0),Color.rgb(255,255,0)};

        // The visuals
        mrectRed = new Rectangle((new Rect(100,800,300,1000)),red);
        mrectBlue = new Rectangle((new Rect(450,800,650,1000)),blue);
        mrectGreen = new Rectangle((new Rect(800,800,1000,1000)),green);
        mrectPurple = new Rectangle((new Rect(1150,800,1350,1000)),purple);
        mrectYellow = new Rectangle((new Rect(1500,800,1700,1000)),yellow);

        //init mechanics
        mScore = 120; //The player has two minutes
        mLevelIndicator = 1;
        mTimestamp = System.currentTimeMillis();


        //initializing drawing objects
        surfaceHolder = getHolder();
        mContext = context;


        LevelInit();

        //Stuff that we might need later
        mPlayer = new Point(1500,300);
        playing = true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPlayer.set((int) event.getX(), (int) event.getY());
                mrectGreen.checkClicked(mPlayer);
                mrectRed.checkClicked(mPlayer);
                mrectBlue.checkClicked(mPlayer);
                mrectPurple.checkClicked(mPlayer);
                mrectYellow.checkClicked(mPlayer);
                mLevel.checkClicked(mPlayer);
        }


        return true;
        //return super.onTouchEvent(event);
    }


    private void update() {

        if((System.currentTimeMillis()-mTimestamp) > 1000 ) {
            mScore--;
            mTimestamp = System.currentTimeMillis();
        }

        if (mScore < 0){
            playing = false;
        }

        if(mLevel.clicked == true){
            //Evaluate configuration
            boolean[] rbgpyPlayer = {mrectRed.clicked, mrectBlue.clicked, mrectGreen.clicked, mrectPurple.clicked, mrectYellow.clicked};
            mrectBlue.processClicked();
            mrectRed.processClicked();
            mrectGreen.processClicked();
            mrectPurple.processClicked();
            mrectYellow.processClicked();
            if (Arrays.equals(rbgpy, rbgpyPlayer)) {
                mScore = mScore + 10;

            } else {
                mScore = mScore - 10;
                //canvas.drawText("WRONG", 600, 700, paint);
            }

            //What happens next?
            if (mLevelIndicator <= 15) {
                mLevelIndicator++;
                LevelInit();
            }
            else{
                playing = false;
            }
        }
    }

    private void draw() {
        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.BLACK);
            //drawing a background color for canvas
            canvas.drawColor(Color.WHITE);

            if(playing == true) {
                mLevel.draw(canvas);
                mrectRed.draw(canvas);
                mrectBlue.draw(canvas);
                mrectGreen.draw(canvas);
                mrectPurple.draw(canvas);
                mrectYellow.draw(canvas);
                canvas.drawText(String.valueOf(mScore), 1650, 100, paint);
                //Unlocking the canvas

            }
            else{
                if(mScore < 0){
                    canvas.drawText("Game Over", 600, 600, paint);
                }
                else{
                    canvas.drawText("Your Score: " + String.valueOf(mScore), 500, 600, paint);
                }

            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }








    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }


    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (playing != true){

            while((System.currentTimeMillis()-mTimestamp)< 5000){
                control();
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("score",mScore);
            ((Activity) mContext).setResult(Activity.RESULT_OK, resultIntent);
            ((Activity) mContext).finish();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public int getScore(){
        return mScore;
    }






    // The level architecture

    public void LevelInit(){
        switch (mLevelIndicator){
            case 1:
                mLevel = new Picture(R.drawable.level1,mContext);
                rbgpy = new boolean[] {true,false,false,false,false};
                break;
            case 2:
                mLevel = new Picture(R.drawable.level2,mContext);
                rbgpy = new boolean[] {true,false,true,false,false};
                break;
            case 3:
                mLevel = new Picture(R.drawable.level3,mContext);
                rbgpy = new boolean[] {true,false,true,false,false};
                break;
            case 4:
                mLevel = new Picture(R.drawable.level4,mContext);
                rbgpy = new boolean[] {true,false,true,false,false};
                break;
            case 5:
                mLevel = new Picture(R.drawable.level5,mContext);
                rbgpy = new boolean[] {false,false,true,false,true};
                break;
            case 6:
                mLevel = new Picture(R.drawable.level6,mContext);
                rbgpy = new boolean[] {true,false,true,false,true};
                break;
            case 7:
                mLevel = new Picture(R.drawable.level7,mContext);
                rbgpy = new boolean[] {true,false,false,false,true};
                break;
            case 8:
                mLevel = new Picture(R.drawable.level8,mContext);
                rbgpy = new boolean[] {true,false,false,false,true};
                break;
            case 9:
                mLevel = new Picture(R.drawable.level9,mContext);
                rbgpy = new boolean[] {true,false,true,false,true};
                break;
            case 10:
                mLevel = new Picture(R.drawable.level10,mContext);
                rbgpy = new boolean[] {false,false,false,true,false};
                break;
            case 11:
                mLevel = new Picture(R.drawable.level11,mContext);
                rbgpy = new boolean[] {true,false,true,false,false};
                break;
            case 12:
                mLevel = new Picture(R.drawable.level12,mContext);
                rbgpy = new boolean[] {true,false,false,true,false};
                break;
            case 13:
                mLevel = new Picture(R.drawable.level13,mContext);
                rbgpy = new boolean[] {true,false,true,false,false};
                break;
            case 14:
                mLevel = new Picture(R.drawable.level14,mContext);
                rbgpy = new boolean[] {true,false,true,false,true};
                break;
            case 15:
                mLevel = new Picture(R.drawable.level15,mContext);
                rbgpy = new boolean[] {true,false,true,true,false};
                break;
        }

    }
}