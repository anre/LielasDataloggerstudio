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
import org.lielas.dataloggerstudio.lib.FileCreator.CsvFileCreator;
import org.lielas.dataloggerstudio.lib.FileCreator.FileCreator;
import org.lielas.dataloggerstudio.lib.FileCreator.FileSaver;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.dataloggerstudio.lib.LoggerRecordManager;
import org.lielas.dataloggerstudio.pc.gui.BodyButton;
import org.lielas.dataloggerstudio.pc.gui.ImageButton;
import org.lielas.dataloggerstudio.pc.gui.MainFrame;
import org.lielas.dataloggerstudio.pc.gui.MouseOverHintManager;
import org.lielas.dataloggerstudio.pc.gui.Panels.MenuPanels.ExportPanel.CsvExportPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.MenuPanels.ExportPanel.ExportPanel;
import org.lielas.dataloggerstudio.pc.gui.Toast.Toast;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Andi on 06.01.2015.
 */
public class ExportMenuPanel extends MenuPanel {

    JLabel lblHeader;
    JLabel lblFormat;
    JComboBox<String> cbFileFormat;
    JLabel lblPath;
    JTextField txtPath;
    ImageButton bttnOpenPath;
    JLabel lblFilename;
    JTextField txtFilename;
    BodyButton bttnSave;

    ExportPanel exportPanel;
    LoggerRecord lr;

    public ExportMenuPanel(MainFrame mainFrame){
        super(mainFrame);

        this.setOpaque(false);
        this.setBorder(null);

        int row = 2;

        setLayout(new FormLayout(
            new ColumnSpec[] {
                ColumnSpec.decode("20px"),
                FormFactory.PREF_COLSPEC,
                ColumnSpec.decode("20px"),
                ColumnSpec.decode("max(80dlu;pref)"),
                ColumnSpec.decode("20px"),
                FormFactory.PREF_COLSPEC,
                ColumnSpec.decode("30px:grow"),},
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
                RowSpec.decode("20px"),
                FormFactory.PREF_ROWSPEC,
                RowSpec.decode("20px:grow"),}));

        lblHeader = new JLabel("");
        lblHeader.setFont(fontBold);
        add(lblHeader, getLayoutString(2, row, "left", "center"));
        row += 2;

        lblFormat = new JLabel("");
        lblFormat.setFont(font);
        add(lblFormat, getLayoutString(2, row, "left", "center"));

        cbFileFormat = new JComboBox<String>();
        cbFileFormat.setBorder(null);
        cbFileFormat.setModel(new DefaultComboBoxModel<String>(new String[] {".csv"}));
        add(cbFileFormat, getLayoutString(4, row, "fill", "center"));
        row += 2;

        exportPanel = (ExportPanel) new CsvExportPanel();
        add(exportPanel, "2, " + Integer.toString(row) + " 3, 1, left, top");
        row += 2;

        lblPath = new JLabel("");
        lblPath.setFont(font);
        add(lblPath, getLayoutString(2, row, "left", "center"));

        txtPath = new JTextField();
        txtPath.setColumns(10);
        add(txtPath, getLayoutString(4, row, "fill", "center"));

        bttnOpenPath = new ImageButton("folder_icon.png", "folder_icon.png");
        bttnOpenPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BttnOpenPathPressed();
            }
        });
        bttnOpenPath.setPreferredSize(new Dimension(16, 16));
        add(bttnOpenPath, getLayoutString(6, row, "left", "center"));
        row += 2;

        lblFilename = new JLabel("");
        lblFilename.setFont(font);
        add(lblFilename, getLayoutString(2, row, "left", "center"));

        txtFilename = new JTextField();
        txtFilename.setColumns(10);
        add(txtFilename, getLayoutString(4, row, "fill", "center"));
        row += 2;

        bttnSave = new BodyButton("");
        bttnSave.setFont(fontBttn);
        bttnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BttnSavePressed();
            }
        });
        add(bttnSave, getLayoutString(4, row, "right", "center"));

    }

    @Override
    public void updateLanguage(LanguageManager lm, MouseOverHintManager hintManager) {
        lblHeader.setText(lm.getString(989));
        lblFormat.setText(lm.getString(990));
        lblPath.setText(lm.getString(991));
        lblFilename.setText(lm.getString(992));
        bttnSave.setText(lm.getString(993));

        hintManager.addHintFor(lblHeader, lm.getString(1080));
        hintManager.addHintFor(lblFormat, lm.getString(1081));
        hintManager.addHintFor(cbFileFormat, lm.getString(1081));
        hintManager.addHintFor(lblPath, lm.getString(1082));
        hintManager.addHintFor(txtPath, lm.getString(1082));
        hintManager.addHintFor(bttnOpenPath, lm.getString(1082));
        hintManager.addHintFor(lblFilename, lm.getString(1083));
        hintManager.addHintFor(txtFilename, lm.getString(1083));

        exportPanel.updateLanguage(lm, hintManager);
    }

    @Override
    public void updateUIContent() {
        this.lr = LoggerRecordManager.getInstance().getSelected();
        if(lr != null) {
            txtFilename.setText(lr.getName()+ cbFileFormat.getSelectedItem().toString());
        }
    }

    private void BttnOpenPathPressed(){
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret = fc.showOpenDialog(this);

        if(ret == JFileChooser.APPROVE_OPTION){
            txtPath.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }



    private void BttnSavePressed(){
        LanguageManager lm = LanguageManager.getInstance();
        String fileName;
        String path;
        int status;

        if(lr == null){
            new Toast(mainFrame, lm.getString(1085));
            return;
        }

        //get file format
        if(exportPanel.getExportType() == ExportPanel.EXPORT_TYPE_CSV){

            CsvExportPanel exPanel = (CsvExportPanel) exportPanel;
            CsvFileCreator fileCreator = new CsvFileCreator();

            fileCreator.setFileSaverType(FileSaver.PC_FILE_SAVER);
            fileCreator.setDelimiter(exPanel.getDelimiter());
            fileCreator.setComma(exPanel.getComma());
            fileName = txtFilename.getText();
            path = txtPath.getText();

            if(path == null || path.equals("")){
                new Toast(mainFrame, lm.getString(1072));
                return;
            }

            if(fileName == null || fileName.equals("")){
                new Toast(mainFrame, lm.getString(1037));
                return;
            }

            if(fileName.contains(" ")){
                new Toast(mainFrame, lm.getString(1074));
                return;
            }


            fileCreator.setPath(path + "\\\\" +  fileName);
            fileCreator.create(lr);
            status = fileCreator.save(lr, false);
            if(status == FileCreator.STATUS_OK){
                new Toast(mainFrame, lm.getString(1078));
            }else if(status == FileCreator.STATUS_FILE_EXISTS){
                int reply = JOptionPane.showConfirmDialog(null, lm.getString(1076),
                        lm.getString(1075), JOptionPane.YES_NO_OPTION);

                if(reply == JOptionPane.YES_OPTION){
                    status = fileCreator.save(lr, true);
                    if(status == FileCreator.STATUS_OK){
                        new Toast(mainFrame, lm.getString(1078));
                    }else{
                        new Toast(mainFrame, lm.getString(1077));
                    }
                }else{
                    new Toast(mainFrame, lm.getString(1077));
                }
            }else{
                new Toast(mainFrame, lm.getString(1077));
            }
        }

    }
}
