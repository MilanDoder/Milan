package com.chess.engine;

import com.chess.engine.player1.BlackPlayer;
import com.chess.engine.player1.Player;
import com.chess.engine.player1.WhitePlayer;

public enum Alliance {
	WHITE {
		@Override
		public
		int getDirection() {
			// TODO Auto-generated method stub
			return -1;
		}
		@Override
		public boolean isBlack() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean isWhite() {
			// TODO Auto-generated method stub
			return true;
		}
		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer,
				final BlackPlayer blackPlayer) {
			// TODO Auto-generated method stub
			return whitePlayer;
		}
	},
	BLACK {
		@Override
		public
		int getDirection() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public boolean isBlack() {
			// TODO Auto-generated method stub
			return true;
		}
		@Override
		public boolean isWhite() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer,
				final BlackPlayer blackPlayer) {
			// TODO Auto-generated method stub
			return blackPlayer;
		}
		
	};
	
	
	public abstract int getDirection();
	public abstract boolean isBlack();
	public abstract boolean isWhite();
	public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
	
}
