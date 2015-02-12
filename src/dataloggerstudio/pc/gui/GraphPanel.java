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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.lielas.dataloggerstudio.lib.LoggerRecord;

public class GraphPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6908231611677946267L;
	
	private LoggerRecord lr;
	
	private final int xpadding = 25;
	private final int ypadding = 25;
	private final int xscales = 6;
	private final int yscales = 10;
	
	private double ymax = 100;
	private double ymin = 0;
	
	private int xmax = 1800;
	
	private final Color[] lineColors = {Color.GREEN, Color.BLUE, Color.RED};
	
	public GraphPanel(){
		//this.setOpaque(false);
		//this.setBorder(null);
	}
	
	public void setLoggerRecord(LoggerRecord loggerRecord){
		lr = loggerRecord;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		int width = (int)getWidth()- xpadding;
		int height = (int)getHeight()- ypadding;
		int x0 = xpadding;
		int y0 = 0;
		int x1 = width + xpadding - 1;
		int y1 = height - 1;
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//draw white background
		g2d.setColor(Color.WHITE);
		g2d.fillRect(x0, y0, width, height);
		
		
		//create y axis
		int scaleHeight = height / yscales;
		double ystep = (ymax + Math.abs(ymin)) / yscales;
		double yval = ymin;
		for(int y = y1; y > 0; y-=scaleHeight){
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.drawLine(x0, y, x1, y);
			g2d.setColor(Color.BLACK);
			g2d.drawLine(x0, y, x0+5, y);
			
			g2d.drawString(String.valueOf(yval), 0, y+4);
			yval += ystep;
		}
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(x1, y0, x1, y1);
		g2d.setColor(Color.BLACK);
		g2d.drawLine(x0, y0, x0, y1);
		

		//create x axis
		int scaleWidth = width / xscales;
		for(int x = (x0 + scaleWidth); x < width; x+=scaleWidth){
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.drawLine(x, y0, x, y1);
			g2d.setColor(Color.BLACK);
			g2d.drawLine(x, y1-5, x, y1);
		}
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(x0, y0, x1, y0);
		g2d.setColor(Color.BLACK);
		g2d.drawLine(x1, y1-5, x1, y1);
		g2d.drawLine(x0, y0, x0+5, y0);
		g2d.drawLine(x0, y1, x1, y1);
		
		if(lr != null){

			int count = lr.getCount();
			int point = 0;
			if(count > xmax){
				point = count - xmax;
			}
			
			//create x axis labels
			//long timespan = lr.get(lr.getCount()-1).getDateTime() - lr.get(0).getDateTime();
			DateTimeFormatter dtfmt = DateTimeFormat.forPattern("HH:mm");
			g2d.setColor(Color.BLACK);
			
			for(int i = 0; i < xscales; i++){
				DateTime dt = new DateTime(lr.get(point).getDateTime() + (i * 600000));
				String strDt = dtfmt.print(dt);
				g2d.drawString(strDt, (x0 - 14) + (i * scaleWidth), y1+20);
			}
			
			//plot values
			
			double k = (double)(y0-y1) / (ymax - ymin) ;
			double d = (double)height;
			
			for(int channel = 0; channel < lr.getChannels(); channel++){
				
				point = 0;
				if(count > xmax){
					point = count - xmax;
				}
				
				g2d.setColor(lineColors[channel]);
				double oldx = x0;
				double fy = (lr.get(0).getValue(channel));
				double oldy = (int)((fy * k) + d);
				double xstep = (double)width/(double)xmax;
				
				for(; point < count; point++){
					double newx = oldx + xstep;
					double newy = (fy * k) + d;
					fy = (lr.get(point).getValue(channel));
					g2d.drawLine((int)oldx, (int)oldy, (int)newx, (int)newy);
					oldx = newx;
					oldy = newy;
				}
			}
		}
		
	}
	
	
	
}