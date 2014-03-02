package fr.xebia.xebay.domain.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class Gravatar {
    private static final Logger log = LoggerFactory.getLogger("Gravatar");


    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    public static String md5Hex (String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            String hash = hex(md.digest(message.getBytes("UTF-8")));
            log.debug("hash " + hash);
            return hash;
        } catch (Exception e) {
            return null;
        }
    }
}
