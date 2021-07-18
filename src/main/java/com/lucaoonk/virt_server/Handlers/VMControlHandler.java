package com.lucaoonk.virt_server.Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.google.gson.Gson;
import com.lucaoonk.virt_server.Backend.Terminal;
import com.lucaoonk.virt_server.Backend.VMController;
import com.lucaoonk.virt_server.Backend.Objects.Context;
import com.lucaoonk.virt_server.Backend.Objects.VM;
import com.lucaoonk.virt_server.Backend.Objects.VMActionObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VMControlHandler implements HttpHandler {


    private Context context;
    public VMControlHandler(Context context){
        this.context = context;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Long timer = System.currentTimeMillis();

        InputStreamReader isr =  new InputStreamReader(exchange.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        Gson g = new Gson();
        String value = br.readLine();
        VMActionObject obj = g.fromJson(value, VMActionObject.class);
        VM vm = new VM(obj.name);
        try {

            VMController controller = new VMController();
            if(obj.action.equals("start")){

                if(context.show_gui){
                    context.textArea.append("[INFO] "+Terminal.getTime()+" Machine start request: "+obj.name+"\n");
                    context.scrollToBottom();
                }
                System.out.println(Terminal.colorText("[INFO] "+Terminal.getTime()+" Machine start request: "+obj.name, Terminal.ANSI_BLUE));
                controller.startVM(vm);
            }
            if(obj.action.equals("stop")){
                if(context.show_gui){
                    context.textArea.append("[INFO] "+Terminal.getTime()+" Machine stop request: "+obj.name+"\n");
                    context.scrollToBottom();

                }
                System.out.println(Terminal.colorText("[INFO] "+Terminal.getTime()+" Machine stop request: "+obj.name, Terminal.ANSI_BLUE));

                controller.stopVM(vm);
            }

            String response = "";

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            Long timeElapsed = (System.currentTimeMillis()- timer);
            if(context.show_gui){
                context.textArea.append("[ERROR] "+timeElapsed+"ms: "+Terminal.getTime()+" Something went wrong while requesting vm list"+"\n");
                context.scrollToBottom();

            }
            context.addRequestTime(timeElapsed);

            System.out.println(Terminal.colorText("[ERROR] "+timeElapsed+"ms: "+Terminal.getTime()+" Something went wrong while requesting vm list", Terminal.ANSI_RED));

        } catch (Exception e) {
            String response = "Something went wrong";

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();        

            Long timeElapsed = (System.currentTimeMillis()- timer);

            if(context.show_gui){
                context.textArea.append("[ERROR] "+Terminal.getTime()+" Something went wrong while starting/stoping machine: "+obj.name+"\n");
                context.scrollToBottom();

            }
            context.addRequestTime(timeElapsed);

            System.out.println(Terminal.colorText("[ERROR] "+Terminal.getTime()+" Something went wrong while starting/stoping machine: "+obj.name, Terminal.ANSI_RED));

        }

        

    }
    
}
