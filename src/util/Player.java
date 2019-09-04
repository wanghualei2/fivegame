package util;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏玩家接口
 * @author 青春进行时
 *
 */
public interface Player {
	
	/**
	 * 判断是否已经赢了游戏
	 * @param point 当前下的位置
	 * 因为如果赢了构成五子的一定与当前的落在构成五子
	 * @return
	 */
	public boolean hasWin();
	
	/**
	 * 
	 * @param point 玩家要落子的地方
	 */
	public void run(Point point);
	
	/**
	 * 设置玩家游戏的棋盘
	 * @param chessboard
	 */
	public void setChessboard(IChessboard chessboard);  
    
	/**
	 * 得到当前玩家落子的所有坐标
	 * @return
	 */
    public List<Point> getMyPoints();  
	
}
