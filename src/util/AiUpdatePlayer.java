package util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 青春进行时
 *
 */
public class AiUpdatePlayer extends AiPlayer {

	/**
	 * 预测层数
	 */
	int deeper = 2;

	/**
	 * 最多预测的节点数
	 */
	int maxPredictPoints = 5;

	/**
	 * 用来模仿对方下棋
	 */
	AiPlayer aiPlayer;

	public AiUpdatePlayer(IChessboard chessboard, List<Point> hunmanPoints) {
		super(chessboard, hunmanPoints);

		aiPlayer = new AiPlayer(chessboard, hadLocalPoints);
		aiPlayer.setHadLocalPoints(hunmanPoints);
	}

	/**
	 * 通过预测对手的下棋来返回最适合落子的点
	 * 
	 * @return
	 */
	public Point getBestPointPredict() {

		// 计算范围内的点
		ArrayList<Point> allNeedpoints = getAllneedComputer();
		Point necessaryPoint = isNecessaryPoints(allNeedpoints);
		if (necessaryPoint != null) {
			return necessaryPoint;
		}

		// 缩小计算范围
		ArrayList<Point> allNeedPredict = new ArrayList<>();
		sort(allNeedpoints, hadLocalPoints);
		for (int i = 0; i < 4; i++) {
			allNeedPredict.add(allNeedpoints.get(i));
		}
		sort(allNeedpoints, aiPlayer.hadLocalPoints);
		for (int i = 0; i < 4; i++) {
			if (!allNeedpoints.contains(allNeedpoints.get(i)))
				allNeedPredict.add(allNeedpoints.get(i));
		}

		int max = -111111;
		Point bestPoints = null;
		for (Point point : allNeedPredict) {

			int count = get_Point_predict_Goal(point, deeper);
			// 恢复状态
			freePoints.add(hadLocalPoints.remove(hadLocalPoints.size() - 1));
			freePoints.add(aiPlayer.hadLocalPoints.remove(aiPlayer.hadLocalPoints.size() - 1));
			if (max < count) {
				max = count;
				bestPoints = point;
			}

		}
//		System.out.println(max + ":(" + bestPoints.x + "," + bestPoints.y + ")");
		return bestPoints;

	}

	/**
	 * 返回某点的预测权重（多重预测）
	 * 
	 * @param point
	 * @param deepth
	 * @return
	 */
	public int get_Point_predict_Goal(Point point, int deepth) {

		runMONI(point);
		aiPlayer.run(null);
		if (deepth == 0) {
			return getGlobalform();
		} else {

			ArrayList<Point> allNeedPredictpoints = getAllneedComputer();
			if (allNeedPredictpoints.size() < 5) {
				int max = -11111111;
				for (int i = 0; i < allNeedPredictpoints.size(); i++) {

					Point nextPoint = allNeedPredictpoints.get(i);
					int count = get_Point_predict_Goal(nextPoint, deepth - 1);
					// 恢复状态
					freePoints.add(hadLocalPoints.remove(hadLocalPoints.size() - 1));
					freePoints.add(aiPlayer.hadLocalPoints.remove(aiPlayer.hadLocalPoints.size() - 1));
					if (max < count) {
						max = count;
					}
				}
				return max;

			} else {

				ArrayList<Point> points = new ArrayList<>();

				int max = -11111111;
				sort(allNeedPredictpoints, hadLocalPoints);
				for (int i = 0; i < 5; i++) {
					points.add(allNeedPredictpoints.get(i));
				}
				sort(allNeedPredictpoints, aiPlayer.hadLocalPoints);
				for (int i = 0; i < 5; i++) {
					points.add(allNeedPredictpoints.get(i));
				}

				for (int i = 0; i < points.size(); i++) {

					Point nextPoint = points.get(i);
					int count = get_Point_predict_Goal(nextPoint, deepth - 1);
					// 恢复状态
					freePoints.add(hadLocalPoints.remove(hadLocalPoints.size() - 1));
					freePoints.add(aiPlayer.hadLocalPoints.remove(aiPlayer.hadLocalPoints.size() - 1));
					if (max < count) {
						max = count;
					}
				}
				return max;

			}

		}

	}

