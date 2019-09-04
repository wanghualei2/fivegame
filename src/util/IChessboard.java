package util;

import java.util.LinkedList;
import java.util.List;

/**
 * 棋盘
 * @author 青春进行时
 *
 */
public class IChessboard implements Chessboard{

	private List<Point> freepointList;

    public IChessboard(){
    	freepointList = new LinkedList<>();
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
            	freepointList.add(new Point(i,j));
            }
        }
    }
    
    public void init(){
    	
    	freepointList.clear();
    	for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
            	freepointList.add(new Point(i,j));
            }
        }
    }

    public void setPointList(List<Point> freepointList) {
        this.freepointList = freepointList;
    }

    @Override
    public int getMaxX() {
        return 15;
    }

    @Override
    public int getMaxY() {
        return 15;
    }

    @Override
    public List<Point> getFreePoints() {
        return freepointList;
    }

}
