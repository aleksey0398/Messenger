package ru.alekseymitkin.messenger;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Митькин on 07.02.2016.
 */
public class RV_holder extends RecyclerView.Adapter<RV_holder.ViewHolder> {
    List<person> person;
    private Context context;

    RV_holder(List<person> person, Context context) {
        this.context = context;
        this.person = person;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(person.get(position).getName());
        holder.message.setText(person.get(position).getMessage());
        if(person.get(position).getUid()==null){
            try {
                holder.name.setGravity(Gravity.RIGHT);
                holder.message.setGravity(Gravity.RIGHT);
                holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.myMessage));
            } catch (Exception e){
                Log.e("adapter",e.getMessage()+" ");
            }
        }
    }

    @Override
    public int getItemCount() {
        return person.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, message;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.card_name);
            message = (TextView) itemView.findViewById(R.id.card_message);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

        }
    }

    public RV_holder add(person p) {
        person.add(p);
        notifyDataSetChanged();
        return null;
    }
}
