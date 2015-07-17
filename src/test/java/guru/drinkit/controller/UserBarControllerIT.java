package guru.drinkit.controller;

import guru.drinkit.domain.BarItem;
import guru.drinkit.domain.User;
import guru.drinkit.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserBarControllerIT extends AbstractRestMockMvc {

    @Autowired
    UserRepository userRepository;

    private String testUserId;

    @Before
    public void setUp() throws Exception {
        User testUser = new User();
        testUser.setUsername("test user");
        userRepository.save(testUser);
        testUserId = testUser.getId();
    }

    @Test
    public void testAddNew() throws Exception {
        BarItem barItem = new BarItem();
        barItem.setIngredientId(1);
        mockMvc.perform(
                post("/users/" + testUserId + "/barItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(barItem))
        )
                .andExpect(status().isCreated());
        List<BarItem> barItems = userRepository.findOne(testUserId).getBarItems();
        assertThat(barItems.size(), is(1));
        assertThat(barItems.get(0).getIngredientId(), is(barItem.getIngredientId()));
        userRepository.removeBarItem(testUserId, barItem.getIngredientId());
    }

    @Test
    public void testGetBarItems() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {
        BarItem barItem = new BarItem();
        barItem.setIngredientId(1);
        barItem.setActive(true);
        userRepository.addBarItem(testUserId, barItem);
        barItem.setActive(false);
        mockMvc.perform(
                put("/users/" + testUserId + "/barItems/" + barItem.getIngredientId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(barItem))
        )
                .andExpect(status().isNoContent());
        List<BarItem> barItems = userRepository.findOne(testUserId).getBarItems();
        assertThat(barItems.size(), is(1));
        assertThat(barItems.get(0).isActive(), is(false));
    }

    @Test
    public void testRemove() throws Exception {
        BarItem barItem = new BarItem();
        barItem.setIngredientId(1);
        userRepository.addBarItem(testUserId, barItem);
        mockMvc.perform(
                delete("/users/" + testUserId + "/barItems/" + barItem.getIngredientId())
        )
                .andExpect(status().isNoContent());
        List<BarItem> barItems = userRepository.findOne(testUserId).getBarItems();
        assertThat(barItems.size(), is(0));
    }
}