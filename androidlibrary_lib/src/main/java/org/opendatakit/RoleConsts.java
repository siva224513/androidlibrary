/*
 * Copyright (C) 2016 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.opendatakit.common.android.utilities.ODKFileUtils;

import java.util.ArrayList;

/**
 * @author mitchellsundt@gmail.com
 */
public class RoleConsts {

    /**
     * There are two additional roles available for permissions modelling:
     *
     * ROLE_SYNCHRONIZE_TABLES -- ability to Sync (users with this role also have ROLE_USER)
     * ROLE_SITE_ACCESS_ADMIN -- ability to create users on ODK Aggregate (also has all other roles)
     *
     */

    /**
     * Basic authenticated username or google account.
     *
     * If the user has the ODK 1.x Data viewer permission and no 2.0 permissions, they can have
     * this role without the ability to Sync to the server.
     *
     * But, commonly, these users have ROLE_SYNCHRONIZE_TABLES
     */
    public static final String ROLE_USER = "ROLE_USER";

    /**
     * Ability to Sync, to alter row-level filters, and edit rows even if locked or restricted
     */
    public static final String ROLE_SUPER_USER = "ROLE_SUPER_USER_TABLES";

    /**
     * Ability to Reset App Server.
     * Ability to Sync, to alter row-level filters, and edit rows even if locked or restricted
     *
     * Commonly, these users have ROLE_SITE_ACCESS_ADMIN
     */
    public static final String ROLE_ADMINISTRATOR = "ROLE_ADMINISTER_TABLES";

   /**
    * List of the roles the highest privileged user would have.
    */
   public static final String ADMIN_ROLES_LIST;

    static {
        ArrayList<String> adminRoleList = new ArrayList<>();
        adminRoleList.add(RoleConsts.ROLE_USER);
        adminRoleList.add(RoleConsts.ROLE_SUPER_USER);
        adminRoleList.add(RoleConsts.ROLE_ADMINISTRATOR);

        String value = null;
        try {
            value = ODKFileUtils.mapper.writeValueAsString(adminRoleList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ADMIN_ROLES_LIST = value;
    }

}
