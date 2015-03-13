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

import org.lielas.dataloggerstudio.lib.LoggerManager;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.dataloggerstudio.lib.LoggerRecordManager;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.dataloggerstudio.pc.CommunicationInterface.mic.MicSerialInterface;
import org.lielas.dataloggerstudio.pc.gui.BodyButton;
import org.lielas.dataloggerstudio.pc.gui.MainFrame;
import org.lielas.dataloggerstudio.pc.gui.MouseOverHintManager;
import org.lielas.dataloggerstudio.pc.gui.Panels.MenuPanels.ExportMenuPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.MenuPanels.ExportPanel.ExportPanel;
import org.lielas.dataloggerstudio.pc.gui.Panels.DataPanel;
import org.lielas.dataloggerstudio.pc.gui.Toast.Toast;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.BevelBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import java.awt.FlowLayout;

import javax.swing.ScrollPaneConstants;
import javax.swing.DefaultComboBoxModel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;



public class MicDataPanel extends DataPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5131409534270240692L;
	private JTable table;

	private MainFrame mainFrame;
	
	//Buttons
	BodyButton bttnReadDatalogger;

	ExportMenuPanel exportMenuPanel;
	
	JLabel lblDataHeader;
	JLabel lblSampleRate;
	JLabel lblSampleRateContent;
	JLabel lblSamples;
	JLabel lblSamplesContent;

	ExportPanel exportPanel;
	
	private boolean downloadRunning = false;
	JScrollPane tableScrollPane;
	
	public MicDataPanel(MainFrame mainFrame){
		super();
		
		this.mainFrame = mainFrame;
		
		setPreferredSize(new Dimension(800, 500));
		setLayout(new BorderLayout(0, 0));

		JPanel menuPanel = new JPanel();
		menuPanel.setBorder(new MatteBorder(0, 1, 0, 0, (Color) Color.LIGHT_GRAY));
		menuPanel.setOpaque(false);
		menuPanel.setBackground(Color.ORANGE);
		menuPanel.setPreferredSize(new Dimension(300, 10));
		
		JScrollPane menuScrollPane = new JScrollPane(menuPanel);
		menuScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		menuScrollPane.setOpaque(false);
		menuScrollPane.getViewport().setOpaque(false);
		menuScrollPane.setBackground(Color.ORANGE);
		menuScrollPane.setBorder(null);
		menuScrollPane.setPreferredSize(new Dimension(300, 2000));
		add(menuScrollPane, BorderLayout.EAST);
		
		JPanel tablePanel = new JPanel();
		tablePanel.setOpaque(false);
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
		tableScrollPane.setVisible(false);
		tablePanel.add(tableScrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		tableScrollPane.setViewportView(table);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null}
			},
			new String[] {
				"Date", "Temperature", "Humidity"
			}
		));
		
		menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel dataPanel = new JPanel();
		menuPanel.add(dataPanel);
		dataPanel.setPreferredSize(new Dimension(300, 150));
		dataPanel.setOpaque(false);
		dataPanel.setBackground(Color.CYAN);
		dataPanel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.LIGHT_GRAY));
		dataPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("20px"),
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("20px"),
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("5px:grow"),},
			new RowSpec[] {
				RowSpec.decode("5px"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("23px:grow"),}));
		
		lblDataHeader = new JLabel("Data");
		lblDataHeader.setFont(new Font("Tahoma", Font.BOLD, 14));
		dataPanel.add(lblDataHeader, "2, 2, left, center");
		
		lblSampleRateContent = new JLabel("");
		lblSampleRateContent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dataPanel.add(lblSampleRateContent, "4, 4, left, center");
		
		lblSampleRate = new JLabel("Samplerate");
		lblSampleRate.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dataPanel.add(lblSampleRate, "2, 4, left, center");
		
		lblSamplesContent = new JLabel("");
		lblSamplesContent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dataPanel.add(lblSamplesContent, "4, 6, left, center");
		
		lblSamples = new JLabel("Samples");
		lblSamples.setFont(new Font("Tahoma", Font.PLAIN, 11));
		dataPanel.add(lblSamples, "2, 6, left, center");
		
		bttnReadDatalogger = new BodyButton("Read Data");
		bttnReadDatalogger.setFont(new Font("Tahoma", Font.BOLD, 11));
		bttnReadDatalogger.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BttnReadDataPressed();
			}
		});
		dataPanel.add(bttnReadDatalogger, "2, 8, center, center");



		exportMenuPanel = new ExportMenuPanel(mainFrame);
		menuPanel.add(exportMenuPanel);
		
		
		int h = (int)dataPanel.getPreferredSize().getHeight() + (int)exportMenuPanel.getPreferredSize().getHeight();
		menuPanel.setPreferredSize(new Dimension(300, h));
		menuScrollPane.revalidate();
		
	}

	@Override
	public void updateUIContent(){

		LoggerRecordManager lrm = LoggerRecordManager.getInstance();
		LoggerRecord lr = lrm.get();
		//LanguageManager lm = LanguageManager.getInstance();
		
		if(mainFrame != null){
			if(lr != null){
				LielasTableModel ltm = new LielasTableModel(lr);
				table.setModel(ltm);
				
				lblSampleRateContent.setText(Integer.toString(lr.getSampleRate()) + "s");
				tableScrollPane.setVisible(true);
			}else{
				tableScrollPane.setVisible(false);
			}
		}

		exportMenuPanel.updateUIContent();
	}
	
	private void BttnReadDataPressed(){
		MicUSBStick logger = (MicUSBStick) LoggerManager.getInstance().getActiveLogger();
		
		if(logger == null)
			return;
		
		MicSerialInterface com = (MicSerialInterface) logger.getCommunicationInterface();
		
		
		if(com.isBusy() || downloadRunning){
			new Toast((MainFrame) SwingUtilities.getWindowAncestor(this), "Logger is busy");
		}else{
			downloadRunning = true;
			ReadDataWorker readDataWorker = new ReadDataWorker(lblSamplesContent);
			readDataWorker.execute();
		}
	}


	
	public void updateLanguage(LanguageManager lm, MouseOverHintManager hintManager){
		
		//read data panel
		lblDataHeader.setText(lm.getString(942));
		lblSampleRate.setText(lm.getString(943));
		lblSamples.setText(lm.getString(947));
		bttnReadDatalogger.setText(lm.getString(949));

		
		if(hintManager != null){
			//read data panel
			hintManager.addHintFor(lblSampleRate, lm.getString(944));
			hintManager.addHintFor(lblSampleRateContent, lm.getString(944));
			hintManager.addHintFor(lblSamples, lm.getString(948));
			hintManager.addHintFor(lblSamplesContent, lm.getString(948));
			hintManager.addHintFor(bttnReadDatalogger, lm.getString(950));

		}

		exportMenuPanel.updateLanguage(lm, hintManager);
		
		if(exportPanel != null){
			exportPanel.updateLanguage(lm, hintManager);
		}
		
	}
	
	
	/*
	 * 
	 * 				Swing workers
	 * 
	 */
	
	
	class ReadDataWorker extends SwingWorker<LoggerRecord, SampleProgress>{
		
		private JLabel lbl;
		
		public ReadDataWorker(JLabel lbl){
			this.lbl = lbl;
			
		}
		
		@Override 
		public LoggerRecord doInBackground(){
			LoggerManager loggerManager = LoggerManager.getInstance();
			MicSerialInterface com = (MicSerialInterface)loggerManager.getActiveLogger().getCommunicationInterface();
			LoggerRecord lr;
			lr = com.getData((MicUSBStick)loggerManager.getActiveLogger());
			
			while(com.addData(lr)){
				SampleProgress progress = new SampleProgress(lr.getCount() * lr.getChannels(), lr.getTotalSamples());
				publish(progress);
				if(progress.samples == progress.maxSamples){

					return lr;
				}
			}
			
			return lr;
		}
		
		@Override
		public void done(){
			try {
				LoggerRecordManager lrm = LoggerRecordManager.getInstance();
				LoggerRecord lr = get();
				lrm.add(lr);
				lrm.select(lr.getIndex());
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			mainFrame.updateUIContent();

			downloadRunning = false;
		}
		
		@Override
		protected void process(List<SampleProgress> samples){
			SampleProgress progress = samples.get(samples.size()-1);
			lbl.setText(progress.samples + "/" + progress.maxSamples);
		}
		
		
	}
	
	private class SampleProgress{
		public final int samples;
		public final int maxSamples;
		
		SampleProgress(int samples, int maxSamples){
			this.samples = samples;
			this.maxSamples = maxSamples;
		}
	}
	
	/*
	 * 
	 * 				Table model
	 * 
	 */	
	
	private class LielasTableModel extends AbstractTableModel{

		private LoggerRecord lr;
		private final String[] COLUMN_NAMES = {"Date", "Temperature [°C]", "Humidity [%]"};
		
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
			return COLUMN_NAMES[column];
		}
		
	}
	
}









