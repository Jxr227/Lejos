package problems.grid;

import java.util.ArrayList;


public class Grid {
	private static int width;
	private static int height;
	private Pair currPosition;
	private static Pair[] theGrid;
	private static ArrayList<GridWall> existingWalls = new ArrayList<GridWall>();
	private Direction currDirection;

	/**
	 * 
	 * Enum for the direction
	 */
	public enum Direction {
		NORTH(), EAST(), SOUTH(), WEST();
		
		

	}
	
	public Grid(Grid _that) {
		currDirection = _that.currDirection;
		currPosition = _that.getCurrPos();
		
	}

	/**
	 * Create a new grid given only the width and height of the grid Takes it to
	 * be in the top left corner, facing south.
	 * 
	 * @param _width
	 *            the width of the grid
	 * @param _height
	 *            the height of the grid
	 */
	public Grid(int _width, int _height) {
		width = _width;
		height = _height;
		currDirection = Direction.SOUTH;
		theGrid = makeGrid();
		currPosition = theGrid[0];
	}

	/**
	 * Creates a new grid
	 * 
	 * @param _width
	 *            the width of the grid
	 * @param _height
	 *            the height of the grid
	 * @param _x
	 *            the starting x position of the robot on the grid
	 * @param _y
	 *            the starting y position of the robot on the grid
	 * @param startDir
	 *            The direction the robot is facing on the grid
	 */
	public Grid(int _width, int _height, int _x, int _y, Direction startDir) {
		width = _width;
		height = _height;
		theGrid = makeGrid();
		currDirection = startDir;
		currPosition = theGrid[setPositionPoint(_x, _y)];
	}

	/**
	 * Generate the grid
	 * 
	 * @return an Array of pairs that represents the grid
	 */
	public Pair[] makeGrid() {
		Pair[] temp = new Pair[height * width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				temp[i * width + j] = new Pair(i, j);
			}
		}
		return temp;
	}
	
	public Direction getActualDir(Direction dir){
		if(currDirection == Direction.NORTH){
			return dir;
		}else if(currDirection == Direction.EAST){
			switch(dir){
			case WEST:
				return Direction.SOUTH;
			case NORTH:
				return Direction.WEST;
			case SOUTH:
				return Direction.EAST;
			case EAST:
				return Direction.NORTH;
			}
		}else if(currDirection == Direction.SOUTH){
			switch(dir){
			case WEST:
				return Direction.EAST;
			case EAST:
				return Direction.WEST;
			case NORTH:
				return Direction.SOUTH;
			case SOUTH:
				return Direction.NORTH;
			}
		}else if(currDirection == Direction.WEST){
			switch(dir){
			case EAST:
				return Direction.SOUTH;
			case NORTH:
				return Direction.EAST;
			case SOUTH:
				return Direction.WEST;
			case WEST:
				return Direction.NORTH;
			}
		}
		return dir;//Shouldn't reach here....		
	}
	
	public void setDirection(Direction dir){
		this.currDirection = dir;
	}
	
	public Direction newFaceDirection(Direction dir){
		switch(dir){
		case EAST:
			return rotRight(currDirection);
		case WEST: 
			return rotLeft(currDirection);
		case SOUTH:
			return rotRight(rotRight(currDirection));
		default:
			return currDirection;
		}
		
	}
	
	private Direction rotRight(Direction dir){
		switch(dir){
		case NORTH:
			return Direction.EAST;
		case EAST:
			return Direction.SOUTH;
		case SOUTH:
			return Direction.WEST;
		case WEST:
			
			return Direction.NORTH;
		default:
			return dir;
		}
	}
	private Direction rotLeft(Direction dir){
		switch(dir){
		case NORTH:
			return Direction.WEST;
		case EAST:
			
			return Direction.NORTH;
		case SOUTH:
			return Direction.EAST;
		case WEST:
			return Direction.SOUTH;
		default:
			return dir;
		}
	}


	/**
	 * To string method for the grid representation Shows coordinates.
	 */
	public String toString() {
		StringBuffer temp = new StringBuffer();
		for (int i = 0; i < height; i++) {
			temp.append("\n");

			for (int j = 0; j < width; j++) {
				if (theGrid[(i * width) + j].equals(currPosition)) {
					temp.append("| " + theGrid[(i * width) + j] + " |");
				} else {
					temp.append(theGrid[(i * width) + j]);
				}

			}
		}
		return temp.toString();
	}

	/**
	 * Add a wall to the grid.
	 * 
	 * @param a
	 *            first point on the grid
	 * @param b
	 *            second point on the grid both points will have a wall between
	 *            them.
	 */
	public void addWall(Pair a, Pair b) {
		GridWall wall = new GridWall(a, b);
		GridWall wall2 = new GridWall(b,a);
		existingWalls.add(wall);
		existingWalls.add(wall2);
	}

	/**
	 * Return if two pairs have a wall between them
	 * 
	 * @param a
	 *            The first point on the grid
	 * @param b
	 *            the second point on the grid
	 * @return
	 */
	public boolean hasWall(Pair a, Pair b) {
		GridWall wall = new GridWall(a, b);
		return existingWalls.contains(wall);

	}

	/**
	 * Calculate if a move is possible
	 * 
	 * @param _move
	 *            The direction wished to move
	 * @return
	 */
	public boolean isPossibleMove(Direction _move) {
		int newPosition = setPositionPoint(currPosition.getX(),
				currPosition.getY())
				+ getMoveInt(_move);
		if (newPosition >= 0 && newPosition < theGrid.length) {
			Pair newPair = theGrid[newPosition];
			int newRow = newPair.getX();
			int currRow = currPosition.getX();
			return !hasWall(currPosition, newPair) && (newRow == currRow || _move == Direction.SOUTH || _move == Direction.NORTH);

		}

		return false;

	}

	public void makeMove(Direction _move) {
		if (isPossibleMove(_move)) {			
			currPosition = theGrid[setPositionPoint(currPosition.getX(),
					currPosition.getY()) + getMoveInt(_move)];
		}
	}

	private int setPositionPoint(int a, int b) {
		return a * width + b;
	}

	private static int getMoveInt(Direction dir) {
		switch (dir) {
		case NORTH:
			return -width;

		case SOUTH:
			return width;
		case EAST:
			return 1;
		case WEST:
			return -1;
		default:
			return 0;
		}
	}
	
	private Pair getCurrPos(){
		return currPosition;
	}
	
	public Grid copy(){
		Grid temp = new Grid(width, height, currPosition.getY(), currPosition.getX(), currDirection);		
		return temp;	
	}
	@Override
	public boolean equals(Object _that){
		return currPosition.equals(((Grid) _that).getCurrPos());
	}

	

}
