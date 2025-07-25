package com.gitlab.controller;

import com.gitlab.dto.BankCardDto;
import com.gitlab.mapper.BankCardMapper;
import com.gitlab.model.BankCard;
import com.gitlab.service.BankCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.CoreMatchers.equalTo;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

class BankCardRestControllerIT extends AbstractIntegrationTest {

    private static final String BANK_CARD_URN = "/api/bank-card";
    private static final String BANK_CARD_URI = URL + BANK_CARD_URN;
    @Autowired
    private BankCardService bankCardService;
    @Autowired
    private BankCardMapper bankCardMapper;

    @Test
    void should_get_all_bankCards() throws Exception {
        String expected = objectMapper.writeValueAsString(
                bankCardService
                        .findAll()
                        .stream()
                        .map(bankCardMapper::toDto)
                        .collect(Collectors.toList())
        );

        mockMvc.perform(get(BANK_CARD_URI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void should_get_bankCard_by_id() throws Exception {
        long id = 1L;
        String expected = objectMapper.writeValueAsString(
                bankCardMapper.toDto(
                        bankCardService
                                .findById(id)
                                .orElse(null))
        );

        mockMvc.perform(get(BANK_CARD_URI + "/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void should_return_not_found_when_get_bankCard_by_non_existent_id() throws Exception {
        long id = 10L;
        mockMvc.perform(get(BANK_CARD_URI + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_create_bankCard() throws Exception {
        BankCardDto bankCardDto = generateBankCardDto();
        String jsonBankCardDto = objectMapper.writeValueAsString(bankCardDto);

        mockMvc.perform(post(BANK_CARD_URI)
                        .content(jsonBankCardDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void should_update_bankCard_by_id() throws Exception {
        long id = 1L;
        int numberOfEntitiesExpected = bankCardService.findAll().size();
        BankCardDto bankCardDto = generateBankCardDto();
        String jsonBankCardDto = objectMapper.writeValueAsString(bankCardDto);

        bankCardDto.setId(id);
        String expected = objectMapper.writeValueAsString(bankCardDto);

        mockMvc.perform(patch(BANK_CARD_URI + "/{id}", id)
                        .content(jsonBankCardDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected))
                .andExpect(result -> assertThat(bankCardService.findAll().size(),
                        equalTo(numberOfEntitiesExpected)));
    }

    @Test
    void should_return_not_found_when_update_bankCard_by_non_existent_id() throws Exception {
        long id = 10L;
        BankCardDto bankCardDto = generateBankCardDto();
        String jsonBankCardDto = objectMapper.writeValueAsString(bankCardDto);

        mockMvc.perform(patch(BANK_CARD_URI + "/{id}", id)
                        .content(jsonBankCardDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_delete_bankCard_by_id() throws Exception {
        BankCardDto bankCardDto = bankCardService.saveDto(generateBankCardDto());
        long id = bankCardDto.getId();
        mockMvc.perform(delete(BANK_CARD_URI + "/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get(BANK_CARD_URI + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private BankCardDto generateBankCardDto() {
        BankCardDto bankCardDto = new BankCardDto();
        bankCardDto.setCardNumber("123456789");
        bankCardDto.setDueDate(LocalDate.now());
        bankCardDto.setSecurityCode(123);
        return bankCardDto;
    }
}
