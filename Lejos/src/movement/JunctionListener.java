package movement;

import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.Sound;

public class JunctionListener implements SensorPortListener {
	private final ChessBoard chess;
	
	public JunctionListener(ChessBoard chessBoard) {
		this.chess = chessBoard;
	}

	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		if(chess.isRunning() && chess.getSensorL().getLightValue()<=chess.getDark()+4){ //whilst lineFollow isRunning, and the left sensor returns a value that's dark, or lower 
			chess.setRunning(false); //stop lineFollow running
			
			Sound.beep();
		}

	}

}