	/**
	 * 排序(从大到小)
	 * 
	 * @param allNeedPredictpoints
	 */
	public void sort(ArrayList<Point> allNeedPredictpoints, List<Point> points) {

		int counts[] = new int[allNeedPredictpoints.size()];
		for (int i = 0; i < counts.length; i++) {
			Point point = allNeedPredictpoints.get(i);
			counts[i] = computerPointGoal(point, points);
		}

		for (int i = 1; i < counts.length; i++) {
			for (int ri = i; ri >= 1; ri--) {
				if (counts[ri] > counts[ri - 1]) {// 交换

					int x = counts[ri];
					counts[ri] = counts[ri - 1];
					counts[ri - 1] = x;

					Point point = allNeedPredictpoints.get(ri);
					allNeedPredictpoints.set(ri, allNeedPredictpoints.get(ri - 1));
					allNeedPredictpoints.set(ri - 1, point);

				} else
					break;
			}
		}
	}

	/**
	 * 返回全局的形势,前提下一步该我走
	 * 
	 * @return
	 */
	public int getGlobalform() {

		// 判断是否已经赢了
		
		if (islianwu(aiPlayer.hadLocalPoints.get(aiPlayer.hadLocalPoints.size() - 1), aiPlayer.hadLocalPoints)
				|| islianwu(aiPlayer.hadLocalPoints.get(aiPlayer.hadLocalPoints.size() - 2), aiPlayer.hadLocalPoints)) {
//			System.out.println("局势已经输了！");
			return -100001;// 局势已经输了！
		}else if (islianwu(hadLocalPoints.get(hadLocalPoints.size() - 1), hadLocalPoints)
				|| islianwu(hadLocalPoints.get(hadLocalPoints.size() - 2), hadLocalPoints)) {

			return 100001;// 局势已经赢了！
		}

		if (isdengous4(hadLocalPoints.get(hadLocalPoints.size() - 1), hadLocalPoints)) {
			return 10002;// 已经非常接近赢了
		}else if (ishuosi(aiPlayer.hadLocalPoints.get(aiPlayer.hadLocalPoints.size() - 1), aiPlayer.hadLocalPoints)) {
			return -10002;// 已经非常接近输入

		}

		if (isdouble3(hadLocalPoints.get(hadLocalPoints.size() - 1), hadLocalPoints)
				&& isdanhuosi(hadLocalPoints.get(hadLocalPoints.size() - 1), hadLocalPoints)) {
//			System.out.println(" 已经相当接近赢了");
			return 10001;// 已经相当接近赢了
		} else if (isdouble3(aiPlayer.hadLocalPoints.get(aiPlayer.hadLocalPoints.size() - 1), aiPlayer.hadLocalPoints)
				&& isdanhuosi(aiPlayer.hadLocalPoints.get(hadLocalPoints.size() - 1), aiPlayer.hadLocalPoints)) {
//			System.out.println(" 已经相当接近输了");
			return -10001;// 已经相当接近输入
		}

		if (isdouble3(hadLocalPoints.get(hadLocalPoints.size() - 1), hadLocalPoints)) {
			return 9001;// 已经接近赢了
		}else if (isdouble3(aiPlayer.hadLocalPoints.get(aiPlayer.hadLocalPoints.size() - 1), aiPlayer.hadLocalPoints)) {
			return -9001;// 已经接近输入
		}

		Point pointme = getBestPointForMe();
		Point pointenemy = getBestPointForEnemy();

		int countme = computerPointGoal(pointme, hadLocalPoints);
		int countEnemy = computerPointGoal(pointenemy, aiPlayer.hadLocalPoints);
		if(countme>countEnemy)
			return countme;
		return countEnemy;
//		return countme - (int) (0.8 * countEnemy);

	}

	/**
	 * 模拟下棋
	 * 
	 * @param want_Point
	 */
	public void runMONI(Point want_Point) {
		hadLocalPoints.add(want_Point);
		freePoints.remove(want_Point);
	}

	@Override
	public void run(Point want_Point) {
		Point point = getBestPointPredict();
		hadLocalPoints.add(point);
		freePoints.remove(point);
	}

}
