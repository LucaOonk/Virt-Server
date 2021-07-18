package com.lucaoonk.virt_server.Handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.lucaoonk.virt_server.Backend.Objects.Context;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RootHandler implements HttpHandler {

    private Context context;
    public RootHandler(Context context){
        this.context = context;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String response = "<html><h1>Virt Server</h1>Version: "+context.getVersion()+"</html>";

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();        
    }
    
}
