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

package org.lielas.dataloggerstudio.lib.CommunicationInterface;


public class SerialInterface extends CommunicationInterface{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2815694568261326330L;
	
	protected int baudrate;
	protected int readTimeout;
	protected int writeTimeout;
	protected int databits;
	protected int stopbits;
	protected int parity;
	
	protected final int maxLineLength = 100;
	
	protected byte lineEnd = '\n';
	
	public SerialInterface(){
		baudrate = 38400;
		readTimeout = 200;
		writeTimeout = 200;
		databits = 8;
		stopbits = 1;
		parity = 0;
	}

    @Override
    public boolean open(String port) {
        return false;
    }

    @Override
    public void close() {

    }

    public void setBaudrate(int baudrate){
		this.baudrate = baudrate;
	}
	
	public int getReadTimeout(){
		return readTimeout;
	}
	
	public void setReadTimeout(int timeout){
		this.readTimeout = timeout;
	}
	
	public int getWriteTimeout(){
		return readTimeout;
	}
	
	public void setWriteTimeout(int timeout){
		this.readTimeout = timeout;
	}
}