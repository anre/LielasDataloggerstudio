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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import org.jdesktop.swingx.JXDatePicker;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.lielas.dataloggerstudio.lib.LielasCommunicationProtocol.LielasSettingsProtocolName;
import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerManager;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.dataloggerstudio.lib.LoggerRecordManager;
import org.lielas.dataloggerstudio.pc.CommunicationInterface.UsbCube.UsbCubeSerialInterface;
import org.lielas.dataloggerstudio.pc.gui.BodyButton;
import org.lielas.dataloggerstudio.pc.gui.MainFrame;
import org.lielas.dataloggerstudio.pc.gui.MouseOverHintManager;
import org.lielas.dataloggerstudio.pc.gui.Panels.SettingsPanel;
import org.lielas.dataloggerstudio.pc.gui.Toast.Toast;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;


public class UsbCubeSettingsPanel extends SettingsPanel{


	/**
	 * 
	 */
	private static final long serialVersionUID = -2709683036726880040L;
	
	private MainFrame mainFrame;
	private boolean nameLoaded;
	private boolean samplerateLoaded;

	JPanel contentPanel;
	JScrollPane scrollPane;

	//info
	JLabel lblDeviceInfo;
	JLabel lblVersion;
	JLabel lblVersioncontent;
	JLabel lblId;
	JLabel lblIdContent;
	JLabel lblName;
	private JTextField txtNameContent;
	BodyButton bttnSaveName;


	//clock
	JLabel lblClockHeader;
	JLabel lblLoggerDateTime;
	JLabel lblLoggerDateTimeContent;
	JLabel lblPcDateTime;
	JLabel lblPcDateTimeContent;
	JLabel lblDate;
	JXDatePicker datePicker;
	JLabel lblTime;
	JSpinner timeSpinner;
	BodyButton bttnSetClock;
	BodyButton bttnSetPcClock;

	//logging
	JLabel lblLoggingHeader;
	JLabel lblLoggingStatus;
	JLabel lblLoggingStatusContent;
	JLabel lblLoggingSamplerate;
	JTextField txtLoggingSamplerate;
	JLabel lblLoggingFilename;
	JTextField txtLoggingFilename;
	BodyButton bttnStartLogger;

	//delete memory
	JLabel lblDeleteHeader;
	JButton bttnDeleteBttn;

