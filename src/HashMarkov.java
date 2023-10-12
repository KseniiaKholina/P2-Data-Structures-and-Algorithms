import java.util.*;

public class HashMarkov implements MarkovInterface {
    
    protected String[] myWords;		// Training text split into array of words 
	protected Random myRandom;		// Random number generator
	protected int myOrder;			// Length of WordGrams used
	protected static String END_OF_TEXT = "*** ERROR ***"; 
    HashMap<WordGram, List<String>> myMap;
    // public HashMarkov() {
	// 	this(3);
	// }

	/**
	 * Initializes a model of given order and random number generator.
	 * @param order Number of words used to generate next 
	 * random word / size of WordGrams used.
	 */
	public HashMarkov(int order){
		myOrder = order;
		myRandom = new Random();
        myMap = new HashMap<>();

	}


    @Override
    public void setTraining(String text) {
        myWords = text.split("\\s+");
        myMap.clear();

        // WordGram currentWG = new WordGram(myWords, 0, myOrder);
        for (int k = 0; k < myWords.length - myOrder; k += 1) {
            WordGram currentWG = new WordGram(myWords, k, myOrder);

            String nextWord = myWords[k + myOrder];
            if (myMap.containsKey(currentWG)) {
                myMap.get(currentWG).add(nextWord);
            }
            else {
                myMap.put(currentWG, new ArrayList<>());
                myMap.get(currentWG).add(nextWord);
            }
        // currentWG = currentWG.shiftAdd(nextWord);
         
    }
}

    @Override
    public List<String> getFollows(WordGram wgram) {
        if (myMap.containsKey(wgram)) {
            return myMap.get(wgram);
        }
        else {
            return new ArrayList<>();
        }
    }
      
        
    private String getNextWord(WordGram wgram) {
		List<String> follows = getFollows(wgram);
		if (follows.size() == 0) {
			return END_OF_TEXT;
		}
		else {
			int randomIndex = myRandom.nextInt(follows.size());
			return follows.get(randomIndex);
		}
	}

    @Override
    public String getRandomText(int length) {
		ArrayList<String> randomWords = new ArrayList<>(length);
		int index = myRandom.nextInt(myWords.length - myOrder + 1);
		WordGram current = new WordGram(myWords,index,myOrder);
		randomWords.add(current.toString());

		for(int k=0; k < length-myOrder; k += 1) {
			String nextWord = getNextWord(current);
			if (nextWord.equals(END_OF_TEXT)) {
				break;
			}
			randomWords.add(nextWord);
			current = current.shiftAdd(nextWord);
		}
		return String.join(" ", randomWords);
	}

    
    @Override
    public int getOrder() {
        return myOrder;
    }

    @Override
    public void setSeed(long seed) {
        myRandom.setSeed(seed);
    }
    
}
