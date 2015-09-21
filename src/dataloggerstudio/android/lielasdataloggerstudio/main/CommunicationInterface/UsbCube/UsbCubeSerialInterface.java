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
		baudrate = 500000;
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
	}

    public boolean realTimeLoggingInProgress(){
        return realtimeLogging;
    }

	private boolean setRealTimeLoging(boolean status){
        if(!isOpen){
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
        byte[]recv = new byte[500];
        byte[] paket = null;
        Byte d;
        int len = 0;
        int state  = 0;
        int paketLength = LielasCommunicationProtocolPaket.HEADER_LEN + LielasCommunicationProtocolPaket.CRC_LEN;
        int recvLen = 0;

        long start = new Date().getTime();
        long end = start + 500;


        if(!isOpen){
            return null;
        }

        while(new Date().getTime() < end) {

            if(getBytesInBuffer() == 0){
                recvLen = read();
            }

            for(int i = 0; i < recvLen; i++){
                d = readByte();
                if (d != null) {
                    if (len == 0) {
                        if (d == 42) {
                            recv[len++] = d;
                        }else{
                            continue;
                        }
                    } else {
                        recv[len++] = d;
                    }

                    switch (state) {
                        case 0:        //header not complete
                            if (len == LielasCommunicationProtocolPaket.HEADER_LEN) {
                                paketLength += recv[1];
                                state += 1;
                            }
                            break;
                        case 1:        //receive paket
                            if (len == paketLength) {
                                paket = new byte[paketLength];
                                for (len = 0; len < paketLength; len++) {
                                    paket[len] = recv[len];
                                }
                                return paket;
                            }
                            break;

                    }
                }
            }
        }
        return paket;
	}

    private byte[] getPaketData(long timeout){
        byte[]recv = new byte[500];
        byte[] paket = null;
        Byte d;
        int len = 0;
        int state  = 0;
        int paketLength = LielasCommunicationProtocolPaket.HEADER_LEN + LielasCommunicationProtocolPaket.CRC_LEN;
        int recvLen = 0;
        long tmpTimeout;

        long start = new Date().getTime();
        long end = start + timeout;


        if(!isOpen){
            return null;
        }

        tmpTimeout = getTimeout();
        setTimeout((int)timeout);

        while(new Date().getTime() < end) {

            recvLen = getBytesInBuffer();
            if(recvLen == 0){
                recvLen = read();
            }

            for(int i = 0; i < recvLen; i++){
                d = readByte();
                if (d != null) {
                    if (len == 0) {
                        if (d == 42) {
                            recv[len++] = d;
                        }else{
                            continue;
                        }
                    } else {
                        recv[len++] = d;
                    }

                    switch (state) {
                        case 0:        //header not complete
                            if (len == LielasCommunicationProtocolPaket.HEADER_LEN) {
                                paketLength += recv[1] & 0xFF;
                                state += 1;
                            }
                            break;
                        case 1:        //receive paket
                            if (len == paketLength) {
                                paket = new byte[paketLength];
                                for (len = 0; len < paketLength; len++) {
                                    paket[len] = recv[len];
                                }
                                setTimeout((int)tmpTimeout);
                                return paket;
                            }
                            break;

                    }
                }
            }
        }
        setTimeout((int)tmpTimeout);
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

        Log.d(TAG, "set Name successfull");
        return true;
	}

	public boolean getSamplerate(UsbCube logger){

        if(!isOpen){
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

    private long syncSecond(){
        long tm = new Date().getTime() % 1000;
        long start = new Date().getTime();
        while(tm > 980 || tm < 973){
            tm = new Date().getTime() % 1000;
        }
        long duration = new Date().getTime() - start;
        Log.d(TAG, "Sync duration: " + Long.toString(duration));
        return duration;
    }

	public boolean setClock(UsbCube logger){

        if(!isOpen){
            return false;
        }

        logger.setDatetime(logger.getDatetime() + syncSecond() + 1000);

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolDatetime lsppDt = new LielasSettingsProtocolDatetime();
        lsppDt.setDatetime(logger.getDatetime());
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
            if((lsppDt.getDatetime() / 1000) < (logger.getDatetime() / 1000) || (lsppDt.getDatetime()/1000) > ((logger.getDatetime()/1000) + 1)){
                isBusy = false;
                return false;
            }
            logger.setDatetime(lsppDt.getDatetime());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        Log.d(TAG, "set Logger Clock successfull");
        return true;
	}

	public boolean getLogfileCount(UsbCube logger){

        if(!isOpen){
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

        if(!isOpen){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolDataset lsppDs = new LielasSettingsProtocolDataset();
        lsppDs.setDatasetStructure(logger.getDatasetStructure());
        lsppDs.setId(id);
        lsppDs.setChannels(logger.getChannels());
        lspp.setPayload(lsppDs);

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
        lcpp = new LielasCommunicationProtocolPaket();


        paket = getPaketData();

        if(paket == null){
            flush();
            return false;
        }

        if(!lcpp.parse(paket, (LielasSettingsProtocolPayload)lsppDs)){
            flush();
            return false;
        }

        if(lcpp == null) {
            return false;
        }
        if(!(lcpp.header.getProtocol() == LielasApplicationProtocolPaket.LAP_PROTOCOL_TYPE_LSP)){
            isBusy = false;
            return false;
        }
        lspp = (LielasSettingsProtocolPaket) lcpp.payload;
        try{
            lsppDs = (LielasSettingsProtocolDataset)lspp.getPayload();
            if(lsppDs.getId() != id){
                return false;
            }

            Dataset ds = new Dataset(logger.getChannels());
            ds.setDateTime(lsppDs.getDatetime());
            for(int i = 0; i < logger.getChannels(); i++){
                ds.setValue((int)lsppDs.getChannelValue(i), lsppDs.getDecimalPoints(i),  i);
            }
            lr.add(ds);

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        Log.d(TAG, "get Logger dataset successfull");
        return true;
	}

    public int getLdpPaket(UsbCube logger, LoggerRecord lr, boolean requestTypeStart){
        int paketsReceived = 0;

        //create application protocol paket
        LielasDataProtocolPaket ldpp = new LielasDataProtocolPaket();
        if(requestTypeStart) {
            ldpp.setRequestType(LielasDataProtocolPaket.RQ_START);
        }else{
            ldpp.setRequestType(LielasDataProtocolPaket.RQ_NEXT);
        }
        ldpp.setLriIndex(lr.getIndex());
        ldpp.setDatasetStructure(logger.getDatasetStructure());
        ldpp.setPaketCount(10);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(ldpp);
        lcpp.pack();
        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return 0;
        }

        for(int i = 0; i < ldpp.getPaketCount(); i++){
            //receive answer
            lcpp = new LielasCommunicationProtocolPaket();


            paket = getPaketData(1000);


            if (paket == null) {
                Log.d("DOWN", "PAket size null " + Integer.toString(i));
                flush();
                return 0;
            }

            Log.d("DOWN", "PAket size" + Integer.toString(paket.length));

            LielasDataProtocolAnswerPaket ldpap = new LielasDataProtocolAnswerPaket();
            lcpp.setDatasetStructure(logger.getDatasetStructure());
            if (!lcpp.parse(paket, ldpap)) {
                flush();
                return 0;
            }

            for (int j = 0; j < ldpap.getDatasetCount(); j++) {
                lr.add(ldpap.getDataset(j));
            }

            paketsReceived += ldpap.getDatasetCount();

            if(lr.getCount() >= (lr.getEndIndex() - lr.getStartIndex())){
                break;
            }

            if (lcpp == null) {
                return paketsReceived;
            }
        }

        return paketsReceived;
    }

	public boolean deleteLogfiles(UsbCube logger){

        if(!isOpen){
            return false;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolDelete lsppDelete = new LielasSettingsProtocolDelete();
        lspp.setPayload(lsppDelete);

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
            lsppDelete = (LielasSettingsProtocolDelete)lspp.getPayload();
            if(lsppDelete.isDataDeleted()){
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        Log.d(TAG, "Logfiles successfully deleted");
        return false;
	}

	public boolean getDatasetStructure(UsbCube logger){

        if(!isOpen){
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
        //Log.d(TAG, "get Logger Dataset Structure successfull");
        return true;
	}

    public Dataset getRTData(UsbCube logger){

        if(!isOpen){
            return null;
        }

        //create application protocol paket
        LielasSettingsProtocolPaket lspp = new LielasSettingsProtocolPaket();
        LielasSettingsProtocolRTData lsprt = new LielasSettingsProtocolRTData();
        lspp.setPayload(lsprt);

        //create lcp protocol paket
        LielasCommunicationProtocolPaket lcpp = new LielasCommunicationProtocolPaket();
        lcpp.setLielasApplicationProtocol(lspp);
        lcpp.pack();
        //get paket
        byte[] paket = lcpp.getBytes();

        if(!write(paket)){
            return null;
        }

        //receive answer
        lcpp = getPaket(logger.getDatasetStructure());
        if(lcpp == null) {
            return null;
        }

        try{
            LielasDataProtocolPaket ldpp = (LielasDataProtocolPaket)lcpp.getLielasApplicationProtocol();
            return ldpp.get();
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

}