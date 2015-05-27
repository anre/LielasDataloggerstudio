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

package org.lielas.dataloggerstudio.pc.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.ColorUIResource;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;

import org.lielas.dataloggerstudio.lib.LoggerManager;
import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.LoggerType;
import org.lielas.dataloggerstudio.pc.gui.Panels.AboutPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.ConnectPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.DataPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.RealTimePanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.SettingsPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.UsbCube.UsbCubeDataPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.UsbCube.UsbCubeRealTimePanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.UsbCube.UsbCubeSettingsPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.mic.MicDataPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.mic.MicRealTimePanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.mic.MicSettingsPanel;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;


public class MainFrame extends JFrame{
	private static final long serialVersionUID = 5411559240592512178L;
	private JPanel mainPanel;

	//content panels
	GradientPanel contentPanel;
	ConnectPanel connectPanel;
	SettingsPanel settingsPanel;
	DataPanel dataPanel;
	RealTimePanel realtimePanel;
	AboutPanel aboutPanel;
	
	//Header buttons
	HeaderButton connectBttn;
	HeaderButton settingsBttn;
	HeaderButton dataBttn;
	HeaderButton realTimeBttn;
	HeaderButton aboutBttn;
	
	//status Bar
	JLabel lblConnectionState;
	
	private MouseOverHintManager hintManager;
	
	
	public MainFrame(){
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			//UIManager.put("JXMonthView.background", new ColorUIResource(123,123,123));
			UIManager.put("JXMonthView.monthStringBackground", new ColorUIResource(152,239,20));
			UIManager.put("JXDatePicker.arrowIcon", new ImageIcon(getClass().getClassLoader().getResource("img/icons/date_icon.png")));
			
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		
		List<Image> icons = new ArrayList<Image>();
		icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/icons/icon16.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/icons/icon20.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/icons/icon32.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/icons/icon40.png")));
		
		setIconImages(icons);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 700);
		setMinimumSize(new Dimension(1024, 700));
		mainPanel = new JPanel();
		mainPanel.setBackground(new Color(245, 245, 245));
		setContentPane(mainPanel);
		
		
		

