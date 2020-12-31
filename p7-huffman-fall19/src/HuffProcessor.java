import java.util.PriorityQueue;

/**
 * Although this class has a history of several years,
 * it is starting from a blank-slate, new and clean implementation
 * as of Fall 2018.
 * <P>
 * Changes include relying solely on a tree for header information
 * and including debug and bits read/written information
 * 
 * @author Owen Astrachan
 */

public class HuffProcessor {

	public static final int BITS_PER_WORD = 8;
	public static final int BITS_PER_INT = 32;
	public static final int ALPH_SIZE = (1 << BITS_PER_WORD);
	public static final int PSEUDO_EOF = ALPH_SIZE;
	public static final int HUFF_NUMBER = 0xface8200;
	public static final int HUFF_TREE  = HUFF_NUMBER | 1;

	private final int myDebugLevel;
	
	public static final int DEBUG_HIGH = 4;
	public static final int DEBUG_LOW = 1;
	
	public HuffProcessor() {
		this(0);
	}
	
	public HuffProcessor(int debug) {
		myDebugLevel = debug;
	}

	/**
	 * Compresses a file. Process must be reversible and loss-less.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be compressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	public void compress(BitInputStream in, BitOutputStream out){
		int[] counts = readForCounts(in);
		HuffNode root = makeTreeFromCounts(counts);
		String[] codings = makeCodingsFromTree(root);

		out.writeBits(BITS_PER_INT,HUFF_TREE);
		writeHeader(root,out);

		in.reset();
		writeCompressedBits(codings,in,out);
		out.close();

	}

	/**
	 * write the tree that will go at the beginning of the code
	 * @param root the node representing the root of the Huffman tree
	 * @param out the data being written out
	 */

	private void writeHeader(HuffNode root, BitOutputStream out) {
		if(root.myLeft != null && root.myRight != null)	//if it's an internal node
		{
			out.writeBits(1,0);	//write out a bit of 0 to represent the internal node
			writeHeader(root.myLeft,out);	//call recursively on left child
			writeHeader(root.myRight,out);	//call recursively on right child
		}
		if(root.myRight==null && root.myLeft == null)	//if the node is a leaf
		{
			out.writeBits(1,1);	//write a single bit of 1 to represent the leaf node
			out.writeBits(BITS_PER_WORD+1,root.myValue);	//write out 9 bits to represent the character
		}
	}

	/**
	 * convert path encodings to a bit sequence
	 * @param codings String array containing the paths of each 8-bit character
	 * @param in the data being read
	 * @param out the data being written out
	 */

	private void writeCompressedBits(String[] codings, BitInputStream in, BitOutputStream out) {
		while (true){
			int val = in.readBits(BITS_PER_WORD); //read the data
			if (val == -1)	//if it's -1, that mean it's hit the end
			{
				String code = codings[PSEUDO_EOF];	//so the path is the encoding for PSEUDO_EOF
				out.writeBits(code.length(),Integer.parseInt(code,2)); //convert this to a bit sequence
				return; //halt the method
			}
			else {
				String code = codings[val]; //otherwise, get the path for the character
				out.writeBits(code.length(), Integer.parseInt(code, 2)); //and it to a bit sequence
			}
		}
	}

	/**
	 *
	 * @param root the node representing the root of teh Huffman tree
	 * @return a String array containing the paths to each 8-bit character
	 */

	private String[] makeCodingsFromTree(HuffNode root) {
		String[] encodings = new String[ALPH_SIZE+1];	//create a String array of all possible character including PSEUDO_EOF
		codingHelper(root,"",encodings); //call the helper method that will populate this array
		return encodings;
	}

	/**
	 * helper method to populate the String array for makeCodingsFromTree
	 * @param root node representing the root of the Huffman tree
	 * @param path current path containing encodings
	 * @param encodings String array containing all the codings
	 */

	private void codingHelper(HuffNode root, String path, String[] encodings) {
		if(root.myRight==null && root.myLeft==null)	//if the node is a leaf
		{
			encodings[root.myValue] = path;	//add the completed path to the array
			return;
		}
		codingHelper(root.myLeft,path+"0",encodings);	//otherwise, add to the path and call the method recursively on the left child to finish
		codingHelper(root.myRight,path+"1",encodings);//otherwise, add to the path and call the method recursively on the right child to finish
	}

