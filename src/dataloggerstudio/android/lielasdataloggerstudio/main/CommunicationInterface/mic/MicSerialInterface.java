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

package org.lielas.lielasdataloggerstudio.main.CommunicationInterface.mic;

import org.joda.time.DateTime;
import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.lielasdataloggerstudio.main.CommunicationInterface.AndroidSerialInterface;


public class MicSerialInterface extends AndroidSerialInterface {

    final private String CMD_READ_IDENTIFIER = "I\r";
    final private String CMD_READ_ARGUMENTS = "M\r";
	
    private boolean realtimeLogging = false;
    
	public MicSerialInterface(){
		super();
	}
	
	
	public boolean connect(String port){
		return open(port);
	}
	
	public boolean realTimeLoggingInProgress(){
		return realtimeLogging;
	}
	
	public boolean readIdentifier(MicUSBStick logger){
        return true;
	}
	
	public boolean readArguments(MicUSBStick logger){
        return true;
	}
	
	public boolean saveId(String id, MicUSBStick logger){
        return true;
	}
	
	public boolean saveSettings(MicUSBStick logger){
        return true;
	}
	
	public boolean setClock(MicUSBStick logger){
        return true;
	}
	
	public LoggerRecord getData(MicUSBStick logger){
        	return null;
	}
	
    public boolean addData(LoggerRecord loggerRecord){
        return true;
    }

    public boolean startRealTimeLogging(){
        return true;
    }
    
    public boolean stopRealTimeLogging(){
        return true;
    }
    
    public boolean getRealTimeValue(LoggerRecord lr){
        return true;
    }
    
    
}



