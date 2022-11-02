package CheckersClasses;

import java.util.ArrayList;

/**
 * @author Jake Strauch
 * An object of this class holds data about a game of checkers.
 * It knows what kind of piece is on each square of the checkerboard.
 * Note that RED moves "up" the board (i.e. row number decreases)
 * while BLACK moves "down" the board (i.e. row number increases).
 * Methods are provided to return lists of available legal moves.
 */
public class CheckersData {

  /*  The following constants represent the possible contents of a square
      on the board.  The constants RED and BLACK also represent players
      in the game. */

    static final int
            EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;


    int[][] board;  // board[r][c] is the contents of row r, column c.
    long blackBitBoard;
    long redBitBoard;
    long kingBitBoard;

    /**
     * Constructor.  Create the board and set it up for a new game.
     */
    CheckersData() {
        board = new int[8][8];
        setUpGame();
    }
    /**
     * Copy Constructor.  copy CheckersData.
     */
    CheckersData(CheckersData data) {
        board = new int[8][8];
        blackBitBoard = data.blackBitBoard;
        redBitBoard = data.redBitBoard;
        kingBitBoard = data.kingBitBoard;
        for(int i=0; i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                board[i][j]= data.board[i][j];
            }
        }
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final long invalid = 0b0101010110101010010101011010101001010101101010100101010110101010L;
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            int[] row = board[i];
            sb.append(8 - i).append(" ");
            for (int n : row) {
                if (n == 0) {
                    sb.append(" ");
                } else if (n == 1) {
                    sb.append(ANSI_RED + "R" + ANSI_RESET);
                } else if (n == 2) {
                    sb.append(ANSI_RED + "K" + ANSI_RESET);
                } else if (n == 3) {
                    sb.append(ANSI_YELLOW + "B" + ANSI_RESET);
                } else if (n == 4) {
                    sb.append(ANSI_YELLOW + "K" + ANSI_RESET);
                }
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("  a b c d e f g h");

        return sb.toString();
    }

    /**
     * Set up the board with checkers in position for the beginning
     * of a game.  Note that checkers can only be found in squares
     * that satisfy  row % 2 == col % 2.  At the start of the game,
     * all such squares in the first three rows contain red squares
     * and all such squares in the last three rows contain black squares.
     */
    void setUpGame() {
    	board = new int[][]{
                {BLACK,EMPTY,BLACK,EMPTY,BLACK,EMPTY,BLACK,EMPTY},
                {EMPTY,BLACK,EMPTY,BLACK,EMPTY,BLACK,EMPTY,BLACK},
                {BLACK,EMPTY,BLACK,EMPTY,BLACK,EMPTY,BLACK,EMPTY},
                {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY},
                {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY},
                {EMPTY,RED  ,EMPTY,RED  ,EMPTY,RED  ,EMPTY,RED  },
                {RED  ,EMPTY,RED  ,EMPTY,RED  ,EMPTY,RED  ,EMPTY},
                {EMPTY,RED  ,EMPTY,RED  ,EMPTY,RED  ,EMPTY,RED  }
    	};
/*        board = new int[][]{
                {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY},
                {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY},
                {EMPTY,EMPTY,BLACK,EMPTY,BLACK,EMPTY,BLACK,EMPTY},
                {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY},
                {EMPTY,EMPTY,BLACK,EMPTY,BLACK,EMPTY,BLACK,EMPTY},
                {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY },
                {EMPTY,EMPTY,BLACK,EMPTY,BLACK,EMPTY,BLACK,EMPTY},
                {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,RED_KING }
        };*/
        /*board = new int[][]{
                {BLACK,EMPTY,RED_KING,EMPTY,EMPTY,EMPTY,BLACK,EMPTY},
                {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY},
                {EMPTY,EMPTY,BLACK,EMPTY,BLACK,EMPTY,BLACK,EMPTY},
                {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY},
                {EMPTY,EMPTY,BLACK,EMPTY,BLACK,EMPTY,BLACK,EMPTY},
                {EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY },
                {BLACK,EMPTY,RED,EMPTY,BLACK,EMPTY,EMPTY,EMPTY},
                {EMPTY,RED,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,RED_KING }
        };*/
        initBitBoards();
        //blackBitBoard =   0b1010101001010101101010100000000000000000000000000000000000000000L;
        //redBitBoard = 0b0000000000000000000000000000000000000000010101011010101001010101L;
        //redBitBoard =     0b0000000000000000000000000100000000000000010101011010001001010101L;
        //blackBitBoard =   0b1010101001010101101010100000000000000000000000000000000000000000L;
    	// Set up the board with pieces BLACK, RED, and EMPTY
    }

