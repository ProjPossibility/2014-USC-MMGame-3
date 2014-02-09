package com.wontonst.blindswordmaster.game;

import com.wontonst.blindswordmaster.game.constants.CombatConstant;
import com.wontonst.blindswordmaster.game.constants.MoveConstant;
import com.wontonst.blindswordmaster.game.constants.OverrideConstant;
import com.wontonst.blindswordmaster.game.model.CombatState;
import com.wontonst.blindswordmaster.game.model.MoveState;
import com.wontonst.blindswordmaster.game.model.OverrideState;
import com.wontonst.blindswordmaster.game.model.PlayerModel;
import com.wontonst.blindswordmaster.sound.SoundManager;

/**
 * Created by RoyZheng on 2/8/14.
 */
public class GameState implements GameComponent {


    public static double STEP_SPEED = 1;
    public static double RUN_SPEED = 2.75;
    public static double BACKFLIP_SPEED = 2;
    public static double MIN_PLAYER_DISTANCE = .5;
    public static double MAX_PLAYER_DISTANCE = 5;
    public static double STRIKE_RANGE = 1;

    private SoundManager m_soundManager;
    //Player one faces to the right, Player 2 faces to the left. Moving to the left is moving in negative direction.
    private PlayerModel player1, player2;

    private Thread gameThread;

    boolean m_inGame = false;

    public boolean inGame() {
        return this.m_inGame;
    }

    public GameState() {

    }

    public void update(double dDelta) {
        player1.update(dDelta);
        player2.update(dDelta);

        if (this.player1.getOverrideState().getState() == OverrideConstant.NONE) {
            this.updateMovement(this.player1, dDelta);
            this.updateCombat(this.player1, dDelta);
        } else {
            if (player1.getOverrideState().counterDone()) {
                player1.stateChange(new OverrideState(OverrideConstant.NONE));
            }
        }
        if (this.player2.getOverrideState().getState() == OverrideConstant.NONE) {
            this.updateMovement(this.player2, dDelta);
            this.updateCombat(this.player2, dDelta);
        } else {
            if (player2.getOverrideState().counterDone()) {
                player2.stateChange(new OverrideState(OverrideConstant.NONE));
            }
        }
    }

    private void updateMovement(PlayerModel player, double dDelta) {
        boolean isPlayer1 = player == player1;
        PlayerModel other = isPlayer1 ? player2 : player1;
        int multiplier = isPlayer1 ? 1 : -1;// movement direction helper

        if (player.getMoveState().counterDone()) {
            player.stateChange(new MoveState(MoveConstant.IDLE));
        } else {
            switch (player.getMoveState().getState()) {
                case IDLE:
                    break;
                case FORWARD_STEP:
                    player.setPosition(player.getPosition() + dDelta * STEP_SPEED * multiplier);
                    break;
                case FORWARD_RUN:
                    player.setPosition(player.getPosition() + dDelta * RUN_SPEED * multiplier);
                    break;
                case BACKWARD_STEP:
                    player.setPosition(player.getPosition() + dDelta * STEP_SPEED * multiplier);
                    break;
                case BACKFLIP:
                    player.setPosition(player.getPosition() + dDelta * BACKFLIP_SPEED * multiplier);
                    break;
            }
            if (Math.abs(other.getPosition() - player.getPosition()) < MIN_PLAYER_DISTANCE) {
                player.setPosition(isPlayer1 ? this.player2.getPosition() - MIN_PLAYER_DISTANCE : this.player1.getPosition() + MIN_PLAYER_DISTANCE);
            }
            if (Math.abs(other.getPosition() - player.getPosition()) > MAX_PLAYER_DISTANCE) {
                player.setPosition(isPlayer1 ? this.player2.getPosition() - MAX_PLAYER_DISTANCE : this.player1.getPosition() + MAX_PLAYER_DISTANCE);
            }
        }
    }

    private void updateCombat(PlayerModel player, double dDelta) {
        boolean isPlayer1 = player == player1;
        PlayerModel other = isPlayer1 ? player2 : player1;
        int multiplier = isPlayer1 ? 1 : -1;// movement direction helper

        if (player.getCombatState().counterDone()) {
            switch (player.getCombatState().getState()) {
                case IDLE:
                    break;
                case SLASH_LEFT:
                    if (other.getCombatState().getState() != CombatConstant.BLOCK_LEFT) {
                        other.receivedDamage(player.getCombatState().getState().DAMAGE);
                    }
                    break;
                case SLASH_RIGHT:
                    if (other.getCombatState().getState() != CombatConstant.BLOCK_RIGHT) {
                        other.receivedDamage(player.getCombatState().getState().DAMAGE);
                    }
                    break;
                case SLASH_UP:
                    if (other.getCombatState().getState() != CombatConstant.BLOCK_UP) {
                        other.receivedDamage(player.getCombatState().getState().DAMAGE);
                    }
                    break;
                case SLASH_DOWN:
                    if (other.getCombatState().getState() != CombatConstant.BLOCK_DOWN) {
                        other.receivedDamage(player.getCombatState().getState().DAMAGE);
                    }
                    break;
            }
            player.stateChange(new OverrideState(OverrideConstant.RECOVERING, player.getCombatState().getState().RECOVERY_TIME));
            player.stateChange(new CombatState(CombatConstant.IDLE));
        }
    }

    public void start() {
        final GameState self = this;
        this.gameThread = new Thread(new Runnable() {

            @Override
            public void run() {
                long last_time = System.nanoTime();
                while (self.inGame()) {
                    long curr_time = System.nanoTime();
                    double fdelta = (curr_time - last_time) * 0.000001;
                    self.update(fdelta);
                    last_time = curr_time;
                }
            }
        });
    }
}
