package com.seamlabs.BlueRide.roles_doalog_switcher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.response.RolesResponseModel;
import com.seamlabs.BlueRide.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.HELPER_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.MENTOR_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.PARENT_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.TEACHER_USER_TYPE;

public class RolesRecyclerViewAdapter extends RecyclerView.Adapter<RolesRecyclerViewAdapter.StudentsViewHolderLayout> {

    Context context;
    ArrayList<RoleItemModel> rolesList = new ArrayList<>();

    private int selectedPosition = -1;

    public RolesRecyclerViewAdapter(Context context, ArrayList<RoleItemModel> schools) {
        this.context = context;
        this.rolesList = schools;
    }

    @NonNull
    @Override
    public StudentsViewHolderLayout onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.roles_recyclerview_item, parent, false);
        StudentsViewHolderLayout studentsViewHolderLayout = new StudentsViewHolderLayout(v);
        return studentsViewHolderLayout;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsViewHolderLayout holder, int position) {
        final RoleItemModel role = rolesList.get(position);

        if (role.getName().equals(PARENT_USER_TYPE))
            holder.role_radiobutton.setText(context.getResources().getString(R.string.parent));
        else if (role.getName().equals(MENTOR_USER_TYPE))
            holder.role_radiobutton.setText(context.getResources().getString(R.string.mentor));
        else if (role.getName().equals(TEACHER_USER_TYPE))
            holder.role_radiobutton.setText(context.getResources().getString(R.string.teacher));
        else if (role.getName().equals(HELPER_USER_TYPE))
            holder.role_radiobutton.setText(context.getResources().getString(R.string.helper));
        if (role.isSelected())
            holder.role_radiobutton.setChecked(true);
        else
            holder.role_radiobutton.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return rolesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class StudentsViewHolderLayout extends ViewHolder {
        @BindView(R.id.role_radiobutton)
        RadioButton role_radiobutton;

        public StudentsViewHolderLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            role_radiobutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectedItemAndClearOther(getAdapterPosition());
                }
            });
        }
    }

    private void setSelectedItemAndClearOther(int position) {
        for (int i = 0; i < rolesList.size(); i++) {
            if (i == position) {
                rolesList.get(i).setSelected(!rolesList.get(i).isSelected());
            } else {
                rolesList.get(i).setSelected(false);
            }
        }
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        for (int i = 0; i < rolesList.size(); i++) {
            if (rolesList.get(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }
}