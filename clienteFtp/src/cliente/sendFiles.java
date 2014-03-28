package cliente;
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import javax.swing.JFileChooser;
public class sendFiles {
    private Socket cliente;
    private ObjectOutputStream oos;
    private BufferedOutputStream bos;
    private File []files;
    private LinkedList info;
    static Interfaz interfaz;
    public void selectFile(){
        JFileChooser selector=new JFileChooser();
        selector.setMultiSelectionEnabled(true);
        if(selector.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            files=selector.getSelectedFiles();
        }
    }    
    public void sendFile(File file){
        byte []bufer=new byte[(int)file.length()];
        int leidos=0;
        interfaz.addMsntoArea("\nenviado "+file.getName());
        try{
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
            bis.read(bufer);
            while((leidos++)<bufer.length){
                bos.write(bufer[leidos-1]);
                bos.flush();
            }
        interfaz.addMsntoArea("\narchivo "+file.getName()+" enviado");
        }catch(Exception e){interfaz.addMsntoArea(e.getMessage());}
    }
    public void start(int port,String ip){
        selectFile();
        info=new LinkedList();
        int i;
        for(i=0;i<files.length;i++){
            info.add(files[i].getName());
            info.add((int)files[i].length());
        }
        try{
            cliente=new Socket(InetAddress.getByName(ip),port);
            cliente.setSoLinger(true,10);
            interfaz.addMsntoArea("\ncliente conectado desde la "+ip+"atraves del puerto "+port);
            oos=new ObjectOutputStream(cliente.getOutputStream());
            oos.writeObject(info);//enviamos nombre y tamaño de archivos
            oos.flush();
            interfaz.addMsntoArea("\nse envio correctamente la informacion de los archivos al servidor");
            bos=new BufferedOutputStream(cliente.getOutputStream());
            for(i=0;i<files.length;i++)
                sendFile(files[i]);
            oos.close();
        }catch(Exception e){interfaz.addMsntoArea("\nerror al crear socket "+e.getMessage());}
    }
    public static void main(String []args){
        interfaz=new Interfaz("cliete ftp");
        interfaz.cliente();
    }
}
