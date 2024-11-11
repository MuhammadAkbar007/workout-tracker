package uz.akbar.workoutTracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** HomeController */
@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

  @GetMapping("/greet")
  public String greet() {
    return "Hello world!";
  }
}
