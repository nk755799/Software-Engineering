package com.dsq2022.game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Board class (Model in M-V-C)
 *
 * rule book:
 *   use this initial setup.
 *   http://veryspecial.us/free-downloads/AncientChess.com-DouShouQi.pdf
 *
 * video of game play & nice picture of initial game setup:
 *   use this (same as above) initial setup.
 *   http://ancientchess.com/page/play-doushouqi.htm
 *
 * play online:
 *   But do NOT use this setup!  Dog and wolf are interchanged.
 *   http://liacs.leidenuniv.nl/~visjk/doushouqi/
 *   Note: Dog and wolf are in different positions.
 *
 * Copyright © George J. Grevera, 2016. All rights reserved.
 */
public class Board implements Serializable {
    private static final long serialVersionUID = 208041731299892L;

    // constants for the size of the Board
    public static final int   fRows = 9;  ///< no. of Board rows
    public static final int   fCols = 7;  ///< no. of Board cols

    /// the (underlying) playing surface/base. base[0][0] is the upper left corner.
    public Base[][]  base = new Base[ fRows ][ fCols ];

    /// the noveable pieces on the playing Board. piece[0][0] is the upper left corner.
    public Piece[][] piece = new Piece[ fRows ][ fCols ];

    public boolean  bluesTurn = true;  ///< by convention, blue goes first
    public boolean  moveWasCapture = false;  ///< last move resulted in a capture
    public static final boolean  universalTraps = true;  ///< all traps are universal (see below)
    //=======================================================================
    /** Init the Board. The "Board" consists of the base which doesn't change
     *  and the pieces which move.
     *  By convention, red will initially be in the top half (0,0) of the
     *  Board, and blue will start in the bottom half. be careful. the
     *  opposite sides do not mirror each other!
     *  @todo v1
     */
    public Board ( ) {
        //init the underlying Board base
        this.base[0][0] = Base.cGround;
        this.base[0][1] = Base.cGround;
        this.base[0][2] = Base.cRTrap;
        this.base[0][3] = Base.cRDen;
        this.base[0][4] = Base.cRTrap;
        this.base[0][5] = Base.cGround;
        this.base[0][6] = Base.cGround;

        this.base[1][0] = Base.cGround;
        this.base[1][1] = Base.cGround;
        this.base[1][2] = Base.cGround;
        this.base[1][3] = Base.cRTrap;
        this.base[1][4] = Base.cGround;
        this.base[1][5] = Base.cGround;
        this.base[1][6] = Base.cGround;

        this.base[2][0] = Base.cGround;
        this.base[2][1] = Base.cGround;
        this.base[2][2] = Base.cGround;
        this.base[2][3] = Base.cGround;
        this.base[2][4] = Base.cGround;
        this.base[2][5] = Base.cGround;
        this.base[2][6] = Base.cGround;

        this.base[3][0] = Base.cGround;
        this.base[3][1] = Base.cWater;
        this.base[3][2] = Base.cWater;
        this.base[3][3] = Base.cGround;
        this.base[3][4] = Base.cWater;
        this.base[3][5] = Base.cWater;
        this.base[3][6] = Base.cGround;

        this.base[4][0] = Base.cGround;
        this.base[4][1] = Base.cWater;
        this.base[4][2] = Base.cWater;
        this.base[4][3] = Base.cGround;
        this.base[4][4] = Base.cWater;
        this.base[4][5] = Base.cWater;
        this.base[4][6] = Base.cGround;

        this.base[5][0] = Base.cGround;
        this.base[5][1] = Base.cWater;
        this.base[5][2] = Base.cWater;
        this.base[5][3] = Base.cGround;
        this.base[5][4] = Base.cWater;
        this.base[5][5] = Base.cWater;
        this.base[5][6] = Base.cGround;

        this.base[6][0] = Base.cGround;
        this.base[6][1] = Base.cGround;
        this.base[6][2] = Base.cGround;
        this.base[6][3] = Base.cGround;
        this.base[6][4] = Base.cGround;
        this.base[6][5] = Base.cGround;
        this.base[6][6] = Base.cGround;

        this.base[7][0] = Base.cGround;
        this.base[7][1] = Base.cGround;
        this.base[7][2] = Base.cGround;
        this.base[7][3] = Base.cBTrap;
        this.base[7][4] = Base.cGround;
        this.base[7][5] = Base.cGround;
        this.base[7][6] = Base.cGround;

        this.base[8][0] = Base.cGround;
        this.base[8][1] = Base.cGround;
        this.base[8][2] = Base.cBTrap;
        this.base[8][3] = Base.cBDen;
        this.base[8][4] = Base.cBTrap;
        this.base[8][5] = Base.cGround;
        this.base[8][6] = Base.cGround;

        //place the pieces
        this.piece[0][0] = Piece.rLion;
        this.piece[0][6] = Piece.rTiger;
        this.piece[1][1] = Piece.rDog;
        this.piece[1][5] = Piece.rCat;
        this.piece[2][0] = Piece.rRat;
        this.piece[2][4] = Piece.rWolf;
        this.piece[2][6] = Piece.rElephant;
        this.piece[4][3] = Piece.rLeopard;
        this.piece[6][0] = Piece.bElephant;
        this.piece[6][4] = Piece.bLeopard;
        this.piece[6][6] = Piece.bRat;
        this.piece[7][1] = Piece.bCat;
        this.piece[7][5] = Piece.bDog;
        this.piece[8][1] = Piece.bTiger;
        this.piece[8][6] = Piece.bLion;

    }
    //-----------------------------------------------------------------------
    /** @return the specific (moveable) piece (e.g., bWolf or rbNone) at the
     *  indicated position.
     *  @todo v1
     */
    public Piece getPiece ( int r, int c ) {
        if(r>-1 && r<fRows && c>-1 && c<fCols){
            Piece piece1 = piece[r][c];
            if(piece1!=null){
                return piece1;
            }else{
                return Piece.rbNone;
            }
        }else{
            return Piece.rbNone;
        }
    }
    //-----------------------------------------------------------------------
    /** @return what appears on the underlying Board base at the specified position
     *  (e.g., cWater), or cNone if out of bounds.
     *  @todo v1
     */
    public Base getBase ( int r, int c ) {
        if(r>-1 && r<fRows && c>-1 && c<fCols) {
            Base base1 = this.base[r][c];
            if (base1 != null) {
                return base1;
            } else {
                return Base.cNone;
            }
        } else {
            return Base.cNone;
        }
    }
    //-----------------------------------------------------------------------
    /** @return a string representing the Board that can be pretty-printed.
     *  It should look something like the following:
     *  <pre><code>
     *     --...-        --...-     \\n
     *    |      |      |      |    \\n
     *    .      .      .      .     .
     *    .      .      .      .     .
     *    .      .      .      .     .
     *    |      |      |      |    \\n
     *     --...-        --...-     \\n
     * </code></pre>
     * The left side of the string should be the underlying Board base.
     * The right side should be the pieces at their specific locations.
     * Put the first 3 characters of the name at each location
     * (e.g., rLi for the red lion, and bEl for the blue elephant).
     * If you have a better idea, please let me know!
     * @todo v1
     */
    @Override
    public String toString() {
        List<String> firstList = new ArrayList<String>();
        List<String> secondList = new ArrayList<String>();
        for (int i = 0; i < fRows; i++) {
            String firstStr = "|\t";
            for (int j = 0; j < fCols; j++) {
                Base base1 = getBase(i,j);
                String threeCharOfpieceName = "";
                if (base1 != null) {
                    if (base1 == Base.cNone) {
                        threeCharOfpieceName = "N";
                    } else if (base1 == Base.cBDen) {
                        threeCharOfpieceName = "BD";
                    } else if (base1 == Base.cBTrap) {
                        threeCharOfpieceName = "BT";
                    } else if (base1 == Base.cGround) {
                        threeCharOfpieceName = "G";
                    } else if (base1 == Base.cWater) {
                        threeCharOfpieceName = "W";
                    } else if (base1 == Base.cRDen) {
                        threeCharOfpieceName = "RD";
                    } else if (base1 == Base.cRTrap) {
                        threeCharOfpieceName = "RT";
                    }
                    firstStr += threeCharOfpieceName + "\t";
                }
            }
            firstStr += "|";
            firstList.add(firstStr);
        }

        for (int i = 0; i < fRows; i++) {
            String secondStr = "|\t";
            for (int j = 0; j < fCols; j++) {
                Piece piece1 = getPiece(i,j);
                String threeCharOfpieceName = "";
                if (piece1 != null) {
                    if (piece1 == Piece.bCat) {
                        threeCharOfpieceName = "bCa";
                    } else if (piece1 == Piece.bDog) {
                        threeCharOfpieceName = "bDo";
                    } else if (piece1 == Piece.bElephant) {
                        threeCharOfpieceName = "bEl";
                    } else if (piece1 == Piece.bLeopard) {
                        threeCharOfpieceName = "bLe";
                    } else if (piece1 == Piece.bLion) {
                        threeCharOfpieceName = "bLi";
                    } else if (piece1 == Piece.bRat) {
                        threeCharOfpieceName = "bRa";
                    } else if (piece1 == Piece.bTiger) {
                        threeCharOfpieceName = "bTi";
                    } else if (piece1 == Piece.bWolf) {
                        threeCharOfpieceName = "bWo";
                    } else if (piece1 == Piece.rCat) {
                        threeCharOfpieceName = "rCa";
                    } else if (piece1 == Piece.rDog) {
                        threeCharOfpieceName = "rDo";
                    } else if (piece1 == Piece.rElephant) {
                        threeCharOfpieceName = "rEl";
                    } else if (piece1 == Piece.rLeopard) {
                        threeCharOfpieceName = "rLe";
                    } else if (piece1 == Piece.rLion) {
                        threeCharOfpieceName = "rLi";
                    } else if (piece1 == Piece.rRat) {
                        threeCharOfpieceName = "rRa";
                    } else if (piece1 == Piece.rTiger) {
                        threeCharOfpieceName = "rTi";
                    } else if (piece1 == Piece.rWolf) {
                        threeCharOfpieceName = "rWo";
                    } else if (piece1 == Piece.rbNone) {
                        threeCharOfpieceName = ".";
                    }
                }
                secondStr += threeCharOfpieceName + "\t";
            }
            secondStr += "|";
            secondList.add(secondStr);
        }
        String finalStr = "";
        for (int i = 0; i < fRows; i++) {
            String f1 = firstList.get(i);
            String f2 = secondList.get(i);
            String f3 = f1 + "\t\t" + f2 + "\n";
            finalStr += f3;
        }
        System.out.println(finalStr);
        return finalStr;
    }

