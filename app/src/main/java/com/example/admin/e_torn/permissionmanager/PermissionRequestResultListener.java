package com.example.admin.e_torn.permissionmanager;

import java.util.ArrayList;

/**
 * Created by gcqui on 30/05/2017.
 */
public interface PermissionRequestResultListener {
    void onPermissionRequestDone(boolean successAll, ArrayList<String> grantedPermissions);
}
