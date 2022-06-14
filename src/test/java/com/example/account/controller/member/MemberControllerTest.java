package com.example.account.controller.member;

import com.example.account.constatnt.Code;
import com.example.account.domain.member.Member;
import com.example.account.dto.member.CreateMemberDto;
import com.example.account.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    private CreateMemberDto request;

    @BeforeEach
    void setup() {
        request = CreateMemberDto.builder()
                .name("신성수")
                .phoneNumber("01020834409")
                .birthDay("19950918")
                .balance(1000L)
                .build();
    }

    @Test
    @DisplayName("회원 가입 Mockito 테스트")
    void 회원가입_테스트() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        doNothing().when(memberService).createMember(any(Member.class));

        // then
        mockMvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(Code.SUCESS.getCode()))
                .andExpect(jsonPath("$.message").value(Code.SUCESS.getMessage()))
                .andExpect(jsonPath("$.accountNumber").value("0000000001"))
                .andExpect(status().isOk());
    }

}
