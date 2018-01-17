package dateserver;

import dateserver.DateServerMessages.DateServerRequest;
import dateserver.DateServerMessages.DateServerResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by srollins on 1/16/18.
 */
public class DateServer {

        public static final int PORT = 1024;
        private static boolean running = true;

        public static void main(String[] args) {

            int visitors = 0;
            try (
                    ServerSocket serve = new ServerSocket(PORT);
            ) {
                while(running) {

                    Socket sock = serve.accept();
                    InputStream instream = sock.getInputStream();
                    OutputStream outstream = sock.getOutputStream();

                    DateServerRequest request = DateServerRequest.parseDelimitedFrom(instream);
                    String format = request.getFormat();
                    System.out.println("Request for format: " + format);

                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    String date = sdf.format(new Date());
                    System.out.println("Response: " + date);

                    DateServerResponse response = DateServerResponse.newBuilder()
                            .setDate(date)
                            .setRequestNumber(++visitors)
                            .build();
                    response.writeDelimitedTo(outstream);
                    sock.close();
                }

            } catch(IOException ioe) {
                ioe.printStackTrace();
            }

        }

}
