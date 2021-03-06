package com.example.lostfound.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * @Author Zero
 * @Date 2021/7/17 15:57
 * @Since 1.8
 * @Description
 **/
@Component
public class JwtTokenUtil {
    private final static Clock CLOCK = DefaultClock.INSTANCE;

    @Value("${jwt.publicKey}")
    private String publicKey;

    @Value("${jwt.privateKey}")
    private String privateKey;

    /**
     * @author Zero
     * @date 2021/3/9 17:36
     * @description 生成jwt token
     */
    public String generateToken(Map<String, Object> claims, String subject, int expiration) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        // 生成签名密钥
        final Date createdDate = CLOCK.now();
        final Date expirationDate = calculateExpirationDate(createdDate, expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.RS256, key)
                .compact();
    }

    private Date calculateExpirationDate(Date createdDate, int expiration) {
        return new Date(createdDate.getTime() + expiration);
    }

    /**
     * 解密Jwt内容
     * @param jwt jwt
     * @return Claims
     */
    public Claims parseJwtRsa256(String jwt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
    }

    /**
     * 检验jwt是否过期
     * @param jwt
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public Boolean checkIsExpired(String jwt) {
        PublicKey key = null;
        try {
            key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        final Date expiration = Jwts.parser().setSigningKey(key)
                .parseClaimsJws(jwt).getBody().getExpiration();
        if(expiration.before(new Date())) {
            return true;
        }
        return false;
    }

    public String getUsername(String token) {
        Claims claims = null;
        try {
            claims = parseJwtRsa256(token);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        final String username = claims.getSubject();
        return username;
    }
}
