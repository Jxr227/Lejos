package action;

import java.util.Random;

import lejos.nxt.Sound;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.mapping.Heading;

/**
 * An example of how you could start writing an action model given the available
 * classes.
 * 
 * @author nah
 * 
 */
public class SensorModel {

	public void updateDistributionAfterSensing(GridPositionDistribution _dist, float data, Heading head) {

		// Commented out the random code to stop people using it without looking

		 
		//
		float prob;
		// iterate through points updating as appropriate
		
		if(data <= 300){
			for (int x = 0; x < _dist.getGridWidth(); x++) {
				for (int y = 0; y < _dist.getGridHeight(); y++) {
					// make sure to respect obstructed grid points
					if (!_dist.isObstructed(x, y)) {
						prob = _dist.getProbability(x, y);	
						if(prob > 0){
							float range = _dist.getGridMap().rangeToObstacleFromGridPoint(x, y, head);
							if(range>30){
								_dist.setProbability(x, y, 0);
							}
						}
						
					}
				}
			}
		}
		
		//
		 _dist.normalise();
	}
}
