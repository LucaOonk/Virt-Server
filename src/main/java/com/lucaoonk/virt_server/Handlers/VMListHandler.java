package com.lucaoonk.virt_server.Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.lucaoonk.virt_server.Backend.Processors.VMListJson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VMListHandler implements HttpHandler {
    
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            InputStreamReader isr =  new InputStreamReader(exchange.getRequestBody(),"utf-8");
            BufferedReader br = new BufferedReader(isr);
            String value = br.readLine();
            
            String response = "This is the response" + " | Request type: "+exchange.getRequestMethod()+" | Request Body: "+value;
        

            try {

                response = VMListJson.getVMList();

                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {

                response="Something went wrong, ";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

                System.out.println(e.getStackTrace());
            }            
        }
}
