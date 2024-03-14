package org.catools.athena.rest.feign.tms.cache;

import org.catools.athena.rest.feign.common.cache.CacheStorage;
import org.catools.athena.rest.feign.tms.clients.TmsClient;
import org.catools.athena.tms.model.*;

public class TmsCache {
  private static final CacheStorage<String, StatusDto> STATUSES = new CacheStorage<>("Status", StatusDto::getCode, TmsClient::searchOrCreateStatus);
  private static final CacheStorage<String, PriorityDto> PRIORITIES = new CacheStorage<>("Priority",
      PriorityDto::getCode,
      TmsClient::searchOrCreatePriority);
  private static final CacheStorage<String, ItemTypeDto> ITEM_TYPES = new CacheStorage<>("ItemType",
      ItemTypeDto::getCode,
      TmsClient::searchOrCreateItemType);

  public static synchronized StatusDto readStatus(StatusDto status) {
    return STATUSES.read(status);
  }

  public static synchronized PriorityDto readPriority(PriorityDto priority) {
    return PRIORITIES.read(priority);
  }

  public static synchronized ItemTypeDto readType(ItemTypeDto type) {
    return ITEM_TYPES.read(type);
  }
}
