package com.eap.imagemapapplication.events;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JOptionPane;

import com.eap.imagemapapplication.model.ImageMaskRectangle;
import com.eap.imagemapapplication.panels.ImageMapPane;

@SuppressWarnings("restriction")
public class AreaDragListener extends MouseAdapter {

	private ImageMapPane parent;
	private boolean _dragging;

	public AreaDragListener(ImageMapPane parent) {
		this.parent = parent;
	}

	private boolean isInsideExistingElement(MouseEvent e) {
		List<ImageMaskRectangle> currentRectangles = parent.getImageMaskRectangles();

		int x = e.getPoint().x;
		int y = e.getPoint().y;

		for (ImageMaskRectangle rect : currentRectangles)
			if (rect.clickInsideRectangle(x, y)) {
				System.out.println(rect);
				return true;
			}

		return false;
	}

	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		int x = e.getPoint().x;
		int y = e.getPoint().y;

		if (isInsideExistingElement(e)) {
			System.out.println("Inside Element! " + e.getPoint());
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);

		if (isInsideExistingElement(e))
			return;

		if (!_dragging) {

			this.parent.setStart(e.getPoint());
			this.parent.setDrawing(true);
			_dragging = true;
		}

		// set the end point to our mouse point
		this.parent.setFinish(e.getPoint());
		
		// calculate the size of the selection rectangle
		int x = Math.min(this.parent.start.x, this.parent.finish.x);
		int y = Math.min(this.parent.start.y, this.parent.finish.y);
		int width = Math.abs(this.parent.start.x - this.parent.finish.x);
		int height = Math.abs(this.parent.start.y - this.parent.finish.y);
		
		// draw the rectangle
		this.parent.setRectangle(new Rectangle2D.Double(x, y, width, height));
		// repaint the panel every drag
		this.parent.repaint();

		System.out.println("mouseDragged: " + e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);

		if (isInsideExistingElement(e))
			return;

		if (!_dragging)
			return;

		_dragging = false;

		int x = Math.min(this.parent.start.x, this.parent.finish.x);
		int y = Math.min(this.parent.start.y, this.parent.finish.y);
		int width = Math.abs(this.parent.start.x - this.parent.finish.x);
		int height = Math.abs(this.parent.start.y - this.parent.finish.y);

		String data = JOptionPane.showInputDialog(null, "Enter Mask Name", "Image Mask", JOptionPane.PLAIN_MESSAGE);

		this.parent.setRectangle(null);
		
		if(data == null) {
			this.parent.repaint();
			return;
		}
		
		int index = this.parent.getImageMaskRectangles().size();
		
		this.parent.addImageMaskRectangle(new ImageMaskRectangle(new Rectangle2D.Double(x, y, width, height), data, index));
		this.parent.repaint();

		System.out.println("mouseReleased: " + e.getPoint());
	}
}