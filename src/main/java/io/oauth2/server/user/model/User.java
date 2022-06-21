package io.oauth2.server.user.model;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter @Setter
@Table(name = "user_info")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    // 사용자 이름
    @Column(name = "user_name", length = 64)
    private String username;

    // 비밀번호
    @Column(name = "password", length = 128)
    private String password;

    // 고객번호
    @Column(name = "customer_no", length = 64)
    private String customerNo;

    // 이메일
    @Column(name = "e_mail", length = 64)
    private String email;

    // 전화번호
    @Column(name = "phone_number", length = 32)
    private String phoneNumber;

    // 권한
    @Column(name = "role", length = 32)
    @Enumerated(EnumType.STRING)
    private Roles role;

    public List<String> getRoleList() {
        List<String> enumList = Stream.of(this.role.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return enumList;
    }

    public User(long id, String username, String password, String customerNo, String email, String phoneNumber, Roles role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.customerNo = customerNo;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
