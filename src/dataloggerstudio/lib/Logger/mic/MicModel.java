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

public class MicModel{
	
	int model;
	int channels;

    public static final int MODEL_UNKNOWN = 0;
    public static final int MODEL_TEMP = 98580;
    public static final int MODEL_TEMP32 = 98581;
    public static final int MODEL_TEMP_HUM01 = 98582;
    public static final int MODEL_TEMP_HUM = 98583;
    public static final int MODEL_TEMP_HUM_CO2 = 98587;
	
	public MicModel(int model){
		switch(model){
		case MODEL_TEMP:
		case MODEL_TEMP32:
		case MODEL_TEMP_HUM01:
		case MODEL_TEMP_HUM:
		case MODEL_TEMP_HUM_CO2:
			this.model = model;
			this.channels = 2;
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
		return channels;
	}
	
}