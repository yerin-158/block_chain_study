package org.example.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by yerin-158 on 7/6/24.
 *
 * @author yerin-158
 * @version 7/6/24.
 * @implNote First created
 */
@Setter
@Getter
public class BlockBody {

  private List<Transaction> transactions;

  private BlockBody() {}

  public static BlockBody create(List<Transaction> transactions) {
    BlockBody blockBody = new BlockBody();
    blockBody.transactions = transactions;
    return blockBody;
  }

}
