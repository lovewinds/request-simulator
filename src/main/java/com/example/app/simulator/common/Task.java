package com.example.app.simulator.common;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class Task {

  private static AtomicInteger counter = new AtomicInteger(1);
  private int id;
  private int sentenceLength;
  private long startTime = 0;
  private long endTime = 0;

  public static void resetCounter() {
    counter = new AtomicInteger(1);
  }

  public Task(int sentenceLength) {
    this.id = counter.incrementAndGet();
    this.sentenceLength = sentenceLength;
    this.startTime = System.nanoTime();
  }

  public long getElaspedTime() {
    if (endTime <= startTime) {
      return 0;
    }

    return ((endTime - startTime) / 1000000);
  }

  public synchronized void request() {
    try {
      wait();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public synchronized void response() {
    notifyAll();
  }

}
