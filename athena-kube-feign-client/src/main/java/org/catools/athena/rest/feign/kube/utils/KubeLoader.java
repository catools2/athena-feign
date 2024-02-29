package org.catools.athena.rest.feign.kube.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.kube.model.ContainerDto;
import org.catools.athena.kube.model.PodDto;
import org.catools.athena.rest.feign.kube.clients.KubeClient;

import java.time.Instant;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.catools.athena.rest.feign.common.utils.ThreadUtils.executeInParallel;

@Slf4j
@UtilityClass
public class KubeLoader {
  /**
   * Transfer KubePods from common.k8s package and load them to DB
   */
  public static void loadPods(Set<PodDto> pods, int totalParallelProcessors, long timeoutInMinutes) {
    setLastSyncValue(pods);
    log.info("Loading {} pods", pods.size());
    AtomicInteger counter = new AtomicInteger();
    Iterator<PodDto> podsToLoad = pods.iterator();
    executeInParallel(totalParallelProcessors, timeoutInMinutes, () -> {
      while (true) {
        PodDto pod = null;
        synchronized (podsToLoad) {
          if (!podsToLoad.hasNext()) {
            return true;
          }
          pod = podsToLoad.next();
        }
        log.info("Process pod {}/{} {} with {} containers", counter.getAndIncrement(), pods.size(), pod.getName(), pod.getContainers().size());
        KubeClient.savePod(pod);
      }
    });
    log.info("{} pods loaded.", pods.size());
  }

  private static void setLastSyncValue(Set<PodDto> pods) {
    for (PodDto pod : pods) {
      pod.setLastSync(Instant.now());
      for (ContainerDto container : pod.getContainers()) {
        container.setLastSync(Instant.now());
      }
    }
  }
}
