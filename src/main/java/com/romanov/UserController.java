package com.romanov;

import com.romanov.data.UserInfoRepository;
import com.romanov.domain.User;
import com.sun.org.apache.regexp.internal.RE;
import javassist.NotFoundException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserInfoRepository repository;

    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestParam(value = "email") String email,
                                     @RequestParam(value = "name") String firstname,
                                     @RequestParam(value = "surname") String secondname,
                                     @RequestParam(value = "dob") String dateofbirth,
                                     @RequestParam(value = "pass") String password) {
        try {
            User user = new User(email,
                    firstname,
                    secondname,
                    dateofbirth,
                    password);
            repository.save(user);
            return new ResponseEntity<>("User created! id = " + user.getId().toString(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error raised -> " + e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @RequestMapping(value = "/deluser", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@RequestParam(value = "id") Long id) {
        try {
            if(repository.findOne(id) == null) {
                return new ResponseEntity<>("No matching users found", HttpStatus.NOT_FOUND);
            }
            repository.deleteById(id);
            return new ResponseEntity<>("User with id = " + id + " was deleted", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error raised -> " + e.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/findbyEmail", method = RequestMethod.GET)
    public ResponseEntity<?> getByEmail(@RequestParam(value = "email") String email){
        try {
            List<User> user = repository.findByEmail(email);
            if (user.isEmpty()) {
                return new ResponseEntity<>("No matching users found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Users, that match the query: " + "\r\n" +
                    new JSONObject(user.toString()), HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error raised -> " + e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

}
