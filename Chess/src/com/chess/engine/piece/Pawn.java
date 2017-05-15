package com.chess.engine.piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.piece.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {
	private final int[] CANDIDATE_MOVE_COORDINATE = {8,16,7,9};
	public Pawn(final int piecePositions,final Alliance pieceAlliance) {
		super(PieceType.PAWN,piecePositions, pieceAlliance);
	}	
	@Override
	public Collection<Move> calculateIlegalMove(Board board) {	
		final List<Move> legalMove =  new ArrayList<>();
		for(final int currentCoordinateOffset: CANDIDATE_MOVE_COORDINATE){
			final int candidateDestinationCoordinate = this.piecePositions +
													   (this.getPieceAlliance().getDirection()* 
													   currentCoordinateOffset);
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
				continue;
			}
			if(currentCoordinateOffset==8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
				legalMove.add(new MajorMove(board,this, candidateDestinationCoordinate));
			}else if(currentCoordinateOffset==16 && this.isFirstMove() &&
					(BoardUtils.SEVEN_ROW[this.piecePositions] && this.getPieceAlliance().isBlack() ) 
					|| (BoardUtils.SECOND_ROW[this.piecePositions])&& this.getPieceAlliance().isWhite()){
				final int behindCandidanteDestinastionCoordinate = 
						this.piecePositions + (this.pieceAlliance.getDirection()*8);
				if(!board.getTile(behindCandidanteDestinastionCoordinate).isTileOccupied() 
						&& !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
						legalMove.add(new MajorMove(board,this, candidateDestinationCoordinate));
				}
			}else if(currentCoordinateOffset== 7  &&  
					!((BoardUtils.EIGHT_COLUMN[this.piecePositions] &&
					this.pieceAlliance.isWhite()) ||
					(BoardUtils.FIRST_COLUMN[this.piecePositions] &&
					this.pieceAlliance.isBlack()))){
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
					Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if(this.pieceAlliance!= pieceOnCandidate.getPieceAlliance()){
						//TODO more to do 
						//legalMove.add(new AttackMove(board,this, candidateDestinationCoordinate));
					}
				}
			}else if(currentCoordinateOffset==9 &&
					!((BoardUtils.EIGHT_COLUMN[this.piecePositions] && this.pieceAlliance.isBlack()) ||
					(BoardUtils.FIRST_COLUMN[this.piecePositions] && this.pieceAlliance.isWhite()))){
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
					Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if(this.pieceAlliance!= pieceOnCandidate.getPieceAlliance()){
						//legalMove.add(new AttackMove(board,this, candidateDestinationCoordinate));
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMove);
	}
	public String toString(){
		 return PieceType.PAWN.toString();
	 }
	@Override
	public Pawn movePieces( final Move move) {
		return new Pawn(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
	}
}
