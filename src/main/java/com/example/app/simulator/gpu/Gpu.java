package com.example.app.simulator.gpu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public class Gpu {

  private String id;
  private String host;
  private Boolean supportParallel = false;

  private void _process(boolean parallel, int time) {
    try {
      log.info("[Host {}][GPU {}] >> grab    : {} // parallel : {}",
        host, id, this.toString(), parallel);
      Thread.sleep(time);
      log.info("[Host {}][GPU {}] << release : {} // parallel : {}",
        host, id, this.toString(), parallel);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void process(int time) {
    if (this.supportParallel) {
      // Mimic MPS parallel processing
      _process(true, time);
    } else {
      // Default blocking operation
      synchronized(this) {
        _process(false, time);
      }
    }
  }
}
