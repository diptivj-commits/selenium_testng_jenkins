package com.automation.config;
import java.io.*;
import java.util.*;
public final class Config {
 private static final Properties VALUES = load();
 private Config() {}
 private static Properties load() {
  Properties p=new Properties();
  try(InputStream in=Config.class.getClassLoader().getResourceAsStream("config.properties")) {
   if(in==null)throw new IllegalStateException("Missing config.properties");p.load(in);return p;
  }catch(IOException e){throw new IllegalStateException(e);}
 }
 // Precedence: Java -D property, environment variable, config.properties.
 public static String get(String key) {
  String v=System.getProperty(key);
  if(v==null)v=System.getenv(key.toUpperCase(Locale.ROOT).replace('.','_'));
  if(v==null)v=VALUES.getProperty(key);
  if(v==null||v.isBlank())throw new IllegalArgumentException("Missing setting: "+key);
  return v.trim();
 }
 public static String optional(String key){return System.getProperty(key,System.getenv(key.toUpperCase(Locale.ROOT).replace('.','_'))!=null?System.getenv(key.toUpperCase(Locale.ROOT).replace('.','_')):VALUES.getProperty(key,"")).trim();}
 public static int positiveInt(String key){int n=Integer.parseInt(get(key));if(n<1)throw new IllegalArgumentException(key+" must be positive");return n;}
}
