// Jaysheel Pandya
// CSE 143 DD with Xunmei Liu
// Homework 4 Bonus
// The HangmanManager2 class extends the HangmanManager class.
// It makes the game more evil by instantly ending if possible
// when there is one guess left, and also makes sure the client
// cannot modify the sets of words left and guesses taken.

import java.util.*;

public class HangmanManager2 extends HangmanManager {

    Set<String> wordsSet;

    Set<Character> guessesSet;

    // Calls the superclass constructor
    // Sets the unmodifiable set of words and guesses
    public HangmanManager2(Collection<String> dictionary, int length, int max) {
        super(dictionary, length, max);
        wordsSet = Collections.unmodifiableSet(super.words());
        guessesSet = Collections.unmodifiableSet(super.guesses());
    }

    // Returns the unmodifiable set of possible words left in the game
    public Set<String> words() {
        return wordsSet;
    }

    // Returns the unmodifiable guesses taken by the player so far in the game
    public Set<Character> guesses() {
        return guessesSet;
    }
    // Overrides record method
    // Checks if it is the last guess, and if so, attempts to
    // alter the dictionary in order to instantly end the game
    // Set of words and guesses can no longer be directly altered
    // by the client
    public int record(char guess) {
        Set<String> s1 = super.words();
        Set<Character> s2 = super.guesses();
        if (guessesLeft() == 1) {
            Iterator<String> iterator = super.words().iterator();
            boolean notYet = true;
            while (iterator.hasNext() && notYet) {
                String word = iterator.next();
                if (!word.contains("" + guess)) {
                    super.words().clear();
                    super.words().add(word);
                    notYet = false;
                }
            }
        }
        int n = super.record(guess);
        if (s1 != super.words()) {
            wordsSet = Collections.unmodifiableSet(super.words());
        }
        if (s2 != super.guesses()) {
            guessesSet = Collections.unmodifiableSet(super.guesses());
        }
        return n;
    }
}