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

import javax.swing.*;

import org.lielas.dataloggerstudio.lib.CommunicationInterface.CommunicationInterface;
import org.lielas.dataloggerstudio.lib.LoggerManager;
import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.LoggerType;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.dataloggerstudio.lib.LoggerRecordManager;
import org.lielas.dataloggerstudio.lib.CommunicationInterface.UsbCube.UsbCubeSerialInterface;
import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.MicSerialInterface;
import org.lielas.dataloggerstudio.pc.gui.BodyButton;
import org.lielas.dataloggerstudio.pc.gui.ImageButton;
import org.lielas.dataloggerstudio.pc.gui.MainFrame;
import org.lielas.dataloggerstudio.pc.gui.MouseOverHintManager;
import org.lielas.dataloggerstudio.pc.gui.Toast.Toast;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import java.awt.Font;

import javax.swing.border.MatteBorder;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jssc.SerialPortList;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

import java.awt.BorderLayout;
import java.util.concurrent.ExecutionException;

public class ConnectPanel extends DataloggerstudioPanel{

	JLabel lblType;
	JComboBox<String> cbType;
	JLabel lblComPort;
	JComboBox<String> cbComPort;
	ImageButton bttnReload;
	BodyButton bttnConnect;

	Toast connectingToast;
	
	private MainFrame mainFrame;
	
	private String[] ports;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1423424347802964495L;
	private JPanel panel_1;

