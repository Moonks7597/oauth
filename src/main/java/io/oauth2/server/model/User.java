package io.oauth2.server.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Data
@Table(name = "user",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "user_name", "e_mail"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 사용자 이름
    @Column(name = "user_name")
    private String username;

    // 비밀번호
    @Column(name = "password")
    private String password;

    // 고객번호
    @Column(name = "customer_no")
    private String customerNo;

    // 이메일
    @Column(name = "e_mail")
    private String email;

    // 전화번호
    @Column(name = "phone_number")
    private String phoneNumber;

    // 권한
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Roles role;

    public List<String> getRoleList() {
        List<String> enumList = Stream.of(this.role.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return enumList;
    }
}
