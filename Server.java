import javax.json.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    //Affiche le contenu de l'objet JSon de type "register"
    public static void printJsonRegisterObject(JsonObject obj){
        System.out.println("type : " + obj.getString("type"));
        System.out.println("sender_class : " + obj.getString("sender_class"));
        System.out.println("sender_name : " + obj.getString("sender_name"));
    }

    //Affiche le contenu de l'objet JSon de type "send"
    public static void printJsonSendObject(JsonObject obj){
        System.out.println("type : " + obj.getString("type"));
        System.out.println("sender_id : " + obj.getInt("sender_id"));
        System.out.println("contents : " + obj.getJsonObject("contents"));
    }

    public static void main(String[] args) throws Exception {

        //Création de la socket serveur
        ServerSocket serveur = new ServerSocket(7182);

        while(true){

            //On attend qu'un client interroge le serveur
            Socket socket = serveur.accept();

            //Création des flux d'entrées/sorties
            InputStream inputDatas = socket.getInputStream();
            JsonReader jsr = Json.createReader(inputDatas);

            JsonObject accJsonObj = jsr.readObject();

            System.out.println("AFFICHAGE BRUT DE L'OBJECT :");
            System.out.println(accJsonObj);

            System.out.println("-----------------------------");

            System.out.println("AFFICHAGE DE L'OBJECT EN LE 'PARSANT' :");
            printJsonSendObject(accJsonObj);

            jsr.close();
            inputDatas.close();

            socket.close();

        }

    }

}


