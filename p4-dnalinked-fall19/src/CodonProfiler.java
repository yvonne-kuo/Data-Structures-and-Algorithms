import java.util.*;

public class CodonProfiler {
	
	/**
	 * Count how many times each codon in an array of codons occurs
	 * in a strand of DNA. Return int[] such that int[k] is number
	 * of occurrences of codons[k] in strand. Strand codons can start
	 * at all valid indexes that are multiples of 3: 0, 3, 6, 9, 12, ...
	 * @param strand is DNA to be analyzed for codon occurrences.
	 * @param codons is an array of strings, each has three characters
	 * @return int[] such that int[k] is number of occurrences of codons[k] in 
	 * strand. 
	 */
	public int[] getCodonProfile(IDnaStrand strand, String[] codons) {
		HashMap<String,Integer> map = new HashMap<>();
		for(int i = 0; i <= strand.size()-3; i+=3)
		{
			char a = strand.charAt(i);
			char b = strand.charAt(i+1);
			char c = strand.charAt(i+2);
			String codo = Character.toString(a) + Character.toString(b) + Character.toString(c);
			map.putIfAbsent(codo, 0);
			map.put(codo, map.get(codo)+1);
		}
		int[] ret = new int[codons.length];
		for(int k = 0; k < ret.length; k++)
		{
			if(map.containsKey(codons[k]))
			{
				ret[k] = map.get(codons[k]);
			}

		}
		return ret;
	}
}
