package org.upgrad.upstac.testrequests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.testrequests.consultation.Consultation;
import org.upgrad.upstac.testrequests.consultation.ConsultationController;
import org.upgrad.upstac.testrequests.consultation.CreateConsultationRequest;
import org.upgrad.upstac.testrequests.consultation.DoctorSuggestion;
import org.upgrad.upstac.testrequests.lab.CreateLabResult;
import org.upgrad.upstac.testrequests.lab.LabRequestController;
import org.upgrad.upstac.testrequests.lab.LabResult;
import org.upgrad.upstac.testrequests.lab.TestStatus;
import org.upgrad.upstac.users.User;
import org.upgrad.upstac.users.models.Gender;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Slf4j
class ConsultationControllerTest {


    @InjectMocks
    ConsultationController consultationController;


    @Mock
    TestRequestQueryService testRequestQueryService;

    @Mock
    UserLoggedInService userLoggedInService;

    @Mock
    LabRequestController labRequestController;

    @Mock
    TestRequestUpdateService testRequestUpdateService;

    @Test
    @WithUserDetails(value = "doctor")
    public void calling_assignForConsultation_with_valid_test_request_id_should_update_the_request_status(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);

        //Implement this method

        //Create another object of the TestRequest method and explicitly assign this object for Consultation using assignForConsultation() method
        // from consultationController class. Pass the request id of testRequest object.
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(createUser());
        Mockito.when(consultationController.assignForConsultation(1l)).thenReturn(testRequest);
        TestRequest testRequestResult=consultationController.assignForConsultation(1l);
        //Use assertThat() methods to perform the following two comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'DIAGNOSIS_IN_PROCESS'
        // make use of assertNotNull() method to make sure that the consultation value of second object is not null
        // use getConsultation() method to get the lab result
        assertThat(testRequest.getRequestId(),equalTo(testRequestResult.getRequestId()));
        assertThat(RequestStatus.DIAGNOSIS_IN_PROCESS ,equalTo( testRequestResult.getStatus()));
        assertNotNull(testRequestResult.getConsultation());

    }

    public TestRequest getTestRequestByStatus(RequestStatus status) {

        TestRequest testRequest= new TestRequest();
        testRequest.setStatus(status);
        testRequest.setName("Test USer");
        testRequest.setCreated(LocalDate.now());
        testRequest.setRequestId(1l);
        testRequest.setAge(25);
        testRequest.setEmail("testUser@gmail.com");
        testRequest.setPhoneNumber("95649546");
        testRequest.setPinCode(415852);
        testRequest.setAddress("some address");
        testRequest.setGender(Gender.FEMALE);
        testRequest.setLabResult(createLabResult());
        testRequest.setCreatedBy(createUser());
        testRequest.setConsultation(createConsultation());
        return testRequest;
    }
    @Test
    public void calling_assignForConsultation_with_valid_test_request_id_should_throw_exception(){

        Long InvalidRequestId= -34L;

        //Implement this method
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(createUser());

        // Create an object of ResponseStatusException . Use assertThrows() method and pass assignForConsultation() method
        // of consultationController with InvalidRequestId as Id
        Mockito.when(consultationController.assignForConsultation(InvalidRequestId)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid ID"));
        ResponseStatusException exception =assertThrows(ResponseStatusException.class,()->{

            consultationController.assignForConsultation(InvalidRequestId);
        });
        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"

        assertThat(exception.getMessage(),containsString("Invalid ID"));



    }

    @Test
    @WithUserDetails(value = "doctor")
    public void calling_updateConsultation_with_valid_test_request_id_should_update_the_request_status_and_update_consultation_details(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.COMPLETED);

        //Implement this method
        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter
        CreateConsultationRequest consultationRequest=getCreateConsultationRequest(testRequest);
        //Create another object of the TestRequest method and explicitly update the status of this object
        // to be 'COMPLETED'. Make use of updateConsultation() method from labRequestController class (Pass the previously created two objects as parameters)
        CreateLabResult createLabResult=getCreateLabResult();

        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(createUser());
        Mockito.when(testRequestUpdateService.updateConsultation(1l,consultationRequest,createUser())).thenReturn(testRequest);
     //   TestRequest request= labRequestController.updateConsultation(1l,createLabResult);
        //Use assertThat() methods to perform the following three comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'COMPLETED'
        // 3. the suggestion of both the objects created should be same. Make use of getSuggestion() method to get the results.
        assertThat(testRequest.getRequestId(),equalTo(request.getRequestId()));
        assertThat(RequestStatus.DIAGNOSIS_IN_PROCESS ,equalTo( request.getStatus()));
        assertNotNull(request.getConsultation());


    }


    @Test
    @WithUserDetails(value = "doctor")
    public void calling_updateConsultation_with_invalid_test_request_id_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);

        //Implement this method

        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter
        CreateConsultationRequest consultationRequest=getCreateConsultationRequest(testRequest);
        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateConsultation() method
        // of consultationController with a negative long value as Id and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"

    }

    @Test
    @WithUserDetails(value = "doctor")
    public void calling_updateConsultation_with_invalid_empty_status_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);

        //Implement this method

        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter
        // Set the suggestion of the above created object to null.

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateConsultation() method
        // of consultationController with request Id of the testRequest object and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method


    }

    public CreateConsultationRequest getCreateConsultationRequest(TestRequest testRequest) {
        CreateConsultationRequest consultationRequest=new CreateConsultationRequest();
        //Create an object of CreateLabResult and set all the values
        CreateLabResult createLabResult=getCreateLabResult();
        if(createLabResult.getResult().equals("Negative")){
            consultationRequest.setComments("OK");
            consultationRequest.setSuggestion(DoctorSuggestion.NO_ISSUES);
        } else {
            consultationRequest.setComments("Admit Immediately ");
            consultationRequest.setSuggestion(DoctorSuggestion.HOME_QUARANTINE);
        }
        // if the lab result test status is Positive, set the doctor suggestion as "HOME_QUARANTINE" and comments accordingly
        // else if the lab result status is Negative, set the doctor suggestion as "NO_ISSUES" and comments as "Ok"
        // Return the object


        return null; // Replace this line with your code

    }

    public CreateLabResult getCreateLabResult() {
        CreateLabResult createLabResult =new CreateLabResult();
        createLabResult.setBloodPressure("101");
        createLabResult.setComments("take rest");
        createLabResult.setHeartBeat("99");
        createLabResult.setOxygenLevel("98");
        createLabResult.setResult(TestStatus.NEGATIVE);
        createLabResult.setTemperature("97");

        //Create an object of CreateLabResult and set all the values
        // Return the object

        return createLabResult; // Replace this line with your code
    }
    private LabResult createLabResult(){
        LabResult result= new LabResult();
        result.setBloodPressure("101");
        result.setComments("take rest");
        result.setHeartBeat("99");
        result.setOxygenLevel("98");
        result.setResult(TestStatus.NEGATIVE);
        result.setTemperature("97");
        return  result;
    }
    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("someuser");

        return user;
    }

    private Consultation createConsultation(){
        Consultation consultation= new Consultation();
       consultation.setDoctor(createUser());
       consultation.setUpdatedOn(LocalDate.now());
       consultation.setComments("take rest");

       consultation.setSuggestion(DoctorSuggestion.HOME_QUARANTINE);
        return consultation;
    }


}