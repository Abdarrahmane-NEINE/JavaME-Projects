/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagetest;


import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

/**
 * @author kaber
 */
public class AppendImage extends MIDlet {
    private Display display;
  private FormClass form;
  private MIDlet midlet;
  
  protected void startApp(){
  form = new FormClass("Form Created", this);
  display = Display.getDisplay(this);
  display.setCurrent(form);
  }

  protected void pauseApp(){}

  protected void destroyApp(boolean unconditional){}

  class FormClass extends Form implements CommandListener{
  private Command exit;
  private Image image;
  
  public FormClass(String title, MIDlet items){
  super(title);
  midlet = items;
  try{
  image = Image.createImage("/boot.png");
  }catch(Exception e){}

  exit = new Command("Exit", Command.BACK, 1);
  append(image);
  this.addCommand(this.exit);
  this.setCommandListener(this);
  }

  public void commandAction(Command c, Displayable d){
  String label = c.getLabel();
  if(label.equals("Exit")){
  midlet.notifyDestroyed(); 
  }
  }
  }
}