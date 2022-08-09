package com.example.app.simulator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimulatorParams {
  private int user = 2;
  private int engine = 2;
  private int host = 2;
  private int gpu = 4;
  private boolean supportParallel = false;
}
