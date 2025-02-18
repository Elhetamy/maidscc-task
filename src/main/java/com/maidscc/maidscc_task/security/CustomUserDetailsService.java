package com.maidscc.maidscc_task.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return new User("admin", "$2y$10$6YtjKLaXYOw5Sd4fqkr3KOK0RlrMwWz/G9ZArc6YmDPf38G06khci", new ArrayList<>()); // Dummy user
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