    public void initBitBoards()
    {
        blackBitBoard = 0L;
        redBitBoard = 0L;
        kingBitBoard = 0L;
        int i = 0;
        for(int r[] : board)
        {
            for(int p : r)
            {
                switch(p)
                {
                    case RED_KING:
                        kingBitBoard |= (0x8000000000000000L >>>i);
                    case RED:
                        redBitBoard |= (0x8000000000000000L >>>i);
                        break;
                    case BLACK_KING:
                        kingBitBoard |= (0x8000000000000000L >>>i);
                    case BLACK:
                        blackBitBoard |= (0x8000000000000000L >>>i);
                        break;
                    default:
                        break;
                }
                ++i;
            }
        }
    }
    /**
     * Return the contents of the square in the specified row and column.
     */
    int pieceAt(int row, int col) {
        if(row > 7 || row < 0 || col < 0 || col > 7)
            return -1;
        return board[row][col];
    }


    /**
     * Make the specified move.  It is assumed that move
     * is non-null and that the move it represents is legal.
     *
     * Update 03/18: make a single move or a sequence of jumps
     * recorded in rows and cols.
     *
     */
    void makeMove(CheckersMove move) {
        int l = move.rows.size();
        for(int i = 0; i < l-1; i++)
            makeMove(move.rows.get(i), move.cols.get(i), move.rows.get(i+1), move.cols.get(i+1));
    }