    //=======================================================================
    /** Set the piece at the specified position (r,c).
     *  This function should NOT change the underlying Board pieces contents
     *  (e.g., cWater) at the specified location.
     *  @param r is the row
     *  @param c is the col
     *  @param p should/must be rbNone or rRat ... rElephant or bRat ...
     *         bElephant.
     *  @todo v2
     */
    public void setPiece ( int r, int c, Piece p ) {
        Base b1 = getBase(r,c);
        if(b1!=Base.cWater || b1!=Base.cBTrap || b1!=Base.cRTrap
                || b1!=Base.cBDen || b1!=Base.cRDen){
            piece[r][c]=p;
        }
    }
    //-----------------------------------------------------------------------
    /** Given a piece p, return its rank (or 0 for an unknown piece).
     *  Rat is 1, cat is 2, dog is 3, wolf is 4, leopard is 5, tiger is 6,
     *  lion is 7, elephant is 8, regardless of color.
     *  @param p is a piece
     *  @return the piece's rank
     *  @todo v2
     */
    public static int getRank ( Piece p ) {
        if(p!=null) {
            if (p == Piece.bRat || p == Piece.rRat) {
                return 1;
            } else if (p == Piece.bCat || p == Piece.rCat) {
                return 2;
            } else if (p == Piece.bDog || p == Piece.rDog) {
                return 3;
            } else if (p == Piece.bWolf || p == Piece.rWolf) {
                return 4;
            } else if (p == Piece.bLeopard || p == Piece.rLeopard) {
                return 5;
            } else if (p == Piece.bTiger || p == Piece.rTiger) {
                return 6;
            } else if (p == Piece.bLion || p == Piece.rLion) {
                return 7;
            } else if (p == Piece.bElephant || p == Piece.rElephant) {
                return 8;
            }
        }
        return 0;
    }
    //-----------------------------------------------------------------------
    /** Return the rank of the piece at the specified position (or 0 for none).
     *  Rat is 1, cat is 2, dog is 3, wolf is 4, leopard is 5, tiger is 6, lion
     *  is 7, elephant is 8, regardless of color.
     *  @return the rank of the piece at the specified position (or 0 for none).
     *  @todo v2
     */
    public int getRank ( int r, int c ) {
        Piece p = getPiece(r,c);
        if(p==Piece.bRat || p==Piece.rRat){
            return 1;
        }else if(p==Piece.bCat || p==Piece.rCat){
            return 2;
        }else if(p==Piece.bDog || p==Piece.rDog){
            return 3;
        }else if(p==Piece.bWolf || p==Piece.rWolf){
            return 4;
        }else if(p==Piece.bLeopard || p==Piece.rLeopard){
            return 5;
        }else if(p==Piece.bTiger || p==Piece.rTiger){
            return 6;
        }else if(p==Piece.bLion || p==Piece.rLion){
            return 7;
        }else if(p==Piece.bElephant || p==Piece.rElephant){
            return 8;
        }
        return 0;
    }
    //-----------------------------------------------------------------------
    /** Returns the color of the piece (or GameColor.None).
     *  @return the color of the piece (or GameColor.None).
     *  @todo v2
     */
    public static GameColor getColor ( Piece p ) {
        if(p!=null) {
            if (p == Piece.rRat || p == Piece.rCat
                    || p == Piece.rDog || p == Piece.rWolf
                    || p == Piece.rLeopard || p == Piece.rTiger
                    || p == Piece.rLion || p == Piece.rElephant) {
                return GameColor.Red;
            } else if (p == Piece.bRat || p == Piece.bCat
                    || p == Piece.bDog || p == Piece.bWolf
                    || p == Piece.bLeopard || p == Piece.bTiger
                    || p == Piece.bLion || p == Piece.bElephant) {
                return GameColor.Blue;
            }
        }
        return GameColor.None;
    }
    //-----------------------------------------------------------------------
    /** Returns the color of the piece (or GameColor.None) at the specified
     *  location.
     *  @return the color of the piece (or GameColor.None) at the specified
     *  location.
     *  @todo v2
     */
    public GameColor getColor ( int r, int c ) {
        Piece p = getPiece(r,c);
        if(p!=null){
            if(p==Piece.rRat || p==Piece.rCat
                    || p==Piece.rDog || p==Piece.rWolf
                    || p==Piece.rLeopard || p==Piece.rTiger
                    || p==Piece.rLion || p==Piece.rElephant){
                return GameColor.Red;
            }else  if(p==Piece.bRat || p==Piece.bCat
                    || p==Piece.bDog || p==Piece.bWolf
                    || p==Piece.bLeopard || p==Piece.bTiger
                    || p==Piece.bLion || p==Piece.bElephant){
                return GameColor.Blue;
            }
        }
        return GameColor.None;
    }
    //-----------------------------------------------------------------------
    /** Returns t if this spot does not have any (moveable) piece on it;
     *  f otherwise or if out of bounds.
     *  @return t if this spot does not have any (moveable) piece on it;
     *  f otherwise or if out of bounds.
     *  @todo v2
     */
    public boolean isEmpty ( int r, int c ) {
        Piece p = getPiece(r,c);
        if(p==Piece.rbNone){
            return true;
        }
        return false;
    }
    //-----------------------------------------------------------------------
    /** Count the number of rats in the water.
     *  @return the count of rats in the water.
     *  @todo v2
     */
    public int countOfRatsInWater ( ) {
        int countOfRatsInWater=0;
        for (int i=0;i<piece.length;i++){
            for (int j=0;j<piece[i].length;j++){
                if(piece[i][j]==Piece.bRat || piece[i][j]==Piece.rRat){
                    countOfRatsInWater++;
                }
            }
        }
        return countOfRatsInWater;
    }
    //=======================================================================
    // v3 (version 3): isValidMove, doMove
    //-----------------------------------------------------------------------
    /** Use these rules for game play (no variations except for the one noted
     *  below):
     *  http://veryspecial.us/free-downloads/AncientChess.com-DouShouQi.pdf
     *  This is the most challenging part of the assignment.
     *  <p>
     *  Required variation (from https://en.wikipedia.org/wiki/Jungle_(board_game)):
     *  "All traps are universal. If an animal goes into a trap in its own
     *  region, an opponent animal is able to capture it regardless of rank
     *  difference if it is beside the trapped animal."  This avoids the
     *  known draw described in http://www.chessvariants.com/other.dir/shoudouqi2.html.
     *  </p>
     *  <p>
     *  Clarification: do not allow moves/captures where the attacker "loses."
     *  For example, do not allow the mouse to attack the lion and "lose"
     *  to the lion on purpose and be removed. </p>
     *  <p>
     *  As much as possible, use the functions that you have already defined.
     *  </p>
     *  @return true if the proposed move is valid
     *  <u><i><b>regardless of whose turn it is</b></i></u>;
     *  false otherwise.
     * @todo v3
     */
    protected boolean isValidMove ( int fromRow, int fromCol, int toRow, int toCol ) {
        return false;
    }
    //-----------------------------------------------------------------------
    /** Perform the specified move (update the piece array), but only if it's
     *  valid!
     *
     *  Hints: You must check whose turn it is. You should call
     *  <em>isValidMove</em>. Also, set <i>moveWasCapture</i> to true iff the
     *  move is valid and it resulted in a capture; otherwise, set it to false.
     *  Also, indicate that it the other player's turn (iff valid).
     *
     *  @return true if the proposed move is valid; false otherwise.
     *  @todo v3
     */
    public boolean doMove ( int fromRow, int fromCol, int toRow, int toCol ) {
        return false;
    }
    //=======================================================================
    // v4 (version 4): countBlue, countRed, isRedWinner, isBlueWinner,
    //                 copy ctor, equals, equalsBoard (?)
    // -----------------------------------------------------------------------
    /** @return the number of blue pieces remaining.
     *  @todo v4
     */
    public int countBlue ( ) {
        return -1;
    }
    //-----------------------------------------------------------------------
    /** @returns the number of red pieces remaining.
     *  @todo v4
     */
    public int countRed ( ) {
        return -1;
    }
    //-----------------------------------------------------------------------
    /** @return true if red is a winner (regardless of whose turn it is);
     *  false otherwise.
     *  accomplish this by checking if a red piece is in the blue den
     *  or when red has remaining pieces and blue does not.
     *  @todo v4
     */
    public boolean isRedWinner ( ) {
        return false;
    }
    //-----------------------------------------------------------------------
    /** @return true if blue is a winner (regardless of whose turn it is);
     *  false otherwise.
     *  accomplish this by checking if a blue piece is in the red den
     *  or when blue has remaining pieces and red does not.
     *  @todo v4
     */
    public boolean isBlueWinner ( ) {
        return false;
    }
    //-----------------------------------------------------------------------
    /** copy ctor.
     *  make this a separate, independent (deep) copy of the original.
     *  @param original is the original Board to copy.
     *  @todo v4
     */
    public Board ( final Board original ) {
        super();
    }
    //-----------------------------------------------------------------------
    /** this is a "proper" equals method.
     *  @return true if the Board _objects_ are equal; false otherwise.
     *  @todo v4
     */
    @Override
    public boolean equals ( Object other ) {
        return false;
    }
    //-----------------------------------------------------------------------
    /** this is NOT a "proper" equals method but can be used by a "proper" one.
     *  do not consider anything else for equality; only consider the array
     *  contents. you may assume that this.piece and otherPiece are both not
     *  null, properly initialized, and both of the same size.
     *  @return true if the piece array _contents_ are equal; false otherwise.
     *  @todo v4
     */
    @PublicForTesting( shouldBe="private" )
    public boolean equals ( Piece[][] otherPiece ) {
        return false;
    }
    //-----------------------------------------------------------------------
    // **** this function is not required / tested / necessary.
    // **** students may assume that the underlying Board base is always the same. ****
    //
    // this is NOT a "proper" equals method but can be used by a "proper" one.
    // return true if the piece array _contents_ are equal; false otherwise.
    // do not consider anything else for equality; only consider the array contents.
    // you may assume that this.piece and otherPiece are both not null, properly
    // initialized, and both of the same size.
    @PublicForTesting( shouldBe="private" )
    public boolean equals ( Base[][] otherBase ) {
        return false;
    }
    //=======================================================================
    // v5 (version 5): hashCode, gameOver (mention change to doMove to call
    // gameOver)
    //=======================================================================
    /** this function deteremines if the game is over.
     *  <pre> when is the game over? the game is over when:
     *      a. blue is a winner, or
     *      b. red is a winner,  or when
     *      c. blue or red don't have any pieces left. </pre>
     *  @return true if the game is over; false otherwise.
     *  @todo v5
     */
    public boolean gameOver ( ) {
        return false;
    }
    //-----------------------------------------------------------------------
    /** this function overrides the default hashCode function.
     *  <p> from "Effective Java" by J. Bloch:
     *  "Item 9: Always override hashCode when you override equals" </p>
     *
     *  <p> use the string algorithm from
     *  https://en.wikipedia.org/wiki/Java_hashCode%28%29#The_java.lang.String_hash_function.
     *  simply treat the individual piece values (i.e., piece[r][c].ordinal()) as the
     *  individual character values. (DO NOT use charAt on the string returned
     *  from toString for this function.) also include the value of mBlacksTurn
     *  as described below as the last (i.e., nth) character. </p>
     *
     *  <p> DO NOT include anything else (just the piece array contents and then bluesTurn).
     *  It quickly becomes too complicated. </p>
     *
     *  @return the calculated hash code.
     *  @todo v5
     */
    @Override
    public int hashCode ( ) {
        return -1;
    }
    //=======================================================================
    /** create a list of all possible moves given this as the starting
     *  position.
     *  @return the list of all possible moves.
     *  @todo v6
     */
    public ArrayList< Board > suggest ( ) {
        ArrayList< Board > list = new ArrayList<>();
        return list;
    }
    //-----------------------------------------------------------------------
    public static void serialize ( String out, ArrayList< Board > list ) {
        FileOutputStream fout;
        try {
            fout = new FileOutputStream( out );
        } catch (Exception e) {
            System.err.println( "MyBoard.serialize: failed to open file output stream " + e );
            return;
        }
        ObjectOutputStream oout;
        try {
            oout = new ObjectOutputStream( fout );
        } catch (Exception e) {
            System.err.println( "MyBoard.serialize: failed to open object output stream " + e );
            return;
        }
        try {
            oout.writeObject( list );
        } catch (Exception e) {
            System.err.println( "MyBoard.serialize: failed to write object(s) " + e );
            return;
        }

        //save toString versions of the Boards as well.
        String s = "\n\n";
        for (Board b : list)    s += b;
        s += "\n";
        try {
            oout.writeObject( s );
        } catch (Exception e) {
            System.err.println( "MyBoard.serialize: failed to write object(s) " + e );
            return;
        }

        try {
            oout.close();
            fout.close();
        } catch (Exception ignored) { }
    }
    //-----------------------------------------------------------------------
    @SuppressWarnings( "unchecked" )  //should use try-catch instead but i'm lazy.
    public static ArrayList< Board > deserialize ( String in ) {
        FileInputStream fin;
        try {
            fin = new FileInputStream( in );
        } catch ( Exception e) {
            System.err.println( "MyBoard.deserialize: failed to open file input stream " + e );
            return null;
        }
        ObjectInputStream oin;
        try {
            oin = new ObjectInputStream( fin );
        } catch (Exception e) {
            System.err.println( "MyBoard.serialize: failed to open object input stream " + e );
            return null;
        }

        ArrayList< Board > list;
        try {
            list = (ArrayList< Board >) oin.readObject();
        } catch (Exception e) {
            System.err.println( "MyBoard.serialize: failed to read object(s) " + e );
            return null;
        }

        String s;
        try {
            s = (String) oin.readObject();
        } catch (Exception e) {
            System.err.println( "MyBoard.serialize: failed to read object(s) " + e );
            return null;
        }
        System.out.println( s );

        try {
            oin.close();
            fin.close();
        } catch (Exception ignored) { }

        return list;
    }
    //=======================================================================
    /** call suggest() and choose the best from the last.
     *  to start, you may choose one at random but you will NOT receive any
     *  credit for that approach.
     *  @return the best
     *  @todo v7
     */
    public Board chooseBest ( ) {
        ArrayList< Board > list = suggest();
        Board best = null;

        //todo: add your code here.
        // call h for each element in list and pick the best.

        return best;
    }

