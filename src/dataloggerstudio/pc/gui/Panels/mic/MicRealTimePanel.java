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

package org.lielas.dataloggerstudio.pc.gui.Panels.mic;

import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.LoggerManager;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.dataloggerstudio.lib.LoggerRecordManager;
import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.dataloggerstudio.pc.CommunicationInterface.mic.MicSerialInterface;
import org.lielas.dataloggerstudio.pc.gui.BodyButton;
import org.lielas.dataloggerstudio.pc.gui.MainFrame;
import org.lielas.dataloggerstudio.pc.gui.MouseOverHintManager;
import org.lielas.dataloggerstudio.pc.gui.GraphPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.RealTimePanel;
import org.lielas.dataloggerstudio.pc.gui.Toast.Toast;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JButton;

import java.awt.Font;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.FlowLayout;


public class MicRealTimePanel extends RealTimePanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6847944491444318288L;

	private GraphPanel graphPanel;
	
	BodyButton bttnStart;

	private MainFrame mainFrame;
	
	JLabel lblValue1;
	JLabel lblValue2;
	JLabel lblValue3;
	JLabel lblUnit1;
	JLabel lblUnit2;
	JLabel lblUnit3;
	private JPanel panel_1;
	private JPanel panel_2;
	private Component verticalStrut;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel colorPanel1;
	private JPanel panel_6;
	private JPanel colorPanel2;
	
	private RealTimeDataWorker realtimeDataWorker;
	
	public MicRealTimePanel(MainFrame mainFrame) {
		super();
		
		this.mainFrame = mainFrame;
		
		setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		panel_1.setOpaque(false);
		add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		panel_2 = new JPanel();
		panel_2.setOpaque(false);
		panel_1.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		panel_3 = new JPanel();
		panel_3.setOpaque(false);
		panel_2.add(panel_3, BorderLayout.SOUTH);
		
		bttnStart = new BodyButton("Start");
		bttnStart.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_3.add(bttnStart);
		bttnStart.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				StartBttnPressed();
			}
		});
		
		verticalStrut = Box.createVerticalStrut(30);
		panel_2.add(verticalStrut, BorderLayout.NORTH);
		
		JPanel valuePanel = new JPanel();
		panel_1.add(valuePanel);
		valuePanel.setPreferredSize(new Dimension(250, 10));
		valuePanel.setOpaque(false);
		valuePanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("30dlu"),},
			new RowSpec[] {
				RowSpec.decode("20px"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("20dlu"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("20dlu"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("20dlu"),
				FormFactory.PREF_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("7dlu:grow"),}));
		
		panel_4 = new JPanel();
		panel_4.setOpaque(false);
		panel_4.setBackground(Color.GREEN);
		valuePanel.add(panel_4, "2, 2, fill, fill");
		panel_4.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		colorPanel1 = new JPanel();
		colorPanel1.setVisible(false);
		FlowLayout fl_colorPanel1 = (FlowLayout) colorPanel1.getLayout();
		fl_colorPanel1.setAlignment(FlowLayout.LEFT);
		colorPanel1.setPreferredSize(new Dimension(30, 10));
		colorPanel1.setBackground(Color.GREEN);
		panel_4.add(colorPanel1, "2, 2, center, center");
		
		lblValue1 = new JLabel("22,3");
		lblValue1.setFont(new Font("Tahoma", Font.PLAIN, 44));
		valuePanel.add(lblValue1, "4, 2, right, bottom");
		
		lblValue1.setVisible(false);
		
		lblUnit1 = new JLabel("°C");
		lblUnit1.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		lblUnit1.setFont(new Font("Tahoma", Font.PLAIN, 30));
		valuePanel.add(lblUnit1, "6, 2, left, bottom");
		lblUnit1.setVisible(false);
		
		panel_6 = new JPanel();
		panel_6.setOpaque(false);
		panel_6.setBackground(Color.GREEN);
		valuePanel.add(panel_6, "2, 4, fill, fill");
		panel_6.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		colorPanel2 = new JPanel();
		colorPanel2.setVisible(false);
		colorPanel2.setPreferredSize(new Dimension(30, 10));
		colorPanel2.setBackground(Color.BLUE);
		
		panel_6.add(colorPanel2, "2, 2, fill, fill");
		
		lblValue2 = new JLabel("48,7");
		lblValue2.setFont(new Font("Tahoma", Font.PLAIN, 44));
		valuePanel.add(lblValue2, "4, 4, right, default");
		lblValue2.setVisible(false);
		
		lblUnit2 = new JLabel("%");
		lblUnit2.setHorizontalAlignment(SwingConstants.LEFT);
		lblUnit2.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		lblUnit2.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblUnit2.setVerticalAlignment(SwingConstants.BOTTOM);
		lblUnit2.setFont(new Font("Tahoma", Font.PLAIN, 30));
		valuePanel.add(lblUnit2, "6, 4");
		lblUnit2.setVisible(false);
		
		lblValue3 = new JLabel("6,3");
		lblValue3.setFont(new Font("Tahoma", Font.PLAIN, 44));
		valuePanel.add(lblValue3, "4, 6, right, default");
		lblValue3.setVisible(false);
		
		lblUnit3 = new JLabel("V");
		lblUnit3.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		lblUnit3.setFont(new Font("Tahoma", Font.PLAIN, 30));
		valuePanel.add(lblUnit3, "6, 6");
		lblUnit3.setVisible(false);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormFactory.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("pref:grow"),
				FormFactory.UNRELATED_GAP_ROWSPEC,}));
		
		graphPanel = new GraphPanel();
		graphPanel.setOpaque(false);
		panel.add(graphPanel, "2, 2, fill, fill");
		
	}

	@Override
	public void updateLanguage(LanguageManager lm,
			MouseOverHintManager hintManager) {
		
		MicUSBStick logger = (MicUSBStick) LoggerManager.getInstance().getActiveLogger();
		if(logger == null){
			bttnStart.setText(lm.getString(960));
		}else{
			MicSerialInterface com = (MicSerialInterface) logger.getCommunicationInterface();
			if(com.realTimeLoggingInProgress()){
				bttnStart.setText(lm.getString(961));
			}else{
				bttnStart.setText(lm.getString(960));
			}
		}
		
		hintManager.addHintFor(bttnStart, lm.getString(962));
	}

	@Override
	public void updateUIContent() {
		LoggerRecord lr = LoggerRecordManager.getInstance().get();
		
		if(lr != null){
			graphPanel.setLoggerRecord(lr);
		}
		lblValue1.setVisible(false);
		lblValue2.setVisible(false);
		lblValue3.setVisible(false);
		lblUnit1.setVisible(false);
		lblUnit2.setVisible(false);
		lblUnit3.setVisible(false);
		colorPanel1.setVisible(false);
		colorPanel2.setVisible(false);
		
	}
	
	private void StartBttnPressed(){
		LoggerRecordManager lrm = LoggerRecordManager.getInstance();
		MicUSBStick logger = (MicUSBStick) LoggerManager.getInstance().getActiveLogger();
		LanguageManager lm = LanguageManager.getInstance();
		
		if(logger == null && lrm != null){
			return;
		}
		MicSerialInterface com = (MicSerialInterface) logger.getCommunicationInterface();
		


		
		if(!com.realTimeLoggingInProgress()){
			
			int reply = JOptionPane.showConfirmDialog(null, LanguageManager.getInstance().getString(965),
					LanguageManager.getInstance().getString(964), JOptionPane.YES_NO_OPTION);
			

			if(reply != JOptionPane.YES_OPTION){
				return;
			}
			
			LoggerRecord lr = new LoggerRecord(logger);
			lr.setChannels(2);
			lrm.add(lr);
			
			realtimeDataWorker = new RealTimeDataWorker(logger, lr, this);
			realtimeDataWorker.execute();
			bttnStart.setText(lm.getString(961));
			
			new Toast(mainFrame, lm.getString(963));
			
		}else{
			realtimeDataWorker.cancel(true);
			realtimeDataWorker = null;
			bttnStart.setText(lm.getString(960));
		}
		
	}
	
	class RealTimeDataWorker extends SwingWorker<Object, Dataset>{

		MicUSBStick logger;
		LoggerRecord lr;
		private JPanel parent;
		
		public RealTimeDataWorker(MicUSBStick logger, LoggerRecord loggerRecord, JPanel panel) {
			this.logger = logger;
			this.lr = loggerRecord;
			this.parent = panel;
		}
		
		@Override
		protected Logger doInBackground(){
			MicSerialInterface com = (MicSerialInterface) logger.getCommunicationInterface();
			
			if(com.isOpen()){
				if(com.startRealTimeLogging()){
					while(!isCancelled()){
						if(com.getRealTimeValue(lr)){
							if(isCancelled()){
								break;
							}
							publish(lr.get(lr.getCount()-1));
						}
					}
				}
				com.stopRealTimeLogging();
			}
			return null;
		}
		
		@Override
		protected void process(List<Dataset> list){
			mainFrame.updateUIContent();
			
			if(list.size() > 0){
				Dataset ds = list.get(list.size() - 1);
				String value = String.format("%.1f",  ds.getValue(0));
				value = value.replace(".", ",");
				lblValue1.setText(value);
				lblValue1.setVisible(true);
				lblUnit1.setVisible(true);
				colorPanel1.setVisible(true);
				
				if(ds.getChannels() > 1){
					value = String.format("%.1f",  ds.getValue(1));
					value = value.replace(".", ",");
					lblValue2.setText(value);
					lblValue2.setVisible(true);
					lblUnit2.setVisible(true);
					colorPanel2.setVisible(true);
				}
				
				if(ds.getChannels() > 2){
					value = String.format("%.1f",  ds.getValue(2));
					value = value.replace(".", ",");
					lblValue3.setText(value);
					lblValue3.setVisible(true);
				}
				
			}
			for(int i = 0; i < list.size(); i++){
				list.get(i);
			}
		}
		
	}
	
	
	
}