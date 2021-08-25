package example.todolist.data.cache.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import org.springframework.data.annotation.Id;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Data
@RedisHash("session")
public class Session {
    private static final Base64.Encoder BASE_64_ENCODER = Base64.getEncoder();
    public static final Duration DEFAULT_SESSION_DURATION = Duration.ofHours(2);

    @Id
    private String token;

    @Indexed
    private String userAccountId;

    private LocalDateTime expiresOn;

    @TimeToLive
    public long getTimeToLive() {
        if (expiresOn == null) {
            return -1;
        }
        return Duration.between(LocalDateTime.now(), expiresOn).toMillis() / 1000L;
    }

    public static final String generateSessionToken(int length) {
        String output = "";
        while (output.length() < length) {
            output += BASE_64_ENCODER.encodeToString(UUID.randomUUID().toString().getBytes());
        }
        return output.substring(0, length);
    }

    public static final String generateSessionToken() {
        return generateSessionToken(32);
    }
}
