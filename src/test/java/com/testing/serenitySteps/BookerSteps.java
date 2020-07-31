package com.testing.serenitySteps;

import com.testing.requestBodies.BaseRequestBody;
import cucumber.api.DataTable;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;

import java.io.IOException;
import java.util.Map;

import static net.serenitybdd.core.Serenity.sessionVariableCalled;
import static net.serenitybdd.core.Serenity.setSessionVariable;

public class BookerSteps extends BaseSteps {
  private final static String _AUTH_ = "/auth/";
  private final static String _BOOKING_ = "/booking/";

  @Steps
  public BookerSteps bookerSteps;

  @Step
  public static void getToken(String username, String password) throws IOException {
    BaseRequestBody baseRequestBody = new BaseRequestBody();

    baseRequestBody.addKey("username", username);
    baseRequestBody.addKey("password", password);
    sendRequestWithBodyJson(POST, _AUTH_, baseRequestBody.getBody());
    assertStatusCode(200);
    saveValueInPathToSessionVariable("token", "token");
  }

  @Step
  public static void getBookingIds(){
    sendRequest(GET, _BOOKING_);
  }

  @Step
  public static void validateAmountOfBookingIds(int amount){
    Response response = Serenity.sessionVariableCalled(RESPONSE);
    Assert.assertEquals("Amount of Booking Ids", amount, response.jsonPath().getList("bookingid").size());
  }

  @Step
  public static void createBooking(DataTable dataTable) throws IOException {
    sendRequestWithBodyJson(POST, _BOOKING_, createBody(dataTable));
    Response response = Serenity.sessionVariableCalled(RESPONSE);
    Integer str = response.jsonPath().get("bookingid");
    setSessionVariable("bookingid").to(str);
  }

  @Step
  public static void getBooking(){
    sendRequest(GET, _BOOKING_ + sessionVariableCalled("bookingid"));
  }

  @Step
  public static void updateBooking(DataTable dataTable) throws IOException {
    sendRequestWithBodyJson(
            PUT,
            _BOOKING_ + sessionVariableCalled("bookingid"),
            createBody(dataTable));
  }

  @Step
  public static void partialUpdateBooking(DataTable dataTable) throws IOException {
    sendRequestWithBodyJson(
            PATCH,
            _BOOKING_ + sessionVariableCalled("bookingid"),
            createBody(dataTable));
  }

  @Step
  public static void deleteBooking() throws IOException {
    sendRequestWithBodyJson(
            DELETE,
            _BOOKING_ + sessionVariableCalled("bookingid"),
            "{}");
  }
}
