import java.io.*;
import java.util.*;
import java.util.stream.*;

//To Do: merge multiple wordLists for better detection capablities. TL;DR: the bigger the data set the better.
public class Main {

    public static final int WORD_LENGTH = 5;
    public static final int ATTEMPTS = 5;
    public static final int SUGGESTION_COUNT = 10;
    private static int passCount = 0; 
    private static List<String> wordList;
    public static void main(String[] args) throws IOException {
        System.out.println("Great Starting Words:\nSTEAK\nTREAD\nTABLE\nAUDIO\nADIEU");
         wordList = getWords();
         //mergeDataLists();

        List<String> wordBuilder = startCheck();

        List<Character> detectedLetters = new ArrayList<>();

        for (int i = 0; i < ATTEMPTS; i++) {
            System.out.print("Enter input: ");
            String guess = readInput();

            System.out.print("Enter feedback (X -> Gray/White, Y -> yellow, G -> green): ");
            String feedback = readInput();
            Clear.clear();
            updateWordBuilder(guess, feedback, detectedLetters, wordBuilder);
          System.out.println("Word Builder: " + wordBuilder);

            System.out.println("Suggestion(s):");
            printTopMatches(detectedLetters, wordBuilder, wordList);
          System.out.println("Checked " + passCount + " words");
        }
    }

    private static void printTopMatches(List<Character> detectedLetters, List<String> wordBuilder, List<String> wordList) {
      passCount = 0;  
      String word = wordBuilder.stream().collect(Collectors.joining());
        int printCount = 0;
        for (int wordIndex = 0; wordIndex < wordList.size(); wordIndex++) {
            String currentWord = wordList.get(wordIndex);
          passCount++;
           
            if (currentWord.matches(word) && allDetectedLettersPresent(currentWord, detectedLetters)) {
                System.out.println(currentWord);
                if (++printCount == SUGGESTION_COUNT) {
                    break;
                }
            }
        }
      if(printCount == 0 && printCount != SUGGESTION_COUNT){
            System.out.println("Could not find suitable word in wordList");
          }
    }

    private static boolean allDetectedLettersPresent(String currentWord, List<Character> detectedLetters) {
        for (int i = 0; i < detectedLetters.size(); i++) {
            if (!currentWord.contains(detectedLetters.get(i).toString())) {
                return false;
            }
        }
        return true;
    }

    
    private static void updateWordBuilder(String guess, String feedback, List<Character> detectedLetters, List<String> wordBuilder) {
        for (int pos = 0; pos < WORD_LENGTH; pos++) {

            char currentChar = guess.charAt(pos);
            char feedbackForCurrentChar = feedback.charAt(pos);

            if (isGrayed(feedbackForCurrentChar)) {
               
                if (detectedLetters.contains(currentChar)) {
                    String currWord = wordBuilder.get(pos);
                    String updatedWordBuilder = currWord.replace(currentChar, '\0');
                    wordBuilder.set(pos, updatedWordBuilder);
                } else {
                    
                    for (int wordIndex = 0; wordIndex < WORD_LENGTH; wordIndex++) {
                        String currWord = wordBuilder.get(wordIndex);
                        String updatedWordBuilder = currWord.replace(currentChar, '\0');
                        wordBuilder.set(wordIndex, updatedWordBuilder);
                    }
                }
            } else if (isYellow(feedbackForCurrentChar)) {
                String currWord = wordBuilder.get(pos);
                String updatedWordBuilder = currWord.replace(currentChar, '\0');
                wordBuilder.set(pos, updatedWordBuilder);

                detectedLetters.add(guess.charAt(pos));
            } else if (isGreen(feedbackForCurrentChar)) {
                
                wordBuilder.set(pos, String.valueOf(currentChar));

                detectedLetters.add(guess.charAt(pos));
            } else {
                System.out.println("Incorrect character '" + feedbackForCurrentChar + "' in feedback : " + feedback);
            }
        }
    }

    private static List<String> startCheck() {
        List<String> wordBuilder = new ArrayList<>();
        for (int i = 0; i < WORD_LENGTH; i++) {
            wordBuilder.add("[ABCDEFGHIJKLMNOPQRSTUVWXYZ]");
        }
        return wordBuilder;
    }

    private static String readInput() {
        Scanner myObj = new Scanner(System.in);
        String input = myObj.nextLine();
        return input.toUpperCase();
    }

    private static List<String> getWords() throws IOException {

        List<String> wordStart = new ArrayList<String>();

        File file = new File("germanWords.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String word;
        while ((word = br.readLine()) != null) {
            wordStart.add(word.toUpperCase());
        }

        return wordStart;
    }

    private static boolean isGrayed(char c) {
        return c == 'X' || c == 'x';
    }

    private static boolean isGreen(char c) {
        return c == 'G' || c == 'g';
    }

    private static boolean isYellow(char c) {
        return c == 'Y' || c == 'y';
    }

  //add method to merge multiple lists together 
  public static void mergeDataLists() throws IOException{
    //add data without duplicate entries

    String[] fileNames = {"englishWords.txt", "realWords.txt"};

    for (int i = 0; i < fileNames.length; i++){
      //grab the data from the files into a temporary array
      List<String> temp = new ArrayList<String>();
      System.out.println("Beginning merge for " + fileNames[i]);
      File file = new File(fileNames[i]);
      BufferedReader br = new BufferedReader(new FileReader(file));

      String word;
      while ((word = br.readLine()) != null){
        temp.add(word.toUpperCase());
      }

      //add the temp arraylist to current word list
      System.out.println("Removing Duplicates for " + fileNames[i]);
      temp.removeAll(wordList);
      System.out.println("Adding words for " + fileNames[i]);
      wordList.addAll(temp);
    }
    Collections.sort(wordList);
  }
}