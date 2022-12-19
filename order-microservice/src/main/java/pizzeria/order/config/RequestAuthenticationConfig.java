package pizzeria.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * The type Web security config.
 */
@Configuration
public class RequestAuthenticationConfig extends WebSecurityConfigurerAdapter {

    public RequestAuthenticationConfig() {

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/order/place").authenticated()
                .antMatchers("/order/list").authenticated()
                .antMatchers("/order/delete").authenticated()
                .antMatchers("/order/edit").authenticated()
                .antMatchers("/order/listAll").hasRole("[ROLE_MANAGER]")
                .antMatchers("/coupon/create").hasRole("[ROLE_MANAGER]")
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
