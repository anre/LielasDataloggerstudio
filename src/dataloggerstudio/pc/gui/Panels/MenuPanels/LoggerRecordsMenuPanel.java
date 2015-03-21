/*
Copyright (c) 2015, Andreas Reder
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of LielasDataloggerstudio nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.lielas.dataloggerstudio.pc.gui.Panels.MenuPanels;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerManager;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.dataloggerstudio.lib.LoggerRecordManager;
import org.lielas.dataloggerstudio.pc.CommunicationInterface.UsbCube.UsbCubeSerialInterface;
import org.lielas.dataloggerstudio.pc.gui.BodyButton;
import org.lielas.dataloggerstudio.pc.gui.MainFrame;
import org.lielas.dataloggerstudio.pc.gui.MouseOverHintManager;
import org.lielas.dataloggerstudio.pc.gui.Panels.DataContentPanels.DataContentPanel;
import org.lielas.dataloggerstudio.pc.gui.Toast.Toast;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Created by Andi on 06.01.2015.
 */
public class LoggerRecordsMenuPanel extends MenuPanel{

    JLabel lblHeader;

    JTree treeLogfiles;
    DefaultMutableTreeNode rootNode;
    DefaultTreeModel treeModel;

    BodyButton bttnLoadData;
    private DataContentPanel dcp;
    JLabel lblDownloadStatus;
    JLabel lblDownloadStatusContent;
    int lrCount;

