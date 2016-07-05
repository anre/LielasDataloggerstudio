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

import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel.MicChannel;
import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel.MicChannelRH;
import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.Channel.MicChannelTemperature;
import org.lielas.dataloggerstudio.lib.CommunicationInterface.mic.MicDataLine;
import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.Logger.MeasurementType.MeasurementTypeHumidity;
import org.lielas.dataloggerstudio.lib.Logger.MeasurementType.MeasurementTypeTemperature;
import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;
import org.lielas.dataloggerstudio.lib.Logger.mic.Models.Model98537;
import org.lielas.dataloggerstudio.lib.Logger.mic.Models.Model98580;
import org.lielas.dataloggerstudio.lib.Logger.mic.Models.Model98581;
import org.lielas.dataloggerstudio.lib.Logger.mic.Models.Model98583;
import org.lielas.dataloggerstudio.lib.Logger.mic.Models.VirtualModelInterface;

public abstract class MicModel  implements VirtualModelInterface {


	protected  MicChannel[] channels;
    protected  String id;
    protected  int maxSamples;

    public static MicModel CreateNewModel(String id){

        if(id.equals(Model98580.ID)){
            return new Model98580();
        }else if(id.equals(Model98583.ID)){
            return new Model98583();
        }else if(id.equals(Model98581.ID)){
            return new Model98581();
        }else if(id.equals(Model98537.ID)){
            return new Model98537();
        }
        return null;
    }

    public int getChannelCount(){
        if(channels == null){
            return 0;
        }
        return  channels.length;
    }

    public int getMaxSamples(){
        return maxSamples;
    }

    public  MicChannel getChannel(int index){
        if(index < 0 || index > (channels.length - 1)){
            return null;
        }
        return channels[index];
    }

    public abstract Dataset parseDataLine(byte[] line, long dt);
    public abstract StringBuilder getCsvHeader(String delimiter, String name, UnitClass unitClass);

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

	/*int model;

    MicChannel[] channels;

    public static final int MODEL_UNKNOWN = 0;
    public static final int MODEL_TEMP = 98580;
    public static final int MODEL_TEMP32 = 98581;
    public static final int MODEL_TEMP_HUM01 = 98583;
    public static final int MODEL_TEMP_HUM = 98582;
    public static final int MODEL_TEMP_HUM_CO2 = 98587;
	
	public MicModel(int model){
		switch(model){
		case MODEL_TEMP:
		case MODEL_TEMP32:
            this.model = model;
            channels = new MicChannel[1];
            channels[0] = new MicChannelTemperature();
            break;
		case MODEL_TEMP_HUM01:
		case MODEL_TEMP_HUM:
		case MODEL_TEMP_HUM_CO2:
			this.model = model;
            channels = new MicChannel[2];
            channels[0] = new MicChannelTemperature();
            channels[1] = new MicChannelRH();
			break;
        default:
            this.model = MODEL_UNKNOWN;
            break;
		}
	}
	
	public int getModel(){
		return model;
	}
	
	@Override
	public String toString(){
		String str = "";
		switch(model){
		case MODEL_TEMP:
			str = "";
			break;
		case MODEL_TEMP32:
			str = "";
			break;
		case MODEL_TEMP_HUM01:
			str = "Temperature + RH(0.1%)";
			break;
		case MODEL_TEMP_HUM:
			str = "Temperature + RH";
			break;
		case MODEL_TEMP_HUM_CO2:
			str = "";
			break;
		default:
			str = "unknown";
			break;
		}
		
		return str;
	}

	public int getChannels() {
		return channels.length;
	}

    public MicChannel getChannel(int index){
        if(index < 0 || index > (channels.length - 1)){
            return null;
        }
        return channels[index];
    }

	public Dataset parseDataLine(byte[] line, long dt){
        MicDataLine micDataLine = new MicDataLine(channels.length);
        Dataset ds = new Dataset(channels.length);

        if(!micDataLine.parse(line)){
            return null;
        }

        ds.setDateTime(dt);

        switch (model){
            case MODEL_TEMP_HUM01:
                ds.setValue(micDataLine.getValueByIndex(0), 1, 0, new MeasurementTypeTemperature());
                ds.setValue(micDataLine.getValueByIndex(1), 1, 1, new MeasurementTypeHumidity());
                break;
        }

        return ds;
    }*/
}