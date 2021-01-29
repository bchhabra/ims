package com.chhabras.parser.request;

public class WordV1 implements Comparable<WordV1> {
    String text;
    int x;

    public WordV1(String text, int x, int y) {
        this.text = text;
        this.x = x;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    @Override
    public int compareTo(WordV1 w) {
        return this.x - w.getX();
    }

    @Override
    public String toString() {
        return text;
    }
}
