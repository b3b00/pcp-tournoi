package org.pcp.tournament.web;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.pcp.tournament.web.exception.PCPError;
import org.pcp.tournament.web.exception.PCPException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class PCPExceptionResolver {
 

  Map<PCPError, HttpStatus> errorMapping = Stream.of(new Object[][] { 
    { PCPError.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR },    
    { PCPError.BAD_REQUEST, HttpStatus.BAD_REQUEST },
    { PCPError.UNAUTHORIZED, HttpStatus.UNAUTHORIZED },
    { PCPError.NOT_FOUND, HttpStatus.NOT_FOUND }
}).collect(Collectors.toMap(data -> (PCPError)data[0], data -> (HttpStatus) data[1]));

    @ExceptionHandler(value 
      = { PCPException.class  })
    protected final ResponseEntity<PCPErrorMessage> handleException(
      Exception ex, WebRequest request) {
   
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        PCPErrorMessage errorMessage  = null;

        if (ex instanceof PCPException) {
          PCPException pcpe = (PCPException)ex;
          httpStatus = errorMapping.get(pcpe.getError());
          httpStatus = httpStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : httpStatus;
          errorMessage = new PCPErrorMessage(pcpe.getError().getValue(),ex.getMessage());
        }

        return new ResponseEntity<PCPErrorMessage>(errorMessage,httpStatus);

    }

   
}