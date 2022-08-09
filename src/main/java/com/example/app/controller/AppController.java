package com.example.app.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.controller.dto.Response;
import com.example.app.service.AppService;
import com.example.app.simulator.Report;
import com.example.app.simulator.ReportGenerator;
import com.example.app.simulator.Simulator;
import com.example.app.simulator.SimulatorParams;

import lombok.extern.slf4j.Slf4j;

/** @author https://github.com/bipinthite */
@RestController
@RequestMapping("/v1")
@Slf4j
public class AppController {

  @Autowired private AppService appService;

  @GetMapping(
    path = "hello",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Response sayHello() {
    try {
      String message = appService.sayHello();
      return Response.builder().data(message).build();
    } catch (Exception e) {
      log.error("Error occurred while calling sayHello()", e);
      Response.Error error = Response.Error.builder().message(e.getMessage()).build();
      return Response.builder().errors(Collections.singletonList(error)).build();
    }
  }

  @GetMapping(
    path = "test",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Response test(SimulatorParams params) {
    ReportGenerator reportGenerator = ReportGenerator.getInstance();
    Simulator simulator = Simulator.getInstance();

    reportGenerator.clear();
    reportGenerator.setStart();
    simulator.start(params);
    reportGenerator.setEnd();

    Report r = reportGenerator.generate();

    return Response.builder()
      .data(r)
      .build();
  }
}
