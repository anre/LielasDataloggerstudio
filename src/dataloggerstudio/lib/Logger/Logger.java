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

package org.lielas.dataloggerstudio.lib.Logger;

import org.lielas.dataloggerstudio.lib.CommunicationInterface.CommunicationInterface;
import org.lielas.dataloggerstudio.lib.Logger.Units.UnitClass;
import org.lielas.dataloggerstudio.lib.Dataset;

public abstract class Logger{
	
	protected LoggerType loggerType;
	protected CommunicationInterface com;
	protected Dataset lastValue;
	protected String name;
	protected UnitClass unitClass;
	protected StartTrigger startTrigger;
	protected long sampleRate;
	
	
	public Logger(){
		loggerType =new LoggerType("");
		com = null;
		lastValue = null;
		unitClass = null;
		startTrigger = null;
	}
	
	
	public void setLoggerType(LoggerType lt){
		this.loggerType = lt;
	}
	
	public LoggerType getLoggerTpye(){
		return loggerType;
	}

	public CommunicationInterface getCommunicationInterface() {
		return com;
	}

	public void setCommunicationInterface(CommunicationInterface com) {
		this.com = com;
	}
	
	public Dataset getLastValue() {
		return lastValue;
	}

	public long getSampleRate() {
		return sampleRate / 1000;
	}
	
	public void setSampleRate(long sampleRate) {
		this.sampleRate = sampleRate * 1000;
	}


	public UnitClass getUnitClass() {
		return unitClass;
	}


	public void setUnitClass(UnitClass unitClass) {
		this.unitClass = unitClass;
	}


	public StartTrigger getStartTrigger() {
		return startTrigger;
	}


	public void setStartTrigger(StartTrigger startTrigger) {
		this.startTrigger = startTrigger;
	}

	public abstract StringBuilder getCsvHeader(String delimiter);
}