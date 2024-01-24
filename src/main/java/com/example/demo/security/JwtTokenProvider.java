package com.example.demo.security;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${demo.quest-app.expires.in}")
    private Long EXPIRES_IN;



/*    @PostConstruct
    public void init() {
        // Anahtar boyutunu belirleyebilirsiniz, örneğin 512 bit
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        // Anahtarı BASE64 formatında alabilirsiniz
        APP_SECRET = Base64.getEncoder().encodeToString(key.getEncoded());
    }*/

    public String generateJwtToken(Authentication auth) {
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal(); //principal auth edeceğimiz useri getirir
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);

        return Jwts.builder().setSubject(Long.toString(userDetails.getId())) //userın id'sini getirir
                .setIssuedAt(new Date()).setExpiration(expireDate) //ne zaman oluştacağını belirler
                .signWith(key).compact(); //hangi algoritmayla oluşacağını seçer
    }

    public String generateJwtTokenByUserId(Long userId) {
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder().setSubject(Long.toString(userId))
                .setIssuedAt(new Date()).setExpiration(expireDate)
                .signWith(key).compact();
    }

    Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    } //tokeni çözmek için kullanılan method

    boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return !isTokenExpired(token); //expired olmadıysa true döner
        } catch (SignatureException e) {
            return false;
        } catch (MalformedJwtException e) {
            return false;
        } catch (ExpiredJwtException e) {
            System.out.println("Token süresi geçmiş!");
            return false;
        } catch (UnsupportedJwtException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    } //tokenin doğruluğunu, geçerliğini kontrol eden method

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    } // expired olup olmadığını kontrol eder
}
