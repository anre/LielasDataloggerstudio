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

package org.lielas.dataloggerstudio.pc.CommunicationInterface.mic;

public class MicLoggerDateTime{

	private long time;
	
	public long getTime(){
		return time;
	}
	
	public boolean parse(byte[] line) {
		int i = 2;

		if (line == null)
			return false;

		// parse answer
		if (line[0] != 'c') {
			return false;
		}

		// parse date
		try {
			byte[] t1 = parseHexDate(line, i, 4);
			i = MicParser.findNextToken(line, i, line.length);
			byte[] t2 = parseHexDate(line, i, 4);
			byte[] t = new byte[8];
			for(int j = 0; j < 4; j++){
				t[j] = t1[j];
				t[j+4] = t2[j];
			}
			String tString = new String(t);
			time = Long.parseLong(tString, 16);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private byte[] parseHexDate(final byte[] line, int start, int digits){
		byte[] t = new byte[4];
		int end = MicParser.findNextToken(line, start, line.length);
		int missingZeros = (digits - (end - start -1));
		int i;
		
		for(i = 0; i < missingZeros; i++){
			t[i] = 48;
		}
		for(;i < 4; i++){
			t[i] = line[start + i - missingZeros];
		}
		
		return t;
	}
}