	public UsbCubeSettingsPanel(MainFrame mainFrame){
		super();
		int row = 2;

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
						FormFactory.PREF_ROWSPEC,  // 10
						RowSpec.decode("20px"),
						FormFactory.PREF_ROWSPEC,
						RowSpec.decode("20px"),
						FormFactory.PREF_ROWSPEC,
						RowSpec.decode("20px"),
						FormFactory.PREF_ROWSPEC,
						RowSpec.decode("20px"),
						FormFactory.PREF_ROWSPEC,
						RowSpec.decode("20px"),
						FormFactory.PREF_ROWSPEC,  // 20
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
						RowSpec.decode("20px:grow"),
		}));

		scrollPane = new JScrollPane(contentPanel);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);

		//device info

		lblDeviceInfo = new JLabel("");
		lblDeviceInfo.setFont(fontBold);
		contentPanel.add(lblDeviceInfo, getLayoutString(2, row, "left", "center"));
		row += 2;
		
		lblVersion = new JLabel("");
		lblVersion.setFont(font);
		contentPanel.add(lblVersion, getLayoutString(2, row, "left", "center"));
		
		lblVersioncontent = new JLabel("");
		lblVersioncontent.setFont(font);
		contentPanel.add(lblVersioncontent, getLayoutString(4, row, "left", "center"));
		row += 2;
		
		lblId = new JLabel("");
		lblId.setFont(font);
		contentPanel.add(lblId, getLayoutString(2, row, "left", "center"));
		
		lblIdContent = new JLabel("");
		lblIdContent.setFont(font);
		contentPanel.add(lblIdContent, getLayoutString(4, row, "left", "center"));
		row += 2;
		
		lblName = new JLabel("");
		lblName.setFont(font);
		contentPanel.add(lblName, getLayoutString(2, row, "left", "center"));
		
		txtNameContent = new JTextField("");
		txtNameContent.setFont(font);
		txtNameContent.setMinimumSize(new Dimension(150, 20));
		txtNameContent.setPreferredSize(new Dimension(150, 20));
		contentPanel.add(txtNameContent, getLayoutString(4, row, "fill", "center"));
		row += 2;

		bttnSaveName = new BodyButton("");
		bttnSaveName.setFont(fontBttn);
		bttnSaveName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BttnSaveNamePressed();
			}
		});
		contentPanel.add(bttnSaveName, getLayoutString(4, row, "right", "center"));
		row += 2;

		//clock

		lblClockHeader = new JLabel("");
		lblClockHeader.setFont(fontBold);
		contentPanel.add(lblClockHeader, getLayoutString(2, row, "left", "center"));
		row += 2;

		lblLoggerDateTime = new JLabel("");
		lblLoggerDateTime.setFont(font);
		contentPanel.add(lblLoggerDateTime, getLayoutString(2, row, "left", "center"));

		lblLoggerDateTimeContent = new JLabel("");
		lblLoggerDateTimeContent.setFont(font);
		contentPanel.add(lblLoggerDateTimeContent, getLayoutString(4, row, "left", "center"));
		row += 2;

		lblPcDateTime = new JLabel("");
		lblPcDateTime.setFont(font);
		contentPanel.add(lblPcDateTime, getLayoutString(2, row, "left", "center"));

		lblPcDateTimeContent = new JLabel("");
		lblPcDateTimeContent.setFont(font);
		contentPanel.add(lblPcDateTimeContent, getLayoutString(4, row, "left", "center"));
		row += 2;


		bttnSetPcClock = new BodyButton("");
		bttnSetPcClock.setFont(fontBttn);
		bttnSetPcClock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BttnSetPcClockPressed();
			}
		});
		contentPanel.add(bttnSetPcClock, getLayoutString(4, row, "right", "center"));
		row += 2;


		lblDate = new JLabel("");
		lblDate.setFont(font);
		contentPanel.add(lblDate, getLayoutString(2, row, "left", "center"));

		datePicker = new JXDatePicker();
		datePicker.setTimeZone(TimeZone.getTimeZone("UTC"));
		datePicker.setDate(new Date());
		datePicker.setName("datePicker");
		datePicker.setFormats("dd.MM.yyyy");
		JButton dateBttn = (JButton) datePicker.getComponent(1);
		dateBttn.setOpaque(false);
		dateBttn.setContentAreaFilled(false);
		dateBttn.setFocusPainted(false);
		dateBttn.setBorderPainted(false);
		datePicker.remove(dateBttn);
		datePicker.add(dateBttn);
		contentPanel.add(datePicker, getLayoutString(4, row, "fill", "center"));
		row += 2;

		lblTime = new JLabel("");
		lblTime.setFont(font);
		contentPanel.add(lblTime, getLayoutString(2, row, "left", "center"));

		timeSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
		LocalTime time = new LocalTime(DateTimeZone.UTC);
		timeSpinner.setEditor(timeEditor);
		timeSpinner.setValue(new Date(time.getMillisOfDay()));
		contentPanel.add(timeSpinner, getLayoutString(4, row, "fill", "center"));
		row += 2;

		bttnSetClock = new BodyButton("");
		bttnSetClock.setFont(fontBttn);
		bttnSetClock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BttnSetClockPressed();
			}
		});
		contentPanel.add(bttnSetClock, getLayoutString(4, row, "right", "center"));
		row += 2;

		// logging

		lblLoggingHeader = new JLabel("");
		lblLoggingHeader.setFont(fontBold);
		contentPanel.add(lblLoggingHeader, getLayoutString(2, row, "left", "center"));
		row += 2;

		lblLoggingStatus = new JLabel("");
		lblLoggingStatus.setFont(font);
		contentPanel.add(lblLoggingStatus, getLayoutString(2, row, "left", "center"));

		lblLoggingStatusContent = new JLabel("");
		lblLoggingStatusContent.setFont(font);
		contentPanel.add(lblLoggingStatusContent, getLayoutString(4, row, "left", "center"));
		row += 2;

		lblLoggingSamplerate = new JLabel("");
		lblLoggingSamplerate.setFont(font);
		contentPanel.add(lblLoggingSamplerate, getLayoutString(2, row, "left", "center"));

		txtLoggingSamplerate = new JTextField("");
		txtLoggingSamplerate.setFont(font);
		txtLoggingSamplerate.setMinimumSize(new Dimension(150, 20));
		txtLoggingSamplerate.setPreferredSize(new Dimension(150, 20));
		contentPanel.add(txtLoggingSamplerate, getLayoutString(4, row, "fill", "center"));
		row += 2;

		lblLoggingFilename = new JLabel("");
		lblLoggingFilename.setFont(font);
		contentPanel.add(lblLoggingFilename, getLayoutString(2, row, "left", "center"));

		txtLoggingFilename = new JTextField("");
		txtLoggingFilename.setFont(font);
		txtLoggingFilename.setMinimumSize(new Dimension(150, 20));
		txtLoggingFilename.setPreferredSize(new Dimension(150, 20));
		contentPanel.add(txtLoggingFilename, getLayoutString(4, row, "fill", "center"));
		row += 2;

		bttnStartLogger = new BodyButton("");
		bttnStartLogger.setFont(fontBttn);
		bttnStartLogger.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BttnStartLogging();
			}
		});
		contentPanel.add(bttnStartLogger, getLayoutString(4, row, "right", "center"));
		row += 2;

		//delete memory
		lblDeleteHeader = new JLabel("");
		lblDeleteHeader.setFont(fontBold);
		contentPanel.add(lblDeleteHeader, getLayoutString(2, row, "left", "center"));
		row += 2;

		bttnDeleteBttn = new BodyButton("");
		bttnDeleteBttn.setFont(fontBttn);
		bttnDeleteBttn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BttnDeleteClicked();
			}
		});
		contentPanel.add(bttnDeleteBttn, getLayoutString(4, row, "right", "center"));
		row += 2;

		java.util.Timer t = new java.util.Timer();
		t.schedule(new UpdateTimeTask(), 1000, 1000);

		samplerateLoaded = false;
		nameLoaded = false;

	}
	
	@Override
	public void updateLanguage(LanguageManager lm, MouseOverHintManager hintManager) {
		lblDeviceInfo.setText(lm.getString(966));
		lblVersion.setText(lm.getString(967));
		lblId.setText(lm.getString(968));
		lblName.setText(lm.getString(969));
		bttnSaveName.setText(lm.getString(970));

		lblClockHeader.setText(lm.getString(972));
		lblLoggerDateTime.setText(lm.getString(973));
		lblPcDateTime.setText(lm.getString(1100));
		lblDate.setText(lm.getString(974));
		lblTime.setText(lm.getString(975));
		bttnSetClock.setText(lm.getString(976));
		bttnSetPcClock.setText(lm.getString(1102));

		lblLoggingHeader.setText(lm.getString(978));
		lblLoggingStatus.setText(lm.getString(980));
		lblLoggingSamplerate.setText(lm.getString(979));
		lblLoggingFilename.setText(lm.getString(981));

		lblDeleteHeader.setText(lm.getString(984));
		bttnDeleteBttn.setText(lm.getString(985));

		hintManager.addHintFor(lblDeviceInfo, lm.getString(1000));
		hintManager.addHintFor(lblVersion, lm.getString(1001));
		hintManager.addHintFor(lblVersioncontent, lm.getString(1001));
		hintManager.addHintFor(lblId, lm.getString(1002));
		hintManager.addHintFor(lblIdContent, lm.getString(1002));
		hintManager.addHintFor(lblName, lm.getString(1003));
		hintManager.addHintFor(txtNameContent, lm.getString(1003));
		hintManager.addHintFor(bttnSaveName, lm.getString(1008));
		hintManager.addHintFor(bttnSetPcClock, lm.getString(1103));

		hintManager.addHintFor(lblClockHeader, lm.getString(1004));
		hintManager.addHintFor(lblLoggerDateTime, lm.getString(1005));
		hintManager.addHintFor(lblLoggerDateTimeContent, lm.getString(1005));
		hintManager.addHintFor(lblPcDateTime, lm.getString(1001));
		hintManager.addHintFor(lblPcDateTimeContent, lm.getString(1001));
		hintManager.addHintFor(lblDate, lm.getString(1006));
		hintManager.addHintFor(datePicker, lm.getString(1006));
		hintManager.addHintFor(lblTime, lm.getString(1007));
		hintManager.addHintFor(timeSpinner, lm.getString(1007));
		hintManager.addHintFor(bttnSetClock, lm.getString(1009));

		hintManager.addHintFor(lblLoggingHeader, lm.getString(1010));
		hintManager.addHintFor(lblLoggingStatus, lm.getString(1011));
		hintManager.addHintFor(lblLoggingStatusContent, lm.getString(1011));
		hintManager.addHintFor(lblLoggingSamplerate, lm.getString(1012));
		hintManager.addHintFor(txtLoggingSamplerate, lm.getString(1012));
		hintManager.addHintFor(lblLoggingFilename, lm.getString(1013));
		hintManager.addHintFor(txtLoggingFilename, lm.getString(1013));
		hintManager.addHintFor(bttnStartLogger, lm.getString(1014));

		hintManager.addHintFor(lblDeleteHeader, lm.getString(1015));
		hintManager.addHintFor(bttnDeleteBttn, lm.getString(1016));
	}

	@Override
	public void updateUIContent() {
		LoggerManager lm = LoggerManager.getInstance();
		UsbCube logger = (UsbCube) lm.getActiveLogger();

		if(logger == null){
			return;
		}

		//update version
		lblVersioncontent.setText(logger.getVersion().toString());

		//update id
		lblIdContent.setText(logger.getId().toString());

		//update name
		if(!nameLoaded) {
			txtNameContent.setText(logger.getName());
			nameLoaded = true;
		}

		//update logger datetime
		lblLoggerDateTimeContent.setText(getLoggerDatetimeString());

		//update logger status
		if(logger.isLogging()){
			lblLoggingStatusContent.setText(LanguageManager.getInstance().getString(1087));
		}else {
			lblLoggingStatusContent.setText(LanguageManager.getInstance().getString(1088));
		}

		//update samplerate
		if(!samplerateLoaded) {
			txtLoggingSamplerate.setText(Long.toString(logger.getSampleRate()));
			samplerateLoaded = true;
		}

		//update start button
		if(logger.isLogging()){
			bttnStartLogger.setText(LanguageManager.getInstance().getString(983));
		}else{
			bttnStartLogger.setText(LanguageManager.getInstance().getString(982));
		}

	}

	private String getLoggerDatetimeString(){
		UsbCube logger = (UsbCube)LoggerManager.getInstance().getActiveLogger();
		org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
		return formatter.print(logger.getDatetime());
	}

	private void BttnSaveNamePressed(){
		String name = txtNameContent.getText();
		UsbCube logger = (UsbCube)LoggerManager.getInstance().getActiveLogger();
		LanguageManager lm = LanguageManager.getInstance();

		if(name.length() > LielasSettingsProtocolName.MAX_LOGGER_NAME_LENGTH){
			new Toast(mainFrame, lm.getString(1052));
			txtNameContent.setText(logger.getName());
			return;
		}

		if(name.length() == 0){
			new Toast(mainFrame, lm.getString(1051));
			txtNameContent.setText(logger.getName());
			return;
		}

		SaveNameWorker worker = new SaveNameWorker(this, name);
		worker.execute();
	}

	class SaveNameWorker extends SwingWorker<Object, UsbCube>{
		private JPanel parent;
		private String name;

		public SaveNameWorker(JPanel panel, String name){
			this.parent = panel;
			this.name = name;
		}

		@Override
		protected Object doInBackground(){
			LoggerManager loggerManager = LoggerManager.getInstance();
			UsbCubeSerialInterface com = (UsbCubeSerialInterface) loggerManager.getActiveLogger().getCommunicationInterface();
            com.getLastError();
            com.flush();
			com.setName(name, (UsbCube)loggerManager.getActiveLogger());
			return (UsbCube)loggerManager.getActiveLogger();
		}

		@Override
		public void done(){
			mainFrame.updateUIContent();

			try{
				UsbCube logger = (UsbCube)get();
				String err = logger.getCommunicationInterface().getLastError();

				if(err == null){
					new Toast(mainFrame, LanguageManager.getInstance().getString(1033));
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

	private void BttnSetPcClockPressed(){
		LoggerManager loggerManager = LoggerManager.getInstance();
		UsbCube logger = (UsbCube)loggerManager.getActiveLogger();

		if(logger.isLogging()){
			new Toast(mainFrame, LanguageManager.getInstance().getString(1104));
			return;
		}

		if(logger.isRealtimeLogging()) {
			new Toast(mainFrame, LanguageManager.getInstance().getString(1105));
			return;
		}

		logger.setDatetime(new Date().getTime());
		SetClockWorker setClockWorker = new SetClockWorker(this);
		setClockWorker.execute();
	}

	private void BttnSetClockPressed(){
		Date date;
		long ms;
		LoggerManager loggerManager = LoggerManager.getInstance();
		UsbCube logger = (UsbCube)loggerManager.getActiveLogger();

		if(logger.isLogging()){
			new Toast(mainFrame, LanguageManager.getInstance().getString(1104));
			return;
		}

		if(logger.isRealtimeLogging()) {
			new Toast(mainFrame, LanguageManager.getInstance().getString(1105));
			return;
		}

		try{
			date = datePicker.getDate();
			ms = date.getTime();

			//only valid betwenn 1.1.2000 and 1.1.2100
			if(ms < 946684800000L || ms > 4102444800000L){
				new Toast(mainFrame, LanguageManager.getInstance().getString(1068));
				return;
			}

		}catch (Exception e){
			new Toast(mainFrame, LanguageManager.getInstance().getString(1068));
			return;
		}

		try{
			timeSpinner.commitEdit();
			date = (Date)timeSpinner.getValue();
			ms += date.getTime();
			logger.setDatetime(ms);
		}catch (Exception e){
			new Toast(mainFrame, LanguageManager.getInstance().getString(1069));
			return;
		}

		SetClockWorker setClockWorker = new SetClockWorker(this);
		setClockWorker.execute();

	}

	class SetClockWorker extends SwingWorker<Object, UsbCube>{

		private JPanel parent;

		public SetClockWorker(JPanel panel){
			this.parent = panel;
		}

		@Override
		protected Object doInBackground() throws Exception {
			LoggerManager lm = LoggerManager.getInstance();
			UsbCube logger = (UsbCube) LoggerManager.getInstance().getActiveLogger();
			UsbCubeSerialInterface com = (UsbCubeSerialInterface)logger.getCommunicationInterface();
			com.getLastError();
            com.flush();
			if(!com.setClock(logger)){

                com.flush();
                com.getLastError();
                com.setClock(logger);
            }
			return logger;
		}

		@Override
		public void done(){
			mainFrame.updateUIContent();

			try {
				Logger logger = (Logger)get();
				if(logger!= null){
					String error = logger.getCommunicationInterface().getLastError();

					if(error == null){
						new Toast(mainFrame, LanguageManager.getInstance().getString(1034));
					}else{
						new Toast(mainFrame, "Error: " + error);
					}
				}else{
					new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
				}
			} catch (InterruptedException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
				e.printStackTrace();
			} catch (ExecutionException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
				e.printStackTrace();
			}
		}
	}


	private void BttnStartLogging(){
		long samplerate;
		String name;

		LoggerManager lm = LoggerManager.getInstance();
		UsbCube logger = (UsbCube) lm.getActiveLogger();

		if(!logger.isLogging()) {

			if(logger.isRealtimeLogging()){
				new Toast(mainFrame, LanguageManager.getInstance().getString(1099));
				return;
			}

			//check samplerate
			try{
				samplerate = Long.parseLong(txtLoggingSamplerate.getText());
			}catch(NumberFormatException e){
				new Toast(mainFrame, LanguageManager.getInstance().getString(1070));
				return;
			}

			if(samplerate < 1){
				new Toast(mainFrame, LanguageManager.getInstance().getString(1070));
				return;
			}

			if(samplerate > 86000){
				new Toast(mainFrame, LanguageManager.getInstance().getString(1070));
				return;
			}

			logger.setSampleRate(samplerate);

			//check logfile name
			name = txtLoggingFilename.getText();

			if(name == null || name.equals("")){
				logger.setLogfileName(null);
			}else{
				if(name.length() > 30){
					name = name.substring(0, 30);
				}
				logger.setLogfileName(name);
			}

			StartLoggingWorker worker = new StartLoggingWorker(this);
			worker.execute();
		}else{
			StopLoggingWorker worker = new StopLoggingWorker(this);
			worker.execute();
		}
	}

	class StartLoggingWorker extends SwingWorker<Object, UsbCube>{
		private JPanel parent;

		public StartLoggingWorker(JPanel panel){
			parent = panel;
		}

		@Override
		public Object doInBackground(){
			int i;
			LoggerManager lm = LoggerManager.getInstance();
			UsbCube logger = (UsbCube)lm.getActiveLogger();
			UsbCubeSerialInterface com = (UsbCubeSerialInterface)logger.getCommunicationInterface();
            com.getLastError();
            com.flush();

			//set samplerate
			for(i = 0; i < 3; i++) {
				if (com.setSamplerate(logger)) {
					break;
				}
				if(i >= 2){
					com.flush();
					return null;
				}
				try {
					Thread.sleep(20);
				}catch(Exception e){}
			}
			try {
				Thread.sleep(20);
			}catch(Exception e){}

			//set logfilename
			if(logger.getLogfileName() != null) {
				for (i = 0; i < 3; i++) {
					if (com.setLoggerFilename(logger)) {
						break;
					}
					if (i >= 2) {
						com.flush();
						return null;
					}
					try {
						Thread.sleep(20);
					}catch(Exception e){}
				}
			}
			try {
				Thread.sleep(20);
			}catch(Exception e){}

			//start logger
			for(i = 0; i < 3; i++) {
				if (com.setLoggerStatus(logger, true)) {
					break;
				}
				if(i >= 2){
					com.flush();
					return null;
				}
				try {
					Thread.sleep(20);
				}catch(Exception e){}
			}

			return logger;
		}

		@Override
		public void done(){
			mainFrame.updateUIContent();

			try {
				Logger logger = (Logger)get();
				if(logger!= null){
					String error = logger.getCommunicationInterface().getLastError();

					if(error == null){
						new Toast(mainFrame, LanguageManager.getInstance().getString(1035));
					}else{
						new Toast(mainFrame, "Error: " + error);
					}
				}else{
					new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
				}
			} catch (InterruptedException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
				e.printStackTrace();
			} catch (ExecutionException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
				e.printStackTrace();
			}
		}
	}

	class StopLoggingWorker extends SwingWorker<Object, UsbCube>{
		private JPanel parent;

		public StopLoggingWorker(JPanel panel){
			parent = panel;
		}

		@Override
		public Object doInBackground(){
			int i;
			LoggerManager lm = LoggerManager.getInstance();
			UsbCube logger = (UsbCube)lm.getActiveLogger();
			UsbCubeSerialInterface com = (UsbCubeSerialInterface)logger.getCommunicationInterface();
            com.getLastError();
            com.flush();

			//stop logger
			for(i = 0; i < 3; i++) {
				if (com.setLoggerStatus(logger, false)) {
					break;
				}
				if(i >= 2){
					com.flush();
					return null;
				}
				try {
					Thread.sleep(20);
				}catch(Exception e){}
			}

			//get new record count
			int receivedCount = logger.getRecordCount();
			if(!com.getLogfileCount(logger)){
				com.flush();
				return null;
			}

			//get new logfile informations
			while(receivedCount < logger.getRecordCount()){
				com.getLogfileProperties(logger, receivedCount);
				receivedCount += 1;
			}

			return logger;
		}

		@Override
		public void done(){
			mainFrame.updateUIContent();

			try {
				Logger logger = (Logger)get();
				if(logger!= null){
					String error = logger.getCommunicationInterface().getLastError();

					if(error == null){
						new Toast(mainFrame, LanguageManager.getInstance().getString(1036));
					}else{
						new Toast(mainFrame, "Error: " + error);
					}
				}else{
					new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
				}
			} catch (InterruptedException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
				e.printStackTrace();
			} catch (ExecutionException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
				e.printStackTrace();
			}
		}

	}

	private void BttnDeleteClicked(){
		LoggerManager lm = LoggerManager.getInstance();
		UsbCube logger = (UsbCube) lm.getActiveLogger();


		int reply = JOptionPane.showConfirmDialog(null, LanguageManager.getInstance().getString(1095),
				LanguageManager.getInstance().getString(1094), JOptionPane.YES_NO_OPTION);


		if(reply != JOptionPane.YES_OPTION){
			return;
		}

		if(logger.isLogging()){
			new Toast(mainFrame, LanguageManager.getInstance().getString(1096));
			return;
		}

		if(logger.isRealtimeLogging()){
			new Toast(mainFrame, LanguageManager.getInstance().getString(1097));
			return;
		}

		DeleteWorker dw = new DeleteWorker(this);
		dw.execute();
	}

	class DeleteWorker extends SwingWorker <Object, Object>{
		JPanel parent;

		public DeleteWorker(JPanel panel){
			this.parent = panel;
		}

		@Override
		protected Object doInBackground() throws Exception {
			LoggerManager lm = LoggerManager.getInstance();
			LoggerRecord lr;
			UsbCube logger = (UsbCube)lm.getActiveLogger();
			UsbCubeSerialInterface com = (UsbCubeSerialInterface)logger.getCommunicationInterface();
			com.getLastError();
			com.flush();

			if(com.deleteLogfiles(logger)){
				//logfiles deleted
				logger.removeAllRecordsets();
				LoggerRecordManager.getInstance().removeAll();
				return logger;
			}
			return logger;
		}

		@Override
		public void done(){
			mainFrame.updateUIContent();

			try {
				Logger logger = (Logger)get();
				if(logger!= null){
					String error = logger.getCommunicationInterface().getLastError();

					if(error == null){
						new Toast(mainFrame, LanguageManager.getInstance().getString(1084));
					}else{
						new Toast(mainFrame, "Error: " + error);
					}
				}else{
					new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
				}
			} catch (InterruptedException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
				e.printStackTrace();
			} catch (ExecutionException e) {
				new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
				e.printStackTrace();
			}
		}

	}

	class UpdateTimeTask extends TimerTask{

		@Override
		public void run() {
			UsbCube logger = (UsbCube)LoggerManager.getInstance().getActiveLogger();

            if(logger == null){
                return;
            }

			Date pcDate = new Date();
			Date loggerDate = new Date(logger.getDatetime());
			long timediff = loggerDate.getTime() - pcDate.getTime();
			org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");

			if(timediff > 2000 || timediff < -2000){
				lblLoggerDateTimeContent.setText(formatter.print(loggerDate.getTime()));
				lblPcDateTimeContent.setText(formatter.print(pcDate.getTime()));
			}else{
				lblLoggerDateTimeContent.setText(formatter.print(pcDate.getTime()));
				lblPcDateTimeContent.setText(formatter.print(pcDate.getTime()));
			}


		}
	}

}