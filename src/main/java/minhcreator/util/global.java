package minhcreator.util;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

/**
 *
 * @author MinhCreatorVN
 */
public class global {
    /**
     * @param PathContentDir  : Path Dir of assets or resources
     * @param name_configFile : Name of resource or file and file extension just like properties file format
     * @return Dictionary : Path of App configuration file
     * @version 1.0.1 Alpha test ver
     * @author MinhCreator
     * load App configuration
     */
    public static Dictionary<String, String> Config(String PathContentDir, String name_configFile) {
        String root_dir_pwd = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfig = root_dir_pwd + PathContentDir + "/" + name_configFile;

        Dictionary<String, String> args = new Hashtable<>();

        Properties props = new Properties();
        try {
            props.load(new java.io.FileInputStream(appConfig));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // add properties to dict
        for (String key : props.stringPropertyNames()) {
            args.put(key, props.getProperty(key));
        }

        return args;
    }

    /**
     * @param ContentDir    : Path Dir of assets or resources
     * @param name_resource : Name of resource or file and file extension
     * @return String : Path of resources
     * @version 1.0.1 Alpha test ver
     * @author MinhCreator
     * load content and assets resources
     */
    public static String getPathResource(String ContentDir, String name_resource) {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath() + ContentDir + "/"
                + name_resource;
    }

    // PreTesting functional
    static void main(String[] args) {
//        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
    }
}