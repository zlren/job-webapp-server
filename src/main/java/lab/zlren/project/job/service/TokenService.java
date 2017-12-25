package lab.zlren.project.job.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lab.zlren.project.job.common.bean.Identity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zlren
 * @date 2017-12-25
 */
@Configuration
@ConfigurationProperties("token")
@Service
@Data
@Slf4j
public class TokenService {

    private String issuer;
    private Long duration;
    private String apiKeySecret;

    /**
     * 生成token，使用了identity的id、user和type字段
     *
     * @param identity
     * @return
     */
    public String createToken(Identity identity) {

        // The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKeySecret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String, Object> claims = new HashMap<>(3);
        claims.put("id", identity.getId());
        claims.put("user", identity.getUser());
        claims.put("type", identity.getType());

        // Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(String.valueOf(identity.getId()))
                .setIssuedAt(now)
                .setIssuer(this.getIssuer())
                .signWith(signatureAlgorithm, signingKey);

        // if it has been specified, let's add the expiration
        // long ttlMillis = identity.getDuration();
        // if (ttlMillis >= 0) {
        long expMillis = nowMillis + duration;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);
        identity.setDuration(exp.getTime());
        // }

        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public Identity parseToken(String token) {

        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(apiKeySecret))
                .parseClaimsJws(token).getBody();

        Integer id = (Integer) claims.get("id");
        String user = (String) claims.get("user");
        String type = (String) claims.get("type");

        // 封装成pojo
        Identity identity = new Identity();
        identity.setId(id);
        identity.setUser(user);
        identity.setType(type);
        identity.setDuration(claims.getExpiration().getTime());

        log.info("已登录的用户，有效token");

        return identity;
    }
}
