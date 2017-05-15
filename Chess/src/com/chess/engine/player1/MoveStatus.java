package com.chess.engine.player1;

public enum MoveStatus {
	DONE {
		@Override
		public boolean isDone() {
			// TODO Auto-generated method stub
			return true;
		}
	},
	
	ILLEGAL_MOVE{
		@Override
		public boolean isDone() {
			// TODO Auto-generated method stub
			return false;
		}
	}, 
	LEAVES_PLAYER_IN_CHECK {
		@Override
		public boolean isDone() {
			// TODO Auto-generated method stub
			return false;
		}
	};	
	
	public abstract boolean isDone();	

}
