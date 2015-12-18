package com.ayoub.speedyfingers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int mIndex;
    String mCurrentWord;
    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String from = "Breakfast procuring nay end happiness allowance assurance frankness. Met simplicity nor difficulty unreserved who. Entreaties mr conviction dissimilar me astonished estimating cultivated. On no applauded exquisite my additions. Pronounce add boy estimable nay suspected. You sudden nay elinor thirty esteem temper. Quiet leave shy you gay off asked large style.";
        final String[] splitted = from.split(" ");


        textView = (TextView) findViewById(R.id.text);
        editText = (EditText) findViewById(R.id.edit);


        mask(splitted, 0);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentWord = splitted[mIndex].substring(1, splitted[mIndex].length() - 1);


                Log.d("natija", "text changed to = " + s.toString());
                Log.d("natija", "splitted index = " + splitted[mIndex]);
                Log.d("natija", "currentWord = " + currentWord);


                if (currentWord.equalsIgnoreCase(s.toString())) {
                    Log.d("natija", "boooo");
                    mIndex++;
                    mask(splitted, mIndex);
                    editText.setText("");
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


    private void mask(String[] original, int index) {
        try {
            original[index - 1] = original[index - 1].replace("{", "");
            original[index - 1] = original[index - 1].replace("}", "");
        } catch (Exception ex) {
            Log.e("natija", ex.getMessage());
        }

        original[index] = new StringBuilder().append("{").append(original[index]).append("}").toString();
        String formatted = "";
        for (String str : original) {
            formatted += new StringBuilder().append(" ").append(str).toString();
        }


        CharSequence charSequence = ColorPhrase.from(formatted)
                .withSeparator("{}")
                .innerColor(0xFFE6454A)
                .outerColor(0xFF666666)
                .format();

        textView.setText(charSequence);

    }
}
