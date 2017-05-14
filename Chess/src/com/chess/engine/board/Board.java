package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.Alliance;
import com.chess.engine.piece.Bishop;
import com.chess.engine.piece.King;
import com.chess.engine.piece.Knight;
import com.chess.engine.piece.Pawn;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Queen;
import com.chess.engine.piece.Rook;
import com.chess.engine.player1.BlackPlayer;
import com.chess.engine.player1.Player;
import com.chess.engine.player1.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Board {

	private final List<Tile> gameboard;
	private final Collection<Piece> whitePiece;
	private final Collection<Piece> blackPiece;
	
	
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	
	private Board(Builder builder){
		this.gameboard = createBoard(builder);
		this.whitePiece = calculateActivePieces(this.gameboard,Alliance.WHITE);
		this.blackPiece = calculateActivePieces(this.gameboard,Alliance.BLACK);
		
		final Collection<Move> whiteStandardMoves = calculateLegalMoves(this.whitePiece);
		final Collection<Move> blackStandardMoves =  calculateLegalMoves(this.blackPiece);
		
		this.whitePlayer = new WhitePlayer(this, whiteStandardMoves,blackStandardMoves);
		this.blackPlayer = new BlackPlayer(this, whiteStandardMoves,blackStandardMoves);
		this.currentPlayer=builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
		
	} 
	
	
	
	@Override
	public String toString() {
			final StringBuilder builder = new StringBuilder();
			for(int i=0;i<BoardUtils.NUM_TILES;i++){
				final String tileText = this.gameboard.get(i).toString();
				builder.append(String.format("%3s",tileText));
				if((i+1)%BoardUtils.NUM_TILES_PER_ROW==0){
					builder.append("\n");
				}
				
			}
			return builder.toString();
	}

	
	
	public Player whitePlayer(){
		return this.whitePlayer;
	}
	
	public Player blackPlayer(){
		return this.blackPlayer;
	}
	
	public Player currentPlayer(){
		return this.currentPlayer;
	}
	
	private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
		
		final List<Move> legalMove = new ArrayList<>();
		
		for(final Piece piece: pieces){
			legalMove.addAll(piece.calculateIlegalMove(this));
		}
		
		return ImmutableList.copyOf(legalMove);
	}

	private static Collection<Piece> calculateActivePieces(final List<Tile> gameboard2,
			final Alliance alliance) {

		final List<Piece> activePiece = new ArrayList<>();
		
		for(final Tile tile : gameboard2){
			final Piece piece = tile.getPiece();
			if(piece.getPieceAlliance() == alliance){
				activePiece.add(piece);
			}
			
		}
		
		
		return ImmutableList.copyOf(activePiece);
	}

	public Tile getTile(final int tileCoordinate){
		return gameboard.get(tileCoordinate);
	}
	
	public Collection<Piece> getBlackPieces(){
		return this.blackPiece;
	}
	
	public Collection<Piece> getWhitePieces(){
		return this.whitePiece;
	}
	
	
	private static List<Tile>  createBoard(final Builder builder){
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
		for(int i=0;i<BoardUtils.NUM_TILES;i++){
			tiles[i] = Tile.createTile(i,builder.boardConfig.get(i));
		}
		///ERROR BECAUSE  i have indexx 16 , who is null.Why?
		return ImmutableList.copyOf(tiles);
	}
	
	public static Board createStandardBoard(){
		final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Rook(63, Alliance.WHITE));
        //white to move
        builder.setNextMoveMaker(Alliance.WHITE);
        //build the board
        return builder.build();
	}
	
	public static class Builder{
		
		Map<Integer, Piece> boardConfig;
		Alliance nextMoveMaker;
		Pawn enPassantPawn;
		
		public Builder(){
			this.boardConfig = new HashMap<>();
		}
		
		public Builder setPiece(final Piece piece){
			this.boardConfig.put(piece.getPiecePosition(),piece);
			return this;
		}
		
		public Builder setNextMoveMaker(final Alliance nextMoveMaker) {
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}

		public Board build(){
			return new Board(this);
		}

		public void setEnPassantPawn(Pawn enPassantPawn) {
			this.enPassantPawn=enPassantPawn;
			
		}
		
	}

	public Iterable<Move> getAllLegalMove() {
		return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMove(),
															   this.blackPlayer.getLegalMove()));
	}
	
}
