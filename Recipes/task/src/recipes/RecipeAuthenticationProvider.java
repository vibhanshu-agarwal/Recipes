package recipes;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
@Log4j2
public class RecipeAuthenticationProvider implements AuthenticationProvider {
  UserService userService;
  @Autowired PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) {
    log.info(
        "authentication: {"
            + authentication.getName()
            + ", "
            + authentication.getCredentials().toString()
            + "}");
    User user = userService.findByEmail(authentication.getName());
      if (passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
        log.info("authentication: confirmed");
        return new RecipeAuthentication(
            authentication.getName(), authentication.getCredentials().toString(), true);
      }
      log.info("authentication: failed");
      throw new AuthenticationCredentialsNotFoundException("CredentialIsNotFoundException");
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }
}
