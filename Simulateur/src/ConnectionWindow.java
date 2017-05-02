import javax.swing.*;
import java.awt.*;

public class ConnectionWindow extends JFrame{

    /* Attributs de la fenêtre
    **************************/
    private JLabel adressLabel = new JLabel("Adresse :");
    private JLabel portLabel = new JLabel("Port :");

    private JTextField adress = new JTextField();
    private JTextField port = new JTextField();

    public ConnectionWindow(){

        this.setTitle("Connection au bus");
        this.setSize(300, 300);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.setLayout(new GridLayout(2,2));

        this.getContentPane().add(adressLabel);
        this.getContentPane().add(adress);
        this.getContentPane().add(portLabel);
        this.getContentPane().add(port);

        this.setVisible(true);

    }

}
