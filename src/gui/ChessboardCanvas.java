package gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import util.BasePlayer;
import util.Point;

public class ChessboardCanvas extends Canvas implements MouseListener {

	int width = 0;
	int height = 0;
	
	GamePanel gamePanel;//用来界面的刷新
	

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	int humanPlayerId;// 人类玩家ID

	BasePlayer humanPlayer = null;// 人类玩家
	ArrayList<Point> human_points;
	BasePlayer computerPlayer = null;// 人类玩家
	ArrayList<Point> computer_points;

	/**
	 * 判断当前点击是否应该落子
	 */
	boolean isUseful = true;

	int paddingX;
	int paddingY;

	/**
	 * 对应白色棋子，白先
	 */
	BasePlayer basePlayerA;// 白棋对应的是人类下棋

	/**
	 * 对应黑色棋子
	 */
	BasePlayer basePlayerB;

	/**
	 * 棋格的宽度
	 */
	int qige;

	int weidu = 15;

	BufferedImage image = new BufferedImage(934, 673, BufferedImage.TYPE_INT_RGB);
	Graphics graphics = image.getGraphics();
	private Image offScreenImage; // 图形缓存

	/**
	 * 棋盘背景
	 */
	private Image chessboardImage;

	private Image white_chess_image;
	private Image black_chess_image;

	ArrayList<Point> white_points;
	ArrayList<Point> black_points;

	@Override
	public void paint(Graphics g) {

		if (width == 0) {
			width = getWidth();
			height = getHeight();

			addMouseListener(this);

			paddingX = 22;
			paddingY = 22;
			qige = (int) ((width - 17 - paddingX) / 14.0);

			try {
				white_chess_image = ImageIO.read(new File("file\\while_chessman_image.png"));
				black_chess_image = ImageIO.read(new File("file\\black_chessman_image.png"));
				chessboardImage = ImageIO.read(new File("file\\qp.jpg"));

				graphics.drawImage(chessboardImage, 0, 0, height, height, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(image, 0, 0, null);
	}

	@Override
	public void update(Graphics g) {

		if (black_points != null)
			for (Point point : black_points) {
				int xx = point.x;
				int yy = point.y;
				graphics.drawImage(black_chess_image, (int) ((xx - 1) * qige + paddingX - qige * 0.24),
						(int) ((yy - 1) * qige + paddingY - qige * 0.24), (int) (qige * 0.8), (int) (qige * 0.8), null);
			}

		if (white_points != null)
			for (Point point : white_points) {
				int xx = point.x;
				int yy = point.y;
				graphics.drawImage(white_chess_image, (int) ((xx - 1) * qige + paddingX - qige * 0.24),
						(int) ((yy - 1) * qige + paddingY - qige * 0.24), (int) (qige * 0.8), (int) (qige * 0.8), null);
			}

		if (offScreenImage == null)
			offScreenImage = this.createImage(width, height); // 新建一个图像缓存空间,这里图像大小为800*600
		Graphics gImage = offScreenImage.getGraphics(); // 把它的画笔拿过来,给gImage保存着
		paint(gImage); // 将要画的东西画到图像缓存空间去
		g.drawImage(offScreenImage, 0, 0, null); // 然后一次性显示出来
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		int clickX = e.getX();
		int clickY = e.getY();

		int xx = (clickX - paddingX + qige / 2) / qige + 1;
		int yy = (clickY - paddingY + qige / 2) / qige + 1;

		if (humanPlayer == null) {
			// 当前游戏为电脑与电脑玩家，点击事件失效
			System.out.println("缺少玩家");
		} else if (isUseful) {
			isUseful=false;

			if (!human_points.contains(new Point(xx, yy))) {
				humanPlayer.run(new Point(xx, yy));
				repaint();

				if (humanPlayer.hasWin()) {
					JOptionPane.showMessageDialog(this, "游戏结束，你赢了！");
					
					clear();
					gamePanel.reStart();
					return;
				}

				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							Thread.sleep(5);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						computerPlayer.run(null);
						isUseful=true;
						
						if (computerPlayer.hasWin()) {
							JOptionPane.showMessageDialog(getParent(), "游戏结束，你输了！");
							gamePanel.reStart();
							clear();
						}
						repaint();
					}
				}).start();
			}
		} 
	}

	/**
	 * 游戏结束，清空游戏数据，刷新界面
	 */
	public void clear() {
		isUseful = true;
		graphics.drawImage(chessboardImage, 0, 0, height, height, null);
		basePlayerA.getMyPoints().clear();
		basePlayerB.getMyPoints().clear();
		basePlayerA.getChessboard().init();
		humanPlayer=null;
		human_points=null;
		computer_points=null;
		computerPlayer=null;
		repaint();

	}

	/**
	 * 设置游戏玩家，
	 * 
	 * @param basePlayerA
	 *            白棋对应玩家
	 * @param basePlayerB
	 *            黒棋对应玩家
	 * @param humanID
	 *            人类玩家的ID，即人类玩家是白棋还是黒棋，也可能存在都是电脑玩家 注：白棋先走
	 */
	public void setPlayers(BasePlayer basePlayerA, BasePlayer basePlayerB, int humanID) {

		this.basePlayerA = basePlayerA;
		this.basePlayerB = basePlayerB;
		this.white_points = (ArrayList<Point>) this.basePlayerA.getMyPoints();
		this.black_points = (ArrayList<Point>) this.basePlayerB.getMyPoints();

		if (humanID == 1) {
			humanPlayer = basePlayerA;
			computerPlayer = basePlayerB;
			human_points=white_points;
			computer_points = black_points;
		} else if (humanID == 2) {
			humanPlayer = basePlayerB;
			human_points = black_points;
			computerPlayer = basePlayerA;
			computer_points = white_points;
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
}
