package com.eap.imagemapapplication;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import com.eap.imagemapapplication.events.AreaDragListener;
import com.eap.imagemapapplication.panels.ImageMapPane;
import com.eap.imagemapapplication.panels.ToolbarPane;

public class ImageMapApplication extends JFrame {
	
	private ImageMapPane _imagePane;
	private ToolbarPane _toolbarPane;
	
	public ImageMapApplication() throws IOException {
		_imagePane = new ImageMapPane();
		_toolbarPane = new ToolbarPane(this);
		
		init();
	}
	
	public void init() {
		setSize(1024,768);
		setFont(new Font("verdana", Font.PLAIN, '9'));
		setResizable(false);
		setLayout(new BorderLayout());
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		
        JPanel imagePaneContainer = new JPanel(new GridLayout(1,1));
    	imagePaneContainer.add( new JScrollPane(_imagePane));
    	
        getContentPane().add(_toolbarPane, BorderLayout.NORTH);
        getContentPane().add(imagePaneContainer, BorderLayout.CENTER);

        setVisible(true);
        
	}
	public Dimension getPreferredSize()
	{
	     return new Dimension(1024,768);
	}

	public static void main(String[] args) {
	    java.awt.EventQueue.invokeLater(new Runnable() {
	          public void run() {
	        	  try {
					ImageMapApplication frame = new ImageMapApplication();
				} catch (IOException e) {
					e.printStackTrace();
				}
	          }
	    });
	}

	public ImageMapPane getImagePane() {
		return _imagePane;
	}
}
