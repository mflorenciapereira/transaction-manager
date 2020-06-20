package controller;

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

import javax.persistence.LockTimeoutException;
import javax.persistence.PessimisticLockException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController underTest = new TransactionController();
    private String type = TransactionType.CREDIT.getCode();
    private Double amount = 200.0;
    private String id = "1";

    @Test
    public void commit_normalCase_returnsOK() {

        ResponseEntity<String> responseEntity = underTest.commit(new TransactionDTO(type, amount));
        verify(transactionService).save(type, amount);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "OK response");
    }

    @Test
    public void commit_IllegalArgument_returnsBadReq() {

        doThrow(new IllegalArgumentException()).when(transactionService).save(type, amount);
        Exception exception = assertThrows(ResponseStatusException.class, () -> underTest.commit(new TransactionDTO(type, amount)), "throws ResponseStatusException");
        assertEquals("400 BAD_REQUEST", exception.getMessage());

    }

    @Test
    public void commit_InternalServerError_returnsBadReq() {

        doThrow(new NullPointerException()).when(transactionService).save(type, amount);
        Exception exception = assertThrows(ResponseStatusException.class, () -> underTest.commit(new TransactionDTO(type, amount)), "throws ResponseStatusException");
        assertEquals("500 INTERNAL_SERVER_ERROR", exception.getMessage());

    }

    @Test
    public void commit_PessimisticLockException_returnsBadReq() {

        doThrow(new PessimisticLockException()).when(transactionService).save(type, amount);
        Exception exception = assertThrows(ResponseStatusException.class, () -> underTest.commit(new TransactionDTO(type, amount)), "throws ResponseStatusException");
        assertEquals("500 INTERNAL_SERVER_ERROR", exception.getMessage());

    }

    @Test
    public void commit_LockTimeoutException_returnsBadReq() {

        doThrow(new LockTimeoutException()).when(transactionService).save(type, amount);
        Exception exception = assertThrows(ResponseStatusException.class, () -> underTest.commit(new TransactionDTO(type, amount)), "throws ResponseStatusException");
        assertEquals("500 INTERNAL_SERVER_ERROR", exception.getMessage());

    }


    @Test
    public void all_normalCase_returnsOK() {

        doReturn(Arrays.asList(new Transaction(type, amount))).when(transactionService).all();
        ResponseEntity responseEntity = underTest.all();
        verify(transactionService).all();
        ((List<TransactionDTO>)responseEntity.getBody()).contains(new TransactionDTO(type, amount));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "OK response");
    }

    @Test
    public void all_exception_throwsException() {

        doThrow(new NullPointerException()).when(transactionService).all();
        Exception exception = assertThrows(ResponseStatusException.class, () -> underTest.all(), "throws ResponseStatusException");
        assertEquals("500 INTERNAL_SERVER_ERROR", exception.getMessage());

    }

    @Test
    public void findById_normalCase_returnsOK() {

        Transaction transaction = new Transaction(type, amount);
        doReturn(transaction).when(transactionService).findById(id);
        ResponseEntity responseEntity = underTest.findById(id);
        verify(transactionService).findById(id);
        assertEquals(responseEntity.getBody(),new TransactionHistoryDTO(transaction));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "OK response");
    }

    @Test
    public void findById_exception_throwsException() {

        doThrow(new NullPointerException()).when(transactionService).findById(id);
        Exception exception = assertThrows(ResponseStatusException.class, () -> underTest.findById(id), "throws ResponseStatusException");
        assertEquals("500 INTERNAL_SERVER_ERROR", exception.getMessage());

    }

    @Test
    public void findById_notFound_throwsException() {

        doThrow(new TransactionNotFoundException(id)).when(transactionService).findById(id);
        Exception exception = assertThrows(ResponseStatusException.class, () -> underTest.findById(id), "throws ResponseStatusException");
        assertEquals("Transaction not found", ((ResponseStatusException) exception).getReason());

    }




}
