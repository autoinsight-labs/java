package br.com.autoinsight.autoinsight_client.exceptions;

public class UserVehicleFoundException extends RuntimeException {
  public UserVehicleFoundException() {
    super("User already has a vehicle registered!");
  }
}
