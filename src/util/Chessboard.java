package util;

import java.util.List;

/**
 * 游戏棋盘
 * @author 青春进行时
 *
 */
public interface Chessboard {

	/**
	 * 取得棋盘最大横坐标  
	 * @return
	 */
	
    public int getMaxX();  
    
    /**
     * 最大纵坐标  
     * @return
     */
    public int getMaxY();  
    
    /**
     * 取得当前所有空白点，这些点才可以下棋  
     * @return
     */
    public List<Point> getFreePoints();  
}
