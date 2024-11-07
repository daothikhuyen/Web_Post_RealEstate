package com.example.backend.service;

import com.example.backend.dto.request.AuthenticationRequest;
import com.example.backend.dto.request.IntrospectRequest;
import com.example.backend.dto.request.LogoutRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.AuthenticationResponse;
import com.example.backend.dto.response.IntrospectResponse;
import com.example.backend.enity.User;
import com.example.backend.enums.InvalidatedToken;
import com.example.backend.exception.AppException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.reponsitory.InvalidatedTokenRepository;
import com.example.backend.reponsitory.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j // .log
public class AuthenticationService {

    @Autowired
    private UserRepository userRespository;
    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal // để ko inset vào contructor
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY ;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        }catch (AppException e){
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AuthenticationException {
        var user = userRespository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(),user.getPassword());

        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken());

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        }catch (AppException exception){
            log.info("Token already expired");
        }
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        // mã hoá bằng thuật toán char do hoá bằng char
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier); // kiểm tra xem token còn hạn sử dụng ko

        if(!(verified && expiryTime.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    // lưu dữ liệu người dùng sau khi Security thành công
    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);// Thuật toán

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("example.backend")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli() // hết sau 1 tiếng đồng hồ
                ))
                .jwtID(UUID.randomUUID().toString()) // tokenId radom khó trùng
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            System.out.println("Cannot created Token: "+ e);
            throw new RuntimeException(e);
        }
    }

//   liên quan đến oatho2
    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRole()))
            user.getRole().forEach(stringJoiner::add);

        return stringJoiner.toString();
    }
}
