package com.eap.imagemapapplication.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.eap.imagemapapplication.utils.SwingUtil.FontUtil;

/**
 * 
 * Image Mask Rectangle
 * Stores the coordinates and the current drawing
 * 
 * @author Patrick McDonald
 *
 */
public class ImageMaskRectangle {
	private Rectangle2D _rectangle;
	private String _maskName;
	private int _index;
	
	public ImageMaskRectangle(Rectangle2D rect, String maskName, int index) {
		this._rectangle = rect;
		this._maskName = maskName;
		this._index = index;
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(Color.RED.darker());
		g2.draw(_rectangle);
		FontUtil.drawCenteredString(g2.create(), _rectangle, _maskName, g2.getFont());
	}

	
	public boolean clickInsideRectangle(int x, int y) {
		if (x > _rectangle.getMinX() && x < _rectangle.getMaxX() && y > _rectangle.getMinY() && y < _rectangle.getMaxY())
			return true;

		return false;
	}

	@Override
	public String toString() {
		return "ImageMaskRectangle [_rectangle=" + _rectangle + " Index: " + _index + "]";
	}

	
}
