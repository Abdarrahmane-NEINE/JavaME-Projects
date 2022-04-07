/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestioninventaire;

import javax.microedition.midlet.*;
import java.io.IOException;
import java.util.Date;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStore;

/**
 * @author kaber
 */
public class GestionInventaireMidlet extends MIDlet implements CommandListener, ItemStateListener  {
    
    private Display display;
    private List listImage;
    //private List listInventory;
    private Command exit, next, openArticle, openInventory, openOpen, openOut, openConsulting, openScan, openremoveOutProduct;
    // Save product in database
    private Command saveCommand;
    // Delete selected appointments
    private Command deleteCommand;
    // Display a list of product already set in database
    private Command displayProduct;
    private Image article, inventaire;
    String[] stringElements = {"Article", "Inventaire"};
    
    DateField apptDate;
    TextField saisieField;
    TextField designationField;
    TextField quantityOfStockField;
    TextField newQuantityField;
             
    private String  saisie, apptTime, designation ,quantityOfStock ,newQuantity;
    
    private StringItem entry, output,accessed;
    
    private StringItem scan;
    
    Form setForm, inventoryOptionsForm, scanForm,removeOutProductForm, displayProductForm, menuForm;
    RecordStore rs;
    static final String DB_NAME = "MP";
    
    // To allow selection of products for delete or view
    ChoiceGroup choiceGroup;
    ChoiceGroup list;
    
    // The vector contains a list of products selected for deletion
    java.util.Vector vector = new java.util.Vector();
    boolean deleted[];
    java.util.Vector delRecords = new java.util.Vector();
    
    // This will retrieve products in name - mm/dd/yyyy hh:mm AM_PM 
    //form from record store
      public void getProducts()
  {
        // Allows single selection
        list = new ChoiceGroup(apptTime + " \n" + saisie  + "  \n" + designation  + "  \n" + quantityOfStock  + "  \n" + newQuantity , ChoiceGroup.EXCLUSIVE);
        // Allows multiple selections
        choiceGroup = new ChoiceGroup("produits",ChoiceGroup.MULTIPLE);
        String name;
        String at; 
            
        try
           {
               // Open a Record store
                rs = RecordStore.openRecordStore(DB_NAME,true);

               // Display list of products already set.
               // Ignore products which 
               // have been deleted and marked with a '@' 
               for (int j=0;j<rs.getNumRecords();j++)
               {
                   at = "";
                   name = "";
                   byte b[] = rs.getRecord(j);
                   // The string will comply to a ####AAAA format
                   String str = new String(b,0,b.length);

                   System.out.println(str);
                   if (!(str.startsWith("@")))
                   {
                        for (int i=0;i<str.length();i++)
                        {
                           // Digits represent time information in total 
                           // number of seconds
                             if (Character.isDigit(str.charAt(i)))
                                  at += str.charAt(i);
                             else
                                  name += str.charAt(i);

                        }
                   }

                    long time = 0;
                    String setAt = "";
                    if (!(at.trim().equals("")))
                    {
                         time = Long.parseLong(at);
                       // The time in seconds are converted to Date and 
                       // Calendar objects to display time of products in 
                       // user-desired format
                       java.util.Calendar rightNow = java.util.Calendar.getInstance();
                           Date date = null;
                        rightNow.setTime(date);
                       String year = String.valueOf(rightNow.get(java.util.Calendar.YEAR));
                       String month = String.valueOf(rightNow.get(java.util.Calendar.MONTH) + 1);
                       String day = String.valueOf(rightNow.get(java.util.Calendar.DATE));
                       String am = "";
                       if ( rightNow.get(java.util.Calendar.AM_PM) == 0)
                           am = "PM";
                           else if (rightNow.get(java.util.Calendar.AM_PM) == 1)
                           am = "AM";
                            String hr = String.valueOf(rightNow.get(java.util.Calendar.HOUR));
                            String min = String.valueOf(rightNow.get(java.util.Calendar.MINUTE));

                            setAt = name + "-" + month + "/" + day + "/" + year + " " + hr  + ":" + min + " " + am;
                    } else setAt = name;

                    // A list of products are formed for display and cancel 
                    // purposes     
                    list.append(setAt,null);
                    choiceGroup.append(setAt,null);
                    at = new String("");
                    name = new String("");
               }

            }catch(javax.microedition.rms.RecordStoreException exc)
            {
                      exc.printStackTrace();
            }
            //catch(javax.microedition.rms.RecordStoreNotOpenException exc)
            //{

            //}

            catch (Exception exc)
            {
                      exc.printStackTrace();

            }
     
     }
    
