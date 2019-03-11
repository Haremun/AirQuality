package com.esp8266collection.airquality;

public class RotationThread extends Thread {

    private boolean animationStart = false;
    private RotationCallback callback;
    private int percent;
    private int angle;
    private int hopDistance;
    private boolean animationUp = true;
    //private int hopsCount = 100;

    public RotationThread(RotationCallback callback) {
        this.callback = callback;
    }

    public void startAnimation(int start, int end){
        animationStart = true;
        hopDistance = (end - start)/100;
    }
    public void stopAnimation(){
        animationStart = false;
    }

    @Override
    public void run() {
        while (animationStart) {
            if(animationUp){
                percent++;
                angle += hopDistance;
            } else {
                percent--;
                angle -= hopDistance;
            }

            if (percent > 100)
                animationUp = false;
            if (percent <= 0){
                animationUp = true;
            }
            callback.AnimationUpdate(percent, angle);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
