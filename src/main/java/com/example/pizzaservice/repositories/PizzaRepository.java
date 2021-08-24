package com.example.pizzaservice.repositories;

import com.example.pizzaservice.entities.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    List<Pizza> findPizzaByIngredientsContains(String name);

    List<Pizza> findPizzaByName(String name);

    List<Pizza> findPizzaByPrice(int price);

}
