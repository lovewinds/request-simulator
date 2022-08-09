package com.example.app.simulator.gpu;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GpuProvider {

  private Map<String, Gpu> gpuPool;

  private static GpuProvider instance = new GpuProvider();

  private GpuProvider() {
    this.gpuPool = null;
  }

  public static GpuProvider getInstance() {
    return instance;
  }

  private void clear() {
    if (this.gpuPool != null) {
      this.gpuPool.clear();
    }
    this.gpuPool = new HashMap<>();
  }

  public void initialize(int numHost, int numGpu, boolean supportParallel) {
    clear();
    for (int h = 1; h <= numHost; h++) {
      for (int g = 1; g <= numGpu; g++) {
        String uid = String.format("host%s-gpu%s", h, g);
        this.gpuPool.putIfAbsent(uid,
          new Gpu(
            String.format("%d", g),
            uid,
            supportParallel
          ));
      }
    }
  }

  public Gpu getGpu(int host, int gpu) {
    log.info("GpuProvider : {}", this.gpuPool.toString());
    String uid = String.format("host%s-gpu%s", host, gpu);
    Gpu gpuInstance = this.gpuPool.get(uid);
    log.info("GpuProvider : Providing [{}]", gpuInstance.toString());
    return gpuInstance;
  }

}
