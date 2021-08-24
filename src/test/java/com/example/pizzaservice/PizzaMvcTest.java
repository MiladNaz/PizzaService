package com.example.pizzaservice;

import com.example.pizzaservice.controllers.PizzaController;
import com.example.pizzaservice.entities.Pizza;
import com.example.pizzaservice.repositories.PizzaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PizzaController.class)
public class PizzaMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PizzaRepository pizzaRepository;

    @Test
    void getAllPizzasExpectStatus200() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/pizzas"))
                .andExpect(status().is(200));
    }

    @Test
    void addPizzaAndReturnPizzaInHeader() throws Exception {
        Pizza pizza = new Pizza("testName", 100, "test ingredient");

        when(pizzaRepository.save(any(Pizza.class))).thenReturn(pizza);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/pizzas")
                        .content(objectMapper.writeValueAsString(pizza))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("testName")).andDo(print());
    }
    @Test
    void addPizzaAndExpectError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/pizzas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().is(400));
    }
}
