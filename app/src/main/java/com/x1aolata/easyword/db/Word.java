package com.x1aolata.easyword.db;

/**
 * @author x1aolata
 * @date 2020/5/29 8:23
 * @script 单词类
 */
public class Word {

    private int word_id;
    private String word;
    private String description;

    public Word(int word_id, String word, String description) {
        this.word_id = word_id;
        this.word = word;
        this.description = description;
    }

    public int getWord_id() {
        return word_id;
    }

    public String getWord() {
        return word;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word_id=" + word_id +
                ", word='" + word + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
