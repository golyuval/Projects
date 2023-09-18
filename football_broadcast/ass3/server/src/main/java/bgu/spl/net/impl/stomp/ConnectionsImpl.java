package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

//import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ConnectionsImpl<T> implements Connections<T> {
    private static ConnectionsImpl connections;
    private ConcurrentHashMap<Integer, ConnectionHandler<T>> connectionHandlerById;
    private ConcurrentHashMap<String, User> registeredUsers;
    private ConcurrentHashMap<Integer,User> userByClient;

    private ConcurrentHashMap<String, ConcurrentHashMap<Integer,Integer>> connectionIdByChannel;
    private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,String>> channelByConnectionIdAndSubId;
    private int currentMessageId = 0;

    private ConnectionsImpl(){
        connectionHandlerById = new ConcurrentHashMap<>();
        registeredUsers = new ConcurrentHashMap<>();
        userByClient = new ConcurrentHashMap<>();
        connectionIdByChannel = new ConcurrentHashMap<>();
        channelByConnectionIdAndSubId = new ConcurrentHashMap<>();
    }
    
    public static synchronized ConnectionsImpl<?> getInstance(){
        if (connections == null)
            connections = new ConnectionsImpl<>();
        return connections;
    }
    
    public boolean send(int connectionId, T msg){
        System.out.println(msg.toString());
        ConnectionHandler<T> connectionHandler = connectionHandlerById.get(connectionId);
        if(connectionHandler == null)
            return false;
        connectionHandler.send(msg);
        return true;
    }

    public void send(String channel, T msg){
        ConcurrentHashMap<Integer,Integer> channelMap = connectionIdByChannel.get(channel);
        if(channelMap != null){
            for (Integer key : channelMap.keySet()){
                if(userByClient.get(key).isLoggedIn()){
                    System.out.println("key: " + String.valueOf(key));
                    System.out.println(userByClient.get(key).isLoggedIn());
                    System.out.println(connectionHandlerById.get(key));
                    connectionHandlerById.get(key).send(msg);
                }
            }
        }
    
    }

    public synchronized void disconnect(int connectionId){
        User user = userByClient.get(connectionId);
        user.setStatus(false);
        user.setConnId(-1);
        userByClient.remove(connectionId);
        connectionHandlerById.remove(connectionId);
        channelByConnectionIdAndSubId.remove(connectionId);
        for(String key : connectionIdByChannel.keySet() ){
            connectionIdByChannel.get(key).remove(connectionId);
        }
    }

    public synchronized void addToRegisteredUsers( String userName, String password, Integer connId){
        User user = new User(userName, password, connId);
        registeredUsers.put(userName, user);
        userByClient.put(connId,user);
    }

    public synchronized void logInUser(User user, int connId){
        user.logIn(connId);
        userByClient.put(connId,user);
    }

    public synchronized boolean checkUserPassword (String username, String password){
        return (registeredUsers.get(username)).getPassword().equals(password);
    }

    public synchronized boolean isUserLoggedIn (User user){
        if (user == null)
            return false;
        return user.isLoggedIn();
    }

    public synchronized User getUserByName (String username){
        return registeredUsers.get(username);

    }

    public synchronized void addConnectionHandler(Integer connId, ConnectionHandler CH){
        connectionHandlerById.put(connId, CH);
    }

    public synchronized void subscribeToChannel(String channel, int connectionID, int subID){//(String channel, int connectionID, int subID)
        ConcurrentHashMap<Integer,Integer> channelMap = connectionIdByChannel.get(channel);
        if(channelMap == null){
            channelMap = new ConcurrentHashMap<>();
            channelMap.put(connectionID, subID);
            connectionIdByChannel.put( channel, channelMap);
        }
        else{
            channelMap.put(connectionID, subID);
        }
        ConcurrentHashMap<Integer,String> SubIdTochannelMap =channelByConnectionIdAndSubId.get(connectionID);
        if(SubIdTochannelMap == null){
            SubIdTochannelMap = new ConcurrentHashMap<>();
            SubIdTochannelMap.put(subID, channel);
            channelByConnectionIdAndSubId.put( connectionID, SubIdTochannelMap);
        }
        else{
            SubIdTochannelMap.put(subID, channel);
        }
    }    

    public synchronized boolean unSubscribeToChannel(int connectionID, int subID){
        ConcurrentHashMap<Integer,String> subIDtoChannel = channelByConnectionIdAndSubId.get(connectionID);
        if(subIDtoChannel==null || subIDtoChannel.get(subID)==null){
            return false; // If the connectionID doesn't have any subscriptions or the subId doesn't exist.
        }

        String channel = subIDtoChannel.get(subID); 
        subIDtoChannel.remove(subID);

        ConcurrentHashMap<Integer,Integer> channelMap = connectionIdByChannel.get(channel);
        channelMap.remove(connectionID);
        return true;
    }     
    
    public synchronized boolean isSubscribed(String channel, Integer connectionID){
        ConcurrentHashMap<Integer,Integer> channelMap = connectionIdByChannel.get(channel);
        if(channelMap != null && channelMap.get(connectionID)!=null)
            return true;
        return false;
    }

    public synchronized String getMessageId(){
        return String.valueOf(++currentMessageId);
    }

    public synchronized int getSubId(String channel, int connId){
        return connectionIdByChannel.get(channel).get(connId);
    }

}

