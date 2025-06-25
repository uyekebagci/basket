package com.umutbasket.berkyazici.service.security; // veya security paketin

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Token'ı imzalamak için kullanılacak gizli anahtar (application.properties'den okunacak)
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    // Token'ın geçerlilik süresi (milisaniye cinsinden, application.properties'den okunacak)
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Gelen token'dan kullanıcı adını (email) çıkartır.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Gelen token'dan belirli bir "claim"i (içerdiği bilgi parçası) çıkartır.
     * Bu, tüm claim çıkarma işlemlerinin temelini oluşturan genel bir metottur.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Bir kullanıcı için JWT üretir.
     */
    public String generateToken(UserDetails userDetails) {
        // YENİ: Roller için ekstra bir "claim" mapi oluşturuyoruz.
        Map<String, Object> extraClaims = new HashMap<>();
        // Roller, "authorities" adında bir listeye ekleniyor.
        extraClaims.put("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        return generateToken(extraClaims, userDetails);
    }

    /**
     * Ekstra "claim"ler ile birlikte bir kullanıcı için JWT üretir.
     * Token'ın "subject" alanı olarak kullanıcının adını (bizim için email),
     * başlangıç ve bitiş tarihlerini ayarlar ve token'ı imzalar.
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims) // Ekstra claim'leri (roller dahil) token'a ekle
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Token'ın geçerli olup olmadığını kontrol eder.
     * Kullanıcı adının doğru olup olmadığını ve token'ın süresinin dolup dolmadığını kontrol eder.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Token'ın süresinin dolup dolmadığını kontrol eder.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Token'dan son kullanma tarihini çıkartır.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Gelen token'ı ve gizli anahtarı kullanarak tüm "claim"leri ayrıştırır ve çıkartır.
     */
    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * application.properties'den okunan Base64 formatındaki gizli anahtarı,
     * JWT imzalama işlemi için uygun bir Key nesnesine dönüştürür.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}