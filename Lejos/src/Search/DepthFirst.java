package Search;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import rp13.search.interfaces.SuccessorFunction;
import rp13.search.util.ActionStatePair;
import rp13.search.util.EqualityGoalTest;

public class DepthFirst<Action, State> {
	
	
	private Stack<Action> path = new Stack<Action>();
	private final EqualityGoalTest<State> test; //The equalityGoalTest to create
	private final State start; //Initial State
	private final SuccessorFunction<Action, State> successorFunction; //Need a successorFunction
	/**
	 * Create a new instance of the solver
	 * @param start The starting state passed.
	 * @param goal The goal state.
	 */
	public DepthFirst(State _start, State _goal, SuccessorFunction<Action, State> succ){
		test = new EqualityGoalTest<State>(_goal); //
		start = _start;
		successorFunction = succ;				
	}
	
	public Stack<Action> Search(){
		Stack<ActionStatePair<Action, State>> statesList = new Stack<ActionStatePair<Action, State>>(); //Frontier, will be filled with ActionPairs to be searched
		ArrayList<State> visitedPuzzles = new ArrayList<State>(); //List of puzzles already visited, avoid duplicate searching
		ActionStatePair<Action, State> startState = new ActionStatePair<Action, State>(null, start);//
		startState.setParent(null);
		statesList.addElement(startState);//Add the first item
		
		for(ActionStatePair<Action, State> state : getChildren(startState)){//For every state in the first states children
			statesList.addElement(state);//Add that state to the LinkedList
			state.setParent(startState);//Set the parent of these nodes as the initialstart
		}
		
		while(!statesList.isEmpty()){ //While the states to search has states contained
			ActionStatePair<Action, State> temp = statesList.pop(); //Take the top item			
			List<ActionStatePair<Action, State>> successors = 	getChildren(temp); //Generate a list of the children of the top state
			if(test.isGoal(temp.getState())){ //If the top State equals the goal State
				//SUCCESS
				tracePath(temp);
//				System.out.println("Size: "+path.size());
//				System.out.println(start.toString());
//				printPath(path);
				
				//System.out.println("5"+"FOUND");
				//System.out.println(temp.getState().toString());
				break;  //End the looping
			}else{ //Top node was not goal
				if(!visitedPuzzles.contains(temp.getState())){ //if the state does not exist in already visisted states
					for(ActionStatePair<Action, State> pair : successors){//for every child the top node generated
						if(!visitedPuzzles.contains(pair.getState())){
							statesList.addElement(pair); //Add that to the list
							pair.setParent(temp);
						}
					}
					
					visitedPuzzles.add(temp.getState()); //Add the now checked state to visitedPuzzles
					//System.out.println(temp);
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
		while(state.getParent()!= null){
			path.push(state.getAction());
			state = state.getParent();
		}
		
	}
	
	public void printPath(Stack<Action> list){
		
		while(!list.isEmpty()){
			System.out.print(list.pop());
		}
	}
	
	
	
	
	

	

}
