package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AiPlayer extends BasePlayer {

	/**
	 * 表示方向,0水平方向，1水平方向逆时针45度，2垂直方向，3垂直方向逆时针旋转45度
	 */
	final int XXXXXX = 0;
	final int XXXXXX_45 = 1;
	final int YYYYYY = 2;
	final int YYYYYY_45 = 3;

	int x_direction[] = { 0, 1, 1, -1 };
	int y_direction[] = { 1, 0, 1, 1 };

	int addcount[] = { 10, 8, 5, 3 };

	int reducecount[] = { 8, 6, 4, 2 };

	// 计算范围，太大的范围会有性能问题
	private class CalcuteRange {
		int xStart, yStart, xStop, yStop;

		private CalcuteRange(int xStart, int yStart, int xStop, int yStop) {
			this.xStart = xStart;
			this.yStart = yStart;
			this.xStop = xStop;
			this.yStop = yStop;
		}
	}

	List<Point> hunmanPoints;

	public List<Point> getHunmanPoints() {
		return hunmanPoints;
	}

	public void setHunmanPoints(List<Point> hunmanPoints) {
		this.hunmanPoints = hunmanPoints;
	}

	CalcuteRange currentRange = new CalcuteRange(6, 6, 10, 10);

	public AiPlayer(IChessboard chessboard, List<Point> hunmanPoints) {
		super(chessboard);
		this.hunmanPoints = hunmanPoints;
	}

	/**
	 * 判断点集中是否有连五，活四，双活三的情况
	 * 
	 * @param arrayList
	 * @return
	 */
	public Point isNecessaryPoints(ArrayList<Point> arrayList) {

		// ---------------------------判断电脑是否有可以构成五连的坐标
		for (Point point : arrayList) {

			if (islianwu(point, hadLocalPoints))
				return point;
		}
		// ---------------------------判断对手是否有可以构成五连的坐标
		for (Point point : arrayList) {

			if (islianwu(point, hunmanPoints))
				return point;
		}
		// ---------------------------判断电脑是否有可以构成活四的坐标，一般会有两个点，需要计算最优点
		for (Point point : arrayList) {

			if (ishuosi(point, hadLocalPoints))
				return point;
		}
		// ---------------------------判断人类是否有可以构成活四的坐标
		for (Point point : arrayList) {

			if (ishuosi(point, hunmanPoints))
				return point;
		}

		// ---------------------------判断电脑是否有可以构成双活三
		for (Point point : arrayList) {

			if (isdouble3(point, hadLocalPoints) && isdanhuosi(point, hadLocalPoints))
				return point;
		}
		// ---------------------------判断人类是否有可以构成双活三
		for (Point point : arrayList) {

			if (isdouble3(point, hunmanPoints) && isdanhuosi(point, hunmanPoints)) {
				return point;
			}
		}

		// ---------------------------判断电脑是否有可以构成双活三
		for (Point point : arrayList) {

			if (isdouble3(point, hadLocalPoints))
				return point;
		}
		// ---------------------------判断人类是否有可以构成双活三
		for (Point point : arrayList) {

			if (isdouble3(point, hunmanPoints)) {
				return point;
			}
		}
		return null;
	}

	/**
	 * 获取最有利的点（自己活的方式）
	 * 
	 * @return
	 */
	public Point getBestPointForMe() {

		// 所有需要计算权值的点
		ArrayList<Point> arrayList = getAllneedComputer();
		ArrayList<Point> max_computer_points = new ArrayList<>();
		int max = 0;
		// 判断是否有特殊情况
		for (Point point : arrayList) {
			if (islianwu(point, hadLocalPoints))
				return point;
		}
		for (Point point : arrayList) {
			if (ishuosi(point, hadLocalPoints))
				return point;
		}
		for (Point point : arrayList) {
			if (isdouble3(point, hadLocalPoints))
				return point;
		}
		for (Point point : arrayList) {
			int count = computerPointGoal(point, hadLocalPoints);
			if (max < count) {
				max_computer_points.clear();
				max_computer_points.add(point);
				max = count;
			} else if (max == count) {
				max_computer_points.add(point);
			}
		}
		return max_computer_points.get(max_computer_points.size() - 1);
	}

	/**
	 * 获取最有利的点（堵对手）
	 * 
	 * @return
	 */
	public Point getBestPointForEnemy() {

		// 所有需要计算权值的点
		ArrayList<Point> arrayList = getAllneedComputer();
		ArrayList<Point> max_Enemy_points = new ArrayList<>();
		int max = 0;
		// 判断是否有特殊情况
		for (Point point : arrayList) {
			if (islianwu(point, hunmanPoints))
				return point;
		}
		for (Point point : arrayList) {
			if (ishuosi(point, hunmanPoints))
				return point;
		}
		for (Point point : arrayList) {
			if (isdouble3(point, hunmanPoints)) {
				return point;
			}
		}
		for (Point point : arrayList) {
			int count = computerPointGoal(point, hunmanPoints);
			if (max < count) {
				max_Enemy_points.clear();
				max_Enemy_points.add(point);
			} else if (max == count) {
				max_Enemy_points.add(point);
			}
		}
		return max_Enemy_points.get(max_Enemy_points.size() - 1);
	}

	/**
	 * AI通过计算返回最有利的点，在落子时就落在该点（只考虑当前形势）
	 * 
	 * @return
	 */
	public Point getBestPoint() {

		// 计算范围内的点
		ArrayList<Point> arrayList = getAllneedComputer();

		Point necessaryPoint = isNecessaryPoints(arrayList);
		if (necessaryPoint != null) {
			return necessaryPoint;
		}

		int max_computer = 0;
		// 记录计算机有利权值最大的点
		ArrayList<Point> max_computer_points = new ArrayList<>();
		for (Point point : arrayList) {

			int computerGoal = computerPointGoal(point, hadLocalPoints);
			int humanGoal = computerPointGoal(point, hunmanPoints);
			if (computerGoal + (int) (0.7 * humanGoal) > max_computer) {
				max_computer = computerGoal + (int) (0.7 * humanGoal);
				max_computer_points.clear();
				max_computer_points.add(point);
			} else if (computerGoal + (int) (0.7 * humanGoal) == max_computer) {
				max_computer_points.add(point);
			}
		}

		Random random = new Random();
		return max_computer_points.get(random.nextInt(max_computer_points.size()));
	}

	/**
	 * 判断落子处是否构成双活三、活三单活四，特殊活三
	 * 
	 * @param point
	 * @param x
	 * @param y
	 * @param points
	 * @return
	 */
	public boolean isdouble3(Point point, List<Point> points) {

		int x;
		int y;

		int threeCount = 0;

		for (int i = 0; i < 4; i++) {
			x = x_direction[i];
			y = y_direction[i];
			if (isdengous(point, points, x, y)) {
				threeCount++;
			}
		}
		if (threeCount >= 2) {
			return true;
		}

		return false;
	}

	/**
	 * 判断某点是否存在单活四的情况
	 * 
	 * @param point
	 * @param points
	 * @return
	 */
	public boolean isdanhuosi(Point point, List<Point> points) {

		int x = 0;
		int y = 0;
		for (int ri = 0; ri < 4; ri++) {
			x = x_direction[ri];
			y = y_direction[ri];
			String winOk11 = "01111";
			String winOk12 = "01111";
			String winOk13 = "11011";
			String winOk14 = "10111";
			String winOk15 = "11101";
			char[] str = new char[9];
			str[4] = '1';
			for (int i = 1; i <= 4; i++) {
				Point nextpoint = new Point(point.x + x * i, point.y + y * i);
				if (points.contains(nextpoint)) {
					str[4 + i] = '1';
				} else if (freePoints.contains(nextpoint)) {
					str[4 + i] = '0';
				} else {
					break;
				}
			}
			for (int i = -1; i >= -4; i--) {
				Point nextpoint = new Point(point.x + x * i, point.y + y * i);
				if (points.contains(nextpoint)) {
					str[4 + i] = '1';
				} else if (freePoints.contains(nextpoint)) {
					str[4 + i] = '0';
				} else {
					break;
				}
			}
			String strs = String.valueOf(str);
			if (strs.contains(winOk12) || strs.contains(winOk11)
					||strs.contains(winOk13) || strs.contains(winOk14)
					||strs.contains(winOk15))
				return true;
		}

		return false;
	}
	
	

	// 判断活三或者是单活四
	public boolean isdengous(Point point, List<Point> points, int x, int y) {

		String winOk11 = "011100";
		String winOk12 = "001110";
		String winOk21 = "11110";
		String winOk22 = "01111";
		String winOk23 = "11011";
		String winOk24 = "10111";
		String winOk25 = "11101";
		String winOk31 = "011010";
		String winOk32 = "010110";

		char[] str = { '?', '?', '?', '?', '?', '?', '?', '?', '?' };
		str[4] = '1';
		for (int i = 1; i <= 4; i++) {
			Point nextpoint = new Point(point.x + x * i, point.y + y * i);
			if (points.contains(nextpoint)) {
				str[4 + i] = '1';
			} else if (freePoints.contains(nextpoint)) {
				str[4 + i] = '0';
			} else {
				break;
			}
		}
		for (int i = -1; i >= -4; i--) {
			Point nextpoint = new Point(point.x + x * i, point.y + y * i);
			if (points.contains(nextpoint)) {
				str[4 + i] = '1';
			} else if (freePoints.contains(nextpoint)) {
				str[4 + i] = '0';
			} else {
				break;
			}
		}
		String strs = String.valueOf(str);
		if (strs.contains(winOk11) || strs.contains(winOk12) || strs.contains(winOk21) || strs.contains(winOk22)
				|| strs.contains(winOk23) || strs.contains(winOk24) ||strs.contains(winOk25) || strs.contains(winOk31)
				|| strs.contains(winOk32)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否构成4颗并且可以活
	 * @param point
	 * @param points
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isdengous4(Point point, List<Point> points) {

		String winOk11 = "11110";
		String winOk12 = "10111";
		String winOk13 = "11110";
		String winOk14 = "11101";
		String winOk15 = "11011";

		
		for(int ri=0;ri<4;ri++){
			char[] str = { '?', '?', '?', '?', '?', '?', '?', '?', '?' };
			str[4] = '1';
			int x=x_direction[ri];
			int y=y_direction[ri];
			
			for (int i = 1; i <= 4; i++) {
				Point nextpoint = new Point(point.x + x * i, point.y + y * i);
				if (points.contains(nextpoint)) {
					str[4 + i] = '1';
				} else if (freePoints.contains(nextpoint)) {
					str[4 + i] = '0';
				} else {
					break;
				}
			}
			for (int i = -1; i >= -4; i--) {
				Point nextpoint = new Point(point.x + x * i, point.y + y * i);
				if (points.contains(nextpoint)) {
					str[4 + i] = '1';
				} else if (freePoints.contains(nextpoint)) {
					str[4 + i] = '0';
				} else {
					break;
				}
			}
			String strs = String.valueOf(str);
			if (strs.contains(winOk11) || strs.contains(winOk12) || strs.contains(winOk13) || strs.contains(winOk14)
					|| strs.contains(winOk15)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 判断落子处是否构成活四
	 * 
	 * @param point
	 * @param x
	 * @param y
	 * @param points
	 * @return
	 */
	public boolean ishuosi(Point point, List<Point> points) {
		
		String winOk1 = "011110"; 

		int x, y;
		int countall = 0;
		for (int ri = 0; ri < 4; ri++) {
			char[] str = { '?', '?', '?', '?', '?', '?', '?', '?', '?' };
			str[4]='1';
			x = x_direction[ri];
			y = y_direction[ri];

			for (int i = 1; i <= 4; i++) {
				Point point2 = new Point(point.x + x * i, point.y + y * i);
				if (points.contains(point2)) {// 有我方的子，非常有利
					str[4+i]='1';
				} else if (freePoints.contains(point2)) {
					str[4+i]='0';
					break;
				} else {
					break;
				}
			}

			for (int i = -1; i >= -4; i--) {
				Point point2 = new Point(point.x + x * i, point.y + y * i);
				if (points.contains(point2)) {// 有我方的子，非常有利
					str[4+i]='1';
				} else if (freePoints.contains(point2)) {
					str[4+i]='0';
					break;
				} else {
					break;
				}
			}
			if(String.valueOf(str).contains(winOk1))
				return true;
		}

		return false;
	}

	/**
	 * 判断落子处是否构成连五
	 * 
	 * @param point
	 * @param x
	 * @param y
	 * @param points
	 * @return
	 */
	public boolean islianwu(Point point, List<Point> points) {

		int x, y;
		for (int ri = 0; ri < 4; ri++) {
			x = x_direction[ri];
			y = y_direction[ri];

			int countall = 1;
			for (int i = 1; i <= 4; i++) {
				Point point2 = new Point(point.x + x * i, point.y + y * i);
				if (points.contains(point2)) {// 有我方的子，非常有利
					countall++;
				} else {
					break;
				}
			}

			for (int i = -1; i >= -4; i--) {
				Point point2 = new Point(point.x + x * i, point.y + y * i);
				if (points.contains(point2)) {// 有我方的子，非常有利
					countall++;
				} else {
					break;
				}
			}

			if (countall > 4) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 获取所有需要计算的点（范围在函数内会自动计算）
	 * 
	 * @return
	 */
	public ArrayList<Point> getAllneedComputer() {
		ArrayList<Point> arrayList = new ArrayList<>();

		initRange(hadLocalPoints, hunmanPoints);

		for (int i = currentRange.xStart; i <= currentRange.xStop; i++) {
			for (int ri = currentRange.yStart; ri <= currentRange.yStop; ri++) {
				Point point = new Point(i, ri);
				if (freePoints.contains(point)) {
					arrayList.add(point);
				}
			}
		}
		return arrayList;
	}

	/**
	 * 计算某点的权重
	 * 
	 * @param point
	 * @param playpoints
	 * @return
	 */
	public int computerPointGoal(Point point, List<Point> playpoints) {

		int countall = 0;
		for (int ri = 0; ri < 4; ri++) {
			int x = x_direction[ri];
			int y = y_direction[ri];

			int count = 0;

			// 1.1 如果该方向不可能构成五子，则结果为 0。
			// 1.2如果有机会构成五子，则计算形势。比如横向构成五子。
			// 如果旁边有自己的子就加权（根据离自己的距离，分别加权10,8,5,3,2,1）--累加
			// 如果旁边有对方的子，则分别减权（15,5,3,1,1）--不累减
			if (iscanSuccess(point, x, y)) {

//				if (islianwu(point, playpoints))
//					return 100000;
//				else if (ishuosi(point, playpoints))
//					return 10000;
//				else if (isdouble3(point, playpoints)) {
//					if (isdanhuosi(point, playpoints))
//						return 10000;
//					return 9000;
//				}
				Point oldPoint = null;
				for (int i = 1; i <= 4; i++) {
					Point point2 = new Point(point.x + x * i, point.y + y * i);
					if (playpoints.contains(point2)) {// 有我方的子，非常有利
						count += addcount[i - 1];
						if (oldPoint == null || playpoints.contains(oldPoint)) {
							count += 10;
							oldPoint = point2;
						}

					} else if (freePoints.contains(point2)) {// 是空闲的区域，可以接受
						count++;
					} else {// 是敌方的子，完蛋
						if (i != 4)
							count -= reducecount[i - 1];
						break;
					}
				}
				oldPoint = null;
				for (int i = -1; i >= -4; i--) {
					Point point2 = new Point(point.x + x * i, point.y + y * i);
					if (playpoints.contains(point2)) {// 有我方的子，非常有利
						count += addcount[-1 - i];
						if (oldPoint == null || playpoints.contains(oldPoint)) {
							count += 10;
							oldPoint = point2;
						}
					} else if (freePoints.contains(point2)) {// 是空闲的区域，可以接受
						count++;
					} else {// 是敌方的子，完蛋
						break;
					}
				}
			}
			countall += count;
		}

		return countall;
	}

	/**
	 * 判断是否有机会构成五子
	 * 
	 * @param point
	 * @param direction
	 * @return
	 */
	public boolean iscanSuccess(Point point, int x, int y) {
		int count = 1;
		for (int i = 1;; i++) {
			Point point2 = new Point(point.x + x * i, point.y + y * i);
			if (freePoints.contains(point2) || hadLocalPoints.contains(point2)) {
				count++;
			} else
				break;
		}
		for (int i = -1;; i--) {
			Point point2 = new Point(point.x + x * i, point.y + y * i);
			if (freePoints.contains(point2) || hadLocalPoints.contains(point2)) {
				count++;
			} else
				break;
		}
		if (count >= 5)
			return true;
		return false;
	}

	@Override
	public void run(Point want_Point) {
		want_Point = getBestPoint();
		// 更新当前玩家可以落子的位置
		freePoints.remove(want_Point);

		// 落子
		hadLocalPoints.add(want_Point);
	}

	// 限定电脑计算范围，如果整个棋盘计算，性能太差，目前是根据所有已下的棋子的边界值加RANGE_STEP值形成，目前为1
	private static final int RANGE_STEP = 1;

	/**
	 * 初始化计算范围
	 * 
	 * @param comuters
	 * @param humans
	 */
	private void initRange(List<Point> comuters, List<Point> humans) {
		if (humans.size() == 0)
			return;
		currentRange.xStart = humans.get(0).getX() - RANGE_STEP;
		currentRange.yStart = humans.get(0).getY() - RANGE_STEP;
		currentRange.xStop = humans.get(0).getX() + RANGE_STEP;
		currentRange.yStop = humans.get(0).getY() + RANGE_STEP;
		for (Point point : humans) {
			if (point.getX() - RANGE_STEP < currentRange.xStart) {
				currentRange.xStart = point.getX() - RANGE_STEP;
			} else if (point.getX() + RANGE_STEP > currentRange.xStop) {
				currentRange.xStop = point.getX() + RANGE_STEP;
			}
			if (point.getY() - RANGE_STEP < currentRange.yStart) {
				currentRange.yStart = point.getY() - RANGE_STEP;
			} else if (point.getY() + RANGE_STEP > currentRange.yStop) {
				currentRange.yStop = point.getY() + RANGE_STEP;
			}
		}
		for (Point point : comuters) {
			if (point.getX() - RANGE_STEP < currentRange.xStart) {
				currentRange.xStart = point.getX() - RANGE_STEP;
			} else if (point.getX() + RANGE_STEP > currentRange.xStop) {
				currentRange.xStop = point.getX() + RANGE_STEP;
			}
			if (point.getY() - RANGE_STEP < currentRange.yStart) {
				currentRange.yStart = point.getY() - RANGE_STEP;
			} else if (point.getY() + RANGE_STEP > currentRange.yStop) {
				currentRange.yStop = point.getY() + RANGE_STEP;
			}
		}

		if (currentRange.xStart < 1)
			currentRange.xStart = 1;
		if (currentRange.xStop > 15)
			currentRange.xStop = 15;
		if (currentRange.yStart < 1)
			currentRange.yStart = 1;
		if (currentRange.yStop > 15)
			currentRange.yStop = 15;
	}

}
