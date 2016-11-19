package problems.grid;

public class Pair {
	private final int x;
	private final int y;
	
	public Pair(int a, int b) {
		x=a;
		y=b;
	}
	
	public String toString(){
		return "("+x+", "+y+")";
	}
	
	@Override
	public boolean equals(Object pair){
		
		return ((Pair) pair).getX() == this.getX() && ((Pair) pair).getY() == this.getY();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
