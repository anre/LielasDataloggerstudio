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

package org.lielas.dataloggerstudio.pc.gui.Panels.DataContentPanels;

import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetItemIds;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetSensorItemPT1K;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetStructure;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerManager;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.dataloggerstudio.lib.LoggerRecordManager;
import org.lielas.dataloggerstudio.pc.gui.MainFrame;
import org.lielas.dataloggerstudio.pc.gui.MouseOverHintManager;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by Andi on 14.01.2015.
 */
public class DataTablePanel extends DataContentPanel {

    private MainFrame mainFrame;

    JScrollPane tableScrollPane;
    private JTable table;

    public DataTablePanel(MainFrame mainFrame){
        super();

        this.setOpaque(false);
        setLayout(new BorderLayout());
        this.mainFrame = mainFrame;

        JPanel tablePanel = new JPanel();
        tablePanel.setOpaque(false);
        tablePanel.setBorder(null);
        tablePanel.setAlignmentY(Component.TOP_ALIGNMENT);
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tablePanel.setBackground(Color.ORANGE);
        add(tablePanel, BorderLayout.CENTER);
        tablePanel.setLayout(new BorderLayout(0, 0));

        Component horizontalStrut = Box.createHorizontalStrut(20);
        tablePanel.add(horizontalStrut, BorderLayout.WEST);

        Component horizontalStrut_1 = Box.createHorizontalStrut(20);
        tablePanel.add(horizontalStrut_1, BorderLayout.EAST);

        Component verticalStrut_1 = Box.createVerticalStrut(20);
        tablePanel.add(verticalStrut_1, BorderLayout.NORTH);

        Component verticalStrut_2 = Box.createVerticalStrut(20);
        tablePanel.add(verticalStrut_2, BorderLayout.SOUTH);

        tableScrollPane = new JScrollPane();
        //tableScrollPane.setVisible(false);
        tableScrollPane.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.gray));
        tableScrollPane.setOpaque(false);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        table = new JTable();
        tableScrollPane.setViewportView(table);
        table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        table.setOpaque(false);
        table.setModel(new DefaultTableModel(
                new Object[][] {
                        {null, null, null}
                },
                new String[] {
                        "", "", ""
                }
        ));

        lr = null;
    }

    @Override
    public void updateLanguage(LanguageManager lm, MouseOverHintManager hintManager) {

    }

    @Override
    public void updateUIContent() {

        setLoggerRecord(LoggerRecordManager.getInstance().getSelected());

        if(this.lr == null){
            tableScrollPane.setVisible(false);
            return;
        }
        tableScrollPane.setVisible(true);

        LielasTableModel ltm = new LielasTableModel(lr);
        table.setModel(ltm);

    }

    private class LielasTableModel extends AbstractTableModel {

        private LoggerRecord lr;

        public LielasTableModel(LoggerRecord lr){
            this.lr = lr;
        }

        public int getColumnCount() {
            if(lr == null){
                return 0;
            }
            return (lr.getChannels() + 1);  //channels + date
        }

        public int getRowCount() {
            if(lr == null){
                return 0;
            }
            return lr.getCount();
        }

        public Object getValueAt(int row, int column) {
            if(lr == null){
                return null;
            }
            return lr.getString(row,  column);
        }

        public String getColumnName(int column){
            Logger logger = LoggerManager.getInstance().getActiveLogger();
            LanguageManager lm = LanguageManager.getInstance();
            String[] columnNames;

            if(logger instanceof UsbCube){
                UsbCube usbCube = (UsbCube)logger;
                columnNames = new String[usbCube.getChannels()+1];

                columnNames[0] = lm.getString(999);
                for(int i = 1; i < usbCube.getChannels()+1; i++){
                    DatasetStructure ds = usbCube.getDatasetStructure();
                    switch(ds.getItem(i+1).getItemId()){
                        case DatasetItemIds.SHT_T:
                        case DatasetItemIds.PT1K1:
                        case DatasetItemIds.PT1K2:
                        case DatasetItemIds.PT1K3:
                        case DatasetItemIds.PT1K4:
                            columnNames[i] = lm.getString(1018);
                            break;
                        case DatasetItemIds.SHT_H:
                            columnNames[i] = lm.getString(1019);
                            break;
                        case DatasetItemIds.MS5607:
                            columnNames[i] = lm.getString(1108);
                            break;
                        default:
                            columnNames[i] = "";
                            break;
                    }
                }
            }else {
                columnNames = new String[3];
                columnNames[0] = lm.getString(999);
                columnNames[1] = lm.getString(1018);
                columnNames[2] = lm.getString(1019);
            }

            return columnNames[column];
        }

    }
}
