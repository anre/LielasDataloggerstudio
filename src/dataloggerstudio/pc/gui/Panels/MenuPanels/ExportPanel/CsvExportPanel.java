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

package org.lielas.dataloggerstudio.pc.gui.Panels.MenuPanels.ExportPanel;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JComboBox;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;

import org.lielas.dataloggerstudio.pc.gui.MouseOverHintManager;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

public class CsvExportPanel extends ExportPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1106710732607759747L;

	JComboBox<String> cbDelimiter;
	JComboBox<String> cbComma;
	
	JLabel lblHeader;
	JLabel lblDelimiter;
	JLabel lblComma;
	
	public CsvExportPanel() {
		super(EXPORT_TYPE_CSV);
		
		setBackground(Color.MAGENTA);
		setOpaque(false);
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("20dlu"),
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("20dlu"),
				FormFactory.BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("10px"),}));
		
		lblHeader = new JLabel("");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(lblHeader, "2, 1, left, center");
		
		lblDelimiter = new JLabel("");
		lblDelimiter.setFont(new Font("Tahoma", Font.PLAIN, 11));
		add(lblDelimiter, "2, 3, left, center");
		
		cbDelimiter = new JComboBox<String>();
		cbDelimiter.setBorder(null);
		cbDelimiter.setModel(new DefaultComboBoxModel<String>(new String[] {";", ".", ",", "tab", "space"}));
		cbDelimiter.setPreferredSize(new Dimension(40, 20));
		add(cbDelimiter, "4, 3, fill, center");
		
		lblComma = new JLabel("");
		lblComma.setFont(new Font("Tahoma", Font.PLAIN, 11));
		add(lblComma, "2, 5, left, center");
		
		cbComma = new JComboBox<String>();
		cbComma.setBorder(null);
		cbComma.setModel(new DefaultComboBoxModel<String>(new String[] {",", "."}));
		add(cbComma, "4, 5, fill, center");
	}
	
	public String getDelimiter(){
		return String.valueOf(cbDelimiter.getSelectedItem());
	}
	
	public String getComma(){
		return String.valueOf(cbComma.getSelectedItem());
	}

	@Override
	public void updateLanguage(LanguageManager lm, MouseOverHintManager hintManager) {
		
		lblHeader.setText(lm.getString(994));
		lblDelimiter.setText(lm.getString(995));
		lblComma.setText(lm.getString(997));
		
		if(hintManager != null){
			hintManager.addHintFor(lblDelimiter, lm.getString(996));
			hintManager.addHintFor(cbDelimiter, lm.getString(996));
			hintManager.addHintFor(lblComma, lm.getString(998));
			hintManager.addHintFor(cbComma, lm.getString(998));
			hintManager.addHintFor(lblHeader, lm.getString(1017));
		}
	}
	
}