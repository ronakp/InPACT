package com.example.izhang.inpact;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link inviteFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link inviteFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class inviteFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public String ownerEmail;
    RecyclerView rv;
    MyAdapter_invites adapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment inviteFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static inviteFrag newInstance(String param1, String param2) {
        inviteFrag fragment = new inviteFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public inviteFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;
    List<contract> contracts = new ArrayList<contract>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_invite, container, false);

        rv = (RecyclerView)view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);

        final ArrayList<Integer> cids = new ArrayList<Integer>();

        SharedPreferences prefs = getActivity().getSharedPreferences("owneremail", getActivity().MODE_PRIVATE);
        ownerEmail = prefs.getString("owneremail", null);
        ParseQuery query = new ParseQuery("UserCommit");
        query.whereContains("status", "invited");
        Log.v("Owner", ownerEmail);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject dealsObject : objects) {
                        if(dealsObject.get("email").toString().equals(ownerEmail)) cids.add((Integer)dealsObject.get("cid"));
                    }
                    for(int i : cids){
                        Log.v("CIDS", cids.toString());
                        ParseQuery cidQuery = new ParseQuery("Commitment");
                        cidQuery.whereEqualTo("cid",i);
                        cidQuery.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    for (ParseObject dealsObject : objects) {
                                        String contractTitle = (String) dealsObject.get("cheader");
                                        Date createdAt = dealsObject.getCreatedAt();
                                        Long currentTime = System.currentTimeMillis();
                                        Long age = currentTime - createdAt.getTime();
                                        int hours = (int) (age / (1000 * 60 * 60));
                                        contract a = new contract(contractTitle, Integer.toString(hours) + " Hours");
                                        addToContractList(a, rv);
                                    }
                                } else {
                                    Log.d("ERROR IN PARSE HOME", "ERROR: " + e.getMessage());
                                }
                            }
                        });
                    }
                } else {
                    Log.d("ERROR IN PARSE HOME", "ERROR: " + e.getMessage());
                }
            }
        });

        return view;
    }


    public void addToContractList(contract con, RecyclerView rv){
        contracts.add(con);
        adapter = new MyAdapter_invites(contracts);
        adapter.setOwnerEmail(ownerEmail);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void notifyDataChangeRv(){
        adapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
