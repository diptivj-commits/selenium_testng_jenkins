package com.automation.base;
import com.automation.config.Config;
import com.automation.support.PracticeServer;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.testng.*;
import org.testng.annotations.*;
import java.nio.file.*;
import java.time.Duration;
public class BaseTest {
 protected WebDriver driver;
 private static PracticeServer server;
 private static String appUrl;
 @BeforeSuite(alwaysRun=true)
 public void startPracticePage()throws Exception {
  int port=Integer.parseInt(Config.get("fixture.port"));
  if(port<0||port>65535)throw new IllegalArgumentException("fixture.port must be 0..65535");
  server=new PracticeServer(Config.get("fixture.bind"),port);
  appUrl="http://127.0.0.1:"+server.port()+"/";
  System.out.println("Practice page: "+appUrl);
 }
 @BeforeMethod(alwaysRun=true)
 public void startBrowser(){
  ChromeOptions options=new ChromeOptions();options.addArguments("--window-size=1440,900");
  if(Boolean.parseBoolean(Config.get("headless")))options.addArguments("--headless=new");
  String binary=Config.optional("chrome.binary");if(!binary.isEmpty())options.setBinary(binary);
  driver=new ChromeDriver(options);
  driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Config.positiveInt("page.load.seconds")));
  driver.get(appUrl);
 }
 @AfterMethod(alwaysRun=true)
 public void stopBrowser(ITestResult result){
  if(driver==null)return;
  try{if(!result.isSuccess())try{
   Path folder=Paths.get("reports","screenshots");Files.createDirectories(folder);
   Path file=Files.createTempFile(folder,result.getName()+"-",".png");
   Files.write(file,((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES));
   Reporter.log("Failure screenshot: "+file.toAbsolutePath(),true);
  }catch(Exception e){System.err.println("Screenshot unavailable: "+e.getMessage());}}
  finally{try{driver.quit();}finally{driver=null;}}
 }
 @AfterSuite(alwaysRun=true) public void stopPracticePage(){if(server!=null){server.close();server=null;}}
}
