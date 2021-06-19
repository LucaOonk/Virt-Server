package com.lucaoonk.virsh_server.Backend.Objects;

import javax.swing.SwingWorker;

import com.lucaoonk.virsh_server.Backend.Objects.Context;
import com.lucaoonk.virsh_server.Backend.Objects.VM;
import com.lucaoonk.virsh_server.Backend.Processors.VMDOMProcessor;

public class VMDOMProcessorThread extends SwingWorker{

    private Context context;

    public VMDOMProcessorThread(Context context){
        super();
        this.context = context;
        this.execute();
    }

    @Override
    protected Object doInBackground() throws Exception {
        for (VM vm : context.getVMList()) {
            VMDOMProcessor.getDetails(vm);

        }

        return null;
    }
    
}
