package br.com.autoinsight.autoinsight_client.exceptions;

public class BookingFoundException extends RuntimeException {
  public BookingFoundException() {
    super("You already have a booking with this vehicle at this hour!");
  }
}
