package cs.numnum;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class NumActivity extends AppCompatActivity {


    private Button check;
    private Button reset;
    private EditText eText;
    private TextView title;
    private TextView time;
    private String number;
    private int playerScore;
    private ArrayList<String> perm = new ArrayList<>();
    private CountDownTimer cntdwn = new CountDownTimer(120000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            time.setText("Time: " + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            check.setEnabled(false);
            time.setText("Time: 00");
            title.setText("Time Up!");
            timerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    title.setText("Your Score: "+playerScore);
                }
            }, 2000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num);
        eText = (EditText) findViewById(R.id.editText);
        time = (TextView) findViewById(R.id.time_left);
        check = (Button) findViewById(R.id.check);
        reset = (Button) findViewById(R.id.reset);
        title = (TextView) findViewById(R.id.title_top);
        title.setText("Find the next highest integer!");
        check.setEnabled(false);
    }

    Handler timerHandler = new Handler();
    Runnable correct = new Runnable() {

        @Override
        public void run() {
            increment(null);
            reenable();
        }
    };
    Runnable next_game = new Runnable() {
        @Override
        public void run() {
            generateNum();
            title.setText(String.valueOf(number));
            reenable();
        }
    };

    public boolean onStart(View v){
        cntdwn.start();
        generateNum();
        title.setText(String.valueOf(number));
        return true;
    }

    private void generateNum(){
        perm.clear();
        Random rng = new Random();
        number = String.valueOf(rng.nextInt((9999 - 1000)+1)+1000);
        permutation("", number);
        Collections.sort(perm);
        perm.subList(0, perm.indexOf(number)+1).clear();
    }

    private void permutation(String prefix, String str){
        int n = str.length();
        if(n == 0 && !perm.contains(prefix)) {
            perm.add(prefix);
        }else{
            for(int i = 0; i < n; i++){
                permutation(prefix + str.charAt(i), str.substring(0,i)+str.substring(i+1, n));
            }
        }
    }

    private void reenable(){
        check.setEnabled(true);
        reset.setEnabled(true);
    }

    private void increment(View view){
        title.setText(perm.get(0));
        perm.remove(0);
    }

    public void click(View v){
        String str = eText.getText().toString();
        eText.getText().clear();
        check.setEnabled(false);
        reset.setEnabled(false);
        if(str.compareTo(perm.get(0)) == 0 && perm.size() > 1){
            title.setText("Correct!");
            playerScore += 5;
            timerHandler.postDelayed(correct, 1000);
        }
        else if(str.compareTo(perm.get(0)) == 0 && perm.size() == 1){
            playerScore += 10;
            title.setText("Next Number...");
            timerHandler.postDelayed(next_game, 1000);
        }else{
            cntdwn.cancel();
            time.setText("Time: 00");
            title.setText("End!");
            timerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    title.setText("Your score: "+ playerScore);
                    reset.setEnabled(true);
                }
            }, 2000);


        }
    }

    public void reset (View v){
        cntdwn.cancel();
        reenable();
        playerScore = 0;
        onStart(null);
    }

}
