package com.example.esp_eventos;

public class Photos {

    private final int photoId;
    private final int imgResId;
    private final String txt;
    public Photos (int photoId, int imgResId, String txt){
        this.photoId = photoId;
        this.imgResId = imgResId;
        this.txt = txt;
    }
    public int getPhotoId(){
        return photoId;
    }
    public int getImgResId(){
        return imgResId;
    }
    public String getTxt(){
        return txt;
    }
}
