package gui.ClienteGui;

import Cliente.Cliente;

public class ClienteGui {
    public static void main(String[] args){
        Cliente cliente=new Cliente("localhost",9999);
        cliente.enviarMensaje("GET FLUJO");
        cliente.enviarMensaje("ADD ACT");
        cliente.enviarMensaje("ADD TAS");
        cliente.enviarMensaje("ADD FAS");
    }
}
