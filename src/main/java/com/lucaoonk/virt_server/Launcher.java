package com.lucaoonk.virt_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import com.lucaoonk.virt_server.Backend.Settings;
import com.lucaoonk.virt_server.Backend.Terminal;
import com.lucaoonk.virt_server.Backend.Objects.Context;
import com.lucaoonk.virt_server.Backend.Objects.VM;
import com.lucaoonk.virt_server.Backend.Processors.VMListJson;
import com.lucaoonk.virt_server.Backend.Processors.VMListProcessor;
import com.lucaoonk.virt_server.Handlers.RootHandler;
import com.lucaoonk.virt_server.Handlers.VMControlHandler;
import com.lucaoonk.virt_server.Handlers.VMDetailHandler;
import com.lucaoonk.virt_server.Handlers.VMListHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Launcher{
    
    public static void main(String[] args) throws Exception {
        Context context = new Context();
        context.loadingIsDone=false;
        context.loadingStatus="Initializing & Checking dependencies";
        LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.showLoadingScreen(context);
        init();

        context.loadingStatus="Reading Settings...";
        Settings.readSettingsFile(context);
        Settings.writeSettings(context);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(context.port), 0);
            System.out.println(Terminal.colorText("[INITIALIZED] "+Terminal.getTime()+" Running on Port: "+context.port, Terminal.ANSI_GREEN));

            server.createContext("/vm/details", new VMDetailHandler());
            server.createContext("/vm", new VMControlHandler());
            server.createContext("/list", new VMListHandler());
            server.createContext("/", new RootHandler());
    
            server.setExecutor(null); // creates a default executor
            server.start();
            context.loadingIsDone = true;
        } catch (Exception e) {
            System.out.println(Terminal.colorText("[ERROR] "+Terminal.getTime()+" Port Already in use", Terminal.ANSI_RED));

            System.exit(1);
        }

    }

    private static void init(){

        checkHomebrewInstall();
        checkRequired();

        // Process process = Runtime.getRuntime().exec("brew install qemu gcc libvirt");


        
    }

    private static void checkRequired() {
        Process process;
        try {
            process = Runtime.getRuntime().exec("/usr/local/bin/brew install qemu gcc libvirt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
        
            while ((line = reader.readLine()) != null) {    
    
                System.out.println(line);

                if(line.equals("")){
                    
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                }else{

                    System.out.println(line);
                    
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void checkHomebrewInstall(){
        Process process;
        try {
            process = Runtime.getRuntime().exec("which brew");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
    
    
            while ((line = reader.readLine()) != null) {
                String[] templine=line.split("\\s+");
    
    
                if(line.equals("")){
                    
                    System.out.println("Homebrew not installed, installing it now:\n");


                    Process installHomeBrewProcess = Runtime.getRuntime().exec("/bin/bash -c '$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)'");
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(installHomeBrewProcess.getInputStream()));
                    String line2 = "";
                    while ((line2 = reader2.readLine()) != null) {
                        System.out.println(line);
                    }

                }else{
                    System.out.println("Homebrew installed:"+line);
    
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
            BufferedReader br = new BufferedReader(isr);
            String value = br.readLine();
            
            String response = "This is the response" + " | Request type: "+t.getRequestMethod()+" | Request Body: "+value;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}