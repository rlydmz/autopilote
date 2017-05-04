import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.Socket;

public class GyroArduino {

    private static JsonObject fromStringToJson(String str) {
        JsonReader jsonReader = Json.createReader(new StringReader(str));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

    public static void main(String[] args) throws Exception {


        CommPortIdentifier id;
        SerialPort port;

        BufferedReader is;

        System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
        id = CommPortIdentifier.getPortIdentifier("/dev/ttyACM0");
        port = (SerialPort)id.open("arduinoRead",1000);
        port.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

        is =  new BufferedReader(new InputStreamReader(port.getInputStream()));


        Socket client = new Socket("192.168.0.11", 7182);

        //Création des flux d'entrées/sorties
        ObjectOutputStream oos= new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

        //Création d'un objet de type 'Accelerometter'
        Gyroscope gyroscope = new Gyroscope();
        gyroscope.setName("GYR1");
        gyroscope.setClasse("Gyro");

        //Création du gestionnaire de requetes
        ClientHandler ch = new ClientHandler(gyroscope);

        //Création d'un objet Json de type 'send' (voir méthode de classe dans Capteur.java)
        JsonObject accJsonObj = gyroscope.toRegisterJsonObject();

        //Envoie de l'objet au serveur
        String str = accJsonObj.toString();
        System.out.println(str);
        oos.writeObject(str);

        while(ch.registerHandler(fromStringToJson((String)ois.readObject())) == false){
            System.out.println("Fail to register to server, retrying in 5 seconds");
            Thread.sleep(5000);
        }

        System.out.println(gyroscope.getId());

        String answer;

        int x=0, y=0, z=0;

        while(true){

            gyroscope.setX(x);
            gyroscope.setY(y);
            gyroscope.setZ(z);

            if(y>500)
                y=0;

            oos.writeObject(gyroscope.toSendJsonObject().toString());

            answer = (String)ois.readObject();
            if(ch.sendHandler(fromStringToJson(answer)) == true){
                System.out.println("Message bien reçu à " + System.currentTimeMillis());
            }

            Thread.sleep(15);
        }

    }

}
