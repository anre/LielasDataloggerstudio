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

package org.lielas.dataloggerstudio.pc.gui.Toast;


import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;


import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.sun.deploy.util.StringUtils;

public class Toast extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9127868293979562170L;
	
	String msg;
	JFrame frame;
	
	public static final int DURATION_SHORT = 4000;
	public static final int DURATION_LONG = 5000;
	
	private final float MAX_OPACITY = 0.6f;
	private final float OPACITY_INCREMENT = 0.05f;
	
	AWTEventListener mouseListener;
	
	public Toast(JFrame frame, String msg){
		super(frame, false);
		this.msg = msg;
		this.frame = frame;
		initComponents();

	}
	
	private void initComponents(){
		int i;
		int row;
		String[] lineString = null;
		
		/*this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e){
				setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
			}
		});*/
		
        setUndecorated(true);
        //setSize(frame.getWidth(), frame.getHeight());

		Font font = new Font("Tahoma", Font.PLAIN, 11);
		lineString = splitString(msg);
		setSize(300,36+(lineString.length*14));

        setLocationRelativeTo(frame);// adding toast to frame or use null
        Point p = frame.getLocation();
        p.x = p.x + frame.getWidth() - getWidth() - 49;
        p.y = p.y + frame.getHeight() - getHeight() - 25;
        setLocation(p);
        getContentPane().setBackground(Color.BLACK);
         
        // Determine what the GraphicsDevice can support.
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        final boolean isTranslucencySupported =
                gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);
 
        //If shaped windows aren't supported, exit.
        if (!gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT)) {
            System.err.println("Shaped windows are not supported");
        }
 
        if (isTranslucencySupported) {
            setOpacity(MAX_OPACITY);
        } else {
            System.out.println("Translucency is not supported.");
        }

		//create rowSpec
		int rowspecs = (lineString.length*2)+1;
		RowSpec rowSpec[] = new RowSpec[rowspecs];
		rowSpec[0] = RowSpec.decode("7dlu:grow");
		rowSpec[1] = FormFactory.PREF_ROWSPEC;
		for(i = 2; i < (rowspecs-1); i+=2){
			rowSpec[i] = RowSpec.decode("4px");
			rowSpec[i+1] = FormFactory.PREF_ROWSPEC;
		}
		rowSpec[i] = RowSpec.decode("7dlu:grow");

        getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
               		ColumnSpec.decode("7dlu:grow"),
               		FormFactory.PREF_COLSPEC,
               		ColumnSpec.decode("7dlu:grow"),},
               		rowSpec));


		row = 2;
		for (i = 0; i < lineString.length ; i++) {
			JLabel label = new JLabel();
			getContentPane().add(label, getLayoutString(2, row, "left", "center"));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setForeground(Color.WHITE);
			label.setText(lineString[i]);
			row += 2;
		}
        

		Timer timer = new Timer(Toast.DURATION_SHORT, new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				fadeOut();
			}
		});
		timer.setRepeats(false);
		timer.start();
		setVisible(true);
		
		/*JPanel transparentPanel = new JPanel();
		transparentPanel.setBackground(new Color(0,0,0,100));
		transparentPanel.setSize(frame.getWidth(), frame.getHeight());
		getContentPane().add(transparentPanel, "1, 1, fill, top");*/
		
		
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				fadeOut();
			}

			public void mouseEntered(MouseEvent arg0) {
				
			}

			public void mouseExited(MouseEvent arg0) {
				
			}

			public void mousePressed(MouseEvent arg0) {
				
			}

			public void mouseReleased(MouseEvent arg0) {
				
			}
		});
		
		
	}

	private String[] splitString(String str){
		String[] s;
		int lines = 1;
		int pos = 0;
		int line = 0;
		int i;

		//calculate number of lines
		for(i = 0; i < str.length(); i++){
			if(str.charAt(i) == '\n'){
				lines += 1;
			}
		}

		//split string
		s = new String[lines];
		for(i = 0; i < (str.length()-1); i++){
			if(str.charAt(i) == '\n'){
				s[line++] = str.substring(pos, i);
				pos = i + 1;
			}
		}
		s[line] = str.substring(pos, str.length());
		return s;
	}


	protected String getLayoutString(int column, int row, String xAlignement, String yAlignement){
		return Integer.toString(column) + " ," + Integer.toString(row) + " , " +
				xAlignement + " , " + yAlignement;
	}

	public void close(){
		setVisible(false);
	}

	public void fadeOut() {
		Toolkit.getDefaultToolkit().removeAWTEventListener(mouseListener);
		final Timer timer = new Timer(20, null);
		timer.setRepeats(true);
		timer.addActionListener(new ActionListener() {
			private float opacity = MAX_OPACITY;
		
			public void actionPerformed(ActionEvent e) {
				opacity -= OPACITY_INCREMENT;
				setOpacity(Math.max(opacity, 0));
				if (opacity <= 0) {
					timer.stop();
					setVisible(false);
					dispose();
				}
			}
		});
		setOpacity(MAX_OPACITY);
		timer.start();
		}
	
}