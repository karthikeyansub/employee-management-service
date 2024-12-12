package util;

import java.util.Base64;

public class EmployeeTestUtils {

    public static String basicAuthHeaderForUser() {
        return "Basic " + Base64.getEncoder().encodeToString("user:userpwd".getBytes());
    }

    public static String basicAuthHeaderForAdmin() {
        return "Basic " + Base64.getEncoder().encodeToString("admin:adminpwd".getBytes());
    }
}
