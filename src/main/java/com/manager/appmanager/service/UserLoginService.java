//package com.manager.appmanager.service;
//
//import com.manager.appmanager.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//public class UserLoginService {
//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    public ServiceResponse login(User requestUser) {
//        UsernamePasswordAuthenticationToken authenticationTokenRequest = new
//                UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword());
//        try {
//            Authentication authentication = this.authenticationManager.authenticate(authenticationTokenRequest);
//            SecurityContext securityContext = SecurityContextHolder.getContext();
//            securityContext.setAuthentication(authentication);
//
//            User user = (User) authentication.getPrincipal();
//            //...
//
//        } catch (BadCredentialsException ex) {
//            // handle User not found exception
//            //...
//        }
//    }
//}
