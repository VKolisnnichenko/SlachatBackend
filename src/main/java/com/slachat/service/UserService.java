package com.slachat.service;

import com.slachat.entity.UserEntity;
import com.slachat.exceptions.UserAlreadyExistException;
import com.slachat.exceptions.UserNotFoundException;
import com.slachat.model.UserModel;
import com.slachat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
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

    public UserEntity findUserByUserName(String userName) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(userName);
        if (userEntity == null) throw new UserNotFoundException("User not found with username: " + userName);
        return userEntity;
    }

    public long deleteUser(long id) {
        userRepository.deleteById(id);
        return id;
    }
}
