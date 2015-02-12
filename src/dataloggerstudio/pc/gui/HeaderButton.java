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
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;



public class HeaderButton extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1774366806598793416L;
	
	private boolean isActive;
	

	/*private Color borderColorActive1 = new Color(38,136,202);
	private Color borderColorActive2 = new Color(23,83,123);
	private Color bttnColorActive1 = new Color(39,143,214);
	private Color bttnColorActive2 = new Color(27,96,143);
	private Color fontColorActive = Color.WHITE;*/
	private Color borderColorActive1 = new Color(192,192,192);
	private Color borderColorActive2 = new Color(192,192,192);
	private Color bttnColorActive1 = new Color(255,255,255);
	private Color bttnColorActive2 = new Color(250,250,250);
	private Color fontColorActive = Color.BLACK;
	
	private Color borderColor1 = new Color(192,192,192);
	private Color borderColor2 = new Color(192,192,192);
	private Color bttnColor1 = new Color(255,255,255);
	private Color bttnColor2 = new Color(250,250,250);
	private Color fontColor = Color.BLACK;
	
	private Color bttnColorDisabled1 = new Color(255,255,255);
	private Color bttnColorDisabled2 = new Color(250,250,250);
	private Color fontColorDisabled = Color.GRAY;
	
	private int radius = 5;
	
	public HeaderButton(String label){
		super(label);
		isActive = false;
		setFont(new Font("Tahoma", Font.BOLD, 11));
	}
	
	@Override
	protected void paintComponent(Graphics g){
		GradientPaint gp;
		Color textColor;
		int dia = 2 * radius;
		
		
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setFocusable(false);
		super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        Graphics2D g2d = (Graphics2D) g;
        
		if(isActive){
			gp = new GradientPaint(0, 0, bttnColorActive1, 0, h, bttnColorActive2);
			textColor = fontColorActive;
	        
		}else if(!isEnabled()){
			gp = new GradientPaint(0, 0, bttnColorDisabled1, 0, h, bttnColorDisabled2);
			textColor = fontColorDisabled;
		}else{
			gp = new GradientPaint(0, 0, bttnColor1, 0, h, bttnColor2);
			textColor = fontColor;
		}
		

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setPaint(gp);
        g2d.fillRoundRect(radius, 0, w - dia-1, dia, dia, dia);
        g2d.fillRect(radius, radius, w - dia, h);
        g2d.drawLine(radius - 1, h-1, w - radius, h-1);
        
        g2d.setColor(textColor);
        if(this.getText() != null){
	        int stringWidth = (int)g2d.getFontMetrics().getStringBounds(this.getText(), g2d).getWidth();
	        int stringHeight = (int)g2d.getFontMetrics().getStringBounds(this.getText(), g2d).getHeight();
	        g2d.drawString(this.getText(), (getWidth()/2) - (stringWidth/2), (getHeight()/2) + (stringHeight/2));
        }
		
	}
	
	@Override
	protected void paintBorder(Graphics g){
        GradientPaint gp;
        
		int dia = 2 * radius;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int h = getHeight();
        
		if(isActive){
			gp = new GradientPaint(0, 0, borderColorActive1, 0, h, borderColorActive2);
	        g2d.setPaint(gp);
	        
	        

	        g2d.drawArc(-radius, getHeight() - dia , dia,  dia, 300, 60);
	        g2d.drawArc(getWidth() - 1 - radius, getHeight() - dia, dia,  dia, 180, 60);
			g2d.drawLine(0, getHeight()-1, 3, getHeight()-1);
			g2d.drawLine(getWidth() - 3, getHeight()-1, getWidth() - 1, getHeight()-1);
	        
		}else{
			gp = new GradientPaint(0, 0, borderColor1, 0, h, borderColor2);
	        g2d.setPaint(gp);
	        
	        g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
	        g2d.drawLine(radius, getHeight()  - radius, radius, getHeight());
	        g2d.drawLine(getWidth() - 1 - radius, getHeight()  - radius, getWidth() - 1 - radius, getHeight());
	        /*g2d.drawArc(0, 0, dia,  dia, 90, 90);
	        g2d.drawArc(getWidth() - 1 - dia, 0, dia,  dia, 0, 90);
	        g2d.drawLine(radius, 0, getWidth() - radius, 0);
	        g2d.drawLine(0, radius, 0, getHeight() - 1);
	        g2d.drawLine(getWidth() - 1, radius, getWidth() - 1, getHeight() - 1);*/
		}
		
        g2d.drawArc(radius, 0, dia,  dia, 90, 90);
        g2d.drawArc(getWidth() - 1 - dia - radius, 0, dia,  dia, 0, 90);
        g2d.drawLine(dia, 0, getWidth() - dia, 0);
        g2d.drawLine(radius, radius, radius, getHeight()  - radius);
        g2d.drawLine(getWidth() - 1 - radius, radius, getWidth() - 1 - radius, getHeight() - radius);
		
        
	}
	
	@Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width += size.height;
        return size;
    }
	
	public void setActive(boolean active){
		isActive = active;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
}