import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.Socket;

public class ServerData implements Runnable {

    public static JsonObject fromStringToJson(String str) {
        JsonReader jsonReader = Json.createReader(new StringReader(str));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

    private Socket socket;
    private Bus bus;

    public ServerData(Socket socket, Bus b) {
        this.socket = socket;
        this.bus = b;
    }

    @Override
    public void run(){
        //Création des flux d'entrées/sorties
        ObjectOutputStream oos= null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String request = "";
        JsonObject answerObj;

        while(true){
            try {
                request = (String)ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println(request);
            answerObj = bus.generalHandler(fromStringToJson(request));
            try {
                oos.writeObject(answerObj.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
