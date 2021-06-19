package com.lucaoonk.virsh_server.Backend.Processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.lucaoonk.virsh_server.Backend.Objects.Context;
import com.lucaoonk.virsh_server.Backend.Objects.VM;

public class VMListProcessor {

    private ArrayList<VM> vmList;
    private Context context;

    public VMListProcessor(Context context){
        this.vmList = new ArrayList<VM>();
        this.context = context;
    }

    public void runCommand() throws IOException{
        this.vmList = getVMdomainList();
        
        context.updateVMList(vmList);
    }


    public static ArrayList<VM> getVMdomainList() throws IOException {

        Process process = Runtime.getRuntime().exec("/usr/local/bin/virsh list --all --id --name");


        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";

        ArrayList<VM> vmList = new ArrayList<VM>();

        while ((line = reader.readLine()) != null) {
            String[] templine=line.split("\\s+");


            if(line.equals("")){
                break;
            }else{
                if(templine[0].equals("-")){
                    vmList.add(new VM(templine[1]));


                }else{
                    vmList.add(new VM(templine[1], Integer.parseInt(templine[0])));

                }

            }
        }

        // for (VM vm : vmList) {
        //     VMDOMProcessor.getDetails(vm);
        // }

        return vmList;
    }
    
}