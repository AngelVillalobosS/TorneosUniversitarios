package com.example.torneouniversitario.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.models.Player;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.VH> {

    public interface OnPlayerActionListener {
        void onEdit(Player p);
        void onDelete(Player p);
    }

    private List<Player> list;
    private OnPlayerActionListener listener;

    public PlayerAdapter(List<Player> list, OnPlayerActionListener listener){
        this.list = list;
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position){
        Player p = list.get(position);
        holder.tvName.setText(p.getName());
        holder.tvPosNum.setText(p.getPosition() + " - #" + p.getNumber());

        if (listener != null) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnEdit.setOnClickListener(v -> listener.onEdit(p));
            holder.btnDelete.setOnClickListener(v -> listener.onDelete(p));
        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount(){ return list.size(); }

    static class VH extends RecyclerView.ViewHolder{
        TextView tvName, tvPosNum;
        ImageButton btnEdit, btnDelete;
        VH(View v){
            super(v);
            tvName = v.findViewById(R.id.tvPlayerName);
            tvPosNum = v.findViewById(R.id.tvPlayerPosNum);
            btnEdit = v.findViewById(R.id.btnEditPlayer);
            btnDelete = v.findViewById(R.id.btnDeletePlayer);
        }
    }
}
