/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.dove.dovechat.logic;

import org.omg.CORBA.UNKNOWN;

/**
 *
 * @author sommovir
 */
public enum Commands {
    
    UNKNOWN("9dhyjapòa9s8udhapsa9dhaksnd"),
    GIGACHAD("-!-lakajaoaua-gigachaddone");
    
    private String command;

    private Commands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }


    public static Commands of(String command){
        switch(command){
            case "-!-lakajaoaua-gigachaddone": return GIGACHAD;
            default: return UNKNOWN;
        }
    }
    
    
}
