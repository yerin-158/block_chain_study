package org.example.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by yerin-158 on 7/6/24.
 *
 * @author yerin-158
 * @version 7/6/24.
 * @implNote First created
 */
@Getter
@Setter
public class BlockHeader {

  private int index;
  private long timestamp;
  private String previousHash;
  private String hash;
  private int difficulty;
  private int nonce;
  private String merkleRoot;

  private BlockHeader() {}

  public static BlockHeader create(int index, long timestamp, String previousHash, int difficulty, String merkleRoot) {
    BlockHeader blockHeader = new BlockHeader();
    blockHeader.index = index;
    blockHeader.timestamp = timestamp;
    blockHeader.previousHash = previousHash;
    blockHeader.difficulty = difficulty;
    blockHeader.merkleRoot = merkleRoot;
    return blockHeader;
  }

  public void increaseNonce() {
    this.nonce++;
  }

}
