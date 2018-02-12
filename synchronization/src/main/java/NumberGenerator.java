import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by srollins on 2/12/18.
 */
public class NumberGenerator implements Runnable {

    private Random r;
    private ConcurrentLinkedQueue<NumberListener> listeners;
    private volatile boolean running;

    public NumberGenerator() {
        this.r = new Random();
        this.listeners = new ConcurrentLinkedQueue<>();
        this.running = true;
    }

    public void run() {
        do {
            int number = r.nextInt(100);

            if(listeners.size() != 0) {
                System.out.println("Next number: " + number);
            }
            //notify listeners
            for(NumberListener listener: listeners) {
                listener.numberEvent(number);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        } while(running);
    }

    public void registerListener(NumberListener nl) {
        this.listeners.add(nl);
    }

    public void deregisterListener(NumberListener nl) {
        this.listeners.remove(nl);
        if(this.listeners.size() == 0) {
            this.running = false;
        }
    }
}
