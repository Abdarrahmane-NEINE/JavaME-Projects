/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listimage;

import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.Image;

/**
 * @author kaber
 */
public class ListImage extends MIDlet {
 private Display display;
  private List list;
  private Command exit, next;
  private Image car, airplane, hotel, mobile, cartoon;
  String[] stringElements = {"Aero plane", "Car", "Hotel", "Mobile"};
  
  public ListImage(){
  try{
  airplane = Image.createImage("/5.jpg");
  car = Image.createImage("/4.jpg");
  hotel = Image.createImage("/1.jpg");
  mobile = Image.createImage("/2.jpg");
  cartoon = Image.createImage("/3.jpg");
  }catch(Exception e){
  System.err.println(e.getMessage());
  }
  }

  public void startApp() {
  display = Display.getDisplay(this);
  Image[] imageElements = {airplane, car, hotel, mobile};
  list = new List("List + Image", List.IMPLICIT,stringElements, imageElements);
  next = new Command("Select", Command.SCREEN, 0);
  exit = new Command("Exit", Command.EXIT, 0);
  list.addCommand(next);
  list.addCommand(exit);
  list.setCommandListener((CommandListener) this);
  display.setCurrent(list);
  }

  public void pauseApp() {}

  public void destroyApp(boolean unconditional){
  notifyDestroyed();
  }

  public void commandAction(Command c, Displayable s){
  int index = list.getSelectedIndex();
  if (c == next || c == List.SELECT_COMMAND) {
  Alert alert = new Alert("Selected", "You have selected:" + list.getString(index)+ ".", cartoon, AlertType.INFO);
  display.setCurrent(alert, list);
  } else if(c == exit){
  destroyApp(true);
  }
  }
}