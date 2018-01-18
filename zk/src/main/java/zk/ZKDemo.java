package zk;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by srollins on 1/17/18.
 */
public class ZKDemo {

    /*
    Much of this code is taken from the following example:
    https://nofluffjuststuff.com/blog/scott_leberknight/2013/07/distributed_coordination_with_zookeeper_part_3_group_membership_example

    IntelliJ was initially unable to find the ZooKeeper dependency.
    The following solved the problem:
    mvn dependency:resolve -U
    */

    //class instance running on mc01 port 2181
    //only accessible from machines behind the CS firewall!
    //use an ssh tunnel as necessary
    //ssh -L <local_port>:mc01.cs.usfca.edu:2181 <username>@stargate.cs.usfca.edu
    public static final int PORT = 2181;
    public static final String HOST = "mc01";

    public static void main(String[] args) throws IOException, InterruptedException {

        String group = "/zkdemo";
        String member = "/member";
        String data = "placeholder";

        //Connect to ZK instance
        final CountDownLatch connectedSignal = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper(HOST + ":" + PORT, 1000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        connectedSignal.countDown();
                    }
                }
            });
        System.out.println("Connecting...");
        connectedSignal.await();
        System.out.println("Connected");

        //Create a new group
        //Note this will be completed for you for your project assignments!
        try {
            zk.create(group, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("Group " + group + " created");

        } catch(KeeperException ke) {
            System.out.println("Unable to create new group " + group + "...maybe it already exists?");
            //ke.printStackTrace();
        }

        //to join a group
        try {
            String createdPath = zk.create(group + member,
                    data.getBytes(),  //probably should be something more interesting here...
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT_SEQUENTIAL);
            System.out.println("Joined group " + group + member);

        } catch(KeeperException ke) {
            System.out.println("Unable to join group " + group + " as " + member);
        }

        //to list members of a group
        try {
            List<String> children = zk.getChildren(group, false);
            for(String child: children) {
                System.out.println(child);

                //get data about each child
                Stat s = new Stat();
                byte[] raw = zk.getData(group + "/" + child, false, s);
                if(raw != null) {
                    String returnedData = new String(raw);
                    System.out.print("\t");
                    System.out.println(returnedData);
                } else {
                    System.out.println("\tNO DATA");
                }
            }
        } catch(KeeperException ke) {
            System.out.println("Unable to list members of group " + group);
        }



    }

}
