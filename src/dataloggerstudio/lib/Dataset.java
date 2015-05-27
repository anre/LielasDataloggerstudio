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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;


public class Dataset implements Comparable<Dataset>{
    private long id;
	private long dt;
	private int channels;
	private Value[] value;
	
	public Dataset(int channels){
		this.channels = channels;
		value = new Value[channels];
		
		dt = 0;
	}

    @Override
    public int compareTo(Dataset o) {
        if(this.dt < o.getDateTime()){
            return -1;
        }else if(this.dt > o.getDateTime()){
            return 1;
        }
        return 0;
    }

    public class Value{
		private int value;
		private int decimals;
		
		public Value(int decimals){
			this.decimals = decimals;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
		
		public int getDecimals(){
			return decimals;
		}
	}

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

	public long getDateTime() {
		return dt;
	}

	public void setDateTime(long dt) {
		this.dt = dt;
	}

	public int getChannels() {
		return channels;
	}

	public void setChannels(int channels) {
		this.channels = channels;
	}

	public void setValue(int value, int decimals, int channel){
		if(channel < this.channels && this.channels >= 0){
			this.value[channel] = new Value(decimals);
			this.value[channel].setValue(value);
		}
	}

	public void setValue(int value, int channel){
		if(channel < this.channels && this.channels >= 0){
			this.value[channel] = new Value(1);
			this.value[channel].setValue(value);
		}
	}
	
	public double getValue(int channel){
		if(channel < this.channels && this.channels >= 0){
			return (double)this.value[channel].getValue() / (java.lang.Math.pow(10,this.value[channel].getDecimals()));
		}
		return 0.0;
	}
	
	public String getString(int channel){
		String s;
		
		if(channel == 0){
			Date date = new Date(dt);
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			return df.format(date);
		}else {
			if (value[channel - 1] == null) {
				return "";
			}

			double d = value[channel - 1].getValue() / (Math.pow(10, value[channel - 1].getDecimals()));
			String format = "%." + Integer.toString(value[channel - 1].getDecimals()) + "f";
			s = String.format(format, d);

			return s;
			//return s.substring(0, s.length() - value[channel - 1].getDecimals()) + "," + s.substring(s.length() - value[channel - 1].getDecimals(), s.length());
		}
	}
	
	public String[] getStringArray(){
		String[] s = new String[channels+1];
		
		
		//date
		Date date = new Date(dt);
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		s[0] = df.format(date);
		
		//values
		for(int i = 0; i < channels; i++){
			String val = Integer.toString(value[i].getValue());
            if(val.equals("0")){
                s[i + 1] = "0,0";
            }else {
                s[i + 1] = val.substring(0, val.length() - value[i].getDecimals()) + "," + val.substring(val.length() - value[i].getDecimals(), val.length());
            }
		}
		return s;
	}
}