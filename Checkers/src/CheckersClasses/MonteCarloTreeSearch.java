package CheckersClasses;

/**
 * 
 * @author Jake Strauch
 *
 */

import java.util.Random;

import static CheckersClasses.CheckersData.BLACK;
import static CheckersClasses.CheckersData.RED;







/**
 * This class implements the Monte Carlo tree search method to find the best
 * move at the current state.
 */
public class MonteCarloTreeSearch extends AdversarialSearch {
    private static final int MAX_TIME = 20000;
    private static final int MAX_ROLLOUT = 150; //triple the average game length
    private int time = 0;
    Random random;
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
    public long makeMove(long[] legalMoves,int player) {
        // The checker board state can be obtained from this.board,
        // which is an 2D array of the following integers defined below:
    	// 
        // 0 - empty square,
        // 1 - red man
        // 2 - red king
        // 3 - black man
        // 4 - black king
        CSTree<CheckersData> tree = new CSTree<>();
        tree.root = new CSNode<CheckersData>(new CheckersData(board));
        tree.root.player = player;
        random = new Random();
        CSNode<CheckersData> leaf;
        CSNode<CheckersData> child;
        int result = 0;
        time = 0;
        while(time < MAX_TIME)
        {
            leaf = select(tree);
            child = expandChildren(leaf);
            result = simulate(child);
            //leaf.backPropogate((result == 1)?0:1);
            child.backPropogateNodes(result);
            ++time;
        }

        
        // Return the move for the current state.
        // Here, we simply return the first legal move for demonstration.
        return bestMove2(tree.root,legalMoves);
    }

    private CheckersMove bestMove(CSNode<CheckersData> root, CheckersMove[] legalMoves) {
        if(root.children == null || root.numLegalChildren ==0) {
            return null;
        }
        CSNode<CheckersData> maxnode = root.children[0];
        int maxval = maxnode.wins;
        int maxi = 0;
        for(int i = 1; i < root.numLegalChildren; ++i)
        {
            if(maxval < root.children[i].wins)
            {
                maxi = i;
                maxval = root.children[i].wins;
            }
        }
        return legalMoves[maxi];
    }

    private long bestMove2(CSNode<CheckersData> root, long[] legalMoves) {
        if(root.children == null || root.numLegalChildren ==0) {
            return 0;
        }
        CSNode<CheckersData> maxnode = root.children[0];
        int maxwins = maxnode.wins;
        int maxsims = maxnode.simulations;
        int maxi = 0;
        for(int i = 1; i < root.numLegalChildren; ++i)
        {
            if(maxsims < root.children[i].simulations || (maxsims == root.children[i].simulations && maxwins < root.children[i].wins))
            {
                maxi = i;
                maxsims = root.children[i].simulations;
                maxwins = root.children[i].wins;
            }
        }
        return legalMoves[maxi];
    }

    private int simulate(CSNode<CheckersData> child) {
        CheckersData b = new CheckersData(child.getData());
        int i = 0;
        int player = child.player;
        long[] moves;
        while(i < MAX_ROLLOUT && !isTerminal(b,player))
        {
            moves = b.getLegalMoves2(player);

            b.makeMove(moves[random.nextInt(moves.length)]);
            player = (player == RED)? BLACK : RED;
            ++i;
        }

        //return (isTerminal(b,player) && player != BLACK)?1:0;
        if(isTerminal(b,player))
            return (child.player == player)? 1:0;
        return -1;
    }



    private boolean isNodeFullyExpanded(CSNode<CheckersData> p)
    {
        return (p.children != null) && p.numLegalChildren == p.children.length;
    }


    private CSNode<CheckersData> select(CSTree gameTree) {
        CSNode<CheckersData> node = gameTree.root;
        while (!isTerminal(node.data,node.player) && isNodeFullyExpanded(node)) {
            node = getBestUCBNode(node);
            //node = getChildWithMaxUCT(node);
        }
        return node;
    }

    private CSNode<CheckersData> getBestUCBNode(CSNode<CheckersData> node) {
        CSNode<CheckersData> maxnode = node.children[0];
        double maxval = maxnode.UCB();
        double ucb = maxval;
        for(int i = 1; i < node.numLegalChildren; ++i)
        {
            ucb = node.children[i].UCB();
            if(maxval < ucb)
            {
                maxnode = node.children[i];
                maxval = ucb;
            }
        }

        return maxnode;
    }


    private boolean isTerminal(CheckersData board,int player) {
        return !(board.hasMoves(player) || (board.getLegalJumpStarts(player) != 0));
    }

    private CSNode<CheckersData> expandChildren(CSNode<CheckersData> p) {
        if(p.numLegalChildren == 0) {
            long[] moves = p.data.getLegalMoves2(p.player);
            CSNode<CheckersData>[] children = new CSNode[moves.length];
            int i = 0;
            int enemyColor = (p.player == RED) ? CheckersData.BLACK : RED;
            for (long m : moves) {
                CheckersData data = new CheckersData(p.getData());
                data.makeMove(m);
                children[i] = new CSNode<CheckersData>(data);
                children[i].parent = p;
                children[i++].player = enemyColor;
            }
            p.setChildren(children);
            if(i != 0) {
                p.numLegalChildren++;
                return children[0];
            }
            return p;
        }
        else if(p.numLegalChildren < p.children.length)
        {
            p.numLegalChildren++;
            return p.children[p.numLegalChildren-1];
        }
        return null;
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











}
