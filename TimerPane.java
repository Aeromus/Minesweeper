package MineSweeper;

import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by Andrew Knoblach on 12/8/2016.
 */
public class TimerPane {

    private Timer timer = new Timer();

    public void incrementTime(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Source.seconds++;
                Source.timerCount.setText("" + Source.seconds);
            }}, 0, 1000);
    }


    public void startTimer(){
        incrementTime();
    }

    public void stop(){
        timer.cancel();
    }
}
