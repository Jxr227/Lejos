package Search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import movement.ChessBoard;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import problems.grid.Grid;
import problems.grid.Grid.Direction;
import problems.grid.GridSuccessorFunction;
import problems.grid.Pair;
//import problems.stringJumble.JumbledString;
//import problems.stringJumble.JumbledStringMove;
//import problems.stringJumble.JumbledStringSuccessorFunction;
import rp13.search.interfaces.SuccessorFunction;
import rp13.search.util.ActionStatePair;
import rp13.search.util.EqualityGoalTest;

public class BreadthFirst<Action, State> {
	
	
	private Stack<Action> path = new Stack<Action>();
	private final EqualityGoalTest<State> test; //The equalityGoalTest to create
	private final State start; //Initial State
	private final SuccessorFunction<Action, State> successorFunction; //Need a successorFunction
	
	final long startTime = System.currentTimeMillis();
	/**
	 * Create a new instance of the solver
	 * @param start The starting state passed.
	 * @param goal The goal state.
	 */
	public BreadthFirst(State _start, State _goal, SuccessorFunction<Action, State> succ){
		test = new EqualityGoalTest<State>(_goal); //
		start = _start;
		successorFunction = succ;	
		
	}
	
	public Stack<Action> Search(){
		LinkedList<ActionStatePair<Action, State>> statesList = new LinkedList<ActionStatePair<Action, State>>(); //Frontier, will be filled with ActionPairs to be searched
		ArrayList<State> visitedPuzzles = new ArrayList<State>(); //List of puzzles already visited, avoid duplicate searching
		ActionStatePair<Action, State> startState = new ActionStatePair<Action, State>(null, start);//
		startState.setParent(null);
		statesList.add(startState);//Add the first item
		
		for(ActionStatePair<Action, State> state : getChildren(startState)){//For every state in the first states children
			statesList.add(state);//Add that state to the LinkedList
			state.setParent(startState);//Set the parent of these nodes as the initialstart
		}
		
		while(!statesList.isEmpty()){ //While the states to search has states contained					
			ActionStatePair<Action, State> temp = statesList.get(0); //Take the top item		
			statesList.remove(0); //Take the top item		
			List<ActionStatePair<Action, State>> successors = 	getChildren(temp); //Generate a list of the children of the top state
			if(test.isGoal(temp.getState())){ //If the top State equals the goal State
				//SUCCESS
				
				System.out.println("SUCCESS");
				tracePath(temp);				
//				printPath(path);				
//				final long endTime = System.currentTimeMillis();				
//				System.out.println(endTime-startTime);
//				System.out.println(path.size()+"SIZE");
				break;  //End the looping
			}else{ //Top node was not goal
				if(!visitedPuzzles.contains(temp.getState())){ //if the state does not exist in already visisted states
					for(ActionStatePair<Action, State> pair : successors){//for every child the top node generated
						statesList.add(pair); //Add that to the list							
						pair.setParent(temp);
					}					
					visitedPuzzles.add(temp.getState()); //Add the now checked state to visitedPuzzles								
				}
			}
		}
		return path;
		
		
		
	}
	
	public List<ActionStatePair<Action, State>> getChildren(ActionStatePair<Action, State> _state){
		List<ActionStatePair<Action, State>> children = new ArrayList<ActionStatePair<Action,State>>();
		successorFunction.getSuccessors(_state.getState(), children);
		return children;
		
		
	}
	
	public void tracePath(ActionStatePair<Action, State> endNode){
		ActionStatePair<Action, State> state = endNode;
		while(state.getParent() != null){			
			path.push(state.getAction());			
			state = state.getParent();

		}
		
	}
	
	public void printPath(Stack<Action> list){
	
		while(!list.isEmpty()){
			System.out.print(list.pop()+" ");
			
		}
	}
	
	public Stack<Action> getPath(){
		return path;
	}
	
	
	
	
	

