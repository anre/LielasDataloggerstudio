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

package org.lielas.dataloggerstudio.pc.CommunicationInterface.mic;

import java.time.LocalTime;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.dataloggerstudio.pc.CommunicationInterface.PcSerialInterface;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;

import jssc.SerialPort;
import jssc.SerialPortException;

public class MicSerialInterface extends PcSerialInterface{

    final private String CMD_READ_IDENTIFIER = "I\r";
    final private String CMD_READ_ARGUMENTS = "M\r";
	
    private boolean realtimeLogging = false;
    
	public MicSerialInterface(){
		super();
		this.baudrate = SerialPort.BAUDRATE_38400;
		this.databits = SerialPort.DATABITS_8;
		this.stopbits = SerialPort.STOPBITS_1;
		this.parity = SerialPort.PARITY_NONE;
		lineEnd = '\r';
	}

    @Override
    public String getPort() {
        return null;
    }

    public boolean connect(String port){
		return open(port);
	}
	
	public boolean realTimeLoggingInProgress(){
		return realtimeLogging;
	}
	
	public boolean readIdentifier(MicUSBStick logger){
		byte[] recv = null;
        
        if(!isOpen){
        	setError(LanguageManager.getInstance().getString(1041));
        	return false;
        }
        
        isBusy = true;
        
        write(CMD_READ_IDENTIFIER);
        recv = readLine();
        
        MicLoggerIdentifier loggerIdentifier = new MicLoggerIdentifier();
        if(!loggerIdentifier.parse(recv)){
        	setError(LanguageManager.getInstance().getString(1042));
            isBusy = false;
        	return false;
        }
        
        logger.setName(loggerIdentifier.getId());
        logger.setModel(loggerIdentifier.getModel().getModel());
        logger.setVersion(loggerIdentifier.getVersion());
        

        isBusy = false;
        return true;
	}
	
	public boolean readArguments(MicUSBStick logger){

		byte[] recv = null;
        
        if(!isOpen){
        	setError(LanguageManager.getInstance().getString(1041));
        	return false;
        }

        isBusy = true;
        
        write(CMD_READ_ARGUMENTS);
        recv = readLine();
        
        MicLoggerArguments loggerArguments = new MicLoggerArguments();
        if(!loggerArguments.parse(recv)){
        	setError(LanguageManager.getInstance().getString(1043));
            isBusy = false;
        	return false;
        }
        
        logger.setSampleRate(loggerArguments.getSampleRate());
        logger.setUnitClass(loggerArguments.getUnitClass());
        logger.setFirstDatasetDate(loggerArguments.getTime());
        

        isBusy = false;
        return true;
	}
	
	public boolean saveId(String id, MicUSBStick logger){
		byte[] recv = null;
		String cmd;
		
		if(id == null)
			return false;
		
        if(!isOpen){
        	setError(LanguageManager.getInstance().getString(1041));
        	return false;
        }

        isBusy = true;
        
        if(id.length() > 20){
        	id = id.substring(0, 20);
        }
		
		cmd = "J " + id + " 1" + "\r"; 
		
		try {
			sp.writeString(cmd);
		} catch (SerialPortException e) {
			setError(LanguageManager.getInstance().getString(1047));
	        isBusy = false;
			return false;
		}
		
		//parse Answer
        recv = readLine();
        while(recv != null && recv[0] == 62){
            recv = readLine();
        }
        
        MicLoggerIdentifier loggerIdentifier = new MicLoggerIdentifier();
        if(!loggerIdentifier.parse(recv)){
        	setError(LanguageManager.getInstance().getString(1042));
            isBusy = false;
        	return false;
        }

        logger.setName(loggerIdentifier.getId());
        logger.setModel(loggerIdentifier.getModel().getModel());
        logger.setVersion(loggerIdentifier.getVersion());
        
        isBusy = false;
		return true;
	}
	
	public boolean saveSettings(MicUSBStick logger){
		String cmd;
		int sampleRate;
		String unit;
		int samples;
		byte[] recv = null;
		
        if(!isOpen){
        	setError(LanguageManager.getInstance().getString(1041));
        	return false;
        }
		
		if(logger == null)
			return false;

        isBusy = true;
        
		sampleRate = (int)logger.getSampleRate();
		
		unit = "M";
		if(logger.getUnitClass().getUnitClass() == UnitClass.UNIT_CLASS_IMPERIAL)
			unit = "I";
		
		samples = logger.getMaxSamples() / 1000;
		
		cmd = "S " + sampleRate + " " + unit + " " + logger.getStartTrigger().getStartTrigger() + " " + samples + " 3\r"; 

		
		try {
			sp.writeString(cmd);
		} catch (SerialPortException e) {
			setError(LanguageManager.getInstance().getString(1047));
	        isBusy = false;
			return false;
		}

		//parse answer
        recv = readLine();
        while(recv != null && recv[0] == 62){
            recv = readLine();
        }
        
        if(recv == null || recv[0] != 'm'){
			setError(LanguageManager.getInstance().getString(1046));
            isBusy = false;
        	return false;
        }
        
        isBusy = false;
		return true;
	}
	
