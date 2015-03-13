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

package org.lielas.dataloggerstudio.lib.Logger.UsbCube;

import org.lielas.dataloggerstudio.lib.CommunicationInterface.SerialInterface;
import org.lielas.dataloggerstudio.lib.LielasCommunicationProtocol.LielasSettingsProtocolName;
import org.lielas.dataloggerstudio.lib.Logger.Lielas.LielasId;
import org.lielas.dataloggerstudio.lib.Logger.Lielas.LielasVersion;
import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.LoggerType;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetItemIds;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetStructure;
import org.lielas.dataloggerstudio.lib.LoggerRecord;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class UsbCube extends Logger{

	LielasVersion version;
	LielasId id;
	boolean logging;
	boolean rtLogging;
	String logfileName;
	long datetime;
	ArrayList<LoggerRecord> records;
	int recordCount;
	int channels;
	DatasetStructure datasetStructure;

	public UsbCube(){
		super();
		
		loggerType.setType(LoggerType.USB_CUBE);
		com = new SerialInterface();
		version = new LielasVersion();
		id = new LielasId();
		logging = false;
		rtLogging = false;
		logfileName = null;

		Timer t = new Timer();
		t.schedule(new TimeTask(), 1000, 1000);
		records = new ArrayList<LoggerRecord>();
		recordCount = 0;
		channels = 1;
	}

	@Override
	public StringBuilder getCsvHeader(String delimiter) {
		StringBuilder sb = new StringBuilder();

		//create header line 1
		sb.append(delimiter);
		sb.append(this.name);
		sb.append(delimiter);
		sb.append(delimiter);
		sb.append("\n");

		//create header line 2
		sb.append(delimiter);
		sb.append(id.toString());
		sb.append(delimiter);
		sb.append(delimiter);
		sb.append("\n");

		//create header line 3
		sb.append(delimiter);
		sb.append("channel 1");
		sb.append(delimiter);
		sb.append("channel 2");
		sb.append(delimiter);
		sb.append("\n");

		//create header line 4
		sb.append(delimiter);
        for(int i = 0; i < channels; i++){
            switch(datasetStructure.getItem(i+2).getItemId()){
                case DatasetItemIds.SHT_T:
                case DatasetItemIds.PT1K1:
                case DatasetItemIds.PT1K2:
                case DatasetItemIds.PT1K3:
                case DatasetItemIds.PT1K4:
                    sb.append("°C");
                    break;
                case DatasetItemIds.SHT_H:
                    sb.append("%");
                    break;
                default:
                    break;
            }
            sb.append(delimiter);
        }
		sb.append("\n");

		return sb;
	}

	public void setVersion(LielasVersion version){
		this.version = version;
	}

	public LielasVersion getVersion(){
		return version;
	}

	public void setId(LielasId id){
		this.id = id;
	}

	public LielasId getId(){
		return id;
	}

	public void setName(String name){
		if(name.length() > LielasSettingsProtocolName.MAX_LOGGER_NAME_LENGTH){
			this.name = name.substring(0, LielasSettingsProtocolName.MAX_LOGGER_NAME_LENGTH);
		}else{
			this.name = name;
		}
	}

	public String getName(){
		return this.name;
	}

	public boolean isLogging(){
		return logging;
	}

	public void setLoggerStatus(boolean status){
		logging = status;
	}

	public boolean isRealtimeLogging(){
		return rtLogging;
	}

	public void setRealtimeLoggerStatus(boolean status){
		rtLogging = status;
	}

	public void setLogfileName(String name){
		logfileName = name;
	}

	public String getLogfileName(){
		return logfileName;
	}

	public void setDatetime(long dt){
		datetime = dt;
	}

	public long getDatetime(){
		return datetime;
	}

	public void setRecordCount(int count){
		recordCount = count;
	}

	public int getRecordCount(){
		return recordCount;
	}

	private boolean recordsetExists(int index){
		for(int i = 0; i < records.size(); i++){
			if(records.get(i).getIndex() == index){
				return true;
			}
		}
		return false;
	}

	public boolean addRecordset(int index, LoggerRecord lr){
		if(recordsetExists(index)){
			return false;
		}
		records.add(lr);
		return true;
	}

	public LoggerRecord getRecordset(int index){
		for(int i = 0; i < records.size(); i++){
			if(records.get(i).getIndex() == index){
				return records.get(i);
			}
		}
		return null;
	}

    public LoggerRecord[] getRecordsetArray(){
        if(records == null || records.size() == 0){
            return null;
        }
        LoggerRecord[] recs = new LoggerRecord[records.size()];

        for(int i = 0; i < records.size(); i++){
            recs[i] = records.get(i);
        }
        return recs;
    }

	public void removeAllRecordsets(){
		records = new ArrayList<LoggerRecord>();
		recordCount = 0;
	}

	public void setChannels(int channels){
		this.channels = channels;
	}

	public int getChannels(){
		return channels;
	}

	public DatasetStructure getDatasetStructure() {
		return datasetStructure;
	}

	public void setDatasetStructure(DatasetStructure datasetStructure) {
		this.datasetStructure = datasetStructure;
	}

	class TimeTask extends TimerTask{
		@Override
		public void run() {
			datetime += 1000;
		}
	}
}