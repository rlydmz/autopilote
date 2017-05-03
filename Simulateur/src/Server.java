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

            /*
            //On attend qu'un client interroge le serveur
            Socket socket = serveur.accept();

            //Création des flux d'entrées/sorties
            ObjectOutputStream oos= new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            String registerString = (String)ois.readObject();

            oos.writeObject(b.generalHandler(fromStringToJson(registerString)).toString());

            String request;
            JsonObject answerObj;

            while(true){
                request = (String)ois.readObject();
                System.out.println(request);
                answerObj = b.generalHandler(fromStringToJson(registerString));
                oos.writeObject(answerObj.toString());
            }

            //oos.close();
            //ois.close();

            //socket.close();
            */

        }

    }

}


