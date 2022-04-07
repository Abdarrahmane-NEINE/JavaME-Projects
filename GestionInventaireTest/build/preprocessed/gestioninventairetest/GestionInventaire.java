/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestioninventairetest;

import javax.microedition.midlet.*;
import java.io.IOException;
import javax.microedition.lcdui.*;

/**
 * @author kaber
 */
public class GestionInventaire extends MIDlet implements CommandListener {

    private Display display;
    private List list;
    private Command exit, next;
    private Image article, inventaire;
    String[] stringElements = {"Article", "Inventaire"};
    
    public GestionInventaire(){
        try{
            article = Image.createImage("/article.jpg");
            inventaire = Image.createImage("/inventaire.jpg");
        }catch(Exception e){
            System.err.println( e.getMessage());
        }
    }
    
    public void startApp() {
        display = Display.getDisplay(this);
        Image[] imageElements = {article, inventaire};
        list = new List("Gestion d'article et d'inventaire", List.IMPLICIT, stringElements, imageElements);
        next = new Command("Selectionner", Command.SCREEN, 0);
        exit = new Command("Sortir", Command.EXIT, 0);
        list.addCommand(next);
        list.addCommand(exit);
        list.setCommandListener(this);
        display.setCurrent(list);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
        notifyDestroyed();
    }
    
    public void commandAction(Command c, Displayable s){
        int index = list.getSelectedIndex();
        if(c == next || c == List.SELECT_COMMAND){
            
        } else if(c == exit){
            destroyApp(true);
        }
    }
    
    
}