		/**
		 * 					Header
		 */
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.LIGHT_GRAY));
		mainPanel.add(statusPanel, BorderLayout.SOUTH);
		

		JLabel lblHints = new JLabel("");
		hintManager = new MouseOverHintManager(lblHints);
		
		GradientPanel headerPanel = new GradientPanel(new Color(100,100,100), new Color(70,70,70));
		//GradientPanel headerPanel = new GradientPanel(new Color(255,255,255), new Color(245,245,245));
		headerPanel.setBorder(null);
		//headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(192, 192, 192)));
		headerPanel.setBackground(Color.GRAY);
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		headerPanel.setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setPreferredSize(new Dimension(0, 10));
		headerPanel.add(verticalStrut, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(10, 25));
		headerPanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setOpaque(false);
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);
		panel.add(panel_1, BorderLayout.WEST);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
		panel_4.setOpaque(false);
		panel_4.setPreferredSize(new Dimension(40, 25));
		panel_1.add(panel_4);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
		panel_2.setOpaque(false);
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		panel_2.setPreferredSize(new Dimension(80, 25));
		panel_1.add(panel_2);
		
		connectBttn = new HeaderButton("Connect");
		connectBttn.setPreferredSize(new Dimension(102, 25));
		panel_1.add(connectBttn);
		connectBttn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ConnectBttnPressed();
			}
		});
		connectBttn.setActive(true);
		
		settingsBttn = new HeaderButton("Settings");
		settingsBttn.setPreferredSize(new Dimension(102, 25));
		panel_1.add(settingsBttn);
		settingsBttn.setEnabled(false);
		
		dataBttn = new HeaderButton("Data");
		dataBttn.setPreferredSize(new Dimension(82, 25));
		panel_1.add(dataBttn);
		dataBttn.setEnabled(false);
		
		realTimeBttn = new HeaderButton("Real-Time Data");
		realTimeBttn.setPreferredSize(new Dimension(144, 25));
		realTimeBttn.setBorder(null);
		realTimeBttn.setEnabled(false);
		panel_1.add(realTimeBttn);
		
		aboutPanel = new AboutPanel(this);
		aboutBttn = new HeaderButton("About");
		aboutBttn.setPreferredSize(new Dimension(70, 25));
		aboutBttn.setBorder(null);
		//panel_1.add(aboutBttn);

		
		dataBttn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DataBttnPressed();
			}
		});
		settingsBttn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SettingsBttnPressed();
			}
		});
		realTimeBttn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RealTimeBttnPressed();
			}
		});
		aboutBttn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AboutBttnPressed();
			}
		});
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		panel_3.setPreferredSize(new Dimension(10, 25));
		panel_3.setOpaque(false);
		panel.add(panel_3, BorderLayout.CENTER);
		
		JPanel panel_5 = new JPanel();
		panel_5.setOpaque(false);
		panel_5.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
		panel_5.setPreferredSize(new Dimension(40, 25));
		panel.add(panel_5, BorderLayout.EAST);
		
		ImagePanel lielasLogo = new ImagePanel(("lielas_logo_small.png"));
		headerPanel.add(lielasLogo, BorderLayout.EAST);
		lielasLogo.setOpaque(false);
		lielasLogo.setVerticalOrientation(ImagePanel.ORIENTATION_LEFT);
		lielasLogo.setPreferredSize(new Dimension(200, 50));
		
		JPanel bodyPanel = new JPanel();
		bodyPanel.setBorder(null);
		bodyPanel.setBackground(Color.WHITE);
		mainPanel.add(bodyPanel);
		bodyPanel.setLayout(new BorderLayout(0, 0));
		
		contentPanel = new GradientPanel(new Color(250,250,250), new Color(240,240,240));
		contentPanel.setBorder(new MatteBorder(0, 1, 0, 1, (Color) new Color(192, 192, 192)));
		bodyPanel.add(contentPanel);
		
		connectPanel = new ConnectPanel(this);
		connectPanel.setBorder(null);
		
		//settingsPanel = (SettingsPanel) new MicSettingsPanel();
		//dataPanel = (DataPanel) new MicDataPanel();
		settingsPanel = null;
		dataPanel = null;

		contentPanel.setLayout(new BorderLayout(0, 0));
		contentPanel.add(connectPanel);
		statusPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("10px"),
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				ColumnSpec.decode("max(150px;pref)"),
				ColumnSpec.decode("10px"),},
			new RowSpec[] {
				RowSpec.decode("1px"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("1px"),}));
		lblConnectionState = new JLabel("   Not connected");
		lblConnectionState.setPreferredSize(new Dimension(150, 14));
		lblConnectionState.setBorder(new MatteBorder(0, 1, 0, 0, (Color) new Color(192, 192, 192)));
		lblConnectionState.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(lblConnectionState, "4, 2, left, top");

		statusPanel.add(lblHints, "2, 2, left, top");

		
		updateLanguage();

        setExtendedState(this.getExtendedState() |  JFrame.MAXIMIZED_BOTH);
		pack();
		revalidate();
		repaint();
		
		
		connectPanel.getSerialPorts();
		hintManager.enableHints(this);
	}
	
	public void ConnectBttnPressed(){
		DeactivateHeaderButtons();
		connectBttn.setActive(true);
		
		contentPanel.removeAll();
		contentPanel.add(connectPanel);

		hintManager.removeAll();
		hintManager.enableHints(this);
		updateLanguage();

		revalidate();
		repaint();
	}
	
	public void SettingsBttnPressed(){
		DeactivateHeaderButtons();
		settingsBttn.setActive(true);
		
		contentPanel.removeAll();
		contentPanel.add(settingsPanel);

		hintManager.removeAll();
		hintManager.enableHints(this);
		updateLanguage();

		revalidate();
		repaint();
	}
	
	public void DataBttnPressed(){
		DeactivateHeaderButtons();
		dataBttn.setActive(true);
		
		contentPanel.removeAll();
		contentPanel.add(dataPanel);

		hintManager.removeAll();
		hintManager.enableHints(this);
		updateLanguage();
		
		revalidate();
		repaint();
	}
	
	public void RealTimeBttnPressed(){
		DeactivateHeaderButtons();
		realTimeBttn.setActive(true);
		
		contentPanel.removeAll();
		contentPanel.add(realtimePanel);

		hintManager.removeAll();
		hintManager.enableHints(this);
		updateLanguage();
		
		revalidate();
		repaint();
	}
	
	public void AboutBttnPressed(){
		DeactivateHeaderButtons();
		aboutBttn.setActive(true);
		
		contentPanel.removeAll();
		contentPanel.add(aboutPanel);

		hintManager.removeAll();
		hintManager.enableHints(this);
		updateLanguage();
		
		revalidate();
		repaint();
	}
	
	public void DeactivateHeaderButtons(){
		connectBttn.setActive(false);
		settingsBttn.setActive(false);
		dataBttn.setActive(false);
		realTimeBttn.setActive(false);
		aboutBttn.setActive(false);
	}
	
	public void updateLanguage(){
		LanguageManager lm = LanguageManager.getInstance();
		lm.loadLanguage();
		
		
		this.setTitle(lm.getString(900) + " " + lm.getString(901));
		
		connectBttn.setText(lm.getString(902));
		settingsBttn.setText(lm.getString(903));
		dataBttn.setText(lm.getString(904));
		realTimeBttn.setText(lm.getString(905));
		aboutBttn.setText(lm.getString(1086));

		hintManager.removeAll();
		hintManager.addHintFor(connectBttn, lm.getString(906));
		hintManager.addHintFor(settingsBttn, lm.getString(907));
		hintManager.addHintFor(dataBttn, lm.getString(908));
		hintManager.addHintFor(realTimeBttn, lm.getString(909));
		hintManager.addHintFor(aboutBttn, lm.getString(910));
		

		connectPanel.updateLanguage(lm, hintManager);
		
		if(settingsPanel != null){
			settingsPanel.updateLanguage(lm, hintManager);
		}
		
		if(dataPanel != null){
			dataPanel.updateLanguage(lm,  hintManager);
		}
		
		if(realtimePanel != null){
			realtimePanel.updateLanguage(lm,  hintManager);
		}
		
		aboutPanel.updateLanguage(lm, hintManager);
		
		updateStatusBar();
		
	}
	
	public void updateStatusBar(){
		LoggerManager loggerManager = LoggerManager.getInstance();
		LanguageManager languageManager = LanguageManager.getInstance();
		Logger logger = loggerManager.getActiveLogger();
		
		if(logger == null)
			return;

		try{
			if(logger.getCommunicationInterface().isOpen()){
				lblConnectionState.setText("   " + languageManager.getString(1023));
			}else{
				lblConnectionState.setText("   " + languageManager.getString(1022));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void updateUIContent(){
		
		connectPanel.updateUIContent();
		if(settingsPanel == null){
			createSettingsPanel();
		}else{
			settingsPanel.updateUIContent();
		}
		
		if(dataPanel == null){
			createDataPanel();
		}else{
			dataPanel.updateUIContent();
		}
		
		if(realtimePanel == null){
			createRealTimePanel();
		}else{
			realtimePanel.updateUIContent();
		}
		
		aboutPanel.updateUIContent();
		
		updateStatusBar();
		revalidate();
		repaint();
	}

	public void disconnectLogger(){
		settingsPanel = null;
		dataPanel = null;
		realtimePanel = null;
		settingsBttn.setEnabled(false);
		dataBttn.setEnabled(false);
		realTimeBttn.setEnabled(false);
		LoggerManager.getInstance().clear();
	}
	
	public void createSettingsPanel(){
		LoggerManager lm = LoggerManager.getInstance();
		
		if(lm.getActiveLogger() == null){
			return;
		}
		
		switch(lm.getActiveLogger().getLoggerTpye().getType()){
			case LoggerType.USB_STICK:
				settingsPanel = (SettingsPanel) new MicSettingsPanel(this);
				settingsPanel.updateUIContent();
				settingsBttn.setEnabled(true);
				break;
			case LoggerType.USB_CUBE:
				settingsPanel = (SettingsPanel) new UsbCubeSettingsPanel(this);
				settingsPanel.updateUIContent();
				settingsBttn.setEnabled(true);
				break;
		}
		settingsPanel.updateLanguage(LanguageManager.getInstance(), hintManager);
	}
	
	public void createDataPanel(){
		LoggerManager lm = LoggerManager.getInstance();
		
		if(lm.getActiveLogger() == null){
			return;
		}
		
		switch(lm.getActiveLogger().getLoggerTpye().getType()){
			case LoggerType.USB_STICK:
				dataPanel = (DataPanel) new MicDataPanel(this);
				dataPanel.updateUIContent();
				dataBttn.setEnabled(true);
				break;
			case LoggerType.USB_CUBE:
				dataPanel = (DataPanel) new UsbCubeDataPanel(this);
				dataPanel.updateUIContent();
				dataBttn.setEnabled(true);
				break;
		}
		dataPanel.updateLanguage(LanguageManager.getInstance(), hintManager);
	}
	
	public void createRealTimePanel(){

		LoggerManager lm = LoggerManager.getInstance();
		
		if(lm.getActiveLogger() == null){
			return;
		}
		
		switch(lm.getActiveLogger().getLoggerTpye().getType()){
			case LoggerType.USB_STICK:
				realtimePanel = (RealTimePanel) new MicRealTimePanel(this);
				realtimePanel.updateUIContent();
				realTimeBttn.setEnabled(true);
				break;
			case LoggerType.USB_CUBE:
				realtimePanel = (RealTimePanel) new UsbCubeRealTimePanel(this);
				realtimePanel.updateUIContent();
				realTimeBttn.setEnabled(true);
				break;
		}
		realtimePanel.updateLanguage(LanguageManager.getInstance(), hintManager);
	}
}