	public static void main(String[] args) {
		ChessBoard chess = new ChessBoard(Motor.A, Motor.B, SensorPort.S1,SensorPort.S2, 120, 68.8,150,20);
		BreadthFirst<Grid.Direction, Grid> solver;
		Grid grid1 = new Grid(10, 7, 0, 0, Direction.SOUTH);
		Grid grid2 = new Grid(10, 7, 5, 8, Direction.NORTH);	
		
		grid1.addWall(new Pair(0, 0), new Pair(0,1));
		grid1.addWall(new Pair(1, 0), new Pair(2,0));
		grid1.addWall(new Pair(1, 1), new Pair(0,1));
		grid1.addWall(new Pair(1, 1), new Pair(1,2));
		
		grid1.addWall(new Pair(2, 1), new Pair(2,0));
		grid1.addWall(new Pair(2, 2), new Pair(2,3));
		grid1.addWall(new Pair(1, 2), new Pair(1,3));
		grid1.addWall(new Pair(0, 2), new Pair(0,3));

		grid1.addWall(new Pair(3, 2), new Pair(4,2));
		grid1.addWall(new Pair(3, 3), new Pair(3,4));
		grid1.addWall(new Pair(3, 3), new Pair(2,3));
		grid1.addWall(new Pair(4, 3), new Pair(4,4));
		
		grid1.addWall(new Pair(4, 3), new Pair(5,3));
		grid1.addWall(new Pair(5, 3), new Pair(6,3));
		grid1.addWall(new Pair(4, 1), new Pair(4,0));
		grid1.addWall(new Pair(4, 1), new Pair(4,2));
		grid1.addWall(new Pair(5, 4), new Pair(5,5));
		
		grid1.addWall(new Pair(4, 4), new Pair(3,4));
		grid1.addWall(new Pair(4, 5), new Pair(3,5));
		grid1.addWall(new Pair(6, 5), new Pair(6,6));
		grid1.addWall(new Pair(5, 5), new Pair(5,6));
		
		grid1.addWall(new Pair(4, 6), new Pair(5,6));
		grid1.addWall(new Pair(4, 7), new Pair(5,7));
		grid1.addWall(new Pair(5, 8), new Pair(5,7));
//		grid1.addWall(new Pair(4, 5), new Pair(3,5));
		
//		grid1.addWall(new Pair(0, 7), new Pair(0,8));
//		grid1.addWall(new Pair(3, 8), new Pair(4,8));
		solver = new BreadthFirst<Grid.Direction, Grid>(grid1, grid2, new GridSuccessorFunction());		
		chess.run(solver.Search());
		//System.out.println(grid1.toString());
//		System.out.println(grid2);
//		grid1.makeMove(Direction.EAST);
//		grid1.makeMove(Direction.SOUTH);
//		System.out.println(solver.path.size());
//		System.out.println("SUCS");
//		for(Direction dir: solver.path){
//			System.out.println(dir);
//		}
		
//		BreadthFirstSolver<PuzzleMove, EightPuzzle> solver;
//		solver = new BreadthFirstSolver<PuzzleMove, EightPuzzle>(EightPuzzle.randomEightPuzzle(), EightPuzzle.orderedEightPuzzle(), new EightPuzzleSuccessorFunction());
//		solver.Search();
//		BreadthFirstSolver<JumbledStringMove, JumbledString> solver;
//		JumbledString string1 = new JumbledString("abcdefghi");
//		JumbledString string2 = string1.jumbleTheString();
//		solver = new BreadthFirstSolver<JumbledStringMove, JumbledString>(string2, string1, new JumbledStringSuccessorFunction());
//		solver.Search();
//		Grid grid1 = new Grid(7, 11, 0, 0, Direction.NORTH);
//		Grid grid2 = new Grid(7, 11, 10, 6, Direction.NORTH);
//		System.out.println(grid2.equals(grid1));
	}

}
