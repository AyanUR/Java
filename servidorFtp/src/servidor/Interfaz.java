package servidor;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
public class Interfaz extends JFrame implements ActionListener{
    private JButton conect;
    private JTextArea area;
    private JScrollPane scroll;
    private JProgressBar progreso;
    private JTextField port,route;
    private GridBagConstraints gbc;
    public Interfaz(String title){
        super(title);
        setSize(300,400);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gbc=new GridBagConstraints();
        scroll=new JScrollPane(area=new JTextArea());
        (conect=new JButton("conectarme")).addActionListener(this);
    }
    public void configGbc(int row,int colum,int numrow,int numcolum,float evolutionrow,float evolutioncolum){
        gbc.gridx=colum;
        gbc.gridy=row;
        gbc.gridheight=numrow;
        gbc.gridwidth=numcolum;
        gbc.weightx=evolutioncolum;
        gbc.weighty=evolutionrow;
        gbc.fill=GridBagConstraints.BOTH;
    }
    public void servidor(){
        configGbc(0,0,1,1,1,1);//fila,columna,numfile,numcoluman,evolutionfila,evolutioncolum
        add(new JLabel("ingrese el puerto "),gbc);
        configGbc(0,1,1,1,1,1);
        add(port=new JTextField(11),gbc);

        configGbc(1,0,1,1,1,1);
        add(new JLabel("ingrese la ruta "),gbc);
        configGbc(1,1,1,1,1,1);
        add(route=new JTextField(11),gbc);

        configGbc(2,0,1,2,1,1);
        this.add(scroll, gbc);
        configGbc(3,0,1,2,1,1);
        this.add(conect, gbc);
     
        configGbc(4,0,1,2,1,1);
        add(progreso=new JProgressBar(0,100),gbc);
        setVisible(true);
    }
    public void addMsntoArea(String msn){
        area.append(msn);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==conect){
            recivFiles servidor=new recivFiles(Integer.parseInt(port.getText()),route.getText());
            servidor.start();
        }
    }
}
