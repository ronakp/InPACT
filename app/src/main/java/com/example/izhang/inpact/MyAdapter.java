package com.example.izhang.inpact;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ContractViewHolder>{

    private Context context;
    String ownerEmail;
    public MyAdapter(Context context){
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
            final View itemViewFinal = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent contractIntent = new Intent(itemViewFinal.getContext(), contractDetails.class);
                    contractIntent.putExtra("ContractName", contractName.getText().toString());
                    contractIntent.putExtra("ContractAge", contractAge.getText().toString());
                    contractIntent.putExtra("Status", "Complete");
                    itemViewFinal.getContext().startActivity(contractIntent);
                }
            });
        }


    }

    List<contract> contracts;

    MyAdapter(List<contract> contracts){
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
    public void onBindViewHolder(final ContractViewHolder contractViewHolder, int i) {
        contractViewHolder.contractName.setText(contracts.get(i).name);
        contractViewHolder.contractAge.setText(contracts.get(i).age);
        contractViewHolder.button.setText("Finish");
        contractViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Approval of Parties");
                alertDialogBuilder
                        .setMessage("Did you approve of the other party members here?")
                        .setCancelable(true)
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No, I don't", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Todo: Create a positive negetive review for the person
                            }
                        })
                        .setNeutralButton("Yes, I do", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Todo: Create a positive Cancel review for the person
                                final ParseQuery query = new ParseQuery("Commitment");
                                query.whereEqualTo("cheader", contractViewHolder.contractName.getText().toString());
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null) {
                                            int cid = 0;
                                            String email = "";
                                            for (ParseObject dealsObject : objects) {
                                                cid = dealsObject.getInt("cid");
                                            }

                                            ParseQuery changeStatus = new ParseQuery("UserCommit");
                                            changeStatus.whereEqualTo("cid", cid);
                                            changeStatus.whereEqualTo("email", ownerEmail);
                                            changeStatus.findInBackground(new FindCallback<ParseObject>() {
                                                public void done(List<ParseObject> objects, ParseException e) {
                                                    if (e == null) {
                                                        for (ParseObject dealsObject : objects) {
                                                            dealsObject.put("status", "complete");
                                                            dealsObject.saveInBackground();
                                                        }
                                                    } else {
                                                        Log.d("ERROR IN PARSE HOME", "ERROR: " + e.getMessage());
                                                    }
                                                }
                                            });
                                        } else {
                                            Log.d("ERROR IN PARSE HOME", "ERROR: " + e.getMessage());
                                        }
                                    }
                                });
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}