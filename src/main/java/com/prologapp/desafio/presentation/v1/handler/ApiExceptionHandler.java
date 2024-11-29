package com.prologapp.desafio.presentation.v1.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.prologapp.desafio.domain.enums.PosicaoPneu;
import com.prologapp.desafio.domain.enums.StatusVeiculo;
import com.prologapp.desafio.presentation.v1.constantes.PresentationConstantes;
import com.prologapp.desafio.application.exceptions.BusinessException;
import com.prologapp.desafio.application.exceptions.EntityNotFoundException;
import com.prologapp.desafio.application.exceptions.ObjectException;
import com.prologapp.desafio.insfraestructure.exceptions.RepositoryException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
@AllArgsConstructor
public class ApiExceptionHandler {
    private static final String BAD_REQUEST = "Bad Request";
    private static final String NOT_FOUND = "Not Found";
    private static final String VALIDATION_ERROR = "Validation Error";
    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    private final ReloadableResourceBundleMessageSource bundleMessageSource;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Problem> handleBusinessExceptions(BusinessException ex) {
        Problem errorResponse = Problem
                .builder()
                .error(BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .codeStatus(HttpStatus.BAD_REQUEST.value())
                .message(this.getCodeMessage(ex.getCodigoErro().toString(), ex.getArgs()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Problem> handleEntityNotFoundException(EntityNotFoundException ex) {
        Problem errorResponse = Problem
                .builder()
                .error(NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .codeStatus(HttpStatus.NOT_FOUND.value())
                .message(this.getCodeMessage(ex.getCodigoErro().toString(), ex.getArgs()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(ObjectException.class)
    public ResponseEntity<Problem> handleObjectException(ObjectException ex) {
        Problem errorResponse = Problem
                .builder()
                .error(BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .codeStatus(HttpStatus.BAD_REQUEST.value())
                .message(this.getCodeMessage(ex.getCodigoErro().toString(), ex.getArgs()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<Problem> handleRepositoryException(RepositoryException ex) {
        Problem errorResponse = Problem
                .builder()
                .error(INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .codeStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(this.getCodeMessage(ex.getCodigoErro().toString(), ex.getArgs()))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> String.format(PresentationConstantes.CAMPOS_REJEITADOS,
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()))
                .toList();

        Problem errorResponse = Problem
                .builder()
                .error(VALIDATION_ERROR)
                .timestamp(LocalDateTime.now())
                .codeStatus(HttpStatus.BAD_REQUEST.value())
                .message(PresentationConstantes.ERRO_VALIDACAO_CAMPOS)
                .details(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Problem> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> bundleMessageSource.getMessage(
                        violation.getMessage(),
                        null,
                        LocaleContextHolder.getLocale()
                ))
                .toList();

        Problem errorResponse = Problem
                .builder()
                .error(VALIDATION_ERROR)
                .timestamp(LocalDateTime.now())
                .codeStatus(HttpStatus.BAD_REQUEST.value())
                .message(PresentationConstantes.ERRO_VALIDACAO)
                .details(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Problem> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {
            String fieldName = invalidFormatException.getPath().stream()
                    .map(reference -> reference.getFieldName())
                    .findFirst()
                    .orElse(PresentationConstantes.CAMPO_DESCONHECIDO);

            String rejectedValue = invalidFormatException.getValue().toString();

            String detailMessage = invalidFormatException.getOriginalMessage();

            String validValues = detailMessage.contains("PosicaoPneu")
                    ? Arrays.toString(PosicaoPneu.values())
                    : Arrays.toString(StatusVeiculo.values());

            String errorMessage = String.format(PresentationConstantes.VALORES_ACEITOS_EM_STATUS,
                    fieldName, rejectedValue, validValues);

            Problem errorResponse = Problem
                    .builder()
                    .error(VALIDATION_ERROR)
                    .timestamp(LocalDateTime.now())
                    .codeStatus(HttpStatus.BAD_REQUEST.value())
                    .message(errorMessage)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Problem errorResponse = Problem
                .builder()
                .error(VALIDATION_ERROR)
                .timestamp(LocalDateTime.now())
                .codeStatus(HttpStatus.BAD_REQUEST.value())
                .message(PresentationConstantes.ERRO_JSON_REQUEST_BODY)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Problem> handleNullPointerException(NullPointerException ex) {
        Problem errorResponse = Problem
                .builder()
                .error(BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .codeStatus(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private String getCodeMessage(String codigoMensagem, Object[] args) {
        return bundleMessageSource.getMessage(codigoMensagem, args, LocaleContextHolder.getLocale());
    }
}
