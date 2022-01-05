/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.dove.dovechat.logic;

import java.util.Arrays;

/**
 * !!!!!!!!!!!!LEGGIMI!!!!!!!!!!!! Consiglieri di mettere questa classe come
 * libreria sia sul client che sul server per avere un doppio controllo
 * Valutiamo l'idea di renderlo statico per fare una cosa cosi
 * ManagementPassword.checkPasswordRegistration(.....)
 *
 * @author lele
 */
public class ManagementPassword {

    private int passwordLeng = 1;
    String a;
    
    public ManagementPassword() {
    }

    public boolean checkPasswordRegistration(String nickName,
            char[] jPasswordField1, char[] jPasswordField2) {

        /* password is empty */
        if (jPasswordField1.length == 0
                || jPasswordField2.length == 0 || nickName.isEmpty()) {
            return false;
        }

        /* password is length */
        if (jPasswordField1.length <= passwordLeng) {
            return false;
        }

        /* password is equal */
        if (!Arrays.equals(jPasswordField1, jPasswordField2)) {
            return false;
        }
        
        if (isUpperCase(jPasswordField1) == false){
            return false;
        }
        
        return true;
    }

    public boolean checkPasswordlogin(String nickName, char[] jPasswordField1) {
        if (jPasswordField1.length == 0 || nickName.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isUpperCase(char[] jPasswordField1) {
        for (int i = 0; i < jPasswordField1.length; i++) {
            if ( Character.isUpperCase( jPasswordField1[i]) ){
                return true;
            }            
        }
        return false;
    }      
}