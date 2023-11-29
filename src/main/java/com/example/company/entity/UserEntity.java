package com.example.company.entity;

import com.sun.istack.NotNull;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "users")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userName","state","partyName"}),
})
@TypeDef(name = "json", typeClass = JsonType.class)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;
    @NotNull
    private String userName;
    private String partyName;
    private Integer state;
    private Long money;
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Long> needTo = new HashMap<>();
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Long> needGive = new HashMap<>();

}
