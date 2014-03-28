package servidor;
import java.net.*;
import java.io.*;
import java.util.*;
public class recivFiles extends Thread{
    static Interfaz interfaz;
    private ServerSocket servidor;
    private ObjectInputStream ois;
    private LinkedList info;
    private BufferedInputStream bis;
    private Socket cliente;
    int port;
    String route;
    public recivFiles(int port,String route){
        this.port=port;
        this.route=route;
    }
    public void atiende(){
        int numfiles,i,leidos=0;
        byte []bufer;
        BufferedOutputStream bos;
        try{
            info=(LinkedList)ois.readObject();
            numfiles=info.size()/2;
            interfaz.addMsntoArea("\nnumfiles "+numfiles);
            //for(i=0;i<info.size();i+=2)
              //  interfaz.addMsntoArea("\narchivo "+info.get(i)+"\t"+info.get(i+1)+" bytes");
            for(i=0;i<numfiles*2;i+=2,leidos=0){//reciviendo archivo
                bos=new BufferedOutputStream(new FileOutputStream(route+info.get(i)));
                interfaz.addMsntoArea("\n"+route+info.get(i));
                bufer=new byte[(int)info.get(i+1)];
                interfaz.addMsntoArea("\n"+(int)info.get(i+1));
                while((leidos++)<bufer.length)
                    bufer[leidos-1]=(byte)bis.read();
                bos.write(bufer);//escrivimos en el flujo de escritura todo el archivo 
                bos.flush();
                bos.close();//cerramos el flujo de escritura
            }
        }catch(Exception e){interfaz.addMsntoArea("\nerror al atender al cliente "+e.getMessage());}
    }
    public void run(){
        int i;
        try{
            servidor=new ServerSocket(port);
            interfaz.addMsntoArea("\nservidor iniciado esperando cliente ...");
            while(true){
                cliente=servidor.accept();
                interfaz.addMsntoArea("\ncliente conectado desde "+cliente.getInetAddress()+" atraves del puerto "+cliente.getPort());
                ois=new ObjectInputStream(cliente.getInputStream());
                bis=new BufferedInputStream(cliente.getInputStream());
                atiende();
                ois.close();
            }    
        }catch(Exception e){interfaz.addMsntoArea("\nerro al conectar el servidor "+e.getMessage());}
    }
    public static void main(String []args){
        interfaz=new Interfaz("servidor ftp");
        interfaz.servidor();
    }
}
