/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.dove.dovechat.mqtt;

import it.dove.dovechat.logic.EventManager;
import it.dove.dovechat.logic.Topics;
import it.dove.dovechat.logic.TrayIconDisplayer;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author Luca
 */
public class MQTTClient implements MqttCallback {

    public static final String LOGIN_TOPIC = "loginTopic";
    private static MQTTClient _instance = null;
    private final int qos = 2;
    private MqttClient sampleClient = null;
    private String myNickName = "unknown";
    private String secret = "bumbu";

    String content = "Visit www.hascode.com! :D";

    //String broker = "tcp://localhost:1883";
    String broker;
    String ip_server;

    static final String clientId = "paho-java-client" + new Date().getTime();

    public static MQTTClient getInstance() {
        if (_instance == null) {
            _instance = new MQTTClient();
            return _instance;
        } else {
            return _instance;
        }
    }

    private MQTTClient() {
        super();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setIp_server(String ip_server) {
        this.ip_server = ip_server;
    }

    public void connect() {
        String topic = "news";
        this.broker = "tcp://" + ip_server + ":1883";
        try {
            sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("paho-client connecting to broker: " + broker);
            sampleClient.connect(connOpts);

            /* subscribe section */
            sampleClient.subscribe("UserConnected");
            sampleClient.subscribe(Topics.USER_DISCONNECTED.getTopic());
            sampleClient.subscribe(Topics.CHAT.getTopic());
            sampleClient.subscribe(Topics.EMERGENCY.getTopic());
            sampleClient.setCallback(this);

            System.out.println("paho-client connected to broker");
            System.out.println("paho-client publishing message: " + content);

            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("paho-client message published");
            
            TrayIconDisplayer.getInstance().displayInfo("Emergy System", "Connected!");

//            sampleClient.disconnect();
//            System.out.println("paho-client disconnected");
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    public void tryLogin(String username, String encryptedPassword) {
        try {
            String topic = Topics.ATTEMPT_LOGIN.getTopic() + "/" + clientId;
            String message = username + "," + encryptedPassword;
            MqttMessage mx = new MqttMessage(message.getBytes());
            mx.setQos(qos);
            if (sampleClient.isConnected()) {
                System.out.println("CONNESSOOOO");
            } else {
                sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                System.out.println("paho-client connecting to broker: " + broker);

                sampleClient.connect(connOpts);
                System.out.println("NOT CONNESSO");
            }
            sampleClient.subscribe(Topics.ACK_LOGIN.getTopic() + "/" + clientId);
            sampleClient.publish(topic, mx);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendChatMessage(String message) {
        publish(Topics.CHAT.getTopic(), myNickName + ":" + message);

    }

    public void publish(String topic, String message) {
        try {
            if (!topic.equals("UserConnected")) {
                message = CryptoManager.getInstance().encrypt(message);
            }
            System.out.println("PUBLISHING MESSAGE: " + message);
//            message = Base64.getEncoder().encodeToString(message.getBytes()); //BASE 64
            MqttMessage mx = new MqttMessage(message.getBytes());
            mx.setQos(qos);
            if (sampleClient.isConnected()) {
                System.out.println("CONNESSOOOO");
            } else {
                sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                System.out.println("paho-client connecting to broker: " + broker);
                sampleClient.connect(connOpts);
                System.out.println("NOT CONNESSO");
            }
            sampleClient.publish(topic, mx);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getMyNickName() {
        return myNickName;
    }

    public void setMyNickName(String myNickName) {
        this.myNickName = myNickName;
        try {
            sampleClient.subscribe(myNickName);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void connectionLost(Throwable thrwbl) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage mm) {
        try {
            System.out.println("TOPIC: " + topic);
            System.out.println("MESSAGE: " + new String(mm.getPayload()));
            String message = new String(mm.getPayload());
//        byte[] decodedBytes = Base64.getDecoder().decode(message);
//        String decodedString = new String(decodedBytes);

            System.out.println("CHAT MESSAGE: " + message);

            if (topic.equals(Topics.EMERGENCY.getTopic())) {
                
                String[] split = message.split(":");
                if(split[0].equals("Q")){
                    int seconds = Integer.parseInt(split[1]);
                    TrayIconDisplayer.getInstance().displayWarning("Server Shutdown", "Server will be shut down in "+seconds+" seconds");
                    EventManager.getInstance().shutDown(seconds);
                }

                
            }
            
            if (topic.equals(Topics.ACK_LOGIN.getTopic() + "/" + clientId)) {

                if (message.equals("ERROR:1")) {
                    EventManager.getInstance().ackReceived(1);
                }
            }
            if (topic.equals("UserConnected")) {
                EventManager.getInstance().userConnected(new String(mm.getPayload()));
            }
            
            if (topic.equals(Topics.USER_DISCONNECTED.getTopic())) {
                EventManager.getInstance().userDisconnected(new String(mm.getPayload()));
            }

            if (topic.equals(myNickName)) {
                System.out.println("MESSAGGIO PER ME : " + new String(mm.getPayload()));
            }
            if (topic.equals(Topics.CHAT.getTopic())) {
                message = CryptoManager.getInstance().decrypt(message);

                String[] chatComponents = message.split(":");

                EventManager.getInstance().chatMessageReceived(chatComponents[0], chatComponents[1]);
            }
        } catch (Exception es) {
            es.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {

    }

}
