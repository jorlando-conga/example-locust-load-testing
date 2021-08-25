package example.todolist.util;

import javax.servlet.http.Cookie;

public final class SessionCookieUtil {
    public static final String SESSION_COOKIE_NAME = "TODOLIST_SID";

    private SessionCookieUtil() {}

    public static Cookie buildSessionCookie(String sessionToken, int cookieExpirySeconds) {
        if (sessionToken == null) {
            return buildLogoutCookie();
        }
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionToken);
        cookie.setMaxAge(cookieExpirySeconds);
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie buildLogoutCookie() {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
