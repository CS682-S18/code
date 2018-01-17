package dateserver;

import dateserver.DateServerMessages.DateServerRequest;
import dateserver.DateServerMessages.DateServerResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by srollins on 1/16/18.
 */
public class DateClient {


    public static void main(String[] args) {

        try (
                Socket sock = new Socket(InetAddress.getLocalHost(), DateServer.PORT); //connecting to localhost
                OutputStream outstream = sock.getOutputStream();
                InputStream instream = sock.getInputStream()
        ) {

            //TODO: get this from the user or a config file.
            String format = "yyyy-MM-dd";
            //String format = "yyyy-MM-dd HH:mm:ss";
            DateServerRequest request = DateServerRequest.newBuilder().setFormat(format).build();
            request.writeDelimitedTo(outstream);
            DateServerResponse response = DateServerResponse.getDefaultInstance();
            response = response.parseDelimitedFrom(instream);
            System.out.println("Response: [" + response + "]");
            FileOutputStream outfile = new FileOutputStream("test.out");
            response.writeDelimitedTo(outfile);

        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
