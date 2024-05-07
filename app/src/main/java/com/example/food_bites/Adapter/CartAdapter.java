package com.example.food_bites.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.food_bites.Domain.Foods;
import com.example.food_bites.Helper.ChangeNumberItemsListener;
import com.example.food_bites.Helper.ManagmentCart;
import com.example.food_bites.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Foods> list;
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Foods> list, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        managmentCart = new ManagmentCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart,parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {

        double total = list.get(position).getNumberInCart() * list.get(position).getPrice();
        DecimalFormat df = new DecimalFormat("#.##");

        holder.title.setText(list.get(position).getTitle());
        holder.feeEachItem.setText("$ " + df.format(total));
        holder.totalEachItem.setText(list.get(position).getNumberInCart()+" X $"+(
                list.get(position).getNumberInCart()
                ));
        holder.num.setText(list.get(position).getNumberInCart()+"");

        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.plusItem.setOnClickListener(view -> managmentCart.plusNumberItem(list, position, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                notifyDataSetChanged();
                changeNumberItemsListener.change();
            }
        }));
        holder.minusItem.setOnClickListener(view -> managmentCart.minusNumberItem(list, position, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                notifyDataSetChanged();
                changeNumberItemsListener.change();
            }
        }));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView title, feeEachItem, plusItem, minusItem;
        ImageView pic;
        TextView totalEachItem, num;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTxt);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            num = itemView.findViewById(R.id.numberItemTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
