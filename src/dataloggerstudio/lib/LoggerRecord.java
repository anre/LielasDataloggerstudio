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

package org.lielas.dataloggerstudio.lib;

import org.joda.time.format.DateTimeFormat;
import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;

import java.util.ArrayList;
import java.util.Collections;

public class LoggerRecord{

	
	private ArrayList<Dataset> data;
	
	private int sampleRate;
	private int totalSamples;
	private int channels;
	private long datetime;
	private long endDatetime;
	private boolean saved;
	private int index;
	private String name;
	private UnitClass unitClass;
	private boolean dataChanged;

	private long startIndex;
	private int id;

	private Logger logger;


	private long endIndex;
	
	private final Object lock = new Object();
	
	public LoggerRecord(Logger logger){
		data = new ArrayList<Dataset>();
		saved = false;
		index = -1;
		this.logger = logger;
		unitClass = new UnitClass();
	}

	public LoggerRecord(Logger logger, int index){
		data = new ArrayList<Dataset>();
		saved = false;
		this.index = index;
		this.logger = logger;
		unitClass = new UnitClass();
	}



	public UnitClass getUnitClass(){
		return unitClass;
	}

	public Logger getLogger(){
		return logger;
	}

	public int getCount(){
        if(data == null){
            return 0;
        }
		return data.size();
	}
	
	public Dataset get(int index){
        if(data == null || index > (data.size() - 1)){
            return null;
        }

        data.get(index).getUnitClass().setUnitClass(unitClass.getUnitClass());
		return data.get(index);
	}
	
	public void add(Dataset ds){
		synchronized (lock) {
			//lock if dataset already exists
			for(int i = 0; i < data.size(); i++) {
				if (ds.getDateTime() == data.get(i).getDateTime())
					return;
			}
			data.add(ds);
            dataChanged = true;
		}
	}

	public double getMax(int channel){
		double max;
		synchronized (lock) {
			if(data == null || data.size() == 0){
				return 0.0;
			}

			max = get(0).getValue(channel);

			for(int i = 0; i < data.size(); i++) {
				if(get(i).getValue(channel) > max){
					max = get(i).getValue(channel);
				}
			}
			return max;
		}
	}

	public double getMin(int channel){
		double min;
		synchronized (lock) {
			if(data == null || data.size() == 0){
				return 0.0;
			}

			min = get(0).getValue(channel);

			for(int i = 0; i < data.size(); i++) {
				if(get(i).getValue(channel) < min){
					min = get(i).getValue(channel);
				}
			}
			return min;
		}
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(int sampleRate) {
		synchronized (lock) {
			this.sampleRate = sampleRate;
		}
	}

	public String getSampleRateString(){
		long tm = sampleRate;
		long seconds = tm % 60;
		tm = (tm - seconds) / 60;
		long minutes = tm % 60;
		tm = (tm - minutes) / 60;
		return String.format("%02d:%02d:%02d", tm, minutes, seconds);
	}

	public int getTotalSamples() {
		return totalSamples;
	}

	public void setTotalSamples(int totalSamples) {
		synchronized (lock) {
			this.totalSamples = totalSamples;
		}
	}

	public int getChannels() {
		return channels;
	}

	public void setChannels(int channels) {
		synchronized (lock) {
			this.channels = channels;
		}
	}
	
	public String[][] getStringArray(){
		String[][] s = new String[totalSamples/2][channels+1];
		String val[];
		
		for(int i = 0; i < (totalSamples / 2); i++){
			val = data.get(i).getStringArray();
            System.arraycopy(val, 0, s[i], 0, channels + 1);
		}
		return s;
	}
	
	public String getString(int row, int column){
		return data.get(row).getString(column);
	}

	public long getDatetime() {
		return datetime;
	}

	public String getDatetimeString(){
		org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
		return formatter.print(datetime);
	}

	public String getEndDatetimeString(){
		org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
		return formatter.print(endDatetime);
	}

	public void setDatetime(long datetime) {
		synchronized (lock) {
			this.datetime = datetime;
		}
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		synchronized (lock) {
			this.saved = saved;
		}
	}

	public int getIndex(){
		return index;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setStartIndex(long startIndex){
		this.startIndex = startIndex;
	}

	public long getStartIndex(){
		return startIndex;
	}

	public long getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(long endDatetime) {
		this.endDatetime = endDatetime;
	}

	public long getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(long endIndex) {
		this.endIndex = endIndex;
	}

	public String toString(){
		return name;
	}

    public void clearData(){
        data = new ArrayList<Dataset>();
    }

    public void sort(){
        Collections.sort(data);
    }

    public boolean newDataAdded(){
        return dataChanged;
    }

    public void setDataProcessed(){
        dataChanged = false;
    }

	public  void requestReprocessing(){
		dataChanged = true;
	}
}