package com.example.admin.e_torn.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.e_torn.R;
import com.example.admin.e_torn.Store;
import com.example.admin.e_torn.Super;

import java.util.List;

/**
 * Created by Patango on 01/03/2017.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {


    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView storeName;
        ImageView storePhoto;

        public StoreViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            storeName = (TextView) itemView.findViewById(R.id.super_name);
            storePhoto = (ImageView) itemView.findViewById(R.id.super_photo);
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
            StoreAdapter.StoreViewHolder storeViewHolder = new StoreAdapter.StoreViewHolder(view);
            return  storeViewHolder;
        }

        @Override
        public void onBindViewHolder(StoreViewHolder storeViewHolder, int position) {
            storeViewHolder.storeName.setText(stores.get(position).getName());
            storeViewHolder.storePhoto.setImageResource(stores.get(position).getPhoto());
        }

        @Override
        public int getItemCount() {
            return stores.size();
        }
}
