package com.springSec.Controller;

import com.springSec.Payload.LoginDto;
import com.springSec.Payload.TokenDto;
import com.springSec.Payload.UserDto;
import com.springSec.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secapi/v1/users")
public class UserContoller {
    private UserService userService;

    public UserContoller(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto){
        try {
            UserDto savedUser = userService.createUser(userDto);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("/signup-propertyowner")
    public ResponseEntity<?> createPropertyOwnerUser(@RequestBody UserDto userDto){
        try {
            UserDto savedUser = userService.createPropertyOwnerUser(userDto);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginDto loginDto){
        String token = userService.verifyLogin(loginDto);
        if (token!=null){
            TokenDto tokenDto= new TokenDto();
            tokenDto.setToken(token);
            tokenDto.setType("JWT");
            return new ResponseEntity<>(tokenDto,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("invalid username/password",HttpStatus.FORBIDDEN);
        }

    }

}
