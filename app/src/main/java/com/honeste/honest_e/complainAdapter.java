package com.honeste.honest_e;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by abhis on 10-Mar-17.
 */

public class complainAdapter extends BaseAdapter {
Context context;
    ArrayList<commonComplain> commonComplains;

    class Holder
{
    ImageView imageView1,imageView2;
    TextView textView1,textView2,textView3,textView4,textView5,textView6;
    Button plusone,comment,setting;
}



    public complainAdapter(Context context, ArrayList<commonComplain> commonComplains) {
        this.context=context;
        this.commonComplains=commonComplains;
    }

    @Override
    public int getCount() {
        return commonComplains.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
final  Holder holder;
final int position = i;

        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.complain,null);

        holder=new Holder();
        holder.imageView1=(ImageView)view.findViewById(R.id.img1);
        holder.imageView2=(ImageView)view.findViewById(R.id.img2);
        holder.imageView1.setImageResource(R.mipmap.ic_launcher);


        //name
        holder.textView1=(TextView)view.findViewById(R.id.tvName);
        holder.textView1.setText(commonComplains.get(i).getName());

        //Category
        holder.textView2=(TextView)view.findViewById(R.id.tvCategory);
        holder.textView2.setText(commonComplains.get(i).getCategory());

        //Area
        holder.textView3=(TextView)view.findViewById(R.id.tvArea);
        holder.textView3.setText(commonComplains.get(i).getArea());

        //time
        holder.textView4=(TextView)view.findViewById(R.id.tvTime);
        holder.textView4.setText(commonComplains.get(i).getHours());

        //Complaint Description
        holder.textView5=(TextView)view.findViewById(R.id.tvComplaintDes);
        holder.textView5.setText(commonComplains.get(i).getDesc());

        holder.plusone = (Button) view.findViewById(R.id.onPlus);
        holder.plusone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Clicked plusone", Toast.LENGTH_SHORT).show();
            }
        });

        holder.comment = (Button) view.findViewById(R.id.onComment);
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "clicked comment", Toast.LENGTH_SHORT).show();
            }
        });

        holder.setting = (Button)view.findViewById(R.id.onSettings);
        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonComplain c1 = new commonComplain();
                Toast.makeText(view.getContext(), "clicked Settings done by " + commonComplains.get(position).getCompid(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}