	/**
	 * use the greedy algorithm to make the Huffman Tree
	 * @param counts the integer array containing the frequencies of each 8-bit character
	 * @return a HuffNode representing the root of the Huffman Tree
	 */

	private HuffNode makeTreeFromCounts(int[] counts) {
		PriorityQueue<HuffNode> pq = new PriorityQueue<>();
		for(int i = 0; i < counts.length; i++)
		{
			if(counts[i] > 0) //if the 8-bit frequency actually appears
			{
				pq.add(new HuffNode(i,counts[i],null,null)); //put its node into the priority queue
			}
		}
		while(pq.size() > 1)
		{
			HuffNode left = pq.remove();	//remove a node from the queue, which is sorted by weight
			HuffNode right = pq.remove();	//remove a node from the queue, which is sorted by weight
			HuffNode temp = new HuffNode(0,left.myWeight+right.myWeight,left,right);	//create a tree with the nodes removed
			pq.add(temp); //add it back to the queue
		}
		HuffNode root = pq.remove();	//ultimately, the last item is a node representing the root, which is linked to the rest of the tree
		return root;

	}

	/**
	 *
	 * @param in the data stream
	 * @return an integer array representing the frequencies of 8-bit characters
	 */

	private int[] readForCounts(BitInputStream in) {
		int[] freq = new int[ALPH_SIZE+1];	//256+1 (including PSEUDO_EOF)
		freq[PSEUDO_EOF] = 1;	//PSEUDO_EOF should only appear once
		while (true){
			int val = in.readBits(BITS_PER_WORD);	//read the bits
			if (val == -1) break;	//shouldn't be -1, so break if it is
			freq[val] += 1;	//otherwise, increment the number of times the 8-bit characters has appeared already
		}
		return freq;
	}

	/**
	 * Decompresses a file. Output file must be identical bit-by-bit to the
	 * original.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be decompressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	public void decompress(BitInputStream in, BitOutputStream out){

		int magic = in.readBits(BITS_PER_INT);	//reads the magic number to check whether the file is Huffman encoded
		if (magic != HUFF_TREE) {
			throw new HuffException("invalid magic number "+magic);	//throw an exception if it's not
		}
		HuffNode root = readTree(in);	//if it is, read the tree
		HuffNode current = root;	//establish the root
		while (true){
			int val = in.readBits(1);
			if (val == -1)
			{
				throw new HuffException("bad input, no PSEUDO_EOF");	//throw exception if there's no signal of end
			}
			else
			{
				if(val == 0)
				{
					current = current.myLeft;	//0 means traverse to the left
				}
				else
				{
                    current = current.myRight;	//1 means traverse to the right
				}
				if(current.myLeft == null && current.myRight==null)	//if the node is a leaf
				{
					if(current.myValue == PSEUDO_EOF)
					{
						break;	//and the leaf signals the end, break out of the loop
					}
					else
					{
						out.writeBits(BITS_PER_WORD, current.myValue);	//otherwise the leaf contains a letter, so write it out
						current = root; //reset the node back to the root
					}
				}
			}
		}
		out.close();	//close the output file
	}

	/**
	 * helper method to read the tree
	 * @param in the data stream that the program reads
	 * @return a HuffNode that is the root of the tree created
	 */
	private HuffNode readTree(BitInputStream in)
	{
		int bit = in.readBits(1);	//reading one bit
		if(bit == -1)
		{
			throw new HuffException("reading bits has failed");	//bit shouldn't = -1, so there an exception if it is
		}
		if(bit == 0)	//that means it's an internal node, so use recursion to continue reading
		{
			HuffNode left = readTree(in);
			HuffNode right = readTree(in);
			return new HuffNode(0,0,left,right); //make the node
		}
		else //if bit = 1, then it's a leaf representing a letter
		{
			int value = in.readBits(BITS_PER_WORD+1);	//store the value as 9 bits
			return new HuffNode(value,0,null,null);	//create the leaf node
		}
	}
}