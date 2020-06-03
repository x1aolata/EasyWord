package com.x1aolata.easyword;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.x1aolata.easyword.adapter.WordInfoAdapter;
import com.x1aolata.easyword.dao.DAO;
import com.x1aolata.easyword.db.WordInfo;
import com.x1aolata.easyword.util.Constants;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;


public class WordInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static Handler handler;
    private List<WordInfo> wordInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_info);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {
                    wordInfoList = (ArrayList<WordInfo>) msg.obj;
                    WordInfoAdapter wordInfoAdapter = new WordInfoAdapter(wordInfoList);
                    recyclerView.setAdapter(wordInfoAdapter);
                }

            }
        };


        new Thread(new Runnable() {
            @Override
            public void run() {

                List<WordInfo> wordInfoList = DAO.getInstance().getWordInfos(Constants.user_id);

                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = wordInfoList;
                handler.sendMessage(message);
            }
        }).start();

    }

    public void init() {
        // 获取实例
        recyclerView = findViewById(R.id.word_info_recycler_view);

    }

}
