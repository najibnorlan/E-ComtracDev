/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssm.controller.token;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Syukri.Mokhtar
 */
public class MB_ErrorCode {

    public static Integer E_200 = 200;
    public static Integer E_400 = 400;
    public static Integer E_401 = 401;
    public static Integer E_403 = 403;
    public static Integer E_405 = 405;
    public static Integer E_503 = 503;
    public static Integer E_406 = 406;

    private static Map<Integer, String> description = null;

    public static String description(Integer errorCode) {
        if (description == null) {
            description = new HashMap();
            description.put(E_200, "Token Verifed");
            description.put(E_400, "Token Invalid");
            description.put(E_401, "Token Expired");
            description.put(E_403, "Authentication Server down");
            description.put(E_405, "Authentication Server return false");
            description.put(E_503, "Service Unavaiable");
            description.put(E_406, "Authentication Token required");
        }
        return description.get(errorCode);
    }

}
