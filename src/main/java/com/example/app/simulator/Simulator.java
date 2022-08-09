package com.example.app.simulator;

import java.util.ArrayList;
import java.util.List;

import com.example.app.simulator.common.Task;
import com.example.app.simulator.engine.Engine;
import com.example.app.simulator.gpu.GpuProvider;
import com.example.app.simulator.queue.ServiceQueue;
import com.example.app.simulator.user.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Simulator {

  private static Simulator instance = new Simulator();
  private static final String[] DEFINED_ENGINES = {
    "ko-en",
    "en-ko"
  };

  private ServiceQueue queue;

  private Simulator() {
    this.queue = ServiceQueue.getInstance();
  }

  public static Simulator getInstance() {
    return instance;
  }

  public void start(SimulatorParams params) {
    // Prepare required resources
    List<Thread> users = new ArrayList<>();
    List<Thread> engineThreads = new ArrayList<>();
    List<Engine> engines = new ArrayList<>();

    log.info("Host   : {}", params.getHost());
    log.info("GPU    : {}", params.getGpu());
    GpuProvider gpuProvider = GpuProvider.getInstance();
    gpuProvider.initialize(
      params.getHost(),
      params.getGpu(),
      params.isSupportParallel()
    );

    log.info("User   : {}", params.getUser());
    for (int i = 0; i < params.getUser() ; i++) {
      User user = new User(i + 1, 10);
      users.add(new Thread(user));
    }
    log.info("Engine : {}", params.getEngine());
    for (int h = 1; h <= params.getHost() ; h++) {
      for (int g = 1; g <= params.getGpu() ; g++) {
        for (int e = 0; e < params.getEngine() ; e++) {
          Engine engine = new Engine("ko-en", h, g);
          engineThreads.add(new Thread(engine));
          engines.add(engine);
        }
      }
    }
    Task.resetCounter();
    queue.initialize(1);

    users.forEach((user) -> {
      user.start();
    });
    engineThreads.forEach((engine) -> {
      engine.start();
    });

    // Wait for termination
    for (int i = 0; i < params.getUser() ; i++) {
      Thread th = users.get(i);
      synchronized(th) {
        try {
          while (th.isAlive()) {
            th.wait(100);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    // Terminate engines
    engines.forEach((engine) -> {
      engine.terminate();
    });
    for (int i = 0; i < params.getEngine() ; i++) {
      Thread th = engineThreads.get(i);
      synchronized(th) {
        try {
          while (th.isAlive()) {
            th.wait(100);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

}
