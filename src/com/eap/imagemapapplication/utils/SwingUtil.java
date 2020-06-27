package com.eap.imagemapapplication.utils;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable;
import javax.swing.RootPaneContainer;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;

import javafx.application.Application;

import static java.awt.RenderingHints.KEY_RENDERING;
import static java.awt.RenderingHints.VALUE_RENDER_QUALITY;
import static java.awt.Transparency.TRANSLUCENT;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.max;
import static java.lang.String.format;
import static javax.swing.SwingUtilities.invokeLater;


import static java.awt.Cursor.DEFAULT_CURSOR;
import static java.awt.Cursor.WAIT_CURSOR;
import static java.awt.dnd.DragSource.DefaultMoveDrop;
public class SwingUtil {

	public static class JMenuHelper {

	    public static JMenu createMenu(String name) {
	        JMenu menu = new JMenu(getString(name + "-menu"));
	        menu.setName(name);
	        String mnemonic = getOptionalString(name + "-menu-mnemonic");
	        if (mnemonic != null && mnemonic.length() > 0)
	            menu.setMnemonic(mnemonic.charAt(0));
	        return menu;
	    }

	    public static JMenuItem createItem(String name, Action action) {
	        JMenuItem item = new JMenuItem(action);
	        initializeItem(name, item);
	        return item;
	    }

	    public static JCheckBoxMenuItem createCheckBoxItem(String name, boolean state, Action action) {
	         JCheckBoxMenuItem item = new JCheckBoxMenuItem(action);
	        item.setState(state);
	        initializeItem(name, item);
	        return item;
	    }

	    public static JRadioButtonMenuItem createRadioItem(String name, Action action) {
	        JRadioButtonMenuItem item = new JRadioButtonMenuItem(action);
	        initializeItem(name, item);
	        return item;
	    }

	    private static void initializeItem(String name, JMenuItem item) {
	        item.setName(name);
	        item.setText(getString(name + "-action"));
	        String tooltip = getOptionalString(name + "-action-tooltip");
	        if (tooltip != null)
	            item.setToolTipText(tooltip);
	        String iconUrl = getOptionalString(name + "-action-icon");
	    }

	    private static String getOptionalString(String z) {
			return z;
		}

		private static String getString(String string) {
			return string;
		}

	    public static JMenu findMenu(JMenuBar menuBar, String menuName) {
	        for (int i = 0; i < menuBar.getMenuCount(); i++) {
	            JMenu menu = menuBar.getMenu(i);
	            if (menuName.equals(menu.getName()))
	                return menu;
	        }
	        return null;
	    }

	    public static JMenu findMenu(JMenuBar menuBar, String menuName, String subMenuName) {
	        return findMenuComponent(menuBar, menuName, subMenuName, JMenu.class);
	    }

	    public static JMenuItem findItem(JMenuBar menuBar, String menuName, String menuItemName) {
	        return findMenuComponent(menuBar, menuName, menuItemName, JMenuItem.class);
	    }

	    public static Component findMenuComponent(JPopupMenu menu, String menuComponentName) {
	        for (int i = 0; i < menu.getComponentCount(); i++) {
	            Component component = menu.getComponent(i);
	            if (menuComponentName.equals(component.getName()))
	                return component;
	        }
	        return null;
	    }

	    private static <T extends Component> T findMenuComponent(JMenu menu, String menuComponentName, Class<T> componentClass) {
	        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
	            Component component = menu.getMenuComponent(i);
	            if (menuComponentName.equals(component.getName()) && componentClass.isInstance(component))
	                return componentClass.cast(component);
	        }
	        return null;
	    }

