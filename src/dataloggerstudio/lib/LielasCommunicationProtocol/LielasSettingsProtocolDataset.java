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
 * Created by Andi on 14.01.2015.
 */
public class LielasSettingsProtocolDataset extends LielasSettingsProtocolPayload{

    boolean hasDataset = false;

    int channels;
    long id;
    long dt;
    long[] channelValue;

    @Override
    public int getLspId() {
        return LielasSettingsProtocolIds.GET_DATASET;
    }

    @Override
    public int getLength() {
        if(hasDataset) {
            return 12+(4*channels);
        }
        return 4;
    }

    @Override
    public byte[] getBytes() {
        byte[] b = new byte[4];

        b[0] = (byte)((id & 0xFF000000) >> 24);
        b[1] = (byte)((id & 0x00FF0000) >> 16);
        b[2] = (byte)((id & 0x0000FF00) >> 8);
        b[3] = (byte) (id & 0x000000FF);

        return b;
    }

    @Override
    public boolean parse(byte[] payload) {

        int i = 0;


        id = (payload[i++] & 0xFF) << 24;
        id += (payload[i++] & 0xFF) << 16;
        id += (payload[i++] & 0xFF) << 8;
        id += (payload[i++] & 0xFF);

        dt = (payload[i++] & 0xFF) << 56;
        dt += (payload[i++] & 0xFF) << 48;
        dt += (payload[i++] & 0xFF) << 40;
        dt += (payload[i++] & 0xFF) << 32;

        dt += (payload[i++] & 0xFF) << 24;
        dt += (payload[i++] & 0xFF) << 16;
        dt += (payload[i++] & 0xFF) <<  8;
        dt += (payload[i++] & 0xFF);
        dt *= 1000;

        for(int j = 0; j < channels; j++){
            channelValue[j] = (payload[i++] & 0xFF) << 24;
            channelValue[j] += (payload[i++] & 0xFF) << 16;
            channelValue[j] += (payload[i++] & 0xFF) <<  8;
            channelValue[j] += (payload[i++] & 0xFF);
        }

        hasDataset = true;

        return true;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setChannels(int channels){
        this.channels = channels;
        channelValue = new long[channels];
    }

    public long getDatetime(){
        return dt;
    }

    public long getId(){
        return id;
    }

    public int getChannels(){
        return channels;
    }

    public long getChannelValue(int index){
        if(channelValue == null || index > channelValue.length){
            return 0;
        }
        return channelValue[index];
    }
}
