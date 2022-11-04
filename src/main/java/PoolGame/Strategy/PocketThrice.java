package PoolGame.Strategy;

import PoolGame.Game;
import PoolGame.Items.Ball;

public class PocketThrice implements BallPocketStrategy {

    private final int FALL_COUNTER_THRESHOLD = 3;

    @Override
    public void fallIntoPocket(Game game, Ball ball) {
        ball.incrementFallCounter();
        if (ball.getFallCounter() >= FALL_COUNTER_THRESHOLD) {
            ball.disable();
        } else {
            ball.resetPosition();
            for (Ball ballB: game.getPoolTable().getBalls()) {
                if (ball.isColliding(ballB)) {
                    ball.disable();
                }
            }
        }
    }
}
