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

package org.lielas.dataloggerstudio.pc.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.JLabel;
import javax.swing.table.JTableHeader;

public class MouseOverHintManager implements MouseListener{

	private Map<Component, String> hintMap;
	private JLabel hintLabel;
	
	public MouseOverHintManager(JLabel label){
		hintMap = new WeakHashMap<Component, String>();
		this.hintLabel = label;
	}
	
	public void addHintFor(Component comp, String hintText){
		hintMap.put(comp,  hintText);
	}
	
	private String getHintFor( Component comp ) {

		String hint = (String)hintMap.get(comp);
		if ( hint == null ) {
			if ( comp instanceof JLabel )
				hint = (String)hintMap.get(((JLabel)comp).getLabelFor());
		    else if ( comp instanceof JTableHeader )
		    	hint = (String)hintMap.get(((JTableHeader)comp).getTable());
		    }
	    return hint;
	}	
	
	public void enableHints(Component comp){
		comp.addMouseListener(this);
		if(comp instanceof Container){
			Component[] components = ((Container)comp).getComponents();
			for(int i=0; i < components.length; i++){
				enableHints(components[i]);
			}
		}
		
	}

	public void removeAll(){
		hintMap = new WeakHashMap<Component, String>();
	}
	
	public void mouseEntered(MouseEvent e) {	
		Component comp = (Component)e.getSource();
		String hint;
		do{
			hint = getHintFor(comp);
			comp = comp.getParent();
		}while((hint == null) && (comp!=null));
		if(hint!=null){
			hintLabel.setText(hint);
		}
	}
	
	public void mouseClicked(MouseEvent arg0) {	}

	public void mouseExited(MouseEvent arg0) { }

	public void mousePressed(MouseEvent arg0) {	}

	public void mouseReleased(MouseEvent arg0) { }
	
}