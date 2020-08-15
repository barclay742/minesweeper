import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;


public class Main extends Application {
    Scene window;
    int WindowSize = 100;
    static int difficulty;
    static int GameWindowSize;
    static int squareSize;
    static int noOfSquares;
    public static Square[][] cells;
    public static SquareFx[][] squaresFx;
    int numOfBombs;
    int unMinedFreeSquares;
    static Boolean gameOver = false;
    Textex numOfBombsText = new Textex(10, 10, "Remaining Bombs:");

    class SquareFx extends Rectangle {
        Shape t;
        int bomb, x, y, nearbyBombs;
        Boolean highlighted;
        ArrayList<SquareFx> neighboursList = new ArrayList<>();

        SquareFx(int x, int y, int bomb, Integer neighbours) {
            super(x, y + 30, squareSize, squareSize);
            this.setFill(Color.GRAY);
            this.bomb = bomb;
            this.highlighted = false;
            this.x = x / squareSize;
            this.y = y / squareSize;
            this.nearbyBombs = neighbours;
            if (bomb == 0) {
                if (neighbours == 0) {
                    Text t = new Text(x + 3, y + 30 + squareSize - 3, "");
                    t.setOpacity(0);
                    this.setStroke(Color.BLACK);
                    this.t = t;
                } else {
                    Text t = new Text(x + 3, y + 30 + squareSize - 3, neighbours.toString());
                    t.setOpacity(0);
                    this.setStroke(Color.BLACK);
                    this.t = t;
                }
            } else {
                Circle t = new Circle((x + squareSize / 2) + 0.5, (y + squareSize / 2) + 0.5 + 30, squareSize / 3);
                t.setFill(Color.BLACK);
                this.setStroke(Color.BLACK);
                t.setOpacity(0);
                this.t = t;
            }
            this.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    reveal();
                    if (this.bomb == 1) {
                        Main.gameOver = true;
                        for (int j = 0; j < noOfSquares; j++) {
                            for (int i = 0; i < noOfSquares; i++) {
                                squaresFx[j][i].reveal();
                            }
                        }
                    }
                }
                if (event.getButton() == MouseButton.SECONDARY) {
                    if (this.bomb == 1) {
                        if (!this.highlighted) {
                            numOfBombs--;
                        }
                        numOfBombsText.update();
                        this.highlighted = true;
                        this.setFill(Color.RED);
                    } else {
                        this.setFill(Color.BLUE);
                    }
                }
            });
        }

        void reveal() {
            if (nearbyBombs == 0) {
                revealNeighbours();
            } else if (this.bomb == 0) {
                if (cells[y][x].reveal()) {
                    unMinedFreeSquares--;
                    System.out.println(unMinedFreeSquares);
                }

            }
            this.setFill(null);
            t.setOpacity(1);
        }

        void Setneighbours() {
            if (this.y - 1 >= 0 && this.y - 1 < noOfSquares) {
                neighboursList.add(squaresFx[y - 1][x]);
            }
            if (this.y + 1 >= 0 && this.y + 1 < noOfSquares) {
                neighboursList.add(squaresFx[y + 1][x]);
            }
            if (this.x - 1 >= 0 && this.x - 1 < noOfSquares) {
                neighboursList.add(squaresFx[y][x - 1]);
            }
            if (this.x + 1 >= 0 && this.x + 1 < noOfSquares) {
                neighboursList.add(squaresFx[y][x + 1]);
            }
            if (this.y - 1 >= 0 && this.y - 1 < noOfSquares && this.x + 1 >= 0 && this.x + 1 < noOfSquares) {
                neighboursList.add(squaresFx[y - 1][x + 1]);
            }
            if (this.y + 1 >= 0 && this.y + 1 < noOfSquares && this.x - 1 >= 0 && this.x - 1 < noOfSquares) {
                neighboursList.add(squaresFx[y + 1][x - 1]);
            }
            if (this.y - 1 >= 0 && this.y - 1 < noOfSquares && this.x - 1 >= 0 && this.x - 1 < noOfSquares) {
                neighboursList.add(squaresFx[y - 1][x - 1]);
            }
            if (this.y + 1 >= 0 && this.y + 1 < noOfSquares && this.x + 1 >= 0 && this.x + 1 < noOfSquares) {
                neighboursList.add(squaresFx[y + 1][x + 1]);
            }
        }

        void revealNeighbours() {
            for (int i = 0; i < neighboursList.size(); i++) {
                //if not a bomb
                if (neighboursList.get(i).bomb == 0) {
                    //if it is removed
                    if (squaresFx[neighboursList.get(i).y][neighboursList.get(i).x].neighboursList.remove(this)) {
                        if (this.nearbyBombs == 0) {
                            squaresFx[neighboursList.get(i).y][neighboursList.get(i).x].revealNeighbours();
                        } else {
                            squaresFx[neighboursList.get(i).y][neighboursList.get(i).x].neighboursList.add(this);
                        }
                        if (cells[y][x].reveal()) {
                            System.out.println(unMinedFreeSquares);
                            unMinedFreeSquares--;
                        }
                        this.setFill(null);
                        this.setStroke(Color.GRAY);
                        this.setStrokeWidth(0.7);
                        t.setOpacity(1);
                    }
                }
            }
        }
    }


    void setUp() {
        Main.gameOver = false;
        GameWindowSize = difficulty * 100 * 2;
        squareSize = GameWindowSize / (difficulty * 10);
        noOfSquares = GameWindowSize / squareSize;
        cells = new Square[noOfSquares][noOfSquares];
        squaresFx = new SquareFx[noOfSquares][noOfSquares];
        numOfBombs = 0;
        unMinedFreeSquares = 0;
        //Generate Starting Position
        Random random = new Random();
        for (int j = 0; j < noOfSquares; j++) {
            for (int i = 0; i < noOfSquares; i++) {
                if (numOfBombs < 900 && random.nextInt(100) < 10) {
                    cells[j][i] = new Square(i, j, 1, 1);
                    numOfBombs++;
                } else {
                    cells[j][i] = new Square(i, j, 1, 0);
                }
            }
        }
        for (int j = 0; j < noOfSquares; j++) {
            for (int i = 0; i < noOfSquares; i++) {
                cells[j][i].setNearby();
            }
        }

        //Generate Board based on starting position
        for (int j = 0; j < noOfSquares; j++) {
            for (int i = 0; i < noOfSquares; i++) {
                int Bomb = cells[j][i].Bomb;
                int neighbours = cells[j][i].nearbyBombs;
                squaresFx[j][i] = new SquareFx(i * squareSize, j * squareSize, Bomb, neighbours);

            }
        }
        for (int j = 0; j < noOfSquares; j++) {
            for (int i = 0; i < noOfSquares; i++) {
                squaresFx[j][i].Setneighbours();
            }
        }
        numOfBombsText.update();
        unMinedFreeSquares = noOfSquares * noOfSquares - numOfBombs;
    }

    class Textex extends Text {
        int x, y;
        String text;

        Textex(int x, int y, String text) {
            super(x, y, text);
            this.x = x;
            this.y = y;
            this.text = text;
        }

        void update() {
            this.text = "Remaining Bombs: " + Integer.toString(numOfBombs);
            this.setText(text);
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {


        Group g = new Group();
        Group layout = new Group();
        Scene s = new Scene(g, WindowSize, WindowSize);
        Scene Menu = new Scene(layout, WindowSize, WindowSize);
        primaryStage.setTitle("MineSweeper");
        primaryStage.setScene(Menu);
        Button returnTotitle = new Button("return");

        returnTotitle.setOnAction(e -> {
            primaryStage.setScene(Menu);
            primaryStage.setWidth(WindowSize + 40);
            primaryStage.setHeight(WindowSize + 60);
        });
        returnTotitle.setOpacity(0);


        Button EasyDifficulty = new Button("Easy");
        Button MediumDifficulty = new Button("Medium");
        Button HardDifficulty = new Button("Hard");
        MediumDifficulty.setLayoutX(35);
        MediumDifficulty.setLayoutY(35);
        EasyDifficulty.setLayoutX(35);
        HardDifficulty.setLayoutX(35);
        HardDifficulty.setLayoutY(70);
        EasyDifficulty.setOnAction(e -> {
            Main.difficulty = 2;
            setUp();
            g.getChildren().clear();
            returnTotitle.setLayoutX(GameWindowSize - 100);
            returnTotitle.setOpacity(0);
            for (int i = 0; i < noOfSquares; i++) {
                for (SquareFx sqa : squaresFx[i]) {
                    g.getChildren().add(sqa.t);
                    g.getChildren().add(sqa);
                }
            }

            g.getChildren().addAll(numOfBombsText, returnTotitle);
            primaryStage.setHeight(GameWindowSize + squareSize * 2 + 30);
            primaryStage.setWidth(GameWindowSize + squareSize - 3);
            primaryStage.setScene(s);
            Thread thread = new Thread(new Runnable() {

                public void run() {
                    try {
                        while (true) {
                            if (gameOver) {
                                System.out.println("GameOver");
                                returnTotitle.setOpacity(1);
                                return;
                            }
                            if (unMinedFreeSquares == 0) {
                                System.out.println("Good Job");
                                returnTotitle.setOpacity(1);
                                return;
                            }
                            try {
                                Thread.sleep(99);
                            } catch (Exception e) {
                            }
                        }
                    } catch (Exception e) {
                    }
                    ;
                }
            });
            thread.start();
        });
        MediumDifficulty.setOnAction(e -> {
            difficulty = 3;
            setUp();
            g.getChildren().clear();
            returnTotitle.setLayoutX(GameWindowSize - 100);
            returnTotitle.setOpacity(0);
            for (int i = 0; i < noOfSquares; i++) {
                for (SquareFx sqa : squaresFx[i]) {
                    g.getChildren().add(sqa.t);
                    g.getChildren().add(sqa);
                }
            }
            g.getChildren().addAll(numOfBombsText, returnTotitle);
            primaryStage.setHeight(GameWindowSize + squareSize * 2 + 30);
            primaryStage.setWidth(GameWindowSize + squareSize - 3);
            primaryStage.setScene(s);
            Thread thread = new Thread(new Runnable() {

                public void run() {
                    try {
                        while (true) {
                            if (gameOver) {
                                System.out.println("GameOver");
                                returnTotitle.setOpacity(1);
                                return;
                            }
                            if (unMinedFreeSquares == 0) {
                                System.out.println("Good Job");
                                returnTotitle.setOpacity(1);
                                return;
                            }
                            try {
                                Thread.sleep(99);
                            } catch (Exception e) {
                            }
                        }
                    } catch (Exception e) {
                    }
                    ;
                }
            });
            thread.start();
        });
        HardDifficulty.setOnAction(e -> {
            difficulty = 4;
            setUp();
            g.getChildren().clear();
            returnTotitle.setLayoutX(GameWindowSize - 100);
            returnTotitle.setOpacity(0);
            for (int i = 0; i < noOfSquares; i++) {
                for (SquareFx sqa : squaresFx[i]) {
                    g.getChildren().add(sqa.t);
                    g.getChildren().add(sqa);
                }
            }
            g.getChildren().addAll(numOfBombsText, returnTotitle);
            primaryStage.setHeight(GameWindowSize + squareSize * 2 + 30);
            primaryStage.setWidth(GameWindowSize + squareSize - 3);
            primaryStage.setScene(s);
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            if (gameOver) {
                                System.out.println("GameOver");
                                returnTotitle.setOpacity(1);
                                return;
                            }
                            if (unMinedFreeSquares == 0) {
                                System.out.println("Good Job");
                                returnTotitle.setOpacity(1);
                                return;
                            }
                            try {
                                Thread.sleep(99);
                            } catch (Exception e) {
                            }
                        }
                    } catch (Exception e) {
                    }
                    ;
                }
            });
            thread.start();
        });


        layout.getChildren().addAll(EasyDifficulty, MediumDifficulty, HardDifficulty);
        primaryStage.show();

    }


}


