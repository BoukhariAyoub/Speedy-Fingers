package com.ayoub.speedyfingers;

import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Administrateur on 19/12/2015.
 */
public abstract class SwissArmyKnife {


    /**
     * @param context current Context
     * @param font    the path of the font file in assets
     * @return TypeFace of the given font
     */
    private static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }


    /**
     * Set The Font Type To Given TextViews
     *
     * @param font      the path of FontAwesome font file in assets
     * @param textViews containers of icons
     */
    public static void setFontawesomeContainer(String font, TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setTypeface(getTypeface(textView.getContext(), font));
        }
    }


    /**
     * Converts a date to a timeless date
     *
     * @param date date with time
     * @return date with time at 00:00:00
     */
    public static Date getDateWithoutTime(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }


    public static String dateToString(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(date);
    }

    public static int getDaysDifference(Date date1, Date date2) {
        long diff = Math.abs(date1.getTime() - date2.getTime());
        long daysDiff = (diff / (1000 * 60 * 60 * 24));
        return (int) daysDiff;
    }

  /*  public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    } */


    public static String getStringFromFile(Context context, String filename,String separator) {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = context.getAssets().open(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str).append(separator);
            }

            in.close();
            return buf.substring(0,buf.length()-1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void randomizeList(ArrayList list){
        long seed = System.nanoTime();
        Collections.shuffle(list, new Random(seed));
    }

    public static void playSound(Context context,int sound){
        final MediaPlayer mp = MediaPlayer.create(context,sound);
        mp.start();
    }


}


