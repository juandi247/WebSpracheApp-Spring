package com.sprache.juandiegodeutsch.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    //Key (unprotected just for the solution)
    private static final String SECRET_KEY="3daXviAMN0te21E0b/E2nxjFW7DLatjLQJtHJYOv428CoIs72Yd8hKeVYCb0+nFyT0R9akKVJr+pwkB3oQknBw==";


    // generates a JWT token for the user
    public String getToken(UserDetails user){
        return getToken(new HashMap<>(), user);

    }


    // generates a JWT with extra claims (custom data)
    private String getToken(Map<String,Object> extraClaims, UserDetails user){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

    }





    //Decodifiing the key from the user
    private Key getKey(){
        byte[] keybytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keybytes);
    }


    //get username from token
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }


    //validation for the token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }



    // Retrieves all claims (data) from the token

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    // Retrieves a specific claim from the token using a resolver function
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token); // Retrieves claims
        return claimsResolver.apply(claims); // Applies the resolver function
    }



    // Gets the expiration date of the token
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration); // Retrieves the expiration claim
    }



    // Checks if the token has expired
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date()); // Compares expiration date with current date
    }


}
