package util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author �ഺ����ʱ
 *
 */
public class AiUpdatePlayer extends AiPlayer {

	/**
	 * Ԥ�����
	 */
	int deeper = 2;

	/**
	 * ���Ԥ��Ľڵ���
	 */
	int maxPredictPoints = 5;

	/**
	 * ����ģ�¶Է�����
	 */
	AiPlayer aiPlayer;

	public AiUpdatePlayer(IChessboard chessboard, List<Point> hunmanPoints) {
		super(chessboard, hunmanPoints);

		aiPlayer = new AiPlayer(chessboard, hadLocalPoints);
		aiPlayer.setHadLocalPoints(hunmanPoints);
	}

	/**
	 * ͨ��Ԥ����ֵ��������������ʺ����ӵĵ�
	 * 
	 * @return
	 */
	public Point getBestPointPredict() {

		// ���㷶Χ�ڵĵ�
		ArrayList<Point> allNeedpoints = getAllneedComputer();
		Point necessaryPoint = isNecessaryPoints(allNeedpoints);
		if (necessaryPoint != null) {
			return necessaryPoint;
		}

		// ��С���㷶Χ
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
			// �ָ�״̬
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
	 * ����ĳ���Ԥ��Ȩ�أ�����Ԥ�⣩
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
					// �ָ�״̬
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
					// �ָ�״̬
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
	 * ����(�Ӵ�С)
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
				if (counts[ri] > counts[ri - 1]) {// ����

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
	 * ����ȫ�ֵ�����,ǰ����һ��������
	 * 
	 * @return
	 */
	public int getGlobalform() {

		// �ж��Ƿ��Ѿ�Ӯ��
		
		if (islianwu(aiPlayer.hadLocalPoints.get(aiPlayer.hadLocalPoints.size() - 1), aiPlayer.hadLocalPoints)
				|| islianwu(aiPlayer.hadLocalPoints.get(aiPlayer.hadLocalPoints.size() - 2), aiPlayer.hadLocalPoints)) {
//			System.out.println("�����Ѿ����ˣ�");
			return -100001;// �����Ѿ����ˣ�
		}else if (islianwu(hadLocalPoints.get(hadLocalPoints.size() - 1), hadLocalPoints)
				|| islianwu(hadLocalPoints.get(hadLocalPoints.size() - 2), hadLocalPoints)) {

			return 100001;// �����Ѿ�Ӯ�ˣ�
		}

		if (isdengous4(hadLocalPoints.get(hadLocalPoints.size() - 1), hadLocalPoints)) {
			return 10002;// �Ѿ��ǳ��ӽ�Ӯ��
		}else if (ishuosi(aiPlayer.hadLocalPoints.get(aiPlayer.hadLocalPoints.size() - 1), aiPlayer.hadLocalPoints)) {
			return -10002;// �Ѿ��ǳ��ӽ�����

		}

		if (isdouble3(hadLocalPoints.get(hadLocalPoints.size() - 1), hadLocalPoints)
				&& isdanhuosi(hadLocalPoints.get(hadLocalPoints.size() - 1), hadLocalPoints)) {
//			System.out.println(" �Ѿ��൱�ӽ�Ӯ��");
			return 10001;// �Ѿ��൱�ӽ�Ӯ��
		} else if (isdouble3(aiPlayer.hadLocalPoints.get(aiPlayer.hadLocalPoints.size() - 1), aiPlayer.hadLocalPoints)
				&& isdanhuosi(aiPlayer.hadLocalPoints.get(hadLocalPoints.size() - 1), aiPlayer.hadLocalPoints)) {
//			System.out.println(" �Ѿ��൱�ӽ�����");
			return -10001;// �Ѿ��൱�ӽ�����
		}

		if (isdouble3(hadLocalPoints.get(hadLocalPoints.size() - 1), hadLocalPoints)) {
			return 9001;// �Ѿ��ӽ�Ӯ��
		}else if (isdouble3(aiPlayer.hadLocalPoints.get(aiPlayer.hadLocalPoints.size() - 1), aiPlayer.hadLocalPoints)) {
			return -9001;// �Ѿ��ӽ�����
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
	 * ģ������
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
