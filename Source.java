package MineSweeper;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by Andrew on 11/15/2016.
 */
public class Source extends Application {

    private Scene scene1;
    private Pane board;
    private HBox topBox;
    private BorderPane mainView;
    private Button startButton;
    private final int TILE_SIZE = 25;
    private  final int X_TILES = 500 / TILE_SIZE;
    private  final int Y_TILES = 500 / TILE_SIZE;
    public Tile[][] grid = new Tile[X_TILES][Y_TILES];
    public static int numOfBombs = 0;
    public static Text bombCount;
    public static TimerPane timer;
    public static Text timerCount;
    public static int winTiles = 0;

    public static int seconds = 0;

    private Parent createBoard() {
        Pane root = new Pane();
        root.setPrefSize(500, 500);
        timer = new TimerPane();
        timer.startTimer();
        seconds = 0;
        numOfBombs = 0;
        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Tile tile = new Tile(x, y, Math.random() < 0.1);
                if (tile.bomb == true)
                    numOfBombs++;
                grid[x][y] = tile;
                root.getChildren().add(tile);
            }
        }
        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Tile tile = grid[x][y];

                if (tile.bomb)
                    continue;

                long bombs = tile.setNumbers(tile, grid, X_TILES, Y_TILES).stream().filter(t -> t.bomb).count();

                if (bombs > 0)
                    tile.text.setText(String.valueOf(bombs));
            }
        }

        Tile.grid = grid;
        bombCount.setText("Bombs Left: " + numOfBombs);
        winTiles = (X_TILES* Y_TILES) - numOfBombs;
        //System.out.println(winTiles);
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        topBox = new HBox();

        //Creating Bomb Count
        bombCount = new Text("Bombs Left:" + numOfBombs);
        topBox.getChildren().add(bombCount);
        bombCount.setTextAlignment(TextAlignment.JUSTIFY);
        bombCount.setFont(Font.font(30));

        //Creating StartButton
        startButton = new Button("Start Game");
        topBox.getChildren().add(startButton);
        startButton.setAlignment(Pos.CENTER);

        //Creating Timer
        timerCount = new Text();
        timerCount.setFont(Font.font(30));
        timerCount.setText(""  + seconds);
        topBox.getChildren().add(timerCount);
        timerCount.setTextAlignment(TextAlignment.JUSTIFY);



        //Creating board
        board =new Pane(createBoard());
        topBox.setSpacing(50);
        mainView = new BorderPane();
        mainView.setTop(topBox);
        mainView.setCenter(board);
        //topBox.setAlignment(Pos.CENTER);


        scene1= new Scene(mainView);
        //topBox.setPadding( new Insets(10, 15 , 10, 15));
        primaryStage.setScene(scene1);
        primaryStage.setResizable(false);

        //Generates a new Board every time its pressed
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Start Button Pressed");
                timer.stop();
                board.getChildren().removeAll();
                board = new Pane(createBoard());
                bombCount.setText("Bombs Left: " + numOfBombs);
                mainView.setCenter(board);
                primaryStage.show();
            }
        });

        //Stops Timer when Program exited
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                timer.stop();
            }
        });


        primaryStage.show();

    }

    public static void main(String[] args) {launch(args);}}


