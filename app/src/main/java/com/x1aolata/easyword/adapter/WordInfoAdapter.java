package com.x1aolata.easyword.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.x1aolata.easyword.R;
import com.x1aolata.easyword.db.WordInfo;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author x1aolata
 * @date 2020/5/31 10:10
 * @script RecyclerView适配器
 */
public class WordInfoAdapter extends RecyclerView.Adapter<WordInfoAdapter.WordViewHolder> {

    private List<WordInfo> wordInfoList;

    public WordInfoAdapter(List<WordInfo> wordInfoList) {
        this.wordInfoList = wordInfoList;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wordinfo, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        WordInfo wordInfo = wordInfoList.get(position);
        holder.word.setText(wordInfo.getWord());
        if (wordInfo.getError_counts() > wordInfo.getCorrect_counts()) {
            holder.word.setTextColor(0xffff0000);
        }
        holder.description.setText(wordInfo.getDescription());
        holder.error_counts.setText(String.valueOf(wordInfo.getError_counts()));
        holder.correct_counts.setText(String.valueOf(wordInfo.getCorrect_counts()));
    }

    @Override
    public int getItemCount() {
        return wordInfoList.size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        TextView word;
        TextView description;
        TextView error_counts;
        TextView correct_counts;

        public WordViewHolder(View item_wordinfo) {
            super(item_wordinfo);
            this.word = item_wordinfo.findViewById(R.id.recycler_item_word);
            this.description = item_wordinfo.findViewById(R.id.recycler_item_description);
            this.error_counts = item_wordinfo.findViewById(R.id.recycler_item_error_counts);
            this.correct_counts = item_wordinfo.findViewById(R.id.recycler_item_correct_counts);
        }
    }

}