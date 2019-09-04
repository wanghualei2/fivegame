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
	
	GamePanel gamePanel;//���������ˢ��
	

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	int humanPlayerId;// �������ID

	BasePlayer humanPlayer = null;// �������
	ArrayList<Point> human_points;
	BasePlayer computerPlayer = null;// �������
	ArrayList<Point> computer_points;

	/**
	 * �жϵ�ǰ����Ƿ�Ӧ������
	 */
	boolean isUseful = true;

	int paddingX;
	int paddingY;

	/**
	 * ��Ӧ��ɫ���ӣ�����
	 */
	BasePlayer basePlayerA;// �����Ӧ������������

	/**
	 * ��Ӧ��ɫ����
	 */
	BasePlayer basePlayerB;

	/**
	 * ���Ŀ��
	 */
	int qige;

	int weidu = 15;

	BufferedImage image = new BufferedImage(934, 673, BufferedImage.TYPE_INT_RGB);
	Graphics graphics = image.getGraphics();
	private Image offScreenImage; // ͼ�λ���

	/**
	 * ���̱���
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
			offScreenImage = this.createImage(width, height); // �½�һ��ͼ�񻺴�ռ�,����ͼ���СΪ800*600
		Graphics gImage = offScreenImage.getGraphics(); // �����Ļ����ù���,��gImage������
		paint(gImage); // ��Ҫ���Ķ�������ͼ�񻺴�ռ�ȥ
		g.drawImage(offScreenImage, 0, 0, null); // Ȼ��һ������ʾ����
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		int clickX = e.getX();
		int clickY = e.getY();

		int xx = (clickX - paddingX + qige / 2) / qige + 1;
		int yy = (clickY - paddingY + qige / 2) / qige + 1;

		if (humanPlayer == null) {
			// ��ǰ��ϷΪ�����������ң�����¼�ʧЧ
			System.out.println("ȱ�����");
		} else if (isUseful) {
			isUseful=false;

			if (!human_points.contains(new Point(xx, yy))) {
				humanPlayer.run(new Point(xx, yy));
				repaint();

				if (humanPlayer.hasWin()) {
					JOptionPane.showMessageDialog(this, "��Ϸ��������Ӯ�ˣ�");
					
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
							JOptionPane.showMessageDialog(getParent(), "��Ϸ�����������ˣ�");
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
	 * ��Ϸ�����������Ϸ���ݣ�ˢ�½���
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
	 * ������Ϸ��ң�
	 * 
	 * @param basePlayerA
	 *            �����Ӧ���
	 * @param basePlayerB
	 *            �\���Ӧ���
	 * @param humanID
	 *            ������ҵ�ID������������ǰ��廹���\�壬Ҳ���ܴ��ڶ��ǵ������ ע����������
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
