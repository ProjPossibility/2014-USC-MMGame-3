package com.wontonst.blindswordmaster.model;

/**
 * Created by Roy Zheng on 2/8/14.
 */
public enum CombatState {
    IDLE(0, 0, 0, "CIDL"),
    LEFT_SLASH(1.25, 1, .5, "SL"), RIGHT_SLASH(1.25, 1, .5, "SR"), UPPERCUT(.9, .75, .5, "SU"), POKE(.9, .75, .5, "SD"),
    BLOCK_LEFT(.1, 0, .1, "BL"), BLOCK_RIGHT(.1, 0, .1, "BR"), BLOCK_UP(.1, 0, .1, "BU"), BLOCK_POKE(.1, 0, .1, "BD"),
    COUNTER_LEFT(0, 1.25, .7, "CL"), COUNTER_RIGHT(0, 1.25, .7, "CR"), COUNTER_UP(0, 1, .5, "CU"), COUNTER_POKE(0, 1, .5, "CD");

    CombatState(double atk_speed, double dmg, double recovery_time, String msg) {
        this.ACTION_SPEED = atk_speed;
        this.DAMAGE = dmg;
        this.RECOVERY_TIME = recovery_time;
        this.S_MSG = msg;
    }

    double ACTION_SPEED;
    double DAMAGE;
    double RECOVERY_TIME;
    String S_MSG;
}