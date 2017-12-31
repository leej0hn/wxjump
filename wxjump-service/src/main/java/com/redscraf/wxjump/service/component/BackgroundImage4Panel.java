package com.redscraf.wxjump.service.component;


import com.google.common.base.Throwables;
import com.redscraf.wxjump.common.contants.Constants;
import com.redscraf.wxjump.service.adb.AdbCaller;
import com.redscraf.wxjump.service.config.WxJumpConfig;
import com.redscraf.wxjump.service.finder.ColorFilterFinder;
import com.redscraf.wxjump.service.finder.EndCenterFinder;
import com.redscraf.wxjump.service.finder.StartCenterFinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * <p>function:
 * <p>User:
 * <p>Date: 16/7/8
 * <p>Version: 1.0
 */
@Slf4j
public class BackgroundImage4Panel extends JFrame {
	private static final long serialVersionUID = 1L;
	private static boolean isFirst = true;
	private static Point firstPoint;
	private static Point secondPoint;

	@Autowired
	private static WxJumpConfig wxJumpConfig = SpringContextHolder.getBean("wxJumpConfig");
	@Autowired
	private static AdbCaller adbCaller = SpringContextHolder.getBean("adbCaller");

	public BackgroundImage4Panel() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public static void start() {
		if (wxJumpConfig.getModel() == Constants.MODE_MANUAL || wxJumpConfig.getModel() == Constants.MODE_SEMI_AUTO) {
			manualMode(wxJumpConfig.getScreenWidth(), wxJumpConfig.getScreenHeight(), wxJumpConfig.getDistancePressTimeRatio(), wxJumpConfig.getScreenshotInterval(),wxJumpConfig.getScreenshotLocation());
		} else if (wxJumpConfig.getModel() == Constants.MODE_AUTO) {
			autoJumpMode(wxJumpConfig.getDistancePressTimeRatio(),  wxJumpConfig.getScreenshotInterval(), wxJumpConfig.getScreenshotLocation());
		}

	}

	private static void manualMode(final int resizedScreenWidth, final int resizedScreenHeight,
			final double resizedDistancePressTimeRatio, final int screenshotInterval, final String screenshotPath) {

		adbCaller.printScreen();
		final BackgroundImage4Panel backgroundImage4Panel = new BackgroundImage4Panel();
		backgroundImage4Panel.setSize(resizedScreenWidth, resizedScreenHeight);
		backgroundImage4Panel.setVisible(true);

		JPanel jPanel = new JPanel() {
			/**
			 * serialVersionId
			 */
			private static final long serialVersionUID = -1183754274585001429L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				try {
					BufferedImage bufferedImage = ImageIO.read(new File(screenshotPath));
					BufferedImage newImage = new BufferedImage(resizedScreenWidth, resizedScreenHeight,
							bufferedImage.getType());
					if (wxJumpConfig.getModel() == Constants.MODE_SEMI_AUTO) {
						firstPoint = StartCenterFinder.findStartCenter(bufferedImage);
						firstPoint.setLocation(firstPoint.getX() * resizedScreenWidth / bufferedImage.getWidth(),
								firstPoint.getY() * resizedScreenWidth / bufferedImage.getWidth());
						log.info("当前位置坐标 = [x=" + firstPoint.x + ",y=" + firstPoint.y + "]");
						isFirst = false;
					}
					/**
					 * try to resize
					 */
					Graphics gTemp = newImage.getGraphics();
					gTemp.drawImage(bufferedImage, 0, 0, resizedScreenWidth, resizedScreenHeight, null);
					gTemp.dispose();
					bufferedImage = newImage;
					g.drawImage(bufferedImage, 0, 0, null);
				} catch (IOException e) {
					log.error("IOException: Please check " + screenshotPath);
					log.error(Throwables.getStackTraceAsString(e));
				}
			}
		};
		backgroundImage4Panel.getContentPane().add(jPanel);

		backgroundImage4Panel.getContentPane().getComponent(0).addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JPanel jp = ((JPanel) backgroundImage4Panel.getContentPane().getComponent(0));
				if (isFirst) {
					log.info("当前位置坐标 " + e.getX() + " " + e.getY());
					firstPoint = e.getPoint();
					isFirst = false;
				} else {
					secondPoint = e.getPoint();
					int distance = distance(firstPoint, secondPoint);
					log.info("距离:" + distance);
					isFirst = true;
					adbCaller.longPress(distance * resizedDistancePressTimeRatio);// magic
					// number
					try {
						Thread.sleep(screenshotInterval);// wait for screencap
					} catch (InterruptedException e1) {
						log.error(Throwables.getStackTraceAsString(e1));
					}
					adbCaller.printScreen();
					jp.validate();
					jp.repaint();
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});
	}

	private static void autoJumpMode(final double resizedDistancePressTimeRatio, final int screenshotInterval,
			final String screenshotPath) {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					adbCaller.printScreen();
					try {
						BufferedImage bufferedImage = ImageIO.read(new File(screenshotPath));
						firstPoint = StartCenterFinder.findStartCenter(bufferedImage);
						secondPoint = EndCenterFinder.findEndCenter(bufferedImage, firstPoint);
						int distance = secondPoint == null ? 0 : distance(firstPoint, secondPoint);
						if (secondPoint == null || secondPoint.getX() == 0 || distance < 75 ||
						// true || //放开可改为全部用ColorFilterFinder来做下一个中心点的查找
						Math.abs(secondPoint.getX() - firstPoint.getX()) < 38) {
							secondPoint = ColorFilterFinder.findEndCenter(bufferedImage, firstPoint);
							if (secondPoint == null) {
								adbCaller.printScreen();
								continue;
							}
						} else {
							Point colorfilterCenter = ColorFilterFinder.findEndCenter(bufferedImage, firstPoint);
							if (Math.abs(secondPoint.getX() - colorfilterCenter.getX()) > 10) {
								secondPoint = colorfilterCenter;
							}
						}
						log.info("当前位置坐标 = [x=" + firstPoint.x + ",y=" + firstPoint.y
								+ "] , 下个位置坐标 = [x=" + secondPoint.x + ",y=" + secondPoint.y + "]");
						distance = distance(firstPoint, secondPoint);
						adbCaller.longPress(distance * resizedDistancePressTimeRatio);// magic
																						// number
						try {
							Thread.sleep(screenshotInterval);// wait for
																// screencap
						} catch (InterruptedException e1) {
							log.error(Throwables.getStackTraceAsString(e1));
						}
						adbCaller.printScreen();
					} catch (IOException e1) {
						log.error("IOException: Please check " + screenshotPath);
						log.error(Throwables.getStackTraceAsString(e1));
					}
				}
			}
		}.start();
	}

	/**
	 * 对图片进行强制放大或缩小
	 *
	 * @param originalImage
	 *            原始图片
	 * @return
	 */
	public static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height) {
		BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
		Graphics g = newImage.getGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return newImage;
	}

	/**
	 * 求两点距离
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int distance(Point a, Point b) {
		return (int) Math.sqrt((a.x - b.getX()) * (a.x - b.getX()) + (a.y - b.getY()) * (a.y - b.getY()));
	}

}
