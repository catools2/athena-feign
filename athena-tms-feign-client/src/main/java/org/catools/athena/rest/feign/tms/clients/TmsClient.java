package org.catools.athena.rest.feign.tms.clients;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;
import org.catools.athena.tms.model.*;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.catools.athena.rest.feign.common.utils.FeignUtils.getClient;

@Slf4j
@UtilityClass
@SuppressWarnings("unused")
public class TmsClient {

  private static final ItemClient ITEM_CLIENT = getClient(ItemClient.class, CoreConfigs.getAthenaHost());
  private static final ItemTypeClient ITEM_TYPE_CLIENT = getClient(ItemTypeClient.class, CoreConfigs.getAthenaHost());
  private static final StatusClient STATUS_CLIENT = getClient(StatusClient.class, CoreConfigs.getAthenaHost());
  private static final PriorityClient PRIORITY_CLIENT = getClient(PriorityClient.class, CoreConfigs.getAthenaHost());
  private static final SyncInfoClient SYNC_INFO_CLIENT = getClient(SyncInfoClient.class, CoreConfigs.getAthenaHost());
  private static final TestCycleClient TEST_CYCLE_CLIENT = getClient(TestCycleClient.class, CoreConfigs.getAthenaHost(), 10, 900);

  public static ItemTypeDto getItemType(ItemTypeDto itemTypeDto) {
    return Optional.ofNullable(ITEM_TYPE_CLIENT.getByCode(itemTypeDto.getCode())).orElseGet(() -> {
      ITEM_TYPE_CLIENT.saveOrUpdate(itemTypeDto);
      return ITEM_TYPE_CLIENT.getByCode(itemTypeDto.getCode());
    });
  }

  public static StatusDto getStatus(StatusDto statusDto) {
    return Optional.ofNullable(STATUS_CLIENT.getByCode(statusDto.getCode())).orElseGet(() -> {
      STATUS_CLIENT.saveOrUpdate(statusDto);
      return STATUS_CLIENT.getByCode(statusDto.getCode());
    });
  }

  public static PriorityDto getPriority(PriorityDto priorityDto) {
    return Optional.ofNullable(PRIORITY_CLIENT.getByCode(priorityDto.getCode())).orElseGet(() -> {
      PRIORITY_CLIENT.saveOrUpdate(priorityDto);
      return PRIORITY_CLIENT.getByCode(priorityDto.getCode());
    });
  }

  public static void saveItem(ItemDto itemDto) {
    ITEM_CLIENT.saveOrUpdate(itemDto);
  }

  public static void saveTestCycle(TestCycleDto testCycleDto) {
    TEST_CYCLE_CLIENT.saveOrUpdate(testCycleDto);
  }

  public static void saveSyncInfo(String projectCode, String action, String component, Instant startTime) {
    SYNC_INFO_CLIENT.saveOrUpdate(new SyncInfoDto(projectCode, action, component, startTime, Instant.now()));
  }

  public static Date getLastSync(String projectCode, String action, String component) {
    SyncInfoDto search = SYNC_INFO_CLIENT.search(action, component, projectCode);
    return search == null ? null : Date.from(search.getStartTime());
  }
}
