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

import java.io.UnsupportedEncodingException;

/**
 * Created by Andi on 02.01.2015.
 */
public class LielasSettingsProtocolName extends LielasSettingsProtocolPayload{
    private byte[] name;

    public static final int MAX_LOGGER_NAME_LENGTH  = 40;

    @Override
    public int getLspId() {
        return LielasSettingsProtocolIds.LOGGERNAME;
    }

    @Override
    public int getLength() {
        if(name == null) {
            return 0;
        }
        return  name.length;
    }

    @Override
    public byte[] getBytes() {
        return name;
    }

    @Override
    public boolean parse(byte[] payload) {
        if(payload == null) {
            return false;
        }

        if(payload.length == 0 || payload.length > MAX_LOGGER_NAME_LENGTH){
            this.name = new byte[0];
            return true;
        }

        name = new byte[payload.length];
        System.arraycopy(payload, 0, name, 0, payload.length);
        return true;
    }

    public boolean setName(String name){
        if(name == null){
            return false;
        }

        if(name.length() == 0 || name.length() > MAX_LOGGER_NAME_LENGTH){
            return  false;
        }
        try {
            this.name = name.getBytes("ASCII");
        }catch(UnsupportedEncodingException e){
            this.name = null;
        }

        return true;
    }
}
