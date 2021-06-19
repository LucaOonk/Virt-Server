package com.lucaoonk.virt_server.Backend.Processors;

import java.io.IOException;
import java.util.ArrayList;

import com.lucaoonk.virt_server.Backend.Objects.VM;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class VMListJson {

    public static String getVMList(){

        JSONObject jsonObject = new JSONObject();
        JSONArray vmArray = new JSONArray();

        ArrayList<VM> vmList;

        try {
            vmList = VMListProcessor.getVMdomainList();
            for (VM vm : vmList) {

                JSONObject vmObject = new JSONObject();
                vmObject.put("name", vm.getDomain());
                vmObject.put("id", vm.getID());
                vmArray.add(vmObject);
    
            }
            jsonObject.put("vms", vmArray);
            return jsonObject.toJSONString();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }


}
