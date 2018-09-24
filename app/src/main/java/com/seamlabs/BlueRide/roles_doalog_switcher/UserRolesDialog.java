package com.seamlabs.BlueRide.roles_doalog_switcher;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.response.RolesResponseModel;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.utils.Constants;
import com.seamlabs.BlueRide.utils.PrefUtils;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.SERVER_ERROR;

public class UserRolesDialog {

    Activity activity;
    final static String LIST_KEY = "RolesList";
    RolesRecyclerViewAdapter adapter;

    public Dialog getNotificationDialog(final ArrayList<RolesResponseModel> roles, final Activity activity) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_roles_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        RecyclerView roles_recyclerView = dialog.findViewById(R.id.roles_recyclerView);
        adapter = new RolesRecyclerViewAdapter(activity, convertRoleResponseToRoleModel(roles));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        roles_recyclerView.setLayoutManager(layoutManager);
        roles_recyclerView.setItemAnimator(new DefaultItemAnimator());
        roles_recyclerView.setAdapter(adapter);

        final Button saveRole = dialog.findViewById(R.id.save);
        Button cancelSelection = dialog.findViewById(R.id.cancel);
        saveRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = adapter.getSelectedPosition();
                RolesResponseModel rolesResponseModel = roles.get(selectedPosition);
                if (!rolesResponseModel.getName().equals(UserSettingsPreference.getUserType(getMyApplicationContext()))) {
                    UserSettingsPreference.setUserType(getMyApplicationContext(), rolesResponseModel.getName());
                    switchUserAccount(rolesResponseModel.getName());
                    dialog.dismiss();
                    activity.recreate();
                } else {
                    dialog.dismiss();
                }
            }
        });
        cancelSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private ArrayList<RoleItemModel> convertRoleResponseToRoleModel(ArrayList<RolesResponseModel> roles) {
        ArrayList<RoleItemModel> rolesList = new ArrayList<>();
        for (RolesResponseModel rolesResponseModel : roles) {
            RoleItemModel roleItemModel = new RoleItemModel();
            roleItemModel.setName(rolesResponseModel.getName());
            roleItemModel.setId(rolesResponseModel.getId());
            roleItemModel.setCreated_at(rolesResponseModel.getCreated_at());
            roleItemModel.setUpdated_at(rolesResponseModel.getUpdated_at());
            if (UserSettingsPreference.getUserType(getMyApplicationContext()).equals(roleItemModel.getName()))
                roleItemModel.setSelected(true);
            rolesList.add(roleItemModel);
        }
        return rolesList;
    }

    private void switchUserAccount(String login_as) {
        ApiService apiService = ApiClient.getClient(getMyApplicationContext())
                .create(ApiService.class);

        apiService.switchAccount(login_as)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response<UserResponseModel>>() {
                    @Override
                    public void onSuccess(Response<UserResponseModel> response) {
                        if (response.isSuccessful()) {
                            UserResponseModel userResponseModel = response.body();
                            Log.i("switchAccount", "isSuccessful " + userResponseModel.getLogin_as());
                            if (userResponseModel != null)
                                UserSettingsPreference.saveUserProfile(getMyApplicationContext(), userResponseModel);
                            return;
                        }
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Log.i("switchAccount", "error");
                        } catch (Exception e) {
                            Log.i("switchAccount", "onSuccessException " + e.getMessage().toString());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.i("updateUserData", "Exception " + e.getMessage().toString());
                    }
                });
    }
}
