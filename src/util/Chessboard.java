package util;

import java.util.List;

/**
 * ��Ϸ����
 * @author �ഺ����ʱ
 *
 */
public interface Chessboard {

	/**
	 * ȡ��������������  
	 * @return
	 */
	
    public int getMaxX();  
    
    /**
     * ���������  
     * @return
     */
    public int getMaxY();  
    
    /**
     * ȡ�õ�ǰ���пհ׵㣬��Щ��ſ�������  
     * @return
     */
    public List<Point> getFreePoints();  
}
