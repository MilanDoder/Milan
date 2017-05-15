package com.chess.engine.board;

import org.omg.CosNaming.IstringHelper;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.piece.Pawn;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;

public abstract class Move {
	final Board board;
	final Piece movedPiece;
	final int destinationCoordinate;
	public static final Move NULL_MOVE = new NullMove();
	protected Move(Board board, Piece movedPiece, int destinationCoordinate) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCoordinate = destinationCoordinate;
	}
	public Board excute() {
		final Board.Builder builder = new Builder();
		for(final Piece piece : this.board.currentPlayer().getActivePieces()){
			if(!this.movedPiece.equals(piece)){
				builder.setPiece(piece);
			}
		}
		for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
			builder.setPiece(piece);
		}
		builder.setPiece(this.movedPiece.movePieces(this));
		builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());	
		return builder.build();
	}
	public int hashCode(){
		final int prime =31;
		int result =1;
		result = prime * result + this.destinationCoordinate;
		result = prime * result + this.movedPiece.hashCode();
		return result;
	}
	public boolean equals(final Object other){
		if(this==other){
			return true;
		}
		if(!(other instanceof Move)){
			return false;
		}
		final Move otherMove = (Move) other;
		return getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
			   getMovedPiece().equals(otherMove.getMovedPiece());	
	}
	public int getCurrentCoordinate(){
		return this.getMovedPiece().getPiecePosition();
	}
	public int getDestinationCoordinate(){
		return this.destinationCoordinate;
	}	
	public Piece getMovedPiece(){
		return this.movedPiece;
	}
	public boolean isAttack(){
		return false;
	}
	public boolean isCastlingMove(){
		return false;
	}
	public Piece getAttackedPiece(){
		return null;	
	}
	public static final class MajorMove extends Move{
		public MajorMove(final Board board,final Piece movedPiece,
				int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}
	}
	public static class AttackMove extends Move {
		final Piece attackedPiece;
		public AttackMove(final Board board,
				final Piece movedPiece,
				int destinationCoordinate,final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate);
			this.attackedPiece=attackedPiece;
		}		
		public int HashCode(){
			return this.attackedPiece.hashCode() + super.hashCode();
		}
		public boolean equals(final Object other){
			if(this==other){
				return true;
			}		
			if(!(other instanceof AttackMove)){
				return false;
			}	
			final AttackMove otherAttackedMove =  (AttackMove) other;
			return super.equals(otherAttackedMove) &&
				   getAttackedPiece().equals(otherAttackedMove.getAttackedPiece());
		}
		public Board excute(){
			return null;
		}
		public boolean isAttack(){
			return true;
		}
		public Piece getAttackedPiece(){
			return this.attackedPiece;
		}
	}
	public static final class PawnMove extends Move {
		public PawnMove(final Board board,
				final Piece movedPiece,
				final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}	
	}
	public static class PawnAttackMove extends AttackMove {	
		public PawnAttackMove(final Board board,
				final Piece movedPiece,
				int destinationCoordinate,final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate,attackedPiece);
		}	
	}
	public static final class PawnEnPassantAttackMove extends PawnAttackMove {
		public PawnEnPassantAttackMove(final Board board,
									   final Piece movedPiece,
									   int destinationCoordinate,
									   final Piece attackedPiece) {
				super(board, movedPiece, destinationCoordinate,attackedPiece);
		}	
	}
	public static final class PawnJump extends Move {
		public PawnJump(final Board board,
						final Piece movedPiece,
						int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}		
		public Board excute(){
			final Builder builder = new Builder();
			for(final Piece piece: this.board.currentPlayer().getActivePieces()){
				
				if(!(this.movedPiece.equals(piece))){
					builder.setPiece(piece);
				}
			}
			for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
				builder.setPiece(piece);
			}
			final Pawn movedPawn = (Pawn)  this.movedPiece.movePieces(this);
			builder.setPiece(movedPawn);
			builder.setEnPassantPawn(movedPawn);
			builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
	}
	static abstract class CastleMove extends Move{
		protected final Rook castleRook;
		protected final int castleRookStart;
		protected final int castleRookDestination;
		public CastleMove(final Board board,
						  final Piece movedPiece,
						  int destinationCoordinate,
						  final Rook castleRook,
						  final int castleRookStart,
						  final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate);
			this.castleRook = castleRook;
			this.castleRookStart = castleRookStart;
			this.castleRookDestination = castleRookDestination;
		}
		public Rook getCasleRook(){
			return this.castleRook;
		}
		public boolean isCastlingMove(){
			return true;
		}
		public Board excute(){
			final Builder builder = new Builder();
			for(final Piece piece: this.board.currentPlayer().getActivePieces()){		
				if(!(this.movedPiece.equals(piece))&& !castleRook.equals(piece)){
					builder.setPiece(piece);
				}
			}
			for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
				builder.setPiece(piece);
			}
			builder.setPiece(this.movedPiece.movePieces(this));
			builder.setPiece(new Rook(this.castleRookDestination,this.castleRook.getPieceAlliance()));
			builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
	}
	public static final class KingSideCastleMove extends CastleMove {
		public KingSideCastleMove(final Board board,
								  final Piece movedPiece,
								  int destinationCoordinate,
								  final Rook castleRook,
								  final int castleRookStart,
								  final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
		}		
		@Override
		public String toString(){
			return "0-0";
		}
	}
	public static final class QueenSideCastleMove extends CastleMove {
		public QueenSideCastleMove(final Board board,
								   final Piece movedPiece,
								   int destinationCoordinate,
								   final Rook castleRook,
								   final int castleRookStart,
								   final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
		}		
		public String toString(){
			return "0-0-0";
		}
	}
	public static final class NullMove extends Move {
		public NullMove() {
			super(null,null,-1);
		}		
		public Board excute(){
			throw new RuntimeException("cannot excute the null move");
		}
	}
	public static class moveFactory{	
		private moveFactory(){
			throw new RuntimeException("Not instantiable!");
		}
		public static Move createMove(final Board board,
									final int currentCoordinate,
									final int destinationCoordinate){	
			for(final Move move: board.getAllLegalMove()){
				if(move.getCurrentCoordinate()== currentCoordinate &&
						move.getDestinationCoordinate()==destinationCoordinate){
					return move;
				}
			}
			return NULL_MOVE;
 		}
	}
}