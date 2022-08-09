package com.example.app.simulator.user;

import java.util.concurrent.CompletableFuture;

import com.example.app.simulator.ReportGenerator;
import com.example.app.simulator.common.Task;
import com.example.app.simulator.service.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class User implements Runnable {

  private int id;
  private int numRepeat;
  private int repeatCount;
  private Service service;
  private ReportGenerator report;

  public User(int id, int numRepeat) {
    this.id = id;
    this.numRepeat = numRepeat;
    this.repeatCount = 1;
    this.service = Service.getInstance();
    this.report = ReportGenerator.getInstance();
  }

  private void request() {
    int length = 100;
    Task userTask = new Task(length);
    log.info("[User {}] >> request  [{}/{}] : Task [# {}]",
     id, this.repeatCount, this.numRepeat,
     userTask.getId()
    );

    CompletableFuture<Task> cf = CompletableFuture.supplyAsync(() -> {
      Task result = service.process(userTask);
      result.setEndTime(System.nanoTime());

      return result;
    });

    try {
      Task result = cf.get();
      log.info("[User {}] << response [{}/{}] : Task [# {} / {} ms]",
        id, this.repeatCount, this.numRepeat,
        result.getId(), result.getElaspedTime()
      );

      report.addElapsedTime(result.getElaspedTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    synchronized(this) {
      this.repeatCount = 1;
      while (this.repeatCount <= this.numRepeat) {
        request();
        this.repeatCount++;
      }
      notify();
    }
  }

}
