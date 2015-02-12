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

package org.lielas.dataloggerstudio.pc.gui.Panels;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lielas.dataloggerstudio.pc.gui.BodyButton;
import org.lielas.dataloggerstudio.pc.gui.MainFrame;
import org.lielas.dataloggerstudio.pc.gui.MouseOverHintManager;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutPanel extends DataloggerstudioPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6209229884829603645L;
	
	private MainFrame mainFrame;

	JPanel panel;
	
	JLabel lblTitle;
	private JLabel lblJoda;
	private JLabel lblJssc;
	private JLabel lblSwingx;
	private JLabel lblJgoodiesFormlayout;

	private BodyButton bttnReloadStrings;
	private BodyButton bttnPrintLbls;

	public AboutPanel(MainFrame mainFrame){
		super();
		this.mainFrame = mainFrame;
		
		setBorder(null);
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		panel.setOpaque(false);
		add(panel);
		panel.setLayout(new FormLayout(
				new ColumnSpec[]{
						ColumnSpec.decode("125px"),
						ColumnSpec.decode("center:pref"),
						ColumnSpec.decode("7dlu:grow"),},
				new RowSpec[]{
						RowSpec.decode("20dlu"),
						RowSpec.decode("center:pref"),
						RowSpec.decode("20dlu"),
						RowSpec.decode("center:pref"),
						RowSpec.decode("20dlu"),
						RowSpec.decode("center:pref"),
						RowSpec.decode("10dlu"),
						RowSpec.decode("center:pref"),
						RowSpec.decode("10dlu"),
						RowSpec.decode("center:pref"),
						RowSpec.decode("10dlu"),
						RowSpec.decode("center:pref"),
						RowSpec.decode("10dlu"),
						RowSpec.decode("center:pref"),
						RowSpec.decode("10dlu"),
						RowSpec.decode("center:pref"),
						RowSpec.decode("10dlu"),
						RowSpec.decode("center:pref"),
						RowSpec.decode("2dlu:grow"),	
		}));
		
		lblTitle = new JLabel("Lielas Dataloggerstudio 0.6.0");
		panel.add(lblTitle, "2, 2, left, default");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblBasedOn = new JLabel("Lielas Dataloggerstudio is based on:");
		lblBasedOn.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(lblBasedOn, "2, 6, left, default");
		
		lblJoda = new JLabel("joda-time 2.5 ");
		lblJoda.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel.add(lblJoda, "2, 8, left, default");
		
		lblJssc = new JLabel("jSSC 2.8.0  ");
		lblJssc.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel.add(lblJssc, "2, 10, left, default");
		
		lblSwingx = new JLabel("swingx 1.6.1 ");
		lblSwingx.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel.add(lblSwingx, "2, 12, left, default");
		
		lblJgoodiesFormlayout = new JLabel("jGoodies-forms 1.2.1 ");
		lblJgoodiesFormlayout.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel.add(lblJgoodiesFormlayout, "2, 14, left, default");

		/*bttnReloadStrings = new BodyButton("Reload Strings");
		bttnReloadStrings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BttnReloadStringsClicked();
			}
		});
		panel.add(bttnReloadStrings, "2, 16, left, default");

		bttnPrintLbls = new BodyButton("Print Labels");
		bttnPrintLbls.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BttnPrintLblsClicked();
			}
		});
		panel.add(bttnPrintLbls, "2, 18, left, default");*/

	}

	void BttnReloadStringsClicked(){
		mainFrame.updateUIContent();
		mainFrame.updateLanguage();
	}

	void BttnPrintLblsClicked(){
		LanguageManager.getInstance().switchDebugMode();
		mainFrame.updateUIContent();
		mainFrame.updateLanguage();
	}
	
	@Override
	public void updateLanguage(LanguageManager lm,
			MouseOverHintManager hintManager) {
		lblTitle.setText(lm.getString(900) + " " + lm.getString(901));
		
	}

	@Override
	public void updateUIContent() {
		// TODO Auto-generated method stub
		
	}
	
}