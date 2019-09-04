package util;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础玩家类，具有玩家的所有功能。
 * @author 青春进行时
 *
 */
public class BasePlayer implements Player { 
	

	/**
	 * 记录玩家已经落子的坐标
	 */
	List<Point> hadLocalPoints=new ArrayList<>();
	
	
	
	public List<Point> getHadLocalPoints() {
		return hadLocalPoints;
	}

	public void setHadLocalPoints(List<Point> hadLocalPoints) {
		this.hadLocalPoints = hadLocalPoints;
	}

	/**
	 * 记录玩家可以落子的坐标
	 */
	List<Point> freePoints;
	
	/**
	 * 玩家游戏的棋盘
	 */
	IChessboard chessboard; 
	
	

	public BasePlayer(IChessboard chessboard) {
		
		this.chessboard=chessboard;
		freePoints=chessboard.getFreePoints();
	}

	/**
	 * 游戏开始，玩家初始化
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

		// 更新当前玩家可以落子的位置
		freePoints.remove(want_Point);
		
		//落子
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
	 * 判断水平方向是否已经完成了五子连珠
	 * 
	 * @return
	 */
	public boolean isGameover_X() {
		
		if(hadLocalPoints.size()==0)
			return false;
		
		//获取玩家最后一次落子的坐标
		Point point=hadLocalPoints.get(hadLocalPoints.size()-1);
		/**
		 * 记录连珠的个数
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
	 * 判断水平方向逆时针旋转45度是否完成了五子连珠
	 * 
	 * @return
	 */
	public boolean isGameover_X45() {
		
		if(hadLocalPoints.size()==0)
			return false;

		//获取玩家最后一次落子的坐标
		Point point=hadLocalPoints.get(hadLocalPoints.size()-1);
		/**
		 * 记录连珠的个数
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
	 * 判断垂直方向是否已经完成了五子连珠
	 * 
	 * @return
	 */
	public boolean isGameover_Y() {
		
		if(hadLocalPoints.size()==0)
			return false;

		//获取玩家最后一次落子的坐标
		Point point=hadLocalPoints.get(hadLocalPoints.size()-1);
		/**
		 * 记录连珠的个数
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
	 * 判断垂直方向逆时针旋转45度是否已经完成了五子连珠
	 * 
	 * @return
	 */
	public boolean isGameover_Y45() {

		if(hadLocalPoints.size()==0)
			return false;
		
		//获取玩家最后一次落子的坐标
		Point point=hadLocalPoints.get(hadLocalPoints.size()-1);
		/**
		 * 记录连珠的个数
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
