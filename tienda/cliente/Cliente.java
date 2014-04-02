import java.net.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
public class Cliente extends JFrame implements ActionListener{
	private static JTextArea area;
	private static JScrollPane scroll;
	private static Socket cliente;
	private static Tienda productos[];
	private static int np;
	private static ObjectInputStream ois;
	private static GridBagConstraints gbc;
	public Cliente(String title){
		super(title);
		setLayout(new GridBagLayout());
		setSize(400,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gbc=new GridBagConstraints();
	}
	public void actionPerformed(ActionEvent e){
		;
	}
	public static void configGbc(int row,int column,int numrow,int numcolumn,float py,float px){
		gbc.gridy=row;
		gbc.gridx=column;
		gbc.gridheight=numrow;
		gbc.gridwidth=numcolumn;
		gbc.weighty=py;
		gbc.weightx=px;
		gbc.fill=GridBagConstraints.BOTH;
	}
	public static void buildTienda(){
		int i;
		Cliente tienda=new Cliente("tienda de ayan");
		JPanel info=new JPanel();
		JButton comprar=new JButton("comprar");
		Icon img;
		JLabel aux=new JLabel();
		for(i=0;i<np;i++){
			configGbc(i,0,1,1,1,1);
			img=new ImageIcon(new ImageIcon(productos[i].imagen).getImage().getScaledInstance(11,11,Image.SCALE_DEFAULT));
			aux.setIcon(img);
			aux.repaint();
			tienda.add(aux,gbc);
			//tienda.add(new JLabel(new ImageIcon(productos[i].imagen)),gbc);
			configGbc(i,1,1,1,1,1);
			info.add(new JLabel("producto: "+productos[i].name));
			info.add(new JLabel("precio: "+productos[i].precio));
			info.add(new JLabel("cantidad: "+productos[i].cantidad));
			info.add(new JLabel("descripcion: "+productos[i].descripcion));
			tienda.add(info,gbc);
			configGbc(i,2,1,1,1,1);
			productos[i].addActionListener(tienda);
			tienda.add(productos[i],gbc);
			configGbc(i,3,1,1,1,1);
			productos[i].setText("quitar del carrito");
			tienda.add(productos[i],gbc);
		}
		comprar.addActionListener(tienda);
		configGbc(i,0,1,4,1,1);
		tienda.add(comprar,gbc);
		tienda.setVisible(true);


/*		JTextArea info;
		JScrollPane sc;
		JButton comprar=new JButton("comprar");
		for(i=0;i<np;i++){
			info=new JTextArea();
			sc=new JScrollPane(info);
			tienda.add(new JLabel(new ImageIcon(productos[i].imagen)));
			info.append("producto: "+productos[i].name+"\nprecio: "+productos[i].precio+"\ncantidad: "+productos[i].cantidad+"\ndescripcion: "+productos[i].descripcion);
			tienda.add(sc);
			productos[i].addActionListener(tienda);
			tienda.add(productos[i]);
			productos[i].setText("quitar del carrito");
			tienda.add(productos[i]);
		}
		comprar.addActionListener(tienda);
		tienda.add(comprar);
		tienda.setVisible(true);*/
		/*for(i=0;i<np;i++){
			System.out.print("\nname = "+productos[i].name);
			System.out.print("\ndescripcion = "+productos[i].descripcion);
			System.out.print("\nprecio = "+productos[i].precio);
			System.out.print("\nimagen = "+productos[i].imagen);
			System.out.print("\ncandiad = "+productos[i].cantidad+"\n\n");
		}*/
	}
	public static void reciveFile(){
		String namefile="";
		int leidos=0,tamanofile;
		try{
			BufferedInputStream bis=new BufferedInputStream(cliente.getInputStream());
			namefile=(String)ois.readObject();
			tamanofile=ois.readInt();
			area.append("\nreciviendo "+namefile+"\ttamano "+tamanofile);
			BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(namefile));
			byte []bufer=new byte[tamanofile];
			while((leidos++)<bufer.length){
				bufer[leidos-1]=(byte)bis.read();
			}
			bos.write(bufer);	bos.flush();
			area.append("\nrecivido :D");
			bos.close();
		}catch(Exception e){area.append("\nerror al recivir archivos "+namefile+" "+e.getMessage());}
	}
	public static void main(String []args){
		int port,nf,i;
		String ip;
		Cliente mensajes;
		Object aux;
		
		ip=JOptionPane.showInputDialog(null,"ingrese la ip de servidor");
		port=Integer.parseInt(JOptionPane.showInputDialog(null,"puerto a iniciar la conexion"));
		mensajes=new Cliente("area de mensajes cliente");
		area=new JTextArea();
		scroll=new JScrollPane(area);
		configGbc(0,0,1,1,1,1);
		mensajes.add(scroll,gbc);
		mensajes.setVisible(true);
		try{
			cliente=new Socket(InetAddress.getByName(ip),port);
			area.append("\ncliente conectado desde "+ip+" con el puerto "+port);
			ois=new ObjectInputStream(cliente.getInputStream());
			nf=ois.readInt();
			System.out.print("\nnumero de imagenes a recivir "+nf);
			for(i=0;i<nf;i++)
				reciveFile();
			np=ois.readInt();
			if((aux=ois.readObject()) instanceof Tienda[]){
				if((productos=(Tienda[])aux)!=null){
					buildTienda();		
				}
			}
			else
				area.append("\nvuelve a intentarlo pero esta ves enviame Tienda porfavor!!!");

		}catch(Exception e){area.append("\nerror al iniciar el cliente "+e.getMessage());}
	}
}
