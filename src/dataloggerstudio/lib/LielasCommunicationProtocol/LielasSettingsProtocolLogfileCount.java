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
public class LielasSettingsProtocolLogfileCount extends LielasSettingsProtocolPayload {
    boolean hasCount;
    int count;

    public LielasSettingsProtocolLogfileCount(){
        hasCount = false;
    }

    @Override
    public int getLspId() {
        return LielasSettingsProtocolIds.LRI_COUNT;
    }

    @Override
    public int getLength() {
        if(hasCount){
            return 2;
        }
        return 0;
    }

    @Override
    public byte[] getBytes() {
        if(hasCount){
            byte[] b = new byte[2];
            b[0] = (byte)((count >> 8) & 0xFF);
            b[1] = (byte)(count & 0xFF);

        }
        return null;
    }

    @Override
    public boolean parse(byte[] payload) {

        if(payload == null){
            return false;
        }

        if(payload.length != 2){
            return false;
        }

        count = (((int)payload[0] & 0xFF) << 8) + ((int)payload[1] & 0xFF);

        return true;
    }

    public int getCount(){
        return count;
    }
}
