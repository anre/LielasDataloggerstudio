package org.lielas.dataloggerstudio.pc.gui.Panels.UsbCube;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.lielas.dataloggerstudio.lib.Logger.Lielas.LielasModel;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerManager;
import org.lielas.dataloggerstudio.pc.CommunicationInterface.UsbCube.UsbCubeSerialInterface;
import org.lielas.dataloggerstudio.pc.bootloader.Bootloader;
import org.lielas.dataloggerstudio.pc.gui.MainFrame;
import org.lielas.dataloggerstudio.pc.gui.MouseOverHintManager;
import org.lielas.dataloggerstudio.pc.gui.Panels.FWUpgradePanel;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Andi on 08.06.2015.
 */
public class UsbCubeFWUpgradePanel extends FWUpgradePanel {
    private MainFrame mainFrame;


    JPanel contentPanel;

    JLabel lblVersion;
    JComboBox<String> cbVersion;

    JButton bttnStart;

    public UsbCubeFWUpgradePanel(MainFrame mainFrame){
        super();

        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new FormLayout(new ColumnSpec[]{
                ColumnSpec.decode("125px"),
                FormFactory.PREF_COLSPEC,
                ColumnSpec.decode("20px"),
                FormFactory.PREF_COLSPEC,
                ColumnSpec.decode("20px"),
                FormFactory.PREF_COLSPEC,
                ColumnSpec.decode("default:grow"),},
                new RowSpec[]{
                        RowSpec.decode("40px"),
                        FormFactory.PREF_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.PREF_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.PREF_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.PREF_ROWSPEC,
                        RowSpec.decode("20px:grow"),
                }));
        add(contentPanel, BorderLayout.CENTER);


        lblVersion = new JLabel("Version");
        contentPanel.add(lblVersion, "2, 2, left, default");

        cbVersion = new JComboBox<String>();
        contentPanel.add(cbVersion, "4, 2, left, default");

        bttnStart = new JButton("Start");
        bttnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BttnStartPressed();
            }
        });
        contentPanel.add(bttnStart, "4, 4, right, default");

    }

    @Override
    public void updateLanguage(LanguageManager lm, MouseOverHintManager hintManager) {

    }

    @Override
    public void updateUIContent() {

    }

    private void BttnStartPressed(){
        UsbCube logger = (UsbCube) LoggerManager.getInstance().getActiveLogger();
        if(!logger.getCommunicationInterface().isOpen()){
            return;
        }

        FWUpgradeWorker fwUpgradeWorker = new FWUpgradeWorker(logger);
        fwUpgradeWorker.execute();


    }


    class FWUpgradeWorker extends SwingWorker<Object, Object>{
        UsbCube logger;

        public FWUpgradeWorker(UsbCube logger){
            this.logger = logger;
        }

        @Override
        protected Object doInBackground() throws Exception {
            Bootloader bl = new Bootloader();

            //read hexfile
            String path = getHexfilePath();
            if(path == null){
                return null;
            }

            System.out.println("reading hex file");
            bl.readHexfile(path);

            System.out.println("format data");
            bl.formatData();


            UsbCubeSerialInterface com = (UsbCubeSerialInterface) logger.getCommunicationInterface();
            com.close();

            bl.connect(com.getPort());

            System.out.println("read bootloader version");
            if(bl.getVersion() == 0){
                if(!bl.startBootloader()){
                    bl.close();
                    com.connect(com.getPort());
                    return null;
                }
            }

            if(bl.getVersion() != 3){
                bl.stopBootloader();
                bl.close();
                com.connect(com.getPort());
                return null;
            }

            System.out.println("read device id");
            if(!bl.readDeviceID()){
                System.out.println("failed to read device id");
                bl.stopBootloader();
                bl.close();
                com.connect(com.getPort());
                return null;
            }


            System.out.println("program memory");
            if(bl.program() == false){
                System.out.println("failed to program memory");
                bl.stopBootloader();
                bl.close();
                com.connect(com.getPort());
                return null;
            }


            System.out.println("verify memory");
            if(bl.verify() == false){
                System.out.println("failed to verify memory");
                bl.stopBootloader();
                bl.close();
                com.connect(com.getPort());
                return null;
            }
            bl.stopBootloader();

            bl.close();
            com.connect(com.getPort());
            return null;
        }

        @Override
        protected void done() {
            super.done();
        }

        private String getHexfilePath(){
            String path = null;

            if(logger.getModelNumber() == 0){
                return null;
            }

            switch (logger.getModelNumber()){
                case LielasModel.MSHT:
                    path = "fw/140/EDLS_140_SHT.hex";
                    break;
                case LielasModel.MSHTAP:
                    path = "fw/140/EDLS_140_SHT_MS5607.hex";
                    break;
                default:
                    return null;
            }

            return path;
        }
    }
}
