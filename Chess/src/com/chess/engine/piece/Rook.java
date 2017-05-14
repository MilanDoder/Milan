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

public class Rook extends Piece{
	
	private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATES ={-8,-1,1,8};


	public Rook(int piecePositions, Alliance pieceAlliance) {
		super(PieceType.ROOK,piecePositions, pieceAlliance);
		// TODO Auto-generated constructor stub
	}

	
	@Override
public Collection<Move> calculateIlegalMove(final Board board) {
		
		final List<Move> legalMove = new ArrayList<>(); 
		 
		for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){
			int candidateDestinationCoordinate = this.piecePositions;
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
				if(isFirstColumnExclustion(candidateDestinationCoordinate,candidateCoordinateOffset)|| 
						isEightColumnExclustion(candidateDestinationCoordinate, candidateCoordinateOffset)){
					break;
				}
				
				candidateDestinationCoordinate+=candidateCoordinateOffset;
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
						break;
					}
				
					
				}
			}
		}
		 
		return ImmutableList.copyOf(legalMove);
	}
	
	public String toString(){
		 return PieceType.ROOK.toString();
	 }
	
	 private static boolean isFirstColumnExclustion(final int currentPosition, final int candidateOffset){
		 return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset==-1);
	 }
	 
	 private static boolean isEightColumnExclustion(final int currentPosition, final int candidateOffset){
		 return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset==1);
	 }
	
	 @Override
		public Rook movePieces( final Move move) {
			return new Rook(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
		}
}