	public ConnectPanel(MainFrame mainFrame){
		super();
		
		this.mainFrame = mainFrame;
		
		setBorder(null);
		setOpaque(false);
		
		String[] types = {"MIC USB Stick ultra wide test", "Cube"};
				setLayout(new BorderLayout(0, 0));
				
				panel_1 = new JPanel();
				panel_1.setOpaque(false);
				add(panel_1);
				panel_1.setLayout(new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("125px"),
						ColumnSpec.decode("center:pref"),
						ColumnSpec.decode("center:8dlu"),
						ColumnSpec.decode("center:pref"),
						ColumnSpec.decode("center:8dlu"),
						ColumnSpec.decode("center:max(70dlu;pref)"),
						ColumnSpec.decode("center:8dlu"),
						ColumnSpec.decode("center:pref"),
						ColumnSpec.decode("center:4dlu"),
						FormFactory.PREF_COLSPEC,
						ColumnSpec.decode("7dlu:grow"),},
					new RowSpec[] {
						RowSpec.decode("40dlu"),
						FormFactory.PREF_ROWSPEC,
						RowSpec.decode("15dlu"),
						FormFactory.PREF_ROWSPEC,
						RowSpec.decode("16dlu"),
						FormFactory.PREF_ROWSPEC,
						RowSpec.decode("4dlu:grow"),}));
				
				lblType = new JLabel("New label");
				panel_1.add(lblType, "2, 2, left, default");
				lblType.setHorizontalAlignment(SwingConstants.LEFT);
				lblType.setHorizontalTextPosition(SwingConstants.LEFT);
				lblType.setFont(new Font("Tahoma", Font.PLAIN, 11));
				
				cbType = new JComboBox(types);
				panel_1.add(cbType, "6, 2, fill, default");
				cbType.setBorder(null);
				
				lblComPort = new JLabel("New label");
				panel_1.add(lblComPort, "2, 4, left, default");
				lblComPort.setHorizontalAlignment(SwingConstants.LEFT);
				lblComPort.setHorizontalTextPosition(SwingConstants.LEFT);
				lblComPort.setFont(new Font("Tahoma", Font.PLAIN, 11));
				
				cbComPort = new JComboBox();
				/*cbComPort.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						ReloadBttnPressed();
					}
				});*/
				panel_1.add(cbComPort, "6, 4, fill, default");
				cbComPort.setBorder(null);
				
				bttnReload = new ImageButton("reload_icon.png", "reload_icon_clicked.png");
				panel_1.add(bttnReload, "8, 4");
				bttnReload.setBorder(BorderFactory.createRaisedBevelBorder());
				bttnReload.setPreferredSize(new Dimension(24, 24));
				bttnReload.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						ReloadBttnPressed();
					}
				});
				
				bttnConnect = new BodyButton("New button");
				panel_1.add(bttnConnect, "6, 6, right, default");
				bttnConnect.setFont(new Font("Tahoma", Font.BOLD, 11));
				bttnConnect.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						ConnectBttnPressed();
					}
				});

	}
	
	
	public void updateLanguage(LanguageManager lm, MouseOverHintManager hintManager){
		
		lblType.setText(lm.getString(910));
		lblComPort.setText(lm.getString(911));
		
		cbType.removeAllItems();
		cbType.addItem(lm.getString(1020));
		cbType.addItem(lm.getString(1021));
		
		cbComPort.removeAllItems();
		getSerialPorts();
		
		bttnConnect.setText(lm.getString(912));

		hintManager.addHintFor(lblType, lm.getString(915));
		hintManager.addHintFor(cbType, lm.getString(915));
		hintManager.addHintFor(lblComPort, lm.getString(916));
		hintManager.addHintFor(cbComPort, lm.getString(916));
		hintManager.addHintFor(bttnConnect, lm.getString(913));
		hintManager.addHintFor(bttnReload, lm.getString(914));
	}
	
	private void ReloadBttnPressed(){
		getSerialPorts();
		mainFrame.updateUIContent();
	}
	
	public void getSerialPorts(){
		ports = SerialPortList.getPortNames();
		cbComPort.removeAllItems();
		for(int i = 0; i < ports.length; i++){
			cbComPort.addItem(ports[i]);
		}
		cbComPort.setSelectedItem("COM2");
		cbType.setSelectedIndex(1);
	}
	
	private void ConnectBttnPressed(){
		LoggerManager lm = LoggerManager.getInstance();
		Logger logger = (Logger) lm.getActiveLogger();

		if(logger != null){
			CommunicationInterface com = logger.getCommunicationInterface();
			if(com != null){
				if(com.isOpen()){
					int reply = JOptionPane.showConfirmDialog(null, LanguageManager.getInstance().getString(1093),
							LanguageManager.getInstance().getString(1092), JOptionPane.YES_NO_OPTION);


					if(reply != JOptionPane.YES_OPTION){
						return;
					}
					com.close();
				}
			}
		}
		mainFrame.disconnectLogger();
		LoggerRecordManager.getInstance().removeAll();
		
		//get selected type
		String type = String.valueOf(cbType.getSelectedItem());
		if(type == null){
			//TODO
			return;
		}
		
		//parse type
		LoggerType lt = new LoggerType(type);
		if(lt.getType() == LoggerType.NONE){
			//TODO
			return;
		}
		
		ConnectWorker cw = new ConnectWorker(lt);
		cw.execute();

		connectingToast = new  Toast(mainFrame, "Connecting...");
	}
	
	class ConnectWorker extends SwingWorker<Object, Logger>{
		
		private LoggerType lt;
		
		public ConnectWorker(LoggerType loggerType){
			lt = loggerType;
		}

		@Override 
		public Logger doInBackground(){
			Logger logger = null;
			LoggerManager lm = LoggerManager.getInstance();
			
			//create logger
			if(lt.getType() == LoggerType.USB_STICK){
				logger = (Logger)new MicUSBStick();
				
				//connect
				MicSerialInterface com = (MicSerialInterface)logger.getCommunicationInterface();
				String port = String.valueOf(cbComPort.getSelectedItem());
				
				if(com.isOpen()){
					return logger;
				}
				
				if(!com.connect(port)){
					com.close();
					return logger;
				}
				
				com.stopRealTimeLogging();
				
				if(!com.readIdentifier((MicUSBStick)logger)){
					com.close();
					return logger;
				}
				if(!com.readArguments((MicUSBStick)logger)){
					com.close();
					return logger;
				}
				lm.add((Logger)logger);
				lm.setLastLoggerActive();
			}else if(lt.getType() == LoggerType.USB_CUBE){
				logger = (Logger)new UsbCube();
				UsbCube usbCube = (UsbCube)logger;
				
				//connect
				String port = String.valueOf(cbComPort.getSelectedItem());
				UsbCubeSerialInterface com = (UsbCubeSerialInterface)logger.getCommunicationInterface();
				
				if(com.isOpen()){
					com.close();
				}

				if(!com.connect(port)){
					com.close();
					return logger;
				}

				//stop realtime logging
				for(int i = 0; i < 3; i++){
					if(com.stopRealTimeLogging()){
						break;
					}
					if(i == 2){
						com.close();
						return logger;
					}
				}

				//get version
				if(!com.getVersion((UsbCube)logger)){
					com.close();
					return logger;
				}

				//get model
				if(!com.getDatasetStructure((UsbCube)logger)){
					com.close();
					return logger;
				}

				//get id
				if(!com.getId((UsbCube)logger)){
					com.close();
					return logger;
				}

				//get logger name
				if(!com.getName((UsbCube)logger)){
					com.close();
					return logger;
				}

				//get samplerate
				if(!com.getSamplerate((UsbCube)logger)){
					com.close();
					return logger;
				}

				//get logger status
				if(!com.getLoggerStatus((UsbCube)logger)){
					com.close();
					return logger;
				}

				//get datetime
				if(!com.getClock((UsbCube)logger)){
					com.close();
					return logger;
				}

				//get logfile count
				if(!com.getLogfileCount((UsbCube)logger)){
					com.close();
					return logger;
				}

				for(int i = 0; i < usbCube.getRecordCount(); i++){
					com.getLogfileProperties(usbCube, i);
					/*if(!com.getLogfileProperties(usbCube, i)){
						com.close();
						return logger;
					}*/
				}
				
				lm.add((Logger)logger);
				lm.setLastLoggerActive();
				
			}
			return logger;
		}
		
		@Override
		public void done(){

			connectingToast.close();
			connectingToast = null;

			try {
				Logger logger = (Logger)get();
				if(logger!= null){
					if(logger.getCommunicationInterface().isOpen()){
						if(lt.getType() == LoggerType.USB_STICK) {
							new Toast(mainFrame, LanguageManager.getInstance().getString(1028));
						}else if(lt.getType() == LoggerType.USB_CUBE){
							new Toast(mainFrame, LanguageManager.getInstance().getString(1032));
						}
					}else{
						String error = logger.getCommunicationInterface().getLastError();
						
						if(error == null){
							new Toast(mainFrame, "Error: " + LanguageManager.getInstance().getString(1039));
						}else{
							new Toast(mainFrame, "Error: " + error);
						}
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
			mainFrame.updateUIContent();
		}
	}
	
	public void updateUIContent(){
		
	}
}


