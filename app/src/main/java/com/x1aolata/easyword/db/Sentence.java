package com.x1aolata.easyword.db;

/**
 * @author x1aolata
 * @date 2020/5/29 8:23
 * @script 例句类
 */
public class Sentence {


    private int sentence_id;
    private int word_id;
    private String sentence;
    private String description;

    public Sentence(int sentence_id, int word_id, String sentence, String description) {
        this.sentence_id = sentence_id;
        this.word_id = word_id;
        this.sentence = sentence;
        this.description = description;
    }

    public int getSentence_id() {
        return sentence_id;
    }

    public int getWord_id() {
        return word_id;
    }

    public String getSentence() {
        return sentence;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public String toString() {
        return "Sentence{" +
                "sentence_id=" + sentence_id +
                ", word_id=" + word_id +
                ", sentence='" + sentence + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}
