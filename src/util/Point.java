package util;

/**
 * 用来描述棋子所在的位置
 * @author 青春进行时
 *
 */
public class Point {

	public int x;
	
	public int y;
	 

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	@Override  
    public boolean equals(Object obj) {  
        if (this == obj)  
            return true;  
        Point other = (Point) obj;  
        if (x != other.x)  
            return false;  
        if (y != other.y)  
            return false;  
        return true;  
    } 
	
}
