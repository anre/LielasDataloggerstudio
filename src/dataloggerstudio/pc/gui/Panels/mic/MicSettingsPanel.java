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

import javax.swing.*;


import java.awt.*;

import org.lielas.dataloggerstudio.lib.LoggerManager;
import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.StartTrigger;
import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicModel;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.MicSerialInterface;
import org.lielas.dataloggerstudio.pc.gui.BodyButton;
import org.lielas.dataloggerstudio.pc.gui.MainFrame;
import org.lielas.dataloggerstudio.pc.gui.MouseOverHintManager;
import org.lielas.dataloggerstudio.pc.gui.Panels.SettingsPanel;
import org.lielas.dataloggerstudio.pc.gui.Toast.Toast;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import com.jhlabs.image.OpacityFilter;

import org.jdesktop.swingx.JXDatePicker;

import org.joda.time.*;


public class MicSettingsPanel extends SettingsPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3102991994912338349L;
	
	JLabel lblDeviceInfo;
	JLabel lblType;
	JLabel lblTypecontent;
	JLabel lblVersion;
	JLabel lblVersioncontent;
	JLabel lblId;
	private JTextField txtID;
	private JLabel lblSettings;
	private JLabel lblSampleRate;
	private JLabel lblUnit;
	private JLabel lblSamples;
	private JLabel lblStartTrigger;
	private JComboBox cbUnit;
	private JComboBox cbSamples;
	private JComboBox cbStartTrigger;
	private JTextField txtSampleRate;
	private BodyButton bttnSaveSettings;
	private JLabel lblDate;
	private JXDatePicker datePicker;
	private JSpinner timeSpinner;
	private JLabel lblTime;
	private BodyButton bttnSaveId;
	private BodyButton bttnSetClock;
	private JLabel lblClock;

	private MainFrame mainFrame;


	JPanel contentPanel;
	JScrollPane scrollPane;

	public MicSettingsPanel(MainFrame mainFrame){
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
						RowSpec.decode("20px"),
						FormFactory.PREF_ROWSPEC,
						RowSpec.decode("20px"),
						FormFactory.PREF_ROWSPEC,
						RowSpec.decode("20px:grow"),}));


		scrollPane = new JScrollPane(contentPanel);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);
		
		lblDeviceInfo = new JLabel("Device Info");
		lblDeviceInfo.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPanel.add(lblDeviceInfo, "2, 2, left, center");
		
		lblTypecontent = new JLabel("");
		lblTypecontent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPanel.add(lblTypecontent, "4, 4, left, center");
		
		lblType = new JLabel("Type");
		lblType.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPanel.add(lblType, "2, 4, left, center");
		
		lblVersioncontent = new JLabel("");
		lblVersioncontent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPanel.add(lblVersioncontent, "4, 6, left, center");
		
		lblVersion = new JLabel("Version");
		lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPanel.add(lblVersion, "2, 6, left, center");
		
		lblId = new JLabel("ID");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPanel.add(lblId, "2, 8, left, center");
		
		txtID = new JTextField();
		txtID.setMinimumSize(new Dimension(150, 20));
		txtID.setPreferredSize(new Dimension(150, 20));
		contentPanel.add(txtID, "4, 8, fill, center");
		txtID.setColumns(10);
		
		bttnSaveId = new BodyButton("New button");
		bttnSaveId.setText("Save Id");
		bttnSaveId.setFont(new Font("Tahoma", Font.BOLD, 11));
		bttnSaveId.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BttnSaveIdPressed();
			}
		});
		contentPanel.add(bttnSaveId, "4, 10, right, default");
		
		lblClock = new JLabel("Clock");
		lblClock.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPanel.add(lblClock, "2, 12");
		
		lblDate = new JLabel("Date");
		contentPanel.add(lblDate, "2, 14, left, center");
		
		datePicker = new JXDatePicker();
		datePicker.setTimeZone(TimeZone.getTimeZone("UTC"));
		datePicker.setDate(new Date());
		datePicker.setName("datePicker");
		datePicker.setFormats("dd.MM.yyyy");
		JButton dateBtn = (JButton)datePicker.getComponent(1);
		dateBtn.setOpaque(false);
		dateBtn.setContentAreaFilled(false);
		dateBtn.setBorderPainted(false);
		dateBtn.setFocusPainted(false);
		datePicker.remove(dateBtn);
		datePicker.add(dateBtn);
		contentPanel.add(datePicker, "4, 14, fill, center");
		
		lblTime = new JLabel("Time");
		contentPanel.add(lblTime, "2, 16, left, center");
		
		timeSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
		LocalTime time = new LocalTime(DateTimeZone.UTC);
		timeSpinner.setEditor(timeEditor);
		timeSpinner.setValue(new Date(time.getMillisOfDay()));
		contentPanel.add(timeSpinner, "4, 16");
		
		
		bttnSaveSettings = new BodyButton("New button");
		bttnSaveSettings.setFont(new Font("Tahoma", Font.BOLD, 11));
		bttnSaveSettings.setText("Start Logger");
		bttnSaveSettings.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BttnSaveSettingsPressed();
			}
		});
		
		bttnSetClock = new BodyButton("New button");
		bttnSetClock.setText("Set Clock");
		bttnSetClock.setFont(new Font("Tahoma", Font.BOLD, 11));
		bttnSetClock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BttnSetClockPressed();
			}
		});
		contentPanel.add(bttnSetClock, "4, 18, right, default");
		
		lblSettings = new JLabel("Settings");
		lblSettings.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPanel.add(lblSettings, "2, 20, left, center");
		
		lblSampleRate = new JLabel("Sample Rate");
		lblSampleRate.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPanel.add(lblSampleRate, "2, 22, left, center");
		
		txtSampleRate = new JTextField();
		contentPanel.add(txtSampleRate, "4, 22, fill, center");
		txtSampleRate.setColumns(10);
		
		lblSamples = new JLabel("Samples");
		lblSamples.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPanel.add(lblSamples, "2, 24, left, center");
		
		cbSamples = new JComboBox();
		cbSamples.setBorder(null);

		contentPanel.add(cbSamples, "4, 24, fill, center");
		
		lblStartTrigger = new JLabel("Start Trigger");
		lblStartTrigger.setFont(new Font("Tahoma", Font.PLAIN, 11));
		contentPanel.add(lblStartTrigger, "2, 26, left, center");
		
		cbStartTrigger = new JComboBox();
		cbStartTrigger.setBorder(null);
		contentPanel.add(cbStartTrigger, "4, 26, fill, center");
		contentPanel.add(bttnSaveSettings, "4, 28, right, center");
		
		
		fillCbSamples();
		fillCbStartTrigger();
		updateUIContent();
		updateLanguage(LanguageManager.getInstance(), null);
	}
	
	@Override
	public void updateUIContent(){
		LoggerManager lm = LoggerManager.getInstance();
		MicUSBStick logger = (MicUSBStick) lm.getActiveLogger();
		
		if(logger == null){
			return;
		}
		
		
		//update model
		MicModel model = logger.getModel();
		if(model != null){
			lblTypecontent.setText(model.toString());
		}
		
		//update version
		String version = logger.getVersion();
		if(version != null){
			lblVersioncontent.setText(version);
		}

		//update name
		String name = logger.getName();
		if(name != null){
			txtID.setText(name);
		}
		
		//update sample rate
		String sampleRate = Long.toString(logger.getSampleRate());
		txtSampleRate.setText(sampleRate);
	}
	
	public void updateLanguage(LanguageManager lm, MouseOverHintManager hintManager){
		
		lblDeviceInfo.setText(lm.getString(917));
		lblType.setText(lm.getString(918));
		lblVersion.setText(lm.getString(919));
		lblId.setText(lm.getString(920));
		bttnSaveId.setText(lm.getString(922));
		
		lblClock.setText(lm.getString(924));
		lblDate.setText(lm.getString(925));
		lblTime.setText(lm.getString(926));
		bttnSetClock.setText(lm.getString(927));
		
		lblSettings.setText(lm.getString(929));
		lblSampleRate.setText(lm.getString(930));
		lblSamples.setText(lm.getString(934));
		lblStartTrigger.setText(lm.getString(936));
		bttnSaveSettings.setText(lm.getString(938));
		
		if(hintManager != null){
			hintManager.addHintFor(txtID, lm.getString(921));
			hintManager.addHintFor(bttnSaveId, lm.getString(923));
			hintManager.addHintFor(bttnSetClock, lm.getString(928));
			hintManager.addHintFor(txtSampleRate, lm.getString(931));
			hintManager.addHintFor(cbUnit, lm.getString(933));
			hintManager.addHintFor(cbSamples, lm.getString(935));
			hintManager.addHintFor(cbStartTrigger, lm.getString(937));
			hintManager.addHintFor(bttnSaveSettings, lm.getString(939));
		}
		
	}
	
	private void fillCbSamples(){
		LoggerManager lm = LoggerManager.getInstance();
		MicUSBStick logger = (MicUSBStick) lm.getActiveLogger();
		String[] samples = logger.getPossibleSampleValues();
		
		if(samples != null){
			cbSamples.removeAllItems();
			for(int i = 0; i < samples.length; i++){
				cbSamples.addItem(samples[i]);
			}
		}
	}

	
	private void fillCbStartTrigger(){
		LoggerManager loggerManager = LoggerManager.getInstance();
		LanguageManager languageManager = LanguageManager.getInstance();
		MicUSBStick logger = (MicUSBStick) loggerManager.getActiveLogger();
		int[] triggers = logger.getPossibleStartTriggers();

		
		if(triggers != null){
			cbStartTrigger.removeAllItems();
			for(int i = 0; i < triggers.length; i++){
				cbStartTrigger.addItem(languageManager.getString(triggers[i]));
			}
		}
	}
	
	private void BttnSaveIdPressed(){
		String id = txtID.getText();
		MicUSBStick logger = (MicUSBStick)LoggerManager.getInstance().getActiveLogger();
		
		if(id.length() > 20){
			new Toast(mainFrame, LanguageManager.getInstance().getString(1048));
			txtID.setText(logger.getName());
			return;
		}
		
		if(id.contains(" ")){
			new Toast(mainFrame, LanguageManager.getInstance().getString(1049));
			txtID.setText(logger.getName());
			return;
		}
		
		SaveIdWorker saveIdWorker = new SaveIdWorker(id, this);
		saveIdWorker.execute();
	}

	private void BttnSaveSettingsPressed(){		
		LoggerManager loggerManager = LoggerManager.getInstance();
		MicUSBStick logger = (MicUSBStick)loggerManager.getActiveLogger();
		long samplerate = 0;
		
		if(logger == null)
			return;
		
		MicSerialInterface com = (MicSerialInterface) logger.getCommunicationInterface();
		
		
		//check if CommunicationInterface is buzy
		if(com.isBusy() ){
			new Toast((MainFrame) SwingUtilities.getWindowAncestor(this), "Logger is busy");
			return;
		}
		
		//check if user is sure to start logging
		int reply = JOptionPane.showConfirmDialog(null, LanguageManager.getInstance().getString(941),
				LanguageManager.getInstance().getString(940), JOptionPane.YES_NO_OPTION);
		
		if(reply != JOptionPane.YES_OPTION){
			return;
		}
		
		//parse samplerate
		try{
			samplerate = Long.parseLong(txtSampleRate.getText());
		}catch(NumberFormatException e){
			new Toast(mainFrame, LanguageManager.getInstance().getString(1050));
			return;
		}
		
		if(samplerate < 1 || samplerate > 21600){
			new Toast(mainFrame, LanguageManager.getInstance().getString(1050));
			return;
		}
		
		try{
			logger.setSampleRate(samplerate);
			logger.setUnitClass(new UnitClass());
			logger.setMaxSamples(Integer.parseInt(cbSamples.getSelectedItem().toString()));
			logger.setStartTrigger(new StartTrigger(LanguageManager.getInstance().getId(cbStartTrigger.getSelectedItem().toString())));
			
			SaveSettingsWorker saveSettingsWorker = new SaveSettingsWorker(this);
			saveSettingsWorker.execute();
		}catch(Exception e){
			//TODO 
			e.printStackTrace();
		}
	}
	
	public void BttnSetClockPressed(){
		Date date;
		long ms;

		LoggerManager loggerManager = LoggerManager.getInstance();
		MicUSBStick logger = (MicUSBStick)loggerManager.getActiveLogger();
		
		//check date and time
		try{
			date = datePicker.getDate();
			ms = date.getTime();
			timeSpinner.commitEdit();
			date = (Date)timeSpinner.getValue();
			ms += date.getTime();
			logger.setFirstDatasetDate(ms);
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		
		
		try{
			SetClockWorker setClockWorker = new SetClockWorker(this);
			setClockWorker.execute();
			
		}catch(Exception e){
			//TODO 
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 * 				Swing workers
	 * 
	 */
	
	
	class SaveIdWorker extends SwingWorker<Object, MicUSBStick>{
		
		private String id;
		private JPanel parent;
		
		public SaveIdWorker(String id, JPanel panel){
			this.id = id;
			this.parent = panel;
		}
		
		@Override 
		public Object doInBackground(){
			LoggerManager loggerManager = LoggerManager.getInstance();
			MicSerialInterface com = (MicSerialInterface)loggerManager.getActiveLogger().getCommunicationInterface();
			com.getLastError();
			com.saveId(id, (MicUSBStick)loggerManager.getActiveLogger());
			return (MicUSBStick)loggerManager.getActiveLogger();
		}
		
		@Override
		public void done(){
			mainFrame.updateUIContent();
			
			try {
				MicUSBStick logger = (MicUSBStick)get();
				String err = logger.getCommunicationInterface().getLastError();
				if(err == null){
					new Toast(mainFrame, LanguageManager.getInstance().getString(1029));
				}else{
					new Toast(mainFrame, "Error: " + err);
				}
			} catch (InterruptedException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
			} catch (ExecutionException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
			}
		}
	}

	class SaveSettingsWorker extends SwingWorker<Object, Logger>{
	
		private JPanel parent;
		
		public SaveSettingsWorker(JPanel panel){
			this.parent = panel;
		}
		
		@Override 
		public Object doInBackground(){
			LoggerManager loggerManager = LoggerManager.getInstance();
			MicUSBStick logger = (MicUSBStick)loggerManager.getActiveLogger();
			MicSerialInterface com = (MicSerialInterface)logger.getCommunicationInterface();
			com.getLastError();
			com.saveSettings(logger);
			return (MicUSBStick)loggerManager.getActiveLogger();
		}
		
		@Override
		public void done(){
			mainFrame.updateUIContent();
			
			try {
				MicUSBStick logger = (MicUSBStick)get();
				String err = logger.getCommunicationInterface().getLastError();
				if(err == null){
					new Toast(mainFrame, LanguageManager.getInstance().getString(1031));
				}else{
					new Toast(mainFrame, "Error: " + err);
				}
			} catch (InterruptedException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
			} catch (ExecutionException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
			}
		}
	}
	
	class SetClockWorker extends SwingWorker<Object, Logger>{
		
		private JPanel parent;
		
		public SetClockWorker(JPanel panel){
			this.parent = panel;
		}
		
		@Override 
		public Object doInBackground(){
			LoggerManager loggerManager = LoggerManager.getInstance();
			MicSerialInterface com = (MicSerialInterface)loggerManager.getActiveLogger().getCommunicationInterface();
			com.getLastError();
			com.setClock((MicUSBStick)loggerManager.getActiveLogger());
			return loggerManager.getActiveLogger();
		}
		
		@Override
		public void done(){
			mainFrame.updateUIContent();
			
			try {
				MicUSBStick logger = (MicUSBStick)get();
				String err = logger.getCommunicationInterface().getLastError();
				if(err == null){
					new Toast(mainFrame, LanguageManager.getInstance().getString(1030));
				}else{
					new Toast(mainFrame, "Error: " + err);
				}
			} catch (InterruptedException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
			} catch (ExecutionException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
			}
		}
	}
}








