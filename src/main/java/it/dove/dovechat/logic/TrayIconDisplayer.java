/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.dove.dovechat.logic;

import it.dove.dovechat.mqtt.MQTTClient;
import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Luca
 */
public class TrayIconDisplayer {

    private static TrayIconDisplayer _instance = null;
    private ImageIcon infoIcon = new javax.swing.ImageIcon(TrayIconDisplayer.class.getResource("/it/dove/dovechat/icons/gdot_red_16.png"));
    private static TrayIcon tryIcon = null;

    public static TrayIconDisplayer getInstance() {
        if (_instance == null) {
            _instance = new TrayIconDisplayer();
            return _instance;
        } else {
            return _instance;
        }
    }

    private final void init() {
        if (!SystemTray.isSupported()) {
            System.err.println("Your computer does suck");
            return;
        }
        if (tryIcon == null) {
            tryIcon = new TrayIcon(infoIcon.getImage(), "prova");
            tryIcon.setToolTip("If you are using this, this is a bad.\nversion " + Globals.VERSION.getLabel());
            SystemTray systemTray = SystemTray.getSystemTray();
            try {
                systemTray.add(tryIcon);
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
        }

    }
    
    public void displayInfo(String title, String info) {
            tryIcon.displayMessage(title, info, TrayIcon.MessageType.INFO);
    }
    
    public void displayWarning(String title, String warning) {
            tryIcon.displayMessage(title, warning, TrayIcon.MessageType.WARNING);
    }
    
    public void displayError(String title, String error) {
            tryIcon.displayMessage(title, error, TrayIcon.MessageType.ERROR);
    }

    public void displayIncomingMessage(String sender) {
        if (!sender.equals(MQTTClient.getInstance().getMyNickName())) {
            tryIcon.displayMessage("New message", "from " + sender, TrayIcon.MessageType.INFO);
        }
    }

    private TrayIconDisplayer() {
        super();
        init();
    }

}