    public GestionInventaireMidlet(){
        try{
            display = Display.getDisplay(this);
            article = Image.createImage("/article.jpg");
            inventaire = Image.createImage("/inventaire.jpg");
            entry = new StringItem("Entrée", "");
            output = new StringItem("Sortie", "");
            accessed = new StringItem("Consultée", "");
            scan = new StringItem("scanner un code bare", "");
        }catch(Exception e){
            System.err.println( e.getMessage());
        }
    }
    
    public void startApp() {
        
        Image[] imageElements = {article, inventaire};
        listImage = new List("Gestion d'article et d'inventaire", List.IMPLICIT, stringElements, imageElements);
        next = new Command("Selectionner", Command.SCREEN, 1);
        saveCommand =  new Command("Save", Command.SCREEN, 1);
        deleteCommand =   new Command("Delete",Command.SCREEN, 1);
        displayProduct = new Command("Vue",Command.BACK, 1);
        openArticle = new Command("Article",Command.SCREEN, 1);
        openInventory = new Command("Inventaire",Command.SCREEN, 1);
        openOpen = new Command("Entrée",Command.SCREEN, 1);
        openOut = new Command("Sortie",Command.SCREEN, 1);
        openScan = new Command("Scanner",Command.SCREEN, 1);
        openConsulting = new Command("Consultée",Command.SCREEN, 1);
        openremoveOutProduct = new Command("retirer",Command.SCREEN, 1);
        exit = new Command("Sortir", Command.EXIT, 1);
        
        
        listImage.addCommand(openArticle);
        listImage.addCommand(openInventory);
        listImage.addCommand(exit);
        listImage.setCommandListener(this);
        display.setCurrent(listImage);
        
       
        
        
        setForm = new Form("Définir un article");
        saisieField = new TextField("Saisie","", 10,0);
        designationField = new TextField("designation","", 10,0);
        quantityOfStockField = new TextField("quantityOfStock","", 10,0);
        newQuantityField = new TextField("newQuantity","", 10,0);
        apptDate = new DateField("Définit le ", DateField.DATE_TIME);
        
        
        setForm.append(designationField);
        setForm.append(saisieField);
        setForm.append(apptDate);
        setForm.append(quantityOfStockField);
        setForm.append(newQuantityField);
        setForm.addCommand(exit);
        setForm.addCommand(saveCommand);
        //setForm.addCommand(displayProduct);
        //setForm.addCommand(menuCommand);
        setForm.setCommandListener(this);
        
        inventoryOptionsForm = new Form("Choisir une options");
        inventoryOptionsForm.append(entry);
        inventoryOptionsForm.append(output);
        inventoryOptionsForm.append(accessed);
        inventoryOptionsForm.addCommand(exit);
        inventoryOptionsForm.addCommand(openOpen);
        inventoryOptionsForm.addCommand(openOut);
        inventoryOptionsForm.addCommand(openConsulting);
        inventoryOptionsForm.setCommandListener(this);
        
        scanForm = new Form("scanner un code bare");
        scanForm.append(scan);
        scanForm.addCommand(openScan);
        scanForm.addCommand(exit);
        scanForm.setCommandListener(this);
        
        removeOutProductForm = new Form("retirer un produit");
        removeOutProductForm.addCommand(openremoveOutProduct);
        removeOutProductForm.addCommand(exit);
        removeOutProductForm.setCommandListener(this);
        
//        displayProductForm = new Form("Vue");
        
//        menuForm = new Form("Options");
//        menuForm.append("Sélectionner la Vue pour afficher les produits déjà enregistrer");
//        menuForm.addCommand(displayProduct);
//        menuForm.addCommand(openArticle);
//        menuForm.setCommandListener(this);
//        menuForm.setItemStateListener(this);
          displayProductForm = new Form("Vue");
          getProducts();
        // If the list of products is empty, notify user
        if (list.size() == 0)
        {

          // displayProductForm.append("Aucun produit n'est definit!. Suivre Menu->Definir");
           displayProductForm.append("aucun produit n'est définit !");
           displayProductForm.addCommand(exit);
           displayProductForm.addCommand(openArticle);
           displayProductForm.setCommandListener(this);
           displayProductForm.setItemStateListener(this);
           //display.setCurrent(displayProductForm);
        }
        // else display the list of products 
        else
        {

         displayProductForm.append(list);
         displayProductForm.addCommand(exit);
         displayProductForm.addCommand(openArticle);
         displayProductForm.setCommandListener(this);
         displayProductForm.setItemStateListener(this);
//         display.setCurrent(displayProductForm);
        }
     }
    
