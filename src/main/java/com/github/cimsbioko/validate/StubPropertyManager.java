package com.github.cimsbioko.validate;

import org.javarosa.core.services.IPropertyManager;
import org.javarosa.core.services.properties.IPropertyRules;

import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;

public class StubPropertyManager implements IPropertyManager {

    private final static String DEVICE_ID_PROPERTY = "deviceid";
    private final static String SUBSCRIBER_ID_PROPERTY = "subscriberid";
    private final static String SIM_SERIAL_PROPERTY = "simserial";
    private final static String PHONE_NUMBER_PROPERTY = "phonenumber";
    private final static String USERNAME = "username";
    private final static String EMAIL = "email";

    private final static String OR_DEVICE_ID_PROPERTY = "uri:deviceid";
    private final static String OR_SUBSCRIBER_ID_PROPERTY = "uri:subscriberid";
    private final static String OR_SIM_SERIAL_PROPERTY = "uri:simserial";
    private final static String OR_PHONE_NUMBER_PROPERTY = "uri:phonenumber";
    private final static String OR_USERNAME = "uri:username";
    private final static String OR_EMAIL = "uri:email";

    private static HashSet<String> PROPERTY_NAMES;

    static {
        PROPERTY_NAMES = new HashSet<>(asList(
                DEVICE_ID_PROPERTY,
                SUBSCRIBER_ID_PROPERTY,
                SIM_SERIAL_PROPERTY,
                PHONE_NUMBER_PROPERTY,
                USERNAME,
                EMAIL,
                OR_DEVICE_ID_PROPERTY,
                OR_SUBSCRIBER_ID_PROPERTY,
                OR_SIM_SERIAL_PROPERTY,
                OR_PHONE_NUMBER_PROPERTY,
                OR_USERNAME,
                OR_EMAIL));
    }

    public void addRules(IPropertyRules rules) {
    }

    public List<IPropertyRules> getRules() {
        return null;
    }

    public List<String> getProperty(String propName) {
        return null;
    }

    public String getSingularProperty(String propName) {
        if (PROPERTY_NAMES.contains(propName)) {
            return "found";
        } else if (propName == null || propName.length() == 0) {
            return "notfound";
        } else {
            throw new IllegalArgumentException("Unrecognized property name: " + propName);
        }
    }

    public void setProperty(String propName, String propValue) {
    }

    public void setProperty(String propName, List<String> propValue) {
    }

}
