import Logica.FileMethods;
import jdk.net.ExtendedSocketOptions;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class Client {

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private String clientPath = "client/src/data/";


    public static void main(String[] args) {
        var client = new Client();
        client.run();
    }

    private void run() {
        String host = "localhost";
        int port = 4444;
        KSocket socket = new KSocket(host, port);
        KConsole console = new KConsole();

        String fromServer;
        String fromUser;

        FileMethods logica = new FileMethods();


        while ((fromServer = socket.readLine()) != null) {

            console.writeLine("Server: " + fromServer);

            if (fromServer.equals("Bye."))
                break;


            //create method
            if(fromServer.equalsIgnoreCase("geef een filepath op voor create"))
            {
                try {

                    var name  = console.readLine();

                    socket.writeLine("0x06 -" + name);
                    System.out.println(
                            "Sending the File  from client to the Server");

                    logica.create(clientPath + name, socket.getSocket());

                }
                catch(Exception e) {
                    e.printStackTrace();
                }

            }
            if(fromServer.equalsIgnoreCase("geef een filepath op voor het verwijderen"))
            {
                try {
                    var name  = console.readLine();

                    socket.writeLine("0x08 - " + name);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }

            }
            if(fromServer.equalsIgnoreCase("geef een filepath op voor het updaten"))
            {
                try {
                    var name  = console.readLine();

                    socket.writeLine("0x08 - " + name);

                    socket.writeLine("0x06 - " + name);
                    System.out.println(
                            "Sending the File  from client to the Server");

                    logica.create(clientPath + name, socket.getSocket());

                }
                catch(Exception e) {
                    e.printStackTrace();
                }

            }


            fromUser = console.readLine();

            if (fromUser != null) {
                //console.writeLine("Client: " + fromUser);
                socket.writeLine(fromUser);
            }


        }
    }
}