         public  void itemStateChanged(Item item)
  {
        java.util.Date date;
        // The date value is set to a variable when the DateField item 
        //  is changed
        if (item == apptDate)
       {
             date = apptDate.getDate();
             apptTime = String.valueOf(date.getTime());
         }

          if (item == saisieField)
        {
            saisie = saisieField.getString();
         }

          if (item == designationField)
        {
            designation = designationField.getString();
         }

          if (item == quantityOfStockField)
        {
            quantityOfStock = quantityOfStockField.getString();
         }

          if (item == newQuantityField)
        {
            newQuantity = newQuantityField.getString();
         }
        // If the ChoiceGroup item state on Delete form is changed, 
         //it sets an array of 
       //products selected for deletion
        if (item == choiceGroup)
        {
        choiceGroup.getSelectedFlags(deleted);
        }
   }
         
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
        notifyDestroyed();
    }
    
    public void commandAction(Command c, Displayable s){
        int index = listImage.getSelectedIndex();
        //if(c == openArticle || c == List.SELECT_COMMAND){
        //if(c == openArticle && c == List.SELECT_COMMAND){
        if(c == openArticle){
            //display.setCurrent(setForm);
//            System.out.println(List.SELECT_COMMAND);
            display.setCurrent(setForm);
        } else if(c == openInventory){
            display.setCurrent(inventoryOptionsForm);
        }
        
        
        else if(c == openOpen){
        display.setCurrent(scanForm);
        }else if(c == openOut){
        display.setCurrent(setForm);
        }else if(c == openConsulting){
//                display.setCurrent(scanForm);
        }
        
        if(c == openScan){
                display.setCurrent(setForm);
        }
        
        if(c == openremoveOutProduct){
                display.setCurrent(setForm);
        }
        // Display a list of products
        if (c==displayProduct)
        {

             getProducts();

             displayProductForm.append(list);
             displayProductForm.addCommand(exit);
             displayProductForm.addCommand(openArticle);
             displayProductForm.setCommandListener(this);
             displayProductForm.setItemStateListener(this);
             display.setCurrent(displayProductForm);


        }
        // When hit Save, insert record into record store as stream of bytes     
        if (c==saveCommand)
        {
             try
             {
                //displayProductForm.append(list);
                setForm.addCommand(displayProduct);
                String appt = apptTime + " " + saisie  + " " + designation  + " " + quantityOfStock  + " " + newQuantity ; 
                byte bytes[] = appt.getBytes();
                rs.addRecord(bytes,0,bytes.length);

             }
             catch (Exception exc)
             {
                  exc.printStackTrace();
             }

        }
        // Actually delete the set of selected appointments. 
        //The selected appointments are 
        // marked as invalid in the record store and are 
        //not displayed later
//         if (c==deleteCommand)
//         {
//              try
//              {
//              for (int m=0;m<deleted.length;m++)
//              {
//                   boolean ifDeleted = deleted[m];
//                   // In case a record is deleted, store some invalid characters
//                   // that signify it is inactive
//                   if (ifDeleted)
//                   {
//                        String deactive = "@";
//                        byte b[] = deactive.getBytes();
//                        rs.setRecord(m+1, b,0,b.length);
//                   }
//              }
//              }
//              catch (Exception exc)
//              {
//                   exc.printStackTrace();
//              }
//
//         }
        if(c == exit){
            destroyApp(true);
        }
        
//        switch(c){
//            
//        }
    }
}
