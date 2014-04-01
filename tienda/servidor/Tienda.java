import java.io.*;
import javax.swing.*;
public class Tienda extends JButton implements Serializable{
	private static final long serialVersionUID=1L;
	public String name,descripcion,imagen;
	public int cantidad,precio;
	public Tienda(String name,String descripcion,String imagen,int cantidad,int precio){
		super("agregar al carro");
		this.name=name;
		this.cantidad=cantidad;
		this.descripcion=descripcion;
		this.imagen=imagen;
		this.precio=precio;
	}
}
