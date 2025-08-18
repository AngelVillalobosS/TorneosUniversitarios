package com.example.torneouniversitario.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.models.MatchModel;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.VH> {

    public interface OnMatchActionListener {
        void onEdit(MatchModel m);
        void onDelete(MatchModel m);
    }

    private List<MatchModel> list;
    private OnMatchActionListener listener;

    public MatchAdapter(List<MatchModel> list, OnMatchActionListener listener){
        this.list = list;
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position){
        MatchModel m = list.get(position);
        holder.tvMatchInfo.setText("Partido: " + m.getDate() + " " + m.getTime() + " en " + m.getPlace());
        if (listener != null) {
            holder.btnEdit.setOnClickListener(v -> listener.onEdit(m));
            holder.btnDelete.setOnClickListener(v -> listener.onDelete(m));
        }
    }

    @Override
    public int getItemCount(){ return list.size(); }

    static class VH extends RecyclerView.ViewHolder{
        TextView tvMatchInfo;
        ImageButton btnEdit, btnDelete;
        VH(View v){
            super(v);
            tvMatchInfo = v.findViewById(R.id.tvMatchInfo);
            btnEdit = v.findViewById(R.id.btnEditMatch);
            btnDelete = v.findViewById(R.id.btnDeleteMatch);
        }
    }
}
