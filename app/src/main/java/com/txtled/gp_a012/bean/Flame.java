package com.txtled.gp_a012.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by Mr.Quan on 2018/4/28.
 */
@Entity
public class Flame implements Serializable{
    static final long serialVersionUID = 42L;

    @Id
    private Long id;
    private int lightStatue;
    private int light;
    private int power;
    private int speed;
    private int toMusic;

    public Flame() {
    }

    @Generated(hash = 1756854768)
    public Flame(Long id, int lightStatue, int light, int power, int speed, int toMusic) {
        this.id = id;
        this.lightStatue = lightStatue;
        this.light = light;
        this.power = power;
        this.speed = speed;
        this.toMusic = toMusic;
    }

    public Flame(boolean init) {
        this.lightStatue = 0;
        this.light = 0;
        this.power = 0;
        this.speed = 0;
        this.toMusic = 0;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getToMusic() {
        return toMusic;
    }

    public void setToMusic(int toMusic) {
        this.toMusic = toMusic;
    }
}