    /**
     * Make the move from (fromRow,fromCol) to (toRow,toCol).  It is
     * assumed that this move is legal.  If the move is a jump, the
     * jumped piece is removed from the board.  If a piece moves to
     * the last row on the opponent's side of the board, the
     * piece becomes a king.
     *
     * @param fromRow row index of the from square
     * @param fromCol column index of the from square
     * @param toRow   row index of the to square
     * @param toCol   column index of the to square
     */
    void makeMove(int fromRow, int fromCol, int toRow, int toCol) {

    	// Update the board for the given move. You need to take care of the following situations:
        // 1. move the piece from (fromRow,fromCol) to (toRow,toCol)
        // 2. if this move is a jump, remove the captublack piece
        // 3. if the piece moves into the kings row on the opponent's side of the board, crowned it as a king
        int fromindex = (((fromRow)*8)+fromCol);
        int toindex = (((toRow)*8)+toCol);
        long temp;
        long fromindexboard = 0x8000000000000000L >>> fromindex;
        long toindexboard = 0x8000000000000000L >>> toindex;
        if((fromindexboard & redBitBoard) !=0){
            //red moved
            if(fromindex - toindex < 0) {
                //upward? move
                if (fromindex - toindex < -9) {
                    //jump
                    temp = fromindexboard >>> ((toindex-fromindex))/2;
                    blackBitBoard ^= temp;
                    if((kingBitBoard & temp) !=0)
                    {
                        kingBitBoard ^= temp;
                    }
                }
                redBitBoard |= toindexboard;
                redBitBoard ^= fromindexboard;
            }
            else
            {
                if (fromindex - toindex > 9) {
                    //jump
                    temp =fromindexboard << ((fromindex-toindex))/2;
                    blackBitBoard ^= temp;
                    if((kingBitBoard & temp) !=0)
                    {
                        kingBitBoard ^= temp;
                    }
                }
                redBitBoard |= toindexboard;
                redBitBoard ^= fromindexboard;

            }
        }
        else{
            //black moved
            if(fromindex - toindex < 0) {
                //upward? move -I dont care it works
                if (fromindex - toindex < -9) {
                    //jump
                    temp = fromindexboard >>> ((toindex-fromindex))/2;
                    redBitBoard ^= temp;
                    if((kingBitBoard & temp) !=0)
                    {
                        kingBitBoard ^= temp;
                    }
                }
                blackBitBoard |= toindexboard;
                blackBitBoard ^= fromindexboard;
            }
            else
            {
                if (fromindex - toindex > 9) {
                    //jump
                    temp = fromindexboard << ((fromindex-toindex))/2;
                    redBitBoard ^= temp;
                    if((kingBitBoard & temp) !=0)
                    {
                        kingBitBoard ^= temp;
                    }
                }
                blackBitBoard |= toindexboard;
                blackBitBoard ^= fromindexboard;

            }
        }
        if((kingBitBoard & fromindexboard) !=0)
        {
            kingBitBoard |= toindexboard;
            kingBitBoard ^= fromindexboard;
        }
        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = EMPTY;

        board[fromRow-((fromRow-toRow)/2)][fromCol-((fromCol-toCol)/2)] = EMPTY;
        if(toRow %7 ==0)
        {
            kingBitBoard |= toindexboard;
            if(board[toRow][toCol] %2 == 1) {
                ++board[toRow][toCol];
            }
        }
        //initBitBoards();
    }
    //check if same state
    //does not consider whose turn it is but cant know if they dont tell us
    public boolean isEqual(CheckersData b)
    {
        return b.blackBitBoard == blackBitBoard && b.redBitBoard == redBitBoard && b.kingBitBoard == kingBitBoard;
    }
    /**
     * Return an array containing all the legal CheckersMoves
     * for the specified player on the current board.  If the player
     * has no legal moves, null is returned.  The value of player
     * should be one of the constants RED or BLACK; if not, null
     * is returned.  If the returned value is non-null, it consists
     * entirely of jump moves or entirely of regular moves, since
     * if the player can jump, only jumps are legal moves.
     *
     * @param player color of the player, RED or BLACK
     */
    CheckersMove[] getLegalMoves(int player) {
        //I can make this so much simpler and faster by not ever converting to the stupid CheckersMove but
        //that would require changing skeleton code
        ArrayList<CheckersMove> movesarr = new ArrayList<CheckersMove>();
        int m;
        int pos;
        long m2;
        long moves;
        //System.out.println(LongToString(blackBitBoard | redBitBoard));
        //System.out.println(LongToString(blackBitBoard ));
        //System.out.println(LongToString(redBitBoard));
        //System.out.println(LongToString(getLegalJumpStarts(player)));
        if((m2=getLegalJumpStarts(player)) != 0){

            //System.out.println(LongToString(m2));
            while (m2 != 0) {
                moves = (m2 & -(m2));
                m2 ^= moves;
                //System.out.println(LongToString(moves));
                m=getBitPosition(moves);
                //System.out.println(m + " " +(m %8) +" "+(m /8) );
                for (CheckersMove cm : getLegalJumpsFrom(player,m/8,m %8)) {
                    //System.out.println(cm);
                    movesarr.add(cm);
                    if(cm.cols.size() ==1){
                        System.out.println("ERROR getLegalJumpStarts(player) failed");
                        System.out.println(LongToString(blackBitBoard | redBitBoard));
                        System.out.println(LongToString(kingBitBoard));
                        System.out.println(this);
                    }
                }
                //movesarr.addAll(getLegalJumpsFrom(player,m %8,m/8))

            }
            CheckersMove[] ms = new CheckersMove[movesarr.size()];
            return movesarr.toArray(ms);
        }
        if(player == BLACK) {
            /**
             * generate all black moves in O(black peices)
             */
            m2 = ((blackBitBoard >>> 7) ^ (invalid | blackBitBoard | redBitBoard)) & (blackBitBoard >>> 7);
            //these while loops seperate the individual moves
            while (m2 != 0) {
                moves = (m2 & -(m2));
                m2 ^= moves;

                m=getBitPosition(moves);

                pos = getBitPosition(moves << 7);
                moves |= moves << 7;
                movesarr.add(new CheckersMove(pos /8,pos%8,m /8,m%8));
                //System.out.println(LongToString((blackBitBoard | redBitBoard) ^ moves));

            }


            m2 = ((blackBitBoard >>> 9) ^ (invalid | blackBitBoard | redBitBoard)) & (blackBitBoard >>> 9);


            while (m2 != 0) {
                moves = (m2 & -(m2));
                m2 ^= moves;
                m=getBitPosition(moves);

                pos = getBitPosition(moves << 9);
                moves |= moves << 9;
                movesarr.add(new CheckersMove(pos /8,pos%8,m /8,m%8));
                //System.out.println(LongToString((blackBitBoard | redBitBoard) ^ moves));

            }


            if((kingBitBoard & blackBitBoard) !=0)
            {
                m2 = (((kingBitBoard & blackBitBoard) << 7) ^ (invalid | blackBitBoard | redBitBoard)) & ((kingBitBoard & blackBitBoard) << 7);

                while (m2 != 0) {
                    moves = (m2 & -(m2));
                    m2 ^= moves;
                    m=getBitPosition(moves);

                    pos = getBitPosition(moves >>> 7);
                    moves |= moves >> 7;
                    movesarr.add(new CheckersMove(pos /8,pos%8,m /8,m%8));
                    //System.out.println(LongToString((blackBitBoard | redBitBoard) ^ moves));

                }


                m2 = (((kingBitBoard & blackBitBoard) << 9) ^ (invalid | blackBitBoard | redBitBoard)) & ((kingBitBoard & blackBitBoard) << 9);


                while (m2 != 0) {
                    moves = (m2 & -(m2));
                    m2 ^= moves;
                    m=getBitPosition(moves);

                    pos = getBitPosition(moves >>> 9);
                    moves |= moves >>> 9;
                    movesarr.add(new CheckersMove(pos /8,pos%8,m /8,m%8));
                    //System.out.println(LongToString((blackBitBoard | redBitBoard) ^ moves));

                }

            }
        }
        else
        {
            m2 = ((redBitBoard << 7) ^ (invalid | blackBitBoard | redBitBoard)) & (redBitBoard << 7);

            while (m2 != 0) {
                moves = (m2 & -(m2));
                m2 ^= moves;
                m=getBitPosition(moves);

                pos = getBitPosition(moves >>> 7);
                moves |= moves >>> 7;
                movesarr.add(new CheckersMove(pos /8,pos%8,m /8,m%8));
                //System.out.println(LongToString((blackBitBoard | redBitBoard) ^ moves));

            }


            m2 = ((redBitBoard << 9) ^ (invalid | blackBitBoard | redBitBoard)) & (redBitBoard << 9);


            while (m2 != 0) {
                moves = (m2 & -(m2));
                m2 ^= moves;
                m=getBitPosition(moves);

                pos = getBitPosition(moves >>> 9);
                moves |= moves >>> 9;
                movesarr.add(new CheckersMove(pos /8,pos%8,m /8,m%8));
                //System.out.println(LongToString((blackBitBoard | redBitBoard) ^ moves));

            }


            if((kingBitBoard & redBitBoard) !=0)
            {
                m2 = (((kingBitBoard & redBitBoard) >>> 7) ^ (invalid | blackBitBoard | redBitBoard)) & ((kingBitBoard & redBitBoard) >>> 7);

                while (m2 != 0) {
                    moves = (m2 & -(m2));
                    m2 ^= moves;
                    m=getBitPosition(moves);

                    pos = getBitPosition(moves << 7);
                    moves |= moves << 7;
                    movesarr.add(new CheckersMove(pos /8,pos%8,m /8,m%8));
                    //System.out.println(LongToString((blackBitBoard | redBitBoard) ^ moves));

                }


                m2 = (((kingBitBoard & redBitBoard) >>> 9) ^ (invalid | blackBitBoard | redBitBoard)) & ((kingBitBoard & redBitBoard) >>> 9);


                while (m2 != 0) {
                    moves = (m2 & -(m2));
                    m2 ^= moves;
                    m=getBitPosition(moves);

                    pos = getBitPosition(moves << 9);
                    moves |= moves << 9;
                    movesarr.add(new CheckersMove(pos /8,pos%8,m /8,m%8));
                    //System.out.println(LongToString((blackBitBoard | redBitBoard) ^ moves));

                }

            }

        }




        CheckersMove[] ms = new CheckersMove[movesarr.size()];

        return movesarr.toArray(ms);
    }

