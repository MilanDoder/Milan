package com.chess.engine.player1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.piece.King;
import com.chess.engine.piece.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class Player {
	  protected final Board board;
	  protected final King playerKing;
	  protected final Collection<Move> legalMoves;
	  private final boolean isInCheck;
	  public Player(Board board, Collection<Move> legalMoves, Collection<Move> opponetMoves) {
			//super();
			this.board = board;
			this.playerKing = establishKing();
			this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,calculateKingCastle(legalMoves, opponetMoves)));
			this.isInCheck= !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),opponetMoves).isEmpty();
	  }
	  protected static Collection<Move> calculateAttacksOnTile(int piecePosition,
			Collection<Move> moves) {
			 final List<Move> attackMoves = new ArrayList<>();	 
			 for(final Move move: moves){
				 if(piecePosition == move.getDestinationCoordinate()){
					 attackMoves.add(move);
				 }
			 }
			 return ImmutableList.copyOf(attackMoves);
	  }
	  public King getPlayerKing(){
			 return this.playerKing;
	  } 
	  public Collection<Move> getLegalMove(){
		  return this.legalMoves;
	  } 
	  protected King establishKing() {
		  for(final Piece piece : getActivePieces()) {
			  if(piece.getPieceType().isKing()) {
				  return (King) piece;
			  }
		  }
		  throw new RuntimeException("Should not reach here! "
				  +this.getAlliance()+ " king could not be established!");
	  } 
	  public boolean isMoveLegal(final Move move){
		  return this.legalMoves.contains(move);
	  }
	  public boolean isInCheck(){
		  return this.isInCheck;
	  }
	  public boolean isInCheckMate(){
		  return this.isInCheck && !hasEscapeMoves();
	  }
	  public boolean isInStaleMate(){
		  return !this.isInCheck && !hasEscapeMoves();
	  }
	  protected boolean hasEscapeMoves() {
		  for(final Move move : this.legalMoves){
			  final MoveTransition transition = makeMove(move);
			  if(transition.getMoveStatus().isDone()){
				  return true;
			  }
		  }
		  return false;
	  } 	
	  public boolean isCastled(){
		  return false;
	  } 	
	  public MoveTransition makeMove(final Move move){
		  if(!isMoveLegal(move)){
			  return new MoveTransition(this.board, move,MoveStatus.ILLEGAL_MOVE);
		  }
		  final Board transitionBoard = move.excute();		
		  final Collection<Move>  kingAttack = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().
		 				getOpponent().getPlayerKing().getPiecePosition(),transitionBoard.currentPlayer().getLegalMove());
		 		
		  if(!kingAttack.isEmpty()){
			  return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		  }
		  return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	  } 	
	  public abstract Collection<Piece> getActivePieces();
	  public abstract Alliance getAlliance();
	  public abstract Player getOpponent();
	  protected abstract Collection<Move> calculateKingCastle(Collection<Move> playerLegel, Collection<Move> opponentsLegals);
}
