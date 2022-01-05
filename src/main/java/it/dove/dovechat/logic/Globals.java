/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.dove.dovechat.logic;

/**
 *
 * @author Luca
 */
public enum Globals {
    
    VERSION("1.1");
    
    private Globals(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    
    
    
    private String label;

}
