package com.automation.pages;
import com.automation.config.Config;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
public class LoginPage {
 private final WebDriver driver;private final WebDriverWait wait;
 public LoginPage(WebDriver driver){this.driver=driver;wait=new WebDriverWait(driver,Duration.ofSeconds(Config.positiveInt("wait.seconds")));}
 public void login(String username,String password){
  WebElement input=wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));input.clear();input.sendKeys(username);
  input=driver.findElement(By.id("password"));input.clear();input.sendKeys(password);
  driver.findElement(By.id("login")).click();
 }
 public String message(String expected){wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("message"),expected));return driver.findElement(By.id("message")).getText();}
 public void logout(){wait.until(ExpectedConditions.elementToBeClickable(By.id("logout"))).click();}
 public boolean logoutHidden(){return !driver.findElement(By.id("logout")).isDisplayed();}
}
