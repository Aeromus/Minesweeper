package MineSweeper;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static MineSweeper.BombType.Flag;
import static MineSweeper.BombType.Norm;
import static MineSweeper.BombType.Unknown;

/**
 * Created by Andrew on 12/5/2016.
 */
public class Tile extends StackPane{


    public static final int TILE_SIZE = 25;

    public int x, y;
    public boolean bomb;
    public boolean revealed = false;
    public static Tile[][] grid;
    private int xTile;
    private int yTile;

    private Rectangle border = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);
    public Text text = new Text();
    public Text flag  = new Text();
    public Text qBomb  =  new Text();

    public BombType bT;


    public Tile(int x, int y, boolean bomb){

        this.x = x;
        this.y = y;
        this.bomb = bomb;
        this.bT = Norm;
        border.setFill(Color.GREY);
        text.setFont(Font.font(15));
        text.setText(bomb ? "\uD83D\uDCA3" : "");
        text.setVisible(false);
        flag.setFont(Font.font(20));
        flag.setText("⚑");
        flag.setVisible(false);
        qBomb.setFont(Font.font(20));
        qBomb.setText("?");
        qBomb.setVisible(false);
        getChildren().addAll(border, text, flag, qBomb);

        setTranslateX(x * TILE_SIZE);
        setTranslateY(y * TILE_SIZE);

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY){
                    if(bT == Norm && revealed != true) {
                        border.setFill(Color.BLUE);
                        bT = Flag;
                        flag.setVisible(true);
                        Source.numOfBombs--;
                        Source.bombCount.setText("Bombs Left: " + Source.numOfBombs);

                        //System.out.print("⚑");
                    }

                    else if(bT == Flag){
                        border.setFill(Color.ORANGE);
                        flag.setVisible(false);
                        qBomb.setVisible(true);
                        bT = Unknown;
                    }

                    else if(bT == Unknown){

                        border.setFill(Color.GREY);
                        qBomb.setVisible(false);
                        bT = Norm;
                        Source.numOfBombs++;
                        Source.bombCount.setText("Bombs Left: " + Integer.toString( Source.numOfBombs));

                    }
                }

                //setOnMouseClicked(e -> open());
                else {
                    if(bT== Norm)
                        open();
                }

            }
        });

    }


    public void open() {

        if (revealed == true) return;

        if (bomb == true) {
            //Game over End timer, score
            border.setFill(Color.RED);
            text.setVisible(true);
            revealed = true;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("GAME OVER");
            alert.setHeaderText("You Hit A Bomb, Game Over");
            alert.setContentText("You Lost in " + Source.seconds + " Seconds");
            //System.out.println("Bomb Clicked, Game Over");
            revealBombs();
            alert.showAndWait();

        }
        else{
            revealed = true;
            text.setVisible(true);
            border.setFill(null);
            if (text.getText().isEmpty()) {
                setNumbers(this, grid, xTile, yTile).forEach(Tile::open);
               // Source.winTiles--;
                //System.out.println(Source.winTiles);
            }
            int bombsLeft = Source.winTiles;
            for(int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid.length; j++)
                    if (grid[i][j].bomb == false && grid[i][j].revealed == true) {
                        bombsLeft--;
                    }
            }
            System.out.println(Source.winTiles);
                if (0 == bombsLeft){
                    Source.timer.stop();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("You Win");
                    alert.setHeaderText("All Tiles Uncovered");
                    alert.setContentText("You won in " + Source.seconds + " Seconds");
                    alert.showAndWait();

                }
        }
    }


    public List<Tile> setNumbers(Tile tile, Tile[][] grid, int xTile, int yTile){
        List<Tile> neighbors = new ArrayList<>();

        int[] points = new int[]{
            -1, -1,-1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1
        };
        this.grid = grid;
        this.xTile = xTile;
        this.yTile = yTile;
        for (int i= 0; i < points.length; i++){


            int dx = points[i];
            int dy = points[++i];
 
            int newX = tile.x +dx;
            int newY =  tile.y + dy;

            if (newX >= 0 && newX < xTile
                    && newY >= 0 && newY < yTile) {
                neighbors.add(grid[newX][newY]);

            }

        }

        return neighbors;
    }

    //Works
    public void revealBombs(){
            Source.timer.stop();
        for(int i = 0; i < grid.length; i++)
            for(int j = 0; j < grid.length; j++)
            {
                if(grid[i][j].bomb == true && grid[i][j].bT == Norm){
                    grid[i][j].border.setFill(Color.RED);
                    grid[i][j].text.setVisible(true);
                    grid[i][j].revealed = true;

                }

                else if(grid[i][j].bomb == false && grid[i][j].bT == Flag){
                    grid[i][j].border.setFill(Color.YELLOW);
                    grid[i][j].text.setVisible(true);
                    grid[i][j].revealed = true;
                    grid[i][j].flag.setVisible(false);
                }

                else if(grid[i][j].bomb == true && grid[i][j].bT == Flag){
                    grid[i][j].border.setFill(Color.LIGHTGREEN);
                    grid[i][j].revealed = true;
                }
            }

    }
}