    public LoggerRecordsMenuPanel(MainFrame mainFrame){
        super(mainFrame);

        this.setOpaque(false);
        this.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
        this.mainFrame = mainFrame;
        dcp = null;

        int row = 2;
        lrCount = 0;

        setLayout(new FormLayout(
                new ColumnSpec[]{
                        ColumnSpec.decode("20px"),
                        FormFactory.PREF_COLSPEC,
                        ColumnSpec.decode("20px"),
                        FormFactory.PREF_COLSPEC,
                        ColumnSpec.decode("15px:grow"),},
                new RowSpec[] {
                        RowSpec.decode("10px"),
                        FormFactory.PREF_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.PREF_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.PREF_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.PREF_ROWSPEC,
                        RowSpec.decode("20px"),
                        FormFactory.PREF_ROWSPEC,
                        RowSpec.decode("20px:grow"),}));


        lblHeader = new JLabel("");
        lblHeader.setFont(fontBold);
        add(lblHeader, getLayoutString(2, row, "left", "center"));
        row += 2;


        rootNode = new DefaultMutableTreeNode("Logfiles");
        treeModel = new DefaultTreeModel(rootNode);
        treeLogfiles = new JTree(treeModel);
        treeLogfiles.setOpaque(false);
        treeLogfiles.setCellRenderer(new TreeCellRenderer());
        treeLogfiles.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                        TreeSelectionChanged(e);
            }
        });

        add(treeLogfiles, "2, " + Integer.toString(row) + ", 3, 1, left, top");
        row += 2;



        bttnLoadData = new BodyButton("");
        bttnLoadData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BttnLoadDataPressed();
            }
        });
        bttnLoadData.setFont(fontBttn);
        add(bttnLoadData, getLayoutString(4, row, "right", "center"));
        row += 2;

        lblDownloadStatus = new JLabel("");
        add(lblDownloadStatus, getLayoutString(2, row, "left", "center"));

        lblDownloadStatusContent = new JLabel("");
        add(lblDownloadStatusContent, getLayoutString(4, row, "left", "center"));


    }


    @Override
    public void updateLanguage(LanguageManager lm, MouseOverHintManager hintManager) {
        lblHeader.setText(lm.getString(986));
        bttnLoadData.setText(lm.getString(988));
        lblDownloadStatus.setText(lm.getString(1091));
        rootNode.setUserObject(lm.getString(1090));
    }

    @Override
    public void updateUIContent() {
        UsbCube logger = (UsbCube)LoggerManager.getInstance().getActiveLogger();
        LoggerRecord lr;
        DefaultMutableTreeNode loggerTreeNode;


        if(logger != null) {

            if(logger.getRecordCount() == 0){
                rootNode.removeAllChildren();
                treeModel.reload();
            }

            for (int index = 0; index < logger.getRecordCount(); index++) {

                try {
                    loggerTreeNode = (DefaultMutableTreeNode) rootNode.getChildAt(index);
                }catch(Exception e){
                    loggerTreeNode = null;
                }


                if(loggerTreeNode == null){
                    //create new entry
                    lr = logger.getRecordset(index);
                    if (lr != null) {
                        loggerTreeNode = new DefaultMutableTreeNode(lr);
                        DefaultMutableTreeNode start = new DefaultMutableTreeNode("Start: " + lr.getDatetimeString());
                        loggerTreeNode.add(start);
                        DefaultMutableTreeNode end = new DefaultMutableTreeNode("End: " + lr.getEndDatetimeString());
                        loggerTreeNode.add(end);
                        DefaultMutableTreeNode samplerate = new DefaultMutableTreeNode("Samplerate: " + lr.getSampleRateString());
                        loggerTreeNode.add(samplerate);
                        DefaultMutableTreeNode samples = new DefaultMutableTreeNode("Samples: " + Long.toString(lr.getEndIndex() - lr.getStartIndex() + 1));
                        loggerTreeNode.add(samples);

                    } else {
                        loggerTreeNode = new DefaultMutableTreeNode("logfile" + Integer.toString(index));
                    }
                    rootNode.add(loggerTreeNode);
                    treeModel.reload();
                    treeLogfiles.expandPath(new TreePath(rootNode.getPath()));
                }
            }


        }

    }

    private void BttnLoadDataPressed(){
        LoggerRecord lr;
        LoggerRecordManager lrm = LoggerRecordManager.getInstance();

        //get selected loggerrecord
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeLogfiles.getLastSelectedPathComponent();
        if(node == null){
            Toast t = new Toast(mainFrame, "You have to select a logfile to download its data");
            return;
        }
        if(node.getChildCount() == 0){
            node = (DefaultMutableTreeNode)node.getParent();
        }

        try {
            lr = (LoggerRecord) node.getUserObject();
            lr = lrm.get(lr.getIndex());
        }catch (Exception e){
            return;
        }

        if(lr == null){
            return;
        }


        LoadDataWorker loadDataWorker = new LoadDataWorker(this, lr);
        loadDataWorker.execute();
    }

    public void setDataContentPanel(DataContentPanel dcp){
        this.dcp = dcp;
    }

    private void TreeSelectionChanged(TreeSelectionEvent e){
        LoggerRecord lr;

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeLogfiles.getLastSelectedPathComponent();
        if(node == null){
            return;
        }
        if(node.getChildCount() == 0){
            node = (DefaultMutableTreeNode)node.getParent();
        }
        try {
            lr = (LoggerRecord) node.getUserObject();
        }catch (Exception ex){
            return;
        }

        if(lr != null){
            LoggerRecordManager.getInstance().select(lr.getIndex());
        }
        mainFrame.updateUIContent();
    }

    private class SampleProgress{
        public final long samples;
        public final long maxSamples;

        SampleProgress(long samples, long maxSamples){
            this.samples = samples;
            this.maxSamples = maxSamples;
        }
    }

    class LoadDataWorker extends SwingWorker<LoggerRecord, SampleProgress>{
        JPanel parent;
        LoggerRecord lr;

        public LoadDataWorker(JPanel panel, LoggerRecord lr){
            parent = panel;
            this.lr = lr;
        }

        @Override
        protected LoggerRecord doInBackground() throws Exception {
            long id;
            int tries = 3;
            int j;
            long start = new Date().getTime();

            LoggerManager loggerManager = LoggerManager.getInstance();
            UsbCubeSerialInterface com = (UsbCubeSerialInterface) loggerManager.getActiveLogger().getCommunicationInterface();

            for(id = lr.getStartIndex(); id <= lr.getEndIndex(); id++){
                for(j = 0; j < tries; j++) {
                    if(com.getLogfileDataset((UsbCube) loggerManager.getActiveLogger(), lr, id)){
                        break;
                    }

                    if(com.getLastError().equals(LanguageManager.getInstance().getString(1066))){
                        break;
                    }
                }
                if(j == tries){
                    return null;
                }

                SampleProgress progress = new SampleProgress(id - lr.getStartIndex(), lr.getEndIndex() - lr.getStartIndex());
                publish(progress);
            }

            double duration = (new Date().getTime() - start)/1000.;
            System.out.println("Duration: " + Double.toString(duration) + "s");
            return lr;
        }

        @Override
        public void done(){

            if(dcp != null && lr != null){
                dcp.setLoggerRecord(lr);
            }

            mainFrame.updateUIContent();
        }

        @Override
        protected void process (java.util.List<SampleProgress> samples){
            SampleProgress progress = samples.get(samples.size()-1);
            long loaded = progress.samples + 1;
            long max = progress.maxSamples + 1;

            lblDownloadStatusContent.setText(loaded + "/" + max);
        }
    }
}
