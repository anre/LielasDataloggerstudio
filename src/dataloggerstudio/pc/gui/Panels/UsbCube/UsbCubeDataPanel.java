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

package org.lielas.dataloggerstudio.pc.gui.Panels.UsbCube;


import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.dataloggerstudio.lib.LoggerRecordManager;
import org.lielas.dataloggerstudio.pc.gui.MainFrame;
import org.lielas.dataloggerstudio.pc.gui.MouseOverHintManager;
import org.lielas.dataloggerstudio.pc.gui.Panels.DataContentPanels.DataContentPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.DataContentPanels.DataTablePanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.DataPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.MenuPanels.ExportMenuPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.MenuPanels.LoggerRecordsMenuPanel;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class UsbCubeDataPanel extends DataPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6397432528587675808L;
	
	private MainFrame mainFrame;


	LoggerRecordsMenuPanel loggerRecordsMenuPanel;
	ExportMenuPanel exportMenuPanel;

	DataContentPanel dcp;

	public UsbCubeDataPanel(MainFrame mainFrame){
		super();
		
		this.mainFrame = mainFrame;

		setLayout(new BorderLayout());

		//add menu panels

		JPanel menuPanel = new JPanel();
		menuPanel.setBorder(new MatteBorder(0, 1, 0, 0, (Color) Color.LIGHT_GRAY));
		menuPanel.setOpaque(false);
		menuPanel.setBackground(Color.ORANGE);
		menuPanel.setLayout(new FormLayout(new ColumnSpec[]{
				ColumnSpec.decode("pref:grow"),},
			new RowSpec[]{
					FormFactory.PREF_ROWSPEC,
					FormFactory.PREF_ROWSPEC,}));

		JScrollPane menuScrollPane = new JScrollPane(menuPanel);
		menuScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		menuScrollPane.setOpaque(false);
		menuScrollPane.getViewport().setOpaque(false);
		menuScrollPane.setBackground(Color.ORANGE);
		menuScrollPane.setBorder(null);
		add(menuScrollPane, BorderLayout.EAST);


		//add content panel
		dcp = new DataTablePanel(mainFrame);
		add(dcp, BorderLayout.CENTER);

		//add logfiles menu panel
		loggerRecordsMenuPanel = new LoggerRecordsMenuPanel(mainFrame);
		loggerRecordsMenuPanel.setDataContentPanel(dcp);
		menuPanel.add(loggerRecordsMenuPanel, getLayoutString(1, 1, "fill", "top"));

		//add export data menu panel
		exportMenuPanel = new ExportMenuPanel(mainFrame);
		menuPanel.add(exportMenuPanel, getLayoutString(1, 2, "fill", "top"));




	}
	
	@Override
	public void updateLanguage(LanguageManager lm, MouseOverHintManager hintManager) {
		exportMenuPanel.updateLanguage(lm, hintManager);
		loggerRecordsMenuPanel.updateLanguage(lm, hintManager);
	}

	@Override
	public void updateUIContent() {
		exportMenuPanel.updateUIContent();
		loggerRecordsMenuPanel.updateUIContent();
		dcp.updateUIContent();
	}
	
}