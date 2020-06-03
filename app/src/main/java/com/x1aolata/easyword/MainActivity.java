package com.x1aolata.easyword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.x1aolata.easyword.dao.DAO;
import com.x1aolata.easyword.db.Sentence;
import com.x1aolata.easyword.db.Word;
import com.x1aolata.easyword.helper.BDCHelper;
import com.x1aolata.easyword.util.Utils;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    // 常量
    private static Handler handler;
    private ArrayList<Word> Words;
    private ArrayList<Sentence> sentences;
    private BDCHelper bdcHelper;


    // 控件
    private Button main_recognize;
    private Button main_no_recognize;
    private Button main_next_word;
    private TextView main_current_word_english;
    private TextView main_current_word_chinese;
    private TextView main_example_sentence;
    private ImageView main_menu;

    // 加载控制
    private LoadingDialog loadingDialog;

    // 音频播放
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        initBDCHelper();


        /**
         * 点击单词
         * 播放音频
         */
        main_current_word_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioPlayer(bdcHelper.getCurrentWord().getWord());
            }
        });


        /**
         * 认识
         */
        main_recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bdcHelper.recognize();
                displayTranslationAndSentences();
                setButtonEnableAfterJudge();
            }
        });

        /**
         * 不认识
         */
        main_no_recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bdcHelper.noRecognize();
                displayTranslationAndSentences();
                setButtonEnableAfterJudge();
            }
        });

        /**
         * 下一个
         */
        main_next_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Word word = bdcHelper.getNext();
                if (word != null) {
                    main_current_word_english.setText(word.getWord());
                    clearTranslationAndSentences();
                    audioPlayer(bdcHelper.getCurrentWord().getWord());
                } else {
                    bdcHelper.submitUserRecords();
                    initBDCHelper();
                }
                setButtonEnableAfterNext();
            }
        });


        main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {
                    Words = (ArrayList<Word>) msg.obj;
                    Toast.makeText(MainActivity.this, "单词获取成功", Toast.LENGTH_SHORT).show();
                }


                if (msg.what == 2) {
                    sentences = (ArrayList<Sentence>) msg.obj;
                    Toast.makeText(MainActivity.this, "例句获取成功", Toast.LENGTH_SHORT).show();
                }


                if (msg.what == 888) {

                    loadingDialog.close();
                    Word word = bdcHelper.getNext();
                    if (word != null) {
                        main_current_word_english.setText(word.getWord());
                        clearTranslationAndSentences();
                        audioPlayer(bdcHelper.getCurrentWord().getWord());
                    } else {
                        bdcHelper.submitUserRecords();
                        initBDCHelper();
                    }
                    setButtonEnableAfterNext();
                }

            }
        };

    }


    private void init() {
        // 获取实例
        main_recognize = findViewById(R.id.main_recognize);
        main_no_recognize = findViewById(R.id.main_no_recognize);
        main_next_word = findViewById(R.id.main_next_word);
        main_current_word_english = findViewById(R.id.main_current_word_english);
        main_current_word_chinese = findViewById(R.id.main_current_word_chinese);
        main_example_sentence = findViewById(R.id.main_example_sentence);
        main_menu = findViewById(R.id.main_menu);
        // loadingDialog初始化
        loadingDialog = new LoadingDialog(MainActivity.this);
        Utils.setTranslucent(this);
    }

    /**
     * 显示翻译及例句
     */
    public void displayTranslationAndSentences() {
        main_current_word_chinese.setText(bdcHelper.getCurrentWord().getDescription());
        main_example_sentence.setText("双语例句\n");
        for (Sentence s : bdcHelper.getSentencesOfCurrentWord()
        ) {
            main_example_sentence.setText(main_example_sentence.getText().toString() + "英文：" + s.getSentence() + "\n");
            main_example_sentence.setText(main_example_sentence.getText().toString() + "中文：" + s.getDescription() + "\n");
            main_example_sentence.setText(main_example_sentence.getText().toString() + "\n");
        }


    }

    /**
     * 清空翻译及例句
     */
    public void clearTranslationAndSentences() {
        main_current_word_chinese.setText("");
        main_example_sentence.setText("");
    }


    /**
     * 判断后设置按钮启用
     */
    public void setButtonEnableAfterJudge() {
        main_recognize.setEnabled(false);
        main_no_recognize.setEnabled(false);
        main_next_word.setEnabled(true);
    }

    /**
     * 下一个后设置按钮启用
     */
    public void setButtonEnableAfterNext() {
        main_recognize.setEnabled(true);
        main_no_recognize.setEnabled(true);
        main_next_word.setEnabled(false);
    }

    /**
     * 重新加载BDCHelper
     */
    public void initBDCHelper() {
        loadingDialog.setLoadingText("正在获取单词，请稍后...")//设置loading时显示的文字
                .setSuccessText("加载成功")
                .show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Word> Words = DAO.getInstance().pop_words(10);
                ArrayList<Sentence> sentences = DAO.getInstance().getExampleSentencesOfWords(Words);
                bdcHelper = new BDCHelper(Words, sentences);
                Message message = handler.obtainMessage();
                message.what = 888;
                handler.sendMessage(message);
            }
        }).start();
    }


    /**
     * 播放单词音频
     *
     * @param word
     */
    private void audioPlayer(String word) {
        if (mediaPlayer.isPlaying())
            return;

        String url = "https://dict.youdao.com/dictvoice?audio=" + word;
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.reset();
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
