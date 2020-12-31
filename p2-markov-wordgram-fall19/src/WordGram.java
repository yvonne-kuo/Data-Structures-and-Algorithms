import java.util.ArrayList;

/**
 * A WordGram represents a sequence of strings
 * just as a String represents a sequence of characters
 * 
 * Yvonne Kuo
 *
 */
public class WordGram {
	
	private String[] myWords;   
	private String myToString;  // cached string
	private int myHash;         // cached hash value

	/**
	 * Create WordGram by creating instance variable myWords and copying
	 * size strings from source starting at index start
	 * @param source is array of strings from which copying occurs
	 * @param start starting index in source for strings to be copied
	 * @param size the number of strings copied
	 */
	public WordGram(String[] source, int start, int size) {
		myWords = new String[size];
		for(int i = 0; i < myWords.length; i++)
		{
			myWords[i] = source[start];
			start++;
		}
		myToString = "";
		myHash = 0;

	}

	/**
	 * Return string at specific index in this WordGram
	 * @param index in range [0..length() ) for string 
	 * @return string at index
	 */
	public String wordAt(int index) {
		if (index < 0 || index >= myWords.length) {
			throw new IndexOutOfBoundsException("bad index in wordAt "+index);
		}
		return myWords[index];
	}

	/**
	 * @return an int representing the length of the
	 * myWords instance variable
	 */
	public int length(){
		return myWords.length;
	}


	/**
	 * @param o
	 * @return a boolean value to indicate
	 * if the object taken as a parameter
	 * is a replicate of this WordGram object
	 */
	public boolean equals(Object o) {
		if (! (o instanceof WordGram) || o == null){
			return false;
		}
		WordGram wg = (WordGram) o;
		if(wg.length() != this.length())
		{
			return false;
		}
		for(int i = 0; i < myWords.length; i++)
		{
			if(!(wg.wordAt(i).equals(this.wordAt(i))))
			{
				return false;
			}

		}

		return true;
	}

	/**
	 * @return a unique hashcode for this WordGram
	 * object
	 */
	public int hashCode(){
		if(myHash == 0)
		{
			myHash = this.toString().hashCode();
		}
		return myHash;
	}
	

	/**
	 * @param last is last String of returned WordGram
	 * @return a WordGram object in which the first
	 * element has been deleted, the rest has been shifted down,
	 * and the "last" input has been added as the last element
	 */
	public WordGram shiftAdd(String last) {
		WordGram wg = new WordGram(myWords,0,myWords.length);
		for(int i = 0; i < wg.length()-1; i++)
		{
			wg.myWords[i] = wg.myWords[i+1];
		}
		wg.myWords[wg.length()-1] = last;
		return wg;
	}

	/**
	 * @return a String representation of
	 * this WordGram object
	 */
	public String toString(){
		if(myToString.equals(""))
		{
			myToString = String.join(" ", myWords);
		}
		return myToString;
	}
}
