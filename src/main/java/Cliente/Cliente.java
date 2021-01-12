package Cliente;

import kanvan.FlujoTrabajo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
    private FlujoTrabajo flujoDeTrabajo;
    private final String HOST;
    private final int PUERTO;

    public Cliente(String HOST, int PUERTO) {
        this.HOST = HOST;
        this.PUERTO = PUERTO;
        this.flujoDeTrabajo = new FlujoTrabajo("Default");
    }

    public FlujoTrabajo getFlujoDeTrabajo() {
        try {
            Socket socket = new Socket(HOST, PUERTO);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject("GET FLUJO");

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.flujoDeTrabajo = (FlujoTrabajo) objectInputStream.readObject();
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flujoDeTrabajo;
    }

    /*public void setFlujoDeTrabajo(FlujoTrabajo flujoDeTrabajo) {
        this.flujoDeTrabajo = flujoDeTrabajo;
    }*/

    public void enviarMensaje(String mensaje) {
        try {
            Socket socket = new Socket(HOST, PUERTO);
            System.out.println("Cliente conectado");

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(mensaje);
            System.out.println("El cliente envio el siguiente mensaje: " + mensaje);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object object = objectInputStream.readObject();

            if (object.getClass().getName().equalsIgnoreCase("kanvan.FlujoTrabajo")) {
                this.flujoDeTrabajo = (FlujoTrabajo) object;
                System.out.println("El servidor respondio el siguiente flujo de trabajo: " + this.flujoDeTrabajo);
            } else {
                System.out.println("El servidor respondio el siguiente objeto: " + object);
            }
            //Cerrarmos objetos.
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
            System.out.println("Cliente desconectado");


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
