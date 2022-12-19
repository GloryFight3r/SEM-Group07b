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
        //here we validate that the user is authenticated and exists in the system
        //we also make the manager only endpoints visible to only the managers
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
