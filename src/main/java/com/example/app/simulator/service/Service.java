package com.example.app.simulator.service;

import com.example.app.simulator.common.Task;
import com.example.app.simulator.queue.ServiceQueue;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
public class Service {

  private ServiceQueue queue;

  private static Service instance = new Service();

  private Service() {
    this.queue = ServiceQueue.getInstance();
  }

  public static Service getInstance() {
    return instance;
  }

  public Task process(Task task) {
    // log.info("request task [{}]", task.getId());

    queue.offerRequest(0, task);
    // task.request();

    Task response = null;
    try {
      response = queue.takeResponse(0);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return response;
  }

}
