package CheckersClasses;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.InputMismatchException;
//Scanner for the external input.
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.Random;

/**
 * This panel lets two users play checkers against each other.
 * Red always starts the game.  If a player can jump an opponent's
 * piece, then the player must jump.  When a player can make no more
 * moves, the game ends.
 *
 * The class has a main() routine that lets it be run as a stand-alone
 * application.  The application just opens a window that uses an object
 * of type Checkers as its content pane.
 * 
 * Update 03/18: adapt to the changes in CheckersMove.
 * 
 */
public class Checkers extends JPanel {

    /**
     * Main routine makes it possible to run Checkers as a stand-alone
     * application.  Opens a window showing a Checkers panel; the program
     * ends when the user closes the window.
     */
	//AIKEY
	static int aiKey = 0;
	//To demonstrate previous board
    static boolean chengeValue = false;


	public static void main(String[] args) {
		System.out.println("A Checker-Playing Agent");
		// 1: use alpha-beta pruning throughout the game.
		// 2: use Monte Carlo tree search throughout the game.
		// 3: randomly choose between alpha-beta and MCTS to decide on the next move. 
		System.out.println("keys: 1 (alpha-beta)  2 (MCTS)  3 (random)\n");
        /*long legalSpots = 0b0101010110101010010101011010101001010101101010100101010110101010L;
        long illegalSpots = 0b1010101001010101101010100101010110101010010101011010101001010101L;
        long blackbb =      0b0101010110101010010101010000000000000000000000000000000000000000L;
        long redbb =        0b0000000000000000000000001000100000000000101010100101010110101010L;
        long LegalJumpSpots=0b0000000000101010010101000010101001010100001010100101010000000000L;
        //long move =0b0000000000000000000000000000000000000000000000000000000000000000000000000L;
        //System.out.println(CheckersData.LongToString(LegalJumpSpots));
        //System.out.println(CheckersData.LongToString((blackbb )));
        //System.out.println(CheckersData.LongToString((legalSpots ^ (blackbb | redbb))));
        //System.out.println(CheckersData.LongToString(blackbb | redbb));

        long m = blackbb >>> 7;
        //System.out.println(CheckersData.LongToString(m));
        //System.out.println(CheckersData.LongToString( redbb));
        //System.out.println(CheckersData.LongToString(((redbb)>>> 7) & legalSpots));
        //System.out.println(CheckersData.LongToString(m & (legalSpots ^ (blackbb | redbb))));
        //System.out.println(CheckersData.LongToString(m));

        CheckersData d = new CheckersData();
        System.out.println(CheckersData.LongToString(d.topBitBoard | d.bottomBitBoard));
        for(long movelong:  d.getLegalMoves2(CheckersData.RED)){
            //System.out.println(CheckersData.LongToString((movelong <<1) & LegalJumpSpots));
            System.out.println(CheckersData.LongToString((movelong ) & legalSpots));
        }*/



        JFrame window = new JFrame("Checkers");
        
        
        Checkers content = new Checkers();
        window.setContentPane(content);
        
        window.pack();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation( (screensize.width - window.getWidth())/2,
                (screensize.height - window.getHeight())/2 );
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        window.setResizable(false);
        window.setVisible(true);




    }

    private JButton newGameButton;  // Button for starting a new game.
    private JButton resignButton;   // Button that a player can use to end
    // the game by resigning.

    private JLabel message;  // Label for displaying messages to the user.
    private static JLabel premessage;
    
