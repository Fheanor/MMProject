package com.ihm.mymuseum.gesture;

/**
 * Created by Julian on 19/11/2016.
 */

public class GestureTimer extends Thread {

    private long maxTime;
    private boolean timeFinished = false;
    private OnFinishedListener listener;

    public GestureTimer(long maxTime) {
        this.maxTime = Math.abs(maxTime);
    }

    @Override
    public void run() {
        try{
            synchronized (this){
                this.wait(maxTime);
                timeFinished = true;
            }
            if(listener != null) listener.onFinished();

        }catch(InterruptedException e){
            synchronized (this){ timeFinished = false;}
        }

    }

    public void setOnFinishedListener(OnFinishedListener listener){
        this.listener = listener;
    }

    public synchronized boolean isTimeFinished(){
        return timeFinished;
    }

    public interface OnFinishedListener {
        void onFinished();
    }
}
