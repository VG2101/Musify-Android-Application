package com.example.musify;


public class GetSongs {
    String songCategory, songTitle, artist, albumArt, songDuration, songLink, mKey;

    public GetSongs() {
    }

    public GetSongs(String songCategory, String songTitle, String artist, String albumArt, String songDuration, String songLink) {
        if(songTitle.trim().equals("")){
            songTitle="NO Title";
        }
        this.songCategory = songCategory;
        this.songTitle = songTitle;
        this.artist = artist;
        this.albumArt = albumArt;
        this.songDuration = songDuration;
        this.songLink = songLink;
    }

    public String getSongCategory() {
        return songCategory;
    }

    public void setSongCategory(String songCategory) {
        this.songCategory = songCategory;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

}
