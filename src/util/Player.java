package util;

import java.util.ArrayList;
import java.util.List;

/**
 * ��Ϸ��ҽӿ�
 * @author �ഺ����ʱ
 *
 */
public interface Player {
	
	/**
	 * �ж��Ƿ��Ѿ�Ӯ����Ϸ
	 * @param point ��ǰ�µ�λ��
	 * ��Ϊ���Ӯ�˹������ӵ�һ���뵱ǰ�����ڹ�������
	 * @return
	 */
	public boolean hasWin();
	
	/**
	 * 
	 * @param point ���Ҫ���ӵĵط�
	 */
	public void run(Point point);
	
	/**
	 * ���������Ϸ������
	 * @param chessboard
	 */
	public void setChessboard(IChessboard chessboard);  
    
	/**
	 * �õ���ǰ������ӵ���������
	 * @return
	 */
    public List<Point> getMyPoints();  
	
}
