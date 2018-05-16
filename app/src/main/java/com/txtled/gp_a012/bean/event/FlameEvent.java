package com.txtled.gp_a012.bean.event;

/**
 * Created by Mr.Quan on 2018/4/28.
 */

public class FlameEvent {

    private int lightStatue;
    private int light;
    private int power;
    private int speed;
    private String type;
    private int toMusic;

    public FlameEvent(int lightStatue, int light, int power, int speed) {
        this.lightStatue = lightStatue;
        this.light = light;
        this.power = power;
        this.speed = speed;
    }

    public FlameEvent(String type, int value){
        this.type = type;
        switch (type){
            case "1":
                this.lightStatue = value;
                break;
            case "2":
                this.light = value;
                break;
            case "3":
                this.power = value;
                break;
            case "4":
                this.speed = value;
                break;
            case "5":
                this.toMusic = value;
                break;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLightStatue() {
        return lightStatue;
    }

    public void setLightStatue(int lightStatue) {
        this.lightStatue = lightStatue;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getToMusic() {
        return toMusic;
    }

    public void setToMusic(int toMusic) {
        this.toMusic = toMusic;
    }
}
