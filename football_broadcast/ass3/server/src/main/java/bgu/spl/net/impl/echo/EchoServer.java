package bgu.spl.net.impl.echo;



public class EchoServer {

    public static void main(String[] args) {

        // // you can use any server... 
        // Server.threadPerClient(
        //         7777, //port
        //         () -> new StompMessagingProtocol(), //protocol factory
        //         MessageEncoderDecoder::new //message encoder decoder factory
        // ).serve();

        // Server.reactor(
        //         Runtime.getRuntime().availableProcessors(),
        //         7777, //port
        //         () -> new EchoProtocol<>(), //protocol factory
        //         LineMessageEncoderDecoder::new //message encoder decoder factory
        // ).serve();
    }
}
