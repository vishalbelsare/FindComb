package ch.ethz.tgumbschbsse.findcomb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.text.TextWatcher;
import android.text.Editable;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnApiRequestCompleted {

    private EditText mNameEntry;
    private Button mEasy, mCont;
    private int mScore;
    private int scoreLevel;

    public final static int REQUEST_CODE = 1;
    public final static int REQUEST_ONE = 5;
    public final static int REQUEST_TWO = 6;
    public final static int REQUEST_THREE = 7;
    public final static int REQUEST_FOUR = 8;
    public final static int REQUEST_FIVE = 9;
    public final static int REQUEST_INFINITY = 1000;
    public final static int REQUEST_INFINITY_TWO = 2000;
    public final static int REQUEST_INFINITY_LEADERBOARD = 1001;

    private Intent intent;
    private static int LGLOBAL = 1;
    String name;
    RequestParams params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.icon_mlcb);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        mNameEntry = (EditText) findViewById(R.id.et_name);
        //mEmail = (EditText) findViewById(R.id.et_email);
        mEasy = (Button) findViewById(R.id.b_easy);
        mCont = (Button) findViewById(R.id.b_cont);
        mNameEntry.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    mEasy.setEnabled(false);
                    //mEasy.setTextColor(Color.parseColor("#FF999A9A"));
                    //mEasy.setBackgroundColor( getResources().getColor(R.color.colorPrimary));
                    mCont.setEnabled(false);
                    //mCont.setTextColor(Color.parseColor("#FF999A9A"));
                    //mCont.setBackgroundColor( getResources().getColor(R.color.colorPrimary));

                } else {
                    mEasy.setEnabled(true);
                    //mEasy.setTextColor(Color.parseColor("white"));
                    //mEasy.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    mCont.setEnabled(true);
                    //mCont.setTextColor(Color.parseColor("white"));
                    //mCont.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //mImageView = (ImageView) findViewById(R.id.Logo);
        //Bitmap ethlogo = BitmapFactory.decodeResource(getResources(), R.drawable.eth);
        //ethlogo = getResizedBitmap(ethlogo,800,200);
        //mImageView.setImageBitmap(ethlogo);
        //mHard = (Button) findViewById(R.id.b_hard);

        //setting the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mScore = 0;


        //adding a click listener
        mEasy.setOnClickListener(this);
        mCont.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == mEasy) {
            intent = new Intent(this,GroceryTutorialActivity.class);
            intent.putExtra("Level", 1);
            startActivityForResult(intent, REQUEST_INFINITY_TWO);



        }
        else if(v==mCont){
            intent = new Intent(this,TutorialActivity.class);
            intent.putExtra("Level", 1);
            startActivityForResult(intent, REQUEST_INFINITY);


        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE: //Move to High score and finish
                int temp_score_lvl3 = data.getExtras().getInt("score");
                if (temp_score_lvl3 > 0) {
                    mScore = temp_score_lvl3;
                    scoreLevel = 3;
                }
                name = mNameEntry.getText().toString();
                params = new RequestParams();
                params.put("user", name);
                params.put("score", mScore);
                params.put("level", scoreLevel);
                params.put("username", "admin");
                params.put("password", "mlcb2017");
                intent =  new Intent(this, HighScore.class);
                intent.putExtra("name", name);
                intent.putExtra("score", mScore);
                intent.putExtra("level", scoreLevel);
                intent.putExtra("global", LGLOBAL);
                if(LGLOBAL == 1) {
                    Api postApi = new Api(this);
                    try {
                        Api.post(params);
                        System.out.println("Post level 3 ok");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    intent.putExtra("position", 15);
                    this.startActivity(intent);
                }
                break;
            case REQUEST_ONE:
                intent = new Intent(this,TutorialActivity.class);
                intent.putExtra("Level", 4);
                startActivityForResult(intent,REQUEST_INFINITY);
                break;
            case REQUEST_TWO:
                int temp_score_lvl1 = data.getExtras().getInt("score");
                if (temp_score_lvl1 > 0){//Level1 successful
                    mScore = temp_score_lvl1;
                    scoreLevel = 1;
                    intent = new Intent(this,TutorialActivity.class);
                    intent.putExtra("Level", 2);
                    startActivityForResult(intent,REQUEST_THREE);
                }
                else{
                    name = mNameEntry.getText().toString();
                    params = new RequestParams();
                    params.put("user", name);
                    params.put("score", mScore);
                    params.put("level", scoreLevel);
                    params.put("username", "admin");
                    params.put("password", "mlcb2017");
                    intent =  new Intent(this, HighScore.class);
                    intent.putExtra("name", name);
                    intent.putExtra("score", mScore);
                    intent.putExtra("level", scoreLevel);
                    intent.putExtra("global", LGLOBAL);
                    if(LGLOBAL == 1) {
                        Api postApi = new Api(this);
                        try {
                            Api.post(params);
                            System.out.println("Post ok");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        intent.putExtra("position", 15);
                        this.startActivity(intent);
                    }
                }
                break;
            case REQUEST_THREE:
                intent = new Intent(this,GameActivity.class);
                intent.putExtra("Level", 2);
                intent.putExtra("score", mScore);
                startActivityForResult(intent,REQUEST_FOUR);
                break;
            case REQUEST_FOUR:
                int temp_score_lvl2 = data.getExtras().getInt("score");
                if (temp_score_lvl2 > 0){//Level2 successful
                    mScore = temp_score_lvl2;
                    scoreLevel = 2;
                    intent = new Intent(this,TutorialActivity.class);
                    intent.putExtra("Level", 3);
                    startActivityForResult(intent,REQUEST_FIVE);
                }
                else{
                    name = mNameEntry.getText().toString();
                    params = new RequestParams();
                    params.put("user", name);
                    params.put("score", mScore);
                    params.put("level", scoreLevel);
                    params.put("username", "admin");
                    params.put("password", "mlcb2017");
                    intent =  new Intent(this, HighScore.class);
                    intent.putExtra("name", name);
                    intent.putExtra("score", mScore);
                    intent.putExtra("level", scoreLevel);
                    intent.putExtra("global", LGLOBAL);
                    if(LGLOBAL == 1) {
                        Api postApi = new Api(this);
                        try {
                            Api.post(params);
                            System.out.println("Post level 1 ok");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        intent.putExtra("position", 15);
                        this.startActivity(intent);
                    }
                }
                break;
            case REQUEST_FIVE://Level3
                intent = new Intent(this,GameActivity.class);
                intent.putExtra("score", mScore);
                intent.putExtra("Level", 3);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case REQUEST_INFINITY:
                System.out.println("Move to infinity");
                intent = new Intent(this,GameActivity.class);
                intent.putExtra("Level", 100);
                startActivityForResult(intent,REQUEST_INFINITY_LEADERBOARD);
                break;
            case REQUEST_INFINITY_TWO:
                System.out.println("Move to gropcery store");
                intent = new Intent(this,GroceryGameActivity.class);
                intent.putExtra("Level", 100);
                startActivityForResult(intent,REQUEST_INFINITY_LEADERBOARD);
                break;
            case REQUEST_INFINITY_LEADERBOARD:
                int result = data.getExtras().getInt("score");
                mScore = result; //we count negative levels
                System.out.println(String.valueOf(mScore));
                System.out.println(String.valueOf(result));
                scoreLevel = 4;
                name = mNameEntry.getText().toString();
                params = new RequestParams();
                params.put("user", name);
                params.put("score", mScore);
                params.put("level", scoreLevel);
                params.put("username", "admin");
                params.put("password", "mlcb2017");
                intent =  new Intent(this, HighScore.class);
                intent.putExtra("name", name);
                intent.putExtra("score", mScore);
                intent.putExtra("level", scoreLevel);
                intent.putExtra("global", LGLOBAL);
                if(LGLOBAL == 1) {
                    Api postApi = new Api(this);
                    try {
                        Api.post(params);
                        System.out.println("Post level 4 ok");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    intent.putExtra("position", 15);
                    this.startActivity(intent);
                }
                break;


        }
    }
    @Override
    public void taskCompleted(JSONArray response) {
        System.out.println("finished posting");
        System.out.println(response);

        int position = 0;
        try {
            JSONObject positionjson = (JSONObject) response.get(0);
            position = positionjson.getInt("position");
        }catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(position);
        intent.putExtra("position", position);
        this.startActivity(intent);

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
