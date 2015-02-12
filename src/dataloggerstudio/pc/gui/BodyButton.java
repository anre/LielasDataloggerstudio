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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class BodyButton extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2778033058289901381L;

	Color bgColor1 = new Color(255,179,8);
	Color bgColor2 = new Color(242,168,0);
	Color bgColorClicked1 = new Color(255,200,63);
	Color bgColorClicked2 = new Color(255,160,63);
	
	Color borderColor1 = new Color(180,180,180);
	Color borderColor2 = new Color(120,120,120);
	Color borderColorClicked1 = new Color(180,180,180);
	Color borderColorClicked2 = new Color(120,120,120);
	
	Color textColor = Color.BLACK;
	
	private final int radius = 10;
	
	public BodyButton(String label){
		super(label);
	}
	

	
	@Override
	protected void paintComponent(Graphics g){
		GradientPaint gp;
		
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setFocusable(false);
		super.paintComponent(g);
		
		Dimension d = getPreferredSize();
		int x = (int)d.getWidth() - 1;
		int y = (int)d.getHeight() - 1;
		
		if(getModel().isPressed()){
			gp = new GradientPaint(0, 0, bgColorClicked1, 0, y, bgColorClicked2);
		}else{
			gp = new GradientPaint(0, 0, bgColor1, 0, y, bgColor2);
		}

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setPaint(gp);
        g2d.fillRoundRect(0, 0, x, y, radius, radius);
        
        g2d.setColor(textColor);
        int stringWidth = (int)g2d.getFontMetrics().getStringBounds(this.getText(), g2d).getWidth();
        int stringHeight = (int)g2d.getFontMetrics().getStringBounds(this.getText(), g2d).getHeight();
        g2d.drawString(this.getText(), (getWidth()/2) - (stringWidth/2), (getHeight()/2) + (stringHeight/3));
	}
	

	@Override
	protected void paintBorder(Graphics g){
        GradientPaint gp;
		Dimension d = getPreferredSize();
		int x = (int)d.getWidth() - 1;
		int y = (int)d.getHeight() - 1;
		
        if(getModel().isPressed()){
			gp = new GradientPaint(0, 0, borderColorClicked1, 0, y, borderColorClicked2);
        }else{
			gp = new GradientPaint(0, 0, borderColor1, 0, y, borderColor2);
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setPaint(gp);
        g2d.drawRoundRect(0, 0, x, y, radius, radius);
        
        
	}
	
}