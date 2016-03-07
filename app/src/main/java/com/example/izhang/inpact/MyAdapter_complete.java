package com.example.izhang.inpact;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

public class MyAdapter_complete extends RecyclerView.Adapter<MyAdapter_complete.ContractViewHolder>{
    private Context context;
    String ownerEmail;

    public MyAdapter_complete(Context context){
        this.context = context;
    }

    public void setOwnerEmail(String email){
        this.ownerEmail = email;
    }

    public static class ContractViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView contractName;
        TextView contractAge;
        Button button;
        ContractViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);

            contractName = (TextView)itemView.findViewById(R.id.person_name);
            contractAge = (TextView)itemView.findViewById(R.id.person_age);
            button = (Button)itemView.findViewById(R.id.button);
            button.setVisibility(View.INVISIBLE);
            final View itemViewFinal = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent contractIntent = new Intent(itemViewFinal.getContext(), contractDetails.class);
                    contractIntent.putExtra("ContractName", contractName.getText().toString());
                    contractIntent.putExtra("ContractAge", contractAge.getText().toString());
                    itemViewFinal.getContext().startActivity(contractIntent);
                }
            });
        }


    }

    List<contract> contracts;

    MyAdapter_complete(List<contract> contracts){
        this.contracts = contracts;
    }

    @Override
    public int getItemCount(){
        return contracts.size();
    }

    @Override
    public ContractViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_home, viewGroup, false);
        ContractViewHolder pvh = new ContractViewHolder(v);
        return pvh;
    }

    // TODO : Change the text to complete contract or leave feedback
    @Override
    public void onBindViewHolder(ContractViewHolder contractViewHolder, int i) {
        contractViewHolder.contractName.setText(contracts.get(i).name);
        contractViewHolder.contractAge.setText(contracts.get(i).age);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}