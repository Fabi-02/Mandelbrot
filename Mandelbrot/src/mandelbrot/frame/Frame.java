package mandelbrot.frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

	final int ITERATIONS = 200;
	final float hueOffset = 0.65f;

	public double zoom = 1;
	public double posX = 0;
	public double posY = 0;

	double[][] field;

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
		field = new double[600][];
		for (int x = 0; x < 600; x++) {
			field[x] = new double[600];
			for (int y = 0; y < 600; y++) {
				field[x][y] = 0;
			}
		}

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
				field[x][y] = i;
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		for (int x = 0; x < 600; x++) {
			for (int y = 0; y < 600; y++) {
				double dis = field[x][y];
				if (dis == ITERATIONS) {
					g2D.setColor(new Color(0x000));
				} else {
					g2D.setColor(new Color(Color.HSBtoRGB((((float) dis / ITERATIONS) + hueOffset) % 1, 1f, 1f)));
				}
				g2D.drawLine(10 + x, 10 + y, 10 + x, 10 + y);
			}
		}

		g2D.setColor(Color.BLACK);

		g2D.drawRect(10, 10, 600, 600);
		g2D.setColor(Color.WHITE);
		g2D.drawLine(10 + 280, 10 + 300, 10 + 320, 10 + 300);
		g2D.drawLine(10 + 300, 10 + 280, 10 + 300, 10 + 320);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

	private class Listener extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (e.isControlDown()) {
					zoom += zoom * 0.5;
				} else {
					if (e.isShiftDown()) {
						posY -= 100 / zoom;
					} else {
						posY -= 20 / zoom;
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (e.isControlDown()) {
					zoom -= zoom * 0.5;
				} else {
					if (e.isShiftDown()) {
						posY += 100 / zoom;
					} else {
						posY += 20 / zoom;
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (e.isShiftDown()) {
					posX -= 100 / zoom;
				} else {
					posX -= 20 / zoom;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (e.isShiftDown()) {
					posX += 100 / zoom;
				} else {
					posX += 20 / zoom;
				}
			}
			calculateMandelbrot();
		}
	}
}
