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

package org.lielas.dataloggerstudio.lib.LielasCommunicationProtocol;

public class LielasSettingsProtocolPaket extends LielasApplicationProtocolPaket{
	
	int lspid;
	LielasSettingsProtocolPayload payload;
	LielasSettingsProtocolPayload lspp;
	
	private final int LSPID_LEN = 2;

	public LielasSettingsProtocolPaket() {
		super(LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP);
		payload = null;
		lspp = null;
	}

	public LielasSettingsProtocolPaket(LielasSettingsProtocolPayload lspp) {
		super(LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP);
		this.lspp = lspp;
		payload = null;
	}
	
	@Override
	public int getLength() {
		if(payload == null){
			return LSPID_LEN;
		}
		return (payload.getLength() + LSPID_LEN); 
	}

	@Override
	public byte[] getBytes() {
		byte[] bytes = new byte[getLength()];
		byte[] payloadBytes = null;
		
		if(payload != null){
			payloadBytes = payload.getBytes();
		}
		
		//fill header
		bytes[0] = (byte)(lspid >> 8);
		bytes[1] = (byte)(lspid);
		
		//fill payload
		if(payloadBytes != null){
			System.arraycopy(payloadBytes, 0, bytes, LSPID_LEN, payloadBytes.length);
		}
		
		return bytes;
	}

	public int getLspid() {
		return lspid;
	}

	public void setPayload( LielasSettingsProtocolPayload payload){
		this.payload = payload;
		this.lspid = payload.getLspId();
	}
	
	public LielasSettingsProtocolPayload getPayload(){
		return payload;
	}
	
	
	@Override
	public boolean parse(byte[] bytes) {
		byte[] payloadBytes;
		
		if(bytes == null){
			return false;
		}
		
		lspid = LielasCommunicationProtocolPaket.getUnsignedShort(bytes, 0);

		if(lspp == null) {
			//create payload
			switch (lspid) {
				case LielasSettingsProtocolIds.REALTIMELOGGING:
					payload = (LielasSettingsProtocolPayload) new LielasSettingsProtocolRTLStatus();
					break;
				case LielasSettingsProtocolIds.VERSION:
					payload = (LielasSettingsProtocolPayload) new LielasSettingsProtocolVersion();
					break;
				case LielasSettingsProtocolIds.ID:
					payload = (LielasSettingsProtocolPayload) new LielasSettingsProtocolId();
					break;
				case LielasSettingsProtocolIds.LOGGERNAME:
					payload = (LielasSettingsProtocolPayload) new LielasSettingsProtocolName();
					break;
				case LielasSettingsProtocolIds.SAMPLERATE:
					payload = (LielasSettingsProtocolPayload) new LielasSettingsProtocolSamplerate();
					break;
				case LielasSettingsProtocolIds.LOGGER_STATUS:
					payload = (LielasSettingsProtocolPayload) new LielasSettingsProtocolLoggerStatus();
					break;
				case LielasSettingsProtocolIds.SET_LRI_NAME:
					payload = (LielasSettingsProtocolPayload) new LielasSettingsProtocolLogfilename();
					break;
				case LielasSettingsProtocolIds.DATETIME:
					payload = (LielasSettingsProtocolPayload) new LielasSettingsProtocolDatetime();
					break;
				case LielasSettingsProtocolIds.LRI_COUNT:
					payload = (LielasSettingsProtocolPayload) new LielasSettingsProtocolLogfileCount();
					break;
				case LielasSettingsProtocolIds.GET_LRI:
					payload = (LielasSettingsProtocolPayload) new LielasSettingsProtocolLogfileProperties();
					break;
				case LielasSettingsProtocolIds.GET_DATASET:
					payload = (LielasSettingsProtocolPayload) new LielasSettingsProtocolDataset();
					break;
				case LielasSettingsProtocolIds.DELETE_DATA:
					payload = (LielasSettingsProtocolPayload) new LielasSettingsProtocolDelete();
					break;
				default:
					return false;
			}
		}else{
			payload = lspp;
		}
		
		
		//get lsp payload
		payloadBytes = new byte[bytes.length - LSPID_LEN];
		System.arraycopy(bytes, LSPID_LEN, payloadBytes, 0, (bytes.length - LSPID_LEN));
		
		//parse payload
		if(!payload.parse(payloadBytes)){
			return false;
		}
		
		return true;
	}

}