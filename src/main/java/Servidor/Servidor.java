package Servidor;

import com.sun.security.ntlm.Server;
import kanvan.Actividad;
import kanvan.Fase;
import kanvan.FlujoTrabajo;
import kanvan.Tarea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private FlujoTrabajo flujoTrabajo;
    private ServerSocket serverSocket;
    private Socket socket;
    private int PUERTO;

    public Servidor(int PUERTO){
        flujoTrabajo=new FlujoTrabajo("Mi flujo de trabajo");
        this.PUERTO=PUERTO;
        iniciar();
    }

    public void iniciar(){
        try {
            serverSocket=new ServerSocket(PUERTO);
            System.out.println("Servidor conectado en el puerto " + this.PUERTO);
            while(true){
                System.out.println("Esperando un nuevo cliente");
                socket=serverSocket.accept();
                System.out.println("Cliente conectado");

                ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
                String mensaje= (String)objectInputStream.readObject();
                System.out.println("El cliente ha enviado "+mensaje);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                if(mensaje.contains("GET FLUJO")){
                    objectOutputStream.writeObject(flujoTrabajo);
                    System.out.println("El servidor respondio el nuevo objeto "+flujoTrabajo);
                }
                else if(mensaje.contains("ADD ACT")){
                    flujoTrabajo.getActividad().add(new Actividad(mensaje.substring(8),flujoTrabajo));
                    String respuesta="Se agrego la actividad: "+mensaje.substring(8);
                    objectOutputStream.writeObject(respuesta);
                }
                else if(mensaje.contains("ADD FAS")){
                    flujoTrabajo.getFase().add(new Fase(mensaje.substring(8),flujoTrabajo));
                    String respuesta="Se agrego la fase: "+mensaje.substring(8);
                    objectOutputStream.writeObject(respuesta);
                }
                else if(mensaje.contains("ADD TAS")){
                    Actividad actividad = flujoTrabajo.getActividad().get(Integer.parseInt(mensaje.substring(8,9)));
                    Fase fase = flujoTrabajo.getFase().get(Integer.parseInt(mensaje.substring(9,10)));
                    Tarea tarea=new Tarea(mensaje.substring(10),flujoTrabajo,actividad,fase);
                    flujoTrabajo.getTarea().add(tarea);
                    actividad.getTarea().add(tarea);
                    fase.getTarea().add(tarea);
                    String respuesta="Se agrego la tarea: "+mensaje.substring(10);
                    objectOutputStream.writeObject(respuesta);
                }
                else {
                    String respuesta="I DONT KNOW";
                    objectOutputStream.writeObject(respuesta);
                    System.out.println("El servidor respondio el nuevo objeto "+respuesta);
                }
                objectOutputStream.close();
                socket.close();
                System.out.println("Cliente desconectado");

            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
