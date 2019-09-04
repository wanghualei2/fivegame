package util;

import java.util.ArrayList;
import java.util.List;

/**
 * ��������࣬������ҵ����й��ܡ�
 * @author �ഺ����ʱ
 *
 */
public class BasePlayer implements Player { 
	

	/**
	 * ��¼����Ѿ����ӵ�����
	 */
	List<Point> hadLocalPoints=new ArrayList<>();
	
	
	
	public List<Point> getHadLocalPoints() {
		return hadLocalPoints;
	}

	public void setHadLocalPoints(List<Point> hadLocalPoints) {
		this.hadLocalPoints = hadLocalPoints;
	}

	/**
	 * ��¼��ҿ������ӵ�����
	 */
	List<Point> freePoints;
	
	/**
	 * �����Ϸ������
	 */
	IChessboard chessboard; 
	
	

	public BasePlayer(IChessboard chessboard) {
		
		this.chessboard=chessboard;
		freePoints=chessboard.getFreePoints();
	}

	/**
	 * ��Ϸ��ʼ����ҳ�ʼ��
	 */
	public void init(){
		
		hadLocalPoints.clear(); 
	}
	
	
	@Override
	public boolean hasWin() {
		
		if(isGameover_X()||isGameover_X45()||isGameover_Y()||isGameover_Y45())
			return true;
		else 
			return false;
	}

	@Override
	public void run( Point want_Point) {

		// ���µ�ǰ��ҿ������ӵ�λ��
		freePoints.remove(want_Point);
		
		//����
		hadLocalPoints.add(want_Point);
	}

	@Override
	public void setChessboard(IChessboard chessboard) {
		this.chessboard=chessboard;
	}

	@Override
	public List<Point> getMyPoints() {
		return hadLocalPoints;
	}
	
	/**
	 * �ж�ˮƽ�����Ƿ��Ѿ��������������
	 * 
	 * @return
	 */
	public boolean isGameover_X() {
		
		if(hadLocalPoints.size()==0)
			return false;
		
		//��ȡ������һ�����ӵ�����
		Point point=hadLocalPoints.get(hadLocalPoints.size()-1);
		/**
		 * ��¼����ĸ���
		 */
		int count = 1;

		for (int i = 1;; i++) {
			if (count == 5)
				break;
			if (hadLocalPoints.contains(new Point(point.getX() + i , point.getY())))
				count++;
			else {
				break;
			}

		}
		for (int i = 1;; i++) {
			if (count == 5)
				break;
			if (hadLocalPoints.contains(new Point(point.getX() - i, point.getY())))
				count++;
			else {
				break;
			}

		}

		if (count >= 5) {
			return true;
		}

		return false;
	}

	/**
	 * �ж�ˮƽ������ʱ����ת45���Ƿ��������������
	 * 
	 * @return
	 */
	public boolean isGameover_X45() {
		
		if(hadLocalPoints.size()==0)
			return false;

		//��ȡ������һ�����ӵ�����
		Point point=hadLocalPoints.get(hadLocalPoints.size()-1);
		/**
		 * ��¼����ĸ���
		 */
		int count = 1;

		for (int i = 1;; i++) {
			if (count == 5)
				break;
			if (hadLocalPoints.contains(new Point(point.getX() + i , point.getY() - i)))
				count++;
			else {
				break;
			}

		}
		for (int i = 1;; i++) {
			if (count == 5)
				break;
			if (hadLocalPoints.contains(new Point(point.getX() - i,  point.getY() + i )))
				count++;
			else {
				break;
			}

		}

		if (count >= 5) {
			return true;
		}

		return false;
	}

	/**
	 * �жϴ�ֱ�����Ƿ��Ѿ��������������
	 * 
	 * @return
	 */
	public boolean isGameover_Y() {
		
		if(hadLocalPoints.size()==0)
			return false;

		//��ȡ������һ�����ӵ�����
		Point point=hadLocalPoints.get(hadLocalPoints.size()-1);
		/**
		 * ��¼����ĸ���
		 */
		int count = 1;

		for (int i = 1;; i++) {
			if (count == 5)
				break;
			if (hadLocalPoints.contains(new Point(point.getX(),  point.getY() - i )))
				count++;
			else {
				break;
			}

		}
		for (int i = 1;; i++) {
			if (count == 5)
				break;
			if (hadLocalPoints.contains(new Point(point.getX(), point.getY() + i)))
				count++;
			else {
				break;
			}

		}

		if (count >= 5) {
			return true;
		}

		return false;
	}

	/**
	 * �жϴ�ֱ������ʱ����ת45���Ƿ��Ѿ��������������
	 * 
	 * @return
	 */
	public boolean isGameover_Y45() {

		if(hadLocalPoints.size()==0)
			return false;
		
		//��ȡ������һ�����ӵ�����
		Point point=hadLocalPoints.get(hadLocalPoints.size()-1);
		/**
		 * ��¼����ĸ���
		 */
		int count = 1;

		for (int i = 1;; i++) {
			if (count == 5)
				break;
			if (hadLocalPoints.contains(new Point(point.getX() + i , point.getY() + i )))
				count++;
			else {
				break;
			}

		}
		for (int i = 1;; i++) {
			if (count == 5)
				break;
			if (hadLocalPoints.contains(new Point(point.getX() - i,point.getY() - i)))
				count++;
			else {
				break;
			}

		}

		if (count >= 5) {
			return true;
		}

		return false;
	}

	public IChessboard getChessboard() {
		return chessboard;
	}

	
}
