package br.com.autoinsight.autoinsight_client.exceptions;

public class PlateFoundException extends RuntimeException {
  public PlateFoundException() {
    super("Vehicle license plate already registered!");
  }
}
