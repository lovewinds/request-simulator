package com.example.app.simulator;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Report {
  private double tps;
  private long min;
  private long max;
  private double average;
  private double percentile;
  private List<Long> elapsed;
}
