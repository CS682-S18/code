package udppeer;

import udppeer.UDPPeerMessages.Packet;
import udppeer.UDPPeerMessages.Type2;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSender {

    public static void main(String[] args) throws Exception {

        int testData = 999;
        DatagramSocket socket = new DatagramSocket(8088);

        Type2 msg = Type2.newBuilder().setIntData(testData).build();
        Packet packet = Packet.newBuilder().setT2(msg).build();
        ByteArrayOutputStream outstream = new ByteArrayOutputStream(1024);
        packet.writeDelimitedTo(outstream);
        byte[] item = outstream.toByteArray();

        //send
        DatagramPacket datagramPacket = new DatagramPacket(item, item.length, InetAddress.getLocalHost(), 8080);
        socket.send(datagramPacket);
    }
}
