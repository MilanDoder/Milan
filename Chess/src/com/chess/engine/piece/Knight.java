package com.chess.engine.piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.chess.engine.board.Tile;
import com.chess.engine.piece.Piece.PieceType;
import com.google.common.collect.ImmutableList;



public class Knight extends Piece {
	private static final int[] Candidates_Move_Coordinates = {-17,-15,-10,-6,6,10,15,17};
	public Knight(final int piecePositins, final Alliance pieceAlliance){
		super(PieceType.KNIGHT,piecePositins,pieceAlliance);
	}
	public Collection<Move> calculateIlegalMove (final Board board) {	
		final List<Move> legalMove = new ArrayList<>();
		for(final int currentCandiateOffset: Candidates_Move_Coordinates){
			final int candidateDestinationCoordinate =  this.piecePositions+currentCandiateOffset;			
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){				
				if(isFirstColumnExclusion(this.piecePositions,currentCandiateOffset) ||
						isSecondColumnExclusion(this.piecePositions, currentCandiateOffset) || 
						isSevenColumnExclusion(this.piecePositions,currentCandiateOffset)||
						isEightColumnExclusion(this.piecePositions, currentCandiateOffset)){
					continue;
				}
				final Tile candidateDistiantionTile = board.getTile(candidateDestinationCoordinate);
				if(!candidateDistiantionTile.isTileOccupied()){
					legalMove.add(new MajorMove(board,this, candidateDestinationCoordinate));
				}else{
					final Piece pieceAtDestination = candidateDistiantionTile.getPiece();
					final Alliance pieceAtAlliance = pieceAtDestination.getPieceAlliance();
					if(this.pieceAlliance!= pieceAtAlliance){
						legalMove.add(new AttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMove);
	}
	public String toString(){
		 return PieceType.KNIGHT.toString();
	 }
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset==-17) || (candidateOffset==-10) || (candidateOffset==6)
				|| (candidateOffset==15);
	}
	private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
		return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset==6) || (candidateOffset==-10);
	}
	private static boolean isSevenColumnExclusion(final int currentPosition, final int candidateOffset){
		return BoardUtils.SEVEN_COLUMN[currentPosition] &&  (candidateOffset==-6) || (candidateOffset==10);
	}
	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
		return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset==-15) || (candidateOffset==-6) || 
				(candidateOffset==10) || (candidateOffset==17);
	}
	@Override
	public Knight movePieces( final Move move) {
		return new Knight(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
	}
}
