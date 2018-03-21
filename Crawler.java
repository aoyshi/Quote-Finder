package quotefinder;

//CLASS contains quoteFetcher and googleSearch functions

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Crawler { 
  
    //------------------MAIN QUOTE FETCH FUNCTION-------------------
    public static void CRAWL(String[] quote){
      
    //-----------INITIALIZE WEBDRIVER----------
    System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ryz\\Downloads\\chromedriver_win32\\chromedriver.exe");
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");   //hides browser
    WebDriver driver = new ChromeDriver(options);
    
    

    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS); //waits for 2s till (ANY) element discoverable   

    //------GENERATE SEARCH TAG---------    
    String tagList[]  = {"motivational", "success", "motivation", "inspiration","wisdom", "optimism", "hope"};
    int tagIndex = (int) (Math.random()*(tagList.length-1));
    String tagName = tagList[tagIndex]; 
    System.out.println("tagIndex=" + tagIndex +" tagName=" + tagName);  
    
    long start1 = System.currentTimeMillis();
    System.out.println("Start time 1 is: " + start1);
    
    driver.navigate().to("https://www.goodreads.com/quotes/tag/" + tagName);
    long time1 = System.currentTimeMillis() - start1;
    System.out.println("tag time: " + time1);
    
    //------GET PAGE COUNT-----------
    List<WebElement> linksList = driver.findElements(By.xpath("/html/body/div[1]/div[2]/div[1]/div[2]/div[3]/div[33]/div/a")); 
          //^collects all links (subpages) from current page footer
    int listSize = (linksList.size() - 1);  
    String lastPagePath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[3]/div[33]/div/a[" + listSize + "]";
    String lastPage = driver.findElement(By.xpath(lastPagePath)).getText(); //gets very last page, (right before NEXTPAGE) in footer
    int pageCount=0;
    try {
      pageCount = Integer.parseInt(lastPage);
    }
    catch (Exception e) {
      System.out.printf("Error: Can't convert %s to int!\n", lastPage);
    } 
    
    //-----GENERATE RANDOM PAGE NUMBER-------- 
    int pageNum;
    while(true)
    {
      pageNum = (int) (Math.random()*pageCount);    
      if(pageNum != 0)
      { break; }
    }   
    System.out.println("pageCount=" + pageCount + " pageNum=" + pageNum);  //
    
    
    //GET TOTAL QUOTE COUNT OF CURR.PAGE---------
    long start3 = System.currentTimeMillis();
    
    driver.navigate().to("https://www.goodreads.com/quotes/tag/" + tagName + "?page=" + pageNum);
        long time3 = System.currentTimeMillis() - start3;
    System.out.println("get smallText " + time3);
    String pageInfo = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[1]/div[2]/div[3]/div[1]/span")).getText();
    System.out.println("smallText=" + pageInfo); //Showing 203-233 quotes
    


    String[] parts = pageInfo.split(" "); 
    String range = parts[1];     //get 203-233 part
    
    String[] numbers = range.split("-");
    String first = numbers[0];  //get 203
    String last = numbers[1];   //get 233
    int start, stop;
    
    //if quote range format is 2,900-3,100, remove commas
    if(first.length()>3){
       first = first.substring(0,1)+first.substring(2,5);
       start = Integer.parseInt(first);    
    }
    else {          
      start = Integer.parseInt(first);  
    }
    if(last.length()>3){
      last = last.substring(0,1)+last.substring(2,5);
      stop = Integer.parseInt(last);    
    }
    else {          
      stop = Integer.parseInt(last);  
    }

    System.out.println("range is: " + start + " to " + stop); //
    
    //---------GENERATE RANDOM QUOTE NUMBER---------
    int total = (stop-start)+3; //total quotes per (current) page
    int quoteNum;
    while(true)
    {
      quoteNum = (int) (Math.random()*total);
      if(quoteNum!=3 && quoteNum!=0 && quoteNum!=1) //0,1 doesn't exist, #3 is google ad amongst quotes
      {
        break;
      }
    }
    System.out.println("#Quotes/Pg=" + total + " quoteNum=" + quoteNum); //
    
    //-----FETCH RANDOM QUOTE-------
     long start5 = System.currentTimeMillis();
    String quoteXPath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[3]/div[" + quoteNum + "]/div[1]/div[1]"; 
    String quoteFinal = driver.findElement(By.xpath(quoteXPath)).getText();  
    System.out.printf("\n%s\n", quoteFinal);
        long time5 = System.currentTimeMillis() - start5;
    System.out.println("fecth quote " + time5);
    
    //----------END-----------
    driver.close();  
    quote[0] = quoteFinal;
  }
    
    
  //--------------GOOGLE SEARCH FUNCTION-----------------  
  public static void googleSearch(String quote) 
  { 
        WebDriver engine = new ChromeDriver();
        engine.navigate().to("https://images.google.com/");
        engine.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        engine.findElement(By.xpath("//*[@id=\"lst-ib\"]")).sendKeys(quote, Keys.RETURN);
        System.out.println("Sent to Google.");
  }  

  //added by IDE
  static void googleSearch() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
    
}
  
