package com.dsa;

import java.util.*;

public class TypeAhead {

/*
    You need to implement search typeahead on a single machine. There are 2 kinds of functions that you have to implement :-
    incrementSearchTermFrequency(string search_term, int increment) - Increase the frequency of search_term by increment .
    findTopXSuggestion(string queryPrefix, int X) - Find the top X (X <= 5) search terms i.e. 5 terms that have queryPrefix as a strict prefix and have the highest frequency in the eligible set.
    Note:- For two strings with the same frequency, we give more preference to the lexicographically larger one.
    For the second function, if there are fewer than X strings with the given prefix then add empty strings.
    Also, the strings must be returned after sorting them lexicographically.

    Constraints
    Sum of increment over all test cases <= 109
    1 <= X <= 5
    'a' <= queryPrefix <= 'z'
 */

    public static void main(String[] args) {
        var problem = new TypeAhead();
        problem.testCase1();
    }

    void testCase1() {
        String[] expected = new String[]{"a", "aabbc", "abc", "ac"};
        var B = new int[]{1, 7, 1, 36, 1, 48, 1, 40, 1, 35, 1, 21, 1, 7, 1, 96, 1, 31};
        var C = new String[]{"babc", "ac", "aabbc", "b", "abc", "bbbac", "a", "b", "a"};

        for (int i = 0; i < B.length; i = i + 2) {
            if (B[i] == 1) {
                int freq = B[i + 1];
                //insert.
                incrementSearchTermFrequency(C[i / 2], freq);
            }
        }
        var res = findTopXSuggestion("a", 4);
        System.out.println(Arrays.equals(res, expected));
    }

    class TrieNode {
        TrieNode[] child = new TrieNode[26]; //'a'-'z' 26 alphabets
        TreeMap<String, Integer> searchFreqMap = new TreeMap<>(Collections.reverseOrder());   //will store the related search query here, in descending frequency.
        TreeMap<Integer, String> freqSearchMap = null;   //will store the related search query here, in descending frequency.
    }

    final TrieNode root = new TrieNode();

    // final HashMap<String, Integer> searchFreqMap = new HashMap<>();
    public void incrementSearchTermFrequency(String search_term, int increment) {
//         System.out.printf("I %s %s|",search_term,increment);
        // searchFreqMap.merge(search_term, increment, Integer::sum);  //store overall map.
        TrieNode curr = root;
        for (int i = 0; i < search_term.length(); i++) {
            char ch = search_term.charAt(i);
            int arrayPos = ch - 'a';    // Since chars are a-z, index will be 0 to 26

            if (curr.child[arrayPos] == null) {
                curr.child[arrayPos] = new TrieNode();
            }
            curr = curr.child[arrayPos];    //update curr node to the character node.
            int freq = curr.searchFreqMap.getOrDefault(search_term, 0) + increment; // if search_term exists get that value, else use the input.
            curr.searchFreqMap.put(search_term, freq);      //Store search <-> frequency

            curr.freqSearchMap = new TreeMap<>(Collections.reverseOrder());
            for (Map.Entry<String, Integer> map : curr.searchFreqMap.entrySet()) {
                // stores the frequency <-> search
                curr.freqSearchMap.put(map.getValue(), map.getKey());
            }

        }
    }

    public String[] findTopXSuggestion(String queryPrefix, int X) {
//         System.out.printf("Q %s %s|",queryPrefix,X);
        List<String> topFrequenciesSuggestions = new ArrayList<>();
        TrieNode curr = root;
        for (int i = 0; i < queryPrefix.length(); i++) {
            char ch = queryPrefix.charAt(i);
            int arrayPos = ch - 'a';    // Since chars are a-z, index will be 0 to 26

            if (curr.child[arrayPos] == null) {
                return getResult(X, topFrequenciesSuggestions);   //return empty if prefix not matched.
            }
            curr = curr.child[arrayPos];    //update curr node to the character node.
        }

//        System.out.println("MAP - " + curr.searchFreqMap);

        // Sort the entries by values in descending order
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(curr.searchFreqMap.entrySet());
        // Currently optimised for entry., need to move below logic to insert search_term to optimise for read speeds.
        sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (int i = 0; i < X && i < sortedEntries.size(); i++) {
            topFrequenciesSuggestions.add(sortedEntries.get(i).getKey());
        }
        return getResult(X, topFrequenciesSuggestions);
    }

    private String[] getResult(int X, List<String> topFrequenciesSuggestions) {
        String[] result = new String[X];
        Arrays.fill(result, "");
        int N = topFrequenciesSuggestions.size();
        for (int i = 0, j = X - N; j < X; i++, j++) {
            result[j] = topFrequenciesSuggestions.get(i);
        }
        Arrays.sort(result);
        return result;
    }
}