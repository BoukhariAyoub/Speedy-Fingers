package com.ayoub.speedyfingers;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    int mIndex, mScore, mKeyStroke;
    EditText mEditText;
    TextView mScoreText, mTimerTextView;
    WheelView mWheelView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mScoreText = (TextView) findViewById(R.id.tv_score);
        mTimerTextView = (TextView) findViewById(R.id.tv_timer);
        mWheelView = (WheelView) findViewById(R.id.main_wv);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTimerTextView.setText(String.format("%d", millisUntilFinished / 1000));
            }

            public void onFinish() {
                mTimerTextView.setText("done!");
            }
        }.start();


        String from = "Breakfast procuring nay end happiness allowance assurance frankness. Met simplicity nor difficulty unreserved who. Entreaties mr conviction dissimilar me astonished estimating cultivated. On no applauded exquisite my additions. Pronounce add boy estimable nay suspected. You sudden nay elinor thirty esteem temper. Quiet leave shy you gay off asked large style.";
        final String[] splitted = from.split(" ");


        mWheelView.setOffset(1);
        mWheelView.setItems(Arrays.asList(splitted));
        mWheelView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mWheelView.setSelection(mIndex);

        mWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d("natija", "selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });


        mEditText = (EditText) findViewById(R.id.edit);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (!text.toString().isEmpty()) {
                    mKeyStroke++;
                }
                Log.d("natija", "key stroke " + mKeyStroke);
                if (splitted[mIndex].equalsIgnoreCase(text.toString())) {
                    mIndex++;
                    mScore++;
                    mEditText.setText("");
                    mWheelView.setSelection(mIndex);
                    mScoreText.setText(String.format("Score : %d", mScore));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
