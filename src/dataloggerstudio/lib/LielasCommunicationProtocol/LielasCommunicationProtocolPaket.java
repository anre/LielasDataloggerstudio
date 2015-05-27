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

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetStructure;
import org.lielas.dataloggerstudio.lib.crc.Crc16;

public class LielasCommunicationProtocolPaket{
	
	public LielasCommunicationProtocolHeader header;
	public LielasApplicationProtocolPaket payload;
	private int crc;
	
	public static int HEADER_LEN  = 5;
	public static int CRC_LEN = 2;
	
	private static int LENGTH_POS = 1;
	private static int ID_POS = 2;
	private static int PROT_POS = 4;

    private DatasetStructure datasetStructure;

	public LielasCommunicationProtocolPaket(){
		header = new LielasCommunicationProtocolHeader();
		payload = null;
		crc = 0;
        datasetStructure = null;
	}
	
	public boolean parse(byte[] paket){
		int receivedCrc;
		byte[] payloadBytes;
		
		//check for paket length error
		if(paket.length < LielasCommunicationProtocolHeader.HEADER_LENGTH){
			answer(LcpErrorCode.PAYLOAD_LENGTH_ERROR);
			return false;
		}
		
		//check for crc error
		crc = Crc16.calculate(paket, paket.length - CRC_LEN);
		receivedCrc = getUnsignedShort(paket, paket.length - CRC_LEN);
		
		if(crc != receivedCrc){
			answer(LcpErrorCode.CRC_ERROR);
			return false;
		}
		
		header.length = getUnsignedByte(paket[LENGTH_POS]);
		header.setId(getUnsignedShort(paket, ID_POS));
		header.setProtocol(paket[PROT_POS]);

		//check application protocol
		payloadBytes = new byte[header.length];
		System.arraycopy(paket, LielasCommunicationProtocolHeader.HEADER_LENGTH,
							payloadBytes, 0, payloadBytes.length);
		
		switch(header.getProtocol()){
			case LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP:
				payload  = (LielasApplicationProtocolPaket) new LielasSettingsProtocolPaket();
				if(!payload.parse(payloadBytes)){
					return false;
				}
				break;
			case LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LDP:
				payload  = (LielasApplicationProtocolPaket) new LielasDataProtocolPaket();

                if(datasetStructure != null){
                    ((LielasDataProtocolPaket)payload).setDatasetStructure(datasetStructure);
                }

				if(!payload.parse(payloadBytes)){
					return false;
				}
				break;
			default:
				answer(LcpErrorCode.UNKNOWN_PROTOCOL);
				return false;
		}
		
		return true;
	}

	public boolean parse(byte[] paket, LielasSettingsProtocolPayload lspp){
		int receivedCrc;
		byte[] payloadBytes;

		//check for paket length error
		if(paket.length < LielasCommunicationProtocolHeader.HEADER_LENGTH){
			answer(LcpErrorCode.PAYLOAD_LENGTH_ERROR);
			return false;
		}

		//check for crc error
		crc = Crc16.calculate(paket, paket.length - CRC_LEN);
		receivedCrc = getUnsignedShort(paket, paket.length - CRC_LEN);

		if(crc != receivedCrc){
			answer(LcpErrorCode.CRC_ERROR);
			return false;
		}

		header.length = getUnsignedByte(paket[LENGTH_POS]);
		header.setId(getUnsignedShort(paket, ID_POS));
		header.setProtocol(paket[PROT_POS]);

		//check application protocol
		payloadBytes = new byte[header.length];
		System.arraycopy(paket, LielasCommunicationProtocolHeader.HEADER_LENGTH,
				payloadBytes, 0, payloadBytes.length);

		switch(header.getProtocol()){
			case LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP:
				payload  = (LielasApplicationProtocolPaket) new LielasSettingsProtocolPaket(lspp);
				if(!payload.parse(payloadBytes)){
					return false;
				}
				break;
			case LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LDP:
				payload  = (LielasApplicationProtocolPaket) new LielasDataProtocolPaket();
                if(datasetStructure != null){
                    return false;
                }
				if(!payload.parse(payloadBytes)){
					return false;
				}
				break;
			default:
				answer(LcpErrorCode.UNKNOWN_PROTOCOL);
				return false;
		}

		return true;
	}

