package com.app.APICode.exception;

import com.app.APICode.ctest.CtestNotFoundException;
import com.app.APICode.employee.EmployeeNotFoundException;
import com.app.APICode.measure.MeasureNotFoundException;
import com.app.APICode.restaurant.RestaurantDuplicateException;
import com.app.APICode.restaurant.RestaurantNotFoundException;
import com.app.APICode.user.EmailNotFoundException;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserOrEmailExistsException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Generate a standard type of error message structure for most errors such as
     * when binding fails and @Valid fails in a request
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = "";
        for (ObjectError objectError : ex.getBindingResult().getAllErrors()) {
            message = message + objectError.getDefaultMessage();
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message);
        return new ResponseEntity<Object>(apiError, headers, status);
    }

    /**
     * Handle when the method argument is not of the expected type
     * 
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        String message = ex.getName() + " should be of type " + ex.getRequiredType().getName();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle our own exceptions
     * 
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({ EmployeeNotFoundException.class, CtestNotFoundException.class, MeasureNotFoundException.class,
            RestaurantNotFoundException.class, EmailNotFoundException.class, UserNotFoundException.class })
    public ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        String message = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), message);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ UserOrEmailExistsException.class, RestaurantDuplicateException.class })
    public ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        String message = ex.getMessage();

        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), message);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Default handler for any other exception not in the list. Can specify more for
     * more fine grained control.
     * 
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "An error occurred. Please try again later. ");
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

}
