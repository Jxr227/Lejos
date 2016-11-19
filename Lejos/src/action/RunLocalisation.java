package action;

import Search.BreadthFirst;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.util.Delay;
import movement.ChessBoard;
import movement.JunctionListener;
import problems.grid.Grid;
import problems.grid.GridSuccessorFunction;
import problems.grid.Grid.Direction;
import problems.grid.Pair;
import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.Heading;
import rp.robotics.mapping.LocalisationUtils;

public class RunLocalisation {
	private OpticalDistanceSensor sensor;
	private OpticalDistanceSensor sensor2;
	private int sensorError = 16;
	private int sensor2Error = 5;
	private ChessBoard chess;
	private Heading currDirection;
	private GridPositionDistribution distribution;
	private ActionModel model;
	private SensorModel sensorModel;
	
	
	public RunLocalisation(Heading direction) {
		currDirection = direction;
		chess = new ChessBoard(Motor.A, Motor.B, SensorPort.S1,SensorPort.S2, 120, 68.8,150,10);
		sensor = new OpticalDistanceSensor(SensorPort.S3);		
		model = new OurActionModel();
		GridMap gridMap = LocalisationUtils.create2014Map1();
		distribution = new GridPositionDistribution(gridMap);
		sensor2 = new OpticalDistanceSensor(SensorPort.S4);
		sensorModel = new SensorModel();
		SensorPort.S1.addSensorPortListener(new JunctionListener(chess));
	}

	public static void main(String[] args) {
		RunLocalisation run = new RunLocalisation(Heading.PLUS_X);
		run.explore();
		int goalX = 0;
		int goalY = 0;
		Button.waitForAnyPress();
		int x = run.distribution.getX();
		int y = run.distribution.getY();
		System.out.println("X: "+x+" Y: "+y);
		Grid grid1 = new Grid(7, 11, y, x, convertDir(run.currDirection));
		run.addGridWalls(grid1);
		Grid grid2 = new Grid(7, 11, 0, 0, Direction.EAST);
		run.chess.run(new BreadthFirst<Direction, Grid>(grid1, grid2, new GridSuccessorFunction()).Search());
		
	}
	