	    public static <T extends Component> T findMenuComponent(JMenuBar menuBar, String menuName, String menuComponentName, Class<T> componentClass) {
	        JMenu menu = findMenu(menuBar, menuName);
	        if (menu != null) {
	            return findMenuComponent(menu, menuComponentName, componentClass);
	        }
	        return null;
	    }
	}
	
	static class ImageHelper {
	    public static BufferedImage resize(BufferedImage image, int width, int height) {
	        BufferedImage bufferedImage = new BufferedImage(width, height, TRANSLUCENT);
	        Graphics2D graphics = bufferedImage.createGraphics();
	        graphics.addRenderingHints(new RenderingHints(KEY_RENDERING, VALUE_RENDER_QUALITY));
	        graphics.drawImage(image, 0, 0, width, height, null);
	        graphics.dispose();
	        return bufferedImage;
	    }

	    public static BufferedImage resize(File file, int height) {
	        try {
	            BufferedImage image = ImageIO.read(file);
	            if (image == null)
	                return null;
	            double factor = (double) height / image.getHeight();
	            return resize(image, (int) (image.getWidth() * factor), height);
	        } catch (IOException e) {
	            throw new IllegalArgumentException(e);
	        }
	    }
	}
	
	static class JTableHelper {
	
	    private static final int MINIMUM_ROW_HEIGHT = 16;
	    private static final int ROW_HEIGHT_MAGIC_CONSTANT = 4;

	    public static int calculateRowHeight(Object objectWithTable, TableCellEditor cellEditor, Object cellValue) {
	        Component component = cellEditor.getTableCellEditorComponent(null, cellValue, true, 0, 0);
	        int rowHeight = Math.max(component.getPreferredSize().height - ROW_HEIGHT_MAGIC_CONSTANT, MINIMUM_ROW_HEIGHT);
	        return rowHeight;
	    }

	    public static void scrollToPosition(JTable table, int insertRow) {
	        Rectangle rectangle = table.getCellRect(insertRow, 1, true);
	        table.scrollRectToVisible(rectangle);
	    }

	    public static void selectPositions(final JTable table, final int index0, final int index1) {
	        invokeLater(new Runnable() {
	            public void run() {
	                table.getSelectionModel().setSelectionInterval(index0, index1);
	            }
	        });
	    }

	    public static void selectAndScrollToPosition(JTable table, int index0, int index1) {
	        selectPositions(table, index0, index1);
	        scrollToPosition(table, index0);
	    }

	    public static boolean isFirstToLastRow(TableModelEvent e) {
	        return e.getFirstRow() == 0 && e.getLastRow() == MAX_VALUE;
	    }
	}
	
	
	public static class UIHelper {

	    public static void setLookAndFeel() {
	        try {
	            String lookAndFeelClass = "default";
	            if ("default".equals(lookAndFeelClass))
	                lookAndFeelClass = UIManager.getSystemLookAndFeelClassName();
	            UIManager.setLookAndFeel(lookAndFeelClass);
	        } catch (Exception e) {
	            // intentionally do nothing
	        }
	        JFrame.setDefaultLookAndFeelDecorated(true);
	        JDialog.setDefaultLookAndFeelDecorated(true);

	    }

	    public static void startWaitCursor(JComponent component) {
	        RootPaneContainer root = (RootPaneContainer) component.getTopLevelAncestor();
	        startWaitCursor(root.getGlassPane());
	        root.getGlassPane().setVisible(true);
	    }

	    private static void startWaitCursor(Component component) {
	        component.setCursor(Cursor.getPredefinedCursor(WAIT_CURSOR));
	    }

	    public static void startDragCursor(Component component) {
	        component.setCursor(DefaultMoveDrop);
	    }

	    public static boolean isDragCursor(Component component) {
	        return component.getCursor().equals(DefaultMoveDrop);
	    }

	    public static void stopWaitCursor(JComponent component) {
	        RootPaneContainer root = (RootPaneContainer) component.getTopLevelAncestor();
	        stopWaitCursor(root.getGlassPane());
	        root.getGlassPane().setVisible(false);
	    }

	    public static void stopWaitCursor(Component component) {
	        component.setCursor(Cursor.getPredefinedCursor(DEFAULT_CURSOR));
	    }

	    public static JFileChooser createJFileChooser() {
	        JFileChooser chooser;
	        try {
	            chooser = new JFileChooser();
	        } catch (Exception npe) {
	            UIManager.getDefaults().put("FileChooserUI", "javax.swing.plaf.metal.MetalFileChooserUI");
	            chooser = new JFileChooser();
	        }
	        return chooser;
	    }

	    public static void patchUIManager(ResourceBundle bundle, String... keys) {
	        for (String key : keys) {
	            try {
	                if (bundle.containsKey(key))
	                    UIManager.getDefaults().put(key, bundle.getString(key));
	            } catch (MissingResourceException e) {
	                // intentionally left empty
	            }
	        }
	    }

	    private static FontMetrics fontMetrics;

	    public static int getMaxWidth(String string, int extraWidth) {
	        if (fontMetrics == null) {
	            JLabel label = new JLabel();
	            fontMetrics = label.getFontMetrics(label.getFont());
	        }
	        int width = fontMetrics.stringWidth(string);
	        return width + extraWidth;
	    }
	}
	
	public static class FontUtil {
		
		public static void drawCenteredString(Graphics g, Rectangle2D r, String s,  Font font) {
		    FontRenderContext frc =  new FontRenderContext(null, true, true);

		    Rectangle2D r2D = font.getStringBounds(s, frc);
		    int rWidth = (int) Math.round(r2D.getWidth());
		    int rHeight = (int) Math.round(r2D.getHeight());
		    int rX = (int) Math.round(r2D.getX());
		    int rY = (int) Math.round(r2D.getY());

		    double a = (r.getWidth() / 2) - (rWidth / 2) - rX;
		    double b = (r.getHeight() / 2) - (rHeight / 2) - rY;

		    g.setFont(font);
		    g.drawString(s, (int)(r.getX() + a), (int)(r.getY() + b));
		}
	}
}
