package com.example.exc1final;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

public class PicObject {


    public enum Type {CAR, ROCK}

    ImageView imageRes;
    private Type type;


    public PicObject() {
    }


    public PicObject setImageRes(ImageView imageRes) {
        this.imageRes = imageRes;
        return this;
    }


    public void setImage() {
        if (type == Type.CAR)
            imageRes.setImageResource(R.drawable.ic_submarine);
        if (type == Type.ROCK)
            imageRes.setImageResource(R.drawable.ic_bomb);


    }

    public PicObject setType(Type type) {
        this.type = type;
        return this;
    }

    public PicObject setIsOn(boolean isOn) {
        if (isOn)
            this.imageRes.setVisibility(View.VISIBLE);
        else
            this.imageRes.setVisibility(View.INVISIBLE);
        return this;
    }


    public Type getType() {
        return type;
    }

    public boolean getIsOn() {
        if (imageRes.getVisibility() == View.VISIBLE)
            return true;
        else
            return false;
    }


}
