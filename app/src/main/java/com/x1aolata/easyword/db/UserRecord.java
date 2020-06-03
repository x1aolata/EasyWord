package com.x1aolata.easyword.db;

/**
 * @author x1aolata
 * @date 2020/5/29 8:26
 * @script 用户行为记录
 */
public class UserRecord {

    private int word_id;
    private int error_counts;
    private int correct_counts;


    public UserRecord(int word_id) {
        this.word_id = word_id;
        this.error_counts = 0;
        this.correct_counts = 0;
    }


    public UserRecord(int word_id, int error_counts, int correct_counts) {
        this.word_id = word_id;
        this.error_counts = error_counts;
        this.correct_counts = correct_counts;
    }

    public int getWord_id() {
        return word_id;
    }

    public int getError_counts() {
        return error_counts;
    }

    public int getCorrect_counts() {
        return correct_counts;
    }

    @Override
    public String toString() {
        return "UserRecord{" +
                "word_id=" + word_id +
                ", error_counts=" + error_counts +
                ", correct_counts=" + correct_counts +
                '}';
    }
}
