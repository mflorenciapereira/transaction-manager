package controller;

import com.florencia.agileengine.controller.DefaultController;
import com.florencia.agileengine.controller.TransactionController;
import com.florencia.agileengine.domain.Transaction;
import com.florencia.agileengine.domain.TransactionType;
import com.florencia.agileengine.dto.TransactionDTO;
import com.florencia.agileengine.dto.TransactionHistoryDTO;
import com.florencia.agileengine.exception.TransactionNotFoundException;
import com.florencia.agileengine.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DefaultControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private DefaultController underTest = new DefaultController();
    private String type = TransactionType.CREDIT.getCode();
    private Double amount = 200.0;
    private String id = "1";

    @Test
    public void balance_normalcase_ok() {

        doReturn(10.0).when(transactionService).getBalance();
        ResponseEntity<Double> responseEntity = underTest.balance();
        verify(transactionService).getBalance();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "OK response");
    }

    @Test
    public void balance_exception_throwsException() {

        doThrow(new NullPointerException()).when(transactionService).getBalance();
        Exception exception = assertThrows(ResponseStatusException.class, () -> underTest.balance(), "throws ResponseStatusException");
        assertEquals("500 INTERNAL_SERVER_ERROR", ((ResponseStatusException) exception).getMessage());

    }


}
