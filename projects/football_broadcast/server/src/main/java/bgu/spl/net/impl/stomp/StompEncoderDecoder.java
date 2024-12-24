package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncoderDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;


public class StompEncoderDecoder implements MessageEncoderDecoder<Message> {
    
    private byte[] bytes = new byte[1 << 10]; //start with 1024.
    private int len = 0;
    //private int start = 0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == '\u0000') {
            String s = popString();
            return stringToMessage(s);
        }


        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(Message message ) {
        StompCommand cmd = message.command();
        Map<String,String> headers = message.headers();
        String body = message.body();

        StringBuilder messageStr = new StringBuilder(cmd.toString());
        messageStr.append( "\n" );

        if (headers != null) {
            for (Iterator keys = headers.keySet().iterator(); keys.hasNext(); ) {
                String key = (String)keys.next();
                String value = (String)headers.get(key);
                messageStr.append( key );
                messageStr.append( ":" );
                messageStr.append( value );
                messageStr.append( "\n" );
            }
        }
        messageStr.append( "\n" );

        if (body != null) messageStr.append( body );

        messageStr.append( "\0" );
        return (messageStr.toString()).getBytes();

        //return (messageStr.toString()+"\u0000").getBytes();
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private String popString() {
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private Message stringToMessage(String popString){
        // Get the index of the first and last new line characters in the string
        int lastIndexOfCommand = popString.indexOf("\n");
        int firstIndexOfBody = popString.indexOf("\n\n");

        // Get the command from the string
        StompCommand command = StompCommand.valueOf(popString.substring(0, lastIndexOfCommand));

        // Get the body from the string
        String body = popString.substring(firstIndexOfBody + 2);

        // Get the string that represents the headers
        String headersString = popString.substring(lastIndexOfCommand + 1, firstIndexOfBody);

        // Create a map to store the headers
        Map<String, String> headersMap = new HashMap<>();

        // Split the headers string into an array of individual headers
        String[] headersArray = headersString.split("\n");

        // Iterate over the headers array, and add each header to the map
        for (String header : headersArray) {
            int indexOfColon = header.indexOf(":");
            headersMap.put(header.substring(0, indexOfColon), header.substring(indexOfColon + 1));
        }

        // If the body is "body is null", set the body to null
        if (body.equals("body is null")) {
            body = null;
        }

        // Return a new Message object with the extracted command, headers, and body
        return new Message(command, headersMap, body);
    }
}