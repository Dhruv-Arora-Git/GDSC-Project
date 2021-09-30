package com.example.tictactoe;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    boolean gameActive = true;

    // Player representation
    // 0 - X
    // 1 - o

    int activePlayer = 0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    TextView status;
    // Game state Meanings
    // 0 - X
    // 1 - o
    // 2 - null

    int[][] winPosition = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
    };

    public void playerTap(View view){

        status = findViewById(R.id.status);
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        if (!gameActive){
            gameReset();
            return;
        }

        if (gameState[tappedImage] == 2 && gameActive) {
            if (gameState[tappedImage] == 2) {
                gameState[tappedImage] = activePlayer;
                img.setTranslationY(-1000f);
                if (activePlayer == 0) {
                    img.setImageResource(R.drawable.x);
                    activePlayer = 1;
                    status.setText("O's Turn - Tap to Play");
                } else {
                    img.setImageResource(R.drawable.o);
                    activePlayer = 0;
                    status.setText("X's Turn - Tap to Play");
                }
                img.animate().translationYBy(1000f).setDuration(300);
            }
            // check if anyone player has Won ?
            for (int[] winPosition :
                    winPosition) {
                // winning condition
                if ( gameState[winPosition[0]] != 2 && gameState[winPosition[1]] != 2 && gameState[winPosition[2]] != 2) {
                    if (gameState[winPosition[0]] == gameState[winPosition[1]] && gameState[winPosition[1]] == gameState[winPosition[2]]) {
                        // find out who ? X or O
                        gameActive = false;
                        String winnerStr;
                        if (gameState[winPosition[0]] == 0) {
                            winnerStr = "X has Won !";
                            status.setText(winnerStr);
                        } else {
                            winnerStr = "O has Won !";
                            status.setText(winnerStr);
                        }
                    }
                }
            }
            boolean emptySquare = false;
            for (int squareState : gameState){
                if (squareState == 2){
                    emptySquare = true;
                    break;
                }
            }
            if(!emptySquare && gameActive){
                gameActive = false;
                String winnerStr;
                winnerStr = "It's a Draw !";
                status.setText(winnerStr);
            }
        }
    }




    public void gameReset(){
        gameActive = true;
        activePlayer = 0;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }
        ((ImageView)findViewById(R.id.imageView1)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView8)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView9)).setImageResource(0);

        status.setText("X's Turn - Tap to Play");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Welcome ! 🤗", Toast.LENGTH_SHORT).show();
        mediaPlayer = MediaPlayer.create(this, R.raw.guitar);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

    }
    @Override
    protected void onPause() {
        if (this.isFinishing()){ //basically BACK was pressed from this activity
            mediaPlayer.stop();
            Toast.makeText(MainActivity.this, "YOU PRESSED BACK", Toast.LENGTH_SHORT).show();
        }
        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                mediaPlayer.stop();
                Toast.makeText(MainActivity.this, "YOU LEFT YOUR APP", Toast.LENGTH_SHORT).show();
            }
            else {
                mediaPlayer.stop();
                Toast.makeText(MainActivity.this, "YOU SWITCHED ACTIVITIES WITHIN YOUR APP", Toast.LENGTH_SHORT).show();
            }
        }
        super.onPause();
    }
}