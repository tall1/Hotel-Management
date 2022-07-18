package com.hotels.service;
import com.hotels.entity.User;
import com.hotels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @PostConstruct
    public void init453534(){
        // here put any after construction operations
        System.out.println("@PostConstruct");
    }
    @PreDestroy
    public void  exitAll123(){
        System.out.println("@PreDestroy");
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id)  {
        if(userRepository.findById(id).isPresent()){
            return userRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public void insertUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user){
        userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
