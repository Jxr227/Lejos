package action;
import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.mapping.Heading;

public class OurActionModel implements ActionModel {


	@Override
	public GridPositionDistribution updateAfterMove(
			GridPositionDistribution dist, Heading heading) {
		GridPositionDistribution destination = new GridPositionDistribution(
				dist);
		switch (heading) {
		case PLUS_X:
			movePlusX(dist, destination);
			break;
		case PLUS_Y:
			movePlusY(dist, destination);
			break;
		case MINUS_X:
			moveMinusX(dist, destination);
			break;
		case MINUS_Y:
			moveMinusY(dist, destination);
			break;
		}

		return destination;
	}

	private void movePlusX(GridPositionDistribution _from,
			GridPositionDistribution _to) {
		// iterate through points updating as appropriate
		for (int y = 0; y < _to.getGridHeight(); y++) {
			for (int x = 0; x < _to.getGridWidth(); x++) {
				// make sure to respect obstructed grid point
				if (!_to.isObstructed(x, y)) {
					int fromX = x-1;
					int fromY = y;					
					// position after move
					int toX = x;
					int toY = y;
					// set probability for position after move
					float toProb = _from.getProbability(toX, toY);
					float fromProb;
					if(_to.getGridMap().isValidTransition(fromX, fromY, toX, toY)){
						fromProb = _from.getProbability(fromX, fromY);
					}else{
						fromProb = 0;
					}
					_to.setProbability(toX, toY, fromProb);
					if((toX == _to.getGridWidth()-1) || !_to.getGridMap().isValidTransition(toX, toY, toX+1, toY)){
						_to.setProbability(toX, toY, fromProb+toProb); 
					}
				}
			}
		}
		_to.normalise();
	}
	private void moveMinusX(GridPositionDistribution _from,
			GridPositionDistribution _to) {
		// iterate through points updating as appropriate
		for (int y = 0; y < _to.getGridHeight(); y++) {
			for (int x = 0; x < _to.getGridWidth(); x++) {
				// make sure to respect obstructed grid points
				if (!_to.isObstructed(x, y)) {
					
					int fromX = x+1;
					int fromY = y;					
					// position after move
					int toX = x;
					int toY = y;
					// set probability for position after move
					float toProb = _from.getProbability(toX, toY);
					float fromProb;
					if(_to.getGridMap().isValidTransition(fromX, fromY, toX, toY)){
						fromProb = _from.getProbability(fromX, fromY);
					}else{
						fromProb = 0;
					}
					_to.setProbability(toX, toY, fromProb);
					if((toX == 0 || !_to.getGridMap().isValidTransition(toX, toY, toX-1, toY))){
						_to.setProbability(toX, toY, fromProb+toProb);
					}
					
				}
			}
		}
	}
	private void movePlusY(GridPositionDistribution _from,
			GridPositionDistribution _to) {
		// iterate through points updating as appropriate
		for (int y = 0; y < _to.getGridHeight(); y++) {
			for (int x = 0; x < _to.getGridWidth(); x++) {
				// make sure to respect obstructed grid points
				if (!_to.isObstructed(x, y)){
					int fromX = x;
					int fromY = y-1;
					
					// position after move
					int toX = x;
					int toY = y;
					// set probability for position after move
					float toProb = _from.getProbability(toX, toY);
					float fromProb;
					if(_to.getGridMap().isValidTransition(fromX, fromY, toX, toY)){
						fromProb = _from.getProbability(fromX, fromY);
					}else{
						fromProb = 0;
					}
					_to.setProbability(toX, toY, fromProb);
					if(toY == _to.getGridHeight() || !_to.getGridMap().isValidTransition(toX, toY, toX, toY+1)){
						_to.setProbability(toX, toY, fromProb+toProb);
					}
				}
			}
		}
	}
	private void moveMinusY(GridPositionDistribution _from,
			GridPositionDistribution _to) {
		// iterate through points updating as appropriate
		for (int y = 0; y < _to.getGridHeight(); y++) {
			for (int x = 0; x < _to.getGridWidth(); x++) {
				// make sure to respect obstructed grid points
				if (!_to.isObstructed(x, y)) {
					int fromX = x;
					int fromY = y+1;					
					// position after move
					int toX = x;
					int toY = y;
					// set probability for position after move
					float toProb = _from.getProbability(toX, toY);
					float fromProb;
					if(_to.getGridMap().isValidTransition(fromX, fromY, toX, toY)){
						fromProb = _from.getProbability(fromX, fromY);
					}else{
						fromProb = 0;
					}
					_to.setProbability(toX, toY, fromProb);
					if(toY == 0 || !_to.getGridMap().isValidTransition(toX, toY, toX, toY-1)){
						_to.setProbability(toX, toY, fromProb+toProb);
					}
				}
			}
		}
	}
}
