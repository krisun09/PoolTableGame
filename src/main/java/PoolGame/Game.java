package PoolGame;

import java.util.ArrayList;
import java.util.List;

import PoolGame.Builder.BallBuilderDirector;
import PoolGame.Config.BallConfig;
import PoolGame.Items.Ball;
import PoolGame.Items.PoolTable;
import PoolGame.Memento.TableMemento;
import PoolGame.Memento.TableMementoCaretaker;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/** The game class that runs the game */
public class Game {
    private PoolTable table;

    private PoolTable previousTable;
    boolean shownWonText = false;
    private final Text winText = new Text(50, 50, "Win and Bye");

    private TableMementoCaretaker stateKeep;

    int score = 0;

    /**
     * Initialise the game with the provided config
     * @param config The config parser to load the config from
     */
    public Game(ConfigReader config) {
        this.setup(config);
    }

    private void setup(ConfigReader config) {
        this.table = new PoolTable(config.getConfig().getTableConfig());
        List<BallConfig> ballsConf = config.getConfig().getBallsConfig().getBallConfigs();
        List<Ball> balls = new ArrayList<>();
        BallBuilderDirector builder = new BallBuilderDirector();
        builder.registerDefault();
        for (BallConfig ballConf: ballsConf) {
            Ball ball = builder.construct(ballConf);
            if (ball == null) {
                System.err.println("WARNING: Unknown ball, skipping...");
            } else {
                balls.add(ball);
            }
        }
        this.table.setupBalls(balls);
        this.winText.setVisible(false);
        this.winText.setX(table.getDimX() / 2);
        this.winText.setY(table.getDimY() / 2);

        TableMementoCaretaker stateKeep = new TableMementoCaretaker();
        stateKeep.addMemento(new TableMemento(table));
        this.stateKeep = stateKeep;
        table.setStateKeep(stateKeep);
    }

    /**
     * Get the window dimension in the x-axis
     * @return The x-axis size of the window dimension
     */
    public double getWindowDimX() {
        return this.table.getDimX();
    }

    /**
     * Get the window dimension in the y-axis
     * @return The y-axis size of the window dimension
     */
    public double getWindowDimY() {
        return this.table.getDimY();
    }

    /**
     * Get the pool table associated with the game
     * @return The pool table instance of the game
     */
    public PoolTable getPoolTable() {
        return this.table;
    }

    public void setPoolTable(PoolTable poolTable) {
        this.table = poolTable;
    }

    public void addScore(int adding) {
        score += adding;
    }

    public int getScore() {
        return this.score;
    }

    /** Add all drawable object to the JavaFX group
     * @param root The JavaFX `Group` instance
    */
    public void addDrawables(Group root) {
        ObservableList<Node> groupChildren = root.getChildren();
        table.addToGroup(groupChildren);
        groupChildren.add(this.winText);
    }

    /** Reset the game */
    public void reset() {
        this.winText.setVisible(false);
        this.shownWonText = false;
        this.table.reset();
        this.score = 0;
    }

    /** Code to execute every tick. */
    public void tick() {
        if (table.hasWon() && !this.shownWonText) {
            System.out.println(this.winText.getText());
            this.winText.setVisible(true);
            this.shownWonText = true;
        }
        table.checkPocket(this);
        table.handleCollision();
        this.table.applyFrictionToBalls();
        for (Ball ball : this.table.getBalls()) {
            ball.move();
        }

        // something that shows hit, if the previous was 0 and not anymore, that means hit on cue
        // then create table memento
        // if user tries to undo, just set the table to the memento
        checkCueBallHit();
    }

    public void checkCueBallHit() {
        for (Ball ball : table.getBalls()) {
            if (ball.getBallType().equals(Ball.BallType.CUEBALL)) {
                if (!table.getCueBallMoved()) {
                    if (ball.getXVel() != 0 || ball.getYVel() != 0) {
                        table.setCueBallMoved(true);
                        System.out.println("cue ball got hit, the current table obj is " + table.toString());
                        System.out.println("Cue ball pos " + table.getBalls().get(0).getXPos());

                        stateKeep.updateMemento();

                        System.out.println("current table to be saved as memento is" + table.toString());

                        PoolTable cloneTable = new PoolTable("red", 1, 300, 300);
                        List<Ball> clonedBalls = new ArrayList<>();
                        for (Ball cloneBall : table.getBalls()) {
                            clonedBalls.add(cloneBall.clone());
                        }

                        cloneTable.setBalls(clonedBalls);

//                        table.setBalls(clonedBalls);

                        this.previousTable = cloneTable;
                        System.out.println("Cue ball pos previous table " + previousTable.getBalls().get(0).getXPos());

                        System.out.println("current cloned table is" + cloneTable);
                    }
                } else {
                    if (ball.hasStopped()) {
                        stateKeep.addMemento(table.createSnapshot());
                        table.setCueBallMoved(false);
                    }
                }
            }
        }
    }

    public void recoverState(TableMemento tableMemento) {
        PoolTable poolTable = tableMemento.getLastState();
        System.out.println("recovering table: " + poolTable.toString());

        System.out.println("the current table is " + this.table.toString());

//        for (Ball ball : poolTable.getBalls()) {
//            ball.setXPos(ball.getXPos());
//            ball.setYPos(ball.getYPos());
//        }


        System.out.println("Before undo: Cue ball pos previous table " + table.getBalls().get(0).getXPos());

        this.table.setBalls(previousTable.getBalls());


//        this.setPoolTable(previousTable);

        System.out.println("After undo: Cue ball pos previous table " + table.getBalls().get(0).getXPos());

        System.out.println("undoed, the current table is " + this.table.toString());
        System.out.println("Previoud table: " + previousTable.getBalls().get(0).getXPos());
    }

    public void cheatListener(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                switch (ke.getCode()){
                    case BACK_SPACE:
                        System.out.println("Key Pressed: " + ke.getCode());
                        recoverState(stateKeep.getSavedState());
                        ke.consume(); // <-- stops passing the event to next node
                        break;
                    case R:
                        getPoolTable().cheatRemove(Paint.valueOf("red"), Game.this);
                        break;
                    case Y:
                        getPoolTable().cheatRemove(Paint.valueOf("yellow"), Game.this);
                        break;
                    case G:
                        getPoolTable().cheatRemove(Paint.valueOf("darkgreen"), Game.this);
                        break;
                    case N:
                        getPoolTable().cheatRemove(Paint.valueOf("SADDLEBROWN"), Game.this);
                        break;
                    case B:
                        getPoolTable().cheatRemove(Paint.valueOf("blue"), Game.this);
                        break;
                    case P:
                        getPoolTable().cheatRemove(Paint.valueOf("purple"), Game.this);
                        break;
                    case K:
                        getPoolTable().cheatRemove(Paint.valueOf("black"), Game.this);
                        break;
                    case O:
                        getPoolTable().cheatRemove(Paint.valueOf("orange"), Game.this);
                        break;
                }
            }
        });
    }

}
