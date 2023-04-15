import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

public class GameController {

    private final ExecutorService executorService;
    private final Game game;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int RADIUS = 25;
    private static final int LINE_LENGTH = 150;
    private static final int LINE_WIDTH = 10;

    public GameController(Game game) {
        this.game = game;
        executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    @FXML
    private Group board;
    @FXML
    private Label statusLabel;
    @FXML
    private Label enterALetterLabel;
    @FXML
    private TextField textField;
    @FXML
    private Label correctLetters;

    @FXML
    private Label theCorrectLetters;

    public void initialize() throws IOException {
        System.out.println("in initialize");
        //drawHangman();
        addTextBoxListener();
        setUpStatusLabelBindings();
    }

    private void addTextBoxListener() {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (newValue.length() > 0) {
                    System.out.print(newValue);
                    game.makeMove(newValue);
                    if (game.getGameStatus() == Game.GameStatus.BAD_GUESS) {
                        drawHangman();
                    }
                    if (game.getGameStatus() == Game.GameStatus.GAME_OVER) {
                        drawHangman();
                        textField.setEditable(false);
                    }
                    textField.clear();
                }
            }
        });
    }

    private void setUpStatusLabelBindings() {

        System.out.println("in setUpStatusLabelBindings");
        statusLabel.textProperty().bind(Bindings.format("%s", game.gameStatusProperty()));
        enterALetterLabel.textProperty().bind(Bindings.format("%s", "Enter a letter:"));
        correctLetters.textProperty().bind(Bindings.format("%s", "Correct letters: "));
        theCorrectLetters.textProperty().bind(Bindings.format("%s", game.playerAnswer()));
		/*	Bindings.when(
					game.currentPlayerProperty().isNotNull()
			).then(
				Bindings.format("To play: %s", game.currentPlayerProperty())
			).otherwise(
				""
			)
		);
		*/
    }

//	private void drawHangman() {
//		// Draw the base
////		Line base = new Line(150, 225, 450, 225);
////		base.setStrokeWidth(LINE_WIDTH);
////		board.getChildren().add(base);
//
//		Line pole = new Line(300, 550, 300, 50);
//		pole.setStrokeWidth(LINE_WIDTH);
//		board.getChildren().add(pole);
//
//		Line arm = new Line(300, 50, 450, 50);
//		arm.setStrokeWidth(LINE_WIDTH);
//		board.getChildren().add(arm);
//
//		Line noose = new Line(450, 50, 450, 100);
//		noose.setStrokeWidth(LINE_WIDTH);
//		board.getChildren().add(noose);
//
//		// Draw the head
//		Circle head = new Circle(400, 150, RADIUS);
//		head.setStrokeWidth(LINE_WIDTH);
//		head.setFill(Color.WHITE);
//		head.setStroke(Color.BLACK);
//		board.getChildren().add(head);
//
		// Draw the body
//		Line body = new Line(400, 175, 400, 350);
//		body.setStrokeWidth(LINE_WIDTH);
//		board.getChildren().add(body);
//
//		// Draw the arms
//		Line leftArm = new Line(400, 200, 350, 250);
//		leftArm.setStrokeWidth(LINE_WIDTH);
//		board.getChildren().add(leftArm);
//
//		Line rightArm = new Line(400, 200, 450, 250);
//		rightArm.setStrokeWidth(LINE_WIDTH);
//		board.getChildren().add(rightArm);
//
//		// Draw the legs
//		Line leftLeg = new Line(400, 350, 350, 400);
//		leftLeg.setStrokeWidth(LINE_WIDTH);
//		board.getChildren().add(leftLeg);
//
//		Line rightLeg = new Line(400, 350, 450, 400);
//		rightLeg.setStrokeWidth(LINE_WIDTH);
//		board.getChildren().add(rightLeg);
//	}

    private void drawHangman() {

       // Line line = new Line();

       // Circle c = new Circle();
      //  c.setRadius(10);
        if (game.numOfMoves() == 1) {
            Line pole = new Line(300, 550, 300, 50);
            pole.setStrokeWidth(LINE_WIDTH);
            board.getChildren().add(pole);
            Line arm = new Line(300, 50, 450, 50);
            arm.setStrokeWidth(LINE_WIDTH);
            board.getChildren().add(arm);
            Line noose = new Line(450, 50, 450, 100);
            noose.setStrokeWidth(LINE_WIDTH);
            board.getChildren().add(noose);

        } else if (game.numOfMoves() == 2) {
            Circle head = new Circle(400, 150, RADIUS);
            head.setStrokeWidth(LINE_WIDTH);
            head.setFill(Color.WHITE);
            head.setStroke(Color.BLACK);
            board.getChildren().add(head);
        } else if (game.numOfMoves() == 3) {
            Line body = new Line(400, 175, 400, 350);
            body.setStrokeWidth(LINE_WIDTH);
            board.getChildren().add(body);
        } else if (game.numOfMoves() == 4) {
            Line leftArm = new Line(400, 200, 350, 250);
            leftArm.setStrokeWidth(LINE_WIDTH);
            board.getChildren().add(leftArm);
        } else if (game.numOfMoves() == 5) {
            Line rightArm = new Line(400, 200, 450, 250);
            rightArm.setStrokeWidth(LINE_WIDTH);
            board.getChildren().add(rightArm);
        } else if (game.numOfMoves() == 6) {
            Line leftLeg = new Line(400, 350, 350, 400);
            leftLeg.setStrokeWidth(LINE_WIDTH);
            board.getChildren().add(leftLeg);
        } else if (game.numOfMoves() == 7) {
            Line rightLeg = new Line(400, 350, 450, 400);
            rightLeg.setStrokeWidth(LINE_WIDTH);
            board.getChildren().add(rightLeg);
        }
    }

    @FXML
    private void newHangman() {
        game.reset();
        board.getChildren().clear();
        textField.setEditable(true);
    }

    @FXML
    private void quit() {
        board.getScene().getWindow().hide();
    }

}