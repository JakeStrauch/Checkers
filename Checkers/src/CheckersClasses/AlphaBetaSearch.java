package CheckersClasses;

/**
 * 
 * @author Jake Strauch
 *
 */


/**
 * This class implements the Alpha-Beta pruning algorithm to find the best 
 * move at current state.
*/
public class AlphaBetaSearch extends AdversarialSearch {
    private static final int MAXPLY = 13;
    /**
     * The input parameter legalMoves contains all the possible moves.
     * It contains four integers:  fromRow, fromCol, toRow, toCol
     * which represents a move from (fromRow, fromCol) to (toRow, toCol).
     * It also provides a utility method `isJump` to see whether this
     * move is a jump or a simple move.
     * 
     * Update 03/18: each legalMove in the input now contains a single move
     * or a sequence of jumps: (rows[0], cols[0]) -> (rows[1], cols[1]) ->
     * (rows[2], cols[2]).
     *
     * @param legalMoves All the legal moves for the agent at current step.
     */
    //private static final int CHECKMATE = I;
    public long makeMove(long[] legalMoves, int player) {
        // The checker board state can be obtained from this.board,
        // which is a int 2D array. The numbers in the `board` are
        // defined as
        // 0 - empty square,
        // 1 - red man    0b001
        // 2 - red king   0b010
        // 3 - black man  0b011
        // 4 - black king 0b100
        int v = Integer.MIN_VALUE;
        int a = Integer.MIN_VALUE;
        int b = Integer.MAX_VALUE;
        int ply = -1;
        long maxMove = 0;

        System.out.println(player);
        if(terminalTest(player))
            return 0;
        long tempRedBitBoard;
        long tempBlackBitBoard;
        long tempKingBitBoard;
        for(long m : legalMoves)
        {
            tempBlackBitBoard = board.topBitBoard;
            tempRedBitBoard = board.bottomBitBoard;
            tempKingBitBoard = board.kingBitBoard;


            board.makeMove(m);
            v = Math.max(v,MinValue(a,b,ply+1,((player==CheckersData.RED)?CheckersData.BLACK:CheckersData.RED)));
            //revert board to original state

            board.topBitBoard = tempBlackBitBoard;
            board.bottomBitBoard = tempRedBitBoard;
            board.kingBitBoard = tempKingBitBoard;
            //System.out.println(board);

            if(a < v) {
                a = v;
                maxMove = m;
            }
        }


        System.out.println(board);
        // Return the move for the current state.
        // Here, we simply return the first legal move for demonstration.
        return maxMove;
    }


    private int MaxValue(int a, int b,int ply, int player)
    {

        if(ply >= MAXPLY || terminalTest(player))
            return utility();
        int v = Integer.MIN_VALUE;
        long tempRedBitBoard;
        long tempBlackBitBoard;
        long tempKingBitBoard;
        for(long m : board.getLegalMoves2(player))
        {
            tempBlackBitBoard = board.topBitBoard;
            tempRedBitBoard = board.bottomBitBoard;
            tempKingBitBoard = board.kingBitBoard;

            board.makeMove(m);
            v = Math.max(v,MinValue(a,b,ply+1,((player==CheckersData.RED)?CheckersData.BLACK:CheckersData.RED)));

            board.topBitBoard = tempBlackBitBoard;
            board.bottomBitBoard = tempRedBitBoard;
            board.kingBitBoard = tempKingBitBoard;
            if(v >= b) {
                return v;
            }
            a = Math.max(a,v);
        }
        return v;
    }

    private int utility() {

        long bb = board.topBitBoard;
        long rb = board.bottomBitBoard;
        if(bb == 0)
            return Integer.MIN_VALUE;
        if(rb == 0)
            return Integer.MAX_VALUE;
        int count = 0;

        while (bb != 0)
        {
            bb ^= (bb & -(bb));
            count+=2;
        }

        while (rb != 0)
        {
            rb ^= (rb & -(rb));
            count-=2;
        }
        bb = board.topBitBoard & board.kingBitBoard;
        rb = board.bottomBitBoard & board.kingBitBoard;
        while (bb != 0)
        {
            bb ^= (bb & -(bb));
            ++count;

        }
        while (rb != 0)
        {
            rb ^= (rb & -(rb));
            --count;

        }
        return count;
    }

    private boolean terminalTest(int player) {
        return !(board.hasMoves(player) || (board.getLegalJumpStarts(player) != 0));
    }

    private int MinValue(int a, int b,int ply, int player)
    {
        if(ply >= MAXPLY || terminalTest(player))
            return utility();
        int v = Integer.MAX_VALUE;
        long tempRedBitBoard;
        long tempBlackBitBoard;
        long tempKingBitBoard;
        for(long m : board.getLegalMoves2(player))
        {
            tempBlackBitBoard = board.topBitBoard;
            tempRedBitBoard = board.bottomBitBoard;
            tempKingBitBoard = board.kingBitBoard;

            board.makeMove(m);
            v = Math.min(v,MaxValue(a,b,ply+1,((player==CheckersData.RED)?CheckersData.BLACK:CheckersData.RED)));

            board.topBitBoard = tempBlackBitBoard;
            board.bottomBitBoard = tempRedBitBoard;
            board.kingBitBoard = tempKingBitBoard;
            if(v <= a) {
                return v;
            }
            b = Math.min(b,v);
        }
        return v;
    }
    private void backup(int[][] b)
    {
        for(int i=0; i<8;++i)
        {
            for(int j=0;j<8;j++)
            {
                b[i][j]=board.pieceAt(i, j);
            }
        }
    }

    private CheckersData copyBoard(CheckersData board)
    {
        CheckersData new_board = new CheckersData();
        new_board.kingBitBoard = board.kingBitBoard;
        new_board.bottomBitBoard = board.bottomBitBoard;
        new_board.topBitBoard = board.topBitBoard;
        return new_board;
    }
    // TODO
    // Implement your helper methods here.

}
