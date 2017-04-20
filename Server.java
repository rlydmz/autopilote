import javax.json.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static JsonObject fromStringToJson(String str) {
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

            //On attend qu'un client interroge le serveur
            Socket socket = serveur.accept();

            //Création des flux d'entrées/sorties
            ObjectOutputStream oos= new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            System.out.println("AFFICHAGE BRUT DE L'OBJECT :");

            String registerString = (String)ois.readObject();

            oos.writeObject(b.generalHandler(fromStringToJson(registerString)).toString());

            while(true){

            }

            //oos.close();
            //ois.close();

            //socket.close();

        }

    }

}


