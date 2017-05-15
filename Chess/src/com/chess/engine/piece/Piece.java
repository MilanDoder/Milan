package com.chess.engine.piece;

import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.*;


public abstract class Piece {
	protected final PieceType pieceType;
	protected final int piecePositions;
	protected final Alliance pieceAlliance;
	protected final boolean isFirstMove;
	private final int cachedHashCode;
	protected Piece(final PieceType pieceType,int piecePositions, Alliance pieceAlliance) {
		this.pieceType= pieceType;
		this.piecePositions = piecePositions;
		this.pieceAlliance = pieceAlliance;
		this.isFirstMove=false;
		this.cachedHashCode = computeHashCode();
	}
	private int computeHashCode() {
		int result = pieceType.hashCode();
		result = 31 * result +pieceAlliance.hashCode();
		result = 31 * result + piecePositions;
		result = 31 * result + (isFirstMove? 1:0);
		return result;
	}
	@Override
	public int hashCode() {
		return this.cachedHashCode;
	}
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;	
		if (!(obj instanceof Piece))
			return false;
		final Piece other = (Piece) obj;
		return piecePositions == other.getPiecePosition() && 
			   other.getPieceType()==pieceType &&
			   pieceAlliance == other.getPieceAlliance() && 
			   isFirstMove == other.isFirstMove();
	}
	public int getPiecePosition(){
		return this.piecePositions;
	}
	public Alliance getPieceAlliance(){
		return this.pieceAlliance;
	}
	public PieceType getPieceType(){
		return this.pieceType;
	}
	public boolean isFirstMove(){
		return this.isFirstMove;
	}
	public abstract Collection<Move> calculateIlegalMove (final Board board);
	public abstract Piece movePieces(Move move);
	public enum PieceType{
		PAWN("P") {
			@Override
			public boolean isKing() {
				return false;
			}
			@Override
			public boolean isRook() {
				return false;
			}
		},
		KNIGHT("N") {
			@Override
			public boolean isKing() {
				return false;
			}
			@Override
			public boolean isRook() {
				return false;
			}
		},
		BISHOP("B") {
			@Override
			public boolean isKing() {
				return false;
			}
			@Override
			public boolean isRook() {
				return false;
			}
		},
		ROOK("R") {
			@Override
			public boolean isKing() {
				return false;
			}
			@Override
			public boolean isRook() {
				return true;
			}
		},
		QUEEN("Q") {
			@Override
			public boolean isKing() {
				return false;
			}
			@Override
			public boolean isRook() {
				return false;
			}
		},
		KING("K") {
			@Override
			public boolean isKing() {
				return true;
			}
			@Override
			public boolean isRook() {
				return false;
			}
		};		
		private String pieceName;
		 PieceType(final String pieceName){
			 this.pieceName= pieceName;
		 }
		 public String toString(){
			 return this.pieceName;
		 }
		 public abstract boolean isKing();
		 public abstract boolean isRook();	
	}
}