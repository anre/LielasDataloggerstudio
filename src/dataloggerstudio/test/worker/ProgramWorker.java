package org.lielas.dataloggerstudio.test.worker;

import org.lielas.dataloggerstudio.lib.Logger.Lielas.LielasModel;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.pc.CommunicationInterface.UsbCube.UsbCubeSerialInterface;
import org.lielas.dataloggerstudio.pc.bootloader.Bootloader;
import org.lielas.dataloggerstudio.pc.bootloader.BootloaderReporter;
import org.lielas.dataloggerstudio.test.gui.MainFrame;

import javax.swing.*;

/**
 * Created by Andi on 12.06.2015.
 */
public class ProgramWorker extends SwingWorker<Object, String> {

    UsbCube logger;
    MainFrame mainFrame;
    String port;
    LielasModel lielasModel;

    public ProgramWorker(UsbCube logger, MainFrame mainFrame, String port, LielasModel lielasModel){
        super();
        this.logger = logger;
        this.mainFrame = mainFrame;
        this.port = port;
        this.lielasModel = lielasModel;
    }

    @Override
    protected Object doInBackground() throws Exception {
        Bootloader bl = new Bootloader();

        bl.setBootloaderReporter(new Reporter());

        int model;

        publish("<font color='green' size='5'>Programming device</font><br><br>");

        //read hexfile
        String path = getHexfilePath();
        if(path == null){
            publish("failed to read hex file<br>");
            return null;
        }

        publish("Reading hex file<br>");
        if(!bl.readHexfile(path)){
            publish("failed to read hex file<br>");
            return null;
        }
        bl.formatData();



        if(logger != null && logger.getCommunicationInterface() != null && logger.getCommunicationInterface().isOpen()){
            logger.getCommunicationInterface().close();
        }

        logger = new UsbCube();
        logger.setCommunicationInterface(new UsbCubeSerialInterface());

        UsbCubeSerialInterface com = (UsbCubeSerialInterface)logger.getCommunicationInterface();

        if(!bl.connect(port)){
            publish("Error connecting to com port <br>");
            System.out.println("not connected");
            return null;
        }

        publish("Reading bootloader version<br>");
        if(bl.getVersion() == 0){
            if(!bl.startBootloader()){
                bl.close();
                return null;
            }
        }

        if(bl.getVersion() != 3){
            bl.stopBootloader();
            bl.close();
            return null;
        }

        publish("Reading logger model number<br>");
        model = bl.getModel();
        if(model != lielasModel.get()){
            publish("Wrong logger model number, write new model number<br>");
            bl.setModel(lielasModel.get());
            try{
                Thread.sleep(10);
            }catch (Exception e){
                e.printStackTrace();
            }
            model = bl.getModel();
            if(model != lielasModel.get()){
                publish("failed to write model number<br>");
                bl.close();
                return null;
            }
        }

        publish("Read device id<br>");

        if(!bl.readDeviceID()){
            publish("failed to read device id<br>");
            bl.stopBootloader();
            bl.close();
            com.connect(com.getPort());
            return null;
        }


        publish("Program memory<br>");
        if(bl.program() == false){
            publish("failed to program memory<br>");
            bl.stopBootloader();
            bl.close();
            com.connect(com.getPort());
            return null;
        }


        publish("Verify memory<br>");
        if(bl.verify() == false){
            publish("failed to verify memory<br>");
            bl.stopBootloader();
            bl.close();
            com.connect(com.getPort());
            return null;
        }


        publish("<br><font color='green' size='5'>Programming successfull, device started</font><br><br>");

        bl.stopBootloader();

        bl.close();
        return null;
    }

    @Override
    protected void done() {
        super.done();
    }

    @Override
    protected void process(java.util.List<String> msg){
        if(msg == null){
            System.out.println("abort");
            return;
        }

        for(int i = 0; i < msg.size(); i++){
            String s = msg.get(i);
            mainFrame.append(s);
        }
    }

    private String getHexfilePath(){
        String path = null;

        if(lielasModel.get() == 0){
            return null;
        }

        switch (lielasModel.get()){
            case LielasModel.MSHT:
                path = "fw/140/EDLS_140_SHT.hex";
                break;
            case LielasModel.MSHTAP:
                path = "fw/140/EDLS_140_SHT_MS5607.hex";
                break;
            case LielasModel.M1I:
                path = "fw/140/EDLS_140_EDLSI.hex";
                break;
            case LielasModel.M1PT1K:
                path = "fw/140/EDLS_140_1PT1K.hex";
            break;
            case LielasModel.M1U:
                path = "fw/140/EDLS_140_EDLSU.hex";
                break;
            case LielasModel.M2PT1K:
                path = "fw/140/EDLS_140_2PT1K.hex";
                break;
            default:
                return null;
        }

        return path;
    }

    public void publishLog(String str){
        publish(str);
    }

    private class Reporter extends BootloaderReporter{
        @Override
        public void publish(String str) {
            publishLog(str);
        }
    }
}
