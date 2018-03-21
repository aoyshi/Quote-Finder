package quotefinder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QuoteFinder extends Application {
  
  @Override
  public void start(Stage primaryStage) {
    
    //----------MAIN WINDOW-----------
    Stage window = primaryStage;
    window.setTitle("QuoteFinder");
    TextArea display = new TextArea();
    display.setPrefHeight(150);
    String[] quote = new String[1];
    
    //---------MOTIVATE BUTTON----------
    Button mainButton = new Button("Motivate Me!");    
    mainButton.setOnAction((ActionEvent e) -> {
      Crawler.CRAWL(quote);     
      if(quote[0]==null)
        System.out.println("Could not fetch quote. Try Again.");      
      else
        display.setText(quote[0]);        
    });    
    display.setWrapText(true);
    display.setEditable(false);
    
    //Disable save+google button if text display empty
    BooleanBinding boolBind = new BooleanBinding() { 
      {
        super.bind(display.textProperty());
      }       
      @Override
      protected boolean computeValue() 
      {
        return (display.getText().isEmpty());
      }      
    };
    
    //---------SAVE BUTTON----------
    Button save = new Button(" Save ");
    save.disableProperty().bind(boolBind); 
    save.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent ActionEvent) 
      {       
    	try 
        {
              File saveFile = new File("Saved Quotes.txt");

              if (saveFile.createNewFile()){  //returns false if file already exists; creates if not exists.
                System.out.println("saveFile is created!");
                PrintWriter writer = null;
                while(true)
                {
                  try {
                    writer = new PrintWriter("Saved Quotes.txt"); //open file for writing
                  }
                  catch (Exception e)
                  {
                    System.out.printf("Error in opening file for writing.\n");
                    break;
                  }
                  writer.printf("%s\n\n", quote[0]);
                  writer.close();
                  break;
                }        
              }

              else //file already exists, dont create; only append
              {
                System.out.println("saveFile already exists. Appending to file...");
                try
                (FileWriter fw = new FileWriter("Saved Quotes.txt", true); //true = append, dont overwrite
                BufferedWriter bw = new BufferedWriter(fw); //bypasses long disk read/write time
                PrintWriter out = new PrintWriter(bw))
                {
                  out.printf("\n\n");
                  out.println(quote[0]);
                } 
            catch (IOException e) {
              System.out.println("Error appending to file!");
            }
            System.out.println("Quote Appended Successfully.");   
	      }

    	}         
        catch (IOException e) 
        {
	      System.out.println("Error with file IO in main TRY{}block!");
        }
      }      
  });
    
    //-----------GOOGLE SEARCH-------------
    Button google = new Button("Google");
    google.disableProperty().bind(boolBind);
    google.setOnAction(e -> Crawler.googleSearch(quote[0]));
      
    //------FOOTER-------------contains SAVE & GOOGLE buttons------------
    HBox footer = new HBox();
    footer.setSpacing(20);
    footer.getChildren().addAll(save, google);
    footer.setAlignment(Pos.CENTER);
    
    //-------MAIN LAYOUT------------
    VBox layout = new VBox(25);
    layout.setPadding(new Insets(40,30,40,30));
    layout.getChildren().addAll(mainButton, display, footer);
    layout.setAlignment(Pos.CENTER);
    
    //----------MAIN SCENE---------
    Scene scene = new Scene(layout,350,350);
    window.setScene(scene);
    window.show();    
  }
  
  public static void main(String[] args) {
    launch(args);
  }


}
