package org.lielas.dataloggerstudio.lib.LielasCommunicationProtocol;

import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.*;

/**
 * Created by Andi on 20.05.2015.
 */
public class LielasDataProtocolAnswerPaket {

    int datasetCount;
    Dataset[] datasets;

    long id;

    public LielasDataProtocolAnswerPaket(){
        datasetCount = 0;
    }

    boolean parse(byte[] paket, DatasetStructure datasetStructure){
        int dsCount = 0;
        int pos = 0;
        int channel = 0;

        if(paket == null || paket.length < 1){
            return false;
        }

        //get dataset count
        datasetCount = paket[pos++];
        datasets = new Dataset[datasetCount];

        for(dsCount = 0; dsCount < datasetCount; dsCount++) {
            datasets[dsCount] = new Dataset(datasetStructure.getSensorCount());
            channel = 0;

            for (int i = 0; i < datasetStructure.getCount(); i++) {
                DatasetItem dsItem = datasetStructure.getItem(i);

                if (dsItem instanceof DatasetItemId) {
                    DatasetItemId ds = (DatasetItemId) dsItem;
                    ds.parse(paket, pos);
                    pos += ds.getSize();
                    datasets[dsCount].setId(ds.getId());
                } else if (dsItem instanceof DatasetItemDatetime) {
                    DatasetItemDatetime ds = (DatasetItemDatetime) dsItem;
                    ds.parse(paket, pos);
                    pos += ds.getSize();
                    datasets[dsCount].setDateTime(ds.getDt());
                } else if (dsItem instanceof DatasetSensorItem) {
                    DatasetSensorItem ds = (DatasetSensorItem) dsItem;
                    ds.parse(paket, pos);
                    pos += ds.getSize();
                    datasets[dsCount].setValue((int)ds.getValue(), ds.getDecimalPoints(), channel);
                    channel += 1;
                }
            }
            pos += 2;
        }

        return true;
    }

    public int getDatasetCount(){
        return datasetCount;
    }

    public Dataset getDataset(int index){
        if(datasets == null || index > datasets.length){
            return null;
        }
        return datasets[index];
    }
}
