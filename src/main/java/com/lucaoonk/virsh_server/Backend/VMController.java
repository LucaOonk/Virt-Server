package com.lucaoonk.virsh_server.Backend;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.SwingWorker;

import com.lucaoonk.virsh_server.Backend.Objects.Context;
import com.lucaoonk.virsh_server.Backend.Objects.VM;

public class VMController {

    private Context context;
    private VM vm;

    public VMController(Context context){
        this.context = context;
    }

    public void connectToVM(VM vm){

        final VM vmToConnect = vm;
        final Context finalContext = context;
        VMController.startVMConnection(vmToConnect, finalContext);
    }

    public void startVM(VM vm){
        final VM vmToStart = vm;

        VMController.startVMthread(vmToStart, context);

    }

    public void stopVM(VM vm){

        final VM vmToStop = vm;
        VMController.stopVMthread(vmToStop, context);

    }

    public void destroyVM(VM vm){

        final VM vmToStop = vm;
        VMController.destroyVM(vmToStop, context);

    }

    public static String createVMDiskthread(String vmDomain, Context context, String size, String diskFileLocation) 
    {
        final String vmDomain2 = vmDomain;
        final Context context2 = context;
        final String sizeString = size;
        final String diskFileLocation2 = diskFileLocation;
        final String defeninitivePath;
        if(diskFileLocation2.equals("")){
            
            defeninitivePath=  context2.getDefaultSaveLocation()+vmDomain2+"/"+ vmDomain2+".qcow2";

        }else{
            defeninitivePath=  diskFileLocation2+ vmDomain2+".qcow2";

        }


        File myFile = new File(defeninitivePath);
        myFile.getParentFile().mkdirs();

        // define what thread will do here
        if(diskFileLocation2.equals("")){
            try {

                 Process process = Runtime.getRuntime().exec("/usr/local/bin/qemu-img create -f qcow2 "+context2.getDefaultSaveLocation()+vmDomain2+"/"+ vmDomain2+".qcow2 +"+sizeString);

            } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        
            }else{
                try {
                    Process process = Runtime.getRuntime().exec("/usr/local/bin/qemu-img create -f qcow2 "+diskFileLocation2+ vmDomain2+".qcow2 +"+sizeString);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        return defeninitivePath;

    }

    private static void startVMthread(VM vm, Context context) 
    {
        final VM vmToStart = vm;
        final Context context2 = context;

        SwingWorker sw1 = new SwingWorker() 
        {
  
            @Override
            protected String doInBackground() throws Exception 
            {
                // define what thread will do here
                if(!vmToStart.isRunning()){
                    try {
                        Process process = Runtime.getRuntime().exec("/usr/local/bin/virsh start "+vmToStart.getDomain());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
        
                }else{
                    System.out.println("Machine is running");
                }
                return "done";
            }
  
            @Override
            protected void process(List chunks)
            {
                // define what the event dispatch thread 
                // will do with the intermediate results received
                // while the thread is executing

            }
  
            @Override
            protected void done() 
            {
                // this method is called when the background 
                // thread finishes execution
                vmToStart.updateRunningState(true);

                context2.refresh();

            }
        };
          
        // executes the swingworker on worker thread
        sw1.execute(); 
    }

    private static void stopVMthread(VM vm, Context context) 
    {
        final VM vmToStop = vm;
        final Context context2 = context;

        SwingWorker sw1 = new SwingWorker() 
        {
  
            @Override
            protected String doInBackground() throws Exception 
            {
                // define what thread will do here

                if(vmToStop.isRunning()){
                    try {
                        Process process = Runtime.getRuntime().exec("/usr/local/bin/virsh shutdown "+vmToStop.getDomain());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
        
                }else{
                    System.out.println("Machine is already stopped");
                }
                return "done";
            }
  
            @Override
            protected void process(List chunks)
            {
                // define what the event dispatch thread 
                // will do with the intermediate results received
                // while the thread is executing

            }
  
            @Override
            protected void done() 
            {
                // this method is called when the background 
                // thread finishes execution
                vmToStop.updateRunningState(false);
                context2.refresh();

            }
        };
          
        // executes the swingworker on worker thread
        sw1.execute(); 
    }

    private static void startVMConnection(VM vm, Context context) 
    {
        final VM vmToConnect =vm;
        final Context context2 = context;

        SwingWorker sw1 = new SwingWorker() 
        {
  
            @Override
            protected String doInBackground() throws Exception 
            {
                // define what thread will do here

                if(vmToConnect.isRunning()){
                    try {
                        Process process = Runtime.getRuntime().exec("/usr/local/bin/virt-viewer "+vmToConnect.getDomain());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
        
                }else{
                    System.out.println("Machine is not running");
                }
                return "done";
            }
  
            @Override
            protected void process(List chunks)
            {
                // define what the event dispatch thread 
                // will do with the intermediate results received
                // while the thread is executing

            }
  
            @Override
            protected void done() 
            {
                // this method is called when the background 
                // thread finishes execution

                context2.refresh();

            }
        };
          
        // executes the swingworker on worker thread
        sw1.execute(); 
    }
    public static void destroyVM(VM vm, Context context) 
    {
        final VM vmToConnect =vm;
        final Context context2 = context;

        SwingWorker sw1 = new SwingWorker() 
        {
  
            @Override
            protected String doInBackground() throws Exception 
            {
                // define what thread will do here

                if(vmToConnect.isRunning()){
                    try {
                        Process process = Runtime.getRuntime().exec("/usr/local/bin/virsh destroy "+vmToConnect.getDomain());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
        
                }else{
                    System.out.println("Machine is not running");
                }
                return "done";
            }
  
            @Override
            protected void process(List chunks)
            {
                // define what the event dispatch thread 
                // will do with the intermediate results received
                // while the thread is executing

            }
  
            @Override
            protected void done() 
            {
                // this method is called when the background 
                // thread finishes execution

                context2.refresh();

            }
        };
          
        // executes the swingworker on worker thread
        sw1.execute(); 
    }

}
