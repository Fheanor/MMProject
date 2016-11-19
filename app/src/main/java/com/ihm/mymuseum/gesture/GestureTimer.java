package com.ihm.mymuseum.gesture;

import java.util.logging.Logger;

/**
 * Created by Julian on 19/11/2016.
 */

public class GestureTimer extends Thread {

    private long maxTime;
    private boolean timeFinished = false;
    private boolean isGestureFinished = false;
    private OnFinishedListener listener;

    public GestureTimer(long maxTime) {
        this.maxTime = Math.abs(maxTime);
    }

    @Override
    public void run() {
        try{
            synchronized (this){
                Logger.getAnonymousLogger().info("start wait");
                this.wait(maxTime);
                timeFinished = true;
                Logger.getAnonymousLogger().info("time finished");
            }
            if(listener != null) listener.onFinished();

        }catch(InterruptedException e){
            synchronized (this){ timeFinished = false;}
        }

    }

    public synchronized void setGestureFinished(boolean b){
        isGestureFinished = b;
        if(isGestureFinished) {
            this.notify();
            Logger.getAnonymousLogger().info("Set Gesture finished");
        }
    }

    public interface OnFinishedListener {
        void onFinished();
    }

    public void setOnFinishedListener(OnFinishedListener listener){
        this.listener = listener;
    }

    public synchronized boolean isTimeFinished(){
        return timeFinished;
    }
}
