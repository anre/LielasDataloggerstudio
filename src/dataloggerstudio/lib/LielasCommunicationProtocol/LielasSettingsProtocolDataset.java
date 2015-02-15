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

import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.*;

/**
 * Created by Andi on 14.01.2015.
 */
public class LielasSettingsProtocolDataset extends LielasSettingsProtocolPayload{

    boolean hasDataset = false;

    int channels;
    long id;
    long dt;
    long[] channelValue;
    int[] decimalPoints;
    DatasetStructure datasetStructure;

    public LielasSettingsProtocolDataset(){
        datasetStructure = null;
    }

    public void setDatasetStructure(DatasetStructure ds){
        this.datasetStructure = ds;
    }

    public DatasetStructure getDatasetStructure(){
        return datasetStructure;
    }

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
        int pos = 0;
        int channel = 0;

        if(datasetStructure == null){
            return false;
        }

        for(int i = 0; i < datasetStructure.getCount(); i++){
            DatasetItem dsItem = datasetStructure.getItem(i);

            if(dsItem instanceof DatasetItemId){
                DatasetItemId ds = (DatasetItemId)dsItem;
                ds.parse(payload, pos);
                pos += ds.getSize();
                id = ds.getId();
            }else if(dsItem instanceof DatasetItemDatetime){
                DatasetItemDatetime ds = (DatasetItemDatetime)dsItem;
                ds.parse(payload, pos);
                pos += ds.getSize();
                dt = ds.getDt();
            }else if(dsItem instanceof DatasetSensorItem){
                DatasetSensorItem ds = (DatasetSensorItem)dsItem;
                ds.parse(payload, pos);
                pos += ds.getSize();
                channelValue[channel] = ds.getValue();
                decimalPoints[channel] = ds.getDecimalPoints();
                channel += 1;
            }
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
        decimalPoints = new int[channels];
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

    public int getDecimalPoints(int index){
        if(decimalPoints == null || index > decimalPoints.length){
            return 0;
        }

        return decimalPoints[index];
    }
}
