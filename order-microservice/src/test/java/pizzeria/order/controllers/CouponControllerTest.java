package pizzeria.order.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pizzeria.order.authentication.AuthManager;
import pizzeria.order.authentication.JwtTokenVerifier;
import pizzeria.order.domain.coupon.CouponRepository;
import pizzeria.order.integration.utils.JsonUtil;
import pizzeria.order.models.CouponModel;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"mockAuthenticationManager", "mockTokenVerifier"})
@AutoConfigureMockMvc
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient CouponRepository couponRepository;

    @Autowired
    private transient AuthManager mockAuthManager;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @BeforeEach
    public void init() {
        when(mockAuthManager.getNetId()).thenReturn("ExampleUser");
        when(mockAuthManager.getRole()).thenReturn("ROLE_MANAGER");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("ExampleUser");
        when(mockJwtTokenVerifier.getRoleFromToken(anyString())).thenReturn(List.of(new SimpleGrantedAuthority("ROLE_MANAGER")));
    }

    @Test
    public void createPercentageCouponSuccessfully() throws Exception {

        final String id = "COUPON";
        final double percentage = 0.2;
        final String type = "PERCENTAGE";

        CouponModel couponModel = new CouponModel();
        couponModel.setPercentage(percentage);
        couponModel.setId(id);
        couponModel.setType(type);
        ResultActions resultActions = mockMvc.perform(post("/coupon/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(couponModel))
                .header("Authorization", "Bearer MockedToken"));


        // assert that it was build correctly
        resultActions.andExpect(status().isCreated());

        //assert that the right coupon was saved
//        PercentageCoupon newSavedCoupon = (PercentageCoupon) couponRepository.findById(id).orElseThrow();
//        assertEquals(newSavedCoupon.getId(), id);
//        assertEquals(newSavedCoupon.getPercentage(), percentage);
    }
}