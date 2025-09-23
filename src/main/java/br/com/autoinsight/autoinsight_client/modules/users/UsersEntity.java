package br.com.autoinsight.autoinsight_client.modules.users;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UsersEntity implements UserDetails {

  @Id
  private String id;

  @Column(name = "name", nullable = false, length = 150)
  @Size(max = 150, message = "Name must not exceed 150 characters")
  private String name;

  @Column(name = "email", nullable = false, unique = true, length = 150)
  @Size(max = 150, message = "Email must not exceed 150 characters")
  @Email(message = "Invalid email format")
  private String email;

  @JsonIgnore
  @Column(name = "password", nullable = false, length = 100)
  @Size(max = 100, message = "Password must not exceed 100 characters")
  private String password;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}