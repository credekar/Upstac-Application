package org.upgrad.upstac.testrequests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.testrequests.consultation.Consultation;
import org.upgrad.upstac.testrequests.lab.CreateLabResult;
import org.upgrad.upstac.testrequests.lab.LabRequestController;
import org.upgrad.upstac.testrequests.lab.LabResult;
import org.upgrad.upstac.testrequests.lab.TestStatus;
import org.upgrad.upstac.users.User;
import org.upgrad.upstac.users.models.Gender;
import org.upgrad.upstac.exception.UpgradResponseStatusException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Slf4j
@ExtendWith(MockitoExtension.class)
public class LabRequestControllerTest {


    @InjectMocks
  //  @Autowired
    LabRequestController labRequestController;

    @Mock
    UserLoggedInService userLoggedInService;


    @Mock
   // @Autowired
    TestRequestQueryService testRequestQueryService;
    @Mock
    TestRequestUpdateService testRequestUpdateService;
    UpgradResponseStatusException excp;
    @Test
    @WithUserDetails(value = "tester")
    public void calling_assignForLabTest_with_valid_test_request_id_should_update_the_request_status(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.INITIATED);

        // testRequest.setStatus();
        //Implement this method
        User user=createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.assignForLabTest(1l, user)).thenReturn(testRequest);
        //Create another object of the TestRequest method and explicitly assign this object for Lab Test using assignForLabTest() method
        // from labRequestController class. Pass the request id of testRequest object.
        TestRequest test= labRequestController.assignForLabTest(1L);

        /*
        Use assertThat() methods to perform the following two comparisons
          1. the request ids of both the objects created should be same
          2. the status of the second object should be equal to 'INITIATED'
         make use of assertNotNull() method to make sure that the lab result of second object is not null
         use getLabResult() method to get the lab result
        */
        assertThat(testRequest.getRequestId(),equalTo(test.getRequestId()));
        assertThat(RequestStatus.INITIATED ,equalTo( test.getStatus()));
        assertNotNull(test.getLabResult());
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

        return testRequest;
    }

    @Test
    @WithUserDetails(value = "tester")
    public void calling_assignForLabTest_with_valid_test_request_id_should_throw_exception(){

        Long InvalidRequestId= -34L;

        //Implement this method
        User user =createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.assignForLabTest(InvalidRequestId,user)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid ID"));
        ResponseStatusException exception =assertThrows(ResponseStatusException.class,()->{

            labRequestController.assignForLabTest(InvalidRequestId);
        });


        assertThat(exception.getMessage(),containsString("Invalid ID"));
    }

    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_valid_test_request_id_should_update_the_request_status_and_update_test_request_details(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);
        CreateLabResult latResult=getCreateLabResult();
        User user= createUser();
        testRequest.setLabResult(createLabResult());
        //Implement this method
        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        //Create another object of the TestRequest method and explicitly update the status of this object
        // to be 'LAB_TEST_IN_PROGRESS'. Make use of updateLabTest() method from labRequestController class (Pass the previously created two objects as parameters)
        Mockito.when(testRequestUpdateService.updateLabTest(1L,latResult,user)).thenReturn(testRequest);
        TestRequest result=labRequestController.updateLabTest(1L,latResult);
        //Use assertThat() methods to perform the following three comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'LAB_TEST_COMPLETED'
        // 3. the results of both the objects created should be same. Make use of getLabResult() method to get the results.
        assertThat(testRequest.getRequestId(),equalTo(result.getRequestId()));
        assertThat(testRequest.getStatus(),equalTo(result.getStatus()));
        assertThat(testRequest.getLabResult(), equalTo(result.getLabResult()));


    }


    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_invalid_test_request_id_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);


        //Implement this method
        CreateLabResult createLabResult=getCreateLabResult();
        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with a negative long value as Id and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
        User user =createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateLabTest(-34L,createLabResult,user)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid ID"));
        ResponseStatusException exception =assertThrows(ResponseStatusException.class,()->{

            labRequestController.updateLabTest(-34L,createLabResult);
        });

        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
        assertThat(exception.getMessage(),containsString("Invalid ID"));
    }

    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_invalid_empty_status_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

        //Implement this method
        CreateLabResult createLabResult=getCreateLabResult();

        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        // Set the result of the above created object to null.

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with request Id of the testRequest object and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
        User user =createUser();
       ConstraintViolationException e=new ConstraintViolationException("ConstraintViolationException",Mockito.anySet());
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
       Mockito.when(testRequestUpdateService.updateLabTest(1L,createLabResult,user)).thenThrow(e/*excp.asConstraintViolation(e)*/);
        ResponseStatusException exception =assertThrows(ResponseStatusException.class,()->{

            labRequestController.updateLabTest(1L,createLabResult);
        });

        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "ConstraintViolationException"
        assertThat(exception.getMessage(),containsString("ConstraintViolationException"));

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
    public LabResult createLabResult(){
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


}