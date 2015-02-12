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


import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;


public class ImageButton extends JButton{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1717254711381265839L;
	
	ImageIcon icon;
	ImageIcon iconClicked;
	
	public ImageButton(String path){
		super("");
		try{
			icon = new ImageIcon(getClass().getClassLoader().getResource("img/icons/" + path));
		}catch(Exception e){
			
		}
		icon = null;
	}

	public ImageButton(String path1, String path2){
		super("");
		try{
			icon = new ImageIcon(getClass().getClassLoader().getResource("img/icons/" + path1));
			iconClicked = new ImageIcon(getClass().getClassLoader().getResource("img/icons/" + path2));
		}catch(Exception e){
			
		}
	}
	
	@Override
	protected void paintComponent(Graphics g){
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setFocusable(false);
		super.paintComponent(g);
		
		if(icon!= null){
			
			Dimension d = getPreferredSize();
			
			int x = (d.width/2) - (icon.getIconWidth()/2);
			int y = (d.height/2) - (icon.getIconHeight()/2);
			
			if(iconClicked != null){
				if(getModel().isPressed()){
					iconClicked.paintIcon(null, g, x,  y);
				}else{
					icon.paintIcon(null, g, x,  y);
				}
			}else{
				icon.paintIcon(null, g, x,  y);
			}
		}
	}
	
}