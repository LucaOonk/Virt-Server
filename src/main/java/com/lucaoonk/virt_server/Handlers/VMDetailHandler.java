package com.lucaoonk.virt_server.Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.lucaoonk.virt_server.Backend.Terminal;
import com.lucaoonk.virt_server.Backend.Objects.Context;
import com.lucaoonk.virt_server.Backend.Objects.VM;
import com.lucaoonk.virt_server.Backend.Objects.VMDetailsRequest;
import com.lucaoonk.virt_server.Backend.Processors.VMDOMProcessor;
import com.lucaoonk.virt_server.Backend.Processors.VMListProcessor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.json.simple.JSONObject;

public class VMDetailHandler implements HttpHandler {

    private Context context;

    public VMDetailHandler(Context context){
        this.context = context;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Long timer = System.currentTimeMillis();

        
        InputStreamReader isr =  new InputStreamReader(exchange.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        Gson g = new Gson();
        String value = br.readLine();
        VMDetailsRequest obj = g.fromJson(value, VMDetailsRequest.class);

        VM vm = new VM(obj.name);
        try {
            VMDOMProcessor.getDetails(vm);
            checkVMState(vm);
            String response = g.toJson(vm);

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();

            os.write(response.getBytes());
            os.close();

            Long timeElapsed = (System.currentTimeMillis()- timer);

            if(context.show_gui){
                context.textArea.append("[INFO] "+timeElapsed+"ms: "+Terminal.getTime()+" Machine details request: "+obj.name+"\n");
                context.scrollToBottom();

            }
            context.addRequestTime(timeElapsed);

            System.out.println(Terminal.colorText("[INFO] "+timeElapsed+"ms: "+Terminal.getTime()+" Machine details request: "+obj.name, Terminal.ANSI_BLUE));

        } catch (Exception e) {
            String response = "Something went wrong";


            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();       
            Long timeElapsed = (System.currentTimeMillis()- timer);
            if(context.show_gui){
                context.textArea.append("[ERROR] "+timeElapsed+"ms: "+Terminal.getTime()+" Something went wrong while getting machine details: "+obj.name+"\n");
                context.scrollToBottom();

            }
            context.addRequestTime(timeElapsed);

            System.out.println(Terminal.colorText("[ERROR] "+timeElapsed+"ms: "+Terminal.getTime()+" Something went wrong while getting machine details: "+obj.name, Terminal.ANSI_RED));
            e.printStackTrace();
 
        }


    }

    private void checkVMState(VM inVm){

        ArrayList<VM> vmlist;
        try {
            vmlist = VMListProcessor.getVMdomainList();
            for (VM vm2 : vmlist) {
                if(vm2.getDomain().equals(inVm.getDomain())){
    
                    inVm.updateRunningState(vm2.isRunning());
                    break;
                }
                
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
