package recipes;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;

  @Autowired PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return findByEmail(email);
  }

  public User findByEmail(String email) throws UsernameNotFoundException {
    if (userRepository.existsByEmail(email)) {
      return userRepository.findByEmail(email).get();
    }
    else {
      return null;
    }
//    throw new UsernameNotFoundException("Could not find any user with email " + email);
  }

  public Long register(AuthenticationRequest request) throws IllegalArgumentException {
    if (request.getEmail() != null
        && request.getPassword() != null
        && request.getPassword().length() >= 8
        && !request.getPassword().isBlank()
        && request.getEmail().matches("\\w+@\\w+\\.\\w+")) {
      request.setPassword(passwordEncoder.encode(request.getPassword()));
      return userRepository.save(new User(request.getEmail(), request.getPassword())).getId();
    }
    throw new IllegalArgumentException();
  }
}
