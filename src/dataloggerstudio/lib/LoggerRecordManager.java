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

import java.util.ArrayList;

import org.lielas.dataloggerstudio.lib.LoggerRecord;

public class LoggerRecordManager{

	private static LoggerRecordManager instance = null;
	private ArrayList<LoggerRecord> loggerRecords;
	private int active;
	
	private LoggerRecordManager(){
		loggerRecords = new ArrayList<LoggerRecord>();
		active = -1;
	}
	
	public static LoggerRecordManager getInstance(){
		if(instance == null){
			instance = new LoggerRecordManager();
		}
		return instance;
	}
	
	public int getCount(){
		return loggerRecords.size();
	}
	
	public int add(LoggerRecord lr){
		if(lr != null){
			lr.setId(loggerRecords.size());
			loggerRecords.add(lr);
			return loggerRecords.size() - 1;
		}
		return 0;
	}
	
	public LoggerRecord get(int index){
		return loggerRecords.get(index);
	}
	
	public LoggerRecord get(){
		if(loggerRecords.size() == 0){
			return null;
		}
		return loggerRecords.get(loggerRecords.size() - 1);
	}

	public void select(int index){
		if(index >= 0 && index < loggerRecords.size()) {
			active = index;
		}
	}

	public LoggerRecord getSelected(){
		if(active >= 0 && active < loggerRecords.size()) {
			return loggerRecords.get(active);
		}
		return null;
	}

	public void removeAll(){
		loggerRecords = new ArrayList<LoggerRecord>();
		active = -1;
	}
	
}