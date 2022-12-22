package pizzeria.food.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pizzeria.food.authentication.AuthManager;
import pizzeria.food.authentication.JwtTokenVerifier;
import pizzeria.food.communication.HttpRequestService;
import pizzeria.food.domain.ingredient.IngredientRepository;
import pizzeria.food.domain.recipe.RecipeRepository;
import pizzeria.food.models.allergens.FilterMenuRequestModel;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockHttpRequestService", "mockTokenVerifier", "mockAuthenticationManager"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AllergensControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient RecipeRepository recipeRepository;

    @Autowired
    private transient IngredientRepository ingredientRepository;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient HttpRequestService httpRequestService;

    @Autowired
    private transient AuthManager mockAuthManager;

    @BeforeEach
    public void init() {
        when(mockAuthManager.getNetId()).thenReturn("ExampleUser");
        when(mockAuthManager.getRole()).thenReturn("[ROLE_MANAGER]");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("ExampleUser");
        when(mockJwtTokenVerifier.getRoleFromToken(anyString())).thenReturn(List.of(new SimpleGrantedAuthority("ROLE_MANAGER")));
    }

    @Test
    void filterMenu_worksCorrectly() {
        FilterMenuRequestModel requestModel = new FilterMenuRequestModel();
        List<String> allergens = List.of("pig", "dairy");
        requestModel.setAllergens(allergens);

        //make a test where this returns empty optional (unauthorized)
        when(httpRequestService.getUserAllergens(any())).thenReturn(Optional.of(allergens));

        //if you have a token (are authenticated) then you get the allergens back
        //else unauthorized

        //do not mock the repos
        //REPOS: add recipes
        //REPOS: add ingredients (with allergens)
        //if we have allergens (might be empty list even): first we go through the recipes
        //List<Recipe> findAll on recipe repo
        //then per recipe go through all ingredients (might not find ingredient in the repo, throw exception)
        //catch the exception in the controller

        //test cases: first one where everything works, no allergens
        //then everything works but we have allergens
        //then unauthorized
        //then things work but we have an illegal ingredient somewhere
    }
}
