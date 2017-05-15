package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.piece.Piece;
import com.chess.engine.player1.MoveTransition;
import com.google.common.collect.Lists;

public class Table {
	
	
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private Board chessBoard;
	private BoardDirection boardDirection;
	private boolean highLightLegalMoves;  
	private final MoveLog moveLog;

	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
	private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
	private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
	private static String defultPieceImagesPath = "art/";

	
	private final  Color ligthTileColor = Color.decode("#FFFACD");
	private final  Color darkTileColor = Color.decode("#593A3A");

	
	public Table(){
		
		this.gameFrame = new JFrame("Jchess");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar = createTableMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.chessBoard = Board.createStandardBoard();
		this.boardPanel = new BoardPanel();
		this.highLightLegalMoves = false;
		this.moveLog = new MoveLog();
		this.boardDirection = BoardDirection.NORMAL;
		this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
		this.gameFrame.setVisible(true);
	}

	private JMenuBar createTableMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();	
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferencesMenu());
		
		return tableMenuBar;
	}

	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem openPNG = new JMenuItem("Load PNG File");
		
		openPNG.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Open png file now!");
			}
		});
		
		fileMenu.add(openPNG);
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		
		exitMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
				
			}
		});
		fileMenu.add(exitMenuItem);
		return fileMenu;
	}
	
	private class BoardPanel extends JPanel{
		final List<TilePanel> boardTiles;
		
		public BoardPanel(){
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<>();
			for(int i=0;i<BoardUtils.NUM_TILES;i++){
				final TilePanel tilePanel = new TilePanel(this,i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}

		public void drawBoard(final Board board) {
			removeAll();
			
			for(final TilePanel tilePanel: boardDirection.traverse(boardTiles)){
				tilePanel.drawTile(board);
				add(tilePanel);
			}
			validate();
			repaint();
		}
		
		
	}
	
	private JMenu createPreferencesMenu(){
		
		final JMenu  preferencesMenu = new JMenu("Preferences");
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
		flipBoardMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				boardDirection = boardDirection.opposite();
				boardPanel.drawBoard(chessBoard);
			}
		});
		preferencesMenu.add(flipBoardMenuItem);
		preferencesMenu.addSeparator();
		
		final JCheckBoxMenuItem legalMoveHighLigteCheckBox = new JCheckBoxMenuItem("Highlight Legal Moves" , false);
		
		legalMoveHighLigteCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				highLightLegalMoves = legalMoveHighLigteCheckBox.isSelected();
				
			}
		});
		
		preferencesMenu.add(legalMoveHighLigteCheckBox);
		
		
		return preferencesMenu;
		
	}
	
    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();

    }
    public static class MoveLog {

        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        public void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        public void clear() {
            this.moves.clear();
        }

        public Move removeMove(int index) {
            return this.moves.remove(index);
        }

        public boolean removeMove(final Move move) {
            return this.moves.remove(move);
        }

    }
	
	private class TilePanel extends JPanel{
		
		private final int tileId;
		
		public TilePanel(final BoardPanel boardPanel,final int tileId) {
				super(new GridLayout());
				this.tileId = tileId;
				setPreferredSize(TILE_PANEL_DIMENSION);
				assignTileColor();
				assignTilePieceIcon(chessBoard);
				validate();
				
				addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(final MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mousePressed(final MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseExited(final MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseEntered(final MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseClicked(final MouseEvent e) {

						if(isRightMouseButton(e)){
							sourceTile =null;
							destinationTile=null;
							humanMovedPiece = null;						
						}else if(isLeftMouseButton(e)){
							if(sourceTile ==null){
								//first click
								sourceTile = chessBoard.getTile(tileId);
								humanMovedPiece = sourceTile.getPiece();
								if(humanMovedPiece==null){
									sourceTile=null;
								}
							}else{
								destinationTile = chessBoard.getTile(tileId);
								final Move move = Move.moveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(),
																			  destinationTile.getTileCoordinate());
								final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
								if(transition.getMoveStatus().isDone()){
									chessBoard =transition.getTransitionBoard();
									moveLog.addMove(move);
								}
								sourceTile =null;
								destinationTile = null;
								humanMovedPiece =null;								
							}
							invokeLater(new Runnable() {
								
								@Override
								public void run() {
									boardPanel.drawBoard(chessBoard);
									
									
								}
							});
						}
						validate();

					}
				});
		}
		
		private void highlightLegals(final Board board) {
            if (highLightLegalMoves){///Table.get().getHighlightLegalMoves()) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/dot/green_dot.png")))));
                        }
                        catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
		
		  private Collection<Move> pieceLegalMoves(final Board board) {
	            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
	                return humanMovedPiece.calculateIlegalMove(board);
	            }
	            return Collections.emptyList();
	        }
		
		public void drawTile(final Board board){
			assignTileColor();
			assignTilePieceIcon(board);
			validate();
			repaint();
		}
		private void assignTilePieceIcon(final Board board){
			this.removeAll();
			if(board.getTile(this.tileId).isTileOccupied()){
				try {
					final BufferedImage image = ImageIO.read(new File(defultPieceImagesPath + 
																	  board.getTile(this.tileId).getPiece().
																	  getPieceAlliance().toString().substring(0,1)+
																	  board.getTile(this.tileId).getPiece().toString()+ ".gif"));
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

		private void assignTileColor() {
				if(BoardUtils.EIGTH_ROW[this.tileId] ||
				   BoardUtils.SIXTH_ROW[this.tileId] || 
				   BoardUtils.FOURTH_ROW[this.tileId] ||
				   BoardUtils.SECOND_ROW[this.tileId]){
				setBackground(this.tileId%2==0 ? ligthTileColor : darkTileColor);
				
				}else if(BoardUtils.SEVEN_ROW[this.tileId] ||
						 BoardUtils.FIFTH_ROW[this.tileId] || 
						 BoardUtils.THIRD_ROW[this.tileId] ||
						 BoardUtils.FIRST_ROW[this.tileId]){
					setBackground(this.tileId%2!=0 ? ligthTileColor : darkTileColor);

				}
		}
		
	}

}