    //Previous display
    static PreBoard previous = new PreBoard(); // Display previous board
    /**
     * The constructor creates the Board (which in turn creates and manages
     * the buttons and message label), adds all the components, and sets
     * the bounds of the components.  A null layout is used.  (This is
     * the only thing that is done in the main Checkers class.)
     */
    public Checkers() {

        setLayout(null);  // I will do the layout myself.
        //setPreferredSize( new Dimension(350,250) );
        setPreferredSize( new Dimension(550,250) );
        setBackground(new Color(0,150,0));  // Dark green background.

        /* Create the components and add them to the applet. */

        Board board = new Board();  // Note: The constructor for the
        //   board also creates the buttons
        //   and label.
        /*
         * Previous board
         */
        add(board);
        add(previous);
        
        add(newGameButton);
        add(resignButton);
        add(message);
        add(premessage);

      /* Set the position and size of each component by calling
       its setBounds() method. */

        //board.setBounds(20,20,164,164); // Note:  size MUST be 164-by-164 !
        previous.setBounds(20,20,164,164);
        board.setBounds(230,20,164,164);
        
        //previous.setBounds(20,20,164,164);
        newGameButton.setBounds(400, 60, 120, 30);
        resignButton.setBounds(400, 120, 120, 30);
        message.setBounds(140, 200, 350, 30);
        premessage.setBounds(40, 200, 350, 30);

        
        
    } // end constructor
 
    /**
     * This panel displays a 160-by-160 checkerboard pattern with
     * a 2-pixel black border.  It is assumed that the size of the
     * panel is set to exactly 164-by-164 pixels.  This class does
     * the work of letting the users play checkers, and it displays
     * the checkerboard.
     */
    public static class PreBoard extends JPanel{
    	CheckersData preBoard;
    	long moveAI;
    	//setBackground(Color.BLACK);
    	//boolean gameInProgress=true;
    	PreBoard()
    	{
    		premessage = new JLabel("",JLabel.LEFT);
    		premessage.setFont(new  Font("Serif", Font.BOLD, 14));
    		premessage.setForeground(Color.green);
            premessage.setText("Initialization");
            preBoard = new CheckersData();
            preBoard.setUpGame();
            moveAI = 0;
            repaint();
    	}

        public void drawBoard(CheckersData currentBoard, long move)
        {
            premessage = new JLabel("",JLabel.LEFT);
            premessage.setFont(new  Font("Serif", Font.BOLD, 14));
            premessage.setForeground(Color.green);
            premessage.setText("Agent to Play");
            preBoard = copyBoard(currentBoard);
            moveAI = move;
            repaint();
        }
    	private CheckersData copyBoard(CheckersData board)
        {
            this.preBoard = board;
            CheckersData new_board = new CheckersData();
            new_board.kingBitBoard = board.kingBitBoard;
            new_board.bottomBitBoard = board.bottomBitBoard;
            new_board.topBitBoard = board.topBitBoard;
            return new_board;
        }
    	
    	public void paintComponent(Graphics g) {
            /* Draw a two-pixel black border around the edges of the canvas. */
            g.setColor(Color.black);
            g.drawRect(0,0,getSize().width-1,getSize().height-1);
            g.drawRect(1,1,getSize().width-3,getSize().height-3);
            /* Draw the squares of the checkerboard and the checkers. */
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if ( row % 2 == col % 2 )
                        g.setColor(Color.LIGHT_GRAY);
                    else
                        g.setColor(Color.GRAY);
                    g.fillRect(2 + col*20, 2 + row*20, 20, 20);
                    switch (preBoard.pieceAt(row,col)) {
                        case CheckersData.RED:
                            g.setColor(Color.RED);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            break;
                        case CheckersData.BLACK:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            break;
                        case CheckersData.RED_KING:
                            g.setColor(Color.RED);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("K", 7 + col*20, 16 + row*20);
                            break;
                        case CheckersData.BLACK_KING:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("K", 7 + col*20, 16 + row*20);
                            break;
                    }
                }
            }
            