	public void addGridWalls(Grid grid){
//		grid.addWall(new Pair(2, 0), new Pair(2,1));
//		grid.addWall(new Pair(4, 0), new Pair(4,1));
//		grid.addWall(new Pair(5, 0), new Pair(5,1));
//		grid.addWall(new Pair(6, 0), new Pair(6,1));
//		grid.addWall(new Pair(8, 0), new Pair(8,1));
		grid.addWall(new Pair(0, 2), new Pair(1,2));
		grid.addWall(new Pair(0, 4), new Pair(1,4));
		grid.addWall(new Pair(0, 5), new Pair(1,5));
		grid.addWall(new Pair(0, 6), new Pair(1,6));
		grid.addWall(new Pair(0, 8), new Pair(1,8));
		////////////////////////////////////////////
//		grid.addWall(new Pair(2, 1), new Pair(2,2));
//		grid.addWall(new Pair(4, 1), new Pair(4,2));
//		grid.addWall(new Pair(5, 1), new Pair(5,2));
//		grid.addWall(new Pair(6, 1), new Pair(6,2));
//		grid.addWall(new Pair(8, 1), new Pair(8,2));
		/////////////////////////////////////////////
		grid.addWall(new Pair(1, 2), new Pair(2,2));
		grid.addWall(new Pair(1, 4), new Pair(2,4));
		grid.addWall(new Pair(1, 5), new Pair(2,5));
		grid.addWall(new Pair(1, 6), new Pair(2,6));
		grid.addWall(new Pair(1, 8), new Pair(2,8));
		grid.addWall(new Pair(4, 2), new Pair(5,2));
		grid.addWall(new Pair(4, 4), new Pair(5,4));
		grid.addWall(new Pair(4, 5), new Pair(5,5));
		grid.addWall(new Pair(4, 6), new Pair(5,6));
		grid.addWall(new Pair(4, 8), new Pair(5,8));
		////////////////////////////////////////////
//		grid.addWall(new Pair(6, 2), new Pair(5,2));		
//		grid.addWall(new Pair(5, 4), new Pair(6,4));
//		grid.addWall(new Pair(5, 5), new Pair(6,5));
//		grid.addWall(new Pair(5, 6), new Pair(6,6));
//		grid.addWall(new Pair(5, 8), new Pair(6,8));
		////////////////////////////////////////////
		grid.addWall(new Pair(2, 6), new Pair(2,5));		
		grid.addWall(new Pair(4, 5), new Pair(4,6));
		grid.addWall(new Pair(5, 5), new Pair(5,6));
		grid.addWall(new Pair(6, 5), new Pair(6,6));
		grid.addWall(new Pair(8, 5), new Pair(8,6));
		grid.addWall(new Pair(1, 2), new Pair(1,3));		
		grid.addWall(new Pair(1, 7), new Pair(1,8));
		grid.addWall(new Pair(5, 2), new Pair(5,3));
		grid.addWall(new Pair(6, 2), new Pair(5,2));
		grid.addWall(new Pair(5, 7), new Pair(5,8));
		///////////////////////////////////////////
		grid.addWall(new Pair(3, 5), new Pair(4,5));
		grid.addWall(new Pair(3, 5), new Pair(3,4));		
		grid.addWall(new Pair(3, 5), new Pair(3,6));
		////////////////////////////////////////////
		grid.addWall(new Pair(2, 0), new Pair(2,1));
		grid.addWall(new Pair(2, 1), new Pair(2,2));		
		grid.addWall(new Pair(2, 1), new Pair(3,1));
		////////////////////////////////////////////
		grid.addWall(new Pair(2, 9), new Pair(2,8));
		grid.addWall(new Pair(2, 9), new Pair(2,10));		
		grid.addWall(new Pair(2, 9), new Pair(3,9));
//		grid.addWall(new Pair(0, 2), new Pair(1,2));
//		grid.addWall(new Pair(1, 2), new Pair(2,2));		
//		grid.addWall(new Pair(1, 2), new Pair(1,3));
//		////////////////////////////////////////////
//		grid.addWall(new Pair(9, 2), new Pair(8,2));
//		grid.addWall(new Pair(9, 2), new Pair(10,2));		
//		grid.addWall(new Pair(9, 2), new Pair(9,3));
		////////////////////////////////////////////
//		grid.addWall(new Pair(1, 4), new Pair(0,4));
//		grid.addWall(new Pair(1, 4), new Pair(2,4));		
//		grid.addWall(new Pair(1, 4), new Pair(1,3));
//		/////////////////////////////////////////////
//		grid.addWall(new Pair(9, 4), new Pair(8,4));
//		grid.addWall(new Pair(9, 4), new Pair(10,4));		
//		grid.addWall(new Pair(9, 4), new Pair(9,3));
		////////////////////////////////////////////
		grid.addWall(new Pair(4, 1), new Pair(4,0));
		grid.addWall(new Pair(4, 1), new Pair(4,2));		
		grid.addWall(new Pair(4, 1), new Pair(3,1));
		/////////////////////////////////////////////
		grid.addWall(new Pair(4, 9), new Pair(4,8));
		grid.addWall(new Pair(4, 9), new Pair(4,10));		
		grid.addWall(new Pair(4, 9), new Pair(3,9));
		
		
	}
	
	public static Grid.Direction convertDir(Heading head){
		switch(head){
		case PLUS_X: 
			return Direction.EAST;		
		case MINUS_X:
			return Direction.WEST;
		case PLUS_Y: 
			return Direction.SOUTH;
		case MINUS_Y:
			return Direction.NORTH;
			default: return Direction.EAST;
		}
	}
	public boolean passTest(int value, OpticalDistanceSensor sense){
		for(int i=0; i<5; i++) {
			Delay.msDelay(100);
			if(sense.getDistance()> value){				
				return false;				
			}
		}
		return true;		
	}	
	public void explore(){		
		while(!distribution.hasWinner()){
			distribution.printHighest();
			forwardToWall();
			if(sensor.getDistance() <= 300+sensorError*10 && sensor2.getDistance()<= 300+sensor2Error*10){
				if(passTest(300+sensorError*10, sensor) && passTest(300+sensor2Error*10, sensor2)){
					chess.Backward();
					currDirection = Heading.flipHeading(currDirection);
					System.out.println(currDirection);
				}
			}else if(sensor.getDistance() <= 300+(sensorError*10)){
				if(passTest(300+sensorError*10, sensor)){
					chess.Left();
					currDirection = Heading.rotateLeft(currDirection);
					System.out.println(currDirection);
				}
//				Sound.beep();
//				System.out.println("Left: "+sensor.getDistance());
			}
		}
		System.out.println("WINNNER");
		
	}
	
	
	public void forwardToWall(){
		while(sensor.getDistance()>300+sensorError*10){
			chess.runForward();
			distribution = model.updateAfterMove(distribution, currDirection);
			System.out.println("Sensor:"+sensor2.getDistance());
			sensorModel.updateDistributionAfterSensing(distribution, sensor.getDistance()-sensorError*10, currDirection);			
			sensorModel.updateDistributionAfterSensing(distribution, sensor2.getDistance()-sensor2Error*10, Heading.rotateLeft(currDirection));
			
		}
	}

}
