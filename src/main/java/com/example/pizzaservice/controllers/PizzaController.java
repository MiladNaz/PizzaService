package com.example.pizzaservice.controllers;

import com.example.pizzaservice.entities.Pizza;
import com.example.pizzaservice.repositories.PizzaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PizzaController {

    private final PizzaRepository pizzaRepository;


    public PizzaController(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @GetMapping("/pizzas")
    public List<Pizza> pizzas(){
        return pizzaRepository.findAll();
    }

    @PostMapping("/pizzas")
    ResponseEntity<Pizza> addAPizza(@RequestBody Pizza pizza){
        return new ResponseEntity<>(pizzaRepository.save(pizza), HttpStatus.CREATED);
    }

    @GetMapping("/pizzas/{name}")
    List<Pizza> searchByNameIngredientOrPrice(@PathVariable("name") String name) {
        HashSet<Pizza> pizzas = new HashSet<>();
        pizzas.addAll(pizzaRepository.findPizzaByName(name));
        pizzas.addAll(pizzaRepository.findPizzaByIngredientsContains(name));

        try {
            pizzas.addAll(pizzaRepository.findPizzaByPrice(Integer.parseInt(name)));
        } catch (NumberFormatException e) {

        }
        return new ArrayList<>(pizzas);
    }

    @PutMapping("/pizzas/{id}")
    Optional<Pizza> updatePizza(@RequestBody Pizza newPizza, @PathVariable Long id){
        return pizzaRepository.findById(id)
                .map(pizza -> {
                    pizza.setName(newPizza.getName());
                    pizza.setIngredients(newPizza.getIngredients());
                    pizza.setPrice(newPizza.getPrice());
                    return pizzaRepository.save(pizza);
                });
    }

    @PatchMapping("/pizzas/{id}")
    public Pizza updatePizzaPartially(@RequestBody Map<String, Object> changes, @PathVariable("id") Long id){

        Pizza pizza = pizzaRepository.findById(id).get();

        changes.forEach((key, value) -> {
            switch (key){
                case "name": pizza.setName((String) value);
                    break;
                case "price": pizza.setPrice((int) value);
                    break;
                case "ingredients": pizza.setIngredients((String) value);
                    break;
            }
        });
        return pizzaRepository.save(pizza);
    }

    @DeleteMapping("/pizzas/{id}")
    void deletePizza(@PathVariable Long id){
        pizzaRepository.deleteById(id);
    }
}
