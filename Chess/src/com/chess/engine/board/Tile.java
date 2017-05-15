
package com.chess.engine.board;


import com.chess.engine.piece.Piece;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public abstract class Tile {
    protected int tileCoordinate;
    public static final class EmptyTile extends Tile{
        int coordinate;
        private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();
        //private static final Table<Integer, Piece, OccupiedTile> OCCUPIED_TILES = createAllPossibleOccupiedTiles();
        EmptyTile(final int coordinate){
            super(coordinate);
        }
        @Override
		public String toString() {
        		return "-";
        }
		public boolean isTileOccupied(){
            return  false;
        }
        public Piece getPiece() {
            return null;
        }
    }
    private static final Map<Integer, EmptyTile> EMPTY_TITLES_CACHE = createAllPossbleEmptyTile();
    private static Map<Integer,EmptyTile> createAllPossbleEmptyTile() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        for(int i =0;i<BoardUtils.NUM_TILES;i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }
        Collections.unmodifiableMap(emptyTileMap);
        return ImmutableMap.copyOf(emptyTileMap);
    }
    
    //TODO Problems why?
    public static Tile createTile(final int tileCoordinate, final Piece piece){
    	return piece !=null? new OccupeidTile(tileCoordinate, piece): EMPTY_TITLES_CACHE.get(tileCoordinate);
    }
    
    
    private Tile(final int titleCoordinate){
        this.tileCoordinate = tileCoordinate;
    }
    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }
    public abstract  boolean isTileOccupied();
    public abstract Piece getPiece();
    public static final class OccupeidTile extends  Tile {
        private final Piece pieceOnTile;
        private OccupeidTile(int coordinate,final Piece pieceOnTile){
            super(coordinate);
            this.pieceOnTile=pieceOnTile;
        }
        public boolean isTileOccupied(){
            return  true;
        }
        @Override
		public String toString() {
			return getPiece().getPieceAlliance().isBlack()? getPiece().toString().toLowerCase() :
				   getPiece().toString() ;
		}
		public Piece getPiece() {
            return this.pieceOnTile;
        }
    }
    public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile;
        private OccupiedTile(final int coordinate,
                             final Piece pieceOnTile) {
            super(coordinate);
            this.pieceOnTile = pieceOnTile;
        }
        @Override
        public String toString() {
            return this.pieceOnTile.getPieceAlliance().isWhite() ?
                   this.pieceOnTile.toString() :
                   this.pieceOnTile.toString().toLowerCase();
        }
        @Override
        public boolean isTileOccupied() {
            return true;
        }
        @Override
        public Piece getPiece() {
            return pieceOnTile;
        }
    }
    public int getTileCoordinate() {
		return this.tileCoordinate;
	}
}
