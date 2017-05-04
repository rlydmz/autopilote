import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.Socket;

public class SimulateurWindow extends JFrame{

    public static JsonObject fromStringToJson(String str) {
        JsonReader jsonReader = Json.createReader(new StringReader(str));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

    /* Attributs de la fenêtre
    **************************/
    private JPanel zone = new JPanel();

    private JLabel zLabel = new JLabel("Z :");
    private JLabel zValue = new JLabel("Z :");

    public SimulateurWindow(){

        this.setTitle("Simulateur");
        this.setSize(800, 800);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.add(new TrainCanvas());

        this.setVisible(true);

    }

    class TrainCanvas extends JComponent {

        private int lastX = 0;
        private int lastY = 0;
        private int lastZ = 0;

        public TrainCanvas() {
            Thread animationThread = new Thread(new Runnable() {
                public void run() {

                    ObjectOutputStream oos = null;
                    ObjectInputStream ois = null;
                    JsonObject answer = null;
                    JsonObject tmp = null;

                    //Création de la socket client (port conforme aux spécifications)
                    Socket client = null;
                    try {
                        client = new Socket("127.0.0.1", 7182);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Création des flux d'entrées/sorties
                    try {
                        oos= new ObjectOutputStream(client.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        ois = new ObjectInputStream(client.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Création du gestionnaire de requetes
                    ClientHandler ch = new ClientHandler();

                    //Création d'un objet Json de type 'send' (voir méthode de classe dans Capteur.java)
                    JsonObject requestJsonObj = Json.createObjectBuilder()
                            .add("type", "get_last")
                            .add("sender_id", 1)
                            .build();

                    //Envoie de l'objet au serveur
                    String str = requestJsonObj.toString();

                    while (true) {
                        try {
                            oos.writeObject(str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            answer = ch.getHandler(fromStringToJson((String) ois.readObject()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        if(answer != null){
                            tmp = answer.getJsonObject("contents");
                            lastX = tmp.getInt("x");
                            lastY = tmp.getInt("y");
                            lastZ = tmp.getInt("z");
                        }
                        repaint();
                        try {Thread.sleep(15);} catch (Exception ex) {
                        }
                    }
                }
            });

            animationThread.start();
        }

        public void paintComponent(Graphics g) {
            Graphics2D gg = (Graphics2D) g;

            int w = getWidth();
            int h = getHeight();

            int trainW = 50;

            int x = lastX + 100;
            int y = 500-lastY + 100;

            gg.setColor(Color.BLACK);

            gg.drawLine(100,100,100,600+trainW);
            gg.drawLine(100,600+trainW,600+trainW,600+trainW);

            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            gg.drawString("X", 680, 650);
            gg.drawString("Y", 95, 80);
            gg.drawString("X = ", 100, 720);
            gg.drawString(Integer.toString(lastX), 150, 720);
            gg.drawString("Y = ", 300, 720);
            gg.drawString(Integer.toString(lastY), 350, 720);
            gg.drawString("Z = ", 500, 720);
            gg.drawString(Integer.toString(lastZ), 550, 720);

            gg.setColor(Color.BLUE);
            gg.fillOval(x, y, trainW, trainW);
        }

    }

}
