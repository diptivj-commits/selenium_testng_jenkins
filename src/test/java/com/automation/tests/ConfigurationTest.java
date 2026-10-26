package com.automation.tests;
import com.automation.config.Config;
import com.automation.support.PracticeServer;
import org.testng.annotations.Test;
import java.net.*;
import java.nio.charset.StandardCharsets;
import static org.testng.Assert.*;
public class ConfigurationTest {
 @Test public void headlessSettingIsBoolean(){assertTrue(java.util.Set.of("true","false").contains(Config.get("headless")));}
 @Test public void waitIsPositive(){assertTrue(Config.positiveInt("wait.seconds")>0);}
 @Test public void practicePageContainsLoginFormAndVisibilityFix()throws Exception{
  try(PracticeServer server=new PracticeServer("127.0.0.1",0)){
   HttpURLConnection c=(HttpURLConnection)URI.create("http://127.0.0.1:"+server.port()+"/").toURL().openConnection();
   c.setConnectTimeout(5000);c.setReadTimeout(5000);
   try{assertEquals(c.getResponseCode(),200);try(var in=c.getInputStream()){String html=new String(in.readAllBytes(),StandardCharsets.UTF_8);assertTrue(html.contains("Jenkins Login Practice"));assertTrue(html.contains("button[hidden]{display:none!important}"));assertTrue(html.contains("id=\"username\""));}}finally{c.disconnect();}
  }
 }
}
