package com.example.demo.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientAddress {
    private String address; // 도로명 | 지번 주소
    private String detail; // 상세주소
}
