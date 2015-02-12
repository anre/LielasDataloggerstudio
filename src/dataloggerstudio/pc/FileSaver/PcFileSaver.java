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

package org.lielas.dataloggerstudio.pc.FileSaver;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.lielas.dataloggerstudio.lib.FileCreator.FileSaver;

public class PcFileSaver extends FileSaver{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6987340062006313069L;
	
	public PcFileSaver(){
		super(PC_FILE_SAVER);
	}
	
	
	@Override
	public int save(final StringBuilder sb, String path, boolean overWrite){
		
		//check if filename exists
		if(!overWrite){
			if(fileExists(path)){
				return ERROR_FILE_EXISTS;
			}
		}
		
		//write file
		if(!writeFile(sb, path)){
			return ERROR_FAILED_TO_WRITE_FILE;
		}
		
		return STATUS_OK;
	}
	
	
	
	
	private boolean writeFile(final StringBuilder sb, String path){
		PrintWriter writer;
		byte[] bom = new byte[3];  
		bom[0] = (byte) 0xEF;  
		bom[1] = (byte) 0xBB;  
		bom[2] = (byte) 0xBF; 
		String strBom = "";
		
		try {
			strBom = new String(bom, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		try {
			writer = new PrintWriter(path, "UTF-8");
			if(writer.checkError()){
				writer.close();
				return false;
			}
			writer.print(strBom);
			writer.print(sb);
			if(writer.checkError()){
				writer.close();
				return false;
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean fileExists(String path){
		File f = new File(path);
		if(f.exists()){
			return true;
		}
		return false;
	}
	
}