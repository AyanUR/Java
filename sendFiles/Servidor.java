import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
public class Servidor extends JFrame{
	private static JTextArea area;
	private static JScrollPane scroll;
	private static ServerSocket servidor;
	private static Socket cliente;
	private static ObjectInputStream ois;
	private static int i;
	public Servidor(String title){
		super(title);
		setLayout(new GridLayout(1,1));
		setSize(300,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		area=new JTextArea();
		scroll=new JScrollPane(area);
		this.add(scroll);
		setVisible(true);
	}
	public static void reciveFile(String route){
		String namefile="";
		int leidos=0,tamanofile;
		try{
			BufferedInputStream bis=new BufferedInputStream(cliente.getInputStream());
			namefile=(String)ois.readObject();
			tamanofile=ois.readInt();
			area.append("\nreciviendo "+namefile+"\ttamano = "+tamanofile);
			BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(route+namefile));
			byte []bufer=new byte[tamanofile];
			while((leidos++)<bufer.length){
				bufer[leidos-1]=(byte)bis.read();
			}
			bos.write(bufer);	bos.flush();
			area.append("\nrecivido...");
			bos.close();
		}catch(Exception e){area.append("\nerror al recivir el archivo "+namefile+" "+e.getMessage());}
	}
	public static void main(String []args){
		int port,numfiles;
		String route;
		Servidor mensajes;
		
		route=JOptionPane.showInputDialog(null,"ingrese la ruta donde guardar los archivos");
		port=Integer.parseInt(JOptionPane.showInputDialog(null,"puerto a iniciar la conexion"));
		mensajes=new Servidor("area de mensajes");
		try{
			servidor=new ServerSocket(port);
			while(true){
				area.append("\nesperando cliente...");
				cliente=servidor.accept();
area.append("\ncliente conectado desde "+cliente.getInetAddress()+" con el puerto "+cliente.getPort());
				ois=new ObjectInputStream(cliente.getInputStream());
				numfiles=ois.readInt();
				System.out.print("\nnumfiles = "+numfiles);
				for(i=0;i<numfiles;i++)
					reciveFile(route);
			}
		}catch(Exception e){area.append("\nerror al iniciar el servidor "+e.getMessage());}
	}
}
