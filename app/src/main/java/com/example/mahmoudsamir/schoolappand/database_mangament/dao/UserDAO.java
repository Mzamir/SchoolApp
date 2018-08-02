package com.example.mahmoudsamir.schoolappand.database_mangament.dao;

import android.content.Context;

import com.example.mahmoudsamir.schoolappand.database_mangament.RealmObject;
import com.example.mahmoudsamir.schoolappand.database_mangament.model.User;
import com.example.mahmoudsamir.schoolappand.network.response.UserResponseModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.model.UserProfileModel;

import io.realm.Realm;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.HELPER_USER_TYPE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.PARENT_USER_TYPE;

public class UserDAO {

    public void saveParent(Context context, UserResponseModel loginResponse) {
        Realm realm = RealmObject.getRealmObject(context);
        try {
            final User user = convertResponseToRealmObject(loginResponse);
            if (realm != null) {
                if (user != null) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealm(user);
                            realm.insertOrUpdate(user);
                        }
                    });
                }
            }
        } catch (Exception e) {

        } finally {
            if (realm != null) {
                realm.commitTransaction();
                realm.close();
            }
        }
    }

    private User convertResponseToRealmObject(UserResponseModel loginResponse) {
        User user = new User();

        user.setId(loginResponse.getId());
        user.setName(loginResponse.getName());
        user.setEmail(loginResponse.getEmail());
        user.setNational_id(loginResponse.getNational_id());
        user.setPhone(loginResponse.getPhone());
        user.setCreated_at(loginResponse.getCreated_at());
        user.setUpdated_at(loginResponse.getUpdated_at());
        user.setAuthy_code(loginResponse.getAuthy_code());
        user.setToken(loginResponse.getToken());
        if (loginResponse.getRoles().get(0).getName().equals("user")) {
            user.setRole(PARENT_USER_TYPE);
        } else if (loginResponse.getRoles().get(0).getName().equals("helper")) {
            user.setRole(HELPER_USER_TYPE);
        } else if (loginResponse.getRoles().get(0).getName().equals("mentor")) {
            user.setRole(HELPER_USER_TYPE);
        }
        return user;
    }

    private UserProfileModel convertRealmObjectToProfileObject(User user) {
        UserProfileModel profileModel = new UserProfileModel();

        profileModel.setId(user.getId());
        profileModel.setName(user.getName());
        profileModel.setEmail(user.getEmail());
        profileModel.setNational_id(user.getNational_id());
        profileModel.setPhone(user.getPhone());
        profileModel.setCreated_at(user.getCreated_at());
        profileModel.setUpdated_at(user.getUpdated_at());
        profileModel.setAuthy_code(user.getAuthy_code());
        profileModel.setToken(user.getToken());
        profileModel.setRole(user.getRole());
        return profileModel;
    }
}
