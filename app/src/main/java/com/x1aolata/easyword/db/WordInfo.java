package com.x1aolata.easyword.db;

/**
 * @author x1aolata
 * @date 2020/5/31 9:53
 * @script 单词信息包括个人信息
 */
public class WordInfo {

    private String word;
    private String description;
    private int error_counts;
    private int correct_counts;


    public WordInfo(String word, String description, int error_counts, int correct_counts) {
        this.word = word;
        this.description = description;
        this.error_counts = error_counts;
        this.correct_counts = correct_counts;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getError_counts() {
        return error_counts;
    }

    public void setError_counts(int error_counts) {
        this.error_counts = error_counts;
    }

    public int getCorrect_counts() {
        return correct_counts;
    }

    public void setCorrect_counts(int correct_counts) {
        this.correct_counts = correct_counts;
    }

    @Override
    public String toString() {
        return "WordInfo{" +
                "word='" + word + '\'' +
                ", description='" + description + '\'' +
                ", error_counts=" + error_counts +
                ", correct_counts=" + correct_counts +
                '}';
    }
}
