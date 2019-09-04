package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JRadioButton;

import util.AiPlayer;
import util.BasePlayer;
import util.IChessboard;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class GamePanel extends JFrame {

	public static ChessboardCanvas game_canvas;

	JRadioButton game_mode_1;
	JButton startGame;
	JRadioButton game_mode_2;
	JComboBox computerindex;
	IChessboard chessboard = new IChessboard();

	public GamePanel() {

		setTitle("AI�����壡");
		setBounds(400, 50, 585, 650);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBackground(Color.ORANGE);
		getContentPane().add(panel, BorderLayout.NORTH);

		game_mode_1 = new JRadioButton("�˻���ս");
		game_mode_2 = new JRadioButton("������ս");
		String who_first[] = { "��������", "����" };
		computerindex = new JComboBox(who_first);
		computerindex.setBackground(Color.ORANGE);
		computerindex.setForeground(Color.BLACK);
		game_mode_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				game_mode_1.setSelected(true);
				game_mode_2.setSelected(false);
				computerindex.setVisible(true);
			}
		});
		game_mode_1.setBackground(Color.ORANGE);
		game_mode_1.setSelected(true);
		panel.add(game_mode_1);

		game_mode_2.setBackground(Color.ORANGE);
		game_mode_2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				game_mode_2.setSelected(true);
				game_mode_1.setSelected(false);
				computerindex.setVisible(false);

			}
		});

		panel.add(computerindex);
		panel.add(game_mode_2);

		startGame = new JButton("��ʼ��Ϸ");
		startGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame.setText("��Ϸ��");
				startGame.setEnabled(false);
				game_mode_1.setEnabled(false);
				game_mode_2.setEnabled(false);
				computerindex.setEnabled(false);
				if (game_mode_1.isSelected()) {// ���ѡ�����������ս

					int index = computerindex.getSelectedIndex();
					playWithComputer(index);
				} 
			}
		});
		startGame.setBackground(Color.GREEN);
		startGame.setForeground(Color.BLACK);
		panel.add(startGame);

		game_canvas = new ChessboardCanvas();
		game_canvas.setForeground(Color.GREEN);
		getContentPane().add(game_canvas, BorderLayout.CENTER);
		game_canvas.setGamePanel(this);
	}
	
	public void reStart(){
		startGame.setText("��ʼ��Ϸ");
		startGame.setEnabled(true);
		game_mode_1.setEnabled(true);
		game_mode_2.setEnabled(true);
		computerindex.setEnabled(true);
	}

	/**
	 * �������Խ�����Ϸ
	 * 
	 * @param computerIndex �ж���������˭����
	 */
	public void playWithComputer(int computerIndex) {

		BasePlayer basePlayer = new BasePlayer(chessboard);

		AiPlayer aiPlayer = new AiPlayer(chessboard, null);
		aiPlayer.setHunmanPoints(basePlayer.getMyPoints());

		if (computerIndex == 0) {// ����������
			aiPlayer.run(null);
			game_canvas.repaint();
			game_canvas.setPlayers(aiPlayer, basePlayer,2);
		}else{
			game_canvas.setPlayers( basePlayer,aiPlayer,1);
		} 

	}

	public void setPlayers(BasePlayer basePlayerA, BasePlayer basePlayerB,int id) {

		game_canvas.setPlayers(basePlayerA, basePlayerB,id);
	}

}
