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

package org.lielas.lielasdataloggerstudio.main.CommunicationInterface.UsbCube;

import android.util.Log;

import org.lielas.dataloggerstudio.lib.Dataset;
import org.lielas.dataloggerstudio.lib.LielasCommunicationProtocol.*;
import org.lielas.dataloggerstudio.lib.Logger.Lielas.LielasId;
import org.lielas.dataloggerstudio.lib.Logger.Lielas.LielasVersion;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.Dataset.DatasetStructure;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.dataloggerstudio.lib.LoggerRecordManager;
import org.lielas.lielasdataloggerstudio.main.CommunicationInterface.AndroidSerialInterface;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class UsbCubeSerialInterface extends AndroidSerialInterface {
	
    private boolean realtimeLogging = false;
    private boolean readTimeout;
    private int readTimerTimeout = 500;
    private int retries = 3;

    private final String TAG = "UsbCubeSerialInterface";

	
	public UsbCubeSerialInterface (){
        super();
		baudrate = 115200;
	}
	
	public boolean connect(String port){
        return openUsb();
	}

	public void disconnect(){
        isOpen = false;
        try{
            serialport.close();
        }catch (IOException e){
            e.printStackTrace();
        }
		return;
	}

	public boolean wakeup(){
        byte[] sync = new byte[2];
        int i;

        sync[0] = (byte)0x0A;
        sync[1] = (byte)0xA0;

        if(!isOpen()){
            return false;
        }

        for(i = 0; i < 7; i++) {
            write(sync);
        }
        try {
            Thread.sleep(5);
        }catch(InterruptedException e){
        }
        return true;
	}

    public boolean realTimeLoggingInProgress(){
        return realtimeLogging;
    }

	private boolean setRealTimeLoging(boolean status){
        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolRTLStatus rtlStatus = new LielasSettingsProtocolRTLStatus();
        rtlStatus.setStatus(status);
        lspp.setPayload(rtlStatus);


        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();

        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }

        lcpp = getPaket();
        if(lcpp != null){
            if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
                isBusy = false;
                return false;
            }
            lspp = (LielasSettingsProtocolPaket) lcpp.payload;
            try{
                rtlStatus = (LielasSettingsProtocolRTLStatus) lspp.getPayload();
                if(rtlStatus.getStatus() != status){
                    return false;
                }
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
        }

        if(status) {
            setReadTimeout(2000);
        }else{
            setReadTimeout(200);
        }
        return true;
	}
	
	public boolean getRealTimeData(LoggerRecord lr, UsbCube logger){
        LielasCommunicationProtocolPaket lcpp;
        LielasDataProtocolPaket ldpp = null;
        boolean success = false;

        if(!realtimeLogging){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        lcpp = getPaket(logger.getDatasetStructure());

        if(lcpp != null){
            try{
                ldpp = (LielasDataProtocolPaket) lcpp.payload;
            }catch(Exception e){
                e.printStackTrace();
            }

            if(ldpp != null){
                Dataset ds = ldpp.get();
                if(ds != null){
                    lr.add(ds);
                    success =true;
                }
            }
        }

        return success;
	}

    public void startReadTimer(){
        Timer t = new Timer();
        readTimeout = false;
        t.schedule(new TimerTask(){
            @Override
            public void run(){
                readTimeout = true;
            }
        }, readTimerTimeout);
    }
	
	private LielasCommunicationProtocolPaket getPaket(){
        byte[] paket;

        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();

        paket = getPaketData();
        if(paket == null){
            flush();
            return null;
        }

        if(!lcpp.parse(paket)){
            flush();
            return null;
        }

        return lcpp;
	}

    private LielasCommunicationProtocolPaket getPaket(DatasetStructure ds){
        byte[] paket;

        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setDatasetStructure(ds);

        paket = getPaketData();
        if(paket == null){
            flush();
            return null;
        }

        if(!lcpp.parse(paket)){
            flush();
            return null;
        }

        return lcpp;
    }
	
	private byte[] getPaketData(){
        byte[]recv = new byte[200];
        byte[] paket = null;
        Byte d;
        int len = 0;
        int state  = 0;
        int paketLength = LielasCommunicationProtocolPaket.HEADER_LEN + LielasCommunicationProtocolPaket.CRC_LEN;
        int recvLen;

        if(!isOpen){
            return null;
        }

        try {
            Thread.sleep(200);
        }catch(InterruptedException e){
        }

        recvLen = read();
        while(len < recvLen){
            d = readByte();
            if(d != null){
                if(len == 0){
                    if(d == 42){
                        recv[len] = d;
                    }
                }else{
                    recv[len] = d;
                }
                len += 1;

                switch(state){
                    case 0:		//header not complete
                        if(len == LielasCommunicationProtocolPaket.HEADER_LEN){
                            paketLength += recv[1];
                            state += 1;
                        }
                        break;
                    case 1:		//receive paket
                        if(len == paketLength){
                            paket = new byte[paketLength];
                            for(len = 0; len < paketLength; len++){
                                paket[len] =  recv[len];
                            }
                            return paket;
                        }
                        break;

                }
            }
        }

        return paket;
	}


	public boolean startRealTimeLogging(){
		return true;
	}

	public boolean stopRealTimeLogging(){
		return true;
	}

	public boolean getVersion(UsbCube logger){
        LielasVersion lielasVersion;

        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolVersion version = new LielasSettingsProtocolVersion();
        lspp.setPayload(version);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();

        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }
        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }

        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            version = (LielasSettingsProtocolVersion)lspp.getPayload();
            lielasVersion = version.getVersion();
            if(lielasVersion == null){
                return false;
            }
            logger.setVersion(lielasVersion);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        Log.d(TAG, "get Version successfull");
        return true;
	}

    public boolean getId(UsbCube logger){
        LielasId id = new LielasId();

        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolId lsppId = new LielasSettingsProtocolId();
        lspp.setPayload(lsppId);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();

        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }

        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppId = (LielasSettingsProtocolId)lspp.getPayload();
            id = lsppId.getId();
            if(id == null){
                return false;
            }
            logger.setId(id);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }


        Log.d(TAG, "get Id successfull");
        return true;
    }

	public boolean getName(UsbCube logger){
        String name;

        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolName lsppName = new LielasSettingsProtocolName();
        lspp.setPayload(lsppName);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();

        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }

        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppName = (LielasSettingsProtocolName)lspp.getPayload();
            try {
                name = new String(lsppName.getBytes());
            }catch(Exception e){
                name = "";
            }
            logger.setName(name);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        Log.d(TAG, "get Name successfull");
        return true;
	}

	public boolean setName(String name, UsbCube logger){
        String recvName;

        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolName lsppName = new LielasSettingsProtocolName();
        lsppName.setName(name);
        lspp.setPayload(lsppName);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();
        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }

        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppName = (LielasSettingsProtocolName)lspp.getPayload();
            try {
                recvName = new String(lsppName.getBytes());
            }catch(Exception e){
                recvName = "";
            }

            if(!recvName.equals(name)) {
                return false;
            }
            logger.setName(name);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
	}

	public boolean getSamplerate(UsbCube logger){

        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolSamplerate lsppSamplerate = new LielasSettingsProtocolSamplerate();
        lspp.setPayload(lsppSamplerate);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();
        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }

        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppSamplerate = (LielasSettingsProtocolSamplerate)lspp.getPayload();
            if(lsppSamplerate.error()){
                isBusy = false;
                return false;
            }
            logger.setSampleRate(lsppSamplerate.getSamplerate());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        Log.d(TAG, "get Samplerate successfull");
        return true;
	}

	public boolean setSamplerate(UsbCube logger){
        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolSamplerate lsppSamplerate = new LielasSettingsProtocolSamplerate();
        lsppSamplerate.setSamplerate(logger.getSampleRate());
        lspp.setPayload(lsppSamplerate);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();
        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }

        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppSamplerate = (LielasSettingsProtocolSamplerate)lspp.getPayload();
            if(lsppSamplerate.error()){
                isBusy = false;
                return false;
            }
            if(lsppSamplerate.getSamplerate() != logger.getSampleRate()){
                return false;
            }
            logger.setSampleRate(lsppSamplerate.getSamplerate());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
	}

    public boolean getLoggerStatus(UsbCube logger){

        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolLoggerStatus lsppLoggerStatus = new LielasSettingsProtocolLoggerStatus();
        lspp.setPayload(lsppLoggerStatus);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();
        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }

        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppLoggerStatus = (LielasSettingsProtocolLoggerStatus)lspp.getPayload();
            if(lsppLoggerStatus.error()){
                isBusy = false;
                return false;
            }
            logger.setLoggerStatus(lsppLoggerStatus.isLoggerOn());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        Log.d(TAG, "get Logger Status successfull");
        return true;
    }

	public boolean setLoggerStatus(UsbCube logger, boolean status){
        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolLoggerStatus lsppLoggerStatus = new LielasSettingsProtocolLoggerStatus();
        if(status){
            lsppLoggerStatus.setLoggerOn();
        }else{
            lsppLoggerStatus.setLoggerOff();
        }
        lspp.setPayload(lsppLoggerStatus);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();
        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }

        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppLoggerStatus = (LielasSettingsProtocolLoggerStatus)lspp.getPayload();
            if(lsppLoggerStatus.error()){
                isBusy = false;
                return false;
            }
            logger.setLoggerStatus(lsppLoggerStatus.isLoggerOn());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
	}

	public boolean setLoggerFilename(UsbCube logger){
        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolLogfilename lsppFilename = new LielasSettingsProtocolLogfilename();
        lsppFilename.setName(logger.getLogfileName());
        lspp.setPayload(lsppFilename);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();
        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }

        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppFilename = (LielasSettingsProtocolLogfilename)lspp.getPayload();
            if(!logger.getLogfileName().equals(lsppFilename.getName())){
                isBusy = false;
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
	}

	public boolean getClock(UsbCube logger){
        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolDatetime lsppDt = new LielasSettingsProtocolDatetime();
        lspp.setPayload(lsppDt);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();
        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }

        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppDt = (LielasSettingsProtocolDatetime)lspp.getPayload();
            logger.setDatetime(lsppDt.getDatetime());

            long drift = lsppDt.getDatetime() - (new Date().getTime());
            Log.d(TAG, "Drift:" + drift);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        Log.d(TAG, "get Logger Clock successfull");
        return true;
	}

	public boolean setClock(UsbCube logger){

        return false;
	}

	public boolean getLogfileCount(UsbCube logger){

        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolLogfileCount lsppCount = new LielasSettingsProtocolLogfileCount();
        lspp.setPayload(lsppCount);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();
        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }

        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppCount = (LielasSettingsProtocolLogfileCount)lspp.getPayload();
            logger.setRecordCount(lsppCount.getCount());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        Log.d(TAG, "get Logger logfilecount successfull");
        return true;
	}

	public boolean getLogfileProperties(UsbCube logger, int index){
        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolLogfileProperties lsppLogfileProperties = new LielasSettingsProtocolLogfileProperties();
        lsppLogfileProperties.setIndex(index);
        lspp.setPayload(lsppLogfileProperties);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();
        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }

        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppLogfileProperties = (LielasSettingsProtocolLogfileProperties)lspp.getPayload();
            if(lsppLogfileProperties.getName() != null){

                LoggerRecord lr = new LoggerRecord(logger, index);
                lr.setDatetime(lsppLogfileProperties.getDatetime());
                lr.setEndDatetime(lsppLogfileProperties.getEndDatetime());
                lr.setName(lsppLogfileProperties.getName());
                lr.setSampleRate((int) lsppLogfileProperties.getSamplerate());
                lr.setStartIndex(lsppLogfileProperties.getStartIndex());
                lr.setEndIndex(lsppLogfileProperties.getEndIndex());
                lr.setChannels(logger.getChannels());
                logger.addRecordset(lsppLogfileProperties.getIndex(), lr);
                LoggerRecordManager.getInstance().add(lr);

            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        Log.d(TAG, "get Logger logfilecount successfull");
        return true;
	}

	public boolean getLogfileDataset(UsbCube logger, LoggerRecord lr, long id){

        return false;
	}

	public boolean deleteLogfiles(UsbCube logger){

        return false;
	}

	public boolean getDatasetStructure(UsbCube logger){
        if(!isOpen){
            return false;
        }

        if(!wakeup()){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolDatasetStructure lsppDsStructure = new LielasSettingsProtocolDatasetStructure();
        lspp.setPayload(lsppDsStructure);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();
        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return false;
        }
        //receive answer
        lcpp = getPaket();
        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppDsStructure = (LielasSettingsProtocolDatasetStructure)lspp.getPayload();
            if(lsppDsStructure != null){
                logger.setDatasetStructure(lsppDsStructure.getDatasetStructure());
                logger.setChannels(lsppDsStructure.getDatasetStructure().getChannelCount());
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        Log.d(TAG, "get Logger Dataset Structure successfull");
        return true;
	}

}