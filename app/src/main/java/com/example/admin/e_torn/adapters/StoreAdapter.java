package com.example.admin.e_torn.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.e_torn.R;
import com.example.admin.e_torn.models.Store;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patango on 01/03/2017.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {


    static class StoreViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView storeName;
        TextView itemTime;
        TextView actualNumber;
        TextView disponibleNumber;
        TextView userTurn;

        StoreViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewStore);
            storeName = (TextView) itemView.findViewById(R.id.item_store_name);
            itemTime = (TextView) itemView.findViewById(R.id.item_time);
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
        //storeViewHolder.itemTime.setText(stores.get(position).getTime());
        storeViewHolder.actualNumber.setText(String.valueOf(stores.get(position).getStoreTurn()));
        storeViewHolder.disponibleNumber.setText(String.valueOf(stores.get(position).getUsersTurn()));
        if (stores.get(position).isInTurn()) {
            storeViewHolder.userTurn.setText("El teu torn");
        }
        else {
            storeViewHolder.userTurn.setText("Disponible");
        }

        if (stores.size() == 0) {
            storeViewHolder.storeName.setText("No stores");
        }
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }
}