    public boolean hasMoves(int player)
    {
        long m2;
        if(player == BLACK) {

            m2 = ((blackBitBoard >>> 7) ^ (invalid | blackBitBoard | redBitBoard)) & (blackBitBoard >>> 7);
            //these while loops seperate the individual moves
            if (m2 != 0) {
                return true;
            }


            m2 = ((blackBitBoard >>> 9) ^ (invalid | blackBitBoard | redBitBoard)) & (blackBitBoard >>> 9);


            if  (m2 != 0) {
                return true;

            }


            if((kingBitBoard & blackBitBoard) !=0)
            {
                m2 = (((kingBitBoard & blackBitBoard) << 7) ^ (invalid | blackBitBoard | redBitBoard)) & ((kingBitBoard & blackBitBoard) << 7);

               if (m2 != 0) {
                   return true;

                }


                m2 = (((kingBitBoard & blackBitBoard) << 9) ^ (invalid | blackBitBoard | redBitBoard)) & ((kingBitBoard & blackBitBoard) << 9);


                if (m2 != 0) {
                    return true;

                }

            }
        }
        else
        {
            m2 = ((redBitBoard << 7) ^ (invalid | blackBitBoard | redBitBoard)) & (redBitBoard << 7);

            if (m2 != 0) {
                return true;

            }


            m2 = ((redBitBoard << 9) ^ (invalid | blackBitBoard | redBitBoard)) & (redBitBoard << 9);


           if (m2 != 0) {
                return true;

            }


            if((kingBitBoard & redBitBoard) !=0)
            {
                m2 = (((kingBitBoard & redBitBoard) >>> 7) ^ (invalid | blackBitBoard | redBitBoard)) & ((kingBitBoard & redBitBoard) >>> 7);

               if (m2 != 0) {
                    return true;

                }


                m2 = (((kingBitBoard & redBitBoard) >>> 9) ^ (invalid | blackBitBoard | redBitBoard)) & ((kingBitBoard & redBitBoard) >>> 9);


               if (m2 != 0) {
                    return true;

                }

            }

        }
        return false;
    }
    /**
     * Return a list of the legal jumps that the specified player can
     * make starting from the specified row and column.  If no such
     * jumps are possible, null is returned.  The logic is similar
     * to the logic of the getLegalMoves() method.
     *
     * Update 03/18: Note that each CheckerMove may contain multiple jumps. 
     * Each move returned in the array represents a sequence of jumps 
     * until no further jump is allowed.
     *
     * @param player The player of the current jump, either RED or BLACK.
     * @param row    row index of the start square.
     * @param col    col index of the start square.
     */
    CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
        // TODO

