import java.util.LinkedHashSet;

public class LinkStrand implements IDnaStrand {
    private Node myFirst, myLast;
    private long mySize;
    private int myAppends;
    private Node myCurrent;
    private int myLocalIndex;
    private int myIndex;

    /**
     * Creates an empty strand
     */
    public LinkStrand()
    {
        this("");
    }

    /**
     * Create a strand representing s, where s is a string of nucleotide(s)
     * @param s
     */
    public LinkStrand(String s)
    {
        initialize(s);
    }

    /**
     * @return the size of the strand/total number of nucleotides
     */
    @Override
    public long size() {
        return mySize;
    }

    /**
     * Initialize this strand so that it represents the value of source.
     * Assign pointers to the node that represents the string of nucleotides
     * sets the default values for the instance variables
     * @param source
     */
    @Override
    public void initialize(String source) {
        myFirst = new Node(source);
        myLast = myFirst;
        myAppends = 0;
        mySize = source.length();
        myCurrent = myFirst;
        myIndex = 0;
        myLocalIndex = 0;
    }

    /**
     * @param source, a String representing the nucleotides, is data from which object constructed
     * @return a LinkStrand representing the nucleotides
     */

    @Override
    public IDnaStrand getInstance(String source) {
        return new LinkStrand(source);
    }

    /**
     *
     * @param dna is the string appended to this strand
     * @return a LinkStrand with the String dna appended to the end of this LinkStrand object
     * where dna is converted to a node
     */
    @Override
    public IDnaStrand append(String dna) {
        myLast.next = new Node(dna);
        myLast = myLast.next;
        mySize += dna.length();
        myAppends++;
        return this;
    }

    /**
     * reverses the order of the nucleotides in this object
     * @return a separate LinkStrand object in which the order of the nucleotides is reversed
     */

    @Override
    public IDnaStrand reverse() {
        Node pointer = myFirst;
        LinkStrand current;
        LinkStrand reversed = new LinkStrand();
        while(pointer != null)
        {
            current = new LinkStrand((new StringBuilder(pointer.info)).reverse().toString());
            current.append(reversed.toString());
            reversed = current;
            pointer = pointer.next;
        }
        return reversed;
    }

    /**
     * @return the number of times new dna has been appended to the end of this LinkStrand
     * object
     */
    @Override
    public int getAppendCount() {
        return myAppends;
    }

    /**
     *
     * @param index specifies at which index will the character be returned
     * @return the character, representing a nucleotide, at the given index of the
     * LinkStrand object
     * @throws IndexOutOfBoundsException
     */
    @Override
    public char charAt(int index) throws IndexOutOfBoundsException
    {
        if(index >= mySize || index < 0)
        {
            throw new IndexOutOfBoundsException();
        }
        if(index < myIndex)
        {
            myIndex = 0;
            myLocalIndex = 0;
            myCurrent = myFirst;
        }
        while(myIndex != index)
        {
            myIndex++;
            myLocalIndex++;
            if(myLocalIndex >= myCurrent.info.length())
            {
                myLocalIndex = 0;
                myCurrent = myCurrent.next;
            }
        }
        return myCurrent.info.charAt(myLocalIndex);
    }

    /**
     * @return the string representation of the LinkStrand object/the string of nucleotides
     */
    public String toString()
    {
        StringBuilderStrand ans = new StringBuilderStrand();
        Node point = myFirst;
        while(point != null)
        {
            ans.append(point.info);
            point = point.next;
        }
        return ans.toString();
    }

    /**
     * a nested/inner node class, so that a LinkedList can be utilized to represent dna
     * a LinkStrand object will be comprised of linked nodes
     * info will contain a dna string
     * next will reference the next node linked to this one
     */
    private static class Node
    {
        String info;
        Node next;

        public Node(String s)
        {
            info = s;
            next = null;
        }
    }

}


