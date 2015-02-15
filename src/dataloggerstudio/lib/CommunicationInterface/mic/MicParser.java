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

package org.lielas.dataloggerstudio.lib.CommunicationInterface.mic;

public class MicParser{
	
	public static int findNextToken(byte[] in, int start, int end){
        int i = start;
        for(; i < end; i++){
            if(in[i] == 32)
                break;
            if(in[i] == 13)
                break;
        }
        return i+1;
    }
	
	public static String parseTokenToString(byte[] in, int pos, int end){
        byte[] out = new byte[end-pos];
        String str;
        int len = 0;
        for(int i = pos; i < end; i++){
            if(in[i] == 32)
                break;
            if(in[i] == 13)
                break;
            if(in[i] == 0)
                break;
            out[len] = in[i];
            len += 1;
        }
        if(len == 0){
            return "";
        }

        try{
            str = new String(out, 0, len, "UTF-8");
        }catch (Exception e){
            return "";
        }
        return str;
    }
	
	public static long parseTokenToLong(byte[] in, int pos, int end){
        byte[] out = new byte[end-pos];
        int len = 0;

        for(int i = pos; i < end; i++){
            if(in[i] == 32)
                break;
            if(in[i] == 13)
                break;
            if(in[i] == 0)
                break;
            out[len] = in[i];
            len += 1;
        }
        if(len == 0){
            return -1;
        }
        return byteArrayToLong(out);
    }

	public static long byteArrayToLong(final byte[] b){
        long val = 0;
        for(int i=0; i < b.length && b[i] != 0; i++){
            if(b[i] < 48 || b[i] > 57)
                return -1;
            val = (val*10) + (b[i] - 48);
        }
        return val;
    }
	
	
}