        ArrayList<CheckersMove> movesarr = getLegalJumpsFromRecursion(player, new CheckersMove(row,col), 0, (0x8000000000000000L >>>((((row)*8)+col)) & kingBitBoard) != 0);
        CheckersMove[] moves = new CheckersMove[movesarr.size()];


        return movesarr.toArray(moves);
    }


    ArrayList<CheckersMove> getLegalJumpsFromRecursion(int player, CheckersMove m, long captures, boolean isKing)
    {

        ArrayList<CheckersMove> movesarr = new ArrayList<>();
        int i = -1;
        int enemy = (player == RED)? 2:1;
        int piecerow;
        int ogpiecerow = m.rows.get(m.rows.size()-1);
        int ogpiececol = m.cols.get(m.cols.size()-1);
        int piececol;
        long index;
        CheckersMove tempboy;
        //ArrayList<CheckersMove> templist = new ArrayList<>();
        //movesarr.add(new CheckersMove(row,col));
        piececol = (ogpiececol+2);
        piecerow =(ogpiecerow+2);
        index = 0x8000000000000000L >>>(((piececol-1)*8)+piecerow-1);
        if(enemy == ((pieceAt(piecerow - 1,piececol-1)+1)/2) && (pieceAt(piecerow,piececol) == EMPTY || (piecerow==m.rows.get(0) && piececol==m.cols.get(0) )) && (captures & index) == 0 && (isKing || player == BLACK ))
        {

            tempboy = m.clone();
            tempboy.addMove(piecerow,piececol);
            movesarr.addAll(getLegalJumpsFromRecursion(player, tempboy,captures | index, isKing || (piecerow % 7 ==0) ));
            ++i;

        }
        piecerow =(ogpiecerow-2);
        index = 0x8000000000000000L >>> (((piececol-1)*8)+piecerow+1);
        if(enemy == ((pieceAt(piecerow + 1,piececol-1)+1)/2) && (pieceAt(piecerow,piececol) == EMPTY || (piecerow==m.rows.get(0) && piececol==m.cols.get(0) )) && (captures & index) == 0 && (isKing || player == RED )){
            tempboy = m.clone();
            tempboy.addMove(piecerow,piececol);
            movesarr.addAll(getLegalJumpsFromRecursion(player, tempboy,captures | index, isKing || (piecerow % 7 ==0) ));

            ++i;

        }

        piececol =(ogpiececol-2);
        index = 0x8000000000000000L >>> (((piececol+1)*8)+piecerow+1);
        if(enemy == ((pieceAt(piecerow + 1,piececol+1)+1)/2) && (pieceAt(piecerow,piececol) == EMPTY || (piecerow==m.rows.get(0) && piececol==m.cols.get(0) )) && (captures & index) == 0 && (isKing || player == RED )){

            tempboy = m.clone();
            tempboy.addMove(piecerow,piececol);
            movesarr.addAll(getLegalJumpsFromRecursion(player, tempboy,captures | index, isKing || (piecerow % 7 ==0) ));

            ++i;

        }

        piecerow =(ogpiecerow+2);
        index = 0x8000000000000000L >>> (((piececol+1)*8)+piecerow-1);
        if(enemy == ((pieceAt(piecerow - 1,piececol+1)+1)/2) && (pieceAt(piecerow,piececol) == EMPTY || (piecerow==m.rows.get(0) && piececol==m.cols.get(0) )) && (captures & index) == 0 && (isKing || player == BLACK )){

            tempboy = m.clone();
            tempboy.addMove(piecerow,piececol);
            movesarr.addAll(getLegalJumpsFromRecursion(player, tempboy,captures | index, isKing || (piecerow % 7 ==0) ));


            ++i;

        }
        if(i == -1)
        {
            movesarr.add(m);
        }

        return movesarr;
    }

    public long getLegalJumpStarts(int player){
        long m;
        long m2;
        long places = 0;
        if(player == BLACK){
            /*
                FIND ALL POSSIBLE REGULAR CAPTURES INSTANTLY
             */
            m = ( (blackBitBoard >>>7) ^ (invalid | blackBitBoard)) & redBitBoard;
            if(m !=0)
            {
                m2 = ( (m >>>7) ^ (invalid | blackBitBoard | redBitBoard)) & (m >>>7);
                if(m2 != 0) {
                    places |= m2 << 14;
                }
            }
            m = ( (blackBitBoard >>>9) ^ (invalid | blackBitBoard)) & redBitBoard;
            if(m !=0)
            {

                m2 = ( (m >>>9) ^ (invalid | blackBitBoard | redBitBoard)) & (m >>>9);
                //System.out.println(LongToString(m2));
                if(m2 != 0) {
                    places |= m2 << 18;

                }

            }
            /*
                IF THEY HAVE KINGS THEN FIGURE OUT BACKWARD MOVE CAPTURES
             */

            if((blackBitBoard & kingBitBoard) != 0)
            {
                m = ( ((blackBitBoard & kingBitBoard) << 7) ^ (invalid | blackBitBoard)) & redBitBoard;
                if(m !=0)
                {
                    m2 = ( (m << 7) ^ (invalid | blackBitBoard | redBitBoard)) & (m << 7);
                    if(m2 != 0) {
                        places |= m2 >>> 14;
                    }
                }
                m = ( ((blackBitBoard & kingBitBoard) << 9) ^ (invalid | blackBitBoard)) & redBitBoard;
                if(m !=0)
                {
                    m2 = ( (m << 9) ^ (invalid | blackBitBoard | redBitBoard)) & (m << 9);
                    if(m2 != 0) {
                        places |= m2 >>> 18;
                    }
                }


            }

        }
        else{
            //FOR RED
            /*
                FIND ALL POSSIBLE CAPTURES INSTANTLY
             */
            if((redBitBoard & kingBitBoard) != 0) {
                m = (((redBitBoard & kingBitBoard) >>> 7) ^ (invalid | redBitBoard)) & blackBitBoard;
                if (m != 0) {
                    m2 = ((m >>> 7) ^ (invalid | redBitBoard | blackBitBoard)) & (m >>> 7);
                    if (m2 != 0) {
                        places |= m2 << 14;
                    }
                }
                m = (((redBitBoard & kingBitBoard) >>> 9) ^ (invalid | redBitBoard)) & blackBitBoard;
                if (m != 0) {
                    m2 = ((m >>> 9) ^ (invalid | blackBitBoard | redBitBoard)) & (m >>> 9);
                    if (m2 != 0) {
                        places |= m2 << 18;
                    }

                }
            }


                m = ( (redBitBoard << 7) ^ (invalid | redBitBoard)) & blackBitBoard;
                //System.out.println("1:\n"+LongToString(m));
                if(m !=0)
                {
                    m2 = ( (m << 7) ^ (invalid | blackBitBoard | redBitBoard)) & (m << 7);
                    if(m2 != 0) {
                        places |= m2 >>> 14;
                    }
                }
                m = ( (redBitBoard << 9) ^ (invalid | redBitBoard)) & blackBitBoard;
            //System.out.println("2:\n"+LongToString(m));
            //System.out.println("2:\n"+LongToString(redBitBoard));
                if(m !=0)
                {
                    m2 = ( (m << 9) ^ (invalid | blackBitBoard | redBitBoard)) & (m << 9);
                    if(m2 != 0) {
                        places |= m2 >>> 18;
                    }
                }



        }
        return places;
    }



    /**
    * I modified an integer algorithm to work with longs from http://graphics.stanford.edu/~seander/bithacks.html#ZerosOnRightLinear
     */
    public int getBitPosition(long l)
    {
        //0b1111111111111111111111111111111111111111111111111111111111111111
        //0b1111111111111111111111111111111111111111111111111111111111111111
        //0b11111111111111111111111111111111
        //0b1111111100000000111111111111111100000000111111111111111100000000
        //0b10101010101010101010101010101011010101010101010101010101010101
        //0b11011011011011011011011011011011011011011011011
        //0b1010101010101010101010101010101101010101010101010101010101010101L;
        int c = 0; // c will be the number of zero bits on the right
        //if (l != 0) c++;
        if ((l & 0xFFFFFFFFL) != 0){ c += 32;}
        if ((l & 0x0000FFFF0000FFFFL) != 0){ c += 16;}
        if ((l & 0x00FF00FF00FF00FFL) != 0){ c += 8;}
        if ((l & 0x0F0F0F0F0F0F0F0FL) != 0){ c += 4;}
        if ((l & 0x3333333333333333L) != 0){ c += 2;}
        if ((l & 0x5555555555555555L) != 0){ c += 1;}
        return c;
    }

