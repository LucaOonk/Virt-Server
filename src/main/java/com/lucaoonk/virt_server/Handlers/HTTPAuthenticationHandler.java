package com.lucaoonk.virt_server.Handlers;

import com.lucaoonk.virt_server.Backend.Terminal;
import com.lucaoonk.virt_server.Backend.Objects.Context;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HTTPAuthenticationHandler {

    private int size = 25;
    private Context context;

    public HTTPAuthenticationHandler(Context context){
        this.context = context;
    }


    public void generateNewRandomAuthentication(){

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(size);

        for (int i = 0; i < size; i++) {

        // generate a random number between
        // 0 to AlphaNumericString variable length
        int index = (int)(AlphaNumericString.length()
            * Math.random());

        // add Character one by one in end of sb
        sb.append(AlphaNumericString
            .charAt(index));
        }

        context.setHTTPAuth(sb.toString());
    }

    public static void returnAccessDeniedPage(HttpExchange exchange){

        String response = "<html><h1>Error 401: Not authenticated</h1></html>";

        try {
            exchange.sendResponseHeaders(401, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();     

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println(Terminal.colorText("[ERROR] "+Terminal.getTime()+" Error 401 on request: "+exchange.getRequestURI(), Terminal.ANSI_RED));

    }

    public void printErrorDenied(HttpExchange exchange){
        context.textArea.append("[ERROR] "+Terminal.getTime()+" Error 401 on request: "+exchange.getRequestURI()+"\n");
        System.out.println(Terminal.colorText("[ERROR] "+Terminal.getTime()+" Error 401 on request: "+exchange.getRequestURI(), Terminal.ANSI_RED));

    }
}
