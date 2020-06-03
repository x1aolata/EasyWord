package com.x1aolata.easyword.helper;

import com.x1aolata.easyword.dao.DAO;
import com.x1aolata.easyword.db.Sentence;
import com.x1aolata.easyword.db.UserRecord;
import com.x1aolata.easyword.db.Word;
import com.x1aolata.easyword.util.Constants;

import java.util.ArrayList;

/**
 * @author x1aolata
 * @date 2020/5/29 8:35
 * @script 背单词帮助类
 */
public class BDCHelper {

    private ArrayList<Word> words;
    private ArrayList<Sentence> sentences;
    private ArrayList<UserRecord> userRecords = new ArrayList<>();
    private int current_word = -1;
    private int numberOfWords;


    public BDCHelper(ArrayList<Word> words, ArrayList<Sentence> sentences) {
        this.words = words;
        this.sentences = sentences;
        current_word = -1;
        numberOfWords = words.size();
    }

    /**
     * 获取下一个单词
     * 如果已经到末尾，则返回空值
     *
     * @return
     */
    public Word getNext() {

        current_word++;
        if (current_word < numberOfWords) {
            Word word = words.get(current_word);
            return word;
        } else {
            return null;
        }
    }

    /**
     * 获取当前单词
     *
     * @return
     */
    public Word getCurrentWord() {
        return words.get(current_word);
    }


    /**
     * 认识当前单词
     * 向学习记录中添加记录
     */
    public void recognize() {
        userRecords.add(new UserRecord(getCurrentWord().getWord_id(), 0, 1));

    }

    /**
     * 不认识当前单词
     * 向学习记录中添加记录
     */
    public void noRecognize() {
        userRecords.add(new UserRecord(getCurrentWord().getWord_id(), 1, 0));

    }

    /**
     * 向数据库提交学习记录
     *
     * @return
     */
    public boolean submitUserRecords() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (UserRecord u :
                        userRecords) {
                    DAO.getInstance().modifyUserRecord(Constants.user_id, u);

                }
            }
        }).start();


        return true;
    }

    /**
     * 获取当前单词的例句
     *
     * @return
     */
    public ArrayList<Sentence> getSentencesOfCurrentWord() {
        ArrayList<Sentence> sentencesOfCurrentWord = new ArrayList<>();
        for (Sentence s : sentences
        ) {
            if (s.getWord_id() == getCurrentWord().getWord_id()) {
                sentencesOfCurrentWord.add(s);
            }
        }
        return sentencesOfCurrentWord;
    }
}
