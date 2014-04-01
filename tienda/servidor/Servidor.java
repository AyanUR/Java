import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
public class Servidor extends JFrame{
	private static JTextArea area;
	private static JScrollPane scroll;
	private static ServerSocket servidor;
	private static Socket cliente;
	private static File []files;
	private static Tienda []productos;
	private static int np=0;
	private static ObjectOutputStream oos;
	public Servidor(String title,int row,int column){
		super(title);
		setLayout(new GridLayout(row,column));
		setSize(400,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void selectFiles(){
		JFileChooser selector=new JFileChooser();
		selector.setMultiSelectionEnabled(true);
		if(selector.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
			files=selector.getSelectedFiles();
	}
	public static void sendFile(File file){
		byte []bufer=new byte[(int)file.length()];
		int leidos=0,completado=0;
		try{
			BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos=new BufferedOutputStream(cliente.getOutputStream());
			oos.writeObject(file.getName());        oos.flush();
			area.append("\nse envio el nombre del archivo "+file.getName()+" correctamente");
			oos.writeInt((int)file.length());	oos.flush();
			area.append("\nse envio el tamano del archivo "+file.length()+" correctamente");
			bis.read(bufer);//guardo el archivo completo en el bufer
			while((leidos++)<bufer.length){//envio byte por byte
				bos.write(bufer[leidos-1]);	bos.flush();
			}
			area.append("\nse envio correctamente el archivo");
			bis.close();
		}catch(Exception e){area.append("\nerror al enviar "+file.getName()+" "+e.getMessage());}
	}
	public static void ingresarProductos(){
		int i,cantidad,precio;
		String name,descripcion,imagen;
		np=Integer.parseInt(JOptionPane.showInputDialog(null,"cuantos productos ingresara"));
		productos=new Tienda[np+11];
		for(i=0;i<np;i++){
			JOptionPane.showMessageDialog(null,"producto #"+(i+1));
			name=JOptionPane.showInputDialog(null,"nombre del producto");
			cantidad=Integer.parseInt(JOptionPane.showInputDialog(null,"cantidad"));
			precio=Integer.parseInt(JOptionPane.showInputDialog(null,"precio"));
			descripcion=JOptionPane.showInputDialog(null,"descripcion");
			imagen=JOptionPane.showInputDialog(null,"nombre de la imagen");
			productos[i]=new Tienda(name,descripcion,imagen,cantidad,precio);
		}
	}
	public static void main(String []args){
		int port,i;
		Servidor mensajes;
		
		port=Integer.parseInt(JOptionPane.showInputDialog(null,"puerto a iniciar la coneccion"));
		mensajes=new Servidor("area de mesajes servidor",1,1);
		area=new JTextArea();
		scroll=new JScrollPane(area);
		mensajes.add(scroll);
		mensajes.setVisible(true);
		selectFiles();
		ingresarProductos();
		/*for(i=0;i<np;i++){
			System.out.print("\nname = "+productos[i].name);
			System.out.print("\ndescripcion = "+productos[i].descripcion);
			System.out.print("\nprecio = "+productos[i].precio);
			System.out.print("\nimagen = "+productos[i].imagen);
			System.out.print("\ncandiad = "+productos[i].cantidad+"\n\n");
		}*/
		try{
			servidor=new ServerSocket(port);
			while(true){
				area.append("\nesperando cliente...");
				cliente=servidor.accept();
	area.append("\ncliente conectado desde "+cliente.getInetAddress()+" con el puerto"+cliente.getPort());
				oos=new ObjectOutputStream(cliente.getOutputStream());
				oos.writeInt(files.length);	oos.flush();
				for(i=0;i<files.length;i++)
					sendFile(files[i]);
				oos.writeInt(np);	oos.flush();
				oos.writeObject(productos);	oos.flush();	
			}
		}catch(Exception e){area.append("\nerrro al iniciar el servidor "+e.getMessage());}
	}
}
