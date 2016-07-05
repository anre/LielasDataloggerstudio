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

import org.lielas.dataloggerstudio.lib.Logger.mic.MicModel;

public class MicLoggerIdentifier{
	
	private String id;
	private MicModel model;
	private String version;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MicModel getModel() {
		return model;
	}
	public void setModel(MicModel model) {
		this.model = model;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public boolean parse(byte[] line){
		int i = 2;
		
		if(line == null) 
			return false;
		
        //parse answer
        if(line[0] != 'i'){
            return false;
        }

        //parse identifier
        try{
        	id = MicParser.parseTokenToString(line,  i,  line.length);
	    }catch(Exception e){
	    	e.printStackTrace();
	        return false;
	    }

        //parse model
        try{
        	i = MicParser.findNextToken(line, i, line.length); 
        	//model = new MicModel((int)MicParser.parseTokenToLong(line, i, line.length));
	    }catch(Exception e){
	    	e.printStackTrace();
	        return false;
	    }

        //parse Version
        try{
        	i = MicParser.findNextToken(line, i, line.length);
        	version = MicParser.parseTokenToString(line,  i,  line.length);
        	version.substring(1,  version.length());
	    }catch(Exception e){
	    	e.printStackTrace();
	        return false;
	    }
        
		
		return true;
	}
	
}
