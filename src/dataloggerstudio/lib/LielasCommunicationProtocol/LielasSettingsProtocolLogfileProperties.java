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
 * Created by Andi on 06.01.2015.
 */
public class LielasSettingsProtocolLogfileProperties extends LielasSettingsProtocolPayload {
    boolean hasData;

    long datetime;
    long endDatetime;
    long startIndex;
    long endIndex;
    long samplerate;
    int index;
    String name;

    public LielasSettingsProtocolLogfileProperties(){
        hasData = false;
        name = null;
        index = 0;
    }


    @Override
    public int getLspId() {
        return LielasSettingsProtocolIds.GET_LRI;
    }

    @Override
    public int getLength() {
        if(hasData){
            return name.length() + 10;
        }
        return 2;
    }

    @Override
    public byte[] getBytes() {
        byte[] b = new byte[2];
        b[0] = (byte)((index & 0xFF00) >> 8);
        b[1] = (byte) (index & 0xFF);

        return b;
    }

    @Override
    public boolean parse(byte[] payload) {
        int i;

        if(payload == null){
            return false;
        }

        if(payload.length < 10){
            return false;
        }

        index = (((int)payload[0] & 0xFF) << 8) + ((int)payload[1] & 0xFF);
        i = 9;

        datetime = (payload[i--] & 0xFF);
        datetime += (payload[i--] & 0xFF) << 8;
        datetime += (payload[i--] & 0xFF) << 16;
        datetime += (payload[i--] & 0xFF) << 24;

        datetime += (payload[i--] & 0xFF) << 32;
        datetime += (payload[i--] & 0xFF) << 40;
        datetime += (payload[i--] & 0xFF) << 48;
        datetime += (payload[i--] & 0xFF) << 56;

        i = 17;
        
        endDatetime = (payload[i--] & 0xFF);
        endDatetime += (payload[i--] & 0xFF) << 8;
        endDatetime += (payload[i--] & 0xFF) << 16;
        endDatetime += (payload[i--] & 0xFF) << 24;

        endDatetime += (payload[i--] & 0xFF) << 32;
        endDatetime += (payload[i--] & 0xFF) << 40;
        endDatetime += (payload[i--] & 0xFF) << 48;
        endDatetime += (payload[i--] & 0xFF) << 56;
        
        i = 21;

        startIndex = (payload[i--] & 0xFF);
        startIndex += (payload[i--] & 0xFF) << 8;
        startIndex += (payload[i--] & 0xFF) << 16;
        startIndex += (payload[i--] & 0xFF) << 24;

        i = 25;

        endIndex = (payload[i--] & 0xFF);
        endIndex += (payload[i--] & 0xFF) << 8;
        endIndex += (payload[i--] & 0xFF) << 16;
        endIndex += (payload[i--] & 0xFF) << 24;

        i = 33;

        samplerate = (payload[i--] & 0xFF);
        samplerate += (payload[i--] & 0xFF) << 8;
        samplerate += (payload[i--] & 0xFF) << 16;
        samplerate += (payload[i--] & 0xFF) << 24;

        samplerate += (payload[i--] & 0xFF) << 32;
        samplerate += (payload[i--] & 0xFF) << 40;
        samplerate += (payload[i--] & 0xFF) << 48;
        samplerate += (payload[i--] & 0xFF) << 56;

        
        i = 34;
        byte[] b = new byte[payload.length-i];
        System.arraycopy(payload, i, b, 0, payload.length-i);
        name = new String(b);

        hasData = true;

        return true;
    }

    public int getIndex(){
        return index;
    }

    public long getDatetime(){
        return (datetime * 1000);
    }

    public String getName(){
        return name;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public long getSamplerate(){
        return samplerate;
    }

    public long getEndDatetime() {
        return (endDatetime * 1000);
    }

    public long getStartIndex() {
        return startIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }
}
