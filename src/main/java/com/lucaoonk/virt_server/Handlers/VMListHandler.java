package com.lucaoonk.virt_server.Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.lucaoonk.virt_server.Backend.Terminal;
import com.lucaoonk.virt_server.Backend.Objects.Context;
import com.lucaoonk.virt_server.Backend.Processors.VMListJson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VMListHandler implements HttpHandler {

    private Context context;

    public VMListHandler(Context context){
        this.context = context;
    }
    
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Long timer = System.currentTimeMillis();

            String requestAuthenticationHeader = "";
            if(exchange.getRequestHeaders().containsKey("Authentication")){
                requestAuthenticationHeader = exchange.getRequestHeaders().get("Authentication").get(0).toString();
            }

            if(requestAuthenticationHeader.equals(context.getHTTPAuth()) || !context.enable_http_auth){
            
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
                Long timeElapsed = (System.currentTimeMillis()- timer);
                if(context.show_gui){
                    context.textArea.append("[INFO] "+timeElapsed+"ms: "+Terminal.getTime()+" Machine list requested"+"\n");
                    context.scrollToBottom();

                }
                context.addRequestTime(timeElapsed);
                System.out.println(Terminal.colorText("[INFO] "+timeElapsed+"ms: "+Terminal.getTime()+" Machine list requested", Terminal.ANSI_BLUE));

            } catch (Exception e) {

                response="Something went wrong";

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

                System.out.println(e.getStackTrace());
            }            
        }else{
            context.textArea.append("[INFO] "+Terminal.getTime()+" Access Denied request \n");

            HTTPAuthenticationHandler.returnAccessDeniedPage(exchange);
        }

    }
}
