package com.example.app.simulator;

import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {

  private static ReportGenerator instance = new ReportGenerator();

  private ReportGenerator() { }

  public static ReportGenerator getInstance() {
    return instance;
  }

  private List<Long> elapsedTime = new ArrayList<>();
  private long startTime = 0;
  private long endTime = 0;

  public synchronized void addElapsedTime(long time) {
    elapsedTime.add(time);
  }

  public synchronized void clear() {
    elapsedTime.clear();
  }

  public void setStart() {
    this.startTime = System.nanoTime() / 1000000;
  }

  public void setEnd() {
    this.endTime = System.nanoTime() / 1000000;
  }

  public Report generate() {
    Report r = new Report();

    r.setTps(getTps());
    r.setAverage(getAverage());
    r.setMin(getMin());
    r.setMax(getMax());
    r.setPercentile(0);
    r.setElapsed(elapsedTime);

    return r;
  }

  private double getTps() {
    double elapsed = endTime - startTime;
    return (double)elapsedTime.size() / elapsed * 1000.0;
  }

  private double getAverage() {
    long accumulated = 0;
    for (Long e : elapsedTime) {
      accumulated += e;
    }

    return accumulated / elapsedTime.size();
  }

  private long getMin() {
    long min = 9999999;
    for (Long e : elapsedTime) {
      if (min > e) {
        min = e;
      }
    }
    return min;
  }

  private long getMax() {
    long max = 0;
    for (Long e : elapsedTime) {
      if (max < e) {
        max = e;
      }
    }
    return max;
  }

}
