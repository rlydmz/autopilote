import javax.json.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static JsonObject fromStringToJson(String str) {
        JsonReader jsonReader = Json.createReader(new StringReader(str));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

    public static void main(String[] args) throws Exception {

        //Création de la socket serveur
        ServerSocket serveur = new ServerSocket(7182);

        Bus b = new Bus();

        while(true){

            Socket socket = serveur.accept();
            new Thread(new ServerData(socket, b)).start();
            System.out.println("Connection réussie !");

        }

    }

}


