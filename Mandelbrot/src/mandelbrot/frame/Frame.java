package mandelbrot.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import mandelbrot.utils.ImaginaryNum;

/**
 * @author Fabian
 */

@SuppressWarnings("serial")
public class Frame extends JPanel implements ActionListener {
	JFrame frame;

	public int ITERATIONS = 200;
	final float hueOffset = 0.65f;

	public double zoom = 1;
	public double posX = 0;
	public double posY = 0;

	BufferedImage img;

	public Frame() {
		frame = new JFrame("Mandelbrot");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(630, 650);
		frame.setVisible(true);
		frame.add(this);

		init();
	}

	Timer timer;

	private void init() {
		img = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);

		calculateMandelbrot();

		frame.addKeyListener(new Listener());

		timer = new Timer(1000 / 60, this);
		timer.start();
	}

	public void calculateMandelbrot() {
		for (int x = 0; x < 600; x++) {
			for (int y = 0; y < 600; y++) {
				ImaginaryNum num = new ImaginaryNum(((x + (posX - 100) * zoom - 300) / zoom) / 200d,
						((y + posY * zoom - 300) / zoom) / 200d);
				ImaginaryNum num2 = num.clone();
				int i = 0;
				for (; i < ITERATIONS; i++) {
					num2.multi(num2.clone()).add(num);
					if (num2.r * num2.r + num2.j * num2.j > 4) {
						break;
					}
				}
				if (i == ITERATIONS) {
					img.setRGB(x, y, Color.BLACK.getRGB());
				} else {
					img.setRGB(x, y, Color.HSBtoRGB((((float) i / ITERATIONS) + hueOffset) % 1, 0.9f, 1f));
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		g2D.drawImage(img, 10, 10, null);

		g2D.setColor(Color.BLACK);

		g2D.drawRect(10, 10, 600, 600);
		g2D.setColor(Color.WHITE);
		g2D.drawLine(10 + 280, 10 + 300, 10 + 320, 10 + 300);
		g2D.drawLine(10 + 300, 10 + 280, 10 + 300, 10 + 320);

		g2D.setColor(Color.WHITE);
		g2D.setFont(new Font("Courier", Font.BOLD, 20));
		g2D.drawString("Interations (Alt): " + ITERATIONS, 15, 30);
		g2D.drawString("Zoom (Ctrl): " + zoom, 15, 50);
		g2D.setColor(Color.BLACK);
		g2D.setFont(new Font("Courier", Font.BOLD, 20));
		g2D.drawString("Interations (Alt): " + ITERATIONS, 16, 31);
		g2D.drawString("Zoom (Ctrl): " + zoom, 16, 51);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

	private class Listener extends KeyAdapter {

		private boolean press = false;

		public void keyPressed(KeyEvent e) {

			if (press == true) {
				return;
			}

			if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (e.isControlDown()) {
					zoom += zoom * 0.5;
				} else {
					if (e.isShiftDown()) {
						posY -= 100 / zoom;
					} else if (e.isAltDown()) {
						if (ITERATIONS < 10) {
							ITERATIONS += 1;
						} else if (ITERATIONS >= 200) {
							ITERATIONS += 50;
						} else {
							ITERATIONS += 10;
						}
					} else {
						posY -= 20 / zoom;
					}
				}
				press = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (e.isControlDown()) {
					zoom -= zoom / 3;
				} else {
					if (e.isShiftDown()) {
						posY += 100 / zoom;
					} else if (e.isAltDown()) {
						if (ITERATIONS <= 10) {
							if (ITERATIONS > 1) {
								ITERATIONS -= 1;
							}
						} else if (ITERATIONS > 200) {
							ITERATIONS -= 50;
						} else {
							ITERATIONS -= 10;
						}
					} else {
						posY += 20 / zoom;
					}
				}
				press = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (e.isShiftDown()) {
					posX -= 100 / zoom;
				} else {
					posX -= 20 / zoom;
				}
				press = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (e.isShiftDown()) {
					posX += 100 / zoom;
				} else {
					posX += 20 / zoom;
				}
				press = true;
			}
			calculateMandelbrot();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			press = false;
		}
	}
}
