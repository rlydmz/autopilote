import javax.json.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class ClientCapteur{

    //M�thode permettant de v�rifier si l'identification sur le serveur
    //a �t� fructueuse ou non
    //Si oui, la fonction renvoie 0
    //Sinon elle renvoie le code d'erreur associ�
    //Pour le moment inutilis�e
    //A utiliser plus tard lorsqu'on recevra une r�ponse de la part du serveur
    public static int checkRegistration(JsonObject obj){
        JsonObject ackObj = obj.getJsonObject("ack");
        if(ackObj.getString("resp").equals("ok"))
            return 0;
        else
            return ackObj.getInt("error_id");
    }

    public static void main(String[] args) throws Exception {

        //Cr�ation de la socket client (port conforme aux sp�cifications)
        @SuppressWarnings("resource")
		Socket client = new Socket("127.0.0.1", 7182);

        
        //Cr�ation des flux d'entr�es/sorties
        OutputStream outputDatas = client.getOutputStream();
        	

        //Cr�ation d'un objet de type 'Accelerometter'
        Accelerometer acc = new Accelerometer(5,5,5);
        acc.setName("ACC1");
        acc.setClasse("Accelerometter");
        
        JsonObject accJsonObj = acc.toSendJsonObject();
        Message accMsg = new Message(0, accJsonObj);
        
        outputDatas.write(accMsg.toCompleteJsonObject().toString().getBytes(Charset.forName("UTF-8")));
        
        
        
        
        outputDatas.close();

    }

}