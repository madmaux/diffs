package org.mq.diff.service;

import org.mq.diff.dto.DiffResultDTO;

public interface DataComparator {

  DiffResultDTO compare(String id);
}
