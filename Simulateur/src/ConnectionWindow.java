import javax.swing.*;
import java.awt.*;

public class ConnectionWindow extends JFrame{

    /* Attributs de la fenêtre
    **************************/
    private JPanel informationPanel = new JPanel(new GridLayout(1,2));

    private JLabel adressLabel = new JLabel("Adresse :");
    private JLabel portLabel = new JLabel("Port :");

    private JTextField adress = new JTextField();
    private JTextField port = new JTextField();

    private JButton connexionButton = new JButton("Connexion");

    public ConnectionWindow(){

        this.setTitle("Connection au bus");
        this.setSize(300, 300);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        informationPanel.setSize(200, 200);
        informationPanel.add(adressLabel);
        informationPanel.add(adress);
        //informationPanel.add(portLabel);
        //informationPanel.add(port);

        this.add(informationPanel);

        /*
        this.getContentPane().add(adressLabel);
        this.getContentPane().add(adress);
        this.getContentPane().add(portLabel);
        this.getContentPane().add(port);
        */

        this.setVisible(true);

    }

}
