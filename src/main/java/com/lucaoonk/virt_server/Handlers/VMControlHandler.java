package com.lucaoonk.virt_server.Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.google.gson.Gson;
import com.lucaoonk.virt_server.Backend.VMController;
import com.lucaoonk.virt_server.Backend.Objects.VM;
import com.lucaoonk.virt_server.Backend.Objects.VMActionObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VMControlHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        InputStreamReader isr =  new InputStreamReader(exchange.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        Gson g = new Gson();
        String value = br.readLine();
        VMActionObject obj = g.fromJson(value, VMActionObject.class);
        VM vm = new VM(obj.name);
        try {

            VMController controller = new VMController();
            if(obj.action.equals("start")){
                controller.startVM(vm);
            }
            if(obj.action.equals("stop")){
                controller.stopVM(vm);
            }

            String response = "";

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            String response = "Something went wrong";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();        
        }

        

    }
    
}
