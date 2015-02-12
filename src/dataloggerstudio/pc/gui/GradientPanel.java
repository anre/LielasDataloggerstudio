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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class GradientPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7583960392898944394L;
	private Color c1, c2;
	private Color b1, b2;
	
	boolean gradientBorder;
	
	public GradientPanel(Color colorTop, Color colorBottom){
		c1 = colorTop;
		c2 = colorBottom;
		gradientBorder = false;
	}
	

	public GradientPanel(Color colorTop, Color colorBottom, Color borderTop, Color borderBottom){
		c1 = colorTop;
		c2 = colorBottom;
		gradientBorder = true;
		b1 = borderTop;
		b2 = borderBottom;
	}
	
	@Override
	protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, c1, 0, h, c2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
	}
	
	@Override
	protected void paintBorder(Graphics g){
		if(!gradientBorder){
			super.paintBorder(g);
		}else{
			this.setBorder(null);
			super.paintBorder(g);

	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        int w = getWidth();
	        int h = getHeight();
	        GradientPaint gp = new GradientPaint(0, 0, b1, 0, h, b2);
	        g2d.setPaint(gp);
			g2d.drawLine(0, 0, w, 0);
			g2d.drawLine(0, h, w, h);
			g2d.drawLine(0, 0, 0, h);
			g2d.drawLine(w, 0, w, h);
		}
	}
	
}