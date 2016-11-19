package movement;

import java.util.Random;
import java.util.Stack;

import problems.grid.Grid;
import problems.grid.Grid.Direction;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

public class ChessBoard {
	private final SensorPort leftSensor;
	private final SensorPort centerSensor;
	private final NXTRegulatedMotor motorA;
	private final NXTRegulatedMotor motorB;
	private final LightSensor sensorC;
	private final LightSensor sensorL;
	private final int leftLight;
	private final int dark;
	private final int light;
	private final int offset;
	private final DifferentialPilot pilot;
	private final int Tp;
	private final int Kp;
	private boolean running;
	private int error;
	private int lightValue;
	
	/**
	 * Create a new instance of ChessBoard
	 * @param motor1 The left motor
	 * @param motor2 The right motor
	 * @param leftSense The light sensor on the left
	 * @param centerSense The light sensor in the center
	 * @param trackWidth The trackwidth of the setup
	 * @param wheelDist  The diameter of the wheel(with tyre)
	 * @param Tp Total power constant
	 * @param Kp The turning constant
	 */
	public ChessBoard(NXTRegulatedMotor motor1, NXTRegulatedMotor motor2,
			SensorPort leftSense, SensorPort centerSense, double trackWidth,
			double wheelDist, int Tp, int Kp) {
		leftSensor = leftSense;
		centerSensor = centerSense;
		motorA = motor1;
		motorB = motor2;
		sensorC = new LightSensor(centerSensor, true);
		sensorL = new LightSensor(leftSensor, true);		
		Button.waitForAnyPress();
		leftLight = sensorL.getLightValue();
		dark = sensorC.getLightValue(); //(Must be placed on line, to get the dark value)
		motorA.forward(); //Swings the robot around so lighter value can be taken
		Delay.msDelay(1000);//Delay whilst moving
		motorA.stop(); 
		light = sensorC.getLightValue(); //Should now be on the background,rather than tape
		offset = (dark + light) / 2; //The average to aim for, on the verge of background and tape
		System.out.println("Dark: " + dark + "Light: " + light + "offset: "
				+ offset); //Checking values before starting
		Button.waitForAnyPress();   //Press a button if values are okay.		
		pilot = new DifferentialPilot(wheelDist, trackWidth, motorB, motorA);
		this.Tp = Tp; 
		this.Kp = Kp;
		pilot.setTravelSpeed(this.Tp);
		pilot.setRotateSpeed(this.Tp);
//		leftSensor.addSensorPortListener(new JunctionListener(this)); //give the leftsensor a junctionlistener
		
		
	}
	
	public static void main(String[] args){		
		ChessBoard chess = new ChessBoard(Motor.A, Motor.B, SensorPort.S1,SensorPort.S2, 120, 68.8,150,20);
//		BreadthFirst<Grid.Direction, Grid> solver;
//		Grid grid1 = new Grid(7, 10, 0, 0, Direction.EAST);
//		Grid grid2 = new Grid(7, 10, 9, 6, Direction.NORTH);	
//		grid1.addWall(new Pair(0, 1), new Pair(1,1));
//		grid1.addWall(new Pair(0, 2), new Pair(0,3));
//		grid1.addWall(new Pair(1, 0), new Pair(2, 0));
//		grid1.addWall(new Pair(5, 0), new Pair(6,0));
//		grid1.addWall(new Pair(4, 1), new Pair(5,1));		
//		grid1.addWall(new Pair(4, 2), new Pair(5,2));
//		grid1.addWall(new Pair(5, 2), new Pair(5,3));
//		grid1.addWall(new Pair(2, 2), new Pair(2,3));
//		grid1.addWall(new Pair(2, 3), new Pair(3,3));
//		grid1.addWall(new Pair(1, 5), new Pair(2,5));		
//		grid1.addWall(new Pair(3, 6), new Pair(4,6));
//		grid1.addWall(new Pair(4, 4), new Pair(5,4));
//		grid1.addWall(new Pair(5, 5), new Pair(5,6));
//		grid1.addWall(new Pair(6, 4), new Pair(7,4));
//		grid1.addWall(new Pair(6, 5), new Pair(7,5));		
//		solver = new BreadthFirst<Grid.Direction, Grid>(grid1, grid2, new GridSuccessorFunction());		
//		chess.run(solver.Search());	
		Stack<Direction> moves = new Stack<Direction>();	
		for(int i=0; i<6; i++) {
			moves.push(Direction.NORTH);
		}
		moves.push(Direction.WEST);
		for(int i=0; i<10; i++) {
			moves.push(Direction.NORTH);
		}
		moves.push(Direction.WEST);
		for(int i=0; i<6; i++) {
			moves.push(Direction.NORTH);
		}
		moves.push(Direction.WEST);
		for(int i=0; i<10; i++) {
			moves.push(Direction.NORTH);
		}
		
		
		chess.run(moves);
		
	}
	
	
	public void run(){
		while(true){
			this.LineFollow();  //LineFollow() will run until junctionlistener is triggered in which case the loop in junctionlistener ends, allow program to proceed
			this.randomTask(); //Perform a random task
			Sound.beep(); //perform a beep to inform us that the task has succesfully completed
			Delay.msDelay(1000); //Delay a second before re-iterating 
		}
	}
	
