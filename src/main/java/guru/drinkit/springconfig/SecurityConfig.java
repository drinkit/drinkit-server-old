package guru.drinkit.springconfig;

import javax.annotation.Resource;

import guru.drinkit.security.CustomDigestAuthenticationEntryPoint;
import guru.drinkit.security.SimpleCORSFilter;
import guru.drinkit.service.impl.BasicUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
@EnableWebSecurity()
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    Environment environment;

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new BasicUserDetailsService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().disable()
                .httpBasic().authenticationEntryPoint(digestAuthenticationEntryPoint()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .antMatcher("/rest/**")
                .addFilterAfter(digestAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SimpleCORSFilter(), DigestAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/rest/admin/**").hasRole("ADMIN")
                .and().authorizeRequests().antMatchers("/rest/**").anonymous()
        ;
    }

    @Bean
    public DigestAuthenticationFilter digestAuthenticationFilter() {
        DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
        digestAuthenticationFilter.setAuthenticationEntryPoint(digestAuthenticationEntryPoint());
        digestAuthenticationFilter.setUserDetailsService(userDetailsService());
        return digestAuthenticationFilter;
    }

    @Bean
    public DigestAuthenticationEntryPoint digestAuthenticationEntryPoint() {
        DigestAuthenticationEntryPoint digestAuthenticationEntryPoint = new CustomDigestAuthenticationEntryPoint();
        digestAuthenticationEntryPoint.setRealmName("DrinkIt");
        digestAuthenticationEntryPoint.setKey(environment.getRequiredProperty("digest.key"));
        return digestAuthenticationEntryPoint;
    }

}
