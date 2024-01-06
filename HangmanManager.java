// Jaysheel Pandya
// CSE 143 DD with Xunmei Liu
// Homework 4
// The HangmanManager class keeps track of the state of a game of 
// evil hangman, in which the computer does not choose a word until
// it is forced to do so

import java.util.*;

public class HangmanManager {

   // All the possible winning words in the game
   private Set<String> words;
   
   // Number of guesses the player has remaining
   private int guessesLeft;
   
   // All the guesses that the player has made
   private Set<Character> guesses;
   
   // Shows player what letters they have in the word
   // Unknown letters are represented by '-'
   private String pattern;
   
   // PRE: Target word length must be at least 1 and maximum number of wrong
   // guesses must be at least 0 (throws IllegalArgumentException otherwise).
   // POST: Sets up the game of hangman with a word length, a maximum number
   // of guesses, and a list of possible words matching the given word length
   // (without duplicates).
   // Creates the starting pattern shown to the player
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }
      
      guessesLeft = max;
      words = new TreeSet<>();
      guesses = new TreeSet<>();
      
      for (String word : dictionary) {
         if (word.length() == length && !words.contains(word)) {
            words.add(word);
         }
      }
      
      pattern = "";
      for (int i = 0; i < length; i++) {
         pattern += "-";
      }    
   }
   
   // Returns the set of possible words left in the game
   public Set<String> words() {
      return words;
   }
   
   // Returns the number of guesses left for the player
   public int guessesLeft() {
      return guessesLeft;
   }
   
   // Returns the guesses taken by the player
   public Set<Character> guesses() {
      return guesses;
   }
   
   // PRE: Current set of possible words must not be empty 
   // (throws IllegalStateException otherwise).
   // POST: Returns a pattern, separated by spaces, showing the 
   // player which letters currently match the winning word
   // Unknown letters are represented by a '-'
   public String pattern() {
      if (words.isEmpty()) {
         throw new IllegalStateException();
      }
      return pattern.replace("", " ").trim();
   }
   
   // PRE: Player must have at least one guess left and current set of 
   // possible words must not be empty (throws IllegalStateException
   // otherwise).
   // Player must not have guessed this letter already (throws 
   // IllegalArgumentException otherwise).
   // POST: Returns the number of newly revealed letters from the guess
   // Adds guess to the player's guesses
   // Updates the pattern, remaining possible winning words,
   // and number of guesses left for the player
   public int record(char guess) {
      if (guessesLeft < 1 || words.isEmpty()) {
         throw new IllegalStateException();
      }
      if (guesses.contains(guess)) {
         throw new IllegalArgumentException();
      }
      guesses.add(guess);
      
      Map<String, Set<String>> patternMap = new TreeMap<>();
      int revealedLetters = 0;
      String maxPattern = "";
      int maxWords = 0;
      
      for (String word : words) {
         String tempPattern = tempPat(word, guess);
         addToMap(patternMap, tempPattern, word);
         if (isMaxPattern(patternMap, tempPattern, maxWords, maxPattern)) {
            maxWords = patternMap.get(tempPattern).size();
            maxPattern = tempPattern;
            revealedLetters = tempLetters(word, guess);
         }
      }
      updateGame(maxPattern, patternMap, revealedLetters);
      return revealedLetters;
   }
   
   // Returns the pattern created by a specific word from the
   // remaining possible winning words
   private String tempPat(String word, char guess) {
      String p = "";
      for (int i = 0; i < word.length(); i++) {
         if (word.charAt(i) == guess) {
            p += guess;
         }
         else {
            p += pattern.charAt(i);
         }
      }
      return p;
   }
  
   // Returns the number of letters revealed by a specific word
   // from the remaining possible winning words
   private int tempLetters(String word, char guess) {
      int tempRevealedLetters = 0;
      for (int i = 0; i < word.length(); i++) {
         if (word.charAt(i) == guess) {
            tempRevealedLetters++; 
         }
      }
      return tempRevealedLetters;
   } 
   
   // Adds a pattern to the map along with its corresponding word from the
   // remaining possible winning words
   private void addToMap(Map<String, Set<String>> patternMap, String tempPattern, String word) {
      if (!patternMap.containsKey(tempPattern)) {
         patternMap.put(tempPattern, new TreeSet<>());
      }
      patternMap.get(tempPattern).add(word);
   }
   
   // Returns true if the given pattern represents the biggest number of
   // remaining possible winning words
   private boolean isMaxPattern(Map<String, Set<String>> patternMap, String tempPattern, 
      int maxWords, String maxPattern) {
      return (patternMap.get(tempPattern).size() > maxWords || (patternMap.get(tempPattern).size()
         == maxWords && tempPattern.compareTo(maxPattern) < 0));
   }
   
   // Updates the player's current pattern and the remaining possible
   // winning words
   // Updates the player's number of guesses left if the current guess is wrong
   private void updateGame(String maxPattern, Map<String, Set<String>> patternMap,
      int revealedLetters) {
      pattern = maxPattern;
      words = patternMap.get(pattern);
      if (revealedLetters == 0) {
         guessesLeft--;
      }
   }
}