package udppeer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import udppeer.UDPPeerMessages.Packet;

public class UDPReceiver {


    public static void main(String[] args) {

        try {
            DatagramSocket socket = new DatagramSocket(8080);

            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);

            try {
                socket.receive(packet);
                byte[] rcvdData = packet.getData();

                ByteArrayInputStream instream = new ByteArrayInputStream(rcvdData);
                Packet protoPkt = Packet.parseDelimitedFrom(instream);

                if(protoPkt.hasT1()) {
                    System.out.println(protoPkt.getT1().getStrData());
                } else if(protoPkt.hasT2()) {
                    System.out.println(protoPkt.getT2().getIntData());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }

    }


}
