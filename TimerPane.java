package MineSweeper;

import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by Andrew Knoblach on 12/8/2016.
 */
 class TimerPane {

    private Timer timer = new Timer();

    private void incrementTime(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Source.seconds++;
                Source.timerCount.setText("" + Source.seconds);
            }}, 0, 1000);
    }


     void startTimer(){
        incrementTime();
    }

     void stop(){
        timer.cancel();
    }
}
