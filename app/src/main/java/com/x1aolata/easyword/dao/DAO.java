package com.x1aolata.easyword.dao;

import android.graphics.Bitmap;
import android.util.Log;

import com.x1aolata.easyword.db.Sentence;
import com.x1aolata.easyword.db.User;
import com.x1aolata.easyword.db.UserRecord;
import com.x1aolata.easyword.db.Word;
import com.x1aolata.easyword.db.WordInfo;
import com.x1aolata.easyword.helper.DBHelper;
import com.x1aolata.easyword.util.Utils;
import com.x1aolata.easyword.util.wave.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author x1aolata
 * @date 2020/5/29 8:32
 * @script Data Access objects
 */
public class DAO {


    /**
     * 单例类
     */
    private static DAO instance;

    private DAO() {
    }

    public static DAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }
        return instance;
    }


    /**
     * 随机弹出 number 个单词
     *
     * @param number
     * @return
     */
    public ArrayList<Word> pop_words(int number) {
        return queryMulti("select word_id, word, description from dictionary_cet6 order by rand() limit ?;", number);
    }


    /**
     * 查询 WordInfo 信息
     *
     * @param user_id
     * @return
     */
    public ArrayList<WordInfo> getWordInfos(String user_id) {
        user_id = "user_" + user_id;
        String sql = "select word , description , error_counts, correct_counts from " + user_id + " join dictionary_cet6 d on " + user_id + ".word_id = d.word_id;";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        try {
            connection = DBHelper.getConnection();
            statement = connection.prepareStatement(sql);

            set = statement.executeQuery();
            ArrayList<WordInfo> wordInfoArrayList = new ArrayList<>();
            while (set.next()) {
                WordInfo wordInfo = new WordInfo(set.getString("word"), set.getString("description"), set.getInt("error_counts"), set.getInt("correct_counts"));
                wordInfoArrayList.add(wordInfo);
            }
            return wordInfoArrayList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBHelper.close(set, statement, connection);
        }

    }

    /**
     * 查询用户记录数
     *
     * @param user_id
     * @return
     */
    public int getCountsOfUser(String user_id) {
        return getCountsOfTable("user_" + user_id);
    }

    /**
     * 查询单个表中的记录个数
     *
     * @param table_name
     * @return
     */
    public int getCountsOfTable(String table_name) {
        String sql = "select count(*) counts from " + table_name;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        int counts = 0;
        try {
            connection = DBHelper.getConnection();
            statement = connection.prepareStatement(sql);

            set = statement.executeQuery();
            if (set.next()) {
                counts = set.getInt("counts");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBHelper.close(set, statement, connection);
        }
        return counts;


    }

    /**
     * 查询用户基本信息
     *
     * @param user_id
     * @return
     */
    public User getUserInfo(String user_id) {
        String sql = "select * from total_users where  user_id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        User user = null;
        try {
            connection = DBHelper.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, user_id);
            set = statement.executeQuery();
            if (set.next()) {
                user = new User(set.getString("user_id"), set.getString("phone_number"), set.getString("username"), null);
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBHelper.close(set, statement, connection);
        }

    }

    /**
     * 根据单词查询相应的例句
     *
     * @param words
     * @return
     */
    public ArrayList<Sentence> getExampleSentencesOfWords(ArrayList<Word> words) {
        ArrayList<Sentence> sentences = new ArrayList<>();
        for (Word word : words
        ) {
            ArrayList<Sentence> s = queryMulti_Sentence("select  * from sentence_cet6 where word_id = ? ", word.getWord_id());
            sentences.addAll(s);
        }
        return sentences;
    }

    /**
     * 判断此账户是否被注册
     *
     * @param user_id
     * @return
     */
    public boolean isRegistered(String user_id) {
        String sql = "select * from  total_users where  user_id = ?;";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        boolean isregistered = false;
        try {
            connection = DBHelper.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setObject(1, user_id);
            set = statement.executeQuery();
            if (set.next()) {
                isregistered = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBHelper.close(set, statement, connection);
        }
        return isregistered;
    }

    /**
     * 注册用户
     * 在总用户表中添加用户信息
     * 并且创建用户表
     *
     * @param user
     * @return
     */
    public int create_account(User user) {

        int insert = update("insert into total_users values (?, ?, ? ,? ,default)", user.getUser_id(), user.getPhone_number(), user.getUsername(), user.getPassword());
        String table_name = "user_" + user.getUser_id();
        int create = update("create table if not exists " + table_name + " like user_0000");
        return insert + create;
    }


    public int uploadAvatar(Bitmap bitmap, String user_id) {
        String s = Utils.image2Base64(bitmap);

        int result = update("insert into total_avatars values (?,?);", user_id, s);
        return result;
    }


    public Bitmap getAvatar(String user_id) {
        String sql = "select avatar from total_avatars where user_id=?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        String sss = "";
        try {
            connection = DBHelper.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setObject(1, user_id);
            set = statement.executeQuery();
            if (set.next()) {
                sss = set.getString("avatar");
            }
            return Utils.base642Image(sss);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBHelper.close(set, statement, connection);
        }
    }

    /**
     * 修改用户记录
     *
     * @param user_id
     * @param userRecord
     * @return
     */
    public boolean modifyUserRecord(String user_id, UserRecord userRecord) {

        user_id = "user_" + user_id;
        String sql = "insert ignore into " + user_id + " values (?, ?, ?)";
        int res = update(sql, userRecord.getWord_id(), userRecord.getError_counts(), userRecord.getCorrect_counts());
        if (res != 1) {
            int res2 = 0;
            if (userRecord.getCorrect_counts() > 0) {
                sql = "update  " + user_id + "  set correct_counts=correct_counts + 1 where word_id = ?";
                res2 = update(sql, userRecord.getWord_id());
            }

            if (userRecord.getError_counts() > 0) {
                sql = "update  " + user_id + "  set error_counts=error_counts + 1 where word_id = ?";
                res2 = update(sql, userRecord.getWord_id());
            }
            return res2 == 1;
        }
        return true;

    }


    /**
     * 验证密码
     *
     * @param user_id
     * @param password
     * @return true or false
     */
    public boolean verification_account(String user_id, String password) {
        String sql = "select * from total_users where user_id = ? and password = ?";
        boolean login = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        try {
            connection = DBHelper.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, user_id);
            statement.setString(2, password);
            set = statement.executeQuery();
            if (set.next()) {
                login = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBHelper.close(set, statement, connection);
        }

        return login;
    }


    /**
     * 通用增删改
     *
     * @param sql
     * @param params
     * @return 修改的行数
     */
    private int update(String sql, Object... params) {
        try {
            Connection connection = DBHelper.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            int update = statement.executeUpdate();
            return update;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 单条查询
     * Word
     *
     * @param sql
     * @param params
     * @return
     */
    private Word querySingle(String sql, Object... params) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        Word word = null;
        try {
            connection = DBHelper.getConnection();
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            set = statement.executeQuery();
            if (set.next()) {
                word = new Word(set.getInt("word_id"), set.getString("word"), set.getString("description"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBHelper.close(set, statement, connection);
        }
        return word;
    }

    /**
     * 多条查询
     * Word
     *
     * @param sql
     * @param params
     * @return
     */
    private ArrayList<Word> queryMulti(String sql, Object... params) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        try {
            connection = DBHelper.getConnection();
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            set = statement.executeQuery();
            ArrayList<Word> words = new ArrayList<>();
            while (set.next()) {
                Word word = new Word(set.getInt("word_id"), set.getString("word"), set.getString("description"));
                words.add(word);
            }
            return words;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBHelper.close(set, statement, connection);
        }
    }

    /**
     * 多条查询
     * 例句
     *
     * @param sql
     * @param params
     * @return
     */
    private ArrayList<Sentence> queryMulti_Sentence(String sql, Object... params) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        try {
            connection = DBHelper.getConnection();
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            set = statement.executeQuery();
            ArrayList<Sentence> sentences = new ArrayList<>();
            while (set.next()) {
                Sentence sentence = new Sentence(set.getInt("sentence_id"), set.getInt("word_id"), set.getString("sentence"), set.getString("description"));
                sentences.add(sentence);
            }
            return sentences;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBHelper.close(set, statement, connection);
        }
    }
}
