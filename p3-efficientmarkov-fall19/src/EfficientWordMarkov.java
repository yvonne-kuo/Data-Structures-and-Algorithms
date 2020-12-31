import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class EfficientWordMarkov extends BaseWordMarkov
{
    /**
     * Constructor with a default order of 3
     */
    private Map<WordGram, ArrayList<String>> myMap;
    public EfficientWordMarkov()
    {
        this(3);
    }

    /**
     * Construct an EfficientWordMarkov with the specified order
     * initialize a new HashMap object called myMap
     * @param order size of this markov generator
     */
    public EfficientWordMarkov(int order)
    {
        super(order);
        myMap = new HashMap<>();
    }
    /**
     * update the training text by splitting text into an array of Strings
     * clear the hashMap object, so it can be used for this new training text
     * update the hashMap object with keys (WordGram objects) and values (Strings that follow the WordGram objects
     * in the updated String array)
     * @param text the new training text that's being used
     */
    public void setTraining(String text){
        super.setTraining(text);
        myMap.clear();
        WordGram temp;
        for(int i = 0; i <= myWords.length-myOrder; i++)
        {
            temp = new WordGram(myWords,i,myOrder);
            myMap.putIfAbsent(temp, new ArrayList<String>());
            if(i+myOrder >= myWords.length)
            {
                myMap.get(temp).add(PSEUDO_EOS);
            }
            else
            {
                myMap.get(temp).add(myWords[myOrder+i]);
            }
        }
    }
    /**
     * return the ArrayList of Strings (the value) that follows the WordGram object (the key)
     * @param key the WordGram objects stored in the myMap
     * @return the value/string that follows the key
     * @throws NoSuchElementException when the key doesn't exist
     */
    public ArrayList<String> getFollows(WordGram key) throws NoSuchElementException
    {
        if(!myMap.containsKey(key))
        {
            throw new NoSuchElementException(key+" not in map");
        }
        return myMap.get(key);
    }
}
