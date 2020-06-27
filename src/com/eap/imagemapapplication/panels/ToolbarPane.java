package com.eap.imagemapapplication.panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.eap.imagemapapplication.ImageMapApplication;
import com.eap.imagemapapplication.events.AreaDragListener;

public class ToolbarPane  extends JPanel   implements ActionListener {
	
	private ImageMapApplication _parent;

	private JFileChooser _fileChooser;
	private void init() {

    	JButton loadImage = new JButton("Load Image");
    	loadImage.addActionListener(this);
    	loadImage.setActionCommand("loadImage");
    	
    	JButton clearButton = new JButton("Clear Image Mask");
    	clearButton.addActionListener(this);
    	clearButton.setActionCommand("clearMask");
    	
    	
    	add(loadImage);
    	add(clearButton);
    	add(new JButton("Save Markings"));
    	add(new JButton("Load Markings"));
    
	}
	public ToolbarPane(ImageMapApplication parent) {
		super(new GridLayout(1,6));
		this._fileChooser = new JFileChooser("/");
		this._parent = parent;
		init();
    	
	}
	
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
       
        System.out.println(cmd);
        switch(cmd) {
        case "clearMask":
        	_parent.getImagePane().clearAllImageMasks();
        	_parent.getImagePane().repaint();
        break;
        case "loadImage":
        	int returnVal = _fileChooser.showOpenDialog(ToolbarPane.this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = _fileChooser.getSelectedFile();
                
                try {
                	_parent.getImagePane().setCurrentImage(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            } 
          break;
        }
        
    }
}
