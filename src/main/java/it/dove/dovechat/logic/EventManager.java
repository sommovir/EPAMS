/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.dove.dovechat.logic;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author lele
 */
public class EventManager {

    private static EventManager instance = null;
    private List< ConnectionEvent> connectionEvents = new LinkedList<>();

    private EventManager() {

    }

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void addConnectionEvents(ConnectionEvent connectionEvent) {
        this.connectionEvents.add(connectionEvent);
    }

    public void userConnected(String nickName) {
        for (ConnectionEvent connectionEvent : connectionEvents) {
            connectionEvent.userConnected(nickName);
        }
    }
    
    public void userDisconnected(String nickName) {
        for (ConnectionEvent connectionEvent : connectionEvents) {
            connectionEvent.userDisonnected(nickName);
        }
    }

    public void ackReceived(int error){
        for (ConnectionEvent connectionEvent : connectionEvents) {
            connectionEvent.ackReceived(error);
        }
    }
    
    public void chatMessageReceived(String author, String message){
        for (ConnectionEvent connectionEvent : connectionEvents) {
            connectionEvent.chatMessageReceived(author, message);
        }
    }
    
    public void shutDown(int seconds){
        for (ConnectionEvent connectionEvent : connectionEvents) {
            connectionEvent.serverShutDown(seconds);
        }
    }
}