            // paint AI move on the left board
            if(moveAI !=0)
            {
                long m2;
                long moveAI2 = moveAI & CheckersData.valid;
            	g.setColor(Color.green);
            	while (moveAI2 !=0)
            	{
                    m2 = moveAI2 & -moveAI2;
                    moveAI2 ^= m2;
                    int pos = CheckersData.getBitPosition(m2);
            		g.drawRect(2 + ((pos%8) * 20), 2 + ((pos/8) * 20), 19, 19);
                    g.drawRect(3 + ((pos%8) * 20), 3 + ((pos/8) * 20), 17, 17);
            	}
            }
            
        }  // end paintComponent()
    }
    
    private class Board extends JPanel implements ActionListener, MouseListener {
        CheckersData board;  // The data for the checkers board is kept here.
        //    This board is also responsible for generating
        //    lists of legal moves.
        boolean gameInProgress; // Is a game currently in progress?
        /* The next three variables are valid only when the game is in progress. */
        int currentPlayer;      // Whose turn is it now?  The possible values
        //    are CheckersData.RED and CheckersData.BLACK.
        int selectedRow, selectedCol;  // If the current player has selected a piece to
        //     move, these give the row and column
        //     containing that piece.  If no piece is
        //     yet selected, then selectedRow is -1.
        long[] legalMoves;  // An array containing the legal moves for the
        //   current player.
        AdversarialSearch player_1; // AI player, Alpha-beta
        AdversarialSearch player_2; // MCTS
        /**
         * Constructor.  Create the buttons and label.  Listens for mouse
         * clicks and for clicks on the buttons.  Create the board and
         * start the first game.
         */
        CheckersData displayBoard;
        CheckersData agentBoard;
        
        Board() {
            setBackground(Color.BLACK);
            addMouseListener(this);
            resignButton = new JButton("Resign");
            resignButton.addActionListener(this);
            newGameButton = new JButton("New Game");
            newGameButton.addActionListener(this);
            message = new JLabel("",JLabel.CENTER);
            message.setFont(new  Font("Serif", Font.BOLD, 14));
            message.setForeground(Color.green);
            board = new CheckersData();
            //Display board
            displayBoard = new CheckersData();
            agentBoard = new CheckersData();
            //Select the AI players.
            decideAIplayer();
            //Start new game
            doNewGame();
        }

        public void decideAIplayer()
        {
        	Scanner stdin = new Scanner(System.in);
    		boolean done = false;
    		player_1 = new AlphaBetaSearch();
        	player_2 = new MonteCarloTreeSearch();
            while (!done) {
                try {
                	int aikey = stdin.nextInt();
                    if (aikey==1) {done = true; aiKey = 1;}
                    else if(aikey==2) 
                    { done =true; aiKey= 2;}
                    else if(aikey==3) 
                    {
                    	done =true; aiKey= 3;}
                    else {
                        System.out.println("\tThe entered number should be (1-3)");
                    }
                }
                catch (InputMismatchException e) {
                    System.out.println("\tInvalid input type (must be an integer)");
                    stdin.nextLine();  // Clear invalid input from scanner buffer.
                }
            }
        }
        /**
         * Respond to user's click on one of the two buttons.
         */
        public void actionPerformed(ActionEvent evt) {
            Object src = evt.getSource();
            if (src == newGameButton)
                doNewGame();
            else if (src == resignButton)
                doResign();
        }


        /**
         * Start a new game
         */
        void doNewGame() {
            if (gameInProgress) {
                // This should not be possible, but it doesn't hurt to check.
                message.setText("Finish the current game first!");
                return;
            }
            board.setUpGame();   // Set up the pieces.
            
            //
            displayBoard.setUpGame(); // S_R, Set up the pieces.
            agentBoard.setUpGame(); // S_L
            //
            currentPlayer = CheckersData.RED;   // RED moves first.
            player_1.setCheckersData(board);
            player_2.setCheckersData(board);
            legalMoves = board.getLegalMovesPartial(CheckersData.RED);
            if (legalMoves.length == 0 || legalMoves[0] == 0) {
                legalMoves = board.getLegalMoves2(CheckersData.RED);  // Get RED's legal moves.
            }
            /*for(CheckersMove m:  legalMoves){
                System.out.println(m);
            }*/
            selectedRow = -1;   // RED has not yet selected a piece to move.
            message.setText("Red:  Make your move.");
            gameInProgress = true;
            newGameButton.setEnabled(false);
            resignButton.setEnabled(true);
            
            ///
            previous.drawBoard(agentBoard, 0);
            ////
            repaint();
        }


        /**
         * Current player resigns.  Game ends.  Opponent wins.
         */
        void doResign() {
            if (!gameInProgress) {  // Should be impossible.
                message.setText("There is no game in progress!");
                return;
            }
            if (currentPlayer == CheckersData.RED)
                gameOver("RED resigns.  BLACK wins.");
            else
                gameOver("BLACK resigns.  RED wins.");
            	//previous.drawBoard();
        }


        /**
         * The game ends.  The parameter, str, is displayed as a message
         * to the user.  The states of the buttons are adjusted so players
         * can start a new game.  This method is called when the game
         * ends at any point in this class.
         */
        void gameOver(String str) {
            message.setText(str);
            newGameButton.setEnabled(true);
            resignButton.setEnabled(false);
            gameInProgress = false;
            //Previous state
            premessage.setText("Game is done");
        }


        /**
         * This is called by mousePressed() when a player clicks on the
         * square in the specified row and col.  It has already been checked
         * that a game is, in fact, in progress.
         */
        void doClickSquare(int row, int col) {

            /* If the player clicked on one of the pieces that the player
               can move, mark this row and col as selected and return.  (This
               might change a previous selection.)  Reset the message, in
               case it was previously displaying an error message. */
            long position = 0x8000000000000000L >>>(row*8 + col);
            long currentplayerboard = (currentPlayer == CheckersData.BLACK)? board.topBitBoard:board.bottomBitBoard;
            for (long legalMove : legalMoves) {
                if ((currentplayerboard & legalMove & position) != 0) {
                    selectedRow = row;
                    selectedCol = col;
                    if (currentPlayer == CheckersData.RED)
                        message.setText("RED:  Make your move.");
                    else
                        message.setText("BLACK:  Make your move.");
                    repaint();
                    return;
                }
            }

            /* If no piece has been selected to be moved, the user must first
               select a piece.  Show an error message and return. */
            if (selectedRow < 0) {
                message.setText("Click the piece you want to move.");
                return;
            }
            long position2 = 0x8000000000000000L >>>(selectedRow*8 + selectedCol);
            /* If the user clicked on a square where the selected piece can be
               legally moved, then make the move and return. */
            long unoccupiedspots = CheckersData.valid ^ (board.topBitBoard | board.bottomBitBoard);
            for (long legalMove : legalMoves) {
                if ((legalMove & position & unoccupiedspots) != 0 && (currentplayerboard & legalMove & position2) != 0) {
                    if ((legalMove & CheckersData.invalid)!=0)
                    {
                        doMakePartialMove(legalMove);
                        return;
                    }
                    doMakeMove(legalMove);
                    return;
                }
            }

         /* If we get to this point, there is a piece selected, and the square where
          the user just clicked is not one where that piece can be legally moved.
          Show an error message. */

            message.setText("Click the square you want to move to.");

        }  // end doClickSquare()

        void doMakePartialMove(long move)
        {
            board.makeMove(move);
            legalMoves = board.getLegalMovesPartial(currentPlayer);
            displayBoard = copyBoard(board);
            //Add time
/* Set selectedRow = -1 to record that the player has not yet selected
               a piece to move. */
            selectedRow = -1;

            /* As a courtesy to the user, if all legal moves use the same piece, then
               select that piece automatically so the user won't have to click on it
               to select it. */
            if (legalMoves != null && legalMoves.length != 0 && legalMoves[0] != 0) {
                long StartSquare = (currentPlayer == CheckersData.BLACK)? board.topBitBoard:board.bottomBitBoard;
                for (int i = 0; i < legalMoves.length; i++)
                {
                    StartSquare = StartSquare & legalMoves[i];
                    if (StartSquare == 0) break;
                }
                if (StartSquare != 0) {
                    int pos = CheckersData.getBitPosition(StartSquare);
                    selectedRow = pos/8;
                    selectedCol = pos%8;
                }
            }
            repaint();
            if (legalMoves.length == 0 || legalMoves[0] == 0)
            {
                doMakeMove(0);
            }
        }
        /**
         * This is called when the current player has chosen the specified
         * move.  Make the move, and then either end or continue the game
         * appropriately.
         */
        void doMakeMove(long move) {
            board.makeMove(move);
            agentBoard=copyBoard(board);
            
            long moveAI = 0;
             /* The current player's turn is ended, so change to the other player.
                Get that player's legal moves.  If the player has no legal moves,
                then the game ends. */
            //Play checkers game on agentboard
            if (currentPlayer == CheckersData.RED) {
                currentPlayer = CheckersData.BLACK;
                legalMoves = board.getLegalMoves2(currentPlayer);
                /*for(CheckersMove m:  legalMoves){
                    System.out.println(m);
                }*/
                if (legalMoves == null || legalMoves.length == 0) {
                    gameOver("BLACK has no moves.  RED wins.");
                    displayBoard = copyBoard(board);
                    previous.drawBoard(board, moveAI);
                    repaint();
                    return;
                } else {
                    message.setText("BLACK:  Now AI's turn.");
                }

                player_1.setCheckersData(board);
                player_2.setCheckersData(board);
                
                switch(aiKey){
                case 1: moveAI = player_1.makeMove(legalMoves,currentPlayer); break;
                case 2: moveAI = player_2.makeMove(legalMoves,currentPlayer); break;
                case 3: Random rand = new Random();
            			if(rand.nextInt(2) == 1)
            				moveAI = player_1.makeMove(legalMoves,currentPlayer);
            			else
            				moveAI = player_2.makeMove(legalMoves,currentPlayer);
                }

                board.makeMove(moveAI);

                displayBoard = copyBoard(board);
                //Add time
                
                repaint();
                
                //timeDelay(1);
                //previous.drawBoard(board);
                
            }
            
            previous.drawBoard(agentBoard, moveAI);

            currentPlayer = CheckersData.RED;
            legalMoves = board.getLegalMovesPartial(currentPlayer);
            if (legalMoves.length == 0 || legalMoves[0] == 0)
            {
                legalMoves = board.getLegalMoves2(currentPlayer);
            }
//            for(CheckersMove m:  legalMoves){
//                System.out.println(m);
//            }
            if (legalMoves == null || legalMoves.length == 0 || legalMoves[0] == 0)
                gameOver("RED has no moves.  BLACK wins.");
            else if (((legalMoves[0] << 1) & CheckersData.validJumpSpots) != 0)
                message.setText("RED:  Make your move.  You must jump.");
            else
                message.setText("RED:  Make your move.");

            /* Set selectedRow = -1 to record that the player has not yet selected
               a piece to move. */
            selectedRow = -1;

            /* As a courtesy to the user, if all legal moves use the same piece, then
               select that piece automatically so the user won't have to click on it
               to select it. */
            if (legalMoves != null && legalMoves.length != 0 && legalMoves[0] != 0) {
                long StartSquare = (currentPlayer == CheckersData.BLACK)? board.topBitBoard:board.bottomBitBoard;
                for (int i = 0; i < legalMoves.length; i++)
                {
                    StartSquare = StartSquare & legalMoves[i];
                    if (StartSquare == 0) break;
                }
                if (StartSquare != 0) {
                    int pos = CheckersData.getBitPosition(StartSquare);
                    selectedRow = pos/8;
                    selectedCol = pos%8;
                }
            }

            /* Make sure the board is redrawn in its new state. */
            repaint();
        }  // end doMakeMove();
        /**
         * Draw a checkerboard pattern in gray and lightGray.  Draw the
         * checkers.  If a game is in progress, highlight the legal moves.
         */
        @Override
        public void paintComponent(Graphics g) {

            /* Draw a two-pixel black border around the edges of the canvas. */

            g.setColor(Color.black);
            g.drawRect(0,0,getSize().width-1,getSize().height-1);
            g.drawRect(1,1,getSize().width-3,getSize().height-3);

            /* Draw the squares of the checkerboard and the checkers. */
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if ( row % 2 == col % 2 )
                        g.setColor(Color.LIGHT_GRAY);
                    else
                        g.setColor(Color.GRAY);
                    g.fillRect(2 + col*20, 2 + row*20, 20, 20);
                    switch (displayBoard.pieceAt(row,col)) {
                        case CheckersData.RED:
                            g.setColor(Color.RED);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            break;
                        case CheckersData.BLACK:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            break;
                        case CheckersData.RED_KING:
                            g.setColor(Color.RED);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("K", 7 + col*20, 16 + row*20);
                            break;
                        case CheckersData.BLACK_KING:
                            g.setColor(Color.BLACK);
                            g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                            g.setColor(Color.WHITE);
                            g.drawString("K", 7 + col*20, 16 + row*20);
                            break;
                    }
                }
            }

         /* If a game is in progress, highlight the legal moves.   Note that legalMoves
          is never null while a game is in progress. */

            if (gameInProgress) {
                /* First, draw a 2-pixel cyan border around the pieces that can be moved. */
                long currentPlayerBoard = (currentPlayer == CheckersData.BLACK)? displayBoard.topBitBoard:displayBoard.bottomBitBoard;
                g.setColor(Color.cyan);
                for (long legalMove : legalMoves) {
                    int pos = CheckersData.getBitPosition(legalMove & currentPlayerBoard);
                    g.drawRect(2 + ((pos%8) * 20), 2 + ((pos/8) * 20), 19, 19);
                    g.drawRect(3 + ((pos%8) * 20), 3 + ((pos/8) * 20), 17, 17);
                }
               /* If a piece is selected for moving (i.e. if selectedRow >= 0), then
                draw a 2-pixel white border around that piece and draw green borders
                around each square that that piece can be moved to. */
                if (selectedRow >= 0) {
                    g.setColor(Color.white);
                    g.drawRect(2 + selectedCol*20, 2 + selectedRow*20, 19, 19);
                    g.drawRect(3 + selectedCol*20, 3 + selectedRow*20, 17, 17);
                    g.setColor(Color.green);
                    long selectedSquare =0x8000000000000000L >>>(selectedRow*8 + selectedCol);
                    long unoccupiedspots = CheckersData.valid ^ (displayBoard.topBitBoard | displayBoard.bottomBitBoard);
                    for (long legalMove : legalMoves) {
                        if ((selectedSquare & legalMove & currentPlayerBoard)!= 0) {
                            //g.drawRect(2 + legalMove.toCol * 20, 2 + legalMove.toRow * 20, 19, 19);
                            //g.drawRect(3 + legalMove.toCol * 20, 3 + legalMove.toRow * 20, 17, 17);
                            int pos = CheckersData.getBitPosition(legalMove &  unoccupiedspots & CheckersData.valid);
                            g.drawRect(2 + ((pos%8) * 20), 2 + ((pos/8) * 20), 19, 19);
                            g.drawRect(3 + ((pos%8) * 20), 3 + ((pos/8) * 20), 17, 17);
                        	
                        }
                    }
                }
            }

        }  // end paintComponent()

        private CheckersData copyBoard(CheckersData board)
        {
            this.board = board;
            CheckersData new_board = new CheckersData();
            new_board.topBitBoard = board.topBitBoard;
            new_board.bottomBitBoard = board.bottomBitBoard;
            new_board.kingBitBoard = board.kingBitBoard;

            return new_board;
        }
        /**
         * Respond to a user click on the board.  If no game is in progress, show
         * an error message.  Otherwise, find the row and column that the user
         * clicked and call doClickSquare() to handle it.
         */
        @Override
        public void mousePressed(MouseEvent evt) {
            if (!gameInProgress)
                message.setText("Click \"New Game\" to start a new game.");
            else {
                int col = (evt.getX() - 2) / 20;
                int row = (evt.getY() - 2) / 20;
                if (col >= 0 && col < 8 && row >= 0 && row < 8)
                    doClickSquare(row,col);
            }
        }

        @Override
        public void mouseReleased(MouseEvent evt) { }
        @Override
        public void mouseClicked(MouseEvent evt) { }
        @Override
        public void mouseEntered(MouseEvent evt) { }
        @Override
        public void mouseExited(MouseEvent evt) { }
    }  // end class Board
    public void timeDelay(int t) {
        try {
            
            // delay 5 seconds
            TimeUnit.SECONDS.sleep(t);
            
            // delay 0.5 second
            //TimeUnit.MICROSECONDS.sleep(500);

			// delay 1 minute
            //TimeUnit.MINUTES.sleep(1);
			
        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
} // end class Checkers
