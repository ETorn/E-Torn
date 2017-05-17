package com.example.admin.e_torn.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.e_torn.R;
import com.example.admin.e_torn.models.Store;

import java.util.List;

/**
 * Created by Patango on 01/03/2017.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private static final String TAG = "StoreAdapter";

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView storeName;
        TextView itemTime;
        TextView actualNumber;
        TextView disponibleNumber;
        TextView userTurn;
        ImageView timeIcon;

        StoreViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewStore);
            storeName = (TextView) itemView.findViewById(R.id.item_store_name);
            itemTime = (TextView) itemView.findViewById(R.id.item_time);
            timeIcon = (ImageView) itemView.findViewById(R.id.timeIcon);
            actualNumber = (TextView) itemView.findViewById(R.id.textView_actual_number);
            userTurn = (TextView) itemView.findViewById(R.id.disponible);
            disponibleNumber = (TextView) itemView.findViewById(R.id.textView_disponible_number);
        }
    }

    List<Store> stores;

    public StoreAdapter(List<Store> stores) {
        this.stores = stores;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.store_item, viewGroup, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoreViewHolder storeViewHolder, int position) {
        storeViewHolder.storeName.setText(stores.get(position).getName());
        if (stores.get(position).getAproxTime() < 1) {
            storeViewHolder.itemTime.setVisibility(View.GONE);
            storeViewHolder.timeIcon.setVisibility(View.GONE);
        }
        else {
            storeViewHolder.itemTime.setVisibility(View.VISIBLE);
            storeViewHolder.timeIcon.setVisibility(View.VISIBLE);
        }
        storeViewHolder.itemTime.setText(String.valueOf(stores.get(position).getAproxTime()));
        Log.d(TAG, "timeAproxAdapter: " + String.valueOf(stores.get(position).getAproxTime()));
        storeViewHolder.actualNumber.setText(String.valueOf(stores.get(position).getStoreTurn()));
        storeViewHolder.disponibleNumber.setText(String.valueOf(stores.get(position).getUsersTurn()));
        if (stores.get(position).isInTurn()) {
            storeViewHolder.userTurn.setText("El teu torn");
        }
        else {
            storeViewHolder.userTurn.setText("Disponible");
        }

        if (stores.size() == 0) {
            storeViewHolder.storeName.setText(R.string.info_no_stores);
        }
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }
}
