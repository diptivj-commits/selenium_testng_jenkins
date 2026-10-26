package com.automation.support;
import com.sun.net.httpserver.HttpServer;
import java.net.*;
import java.io.*;
public final class PracticeServer implements AutoCloseable {
 private final HttpServer server;
 public PracticeServer(String bind,int port)throws IOException {
  server=HttpServer.create(new InetSocketAddress(bind,port),0);
  server.createContext("/",exchange->{
   if(!exchange.getRequestURI().getPath().equals("/")){exchange.sendResponseHeaders(404,-1);exchange.close();return;}
   try(InputStream in=PracticeServer.class.getResourceAsStream("/site/index.html")) {
    if(in==null){exchange.sendResponseHeaders(500,-1);return;}
    byte[] bytes=in.readAllBytes();exchange.getResponseHeaders().set("Content-Type","text/html; charset=UTF-8");
    exchange.sendResponseHeaders(200,bytes.length);exchange.getResponseBody().write(bytes);
   }finally{exchange.close();}
  });server.start();
 }
 public int port(){return server.getAddress().getPort();}
 @Override public void close(){server.stop(0);}
}
