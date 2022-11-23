package com.example.exc1final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private AppCompatImageView sea_IMG_background;
    private FloatingActionButton game_BTN_Right;
    private FloatingActionButton game_BTN_Left;
    private GameManager game;
    GameManager.Direction direction;
    private ArrayList<View> hearts;
    ArrayList<ArrayList<View>> viewsArray;
    private int DELAY = 1000;
    private Timer timer = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameInit();


        game_BTN_Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clicked(GameManager.Direction.RIGHT);


            }
        });

        game_BTN_Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked(GameManager.Direction.LEFT);
            }
        });


    }

    private void clicked(GameManager.Direction direction) {
        int row = game.getObjects().size() - 1;
        int col = 0;
        for (int i = 0; i < game.getObjects().get(row).size(); i++) {
            if (game.getObjects().get(row).get(i).getIsOn())
                col = i;
        }
        game.move(direction, row, col);


    }

    private ArrayList<View> getLinearLayoutChild(View view) {
        int layoutChildren = ((LinearLayout) view).getChildCount();
        ArrayList<View> children = new ArrayList<>();

        for (int i = 0; i < layoutChildren; i++) {
            View child = ((LinearLayout) view).getChildAt(i);

            if (child instanceof LinearLayout)
                children.add((LinearLayout) (((LinearLayout) view).getChildAt(i)));

            else if (child instanceof ImageView)
                children.add((ImageView) (((LinearLayout) view).getChildAt(i)));
        }
        return children;
    }


    private ArrayList<ArrayList<View>> findGridViews() {

        LinearLayout layout = (LinearLayout) findViewById(R.id.main_linearLayout_1);
        ArrayList<View> mainLinear = getLinearLayoutChild(layout);
        ;
        ArrayList<ArrayList<View>> grid = new ArrayList<>();

        for (int i = 0; i < getLinearLayoutChild(layout).size(); i++)
            grid.add(getLinearLayoutChild(mainLinear.get(i)));

        return grid;

    }


    private void initViewsGrid(ArrayList<ArrayList<PicObject>> objectsArray) {
        for (int i = 0; i < objectsArray.size(); i++) {
            for (int j = 0; j < objectsArray.get(i).size(); j++) {
                objectsArray.get(i).get(j).setImageRes((ShapeableImageView) viewsArray.get(i).get(j));
                objectsArray.get(i).get(j).setImage();
                objectsArray.get(i).get(j).setIsOn(false);

            }

        }

    }

    private void findAllViews() {
        viewsArray = findGridViews();
        hearts = getLinearLayoutChild(findViewById(R.id.game_Layout_Hearts));
        game_BTN_Right = findViewById(R.id.main_FAB2_Right);
        game_BTN_Left = findViewById(R.id.main_FAB1_Left);
        sea_IMG_background = findViewById(R.id.sea_IMG_background);

    }

    private void initHeatsView() {
        for (int i = 0; i < hearts.size(); i++)
            Glide.with(this).load("https://www.freepngimg.com/download/russia/86808-head-putin-vladimir-of-jaw-president-russia.png").into((ShapeableImageView) hearts.get(i));
    }

    private void initBackground() {

        Glide.with(this).load("https://wallpapershome.com/images/wallpapers/st-basil-039-s-cathedral-2160x3840-st-basils-cathedral-moscow-russia-red-square-5330.jpg").into((sea_IMG_background));
    }

    private void onCollision(int collisionRow, int collisionCol) {
        game.setWrong(game.getWrong() + 1);
        for (int h = 0; h < game.getWrong(); h++)
            hearts.get(h).setVisibility(View.INVISIBLE);
        vibrate();
        toast("fuc****!!!!");
        makeVoice(true);
        initRound();

    }


    private void setInitialCar() {
        game.getObjects().get(game.getObjects().size() - 1).get(0).setIsOn(true);
    }


    private void initRound() {
        game.buildNewRoundGrid();
        initViewsGrid(game.getObjects());
        game.initialState(2);
        setInitialCar();

    }

    private void gameInit() {

        findAllViews();
        game = new GameManager(hearts.size(), viewsArray.size(), viewsArray.get(0).size(), 2);
        initViewsGrid(game.getObjects());
        initBackground();
        initHeatsView();
        game.initialState(2);
        setInitialCar();
        startTimer();


    }

    private void startTimer() {


        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        gameProcess();


                    }
                });
            }
        }, DELAY, DELAY);


    }

    private void makeVoice(boolean answer) {
        final MediaPlayer mp;
        if (answer) {
            mp = MediaPlayer.create(this, R.raw.explosion);
            mp.start();
        }

    }
    private void vibrate(){
        Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        v.vibrate(400);
    }
    private void toast(String s){
        Toast.makeText(this, s,
                Toast.LENGTH_LONG).show();
    }

    private void gameProcess() {
        boolean[][] b = game.getCurrentOn();
        boolean collision = false;
        GameManager.Direction d = GameManager.Direction.DOWN;
        for (int i = 0; i < game.getObjects().size() && !collision; i++) {
            for (int j = 0; j < game.getObjects().get(i).size(); j++) {
                if (b[i][j] == true) {
                    game.move(d, i, j);
                    if (game.checkCollision(i, j)) {
                        onCollision(i, j);
                        break;
                    }


                }


            }

        }
    }


}











