package com.example.todoList;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private List<Item> items;
    private final Context context;
    //public String item;

    public ItemAdapter(Context context,List<Item> items) {

        this.context=context;
        this.items = items;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

        if(!items.isEmpty()) {
            Item currentItem = items.get(position);
            holder.textViewName.setText(currentItem.getName());
            holder.textViewDescription.setText(currentItem.getDescription());
            holder.textViewDate.setText(String.valueOf(currentItem.getDate()));
            if (currentItem.getImage()!=null) {
                holder.imageView2.setImageBitmap(DataConverter.convertByteArray2Image(currentItem.getImage()));
            }
        }

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);

        return new ItemHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items) {
       this.items=items;
    }

    public Item getItemAt(int position){
        return items.get(position);
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private final TextView textViewName;
        private final TextView textViewDescription;
        private final TextView textViewDate;
        private final ImageView imageView2;

        ItemHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.name);
            textViewDescription = itemView.findViewById(R.id.description);
            textViewDate = itemView.findViewById(R.id.date);
            imageView2 = itemView.findViewById(R.id.imageView2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Item item = items.get(getAdapterPosition());

            Intent intent = new Intent(context, ItemUpdate.class);
            intent.putExtra("item", item);

            context.startActivity(intent);

        }
    }
}
