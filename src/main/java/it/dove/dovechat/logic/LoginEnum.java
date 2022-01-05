/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.dove.dovechat.logic;

/**
 *
 * @author lele
 */
public enum LoginEnum {
    USER_CONNECTED("UserConnected"),
    REGISTRATION("Registration");
    
    private String user;

    private LoginEnum(String user) {
        this.user = user;
    }
    
    public String getLoginEnum(){
        return user;
    }
}
