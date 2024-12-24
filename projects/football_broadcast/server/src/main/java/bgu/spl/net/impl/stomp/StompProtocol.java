package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.stomp.ConnectionsImpl;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class StompProtocol<T> implements StompMessagingProtocol<Message>{
    
    private boolean shouldTerminate;
    private int connectionId;
    private ConnectionsImpl connections;
    

    public StompProtocol(){
        shouldTerminate = false;
    }
    /**
	 * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
	**/
    public void start(int connectionId, Connections<Message> connections) {
        this.connectionId = connectionId;
        this.connections = (ConnectionsImpl<Message>)(connections);
    }
    @Override
    public Message process(Message message) {
        StompCommand c = message.command();
        String commandType = c.toString().toUpperCase().trim();
        
        switch (commandType) {
            case "CONNECT":
                return connect(message);
            case "DISCONNECT":
                return disconnect(message);
            case "SUBSCRIBE":
                return subscribe(message);
            case "UNSUBSCRIBE":
                return unsubscribe(message);
            case "SEND":
                return send(message);
            default:
                return illegalCommandError(message);
        }
    }

    private static Message connectedMessage() {
        Map<String, String> headers = new HashMap<>();
        headers.put("version", "1.2");
        return new Message(StompCommand.CONNECTED, headers, null);
    }

    private static Message receipt(Map<String, String> headers) {
        return new Message(StompCommand.RECEIPT, headers, null);
    }

    private static Message error(Map<String, String> headers, String body) {
        return new Message(StompCommand.ERROR, headers, body);
    }

    private Message illegalCommandError(Message message) {
        Map<String, String> headers = new HashMap<>();
        String msgID = message.headers().get("message-id");
        headers.put("receipt-id", "message" + msgID);
        headers.put("message", "Illegal command - the command does not exist");
        String body = "The message:\n-----\n" + message.toString() + "\n-----\n";
        connections.disconnect(connectionId);
        return error(headers, body);
    }

    private Message disconnect(Message message) {
        Map<String, String> headers = message.headers();
        String receiptID = headers.get("receipt");
        headers.clear();
        headers.put("receipt id", receiptID);
        connections.disconnect(connectionId);
        return receipt(headers);
    }

    private Message send(Message message) {
        Map<String, String> headers = message.headers();
        String channel = headers.get("destination");
        if(channel == null){
            String body = "The message:\n-----\n"+message.toString()+"\n-----\n Did not contain a destination header,\n which is REQUIRED for message propagation.";
            headers.clear();
            headers.put("message","malformed frame received");
            connections.disconnect(connectionId);
            return error(headers,body);
        }
        else{
            //if the user is subscribed to the channel
            if(connections.isSubscribed(channel,connectionId)){
                //add the subscription id header to the message
                headers.put("subscription", String.valueOf(connections.getSubId(channel, connectionId)));
                //add the message id header to the message
                headers.put("message-id", connections.getMessageId());
                //remove the receipt header if exists
                String receiptID = headers.get("receipt");
                headers.remove("receipt");

                //send the message to all subscribers of the channel
                connections.send(channel,new Message(StompCommand.MESSAGE, headers, message.body()));

                headers.clear();
                headers.put("receipt id" , receiptID);
                return receipt(headers);
            }
            else{
                //if the user is not subscribed to the channel
                headers.clear();
                headers.put("message","user not subscribed to channel");
                String body = "The message:\n-----\n"+message.toString()+"\n-----\n";
                connections.disconnect(connectionId);
                return error(headers,body);
            }
        }
    }

    private Message unsubscribe(Message message) {
        // Get the headers of the message
        Map<String, String> headers = message.headers();
        // Get the id header
        String subId = headers.get("id");

        // If the header is not null
        if (subId != null) {
            int subscriptionId = Integer.valueOf(subId);
            // Try to unsubscribe from the channel
            boolean success = connections.unSubscribeToChannel(connectionId, subscriptionId);
            // If the process was successful
            if (success) {
                // Get the receipt header
                String receiptId = headers.get("receipt");
                // Clear the headers
                headers.clear();
                // Put the receipt id header
                headers.put("receipt id", receiptId);
                // Return the receipt
                return receipt(headers);
            } else {
                // Return an error
                connections.disconnect(connectionId);
                return error(headers, null);
            }
        }
        // If the header is null, return an error
        connections.disconnect(connectionId);
        return error(headers, null);
    }



    private Message subscribe(Message message) {
        // Get the headers of the message
        Map<String, String> headers = message.headers();
        // Get the destination header
        String channel = headers.get("destination");
        // Get the id header
        String subId = headers.get("id");

        // If the destination header or the id header are missing
        if (channel == null || subId == null) {
            String body = "The message:\n-----\n" + message.toString() + "\n-----\n Did not contain a destination header or subscription id,\n which is REQUIRED for message propagation.";
            // Clear the headers
            headers.clear();
            // Put the error message header
            headers.put("message", "malformed frame received ");
            // Return an error
            connections.disconnect(connectionId);
            return error(headers, body);
        } else {
            int subscriptionId = Integer.valueOf(subId);
            // Get the receipt header
            String receiptId = headers.get("receipt");
            // Clear the headers
            headers.clear();
            // Put the receipt id header
            headers.put("receipt id", receiptId);

            // If the user is not already subscribed to the channel
            if (!connections.isSubscribed(channel, connectionId)) {
                // Subscribe to the channel
                connections.subscribeToChannel(channel, connectionId, subscriptionId);
                // Return the receipt
                return receipt(headers);
            } else {
                // Return an error
                connections.disconnect(connectionId);
                return error(headers, null);
            }
        }
    }

    private Message connect(Message message) {
        Map<String, String> headers = message.headers();
        String userName = headers.get("login");
        String password = headers.get("passcode");
        User user = connections.getUserByName(userName);

        // if the user does not exist
        if (user == null) {
            connections.addToRegisteredUsers(userName, password, connectionId);
            user = connections.getUserByName(userName);
            connections.logInUser(user, connectionId);
            return createConnectedMessage();
        }
        // if the user already exists
        else {
            // check if the user is already logged in
            if (connections.isUserLoggedIn(user)) {
                String body = "The message:\n-----\n" + message.toString() + "\n-----\nYou must disconnect before reconnecting.";
                String receipt = headers.get("receipt");
                headers.clear();
                if (receipt != null) {
                    headers.put("receipt id", receipt);
                }
                headers.put("message", "The user is already connected.");
                return createErrorMessage(headers, body);
            }
            // if the user is not already logged in
            else {
                // check if the provided password is correct
                if (!connections.checkUserPassword(userName, password)) {
                    String body = "The message:\n-----\n" + message.toString() + "\n-----\nPlease try a different password.";
                    headers.clear();
                    headers.put("message", "Incorrect password.");
                    return createErrorMessage(headers, body);
                }
                // if the password is correct
                else {
                    connections.logInUser(user, connectionId);
                    return createConnectedMessage();
                }
            }
        }
    }

    private Message createConnectedMessage() {
        Map<String, String> h = new HashMap<>();
        h.put("version","1.2");
        return new Message(StompCommand.CONNECTED,h,null);
    }

    private Message createErrorMessage(Map<String, String> headers, String body) {

        connections.disconnect(connectionId);
        headers.put("message-type", "ERROR");
        return new Message(StompCommand.ERROR, headers, body);
    }

    @Override
    public boolean shouldTerminate() {
        
        return shouldTerminate;
    }

    @Override
    public void start(int connectionId, ConnectionsImpl<Message> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }

}   