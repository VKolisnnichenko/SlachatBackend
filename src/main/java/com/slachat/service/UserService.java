package com.slachat.service;

import com.slachat.entity.ChatEntity;
import com.slachat.entity.TokenResponse;
import com.slachat.entity.UserChatDTO;
import com.slachat.entity.UserEntity;
import com.slachat.exceptions.InvalidCredentialsException;
import com.slachat.exceptions.TokenInvalidException;
import com.slachat.exceptions.UserAlreadyExistException;
import com.slachat.exceptions.UserNotFoundException;
import com.slachat.model.LoginDTO;
import com.slachat.model.UserModel;
import com.slachat.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Autowired
    private UserRepository userRepository;

    public UserEntity registration(UserEntity user) throws UserAlreadyExistException {
        if (userRepository.findByUsername(user.getUserName()) != null) {
            throw new UserAlreadyExistException("User already exist");
        }
        return userRepository.save(user);
    }

    public UserModel findUserById(long id) throws UserNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);
        if (userEntityOptional.isPresent()) {
            return UserModel.mapUser(userEntityOptional.get());
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    public UserEntity findUserByUserName(String userName, String authorizationHeader) throws UserNotFoundException, TokenInvalidException {
        String token = authorizationHeader.substring("Bearer ".length());
        if (validateAndDecodeToken(token)) {
            UserEntity userEntity = userRepository.findByUsername(userName);
            if (userEntity == null) throw new UserNotFoundException("User not found with username: " + userName);
            return userEntity;
        } else {
            throw new TokenInvalidException("Token invalid");
        }
    }

    public long deleteUser(long id) {
        userRepository.deleteById(id);
        return id;
    }

    public TokenResponse login(LoginDTO loginRequest) throws UserNotFoundException, InvalidCredentialsException {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        var user = userRepository.findByUsername(username);
        if (user == null) throw new UserNotFoundException("User not found with userName " + username);
        if (validateCredentials(user, password)) {
            String token = generateAuthToken(user.getId(), username);
            return new TokenResponse(token);
        } else {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    private boolean validateCredentials(UserEntity user, String password) {
        return user.getPassword().equals(password);
    }

    private boolean validateAndDecodeToken(String token) {
        try {
            // Parse and validate the token using the same secret key
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

            // If the above line doesn't throw an exception, the token is valid
            return true;
        } catch (Exception e) {
            // Token validation failed
            return false;
        }
    }


    private String generateAuthToken(long userId, String username) {
        Date expiration = new Date((System.currentTimeMillis() + 3600000) * 24); // Token expiration in 1 hour
        return Jwts.builder()
                .claim("userId", userId)
                .setSubject(username)
                .setExpiration(expiration)
                .signWith(SECRET_KEY) // Use the same secret key for signing
                .compact();
    }

    public UserModel getUserProfile(String authorizationHeader) throws TokenInvalidException, UserNotFoundException {
        String token = authorizationHeader.substring("Bearer ".length());
        if (validateAndDecodeToken(token)) {
            Claims claims = extractTokenClaims(token);
            Long userId = claims.get("userId", Long.class);
            try {
                return findUserById(userId);
            } catch (UserNotFoundException e) {
                throw new UserNotFoundException(e.getMessage());
            }
        } else {
            throw new TokenInvalidException("Token invalid");
        }
    }

    public List<UserChatDTO> getUserChats(String authorizationHeader) throws TokenInvalidException, UserNotFoundException {
        String token = authorizationHeader.substring("Bearer ".length());
        if (validateAndDecodeToken(token)) {
            Claims claims = extractTokenClaims(token);
            Long userId = claims.get("userId", Long.class);
            Optional<UserEntity> user = userRepository.findById(userId);
            if (user.isPresent()) {
                // Create a list of UserChatDTO objects
                List<UserChatDTO> userChats = new ArrayList<>();
                for (ChatEntity chat : user.get().getChats()) {

                    UserChatDTO userChatDTO = new UserChatDTO();
                    userChatDTO.setChatId(chat.getId());
                    for (UserEntity participant : chat.getParticipants()) {
                        if (!participant.getId().equals(userId)) {
                            userChatDTO.setUser(UserModel.mapUser(participant));
                            break;
                        }
                    }
                    userChats.add(userChatDTO);
                }

                return userChats;
            } else {
                throw new UserNotFoundException("User not found with id " + userId);
            }
        } else {
            throw new TokenInvalidException("Token invalid");
        }
    }


    private Claims extractTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
