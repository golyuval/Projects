package bgu.spl.net.api;

import bgu.spl.net.impl.stomp.ConnectionsImpl;

public interface StompMessagingProtocol<T> extends MessagingProtocol<T>  {
	/**
	 * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
	**/
    void start(int connectionId, ConnectionsImpl<T> connections);


    @Override
    T process(T message);
	
	/**
     * @return true if the connection should be terminated
     */
    @Override
    boolean shouldTerminate();
}