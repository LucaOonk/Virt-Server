package com.lucaoonk.virt_server.Backend;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import com.lucaoonk.virt_server.Backend.Objects.Context;

public class LoadingScreenThread extends SwingWorker{

    private JLabel statusLabel;
    private JFrame progressFrame;
    private Context context;

    public LoadingScreenThread(Context context, JLabel statusLabel, JFrame progessFrame){
        super();
        this.context = context;
        this.statusLabel = statusLabel;
        this.progressFrame = progessFrame;
        this.execute();
    }

    @Override
    protected Object doInBackground() throws Exception {
        // define what thread will do here 
        while(!context.loadingIsDone){
            statusLabel.setText(context.loadingStatus);
        }
        statusLabel.setText("Loading Done");

        progressFrame.setVisible(false);

        done();

        String res = "Finished Execution"; 
        return res; 
    }


    @Override
    protected void process(List chunks) 
    { 
        // define what the event dispatch thread  
        // will do with the intermediate results received 
        // while the thread is executing 
        int val = (int) chunks.get(chunks.size() - 1);
          
        statusLabel.setText(String.valueOf(val)); 
    } 

    @Override
    protected void done()  
    { 
        // this method is called when the background  
        // thread finishes execution 
        try 
        { 
            String statusMsg = (String) get();
            statusLabel.setText(statusMsg); 
            progressFrame.setVisible(false);
              
        }  
        catch (InterruptedException e)  
        { 
            e.printStackTrace(); 
        }  
        catch (ExecutionException e)  
        { 
            e.printStackTrace(); 
        } 
    } 
    
}
