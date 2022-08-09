package com.example.app.simulator.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.example.app.simulator.common.Task;

public class ServiceQueue {
  private static ServiceQueue instance = new ServiceQueue();

  private List<LinkedBlockingQueue<Task>> requestQueues = new ArrayList<>();
  private List<LinkedBlockingQueue<Task>> responseQueues = new ArrayList<>();

  private ServiceQueue() {}

  public int currentRequestSize() {
    return (null == requestQueues) ? 0 : requestQueues.size();
  }

  public int currentResponseSize() {
    return (null == responseQueues) ? 0 : responseQueues.size();
  }

  public void initialize(int numOfQueue) {
    for (int i = 0; i < numOfQueue; i++) {
      requestQueues.add(new LinkedBlockingQueue<Task>());
      responseQueues.add(new LinkedBlockingQueue<Task>());
    }
  }

  public static ServiceQueue getInstance() {
    return instance;
  }

  public Task pollRequest(int index) throws InterruptedException {
    return requestQueues.get(index).poll(500, TimeUnit.MILLISECONDS);
  }

  public boolean offerRequest(int index, Task task) {
    boolean r = requestQueues.get(index).offer(task);
    task.request();
    return r;
  }

  public Task takeResponse(int index) throws InterruptedException {
    return responseQueues.get(index).take();
  }

  public boolean offerResponse(int index, Task task) {
    boolean r = responseQueues.get(index).offer(task);
    task.response();
    return r;
  }

}
