package problems.grid;

import java.util.List;

import problems.grid.Grid.Direction;
import rp13.search.interfaces.SuccessorFunction;
import rp13.search.util.ActionStatePair;

public class GridSuccessorFunction implements SuccessorFunction<Direction, Grid>{

	public GridSuccessorFunction() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void getSuccessors(Grid _state,
			List<ActionStatePair<Direction, Grid>> _successors) {
		assert(_successors != null);
		for(Direction dir : Direction.values()){
			
			if(_state.isPossibleMove(dir)){
				Grid grid = new Grid(_state);
				grid.makeMove(dir);
				
				grid.setDirection(_state.newFaceDirection(_state.getActualDir(dir)));
				_successors.add(new ActionStatePair<Direction, Grid>(_state.getActualDir(dir), grid));
			}
		}
		
	}

	

}
