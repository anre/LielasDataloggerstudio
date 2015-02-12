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
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	/**
	 * 
	 */

	public static final int ORIENTATION_LEFT = 0;
	public static final int ORIENTATION_CENTER = 1;
	public static final int ORIENTATION_RIGHT = 2;
	
	private boolean useIcon = true;
	
	private int vertOrientation;
	private boolean repeaty;
	
	private static final long serialVersionUID = -7583960392898944394L;
	ImageIcon icon;

	private BufferedImage image;
	
	public ImagePanel(String path){
		vertOrientation = ORIENTATION_LEFT;
		if(useIcon){
			try{
				//image = ImageIO.read(new File(path));
				icon = new ImageIcon(getClass().getClassLoader().getResource("img/img/" + path));
				//image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				//Graphics g = image.createGraphics();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			try{
				image = ImageIO.read(new File("img/" + path));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g){
		this.setOpaque(false);
		super.paintComponent(g);
		
		Dimension d = getSize();
		
		if(icon != null || image != null){
			
			if(repeaty){
				int y = 0;
				while(y < d.getHeight()){
					icon.paintIcon(null, g, 0,  y);
					y += icon.getIconHeight();
				}
			}else{
				int x = 0;
				
				if(vertOrientation == ORIENTATION_RIGHT){
					x = d.width - icon.getIconWidth();
				}else if(vertOrientation == ORIENTATION_CENTER){
					x = (d.width/2) - (icon.getIconWidth()/2);
				}

				if(useIcon){
					icon.paintIcon(null, g, x,  0);
				}else{
					g.drawImage(image,  0,  0,  null);
				}
			}
			
		}
	}
	
	public void setVerticalOrientation(int orientation){
		vertOrientation = orientation;
	}
	
	public void setRepeatImageY(boolean repeaty){
		this.repeaty = repeaty;
	}
	
	
}