	public void run(Stack<Grid.Direction> moves){
		LCD.clear();
		System.out.println(moves.size());
		Delay.msDelay(5000);
		while(!moves.empty()){
			makeMove(moves.pop());
			LineFollow();
		}
		Sound.setVolume(Sound.VOL_MAX);
		Sound.beep();
		Sound.beep();
		Sound.beep();
		Sound.beep();
		Sound.beep();
		Sound.beep();
		Sound.beep();
	}
	public void runForward(){
		Forward();
		LineFollow();
		pilot.stop();
	}
	
	public void makeMove(Grid.Direction dir){
		switch(dir){
		case NORTH:
			Forward();
			break;
		case EAST:
			Right();
			break;
		case WEST:
			Left();
			break;
		case SOUTH:
			Backward();
			break;			
		}
	}
	
	public void LineFollow(){
		
		running = true;   //Allow the loop to begin 
		while(running){
			lightValue = sensorC.getLightValue(); //get the current light value
			error = lightValue - offset; //calculate the current error from the offset
			int turn = Kp * (error); //a way of increasing turn, as error alone is usually between 2 and 10
			int powerA = Tp - turn; // both these mean a positive error will make B faster than A
			int powerB = Tp + turn; // and a negative will make A faster than B
			if (powerA > 0) {  //If the calculate power isn't negative
				motorA.setSpeed(powerA); //set the calculated speed
				motorA.forward(); //travel forward
			} else {  //else (if powerA<=0) 
				motorA.setSpeed(-powerA); //set the speed to negative powerA (postive number)
				motorA.backward(); //move backwards since it's a negative speed
			}
			if (powerB > 0) {          //Same as above
				motorB.setSpeed(powerB);
				motorB.forward();
			} else {                 //same as above
				motorB.setSpeed(-powerB);
				motorB.backward();
			}
			
		}
		motorA.stop(true);
		motorB.stop(true);
		
	}
	
	public void Left(){		//a left turn at a junction
		pilot.travel(80);
		pilot.rotate(120);
		while(sensorC.getLightValue()>dark+3){
			motorB.forward();
		}
		motorB.stop();
	
	}
	
	public void Right(){ //a right turn at a junction
		pilot.travel(50);
		pilot.rotate(-100); //overshoot to ensure the sensor is passed the tape
		while(sensorC.getLightValue()>dark){ //move back to the line
			pilot.rotateLeft();
		}
		pilot.stop();
	}
	
	public void Forward(){  //move forward at a junction
//		while(sensorL.getLightValue() < leftLight){ //while leftSensor is still on the tape
//			pilot.forward();
//		}
//		pilot.stop();
		pilot.travel(30);
	}
	
	public void Backward(){
		pilot.rotate(170);
		while(sensorC.getLightValue() >dark){
			pilot.rotateLeft();
		}
	}
	
	public void randomTask(){  //select a random class
		Random rand = new Random();
		int random = rand.nextInt(3); //generate a random int
		if(random == 0){            //select a move based on this
			this.Forward();
		}else if(random==1){
			this.Left();
		}else{
			this.Right();
		}
	}
	

	/**
	 * Set the running boolean
	 * @param running the value
	 */
	public void setRunning(boolean running){ 
		this.running = running;
	}
	/**
	 * Get the boolean value of running
	 * @return running
	 */
	public boolean isRunning(){
		return this.running;
	}
	
	/**
	 * Get the left lightSensor
	 * @return the sensor 
	 */
	public LightSensor getSensorL() {
		return sensorL;
	}
	
	/**
	 * get the dark value recorded by the config
	 * @return dark
	 */
	public int getDark() {
		return dark;
	}
	

}
