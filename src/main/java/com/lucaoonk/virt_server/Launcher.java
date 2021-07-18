package com.lucaoonk.virt_server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import com.lucaoonk.virt_server.Backend.Settings;
import com.lucaoonk.virt_server.Backend.Terminal;
import com.lucaoonk.virt_server.Backend.Objects.Context;
import com.lucaoonk.virt_server.Handlers.RootHandler;
import com.lucaoonk.virt_server.Handlers.VMControlHandler;
import com.lucaoonk.virt_server.Handlers.VMDetailHandler;
import com.lucaoonk.virt_server.Handlers.VMListHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Launcher{
    private JPanel statsPanel;

    public static void main(String[] args) throws Exception {
        Context context = new Context();
        context.loadingIsDone=false;
        context.loadingStatus="Initializing & Checking dependencies";
        LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.showLoadingScreen(context);
        init();

        context.loadingStatus="Reading Settings...";
        Settings.readSettingsFile(context);

        // if(context.checkForUpdates){
        //     UpdateChecker update = new UpdateChecker(context);
        //     update.isNewewVersionAvailable();
        // }

        Settings.writeSettings(context);

        if(context.show_gui){
            System.out.println(Terminal.colorText("[INITIALIZING] "+Terminal.getTime()+" Show Gui set, initializing Gui", Terminal.ANSI_GREEN));

            initGui(context);
        }else{
            System.out.println(Terminal.colorText("[INITIALIZING] "+Terminal.getTime()+" Show Gui not set, running headless", Terminal.ANSI_GREEN));

        }

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(context.port), 0);
            System.out.println(Terminal.colorText("[INITIALIZED] "+Terminal.getTime()+" Running on Port: "+context.port, Terminal.ANSI_GREEN));
            if(context.show_gui){
                context.textArea.append("[INITIALIZED] "+Terminal.getTime()+" Running on Port: "+context.port+"\n");
            }
            server.createContext("/vm/details", new VMDetailHandler(context));
            server.createContext("/vm", new VMControlHandler(context));
            server.createContext("/list", new VMListHandler(context));
            server.createContext("/", new RootHandler(context));
    
            server.setExecutor(null); // creates a default executor
            server.start();
            context.loadingIsDone = true;
        } catch (Exception e) {
            if(context.show_gui){
                context.textArea.append("[ERROR] "+Terminal.getTime()+" Port Already in use"+"\n");
            }
            System.out.println(Terminal.colorText("[ERROR] "+Terminal.getTime()+" Port Already in use", Terminal.ANSI_RED));

            System.exit(1);
        }

    }

    private static void initGui(Context context){
        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        context.frame = frame;
        frame.setSize(600,300);
        context.textArea = new JTextArea();
        context.scrollPanel = new JScrollPane();
        context.textArea.setSize(new Dimension(600, 400));
        context.scrollPanel.setViewportView(context.textArea);
        context.textArea.setEditable(false);
        context.textArea.setAutoscrolls(true);
        context.scrollPanel.setAutoscrolls(true);
        // context.textArea.setBackground(Color.BLACK);

        mainPanel.add(context.scrollPanel, BorderLayout.CENTER);
        // mainPanel.add(statisticsPanel(), BorderLayout.SOUTH);

        context.textArea.setLineWrap(true);
        frame.setTitle("Virt Server Console");
        frame.setLocationRelativeTo(null);
        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private static void init(){

        checkHomebrewInstall();
        checkRequired();

        // Process process = Runtime.getRuntime().exec("brew install qemu gcc libvirt");

    }



    // private static JPanel statisticsPanel(){
    //     JPanel panel = new JPanel();
        
    //     panel.add(new JLabel(c));


    //     return panel;
    // }

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