package example.todolist.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.slf4j.Logger;

public final class PasswordUtil {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(PasswordUtil.class);

    private PasswordUtil() {}

    public static final String hash(String plainText) {
        if (plainText == null) {
            return null;
        }

        try {
            return BCrypt.withDefaults().hashToString(12, plainText.toCharArray());
        } catch (Exception e) {
            LOG.error("Unable to generate hash for password of length {}", plainText.length(), e);
        }
        return null;
    }

    public static boolean matches(String plainText, String hashString) {
        if (plainText != null && hashString != null) {
            try {
                BCrypt.Result result = BCrypt.verifyer().verify(plainText.toCharArray(), hashString);
                return result.verified;
            } catch (Exception e) {
                LOG.error("Unable to verifiy hash for password of length: {}, hashString of length: {}",
                        plainText.length(), hashString.length(), e);
            }
        }
        return false;
    }
}
