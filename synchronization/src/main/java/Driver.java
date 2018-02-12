/**
 * Created by srollins on 2/12/18.
 */
public class Driver {

    public static void main(String[] args) {

        NumberGenerator ng = new NumberGenerator();
        Thread thread = new Thread(ng);
        thread.start();

        EvenListener evens = new EvenListener("Even Listener 1", ng);
        Thread listen1 = new Thread(evens);
        listen1.start();
        ng.registerListener(evens);

    }


}
