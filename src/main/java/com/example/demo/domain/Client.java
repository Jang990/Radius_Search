package com.example.demo.domain;

import com.example.demo.auditing.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Client extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phoneNumber;

    @Embedded
    private ClientAddress address;

    @Embedded
    private ClientLocation location;

    public Client(int sigId, double pointSig) {
        String sig = sigId+""+sigId;
        this.name = "사용자 " + sig;
        this.phoneNumber = "010-1111-" + sig;
        this.address = new ClientAddress("address " + sig, "detail " + sig);
        this.location = new ClientLocation(35d + pointSig * sigId, 125.0d + pointSig * sigId);
    }

    public Client(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
