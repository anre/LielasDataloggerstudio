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

/**
 * Created by Andi on 04.01.2015.
 */
public class LielasSettingsProtocolDatetime extends LielasSettingsProtocolPayload {
    long datetime;
    boolean hasDatetime;

    public LielasSettingsProtocolDatetime(){
        hasDatetime = false;
    }

    @Override
    public int getLspId() {
        return LielasSettingsProtocolIds.DATETIME;
    }

    @Override
    public int getLength() {
        if(hasDatetime) {
            return 8;
        }
        return 0;
    }

    @Override
    public byte[] getBytes() {

        if(!hasDatetime){
            return null;
        }

        int i = 0;
        byte[] b = new byte[8];

        b[i++] = (byte)((datetime >> 56) & 0xFF);
        b[i++] = (byte)((datetime >> 48) & 0xFF);
        b[i++] = (byte)((datetime >> 40) & 0xFF);
        b[i++] = (byte)((datetime >> 32) & 0xFF);

        b[i++] = (byte)((datetime >> 24) & 0xFF);
        b[i++] = (byte)((datetime >> 16) & 0xFF);
        b[i++] = (byte)((datetime >> 8) & 0xFF);
        b[i++] = (byte)((datetime) & 0xFF);

        return b;
    }

    @Override
    public boolean parse(byte[] payload) {
        int i = 7;

        if(payload == null){
            return false;
        }

        if(payload.length != 8){
            return false;
        }

        datetime = (payload[i--] & 0xFF);
        datetime += (payload[i--] & 0xFF) << 8;
        datetime += (payload[i--] & 0xFF) << 16;
        datetime += (payload[i--] & 0xFF) << 24;

        datetime += (payload[i--] & 0xFF) << 32;
        datetime += (payload[i--] & 0xFF) << 40;
        datetime += (payload[i--] & 0xFF) << 48;
        datetime += (payload[i--] & 0xFF) << 56;

        hasDatetime = true;

        return true;
    }

    public long getDatetime(){
        return datetime * 1000;
    }

    public void setDatetime(long dt){
        datetime = dt / 1000;
        hasDatetime =true;
    }
}
