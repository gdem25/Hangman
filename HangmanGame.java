import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class HangmanGame extends Application {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int RADIUS = 25;
    private static final int LINE_LENGTH = 150;
    private static final int LINE_WIDTH = 10;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.WHITE);

        // Draw the base
        Line base = new Line(150, 550, 450, 550);
        base.setStrokeWidth(LINE_WIDTH);
        root.getChildren().add(base);

        Line pole = new Line(300, 550, 300, 50);
        pole.setStrokeWidth(LINE_WIDTH);
        root.getChildren().add(pole);

        Line arm = new Line(300, 50, 450, 50);
        arm.setStrokeWidth(LINE_WIDTH);
        root.getChildren().add(arm);

        Line noose = new Line(450, 50, 450, 100);
        noose.setStrokeWidth(LINE_WIDTH);
        root.getChildren().add(noose);

        // Draw the head
        Circle head = new Circle(400, 150, RADIUS);
        head.setStrokeWidth(LINE_WIDTH);
        head.setFill(Color.WHITE);
        head.setStroke(Color.BLACK);
        root.getChildren().add(head);

        // Draw the body
        Line body = new Line(400, 175, 400, 350);
        body.setStrokeWidth(LINE_WIDTH);
        root.getChildren().add(body);

        // Draw the arms
        Line leftArm = new Line(400, 200, 350, 250);
        leftArm.setStrokeWidth(LINE_WIDTH);
        root.getChildren().add(leftArm);

        Line rightArm = new Line(400, 200, 450, 250);
        rightArm.setStrokeWidth(LINE_WIDTH);
        root.getChildren().add(rightArm);

        // Draw the legs
        Line leftLeg = new Line(400, 350, 350, 400);
        leftLeg.setStrokeWidth(LINE_WIDTH);
        root.getChildren().add(leftLeg);

        Line rightLeg = new Line(400, 350, 450, 400);
        rightLeg.setStrokeWidth(LINE_WIDTH);
        root.getChildren().add(rightLeg);

        primaryStage.setTitle("Hangman Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}