package com.danolog.controller;

import com.danolog.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class PostController {

  @PostMapping("/posts")
//  public String post(@RequestParam String title, @RequestParam String content) {
//  public String post(@RequestParam Map<String, String> params) {
  public String post(@RequestBody PostCreate params) {
    log.info("params={}", params.toString());
    return "Hello world";
  }
}
