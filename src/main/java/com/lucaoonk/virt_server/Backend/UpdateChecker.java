package com.lucaoonk.virt_server.Backend;

import javax.swing.SwingWorker;

import com.lucaoonk.virt_server.Backend.Objects.Context;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class UpdateChecker extends SwingWorker{
    
    private static final String urlToCheck = "https://api.github.com/repos/LucaOonk/Virt-Server/releases/latest";
    private Context context;


    public UpdateChecker(Context context){

        super();
        this.context = context;

        try {
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Boolean isNewewVersionAvailable(){
        return this.doInBackground();

    }

    @Override
    protected Boolean doInBackground() {

        System.out.println("Current: "+context.getVersion());
        try {
            HttpResponse<JsonNode> response = Unirest.get(urlToCheck).asJson();
            JsonNode responseBody = response.getBody();

        if (response.isSuccess()) {
        String latestVersion = responseBody.getObject().get("tag_name").toString();

        System.out.println("Latest version: "+ latestVersion);

        if(context.getVersion().equals(latestVersion)){
            context.updateAvailable = false;
            return false;

        }else{
            
            context.latestVersion = latestVersion;
            context.updateAvailable = true;
            context.textArea.append("[INFO] A new version is available: "+latestVersion+" current version: "+context.getVersion()+"\n");

            return true;


        }

    } else {
        System.out.println("Connection Failed");
        return null;
     }

    }catch(Exception e){

    }
        return null;
    }
}
