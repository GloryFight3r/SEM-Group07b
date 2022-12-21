package pizzeria.order.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pizzeria.order.domain.coupon.Coupon;
import pizzeria.order.domain.coupon.CouponRepository;
import pizzeria.order.domain.coupon.PercentageCoupon;
import pizzeria.order.integration.utils.JsonUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient CouponRepository couponRepository;

    @Test
    public void createPercentageCouponSuccessfully() throws Exception {

        final String id = "COUPON";
        final double percentage = 0.2;

        PercentageCoupon percentageCoupon = new PercentageCoupon(id, percentage);
        ResultActions resultActions = mockMvc.perform(post("/coupon/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(percentageCoupon)));

        // assert that is was build correctly
        resultActions.andExpect(status().isCreated());

        //assert that the right coupon was saved
        PercentageCoupon newSavedCoupon = (PercentageCoupon) couponRepository.findById(id).orElseThrow();
        assertEquals(newSavedCoupon.getId(), id);
        assertEquals(newSavedCoupon.getPercentage(), percentage);
    }
}