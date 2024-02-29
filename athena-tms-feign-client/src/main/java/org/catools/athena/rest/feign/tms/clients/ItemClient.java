package org.catools.athena.rest.feign.tms.clients;

import feign.Headers;
import feign.RequestLine;
import org.catools.athena.tms.model.ItemDto;

interface ItemClient {

  @RequestLine("POST /tms/item")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(ItemDto item);

}
