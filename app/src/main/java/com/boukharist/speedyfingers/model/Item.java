package com.boukharist.speedyfingers.model;

import com.boukharist.speedyfingers.custom.views.WaveLoadingView;
import com.boukharist.speedyfingers.custom.views.PreciseCountdown;

/**
 * Created by Administrateur on 1/22/2016.
 */
public class Item {

    String text;
    long time = 0;
    long delay = 0;
    boolean isStopped = false;
    PreciseCountdown countdown;
    WaveLoadingView waveLoadingView;



    public Item(String text, long time, long delay, boolean isStopped) {
        this.text = text;
        this.time = time;
        this.delay = delay;
        this.isStopped = isStopped;
    }

    public Item(String text) {
        this.text = text;
    }

    public PreciseCountdown getCountdown() {
        return countdown;
    }

    public void setCountdown(PreciseCountdown countdown) {
        this.countdown = countdown;
    }

    public void setWaveLoadingView(WaveLoadingView waveLoadingView) {
        this.waveLoadingView = waveLoadingView;
    }

    public WaveLoadingView getWaveLoadingView() {
        return waveLoadingView;
    }




    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setIsStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }
}
