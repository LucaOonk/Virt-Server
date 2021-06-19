package com.lucaoonk.virt_server.Backend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.google.gson.Gson;
import com.lucaoonk.virt_server.Backend.Objects.Context;

import org.json.simple.JSONObject;


public class Settings {

    private static final String settingsFileLocation = System.getProperty("user.home") + "/Library/Application Support/Virt_Server/settings.json";

    public static Boolean settingsFileExists(){

        File f = new File(settingsFileLocation);
        if(f.exists() && !f.isDirectory()) { 
            return true;
        }else{
            return false;
        }


    }

    public static void readSettingsFile(Context context){

        if(Settings.settingsFileExists()){

            try {
                // create Gson instance
                Gson gson = new Gson();
            
                // create a reader
                Reader reader = Files.newBufferedReader(Paths.get(settingsFileLocation));
            
                // convert JSON file to map
                Map<?, ?> map = gson.fromJson(reader, Map.class);
            
                // print map entries
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if(entry.getKey().equals("port")){
                        context.port = ((Double) entry.getValue()).intValue();
                    }
                }
            
                // close reader
                reader.close();
            
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
        }else{
            System.out.println("Settings-File does not exists. Using defaults");

        }


    }


    public static void writeSettings(Context context){
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("port", context.port);

        File location = new File(settingsFileLocation);
        location.getParentFile().mkdirs();

        FileWriter file;
        try {
            if(Settings.settingsFileExists()){


                    file = new FileWriter(settingsFileLocation, false);

                file.write(jsonObject.toJSONString());
                file.close();       
            }else{

                file = new FileWriter(settingsFileLocation);


                file.write(jsonObject.toJSONString());
                file.close();   
            }
        } catch (IOException e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
    }
}
