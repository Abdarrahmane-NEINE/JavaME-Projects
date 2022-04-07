/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagetest;

import java.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author kaber
 */
public class ImageItemExample extends MIDlet implements CommandListener{

private Display display;
private Command exit;
private Form form;
private ImageItem logo;

public ImageItemExample(){
form = new Form("Image Item");
exit = new Command("Exit", Command.EXIT, 0);
try{
  logo = new ImageItem(null, Image.createImage("src/img.png"),
   ImageItem.LAYOUT_CENTER | ImageItem.LAYOUT_NEWLINE_BEFORE |
    ImageItem.LAYOUT_NEWLINE_AFTER, "Roseindia");
  form.append(logo);
}catch(IOException e){
  form.append(new StringItem(null, "Roseindia: Image not available: "+ e));
}
}

public void startApp(){
display = Display.getDisplay(this);
form.addCommand(exit);
form.setCommandListener(this);
display.setCurrent(form);
}

public void pauseApp(){}

public void destroyApp(boolean unconditional){
notifyDestroyed();
}

public void commandAction(Command c, Displayable d){
String label = c.getLabel();
if(label.equals("Exit")){
  destroyApp(true);
}
}
}