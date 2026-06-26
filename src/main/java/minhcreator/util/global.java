package minhcreator.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * global class for load App configuration and resources
 *
 * @author MinhCreatorVN
 * @version 1.0.1 Alpha test ver
 */
public class global {
    public static final String BANKCODE = "BIDV"; // replace your Bank code
    public static final String ACCOUNTNUMBER = "8845777432";
    public static final String ACCOUNTNAME = "WareHouse system";
    public static final String ADDINFO = "Warehouse Payment";
    public static Map<String, String> Config(String PathContentDir, String name_configFile) {
        Map<String, String> args = new HashMap<>();
        Properties props = new Properties();
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(PathContentDir + "/" + name_configFile)) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String key : props.stringPropertyNames()) {
            args.put(key, props.getProperty(key));
        }
        return args;
    }

    public static String getPathResource(String ContentDir, String name_resource) {
        var url = Thread.currentThread().getContextClassLoader().getResource(ContentDir + "/" + name_resource);
        return url != null ? url.getPath() : null;
    }

    // PreTesting functional
    static void main(String[] args) {
//        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
    }
}