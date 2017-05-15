package com.chess.engine.piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.piece.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class King extends Piece{
	private final int[] CANDIDATE_MOVE_COORDINATE = {-9,-8,-7,-1,1,7,8,9};
	public King(int piecePositions, Alliance pieceAlliance) {
		super(PieceType.KING,piecePositions, pieceAlliance);
	}
	@Override
	public Collection<Move> calculateIlegalMove(Board board) {
		// TODO Auto-generated method stub	
		final List<Move> legalMove =  new ArrayList<>();
		for(final int currentCoordinateOffset: CANDIDATE_MOVE_COORDINATE){
			final int candidateDestinationCoordinate = this.piecePositions +currentCoordinateOffset;
			if(isFirstColumnExclusion(this.piecePositions,currentCoordinateOffset) || 
					isEightColumnExclusion(this.piecePositions, currentCoordinateOffset)){
				continue;
			}
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
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
		 return PieceType.KING.toString();
	 }
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
		return BoardUtils.FIRST_COLUMN[currentPosition] && 
				(candidateOffset==-9) || (candidateOffset==-1) || (candidateOffset==7);
	}
	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
		return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset==-7) || (candidateOffset==1) || 
				(candidateOffset==9);
	}
	@Override
	public King movePieces( final Move move) {
		return new King(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
	}
}
