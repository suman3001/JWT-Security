package com.springSec;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class A {
    public static void main(String[] args) {
        System.out.println(BCrypt.hashpw("testing", BCrypt.gensalt(5)));
    }
}
