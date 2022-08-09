package com.example.app.simulator.engine;

import java.util.concurrent.atomic.AtomicBoolean;

import com.example.app.simulator.common.Task;
import com.example.app.simulator.gpu.Gpu;
import com.example.app.simulator.gpu.GpuProvider;
import com.example.app.simulator.queue.ServiceQueue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Engine implements Runnable {

  private String id;
  private int hostId;
  private int gpuId;
  private ServiceQueue queue;
  private Gpu gpu;
  private final AtomicBoolean running = new AtomicBoolean(false);

  public Engine(String id, int host, int gpu) {
    this.id = id;
    this.hostId = host;
    this.gpuId = gpu;
    this.queue = ServiceQueue.getInstance();
    this.gpu = GpuProvider.getInstance().getGpu(host, gpu);
  }

  public void terminate() {
    log.info("[Engine {}] .. stopping", getId());
    running.set(false);
  }

  public String getId() {
    return String.format("%s-h%s-g%s", id, hostId, gpuId);
  }

  @Override
  public void run() {
    running.set(true);
    while (running.get()) {
      try {
        log.info("[Engine {}] .. waiting", getId());
        Task task = queue.pollRequest(0);
        if (task == null) {
          // polling method
          continue;
        }
        log.info("[Engine {}] >> request : Task [# {}]", getId(), task.getId());

        log.info("[Engine {}] .. processing", getId());
        this.gpu.process(task.getSentenceLength());

        log.info("[Engine {}] << response : Task [# {}]", getId(), task.getId());
        queue.offerResponse(0, task);
        // task.response();
      } catch (InterruptedException e) {
        // e.printStackTrace();
      }
    }
    log.info("[Engine {}] .. stopped", getId());
  }

}
