package org.example.models;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockchainTest {

  @Test
  public void test() {
    int difficulty = 1;
    Blockchain blockchain = Blockchain.create(difficulty);

    List<Transaction> transactions1 = new ArrayList<>();
    transactions1.add(new Transaction("Alice", "Bob", 50));
    transactions1.add(new Transaction("Bob", "Charlie", 30));

    System.out.println("Mining block 1...");
    blockchain.addBlock(Block.create(1, new Date().getTime(), blockchain.getLatestBlock().getHash(), difficulty, transactions1));

    List<Transaction> transactions2 = new ArrayList<>();
    transactions2.add(new Transaction("Charlie", "Dave", 20));

    System.out.println("Mining block 2...");
    blockchain.addBlock(Block.create(2, new Date().getTime(), blockchain.getLatestBlock().getHash(), difficulty, transactions2));

    System.out.println("\nBlockchain is valid: " + blockchain.isChainValid());

  }

}
