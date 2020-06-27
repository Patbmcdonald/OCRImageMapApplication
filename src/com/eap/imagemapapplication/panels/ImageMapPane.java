package com.eap.imagemapapplication.panels;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.eap.imagemapapplication.events.AreaDragListener;
import com.eap.imagemapapplication.model.ImageMaskRectangle;
import com.eap.imagemapapplication.utils.SwingUtil.FontUtil;

/**
 * 
 * Image Map Pane 
 * 
 * @author Patrick McDonald
 *
 */
public class ImageMapPane extends JPanel {
	
	/** Our start point for rectangle drawing **/
	public Point start = new Point();
	
	/** 
	 */
	private List<ImageMaskRectangle> _imageMaskRectangles; 
	
	/** Our finished point **/
	public Point finish = new Point();
	
	/** Our rectangle **/
	private Rectangle2D rectangle;
	
	/** Are we drawing? **/
	private boolean drawing;

	private int w;
	private int h;
	
	private BufferedImage currentImage;
	
	public ImageMapPane() throws IOException {
		super(null);
		_imageMaskRectangles = new ArrayList<ImageMaskRectangle>();
		init();
	}
	
	private void init() {

		setLayout(null);
		setBackground(Color.BLACK);
		
		AreaDragListener areaDragListener = new AreaDragListener(this);
		addMouseListener(areaDragListener);
		addMouseMotionListener(areaDragListener);
    
	}
	
	/**
	 * 
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g.create();

		if(currentImage == null) {
			g2.setColor(Color.WHITE.brighter());
			FontUtil.drawCenteredString(g2.create(), new Rectangle2D.Float(0,0,getWidth(), getHeight()), "Please click \"Load Image\" to get started.", g2.getFont());
			g2.dispose();
			return;
		}
        g.drawImage(currentImage, 0, 0, getWidth(), getHeight(), this);
		   
		for(ImageMaskRectangle rect : _imageMaskRectangles)
			rect.draw(g2);
		
		if (getCurrentRectangle() != null && isDrawing()) 
			drawCurrentRectangle(g2);
		
		g2.dispose();
	}

	private void drawCurrentRectangle(Graphics2D g2) {
		
		
		AlphaComposite old = (AlphaComposite) g2.getComposite();
		Font saved = g2.getFont();
		AlphaComposite ta = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		g2.setComposite(ta);

		/** cut out our current rectangle to show a highlight effect **/
		Area area = new Area();
		area.add(new Area(new Rectangle2D.Float(0, 0, getWidth(), getHeight())));
		area.subtract(new Area(getCurrentRectangle()));
		
		g2.setColor(Color.RED.darker());
		g2.setStroke(new BasicStroke(2));
		g2.draw(getCurrentRectangle());
		
		g2.setStroke(g2.getStroke()); // reset our stroke
		g2.setColor(Color.black.darker());
		g2.fill(area);
		area.reset();

		g2.setFont(saved);
		g2.setComposite(old);
	}

	public void setCurrentImage(File file) throws IOException {
		this.currentImage = ImageIO.read(file);
		this.h = currentImage.getHeight();
		this.w = currentImage.getWidth();
		revalidate();
		repaint();
		
	}
	
	public List<ImageMaskRectangle> getImageMaskRectangles(){
		return _imageMaskRectangles;
	}

	public Dimension getPreferredSize() {
		return new Dimension(w,h);
	}

	public Rectangle2D getCurrentRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle2D rectangle) {
		this.rectangle = rectangle;
	}

	public void setFinish(Point point) {
		this.finish = point;
	}

	public void setStart(Point point) {
		this.start = point;
	}

	public boolean isDrawing() {
		return drawing;
	}

	public void setDrawing(boolean isDrawing) {
		this.drawing = isDrawing;
	}
	
	public void addImageMaskRectangle(ImageMaskRectangle rect)
	{
		_imageMaskRectangles.add(rect);
	}
	
	public void clearAllImageMasks() {
		_imageMaskRectangles.clear();
	}
}