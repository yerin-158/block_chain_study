package org.example.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
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
public class Blockchain {

  private ArrayList<Block> chain;
  private int difficulty;

  private Blockchain() {}

  public static Blockchain create(int difficulty) {
    Blockchain blockchain = new Blockchain();
    blockchain.difficulty = difficulty;
    blockchain.chain = new ArrayList<>();
    blockchain.chain.add(createGenesisBlock(difficulty));
    return blockchain;
  }

  // 체인의 최초 블록
  private static Block createGenesisBlock(int difficulty) {
    System.out.println("Mining genesis Block ... ");
    List<Transaction> genesisTransactions = new ArrayList<>();
    genesisTransactions.add(new Transaction("genesis", "genesis", 0));
    Block genesisBlock = Block.create(0, new Date().getTime(), "0", difficulty, genesisTransactions);
    // 최초 블록도 채굴해야 해시값 생김
    genesisBlock.mineBlock();
    return genesisBlock;
  }

  public Block getLatestBlock() {
    return chain.get(chain.size() - 1);
  }

  public void addBlock(Block newBlock) {
    Block latestBlock = getLatestBlock();

    // 1. 가장 최근에 연결된 블록(마지막 블록)의 해시값을 prevHash로 추가하여 연결함
    newBlock.setPreviousBlockHash(latestBlock.getHash());
    // 2. 새 블록에 Hash를 추가하도록 채굴
    newBlock.mineBlock();
    // 3. 채굴되면 체인에 추가
    chain.add(newBlock);
  }

  public boolean isChainValid() {
    for (int i = 1; i < chain.size() ; i++) {
      Block currentBlock = chain.get(i);
      Block previousBlock = chain.get(i - 1);

      // 해시값 오류
      if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
        return false;
      }

      // 체인 오류 (앞의 블록의 해시와 다름)
      if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
        return false;
      }

      // 루트 정보가 다름
      if (!currentBlock.getMerkleRoot().equals(Block.calculateMerkleRoot(currentBlock.getTransactions()))) {
        return false;
      }
    }
    return true;
  }
}
