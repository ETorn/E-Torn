package com.example.admin.e_torn.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.e_torn.R;
import com.example.admin.e_torn.Store;
import com.example.admin.e_torn.StoreActivity;
import com.example.admin.e_torn.Super;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patango on 01/03/2017.
 */



public class SuperAdapter extends RecyclerView.Adapter<SuperAdapter.SuperViewHolder>{
    private final Context context;

    List<Super> supers;
    List<Store> stores;

    public void startStore () {
        Intent intent = new Intent(context, StoreActivity.class);
        intent.putParcelableArrayListExtra("stores", (ArrayList<? extends Parcelable>) stores); // Pasem a StoreActivity la array de Stores a carregar
        context.startActivity(intent);
    }

    public SuperAdapter(Context context, List<Super> supers) {
        this.context = context;
        this.supers = supers;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        SuperViewHolder superViewHolder = new SuperViewHolder(view);
        return  superViewHolder;
    }

    @Override
    public void onBindViewHolder(SuperViewHolder superViewHolder, int position) {
        superViewHolder.superAddress.setText(supers.get(position).getAddress());
        superViewHolder.superCity.setText(supers.get(position).getCity());
        //superViewHolder.superDistance.setText(supers.get(position).getDistance());
        superViewHolder.superDistance.setText("10m"); // Canviar quan tinguem la localització funcional
    }

    @Override
    public int getItemCount() {
        return supers.size();
    }



    public class SuperViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView superCity;
        TextView superAddress;
        TextView superDistance;

        public SuperViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            superCity = (TextView) itemView.findViewById(R.id.item_city);
            superAddress = (TextView) itemView.findViewById(R.id.item_name);
            superDistance = (TextView) itemView.findViewById(R.id.item_distance);
        }

    }
}
