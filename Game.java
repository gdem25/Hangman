import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private String answer;
    private String tmpAnswer;
    private String[] letterAndPosArray;
    private String[] words;
    private int moves;
    private int index;
    private final ReadOnlyObjectWrapper<GameStatus> gameStatus;

    ReadOnlyObjectWrapper<String> temp;
    private ObjectProperty<Boolean> gameState = new ReadOnlyObjectWrapper<Boolean>();

    public enum GameStatus {
        GAME_OVER {
            @Override
            public String toString() {
                return "Game over!";
            }
        },
        BAD_GUESS {
            @Override
            public String toString() {
                return "Bad guess...";
            }
        },
        GOOD_GUESS {
            @Override
            public String toString() {
                return "Good guess!";
            }
        },
        WON {
            @Override
            public String toString() {
                return "You won!";
            }
        },
        OPEN {
            @Override
            public String toString() {
                return "Game on, let's go!";
            }
        }
    }

    public Game() {
        gameStatus = new ReadOnlyObjectWrapper<GameStatus>(this, "gameStatus", GameStatus.OPEN);
        temp = new ReadOnlyObjectWrapper<String>(this, "tmpAnswer", tmpAnswer);
//        gameStatus.addListener(new ChangeListener<GameStatus>() {
//            @Override
//            public void changed(ObservableValue<? extends GameStatus> observable,
//                                GameStatus oldValue, GameStatus newValue) {
//                if (gameStatus.get() != GameStatus.OPEN) {
//                    log("in Game: in changed");
//                    //currentPlayer.set(null);
//                }
//            }
//
//        });
        try {
            setRandomWord();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        prepTmpAnswer();
        prepLetterAndPosArray();
        moves = 0;

        gameState.setValue(false); // initial state
        createGameStatusBinding();
    }

    private void createGameStatusBinding() {
        List<Observable> allObservableThings = new ArrayList<>();
        ObjectBinding<GameStatus> gameStatusBinding = new ObjectBinding<GameStatus>() {
            {
                super.bind(gameState);
            }

            @Override
            public GameStatus computeValue() {
                log("in computeValue");
                GameStatus check = checkForWinner(index);
                if (check != null) {
                    return check;
                }

//				if(tmpAnswer.trim().length() == 0 && moves == 0){
//					log("new game");
//					return GameStatus.OPEN;
//				}
                if (index == -1) {
                    moves++;
                    log("bad guess");
                    return GameStatus.BAD_GUESS;
                    //printHangman();
                }
                else if (tmpAnswer.replaceAll("_","").trim().length() == 0 && moves == 0) {
                    log("new game");
                    return GameStatus.OPEN;
                }  else {
                    log("good guess");
                    return GameStatus.GOOD_GUESS;
                }
            }
        };
        gameStatus.bind(gameStatusBinding);
        ObjectBinding<String> answerBinding = new ObjectBinding<String>() {
            {
                super.bind(gameState);
            }

            @Override
            public String computeValue() {
                return tmpAnswer;
            }
        };
        temp.bind(answerBinding);
    }

    public ReadOnlyObjectProperty<GameStatus> gameStatusProperty() {
        return gameStatus.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<String> playerAnswer() {

        return temp.getReadOnlyProperty();
    }



    public GameStatus getGameStatus() {
        return gameStatus.get();
    }

    private void setRandomWord() throws IOException {
        ArrayList<String> words = getWordsFromFile("words.txt");
        answer = getRandomWord(words);
        //System.out.println("WORD = " + answer);
    }

    public static String getRandomWord(ArrayList<String> words) {
        Random random = new Random();
        int index = random.nextInt(words.size());
        return words.get(index);
    }

    public static ArrayList<String> getWordsFromFile(String fileName) throws IOException {
        ArrayList<String> words = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] lineWords = line.split("\\s+");
            for (String word : lineWords) {
                words.add(word);
            }
        }
        reader.close();
        return words;
    }

    private void prepTmpAnswer() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < answer.length(); i++) {
            sb.append("_ ");
        }
        tmpAnswer = sb.toString();
    }

    private void prepLetterAndPosArray() {
        letterAndPosArray = new String[answer.length()];
        for (int i = 0; i < answer.length(); i++) {
            letterAndPosArray[i] = answer.substring(i, i + 1);
        }
    }

    private int getValidIndex(String input) {
        int index = -1;
        for (int i = 0; i < letterAndPosArray.length; i++) {
            if (letterAndPosArray[i].equals(input)) {
                if(i == 0){
                    index = i;
                }
                else if (i ==1) {
                    index = i+1;
                }
                else {
                    index = i*2;
                }
              //  index = i;
                letterAndPosArray[i] = "";
                break;
            }
        }
        return index;
    }

    private int update(String input) {
        int index = getValidIndex(input);
        if (index != -1) {
            StringBuilder sb = new StringBuilder(tmpAnswer);
            sb.setCharAt(index, input.charAt(0));
            tmpAnswer = sb.toString();
        }
        return index;
    }

    private static void drawHangmanFrame() {
    }

    public void makeMove(String letter) {
        index = update(letter);
        log("\nin makeMove: " + tmpAnswer);
        // this will toggle the state of the game
        gameState.setValue(!gameState.getValue());
    }

    public void reset() {
        moves = 0;
        index = -2;
        try {
            setRandomWord();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        prepTmpAnswer();
        prepLetterAndPosArray();
        gameState.setValue(!gameState.getValue());
    }

    private int numOfTries() {
        return 6; // TODO, fix me
    }

    public int numOfMoves() {
        return moves;
    }


    public static void log(String s) {
        System.out.println(s);
    }

    private GameStatus checkForWinner(int status) {
        log("in checkForWinner");
        if (tmpAnswer.replaceAll(" ","").equals(answer)) {
            log("won");
            return GameStatus.WON;
        } else if (moves == numOfTries() && index == -1) {
            moves++;
            log("game over");
            return GameStatus.GAME_OVER;
        } else {
            return null;
        }
    }
}
