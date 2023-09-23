package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.Server;

import java.io.IOException;


public class StompServer {
    

    // public static void threadPerClient(int port) {
    //     Server.threadPerClient(
    //     port, 
    //     () -> new StompProtocol<Message>(), 
    //     StompEncoderDecoder::new 
    // ).serve();
      
    // }


    public static void reactor(int port) {
        Server.reactor(
            1, 
            port, 
            () -> new StompProtocol<Message>() , 
            StompEncoderDecoder::new
            ).serve();
    }

    public static void main(String[] args) {
        int port = 7777;
        reactor(port);
        // if (args.length > 1 && args[1].equals("reactor")) {
        //     reactor(port);
        // } else {
        //     threadPerClient(port);
        // }
    }
}