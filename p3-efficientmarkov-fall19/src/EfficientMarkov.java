import java.util.*;

public class EfficientMarkov extends BaseMarkov {
	/**
	 * Constructor with a default order of 3
	 */
	private Map<String,ArrayList<String>> myMap;
	public EfficientMarkov()
	{
		this(3);
	}

	/**
	 * Construct an EfficientMarkov object with the specified order
	 * initialize a new hashMap object called myMap
	 * @param order size of this markov generator
	 */
	public EfficientMarkov(int order)
	{
		super(order);
		myMap = new HashMap<>();
	}

	/**
	 * update the training text
	 * clear the hashMap object, so it can be used for this new training text
	 * update the hashMap object with keys and values based on the new text
	 * @param text the new training text that's being used
	 */
	public void setTraining(String text) {
		super.setTraining(text);
		myMap.clear();
		for(int i = 0; i <= myText.length()-myOrder; i++)
		{
			myMap.putIfAbsent(myText.substring(i,i+myOrder), new ArrayList<String>());
			if(i+myOrder >= myText.length())
			{
				myMap.get(myText.substring(i,i+myOrder)).add(PSEUDO_EOS);
			}
			else
			{
				myMap.get(myText.substring(i,i+myOrder)).add(myText.substring(i+myOrder,i+myOrder+1));
			}

		}
	}

	/**
	 * return the ArrayList of Strings (values) that follows the String (the key)
	 * @param key the Strings stored in the hashMap
	 * @return the value/ArrayList of Strings that follows the key
	 * @throws NoSuchElementException when the key doesn't exist
	 */
	public ArrayList<String> getFollows(String key) throws NoSuchElementException
	{
		if(!myMap.containsKey(key))
		{
			throw new NoSuchElementException(key+" not in map");
		}
		return myMap.get(key);
	}
}	
