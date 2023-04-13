import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GameController {

	private final ExecutorService executorService;
	private final Game game;	
	
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
	private VBox board ;
	@FXML
	private Label statusLabel ;
	@FXML
	private Label enterALetterLabel ;
	@FXML
	private TextField textField ;
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
				if(newValue.length() > 0) {
					System.out.print(newValue);
					game.makeMove(newValue);
					if(game.getGameStatus() == Game.GameStatus.BAD_GUESS){
						drawHangman();
					}
					if(game.getGameStatus() == Game.GameStatus.GAME_OVER){
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
		correctLetters.textProperty().bind(Bindings.format("%s","Correct letters: "));
		theCorrectLetters.textProperty().bind(Bindings.format("%s",game.playerAnswer()));
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

	private void drawHangman() {

		Line line = new Line();

		Circle c = new Circle();
		c.setRadius(10);
		if(game.numOfMoves() == 1){
			line.setStartX(0.0f);
			line.setStartY(0.0f);
			line.setEndX(0.0f);
			line.setEndY(50.0f);
			board.getChildren().add(line);

		}
		else if(game.numOfMoves() == 2){
			board.getChildren().add(c);
		}
		else if(game.numOfMoves() ==3) {
			line.setStartX(25.0f);
			line.setStartY(0.0f);
			line.setEndX(25.0f);
			line.setEndY(75.0f);
			board.getChildren().add(line);
		}
		else if(game.numOfMoves() ==4) {
			line.setStartX(0.0f);
			line.setStartY(0.0f);
			line.setEndX(-15.0f);
			line.setEndY(-25.0f);
			board.getChildren().add(line);
		}
		else if(game.numOfMoves() ==5) {
			line.setStartX(0.0f);
			line.setStartY(0.0f);
			line.setEndX(-15.0f);
			line.setEndY(25.0f);
			board.getChildren().add(line);
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