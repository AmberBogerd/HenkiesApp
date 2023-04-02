import Logica.FileMethods;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class Server {
    public static void main(String[] args) throws IOException {

        int port = 4444;
        KServerSocket serverSocket = new KServerSocket(port);
        KConsole console = new KConsole();

        while (true) {

            System.out.println("listening to port:5000");
            KSocket socket = serverSocket.accept();
            System.out.println(socket+" connected.");

            var handler = new Handler(console, socket);
            handler.start();
        }
    }
}

class Handler extends Thread {

    private KConsole console;
    private KSocket socket;

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private String serverPath = "server/src/data/";


    FileMethods logica = new FileMethods();

    Handler(KConsole console, KSocket socket) {
        this.console = console;
        this.socket = socket;
    }

    public void run() {
        String inputLine, outputLine;
        Protocol protocol = new Protocol();

        outputLine = protocol.processInput(null);
        //System.out.println("server:" + socket.getPort());

        socket.writeLine(outputLine);

        while ((inputLine = socket.readLine()) != null) {
            console.writeLine("Client: " + inputLine);
            outputLine = protocol.processInput(inputLine);
            socket.writeLine(outputLine);

            if(inputLine.contains("0x06"))
            {
                try{
                    String name = inputLine.substring(inputLine.lastIndexOf('-') + 1);
                    String path = serverPath+name;
                    logica.receiveFile(path, socket.getSocket());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(inputLine.contains("0x08"))
            {
                try{
                    String name = inputLine.substring(inputLine.lastIndexOf('-') + 1);
                    String path = serverPath+name;

                    logica.deleteFile(path);

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (outputLine.equals("Bye.")) {
                break;
            }
        }
    }
}
