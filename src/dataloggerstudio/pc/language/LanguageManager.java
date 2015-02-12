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

package org.lielas.dataloggerstudio.pc.language;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lielas.dataloggerstudio.lib.Logger.LoggerType;

public class LanguageManager {

	// public static String  = "";

	public static String LANG_EN = "en";

	private static LanguageManager instance = null;

	private String lang;
	private String langComment = "/";
	private String langDelimiter = "=";

	private int nrOfTokens;
	
	private HashMap<Integer, String>	map;
	private HashMap<Integer, String> oldMap;

	private boolean dbgMode;

	private LanguageManager() {
		lang = LANG_EN;
		dbgMode = false;
		loadLanguage();
	}

	public static LanguageManager getInstance() {
		if (instance == null) {
			instance = new LanguageManager();
		}
		return instance;
	}


	public void loadLanguage() {
		try {
			File file = new File("lang\\" + lang + ".lang");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF8"));
			String line;
			String[] tmp;
			HashMap<Integer, String> tmpMap = new HashMap<Integer, String>();
			boolean error = false;
			int len = 0;

			line = br.readLine();
			line = line.replaceAll("[\uFEFF-\uFFFF]", "");
			while (line != null && error == false) {
				if (line.startsWith(langComment) || line.isEmpty()) {
					// comment line or empty line, do nothing
				} else {
					tmp = line.split(langDelimiter);
					tmp[1] = tmp[1].replace("\\n", "\n");
					if (tmp.length == 2) {
						Integer id;
						try {
							id = Integer.parseInt(tmp[0].substring(3, tmp[0].length()));
						}catch(NumberFormatException e){
							id = tmpMap.size();
						}
						tmpMap.put(id, tmp[1]);
						len += 1;
					} else {
						error = true;
					}
				}
				line = br.readLine();
			}

			if (error == false) {
				for (nrOfTokens = 0; nrOfTokens < len; nrOfTokens++) {
					oldMap = map;
					map = tmpMap;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setLanguage(String lang) {
		this.lang = lang;
	}

	public String getString(int id){
		if(dbgMode){
			return Integer.toString(id);
		}
		String str = map.get(id);
		if(str == null)
			return "";
		return str;
	}

	public String getString(String description) {
		return "";
		/*String str = map.get(description);
		
		if(str == null)
			return "";
		return str;*/
	}

	public String getOldString(String description) {
		String str = oldMap.get(description);
		
		if(str == null)
			return "";
		return str;
	}

	public void switchDebugMode(){
		dbgMode = !dbgMode;
	}

	public int getId(String str){
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Integer, String> pairs = (Map.Entry<Integer, String>)it.next();

		}
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			String value = entry.getValue();
			if(value.equals(str)){
				return entry.getKey();
			}
		}
		return 0;
	}

}