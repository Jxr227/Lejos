package problems.grid;

public class GridWall {

	private final Pair pairA;
	private final Pair pairB;

	public GridWall(Pair _pairA, Pair _pairB) {
		pairA = _pairA;
		pairB = _pairB;
	}

	public Pair getPairA() {
		return this.pairA;
	}

	public Pair getPairB() {
		return this.pairB;
	}
	
	@Override
	public boolean equals(Object pair) {
		return ((GridWall) pair).getPairA().equals(this.getPairA())
				&& ((GridWall) pair).getPairB().equals(this.getPairB());
	}
//	
//	public static void main(String[] args){
//		Pair pair1 = new Pair(1, 2);
//		Pair pair2 = new Pair(1, 2);
//		Pair pair3 = new Pair(2,1);
//		GridWall wall = new GridWall(pair1, pair2);
//		GridWall wall2 = new GridWall(pair2, pair1);
//		GridWall wall3 = new GridWall(pair3, pair2);
//		System.out.println(wall.equals(wall2));
//		System.out.println(wall.equals(wall3));
//	}
}
