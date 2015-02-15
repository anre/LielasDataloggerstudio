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

import org.joda.time.DateTime;
import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.*;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;

public class LielasDataProtocolPaket extends LielasApplicationProtocolPaket{

	private int datasetCount;
	private Dataset ds;
	private byte[] paket;
    private DatasetStructure datasetStructure;
	
	public LielasDataProtocolPaket() {
		super(LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LDP);
        datasetStructure = null;
	}

    public void setDatasetStructure(DatasetStructure datasetStructure){
        this.datasetStructure = datasetStructure;
    }

	@Override
	public int getLength() {
		return paket.length;
	}

	@Override
	public byte[] getBytes() {
		return paket;
	}

	@Override
	public boolean parse(byte[] bytes) {
		paket = bytes;
        int pos = 1;
        int channel = 0;

        if(datasetStructure == null){
            return false;
        }

        if(bytes == null || bytes.length < 1){
            return false;
        }

        datasetCount = bytes[0];

        for(int i = 0; i < datasetCount; i++){
            ds = new Dataset(datasetStructure.getSensorCount());
            DateTime dt = new DateTime();
            ds.setDateTime(dt.getMillis());


            for(int j = 0; j < datasetStructure.getCount(); j++){
                DatasetItem dsItem = datasetStructure.getItem(j);

                if(dsItem instanceof DatasetItemId){
                    pos += dsItem.getSize();
                }else if(dsItem instanceof DatasetItemDatetime){
                    pos += dsItem.getSize();
                }else if(dsItem instanceof DatasetSensorItem){
                    DatasetSensorItem dsSensorItem = (DatasetSensorItem)dsItem;
                    dsSensorItem.parse(bytes, pos);
                    pos += dsSensorItem.getSize();
                    ds.setValue((int)dsSensorItem.getValue(), dsSensorItem.getDecimalPoints(), channel);
                    channel += 1;
                }
            }

        }
		
		return true;
	}

	public Dataset get(){
		return ds;
	}
	
	
}