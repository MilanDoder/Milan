package com.chess.engine.player1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.chess.engine.board.Tile;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;
import com.google.common.collect.ImmutableList;

public class BlackPlayer extends Player {
	public BlackPlayer(final Board board,
			final Collection<Move> whiteStandardMoves,
			final Collection<Move> blackStandardMoves) {
		// TODO Auto-generated constructor stub
		super(board, blackStandardMoves,whiteStandardMoves);
		
	}
	@Override
	public Collection<Piece> getActivePieces() {
		// TODO Auto-generated method stub
		return this.board.getBlackPieces();
	}
	@Override
	public Alliance getAlliance() {
		// TODO Auto-generated method stub
		return Alliance.BLACK;
	}
	@Override
	public Player getOpponent() {
		// TODO Auto-generated method stub
		return this.board.whitePlayer();
	}
	@Override
	protected Collection<Move> calculateKingCastle(final Collection<Move> playerLegel,
												   final Collection<Move> opponentsLegals) {
		final List<Move> kingCastle = new ArrayList<>();
		if(this.playerKing.isFirstMove() && !this.isInCheck()){
			if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()){
				final Tile rookTile = this.board.getTile(7);	
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
					if(Player.calculateAttacksOnTile(5,opponentsLegals).isEmpty() &&
					   Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty() &&
					   rookTile.getPiece().getPieceType().isRook()){
						kingCastle.add(new KingSideCastleMove(this.board, 
																   this.playerKing,
																   6, 
																   (Rook) rookTile.getPiece(), 
																   rookTile.getTileCoordinate(), 
																   5));
					}
				}
			}
			if(!this.board.getTile(3).isTileOccupied() &&
			   !this.board.getTile(2).isTileOccupied() && 
			   !this.board.getTile(1).isTileOccupied()){
				final Tile rookTile =  this.board.getTile(0);
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
				   Player.calculateAttacksOnTile(2,opponentsLegals).isEmpty() &&
				   Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty() &&
				   rookTile.getPiece().getPieceType().isRook()){
					kingCastle.add(new QueenSideCastleMove(this.board, 
																this.playerKing,
																2, 
																(Rook) rookTile.getPiece(), 
																rookTile.getTileCoordinate(), 
																3));
				}
			}
		}
		return ImmutableList.copyOf(kingCastle);
	}

}
