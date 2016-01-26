package com.ayoub.speedyfingers;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Administrateur on 1/22/2016.
 */
public class DismissHandleEditText extends EditText {

    public DismissHandleEditText(Context context) {
        super(context);
    }

    public DismissHandleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DismissHandleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DismissHandleEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_UP) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