    //define infinity with a little headroom
    public static final int INF = Integer.MAX_VALUE / 2;

    //this function evaluates the current Board piece configuration, b, and returns
    // a value in [-INF, ..., +INF] where +INF indicates a winner, -INF is a
    // loser, and values in between are better (greater than) or worse (less
    // than) a particular alternative.
    //
    // question: "Which player is the maximizing one?" Stated another way,
    // "If bluesTurn is true and the current Board being evaluated by h is a
    // winner for red, should that be assigned -INF or +INF?"
    //
    // answer: +INF because we arrived at this current state after red moved
    // and the game would end before blue gets the next turn (and similarly
    // for the case when bluesTurn is false, a blue winner is assigned +INF.
    //
    // consider the converse. "If bluesTurn is true and the current Board
    // being evaluated by h is a winner for blue, should that be assigned
    // -INF or +INF?" -INF because we arrived at this current state after
    // red moved and red lost.
    //
    // of course, if there is no clear winner, h should return a value in
    // between -INF and +INF with values biased towards the favorite (the
    // maximizing player).
    public static int h ( Board b ) {
        if (b.bluesTurn) {  //it's blues turn next (after this)
            //eval for red (because red moved to get to this position)
            if (b.isRedWinner())   return INF;
            if (b.isBlueWinner())  return -INF;
            //this is where the hard work begins. calc a value between
            // [-INF,        ..., +INF]
            // [blue winner, ..., red winner]
            // [red loser,   ..., red winner]
            //Evaluate e = new Evaluate( b );
        } else {  //it's reds turn next (after this)
            //eval for blue (because blue moved to get to this position)
            if (b.isBlueWinner())  return INF;
            if (b.isRedWinner())   return -INF;
            //this is where the hard work begins. calc a value between
            // [-INF,       ..., +INF]
            // [red winner, ..., blue winner]
            // [blue loser, ..., blue winner]
            //Evaluate e = new Evaluate( b );
        }
        return 0;
    }

}  //end class Board
