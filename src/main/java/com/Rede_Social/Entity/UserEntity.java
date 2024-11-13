package com.Rede_Social.Entity;

import com.Rede_Social.Entity.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @NotNull
    @Size(min = 2, max = 100, message = "O nome n pode ter menos que 2 caracteres e mais que 100")
    private String nome;

    @Column
    @NotNull
    @Min(value = 18, message = "O usuário deve ter pelo menos 18 anos")
    private int idade;

    @Column
    @NotNull
    @NotEmpty
    @Email
    private String email;

    @Column
    @NotNull
    @NotEmpty
    private String senha;

    Boolean ativo = false;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user", "complaints"})  // Evita loop infinito durante a serialização de ComplaintEntity
    private List<ComplaintEntity> complaints = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user", "likes"})  // Evita loop infinito durante a serialização de LikeEntity
    private List<LikeEntity> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user", "posts"})
    private List<PostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user", "comments"})
    private List<CommentEntity> comments = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public UserEntity(Role role, String nome, int idade, String email, String senha, Boolean ativo) {
        this.role = role;
        this.nome = nome;
        this.idade = idade;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
    }
}