	public boolean setClock(MicUSBStick logger){
		byte[] recv = null;
		String cmd;
		long tm;
		
        if(!isOpen){
        	setError(LanguageManager.getInstance().getString(1041));
        	return false;
        }
		
		if(logger == null)
			return false;
		

        isBusy = true;
		
		tm = (logger.getFirstDatasetDate() / 1000) - 1262304000L;
		
		cmd = "C " + Long.toString(tm) + "\r";
		
		try {
			sp.writeString(cmd);
		} catch (SerialPortException e) {
			setError(LanguageManager.getInstance().getString(1047));
	        isBusy = false;
			return false;
		}
		
		//parse answer
        recv = readLine();
        while(recv != null && recv[0] == 62){
            recv = readLine();
        }
        
        MicLoggerDateTime micLoggerDateTime = new MicLoggerDateTime();
        if(!micLoggerDateTime.parse(recv)){
			setError(LanguageManager.getInstance().getString(1044));
        	return false;
        }
        
        
        if(micLoggerDateTime.getTime() != tm){
			setError(LanguageManager.getInstance().getString(1045));
        	return false;
        }

        isBusy = false;
		return true;
	}
	
	public LoggerRecord getData(MicUSBStick logger){
		LoggerRecord lr = new LoggerRecord(logger, 0);
		lr.setChannels(logger.getModel().getChannels());
		lr.setName("logfile");
		String cmd = "D\r";
		byte[] line;
		
        if(!isOpen){
        	setError(LanguageManager.getInstance().getString(1041));
        	return null;
        }
        

        isBusy = true;
        
		try {
			flush();
			sp.writeString(cmd);
		} catch (SerialPortException e) {
			e.printStackTrace();
	        isBusy = false;
			return null;
		}

		MicLoggerArguments loggerArguments = new MicLoggerArguments();
		
		
		line = readLine();
		while(line != null){
	        if(loggerArguments.parse(line)){
	        	break;
	        }
	        line = readLine();
		}
		
		if(line == null){
	        isBusy = false;
        	return null;
		}
        
        lr.setSampleRate(loggerArguments.getSampleRate());
        lr.setTotalSamples(loggerArguments.getSamples());
        lr.setDatetime(loggerArguments.getTime());

        isBusy = false;
		return lr;
	}
	
    public boolean addData(LoggerRecord loggerRecord){
		byte[] line;
		int[] values;
		
		//read line
		line = readLine();
		
		if(line == null)
			return false;
        isBusy = true;
		
		MicDataLine dataLine = new MicDataLine(loggerRecord.getChannels());
		if(!dataLine.parse(line)){
	        isBusy = false;
			return false;
		}

		Dataset ds = new Dataset(loggerRecord.getChannels());
		values = dataLine.getValues();
		if(loggerRecord.getCount() > 0){
			Dataset previousDataset = loggerRecord.get(loggerRecord.getCount() - 1);
			ds.setDateTime(previousDataset.getDateTime() + (loggerRecord.getSampleRate() * 1000));
		}else{
			ds.setDateTime(loggerRecord.getDatetime());
		}
		for(int i = 0; i < ds.getChannels() && i < values.length; i++){
			ds.setValue(values[i], i);
		}
		
		loggerRecord.add(ds);

        isBusy = false;
    	return true;
    }

    public boolean startRealTimeLogging(){
		String cmd = "S 2 M 2 32 3\r";
		byte[] recv = null;
		
        if(!isOpen){
        	setError(LanguageManager.getInstance().getString(1041));
        	return false;
        }
        
        try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
		}
        
        isBusy = true;
        
		try {
			sp.writeString(cmd);
		} catch (SerialPortException e) {
			setError(LanguageManager.getInstance().getString(1047));
	        isBusy = false;
			return false;
		}

		//parse answer
        recv = readLine();
        while(recv != null && recv[0] == 62 ){
            recv = readLine();
        }
        
        if(recv == null || (recv[0] != 'm' && recv[0] != 'v')){
			setError(LanguageManager.getInstance().getString(1046));
            isBusy = false;
        	return false;
        }
        
        setReadTimeout(2100);
        realtimeLogging =true;
        
    	return true;
    }
    
    public boolean stopRealTimeLogging(){
		String cmd = "Q\r";
		byte[] recv = null;
		
        if(!isOpen){
        	setError(LanguageManager.getInstance().getString(1041));
        	return false;
        }

		try {
			sp.writeString(cmd);
		} catch (SerialPortException e) {
			setError(LanguageManager.getInstance().getString(1047));
	        isBusy = false;
			return false;
		}
        isBusy = false;

        setReadTimeout(200);
        recv = readLine();
        
        try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        cmd = "I\r";
		try {
			sp.writeString(cmd);
		} catch (SerialPortException e) {
			setError(LanguageManager.getInstance().getString(1047));
	        isBusy = false;
			return false;
		}
        recv = readLine();
        realtimeLogging = false;
        
		return true;
    }
    
    public boolean getRealTimeValue(LoggerRecord lr){
		byte[] recv = null;
		int[] values;
		
        recv = readLine();
        if(recv == null){
        	return false;
        }
        
		MicDataLine dataLine = new MicDataLine(lr.getChannels());
		if(!dataLine.parse(recv)){
			return false;
		}

		Dataset ds = new Dataset(lr.getChannels());
		values = dataLine.getValues();
		if(lr.getCount() > 0){
			Dataset previousDataset = lr.get(lr.getCount() - 1);
			ds.setDateTime(previousDataset.getDateTime() + (lr.getSampleRate() * 1000));
		}else{
			lr.setSampleRate(2);
			DateTime dt = new DateTime();
			lr.setDatetime(dt.getMillis());
			ds.setDateTime(lr.getDatetime());
		}
		
		for(int i = 0; i < ds.getChannels() && i < values.length; i++){
			ds.setValue(values[i], i);
		}
		
		lr.add(ds);
        
    	return true;
    }
    
    
}