/*
//this method was a work in progress - finished everything except for jump moves
//but I stopped once I realized I wasnt allowed datatype freedom
    public long[] generateRedMoveBoards(long p1, long p2, long kings){
        //long[] boards= new long[30];
        long b =      0b1010101001010101101010100100000000000000010101011010001001010101L;
        long enemy2 = 0b1010101001010101101010100000000000000000000000000000000000000000L;

        long enemy = enemy2 ^ b;
        long tb = b;
        long temp = 0;
        long te = enemy;
        long te2 = enemy2;

        long moves;
        long m1;
        long m2;
        int shift = 9; //shift of 7 or 9 works perfectly
        //get legal regular moves
        m1 = ( (enemy2 >>>shift) ^ (invalid | b)) &(enemy2 >>>shift);
        System.out.println(LongToString(b));
        // System.out.println(LongToString((enemy2)));
        //System.out.println(LongToString(m1));

        while (m1 != 0) {
            moves = (m1 & -(m1));
            m1 ^= moves;
            moves |= moves << shift;
            System.out.println(LongToString(b ^ moves));

        }

        shift = 7;
        m1 = ( (enemy2 >>>shift) ^ (invalid | b)) &(enemy2 >>>shift);
        System.out.println(LongToString(b));
        // System.out.println(LongToString((enemy2)));
        //System.out.println(LongToString(m1));

        while (m1 != 0) {
            moves = (m1 & -(m1));
            m1 ^= moves;
            moves |= moves << shift;
            System.out.println(LongToString(b ^ moves));

        }







        //get legal jump moves
        m1 = ( (te2 >>>shift) ^ (invalid | te2)) & te;

        if (m1 !=0)
        {

            m1 = ((m1 >>>shift) ^ (invalid | b)) & (m1 >>>shift);
            m2 = m1;
            temp = 0;
            System.out.println(LongToString(m1));
            while (m1 != 0) {
                moves = (m1 & -(m1));
                System.out.println(LongToString(enemy ^ (moves << shift)));
                m1 ^= moves;
                moves |= moves << shift;
                moves |= moves << shift;
                temp |= moves;
                System.out.println(LongToString(b ^ moves));
            }
        }
        m1 = ( (te2 >>> shift -2) ^ (invalid | te2)) & te;




        if((kings & enemy2) !=0)
        {

        }

        return null;
    }*/


    public static String LongToString(long l){
        String str = "8    ";
        for(int i =63; i >-1; --i)
        {
            str+=   ((l >>> i) & 1) + " ";
            if((i) %8 == 0)
            {
                str+="\n"+((i/8 == 0)?" ":(i/8))+"    ";
            }
        }
        str+="\n     A B C D E F G H";
        return str;
    }

}
