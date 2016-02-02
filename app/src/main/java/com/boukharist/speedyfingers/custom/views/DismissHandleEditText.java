package com.boukharist.speedyfingers.custom.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Administrateur on 1/22/2016.
 */
public class DismissHandleEditText extends EditText {

    public DismissHandleEditText(Context context) {
        super(context);
        init();
    }

    public DismissHandleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public DismissHandleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public DismissHandleEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER_HORIZONTAL);
        setTextSize(30);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_UP) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
        //    this.setText("");
        }
        return super.onKeyDown(keyCode, event);
    }




}
