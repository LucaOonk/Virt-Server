package com.lucaoonk.virt_server.Backend.Objects;

import java.util.ArrayList;

import com.lucaoonk.virt_server.Backend.Processors.VMListProcessor;

public class Context {
    
    public static String latestVersion;
    private ArrayList<VM> vmList;
    private VM currentSelectedVM;
    public String defaultSaveLocation;
    private static final String versionString = "0.5";
    public Boolean checkForUpdates;
    private String applicationDefaultSaveLocation;
    public Integer windowHeight;
    public Integer windowWidth;
    public boolean autoSizeWindow;
    public boolean autoRefresh;
    public long autoRefreshRate;
    public int port;
    public boolean loadingIsDone;
    public String loadingStatus;


    public Context(){
        initDefaults();
        this.autoRefresh = true;
        this.autoRefreshRate =10;
    }

    private void initDefaults(){
        this.port = 8000;
        this.checkForUpdates = true;
        this.defaultSaveLocation=System.getProperty("user.home")+"/vms/";
        this.applicationDefaultSaveLocation=System.getProperty("user.home")+"/vms/";

    }

    public void refresh(){
        VMListProcessor processor = new VMListProcessor(this);
            
        try {
            
            this.vmList = processor.getVMdomainList();

            VMDOMProcessorThread domThread = new VMDOMProcessorThread(this);
        }catch(Exception e){
            
        }
    }

    public void updateVMList(ArrayList<VM> vmList){
        this.vmList= vmList;
        // defaultSaveLocation= "";
        this.currentSelectedVM = vmList.get(0);
    }

    public ArrayList<VM> getVMList(){
        return this.vmList;
    }

    public static String getVersion(){
        return versionString;
    }

    public String getDefaultSaveLocation() {

        if(defaultSaveLocation.equals("")){

            return applicationDefaultSaveLocation;

        }else{

            return defaultSaveLocation;
        }

    }
}
