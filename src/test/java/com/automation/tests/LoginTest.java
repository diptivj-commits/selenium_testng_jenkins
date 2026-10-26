package com.automation.tests;
import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import org.testng.annotations.*;
import static org.testng.Assert.*;
public class LoginTest extends BaseTest {
 @Test public void validLogin(){LoginPage page=new LoginPage(driver);page.login("demo","DemoPassword123");assertEquals(page.message("Login successful"),"Login successful");assertFalse(page.logoutHidden());}
 @Test public void invalidLogin(){LoginPage page=new LoginPage(driver);page.login("demo","wrongPassword");assertEquals(page.message("Invalid credentials"),"Invalid credentials");assertTrue(page.logoutHidden());}
 @Test public void logout(){LoginPage page=new LoginPage(driver);page.login("demo","DemoPassword123");page.message("Login successful");page.logout();assertEquals(page.message("Logged out"),"Logged out");assertTrue(page.logoutHidden());}
}
