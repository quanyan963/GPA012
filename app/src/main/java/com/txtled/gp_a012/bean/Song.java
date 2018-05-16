package com.txtled.gp_a012.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by KomoriWu on 2017/9/21.
 */
@Entity
public class Song implements Serializable {
    static final long serialVersionUID = 42L;
    @Id
    private Long id;
    private int songId;
    private String name;
    private String singer;
    private String url;
    private String uri;
    private int duration;

    public Song() {
    }

    public Song(int songId, String name, String singer, String url, String uri,
                int duration) {
        this.songId = songId;
        this.name = name;
        this.singer = singer;
        this.url = url;
        this.uri = uri;
        this.duration = duration;
    }

    @Generated(hash = 1824890435)
    public Song(Long id, int songId, String name, String singer, String url,
                String uri, int duration) {
        this.id = id;
        this.songId = songId;
        this.name = name;
        this.singer = singer;
        this.url = url;
        this.uri = uri;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
    public int getFormatDuration() {
        return duration/1000;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
