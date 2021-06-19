package com.lucaoonk.virsh_server.Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.google.gson.Gson;
import com.lucaoonk.virsh_server.Backend.Objects.VM;
import com.lucaoonk.virsh_server.Backend.Objects.VMDetailsRequest;
import com.lucaoonk.virsh_server.Backend.Processors.VMDOMProcessor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.json.simple.JSONObject;

public class VMDetailHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        InputStreamReader isr =  new InputStreamReader(exchange.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        Gson g = new Gson();
        String value = br.readLine();
        VMDetailsRequest obj = g.fromJson(value, VMDetailsRequest.class);

        VM vm = new VM(obj.vmname);
        try {
            VMDOMProcessor.getDetails(vm);
            String response = g.toJson(vm);
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
