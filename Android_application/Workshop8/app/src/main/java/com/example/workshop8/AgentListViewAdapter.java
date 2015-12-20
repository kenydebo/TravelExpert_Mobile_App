package com.example.workshop8;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.View.OnClickListener;

/*
 * Purpose: AgentListViewAdapter class for TravelExperts Database
 * Author: Mark Poffenroth
 * Date: Sept 2015
 * NOTE: This class modified from:
 * http://www.androidbegin.com/tutorial/android-search-listview-using-filter/
 */
public class AgentListViewAdapter extends BaseAdapter {

    // Declare Variables
    private Context myContext;
    private LayoutInflater inflater;
    private List<Agent> agentList = null;
    private ArrayList<Agent> arraylist;

    public AgentListViewAdapter(Context context, List<Agent> agList) {
        // Initialize
        myContext = context;
        this.agentList = agList;
        inflater = LayoutInflater.from(myContext);
        this.arraylist = new ArrayList<Agent>();
        this.arraylist.addAll(agList);
    }

    public class ViewHolder {
        TextView agentName;
    }

    @Override
    public int getCount() {
        return agentList.size();
    }

    @Override
    public Agent getItem(int position) {
        return agentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_agent_list, null);
            // Locate TextViews in activity_agent_list.xml
            holder.agentName = (TextView) view.findViewById(R.id.txtAgtLastName);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set results into TextViews
        if (agentList.get(position).getAgtMiddleInitial() == null)
            holder.agentName.setText(agentList.get(position).getAgtFirstName() + " " + agentList.get(position).getAgtLastName());
        else
            holder.agentName.setText(agentList.get(position).getAgtFirstName() + " " +
                agentList.get(position).getAgtMiddleInitial() + " " + agentList.get(position).getAgtLastName());

        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to AgentDetailActivity Class
                Intent intent = new Intent(myContext, AgentDetailActivity.class);

                // NOTE: Cast int to string for intent to work properly
                intent.putExtra("AgentId", String.valueOf(agentList.get(position).getAgentId()));
                intent.putExtra("AgtFirstName", (agentList.get(position).getAgtFirstName()));
                intent.putExtra("AgtLastName", (agentList.get(position).getAgtLastName()));
                intent.putExtra("AgtMiddleInitial", (agentList.get(position).getAgtMiddleInitial()));
                intent.putExtra("AgtBusPhone", (agentList.get(position).getAgtBusPhone()));
                intent.putExtra("AgtEmail", (agentList.get(position).getAgtEmail()));
                intent.putExtra("AgtPosition", (agentList.get(position).getAgtPosition()));
                intent.putExtra("AgencyId", String.valueOf(agentList.get(position).getAgencyId()));

                // Start AgentDetailActivity Class
                myContext.startActivity(intent);
            }
        });

        return view;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        // Clear ListView
        agentList.clear();

        // If no search filter text then show all items in list
        if (charText.length() == 0) {
            agentList.addAll(arraylist);
        }
        else
        {
            // Check each agent object in array for text matching filter text
            for (Agent agent : arraylist)
            {
                // NOTE: to search by first letter replace contains(charText) w/ startsWith(charText)
                //if (agent.getAgtLastName().toLowerCase(Locale.getDefault()).contains(charText))
                if (agent.getAgtLastName().toLowerCase(Locale.getDefault()).contains(charText) || agent.getAgtFirstName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    agentList.add(agent);
                }
            }
        }
        notifyDataSetChanged();
    }


}
