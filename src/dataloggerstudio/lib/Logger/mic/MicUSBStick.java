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

package org.lielas.dataloggerstudio.lib.Logger.mic;

import org.lielas.dataloggerstudio.lib.CommunicationInterface.CommunicationInterface;
import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;
import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.LoggerType;
import org.lielas.dataloggerstudio.lib.Logger.StartTrigger;
import org.lielas.dataloggerstudio.pc.CommunicationInterface.mic.MicSerialInterface;

public class MicUSBStick extends Logger{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4523456448742699076L;
	
	private MicModel model;
	private String version;
	private int maxSamples;
	private long firstDatasetDate;
	
	public MicUSBStick(){
		super();
		loggerType.setType(LoggerType.USB_STICK);
		com = (CommunicationInterface) new MicSerialInterface();
		unitClass = new UnitClass();
		startTrigger = new StartTrigger();
		maxSamples = 1000;
	}
	
	public void setModel(int m){
		model = new MicModel(m);
	}
	
	public MicModel getModel(){
		return model;
	}
	
	public void setName(String name){
		if(name.length() > 20){
			this.name = name.substring(0, 20);
		}else{
			this.name = name;
		}
	}
	
	public String getName(){
		return this.name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


    public int getMaxSamples() {
		return maxSamples;
	}

	public void setMaxSamples(int maxSamples) {
		if((maxSamples % 1000) != 0){
			return;
		}

    	switch(model.getModel()){
			case MicModel.MODEL_TEMP:
			case MicModel.MODEL_TEMP32:
			case MicModel.MODEL_TEMP_HUM01:
			case MicModel.MODEL_TEMP_HUM:
			case MicModel.MODEL_TEMP_HUM_CO2:
				if(maxSamples > 32000){
					return;
				}
				break;
    	}
		this.maxSamples = maxSamples;
	}

	public String[] getPossibleSampleValues(){
    	String[] samples = null;
    	int i;
    	
    	if(model == null )
    		return null;
    	
    	switch(model.getModel()){
			case MicModel.MODEL_TEMP:
			case MicModel.MODEL_TEMP32:
			case MicModel.MODEL_TEMP_HUM01:
			case MicModel.MODEL_TEMP_HUM:
			case MicModel.MODEL_TEMP_HUM_CO2:
				samples = new String[32];
				for(i = 1000; i <= 32000; i+= 1000){
					samples[(i/1000)-1] = Integer.toString(i);
				}
				break;
    	}
    	return samples;
    }
    
    public String[] getPossibleUnitClasses(){
    	String[] classes = new String[2];
    	classes[0] = UnitClass.UNIT_CLASS_METRIC_STRING;
    	classes[1] = UnitClass.UNIT_CLASS_IMPERIAL_STRING;
    	return classes;
    }
    
    public int[] getPossibleStartTriggers(){
    	int[] triggers = new int[2];
    	triggers[0] = StartTrigger.START_IMMEDIATELY;
    	triggers[1] = StartTrigger.START_AFTER_BUTTON_PRESS;
    	return triggers;
    }

	public long getFirstDatasetDate() {
		return firstDatasetDate;
	}

	public void setFirstDatasetDate(long firstDatasetDate) {
		this.firstDatasetDate = firstDatasetDate;
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
		sb.append("channel 1");
		sb.append(delimiter);
		sb.append("channel 2");
		sb.append(delimiter);
		sb.append("\n");

		//create header line 3
		sb.append(delimiter);
		sb.append("°C");
		sb.append(delimiter);
		sb.append("%");
		sb.append(delimiter);
		sb.append("\n");

		return sb;
	}

}