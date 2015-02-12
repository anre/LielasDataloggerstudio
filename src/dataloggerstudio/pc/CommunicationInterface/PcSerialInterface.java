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

package org.lielas.dataloggerstudio.pc.CommunicationInterface;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import org.lielas.dataloggerstudio.lib.CommunicationInterface.SerialInterface;
import org.lielas.dataloggerstudio.pc.language.LanguageManager;


public class PcSerialInterface extends SerialInterface{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5122716296380027399L;
	
	protected SerialPort sp;
	
	public PcSerialInterface(){
		super();
	}
	
	@Override
	public boolean isOpen(){
		if(sp != null){
			isOpen = sp.isOpened();
			/*if(isOpen){
				try {
					sp.writeString("I\r");
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
		}
		return isOpen;
	}
	
	@Override
	public boolean open(String port){
		try{
			sp = new SerialPort(port);
			sp.openPort();
			sp.setParams(baudrate, databits, stopbits, parity, false, false);
			isOpen = true;
		}catch(SerialPortException e){
			setError(LanguageManager.getInstance().getString(1040));
			e.printStackTrace();
			isOpen = false;
		}
		return isOpen;
	}

	@Override
	public void close(){
		try {
			sp.closePort();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
	
	public void flush(){
		try {
			sp.purgePort(SerialPort.PURGE_RXCLEAR| SerialPort.PURGE_TXABORT);
		} catch (SerialPortException e) {
			// TODO 
			e.printStackTrace();
		}
	}
	
	public void write(String s){
		if(sp.isOpened()){
			try{
				sp.writeString(s);
			} catch (SerialPortException e) {
				// TODO 
				e.printStackTrace();
			}
		}
	}
	
	public byte[] read(){
		byte[] recv = null;
		if(sp.isOpened()){
			try{
				recv = sp.readBytes();
			} catch (SerialPortException e) {
				// TODO 
				e.printStackTrace();
			}
		}
		return recv;
	}
	

	
	public byte[] readLine(){
		byte[] recv= new byte[100];
		byte[] buf;
		int len = 0;
		
		do{
			try {
				buf = sp.readBytes(1, readTimeout);
			} catch (SerialPortException e) {
				e.printStackTrace();
				return null;
			} catch (SerialPortTimeoutException e) {
				e.printStackTrace();
				return null;
			}
			recv[len] = buf[0];
			len += 1;
		}while(recv[len-1] != lineEnd && len < maxLineLength);

		buf = new byte[len];
		for(int i = 0; i < len; i++){
			buf[i] = recv[i];
		}
		
		
		return buf;
	}
	
	public Byte readByte(){
		byte[] recv = null;
		Byte d = null;
		if(sp.isOpened()){
			try{
				recv = sp.readBytes(1, 200);
			} catch (Exception e) {
				// TODO 
				e.printStackTrace();
			}
		}
		
		if(recv != null){
			d =(byte)recv[0];
		}
		return d;
	}
	
}