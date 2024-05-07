package com.example.fooddeliveryapp.controllers;
import com.example.fooddeliveryapp.dtos.OrderDto;
import com.example.fooddeliveryapp.entities.*;
import com.example.fooddeliveryapp.exceptions.ResourceNotFoundException;
import com.example.fooddeliveryapp.models.OrderModel;
import com.example.fooddeliveryapp.repositories.RestaurantRepository;
import com.example.fooddeliveryapp.repositories.UserRepository;
import com.example.fooddeliveryapp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public OrderController(OrderService orderService,
                           RestaurantRepository restaurantRepository,
                           UserRepository userRepository) {
        this.orderService = orderService;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/getAllOrdersPageable")
    public ResponseEntity<Page<Order>> getAllOrdersPageable(@PageableDefault(size = 10, sort = "id") Pageable pageable) {

//        return orderService.findAllOrders();
        Page<Order> orders = orderService.findAllOrders(pageable);
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("List of Orders is empty.");
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<Order>> getAllOrders() {

//        return orderService.findAllOrders();
        List<Order> orders = orderService.findAllOrders();
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("List of Orders is empty.");
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getOrder/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
//        return orderService.findOrderById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
        Order order = orderService.findOrderById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot get Order with this id: " + id.toString() + "\n"));
        return ResponseEntity.ok(order);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto) {
        User user = userRepository.findById(orderDto.getUserId()).orElse(null);
        Restaurant restaurant = restaurantRepository.findById(orderDto.getRestaurantId()).orElse(null);

        if (user == null || restaurant == null) {
            return ResponseEntity.badRequest().body("User or Restaurant not found");
        }

        Order order = new Order();
        order.setPrice(orderDto.getPrice());
        order.setUser(user);
        order.setRestaurant(restaurant);

        Order savedOrder = orderService.saveOrder(order);
        OrderModel modelToShow = new OrderModel();
        modelToShow.setId(savedOrder.getId());
        modelToShow.setPrice(savedOrder.getPrice());
        modelToShow.setRestaurantId(restaurant.getId());
        modelToShow.setUserId(user.getId());
        return ResponseEntity.ok(modelToShow);
    }

    @PutMapping("/updateOrder/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
//        return orderService.findOrderById(id)
//                .map(existingOrder -> {
//                    updatedOrder.setId(id); // Ensure the ID is not changed
//                    return ResponseEntity.ok(orderService.saveOrder(updatedOrder));
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
        Order order = orderService.findOrderById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot update order with this id: " + id.toString() + "\n"));
        updatedOrder.setId(id);
        Order savedOrder = orderService.saveOrder(updatedOrder);
        return ResponseEntity.ok(savedOrder);
    }

    @DeleteMapping("/deleteOrder/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
//        return orderService.findOrderById(id)
//                .map(order -> {
//                    orderService.deleteOrder(id);
//                    return ResponseEntity.ok().build();
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
        Order order = orderService.findOrderById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete order with this id: " + id.toString() + "\n"));
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}
