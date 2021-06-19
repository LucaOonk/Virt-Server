package com.lucaoonk.virsh_server.Backend.Processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lucaoonk.virsh_server.Backend.Objects.Device;
import com.lucaoonk.virsh_server.Backend.Objects.Disk;
import com.lucaoonk.virsh_server.Backend.Objects.VM;
// import com.lucaoonk.virsh_server.CrashReporter.CrashReporter;

public class VMDOMProcessor {
    

    public static void getDetails(VM vm){

        Process process;
        try {

            process = Runtime.getRuntime().exec("/usr/local/bin/virsh dumpxml "+vm.getDomain());//
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            String xmlInfo = "";

            while ((line = reader.readLine()) != null) {

                xmlInfo+=line;

                
            }
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(xmlInfo));
            
            Document doc = builder.parse(src);
            vm.updateUUID(doc.getElementsByTagName("uuid").item(0).getTextContent());

            vm.updateCPUs(doc.getElementsByTagName("vcpu").item(0).getTextContent());

            vm.updateRam(doc.getElementsByTagName("memory").item(0).getTextContent());

            vm.vncIP= doc.getElementsByTagName("graphics").item(0).getAttributes().getNamedItem("listen").getTextContent();
            vm.vncPort= doc.getElementsByTagName("graphics").item(0).getAttributes().getNamedItem("port").getTextContent();

            NodeList element = doc.getElementsByTagName("devices").item(0).getChildNodes();

            
            int length = element.getLength();
            for (int i = 0; i < length; i++) {
                if (element.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) element.item(i);
                    if (el.getNodeName().contains("disk")) {

                        Disk disk = new Disk();
                        disk.device = el.getAttribute("device");
                        disk.type= el.getAttribute("type");
                        disk.source = el.getElementsByTagName("source").item(0).getAttributes().getNamedItem("file").getTextContent();
                        disk.driver = el.getElementsByTagName("driver").item(0).getAttributes().getNamedItem("type").getTextContent();
                        vm.addDevice(disk);
                        


                    }
                }
            }

            NodeList qemuArgs = doc.getElementsByTagName("qemu:commandline").item(0).getChildNodes();

            
            for (int i = 0; i < qemuArgs.getLength(); i++) {
                Node arguments = qemuArgs.item(i);

                if(arguments.getNodeName().contains("qemu:arg")){
                    String[] argumentvalues = arguments.getAttributes().getNamedItem("value").getTextContent().split(",");
                    for (String string : argumentvalues) {

                        if(string.contains("hostfwd")){
                            String[] Ports=string.split("=");
                            vm.addForwardedPort(Ports[1]);

                        }
                    }
                }

                
            }

        } catch (IOException | ParserConfigurationException | SAXException e1) {
            // TODO Auto-generated catch block
            System.out.println(e1.getStackTrace().toString());
        }


    
    }


}

    

