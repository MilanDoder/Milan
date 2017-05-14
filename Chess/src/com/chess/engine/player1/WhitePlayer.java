package com.chess.engine.player1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.piece.Piece;
import com.google.common.collect.ImmutableList;

public class WhitePlayer extends Player{

	public WhitePlayer(final Board board,
			final Collection<Move> whiteStandardMoves,
			final Collection<Move> blackStandardMoves) {
		super(board,whiteStandardMoves,blackStandardMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	
	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() {
		return this.board.blackPlayer();
	}

	@Override
	protected Collection<Move> calculateKingCastle(
			Collection<Move> playerLegel, Collection<Move> opponentsLegals) {
		final List<Move> kingCastle = new ArrayList<>();
		
		if(this.playerKing.isFirstMove() && !this.isInCheck()){
			if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()){
				final Tile rookTile = this.board.getTile(63);
				
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
					if(Player.calculateAttacksOnTile(61,opponentsLegals).isEmpty() &&
					   Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty() &&
					   rookTile.getPiece().getPieceType().isRook()){
						kingCastle.add(null);
					}
				}
			}
			if(!this.board.getTile(59).isTileOccupied() &&
			   !this.board.getTile(58).isTileOccupied() && 
			   !this.board.getTile(57).isTileOccupied()){
				final Tile rookTile =  this.board.getTile(56);
				
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
					kingCastle.addAll(null);
				}
			}
		}
		
		
		return ImmutableList.copyOf(kingCastle);
	}

	
	
}
