package com.example.mahmoudsamir.schoolappand.parent_home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.parent_home.model.SchoolModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SchoolsRecyclerAdapter extends RecyclerView.Adapter<SchoolsRecyclerAdapter.SchoolsViewHolderLayout> {

    Context context;
    ArrayList<SchoolModel> schools = new ArrayList<>();

    public SchoolsRecyclerAdapter(Context context, ArrayList<SchoolModel> schools) {
        this.context = context;
        this.schools = schools;
    }

    @NonNull
    @Override
    public SchoolsViewHolderLayout onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_recyckerview_item, parent, false);
        SchoolsViewHolderLayout schoolsViewHolderLayout = new SchoolsViewHolderLayout(v);
        return schoolsViewHolderLayout;
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolsViewHolderLayout holder, int position) {
        final SchoolModel schoolModel = schools.get(position);

        if (schoolModel.isMarked())
            holder.marked_icon.setVisibility(View.VISIBLE);
        else
            holder.marked_icon.setVisibility(View.INVISIBLE);
        if (schoolModel.getSchoolCover() != null) {
            Uri uri = Uri.parse(schoolModel.getSchoolCover());
            holder.school_cover.setImageURI(uri);
        }
        holder.school_address.setText(schoolModel.getShcoolAddress());
        holder.school_title.setText(schoolModel.getSchoolTitle());
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    SchoolModel schoolModel = schools.get(position);
                    schoolModel.setMarked(!schoolModel.isMarked());
                    notifyItemChanged(position);
                }
            });
        }
    }

    class SchoolsViewHolderLayout extends ViewHolder {

        @BindView(R.id.school_cover)
        SimpleDraweeView school_cover;

        @BindView(R.id.school_address)
        TextView school_address;

        @BindView(R.id.school_title)
        TextView school_title;

        @BindView(R.id.marked_icon)
        ImageView marked_icon;

        public SchoolsViewHolderLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
