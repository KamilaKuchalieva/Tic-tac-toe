package com.kamila.tic_tac_toe;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout playAgainLayout;
    private TextView gameResultText;
    private Button playAgainButton;
    private GridLayout gridlayout;

    // 0 = yellow, 1 = red
    private int activePlayer = 0;
    private boolean gameIsActive = true;

    // to keep the state and prevent user from taping the spot which has already been filled
    private int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2 };

    // arrays of win cases
    private int [][] winCases = {{0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}};

    // function to be called when user taps one of the grids/spots
    public void dropIn(View view ){

        ImageView counter = (ImageView) view;
        // find out which counter has been tapped
        int tappedCounter = Integer.parseInt(counter.getTag().toString());
        int savedActivePlayer = activePlayer;

        // before letting the player play the game check if they tapped an empty (valid, not occupied) spot
        // and if the game is still active (not over)
        if(gameState[tappedCounter] == 2 && gameIsActive){

            // mark this spot as tapped
            gameState[tappedCounter] = activePlayer;

            // make the drop down image initially invisible to the user by transitioning it out of screen
            counter.setTranslationY(-1000f);

            // check which user's turn it is currently
            if(activePlayer == 0){
                // set to user 'yellow'
                counter.setImageResource(R.drawable.yellow);
                // switch to the next user
                activePlayer = 1;
            }
            else{
                // set to user 'red'
                counter.setImageResource(R.drawable.red);
                // switch to the next user
                activePlayer = 0;
            }
            // drop the current user's image down on to the tapped grid
            counter.animate().translationYBy(1000f).setDuration(500);

            // check if the current tap has resulted in a win case or a draw
            tapResult(savedActivePlayer);
        }
    }

    public void tapResult(int currentPlayer){
        // check if the last tap resulted in a win case, by looping through every win case and checking if the
        // spots mentioned in the given win case have the same value, where value should have changed to
        // something else (0 or 1) from 2
        for(int[] winCase : winCases){
            if(gameState[winCase[0]] == gameState[winCase[1]] && gameState[winCase[1]] == gameState[winCase[2]] &&
                    // make sure that this winning case does not consist of all empty spots
                    gameState[winCase[0]] != 2){

                // mark the game as finished
                gameIsActive = false;

                if(currentPlayer == 0){
                    showGameResultAnimation("Yellow wins!");
                }else{
                    showGameResultAnimation("Red wins!");
                }
            }
            else
            {
                // if all grids have been tapped but no win case happened, then it's a draw
                boolean isDraw = true;
                for (int counterState : gameState){
                    if(counterState == 2){
                        isDraw = false;
                    }
                }

                if(isDraw){
                    showGameResultAnimation("It's a draw!");
                }

            }
        }
    }

    public void showGameResultAnimation(String text){
        playAgainLayout.setTranslationY(-1000f);
        playAgainLayout.animate().translationYBy(1000f).rotation(7200).setDuration(500);
        playAgainLayout.setVisibility(View.VISIBLE);
        gameResultText.setText(text);
    }

    public void playAgain(View view){
        // reset the game back to its initial state
        gameIsActive = true;
        playAgainLayout.setVisibility(View.INVISIBLE);
        activePlayer = 0;

        for(int i = 0; i<gameState.length; i++){
            gameState[i] = 2;
        }

        for(int i=0; i<gridlayout.getChildCount(); i++){
            // set every grid to empty image
            ((ImageView) gridlayout.getChildAt(i)).setImageResource(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playAgainLayout = findViewById(R.id.playAgainLayout);

        gameResultText =  findViewById(R.id.gameResultText);

        playAgainButton = findViewById(R.id.playAgainButton);

        gridlayout = findViewById(R.id.gridLayout);

        playAgainButton.setOnClickListener(new View.OnClickListener () {
            public void onClick(View view){
                playAgain(view);
            }
        });
    }
}
