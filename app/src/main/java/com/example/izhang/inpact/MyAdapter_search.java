package com.example.izhang.inpact;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.List;

public class MyAdapter_search extends RecyclerView.Adapter<MyAdapter_search.ContractViewHolder>{
    private Context context;
    String ownerEmail;
    public MyAdapter_search(Context context){
        this.context = context;
    }

    public void setOwnerEmail(String email){
        this.ownerEmail = email;
    }

    public static class ContractViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView contractName;
        TextView contractAge;
        TextView inpactScore;
        Button button;



        ContractViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_search);

            contractName = (TextView)itemView.findViewById(R.id.person_name);
            contractAge = (TextView)itemView.findViewById(R.id.person_age);
            inpactScore = (TextView) itemView.findViewById(R.id.inpact_score);
            button = (Button)itemView.findViewById(R.id.button);
            final View itemViewFinal = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent contractIntent = new Intent(itemViewFinal.getContext(), searchFrag.class);
                    contractIntent.putExtra("ContractName", contractName.getText().toString());
                    contractIntent.putExtra("ContractAge", contractAge.getText().toString());
                    contractIntent.putExtra("inpactScore", inpactScore.getText().toString());
                    itemViewFinal.getContext().startActivity(contractIntent);
                }
            });
        }


    }

    List<contract> contracts;

    MyAdapter_search(List<contract> contracts){
        this.contracts = contracts;
    }

    @Override
    public int getItemCount(){
        return contracts.size();
    }

    @Override
    public ContractViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_search, viewGroup, false);
        ContractViewHolder pvh = new ContractViewHolder(v);
        return pvh;
    }

    // TODO : Change the text to complete contract or leave feedback
    @Override
    public void onBindViewHolder(final ContractViewHolder contractViewHolder, int i) {
        contractViewHolder.contractName.setText(contracts.get(i).name);
        contractViewHolder.inpactScore.setText(contracts.get(i).age);
        contractViewHolder.inpactScore.setTextSize(60);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}