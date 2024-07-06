package org.example.models;

/**
 * Created by yerin-158 on 7/6/24.
 *
 * @author yerin-158
 * @version 7/6/24.
 * @implNote First created
 */
public record Transaction(String sender, String receiver, float amount) {
  @Override
  public String toString() {
    return sender + " -> " + receiver + ": " + amount;
  }
}
