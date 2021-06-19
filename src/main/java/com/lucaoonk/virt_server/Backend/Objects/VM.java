package com.lucaoonk.virt_server.Backend.Objects;

import java.util.ArrayList;

public class VM {
    
    private String name;
    private Integer id;
    private Boolean isRunning;
    private String UUID;
    private String cpus;
    private String ram;
    private ArrayList<Device> devices;
    private ArrayList<String> portsForwarded;
    public String vncPort;
    public String vncIP;

    public VM(String name){

        this.name = name;
        this.isRunning = false;    

        this.devices = new ArrayList<Device>();
        this.portsForwarded = new ArrayList<String>();

    }

    public VM(String name, Integer ID){

        this.name = name;
        this.id = ID;
        this.isRunning = true;    
        this.devices = new ArrayList<Device>();
        this.portsForwarded = new ArrayList<String>();

    }

    public void setID(Integer ID){
        this.id = ID;
        this.isRunning = true;

    }

    public Boolean isRunning(){

        return this.isRunning;

    }

    public void updateRunningState(Boolean state){
        this.isRunning = state;
    }


    public String getDomain(){
        return this.name;
    }

    public Integer getID(){
        return this.id;
    }

    public void updateUUID(String UUID){
        this.UUID = UUID;
    }

    public String getUUID(){
        return this.UUID;
    }

    public void updateCPUs(String cpus){
        this.cpus = cpus;
    }

    public String getcpus(){
        return this.cpus;
    }

    public void updateRam(String ram) {
        this.ram = ram;
    }
    public String getRam(){
        return this.ram;
    }

    public void addDevice(Device device){
        this.devices.add(device);
    }

    public ArrayList<Device> getDevices(){
        return this.devices;
    }

    public void addForwardedPort(String port){
        this.portsForwarded.add(port);
    }

    public ArrayList<String> getForwardedPorts(){
        return this.portsForwarded;
    }

    public String vmDetailsTable(){
        String vmDetails = "<html><table><tr><td><b>Property</b></td><td><b>Value</b></td></tr>";
        vmDetails+= "<tr><td>UUID:</td><td>"+this.getUUID()+"</td></tr>";
        vmDetails+= "<tr><td>vnc:</td><td>"+this.vncIP+":"+this.vncPort+"</td></tr>";
        vmDetails+= "<tr><td>CPU's:</td><td>"+this.getcpus()+"</td></tr>";
        String ramAmount = "0";
        if(this.getRam() == null){
            ramAmount = "Undefined";

        }else{
        if(Integer.parseInt(this.getRam()) > 1024){
            ramAmount = ""+Integer.parseInt(this.getRam()) * 1.024E-6;
        }else{
            ramAmount = ""+Integer.parseInt(this.getRam());
        }
        }
        vmDetails+= "<tr><td>Ram in GB:</td><td>"+ramAmount+"</td></tr>";


        String disksString = "";
        int amountOfDisks = 0;
        for (Device dev : this.getDevices()) {
            if(dev.getClass().getName().equals("com.lucaoonk.virt_server.Backend.Objects.Disk")){
                amountOfDisks+=1;
                Disk disk = (Disk) dev;
                disksString+= disk.device + ":"+"<br>"+"&nbsp;Location: "+ disk.source + "<br>&nbsp;Type: "+ disk.driver + "<br><br>";

            }

        }
        vmDetails+= "<tr><td>Attached Disks ("+amountOfDisks+") :</td><td>"+disksString+"</td></tr>";

        String forwardedPorts = "";
        for (String port : this.getForwardedPorts()) {
            forwardedPorts+= port + "<br>";
        }
        vmDetails+= "<tr><td>Forwarded Ports ("+this.getForwardedPorts().size()+")<br> Protocol::External Port:Internal Port :</td><td>"+forwardedPorts+"</td></tr>";

        vmDetails+= "</table></html>";
        return vmDetails;
    }

}
