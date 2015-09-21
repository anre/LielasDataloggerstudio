package org.lielas.dataloggerstudio.test.gui;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jssc.SerialPortList;
import org.lielas.dataloggerstudio.lib.CommunicationInterface.CommunicationInterface;
import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.Logger.Lielas.LielasModel;
import org.lielas.dataloggerstudio.lib.Logger.LoggerType;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetItemIds;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.dataloggerstudio.pc.CommunicationInterface.UsbCube.UsbCubeSerialInterface;
import org.lielas.dataloggerstudio.test.worker.ProgramWorker;
import org.lielas.dataloggerstudio.test.worker.TestWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Andi on 29.05.2015.
 */
public class MainFrame extends JFrame {

    UsbCube logger;
    String text;

    private JPanel mainPanel;
    private JLabel lblComPort;
    private JComboBox<String> cbComPort;
    private JButton bttnConnect;
    private JComboBox<LielasModel> cbModel;
    private JButton bttnProgram;
    private JTextPane txtPane;
    private int lastLine;
    private int secondLastLine;

    public MainFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 800);
        setMinimumSize(new Dimension(1024, 800));
        setTitle("lielas Test Studio");

        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245,245,245));
        mainPanel.setLayout(new FormLayout(new ColumnSpec[]{
                ColumnSpec.decode("10dlu"),
                ColumnSpec.decode("40dlu"),
                ColumnSpec.decode("10dlu"),
                ColumnSpec.decode("50dlu"),
                ColumnSpec.decode("10dlu"),
                ColumnSpec.decode("7dlu:grow"),
                ColumnSpec.decode("10dlu")
        }, new RowSpec[]{
                RowSpec.decode("10dlu"),
                FormFactory.PREF_ROWSPEC,
                RowSpec.decode("15dlu"),
                FormFactory.PREF_ROWSPEC,
                RowSpec.decode("16dlu"),
                FormFactory.PREF_ROWSPEC,
                RowSpec.decode("16dlu"),
                FormFactory.PREF_ROWSPEC,
                RowSpec.decode("4dlu:grow"),
                RowSpec.decode("10dlu"),}));

        setContentPane(mainPanel);

        lblComPort = new JLabel("COM-Ports");
        mainPanel.add(lblComPort, "2, 2, left, default");

        cbComPort = new JComboBox<String>();
        mainPanel.add(cbComPort, "4, 2, fill, default");

        bttnConnect = new JButton("Start");
        bttnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectButtonPressed();
            }
        });
        mainPanel.add(bttnConnect, "4, 4, fill, default");

        cbModel = new JComboBox<LielasModel>();
        fillModelList();
        mainPanel.add(cbModel, "4, 6, fill, default");

        bttnProgram = new JButton("Program");
        bttnProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonProgramPressed();
            }
        });
        mainPanel.add(bttnProgram, "4, 8, fill, default");

        txtPane = new JTextPane();
        txtPane.setContentType("text/html");
        text = "";
        mainPanel.add(txtPane, "6, 2, 1, 8, fill, fill");

        getSerialPorts();

        pack();
        revalidate();
        repaint();

    }

    private void fillModelList(){
        ArrayList<LielasModel> list = LielasModel.getList();
        if(list != null && list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                cbModel.addItem(list.get(i));
            }
        }
    }

    private void getSerialPorts(){
        String[] ports = SerialPortList.getPortNames();
        cbComPort.removeAllItems();
        for(int i = 0; i < ports.length; i++){
            cbComPort.addItem(ports[i]);
        }
        cbComPort.setSelectedItem("COM15");
    }

    private void ConnectButtonPressed(){
        text = "";
        txtPane.setText(txtPane.getText() + "Attempting to connect:\n");
        TestWorker cw = new TestWorker(logger, this, cbComPort.getSelectedItem().toString());
        cw.execute();
    }

    private void ButtonProgramPressed(){

        //get model
        LielasModel lm = (LielasModel) cbModel.getSelectedItem();

        text = "";
        txtPane.setText(txtPane.getText() + "Attempting to connect:\n");
        ProgramWorker pw = new ProgramWorker(logger, this, cbComPort.getSelectedItem().toString(), lm);
        pw.execute();
    }

    public void append(String txt){
        if(txt.startsWith("<rm>")){
            if(text.lastIndexOf("<br>")>0){
                text = text.substring(0, secondLastLine);
                txt = txt.substring(4, txt.length());
            }else{
                secondLastLine = lastLine;
            }
        }else{
            secondLastLine = lastLine;
        }
        text = text + txt;
        txtPane.setText("<html>" + text + "</html>");
        lastLine = text.length();
    }

    public void appendln(String txt){
        append(txt + "<br>");
    }


}
