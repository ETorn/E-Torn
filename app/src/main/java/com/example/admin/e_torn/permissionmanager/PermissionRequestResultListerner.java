package com.example.admin.e_torn.permissionmanager;

import java.util.ArrayList;

/**
 * Created by gcqui on 28/05/2017.
 */
public interface PermissionRequestResultListerner {
    void onPermissionRequestDone(boolean successAll, ArrayList<String> grantedPermissions);
}
