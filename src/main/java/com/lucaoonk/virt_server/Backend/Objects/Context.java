package com.lucaoonk.virt_server.Backend.Objects;

import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import com.lucaoonk.virt_server.Backend.Processors.VMListProcessor;

public class Context {
    
    public static String latestVersion;
    public static boolean updateAvailable;
    private ArrayList<VM> vmList;
    private VM currentSelectedVM;
    public String defaultSaveLocation;
    private static final String versionString = "0.5.2";
    public Boolean checkForUpdates;
    private String applicationDefaultSaveLocation;
    public Integer windowHeight;
    public Integer windowWidth;
    public boolean autoSizeWindow;
    public boolean autoRefresh;
    public long autoRefreshRate;
    public int port;
    public boolean show_gui;
    public JFrame frame;
    public JTextArea textArea;
    public JScrollPane scrollPanel;
    public ArrayList<Long> requestTimes;

    public void scrollToBottom(){
        JScrollBar vertical = this.scrollPanel.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }
    public boolean loadingIsDone;
    public String loadingStatus;


    public Context(){
        initDefaults();
        this.autoRefresh = true;
        this.autoRefreshRate =10;
    }

    private void initDefaults(){
        this.port = 8000;
        this.show_gui = false;
        this.requestTimes = new ArrayList<Long>();
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

    public void addRequestTime(Long time){
        this.requestTimes.add(time);
    }

    public Long getRequestTime(){
        if(this.requestTimes.size() > 5){
            long t1 = requestTimes.get(requestTimes.size() - 1);
            long t2 = requestTimes.get(requestTimes.size() - 2);
            long t3 = requestTimes.get(requestTimes.size() - 3);
            long t4 = requestTimes.get(requestTimes.size() - 4);
            long t5 = requestTimes.get(requestTimes.size() - 5);

            return (t1 + t2 + t3 + t4 + t5)/5;
        }

        return requestTimes.get(requestTimes.size() - 1);
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
