package bgu.spl.net.impl.stomp;

public class StompCommand {
    public static final String ENCODING = "UTF-8";
    private String command;

    private StompCommand(String msg) {
        this.command = msg;
    }

    public static final StompCommand SEND = new StompCommand("SEND");
    public static final StompCommand SUBSCRIBE = new StompCommand("SUBSCRIBE");
    public static final StompCommand UNSUBSCRIBE = new StompCommand("UNSUBSCRIBE");
    public static final StompCommand DISCONNECT = new StompCommand("DISCONNECT");
    public static final StompCommand CONNECT = new StompCommand("CONNECT");
    public static final StompCommand MESSAGE = new StompCommand("MESSAGE");
    public static final StompCommand RECEIPT = new StompCommand("RECEIPT");
    public static final StompCommand CONNECTED = new StompCommand("CONNECTED");
    public static final StompCommand ERROR = new StompCommand("ERROR");


    public static StompCommand valueOf(String v) {
        switch (v.trim().toUpperCase()) {
            case "SEND":
                return SEND;
            case "SUBSCRIBE":
                return SUBSCRIBE;
            case "UNSUBSCRIBE":
                return UNSUBSCRIBE;
            case "CONNECT":
                return CONNECT;
            case "MESSAGE":
                return MESSAGE;
            case "RECEIPT":
                return RECEIPT;
            case "CONNECTED":
                return CONNECTED;
            case "DISCONNECT":
                return DISCONNECT;
            case "ERROR":
                return ERROR;
            default:
                throw new IllegalArgumentException("Unrecognized command: " + v);
        }
    }

    public String toString() {
        return this.command;
    }
}