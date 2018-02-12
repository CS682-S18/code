import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by srollins on 2/12/18.
 */
public class EvenListener implements NumberListener, Runnable {

    private static final int MAX_COUNT = 5;
    private String name;
    private int evensCount;
    private int evensTotal;

    private NumberGenerator parent;

    private int lastNumber;

    public EvenListener(String name, NumberGenerator parent) {

        this.name = name;
        this.evensCount = 0;
        this.evensTotal = 0;

        this.lastNumber = -1;

        this.parent = parent;

    }

    public void numberEvent(int number) {

        //process event
        synchronized (this) {
            if(number % 2 == 0) {
                lastNumber = number;
                this.notify();
            }
        }

    }

    public void run() {
        //wait for next event
        synchronized (this) {
            while (evensCount < MAX_COUNT) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                this.evensCount++;
                this.evensTotal += lastNumber;
            }
            finish();
        }
    }

    private void finish() {
        this.parent.deregisterListener(this);
        System.out.println(name + ": Evens found - " + evensCount + " Sum - " + evensTotal);
    }

}
