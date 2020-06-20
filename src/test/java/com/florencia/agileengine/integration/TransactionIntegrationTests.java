package com.florencia.agileengine.integration;

import com.florencia.agileengine.dto.TransactionDTO;
import com.florencia.agileengine.dto.TransactionHistoryDTO;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionIntegrationTests {

	public static final String LEFT = "left";
	public static final String RIGHT = "right";
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	String url = "http://localhost:%s/api/transactions";
	String urlWithId = "http://localhost:%s/api/transactions/%s";
	String urlBalance = "http://localhost:%s/api/default";

	@Test
	void testCompleteCycle() {
		ResponseEntity<String> responsePost;
		ResponseEntity<TransactionHistoryDTO> responseResult;
		ResponseEntity<List<TransactionHistoryDTO>> responseResults;
		ResponseEntity<Double> responseBalance;

		responsePost = postTransaction("credit", 1000.0);
		assertEquals(HttpStatus.OK, responsePost.getStatusCode());
		String idCredit = responsePost.getBody();

		responseResult = getResult(idCredit);
		assertEquals(HttpStatus.OK, responseResult.getStatusCode());
		TransactionHistoryDTO dto = responseResult.getBody();
		assertTrue(same(idCredit, "credit", Double.valueOf(1000.0), new Date(), dto));

		responsePost = postTransaction("debit", 50.0);
		assertEquals(HttpStatus.OK, responsePost.getStatusCode());
		String idDebit = responsePost.getBody();

		responseResults = getResults();
		assertEquals(HttpStatus.OK, responseResult.getStatusCode());
		List<TransactionHistoryDTO> dtos = responseResults.getBody();
		assertEquals(2, dtos.size());
		assertTrue(dtos.stream().anyMatch(
				d -> same(idCredit, "credit", 1000.0, new Date(), d) ||
						same(idDebit, "debit", 50.0, new Date(), d)));

		responseBalance = getBalance();
		assertEquals(HttpStatus.OK, responseResult.getStatusCode());
		Double balance = responseBalance.getBody();
		assertEquals(Double.valueOf(950), balance);

	}

	private ResponseEntity<String> postTransaction(String type, Double amount) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		TransactionDTO transactionDTO = new TransactionDTO(type, amount);
		HttpEntity request = new HttpEntity(transactionDTO, headers);
		return restTemplate.postForEntity(createURL(),request, String.class );
	}

	private boolean same(String id, String type, Double amount, Date effectiveDate, TransactionHistoryDTO dto){
		return id.equals(dto.getId()) &&
		type.equals(dto.getType()) &&
		amount.equals(dto.getAmount()) &&
		DateUtils.isSameDay(effectiveDate, dto.getEffectiveDate());
	}

	private String createURL() {
		return String.format(url, port);
	}

	private String createResultURLWithPort(String id) {
		return String.format(urlWithId, port, id);
	}

	private String createBalanceURL() {
		return String.format(urlBalance, port);
	}

	private ResponseEntity<TransactionHistoryDTO> getResult(String id) {
		return restTemplate.getForEntity(createResultURLWithPort(id), TransactionHistoryDTO.class );
	}

	private ResponseEntity<List<TransactionHistoryDTO>> getResults() {
		return restTemplate.exchange(createURL(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<TransactionHistoryDTO>>() {});
	}

	private ResponseEntity<Double> getBalance() {
		return restTemplate.getForEntity(createBalanceURL(), Double.class );
	}

}
