package service;

import com.florencia.agileengine.dao.TransactionRepository;
import com.florencia.agileengine.domain.Transaction;
import com.florencia.agileengine.exception.InvalidTransactionException;
import com.florencia.agileengine.exception.TransactionNotFoundException;
import com.florencia.agileengine.service.TransactionService;
import com.florencia.agileengine.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    public static final String CREDIT_TYPE = "credit";
    public static final String DEBIT_TYPE = "debit";
    public static final double AMOUNT = 1000.0;
    private String ID = "1ADADS";

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService underTest = new TransactionServiceImpl();

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;


    @Test
    public void all_normalCase_returnsOk(){
        Transaction transaction = new Transaction(CREDIT_TYPE, AMOUNT);
        when(transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "effectiveDate"))).thenReturn(Arrays.asList(transaction));
        List<Transaction> transactionList = underTest.all();
        assertEquals(1, transactionList.size());
        assertEquals(transaction, transactionList.iterator().next());
    }

    @Test
    public void all_exception_returnsOk(){
        doThrow(new NullPointerException()).when(transactionRepository).findAll(Sort.by(Sort.Direction.DESC, "effectiveDate"));
        assertThrows(NullPointerException.class, () -> underTest.all(), "throws NullPointerException");
    }
    @Test
    public void findById_normalCase_returnsOk(){
        Transaction transaction = new Transaction(CREDIT_TYPE, AMOUNT);
        when(transactionRepository.findById(ID)).thenReturn(java.util.Optional.of(transaction));
        Transaction result = underTest.findById(ID);
        assertEquals(transaction, result);
    }

    @Test
    public void findById_blankId_throwsException(){
        assertThrows(IllegalArgumentException.class, () -> underTest.findById(""), "throws IllegalArgumentException");
    }

    @Test
    public void findById_nullId_throwsException(){
        assertThrows(IllegalArgumentException.class, () -> underTest.findById(null), "throws IllegalArgumentException");
    }

    @Test
    public void findById_exception_throwsException(){
        doThrow(new NullPointerException()).when(transactionRepository).findById(ID);
        assertThrows(NullPointerException.class, () -> underTest.findById(ID), "throws NullPointerException");
    }

    @Test
    public void findById_notFound_throwsException(){
        when(transactionRepository.findById(ID)).thenReturn(java.util.Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> underTest.findById(ID), "throws TransactionNotFoundException");
    }

    @Test
    public void save_normalCaseCredit_ok(){
        when(transactionRepository.save(any())).thenReturn(new Transaction(ID, CREDIT_TYPE, AMOUNT));
        String result = underTest.save(CREDIT_TYPE, AMOUNT);
        assertEquals(result, ID);
    }

    @Test
    public void save_normalCaseDebit_ok(){
        when(transactionRepository.getTotalCredit()).thenReturn(3000.0);
        when(transactionRepository.save(any())).thenReturn(new Transaction(ID, DEBIT_TYPE, AMOUNT));
        String result = underTest.save(DEBIT_TYPE, AMOUNT);
        assertEquals(result, ID);
    }

    @Test
    public void save_blankType_illegalArgumentException(){
        assertThrows(IllegalArgumentException.class, () -> underTest.save("", AMOUNT), "throws IllegalArgumentException");
    }

    @Test
    public void save_nullType_illegalArgumentException(){
        assertThrows(IllegalArgumentException.class, () -> underTest.save(null, AMOUNT), "throws IllegalArgumentException");
    }

    @Test
    public void save_invalidType_illegalArgumentException(){
        assertThrows(NoSuchElementException.class, () -> underTest.save("invalid", AMOUNT), "throws NoSuchElementException");
    }

    @Test
    public void save_nullAmount_illegalArgumentException(){
        assertThrows(IllegalArgumentException.class, () -> underTest.save(CREDIT_TYPE, null), "throws IllegalArgumentException");
    }

    @Test
    public void save_negativeTransaction_invalidTransactionException(){
        when(transactionRepository.getTotalCredit()).thenReturn(300.0);
        when(transactionRepository.getTotalDebit()).thenReturn(10.0);
        assertThrows(InvalidTransactionException.class, () -> underTest.save(DEBIT_TYPE, AMOUNT), "throws InvalidTransactionException");
    }

    @Test
    public void getBalance_normalCase_returnsOk(){
        when(transactionRepository.getTotalCredit()).thenReturn(300.0);
        when(transactionRepository.getTotalDebit()).thenReturn(10.0);
        assertEquals(Double.valueOf(290),underTest.getBalance());
    }

    @Test
    public void getBalance_exception_returnsOk(){
        doThrow(new NullPointerException()).when(transactionRepository).getTotalDebit();
        assertThrows(NullPointerException.class, () -> underTest.getBalance(), "throws NullPointerException");
    }

}
