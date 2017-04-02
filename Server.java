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

    
    @SuppressWarnings("resource")
	static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    	
    
    public static void main(String[] args) throws Exception {

        //Création de la socket serveur
        ServerSocket serveur = new ServerSocket(7182);

        while(true){
        	
        	

            //On attend qu'un client interroge le serveur
            Socket socket = serveur.accept();

            //Création des flux d'entrées/sorties
            InputStream inputDatas = socket.getInputStream();
            
            // VERSION STRING 
            String theString = convertStreamToString(inputDatas);
            


            System.out.println("-----------------------------");

            System.out.println("AFFICHAGE DE L'OBJECT");
            System.out.println(theString);

            inputDatas.close();
            

            socket.close();

        }

    }

}
