package com.example.projectandroidfinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Score_Adapter extends RecyclerView.Adapter<Score_Adapter.PersonViewHolder> {
    private List<Person> Persons;

    public Score_Adapter(List<Person> Persons) {
        this.Persons = Persons;
    }


    public class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView prize;
        TextView diff;
        TextView time;
        TextView rank;

        public PersonViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.personName);
            prize = itemView.findViewById(R.id.personScore);
            diff = itemView.findViewById(R.id.personDiff);
            time = itemView.findViewById(R.id.personTime);
            rank = itemView.findViewById(R.id.personRank);

        }
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.high_score_cell,parent,false);
        PersonViewHolder personViewHolder = new PersonViewHolder(view);
        return personViewHolder;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        Person person = Persons.get(position);
        holder.name.setText(person.getName());
        holder.prize.setText(person.getPrize());
        holder.diff.setText(person.getDiff());
        holder.rank.setText(String.valueOf(position+1));
        holder.time.setText(person.getTime());

    }

    @Override
    public int getItemCount() {
        return Persons.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }



}
