package recipes;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  UserService userDetailsService;
  RecipeAuthenticationProvider recipeAuthenticationProvider;

  PasswordEncoderService passwordEncoderService;

  //
  //  @Bean
  //  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  //    return http.httpBasic(Customizer.withDefaults())
  //        .csrf(CsrfConfigurer::disable)
  //        .headers(headers -> headers.frameOptions().disable()) // for Postman, the H2 console
  //        .authorizeHttpRequests(
  //            requests ->
  //                requests // manage access
  //                    .requestMatchers(HttpMethod.POST,"/api/register", "/actuator/shutdown" )
  //                    .permitAll()
  //                    .requestMatchers(HttpMethod.POST, "/api/recipe/new")
  //                    .authenticated()
  //                    .requestMatchers(HttpMethod.GET, "/api/recipe/search", "/api/recipe/{id}")
  //                    .authenticated()
  //                    .requestMatchers(HttpMethod.PUT, "/api/recipe/{id}")
  //                    .authenticated()
  //                    .requestMatchers(HttpMethod.DELETE, "/api/recipe/{id}")
  //                    .authenticated()
  //                    .anyRequest()
  //                    .denyAll())
  //        .build();
  //  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoderService.passwordEncoder())
        .and()
        .authenticationProvider(recipeAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic()
        .and()
        .csrf()
        .disable()
        .headers()
        .frameOptions()
        .disable()
        .and()
        .authorizeRequests()
        .antMatchers("/actuator/shutdown", "/api/register")
            .anonymous()
//        .permitAll()
        .anyRequest()
        .authenticated();
  }
}
