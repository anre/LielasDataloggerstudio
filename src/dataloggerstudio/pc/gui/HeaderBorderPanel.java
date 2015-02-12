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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class HeaderBorderPanel extends JPanel{
	/**
	 * 
	 */

	public static final int ORIENTATION_LEFT = 0;
	public static final int ORIENTATION_CENTER = 1;
	public static final int ORIENTATION_RIGHT = 2;
	
	private int vertOrientation;
	
	private static final long serialVersionUID = -7583960392898944393L;
	
	public HeaderBorderPanel(){
		vertOrientation = ORIENTATION_LEFT;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		this.setOpaque(false);
		super.paintComponent(g);
		
	}
	
	@Override
	protected void paintBorder(Graphics g){
		super.paintBorder(g);
		
		Dimension d = getSize();
		int w = (int)d.getWidth();
		int h = (int)d.getHeight();

		g.setColor(new Color(250,250,250));
		g.drawLine(0, h-1, w, h-1);
		g.setColor(new Color(192,192,192));
		g.drawLine(0, h-1, w, h-1);
		if(vertOrientation == ORIENTATION_LEFT){
			g.drawLine(0, h-1, 0, h-1);
		}else if(vertOrientation == ORIENTATION_RIGHT){
			g.drawLine(w-1, h-1, w-1, h-1);
		}
	}
	
	public void setVerticalOrientation(int orientation){
		vertOrientation = orientation;
	}
	
	
	
}