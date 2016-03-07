
package com.example.izhang.inpact;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

public class MyAdapter_invites extends RecyclerView.Adapter<MyAdapter_invites.ContractViewHolder>{
    private Context context;
    String ownerEmail;

    public MyAdapter_invites(Context context){
        this.context = context;
    }

    public void setOwnerEmail(String email){
        this.ownerEmail = email;
    }
    public static class ContractViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView contractName;
        TextView contractAge;
        Button acceptButton;
        Button declineButton;


        ContractViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);

            contractName = (TextView)itemView.findViewById(R.id.person_name);
            contractAge = (TextView)itemView.findViewById(R.id.person_age);
            acceptButton = (Button)itemView.findViewById(R.id.accept);
            declineButton = (Button)itemView.findViewById(R.id.decline);
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

    MyAdapter_invites(List<contract> contracts){
        this.contracts = contracts;
    }

    @Override
    public int getItemCount(){
        return contracts.size();
    }

    @Override
    public ContractViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_invite, viewGroup, false);
        ContractViewHolder pvh = new ContractViewHolder(v);
        return pvh;
    }


    // TODO : Change the text to complete contract or leave feedback
    @Override
    public void onBindViewHolder(final ContractViewHolder contractViewHolder, int i) {

        contractViewHolder.contractName.setText(contracts.get(i).name);
        contractViewHolder.contractAge.setText(contracts.get(i).age);
        contractViewHolder.acceptButton.setText("Accept");
        contractViewHolder.acceptButton.setBackgroundColor(Color.rgb(135, 211, 124));
        contractViewHolder.declineButton.setText("Decline");
        contractViewHolder.declineButton.setBackgroundColor(Color.rgb(192, 57, 43));
        contractViewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Accept Commitment");
                alertDialogBuilder
                        .setMessage("Do you accept this commitment?")
                        .setCancelable(true)
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
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
                                            for (ParseObject dealsObject : objects) {
                                                cid = dealsObject.getInt("cid");
                                            }
                                            Log.d("PARSE", Integer.toString(cid));

                                            ParseQuery changeStatus = new ParseQuery("UserCommit");
                                            changeStatus.whereEqualTo("cid", cid);
                                            changeStatus.whereEqualTo("email", ownerEmail);
                                            changeStatus.findInBackground(new FindCallback<ParseObject>() {
                                                public void done(List<ParseObject> objects, ParseException e) {
                                                    if (e == null) {
                                                        for (ParseObject dealsObject : objects) {
                                                            Log.d("PARSE", "INSIDE Done");
                                                            dealsObject.put("status", "ongoing");
                                                            dealsObject.saveInBackground();
                                                            MyAdapter_invites.super.notifyDataSetChanged();
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
        contractViewHolder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Accept Commitment");
                alertDialogBuilder
                        .setMessage("Do you decline this commitment?")
                        .setCancelable(true)
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setNeutralButton("I decline", new DialogInterface.OnClickListener() {
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
                                            Log.d("PARSE", Integer.toString(cid));

                                            ParseQuery changeStatus = new ParseQuery("UserCommit");
                                            changeStatus.whereEqualTo("cid", cid);
                                            changeStatus.whereEqualTo("email", ownerEmail);
                                            changeStatus.findInBackground(new FindCallback<ParseObject>() {
                                                public void done(List<ParseObject> objects, ParseException e) {
                                                    if (e == null) {
                                                        for (ParseObject dealsObject : objects) {
                                                            try {
                                                                dealsObject.delete();
                                                            } catch (ParseException p) {
                                                                p.printStackTrace();
                                                            }
                                                            dealsObject.saveInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                    MyAdapter_invites.super.notifyDataSetChanged();
                                                                }
                                                            });

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

    public void acceptCommit(){

    }

}