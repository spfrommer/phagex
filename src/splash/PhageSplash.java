package splash;

import glcommon.image.ImageUtils;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Shows the splash screen on startup. Adapted from phage2d project.
 */
public class PhageSplash extends JFrame {
	private static final long serialVersionUID = -8212453752436500345L;

	public PhageSplash() {
		this.setUndecorated(true);
		this.setLayout(null);
		this.setSize(350, 300);
		// this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(new Color(250, 250, 250));
		int libVerticalOffset = 25;
		int libHorizontalOffset = 215;
		int libLogoWidth = 70;
		int libLogoHeight = 30;
		int libLogoPadding = 10;

		JLabel phageLogo = new JLabel(new ImageIcon(scaleImage(readImage("/splash/logo1.png"), 140, 200)));
		phageLogo.setBounds(35, 20, 140, 200);
		this.add(phageLogo);

		JLabel lwjglLogo = new JLabel(new ImageIcon(scaleImage(readImage("/splash/lwjgl.png"), libLogoWidth,
				libLogoHeight)));
		lwjglLogo.setBounds(libHorizontalOffset, libVerticalOffset, libLogoWidth, libLogoHeight);
		this.add(lwjglLogo);

		JLabel dyn4jLogo = new JLabel(new ImageIcon(scaleImage(readImage("/splash/dyn4j.png"), libLogoWidth,
				libLogoHeight)));
		dyn4jLogo.setBounds(libHorizontalOffset, libVerticalOffset + (libLogoPadding + libLogoHeight), libLogoWidth,
				libLogoHeight);
		this.add(dyn4jLogo);

		JLabel box2dLogo = new JLabel(new ImageIcon(scaleImage(readImage("/splash/box2d.png"), libLogoWidth,
				libLogoHeight)));
		box2dLogo.setBounds(libHorizontalOffset, libVerticalOffset + (libLogoPadding + libLogoHeight) * 2,
				libLogoWidth, libLogoHeight);
		this.add(box2dLogo);

		JLabel jythonLogo = new JLabel(new ImageIcon(scaleImage(readImage("/splash/jython.png"), libLogoWidth,
				libLogoHeight)));
		jythonLogo.setBounds(libHorizontalOffset, libVerticalOffset + (libLogoPadding + libLogoHeight) * 3,
				libLogoWidth, libLogoHeight);
		this.add(jythonLogo);

		JLabel slf4jLogo = new JLabel(new ImageIcon(scaleImage(readImage("/splash/slf.png"), libLogoWidth + 2,
				libLogoHeight)));
		slf4jLogo.setBounds(libHorizontalOffset - 2, libVerticalOffset + (libLogoPadding + libLogoHeight) * 4,
				libLogoWidth + 2, libLogoHeight);
		this.add(slf4jLogo);

		char copyright = 169;
		JLabel text = new JLabel("Powered by PhageX Engine " + copyright + " 2014", JLabel.CENTER);
		text.setBounds(35, 235, 280, 50);
		this.add(text);

		this.setVisible(true);

		showInCenter(this);
		// this.setLocation(1000, 500);
	}

	/**
	 * Reads an image from a classpath String.
	 * 
	 * @param name
	 * @return
	 */
	public static BufferedImage readImage(String name) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(ImageUtils.class.getResourceAsStream(name));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return image;
	}

	/**
	 * Scales an image.
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public static Image scaleImage(Image image, double width, double height) {
		return image.getScaledInstance((int) width, (int) height, Image.SCALE_SMOOTH);
	}

	/**
	 * Shows in the center of multiple monitors.
	 * 
	 * @param frame
	 */
	public static void showInCenter(JFrame frame) {
		int halfFrameWidth = (int) (frame.getWidth() / 2f);
		int halfFrameHeight = (int) (frame.getHeight() / 2f);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gds = ge.getScreenDevices();

		float totalX = 0;
		float totalY = 0;
		for (GraphicsDevice gd : gds) {
			totalX += gd.getDisplayMode().getWidth();
			totalY += gd.getDisplayMode().getHeight();
		}

		frame.setLocation((int) (totalX / 2) - halfFrameWidth, (int) (totalY / (gds.length * 2)) - halfFrameHeight);
	}

	public static void main(String args[]) {
		new PhageSplash();
	}
}
