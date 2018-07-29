package com.example.mahmoudsamir.schoolappand.database_mangament.dao;

import android.content.Context;

import com.example.mahmoudsamir.schoolappand.database_mangament.model.Parent;
import com.example.mahmoudsamir.schoolappand.database_mangament.RealmObject;
import com.example.mahmoudsamir.schoolappand.network.response.LoginResponse;

import io.realm.Realm;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.HELPER_USER_TYPE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.PARENT_USER_TYPE;

public class ParentDAO {

    public void saveParent(Context context, LoginResponse loginResponse) {
        Realm realm = RealmObject.getRealmObject(context);
        try {
            final Parent parent = convertResponseToRealmObject(loginResponse);
            if (realm != null) {
                if (parent != null) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealm(parent);
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

    private Parent convertResponseToRealmObject(LoginResponse loginResponse) {
        Parent parent = new Parent();

        parent.setId(loginResponse.getId());
        parent.setName(loginResponse.getName());
        parent.setEmail(loginResponse.getEmail());
        parent.setNational_id(loginResponse.getNational_id());
        parent.setPhone(loginResponse.getPhone());
        parent.setCreated_at(loginResponse.getCreated_at());
        parent.setUpdated_at(loginResponse.getUpdated_at());
        parent.setAuthy_code(loginResponse.getAuthy_code());
        parent.setToken(loginResponse.getToken());
        if (loginResponse.getRoles().get(0).getName().equals("parent")) {
            parent.setRole(PARENT_USER_TYPE);
        } else if (loginResponse.getRoles().get(0).getName().equals("helper")) {
            parent.setRole(HELPER_USER_TYPE);
        }
        return parent;
    }
//    private Parent convertRealmObjectToResponse(Parent parent) {
//        Parent parent = new Parent();
//
//        parent.setId(loginResponse.getId());
//        parent.setName(loginResponse.getName());
//        parent.setEmail(loginResponse.getEmail());
//        parent.setNational_id(loginResponse.getNational_id());
//        parent.setPhone(loginResponse.getPhone());
//        parent.setCreated_at(loginResponse.getCreated_at());
//        parent.setUpdated_at(loginResponse.getUpdated_at());
//        parent.setAuthy_code(loginResponse.getAuthy_code());
//        parent.setToken(loginResponse.getToken());
//        if (loginResponse.getRoles().get(0).getName().equals("parent")) {
//            parent.setRole(PARENT_USER_TYPE);
//        } else if (loginResponse.getRoles().get(0).getName().equals("helper")) {
//            parent.setRole(HELPER_USER_TYPE);
//        }
//        return parent;
//    }
}
