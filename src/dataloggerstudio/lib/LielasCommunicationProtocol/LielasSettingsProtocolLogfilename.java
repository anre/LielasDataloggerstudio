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
public class LielasSettingsProtocolLogfilename extends LielasSettingsProtocolPayload {

    String name;

    public LielasSettingsProtocolLogfilename(){
        name = null;
    }

    @Override
    public int getLspId() {
        return LielasSettingsProtocolIds.SET_LRI_NAME;
    }

    @Override
    public int getLength() {
        if(name == null){
            return 0;
        }
        return name.length();
    }

    @Override
    public byte[] getBytes() {
        if(name == null){
            return null;
        }
        return name.getBytes();
    }

    @Override
    public boolean parse(byte[] payload) {

        if (payload == null) {
            return false;
        }

        name = new String(payload);
        if (name.length() > 30){
            name = name.substring(0,30);
        }
        return true;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
