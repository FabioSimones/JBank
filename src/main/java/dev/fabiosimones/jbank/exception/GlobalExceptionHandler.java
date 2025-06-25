package dev.fabiosimones.jbank.exception;

import dev.fabiosimones.jbank.exception.dto.InvalidParamDTO;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JBankException.class)
    public ProblemDetail handleJBankException(JBankException e){
        return e.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        var invalidParams = e.getFieldErrors()
                .stream()
                .map(fe -> new InvalidParamDTO(fe.getField(), fe.getDefaultMessage()))
                .toList();

        var pd = ProblemDetail.forStatus(400);

        pd.setTitle("Invalid request parameters.");
        pd.setDetail("There is invalid fields on the request.");
        pd.setProperty("invalid-params ", invalidParams);

        return pd;

    }
}
