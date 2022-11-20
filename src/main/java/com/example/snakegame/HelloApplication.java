package com.example.snakegame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelloApplication extends Application {
    //Variable
    static int speed = 5;
    static int foodcolor = 0;
    static int width = 30;
    static int height = 25;
    static int foodY = 0;
    static int foodX = 0;
    static int cornersize = 25;
    static List <Corner> snake = new ArrayList<>(); // body of the snake
    static Dir direction = Dir.RIGHT;
    static boolean gameOver = false;
    static Random random = new Random();
    static int score=-1;
    //corner class
    public static class Corner {
        int x;
        int y;
        public Corner(int x,int y){
            this.x=x;
            this.y=y;
        }

    }

    @Override
    public void start(Stage stage) throws IOException {
        newfood();
        VBox root = new VBox();

//set the background of the layout VBox
//        Image ooo=new Image("https://as1.ftcdn.net/v2/jpg/02/00/80/30/1000_F_200803023_HdTJOyXpiKXIsALglHgXX7CQUGVIAfYI.jpg");
//        BackgroundImage bakcimage=new BackgroundImage(ooo, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
//                new BackgroundSize(100,100,true,true,true,true));
//        root.setBackground(new Background(bakcimage));


        Canvas canvas = new Canvas(width*cornersize,height*cornersize);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        //Change the program icon
        Image img =new Image(getClass().getClassLoader().getResource("RR.png").toString());
//        ImageView imv=new ImageView(img);


//        root.getChildren().add(imv);

        stage.getIcons().add(img);
        //
        new AnimationTimer(){
            long lastTick =0;
            public void handle(long now){
                if (lastTick==0) {
                    lastTick = now;
                    tick(gc);
                    return;
                }
                if (now - lastTick>1000000000/speed){
                    lastTick=now;
                    tick(gc);
                }
            }
        }.start();




        Scene scene = new Scene(root, width*cornersize, height*cornersize);

        scene.addEventFilter(KeyEvent.KEY_PRESSED,key->{
            if (key.getCode()== KeyCode.W){
                direction=Dir.UP;
            }
            if (key.getCode()== KeyCode.S){
                direction=Dir.DOWN;
            }
            if (key.getCode()== KeyCode.A){
                direction=Dir.LEFT;
            }
            if (key.getCode()== KeyCode.D){
                direction=Dir.RIGHT;
            }
            //restart game
            if (key.getCode()== KeyCode.R){
                restart();
            }
        });


        //Snake Body Start
        snake.add(new Corner(width/2,height/2));
        snake.add(new Corner(width/2,height/2));
        snake.add(new Corner(width/2,height/2));

        stage.setTitle("Feed-Snake");
        stage.setScene(scene);
        stage.show();
    }
//Restart Method
    public void restart(){
        gameOver=false;
        speed=5;
        score=0;
        snake.clear();
        snake.add(new Corner(width/2,height/2));
        snake.add(new Corner(width/2,height/2));
        snake.add(new Corner(width/2,height/2));
    }
    //add music

 //Food logic
    public static void newfood() {
        start: while(true) {
            foodX = random.nextInt(width);
            foodY = random.nextInt(height);
            for (Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            foodcolor= random.nextInt(6); //How many color i want to put for the food
            speed++;
            score++;
            break ;
        }
    }


    //Ticks
    public static void tick(GraphicsContext gc){

        if (gameOver){
            //if the game over apper that with red in osition.
            gc.setFill(Color.RED);
            gc.setFont(new Font("",45));
            gc.fillText("GAME OVER",100,250);
            gc.setFont(new Font("",30));
            gc.fillText("Press R to Restart The Game",100,300);
            return;
        }
        // to make ??? add square???
        for(int i= snake.size()-1;i>=1;i--){
            snake.get(i).x=snake.get(i-1).x;
            snake.get(i).y=snake.get(i-1).y;
        }
        switch (direction){
            case UP:
                snake.get(0).y--;
                if (snake.get(0).y<0){
                    snake.get(0).y=height-1; //down
//                    gameOver=true;
                }
                break;
            case DOWN:
                snake.get(0).y++;
                if (snake.get(0).y>height){
//                    gameOver=true;
                    snake.get(0).y=0;//up
                }
                break;
            case LEFT:
                snake.get(0).x--;
                if (snake.get(0).x<0){
                    snake.get(0).x=width-1;
//                    gameOver=true;
                }
                break;
            case RIGHT:
                snake.get(0).x++;
                if (snake.get(0).x>width){
                    snake.get(0).x=0;
//                    gameOver=true;
                }
                break;
        }

        //Eating Food Growing
        if (foodX==snake.get(0).x &&foodY==snake.get(0).y){
            snake.add(new Corner(-1,-1)); //???
            newfood(); // To generate new food
        }

        //Self destroy if crash happen
        //the hit the body
        for (int i =1;i<snake.size();i++){
            if (snake.get(0).x==snake.get(i).x&&snake.get(0).y==snake.get(i).y){
                gameOver=true;
            }
        }

        //fill background in black // Image background
        Image ooo=new Image("M.jpg");
//        gc.setFill(Color.LIGHTPINK);
//        gc.fillRect(0,0,width*cornersize,height*cornersize);
        gc.drawImage(ooo,0,0);



//    //Score of the game
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("",33));
        gc.fillText("YOUR SCOR IS: "+(score),10,30);

  // Food color
        Color color=Color.BISQUE;
        switch (foodcolor){
            case 0:color=Color.PURPLE;


            break;
            case 1:color=Color.BLUE;

                break;
            case 2:color=Color.ORANGE;

                break;
            case 3:color=Color.BROWN;

                break;
            case 4:color=Color.GREEN;

                break;
            case 5:color=Color.FIREBRICK;

                break;
        }

        gc.setFill(color);
//        gc.fillOval(foodX*cornersize,foodY*cornersize,cornersize,cornersize); // regular shape of food
        gc.fillArc(foodX*cornersize,foodY*cornersize,cornersize,cornersize,250,250, ArcType.ROUND); // new shape of food



  //sanke fill snake color with shadow
        for (Corner c:snake) {
            gc.setFill(Color.LIGHTGREEN);
//            gc.fillRect(c.x*cornersize,c.y*cornersize,cornersize-1,cornersize-1);
            gc.fillRoundRect(c.x*cornersize,c.y*cornersize,cornersize-1,cornersize-1,90,90);
            gc.setFill(Color.GREEN);
//            gc.fillRect(c.x*cornersize,c.y*cornersize,cornersize-2,cornersize-2);
            gc.fillRoundRect(c.x*cornersize,c.y*cornersize,cornersize-2,cornersize-2,180,130);
        }
    }


    public static void main(String[] args) {
        launch();
    }
}