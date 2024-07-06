package org.example.models;

import java.security.MessageDigest;
import java.util.List;

/**
 * Created by yerin-158 on 7/6/24.
 *
 * @author yerin-158
 * @version 7/6/24.
 * @implNote First created
 */
public class Block {

  private BlockHeader header;
  private BlockBody body;

  private Block() {}

  public static Block create(int index, long timestamp, String previousHash, int difficulty, List<Transaction> transactions) {
    Block block = new Block();
    block.body = BlockBody.create(transactions);
    block.header = BlockHeader.create(index, timestamp, previousHash, difficulty, calculateMerkleRoot(transactions));

    return block;
  }

  /**
   * 간단한 머클루트 계산 - 머클 트리 구성하고 루트를 가져옴
   * ㄴ 실제 구현은 더 복잡함
   * @param transactions
   * @return
   */
  public static String calculateMerkleRoot(List<Transaction> transactions) {
    // 1. 초기화
    // 트랜잭션이 비어있다면 빈 문자열 반환
    if (transactions.isEmpty()) {
      return "";
    }

    // 비어있지 않으면 각 트랜잭션의 toString 값을 트리에 저장
    String[] merkleTree = new String[transactions.size()];
    // index 앞부분 일수록 먼저 일어난 작업
    for (int index = 0 ; index < merkleTree.length ; index++) {
      merkleTree[index] = transactions.get(index).toString();
    }

    // merkleTree의 길이가 1개이면 그게 루트임
    while (merkleTree.length > 1) {
      // 2. 부모 노드를 새로 만들기 위해 부모노드 개수 계산
      int parentTreeLength = (merkleTree.length + 1) / 2;
      String[] parentTree = new String[parentTreeLength];

      // 3. 부모노드는 자식 두 개를 기반으로 한 해시를 가진다.
      for (int index = 0 ; index < transactions.size() / 2 ; index++) {
        parentTree[index] = applySha256(merkleTree[2 * index] + merkleTree[2 * index + 1]);
      }

      // 4. 요소의 수가 홀수인 경우 마지막 요소는 parentTree에 그대로 복사한다.
      if (merkleTree.length % 2 == 1) {
        parentTree[parentTreeLength - 1] = merkleTree[merkleTree.length - 1];
      }

      // 5. merkleTree를 parentTree로 업데이트하고 루프를 계속 돌린다.
      merkleTree = parentTree;
    }

    // 6. 루트값 반환
    return merkleTree[0];
  }

  private static String applySha256(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
      StringBuilder builder = new StringBuilder();

      for (byte hashByte : hashBytes) {
        builder.append(String.format("%02x", hashByte));
      }

      return builder.toString();
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  /**
   * 블록 채굴
   */
  public void mineBlock() {
    // 1. 조건을 만족하는 해시 값의 목표 설정 : 해시의 첫자리 'header.getDifficulty()'개가 0이여야한다.
    String goal = new String(new char[this.header.getDifficulty()]).replace('\0', '0');

    // 2. 조건을 만족할 때 까지 반복
    String hash;
    do {
      // 3. 채굴자가 할 수 있는 것은 nonce 변경 밖에 없음. nonce를 1씩 증가시키면서 조건을 만족시킬때까지 해시값을 돌린다.
      this.header.increaseNonce();
      hash = calculateHash();
      System.out.println("Now Hash : " + hash);
    } while (!hash.substring(0, this.header.getDifficulty()).equals(goal));

    // 4. 조건을 만족하면(유효해시를 찾으면) 블록에 해시값을 저장한다.
    this.header.setHash(hash);
    System.out.println("Block Mined!! : " + header.getHash());
  }

  public void setPreviousBlockHash(String hash) {
    if (this.header == null) {
      throw new RuntimeException();
    }

    this.header.setPreviousHash(hash);
  }

  public String getHash() {
    if (this.header == null) {
      throw new RuntimeException();
    }

    return this.header.getHash();
  }

  public String getPreviousHash() {
    if (this.header == null) {
      throw new RuntimeException();
    }

    return this.header.getPreviousHash();
  }

  public String getMerkleRoot() {
    if (this.header == null) {
      throw new RuntimeException();
    }

    return this.header.getMerkleRoot();
  }

  public List<Transaction> getTransactions() {
    if (this.body == null) {
      throw new RuntimeException();
    }

    return this.body.getTransactions();
  }

  public String calculateHash() {
    String input = header.getIndex() + Long.toString(header.getTimestamp()) + header.getPreviousHash() + header.getMerkleRoot() + Integer.toString(header.getNonce());
    return Block.applySha256(input);
  }

}
