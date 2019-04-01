package org.mq.diff.service;

import org.mq.diff.domain.Side;
import org.mq.diff.dto.ContentDTO;
import org.mq.diff.dto.DiffEdDTO;

public interface DataManager {

  ContentDTO save(String diffId, DiffEdDTO diffEdDTO, Side side);
}
