package com.lucaoonk.virsh_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import com.lucaoonk.virsh_server.Backend.Objects.VM;
import com.lucaoonk.virsh_server.Backend.Processors.VMListJson;
import com.lucaoonk.virsh_server.Backend.Processors.VMListProcessor;
import com.lucaoonk.virsh_server.Handlers.RootHandler;
import com.lucaoonk.virsh_server.Handlers.VMDetailHandler;
import com.lucaoonk.virsh_server.Handlers.VMListHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Launcher{
    
    public static void main(String[] args) throws Exception {
        init();
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/vm/details", new VMDetailHandler());
        server.createContext("/list", new VMListHandler());
        server.createContext("/", new RootHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
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
                    System.out.println("Depencencies already installed\n");

                }else{

                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
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