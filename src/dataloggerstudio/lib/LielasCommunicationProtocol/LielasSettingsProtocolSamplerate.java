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
public class LielasSettingsProtocolSamplerate extends LielasSettingsProtocolPayload {
    long samplerate;
    boolean hasSamplerate;

    public LielasSettingsProtocolSamplerate(){
        hasSamplerate = false;
        samplerate = 0;
    }

    @Override
    public int getLspId() {
        return LielasSettingsProtocolIds.SAMPLERATE;
    }

    @Override
    public int getLength() {
        if(hasSamplerate) {
            return 8;
        }
        return 0;
    }

    @Override
    public byte[] getBytes() {

        if(!hasSamplerate){
            return null;
        }

        int i = 0;
        byte[] b = new byte[8];

        b[i++] = (byte)((samplerate >> 56) & 0xFF);
        b[i++] = (byte)((samplerate >> 48) & 0xFF);
        b[i++] = (byte)((samplerate >> 40) & 0xFF);
        b[i++] = (byte)((samplerate >> 32) & 0xFF);

        b[i++] = (byte)((samplerate >> 24) & 0xFF);
        b[i++] = (byte)((samplerate >> 16) & 0xFF);
        b[i++] = (byte)((samplerate >> 8) & 0xFF);
        b[i++] = (byte)((samplerate) & 0xFF);

        return b;
    }

    @Override
    public boolean parse(byte[] payload) {
        int i = 7;

        if(payload == null){
            hasSamplerate = false;
            return false;
        }

        if(payload.length != 8){
            hasSamplerate = false;
            return false;
        }

        samplerate = payload[i--];
        samplerate += payload[i--] << 8;
        samplerate += payload[i--] << 16;
        samplerate += payload[i--] << 24;

        samplerate += payload[i--] << 32;
        samplerate += payload[i--] << 40;
        samplerate += payload[i--] << 48;
        samplerate += payload[i--] << 56;

        hasSamplerate = true;

        return true;
    }

    public boolean error(){
        return !hasSamplerate;
    }

    public long getSamplerate(){
        return samplerate;
    }

    public void setSamplerate(long samplerate){
        this.samplerate = samplerate;
        hasSamplerate = true;
    }
}
