//package pizzeria.order.controllers;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import pizzeria.order.authentication.AuthManager;
//import pizzeria.order.authentication.JwtTokenVerifier;
//import pizzeria.order.domain.food.Food;
//import pizzeria.order.domain.mailing.MailingService;
//import pizzeria.order.domain.order.Order;
//import pizzeria.order.domain.order.OrderService;
//import pizzeria.order.domain.store.StoreService;
//import pizzeria.order.integration.utils.JsonUtil;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@ActiveProfiles({"mockAuthenticationManager", "mockTokenVerifier",
//        "mockFood", "mockOrderService", "mockMailingService", "mockStoreService"})
//@AutoConfigureMockMvc
//class OrderControllerTest {
//
//    @Autowired
//    private transient MockMvc mockMvc;
//
//    @Autowired
//    private transient AuthManager mockAuthManager;
//
//    @Autowired
//    private transient Food mockFood;
//
//
//    @Autowired
//    private transient JwtTokenVerifier mockJwtTokenVerifier;
//
//    @Autowired
//    private transient OrderService mockOrderService;
//
//    @Autowired
//    private transient MailingService mockMailingService;
//
//    @Autowired
//    private transient StoreService mockStoreService;
//
//    @Test
//    public void placeOrder() throws Exception{
//
//        final String recipientEmail = "testUser@gmail.com";
//        final Long orderId = 3L;
////        Order order = new Order(orderId, List.of(mockFood), 1L, "userTest",
////                , 32.0, List.of());
////
////        when(mockAuthManager.getNetId()).thenReturn("userTest");
////        when(mockAuthManager.getRole()).thenReturn("ROLE_CUSTOMER");
////        when(mockJwtTokenVerifier.getRoleFromToken(anyString()))
////                .thenReturn(Collections.singleton(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
////        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
////        when(mockOrderService.processOrder(order)).thenReturn(order);
////        when(mockStoreService.getEmailById(anyLong())).thenReturn(recipientEmail);
//////        doNothing().when(mockMailingService.sendEmail(orderId, recipientEmail, MailingService.ProcessType.CREATED));
////
////        ResultActions resultActions = mockMvc.perform(post("/order/place")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(JsonUtil.serialize(order))
////                .header("Autorization", "Bearer MockedToken"));
////
////
////        resultActions.andExpect(status().isCreated());
//    }
//
//    @Test
//    void editOrder() {
//    }
//
//    @Test
//    void deleteOrder() {
//    }
//
//    @Test
//    void listOrders() {
//    }
//
//    @Test
//    void listAllOrders() {
//    }
//}