    public boolean parse(byte[] paket, LielasDataProtocolAnswerPaket ldpap){
        int receivedCrc;
        byte[] payloadBytes;

        //check for paket length error
        if(paket.length < LielasCommunicationProtocolHeader.HEADER_LENGTH){
            answer(LcpErrorCode.PAYLOAD_LENGTH_ERROR);
            return false;
        }

        //check for crc error
        crc = Crc16.calculate(paket, paket.length - CRC_LEN);
        receivedCrc = getUnsignedShort(paket, paket.length - CRC_LEN);

        if(crc != receivedCrc){
            answer(LcpErrorCode.CRC_ERROR);
            return false;
        }

        header.length = getUnsignedByte(paket[LENGTH_POS]);
        header.setId(getUnsignedShort(paket, ID_POS));
        header.setProtocol(paket[PROT_POS]);

        //check application protocol
        payloadBytes = new byte[header.length];
        System.arraycopy(paket, LielasCommunicationProtocolHeader.HEADER_LENGTH,
                payloadBytes, 0, payloadBytes.length);

        switch(header.getProtocol()){
            case LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LDP:

                if(!ldpap.parse(payloadBytes, datasetStructure)){
                    return false;
                }
                break;
            default:
                answer(LcpErrorCode.UNKNOWN_PROTOCOL);
                return false;
        }

        return true;
    }
	
	public void answer(int errorCode){
		
	}
	
	public void pack(){
		if(payload != null){
			
			//len
			header.length += payload.getLength() + 0x2A00;
		
			//id
			header.setId((int)((Math.random() * 29000) + 1000));
		
			//protocol type
			header.setProtocol(payload.getProtocolType());
		
			//crc
			crc = Crc16.calculate(header.getBytes(), header.length);
			
			crc = Crc16.calculate(payload.getBytes(), payload.getLength(), crc);
		}
	}
	
	public void setLielasApplicationProtocol(LielasApplicationProtocolPaket lapp){
		this.payload = lapp;
	}
	
	public byte[] getBytes(){
		int len = 0;
		int paketLength = LielasCommunicationProtocolHeader.HEADER_LENGTH 
							+ payload.getLength() + CRC_LEN;	
		byte[] bytes = new byte[paketLength];
		
		for(byte b : header.getBytes()){
			bytes[len++] = b;
			if(len > bytes.length){
				break;
			}
		}
		for(byte b : payload.getBytes()){
			bytes[len++] = b;
			if(len > bytes.length){
				break;
			}
		}
		bytes[len++] = (byte)(crc >> 8);
		bytes[len++] = (byte)crc;
		
		return bytes;
	}
	
	public static short getUnsignedByte(byte b){
		return (short)(0xFF & ((int)b));
	}
	
	public static int getUnsignedShort(byte[] bytes, int pos){
		short s1, s2;
		if(pos > (bytes.length - 2)){
				return 0;
		}
		s1 = getUnsignedByte(bytes[pos]);
		s2 = getUnsignedByte(bytes[pos+1]);
		return (((int)s1 << 8) | s2);
	}
	
	public static  long getUint32(byte[] bytes, int pos){
		long l;
		if(pos > (bytes.length - 2)){
			return 0;
		}
		l = (bytes[pos++] & 0xFF)  << 24;
		l += (bytes[pos++] & 0xFF) << 16;
		l += (bytes[pos++] & 0xFF) << 8;
		l += (bytes[pos++] & 0xFF);
		return l;
	}

    public void setDatasetStructure(DatasetStructure ds){
        datasetStructure = ds;
    }

	public class LcpErrorCode{
		private static final int UNKNOWN_PROTOCOL = 21;
		private static final int CRC_ERROR = 22;
		private static final int PAYLOAD_LENGTH_ERROR = 23;
	}
}