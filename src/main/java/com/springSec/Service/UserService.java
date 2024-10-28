package com.springSec.Service;

import com.springSec.Entity.AppUser;
import com.springSec.Payload.LoginDto;
import com.springSec.Payload.UserDto;
import com.springSec.Repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private JWTService jwtService;
    private ModelMapper modelMapper;
    private AppUserRepository appUserRepository;

    public UserService(AppUserRepository appUserRepository, JWTService jwtService, ModelMapper modelMapper, AppUserRepository appUserRepository1) {
        this.jwtService = jwtService;

        this.modelMapper = modelMapper;
        this.appUserRepository = appUserRepository1;
    }

    //Convert UserDto to AppUser Entity
    private AppUser mapToEntity(UserDto userDto){
        return modelMapper.map(userDto, AppUser.class);
    }
    //Convert AppUser to UserDto
    private UserDto mapToDto(AppUser appUser){
        return modelMapper.map(appUser, UserDto.class);
    }
    //Check for Duplicate User
    private void checkForDuplicateUser(UserDto userDto){
        Optional<AppUser> opUserName = appUserRepository.findByUsername(userDto.getUsername());
        if(opUserName.isPresent()) {
            throw new RuntimeException("Username already taken");
        }
        Optional<AppUser> opUserEmail = appUserRepository.findByEmail(userDto.getEmail());
        if(opUserEmail.isPresent()) {
            throw new RuntimeException("Email already taken");
        }
    }
    public UserDto createUser(UserDto userDto) {
        checkForDuplicateUser(userDto);
        AppUser appUser = mapToEntity(userDto);
        appUser.setPassword(BCrypt.hashpw(userDto.getPassword(),BCrypt.gensalt(5)));
        appUser.setRole("ROLE_USER");
        appUserRepository.save(appUser);


        return mapToDto(appUser);


    }
    public UserDto createPropertyOwnerUser(UserDto userDto) {
        checkForDuplicateUser(userDto);
        AppUser appUser = mapToEntity(userDto);
        appUser.setPassword(BCrypt.hashpw(userDto.getPassword(),BCrypt.gensalt(5)));
        appUser.setRole("ROLE_OWNER");
        appUserRepository.save(appUser);

        return mapToDto(appUser);
    }
    public String verifyLogin(LoginDto loginDto) {
        Optional<AppUser> opUser = appUserRepository.findByUsername(loginDto.getUsername());
        if(opUser.isPresent()){
            AppUser appUser = opUser.get();
          if(BCrypt.checkpw(loginDto.getPassword(),appUser.getPassword())){
              //generate token
              String token = jwtService.generateToken(appUser.getUsername());
              return token;
          }

        }else {
            return null;
        }

        return null;
    }

}
