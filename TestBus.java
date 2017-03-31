import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.util.Iterator;

public class TestBus {

    public static void main(String[] args) throws Exception {

        //Création d'un objet de type 'Accelerometer'
        Accelerometer acc = new Accelerometer(5,5,5);
        Gyroscope gyr = new Gyroscope(45, 69, 55);
        GPS gps = new GPS(244.333, 255.145);

        Bus b = new Bus();

        acc.setName("Babar");

        System.out.println(b.registerHandler(acc).toString());
        System.out.println(b.registerHandler(gyr).toString());
        System.out.println(b.registerHandler(gps).toString());

        System.out.println(b.listHandler().toString());
        System.out.println(b.listHandlerViaClass("Accelerometer").toString());
        System.out.println(b.listHandlerViaName("Babar").toString());

    }

}
