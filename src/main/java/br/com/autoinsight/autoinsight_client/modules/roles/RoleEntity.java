package br.com.autoinsight.autoinsight_client.modules.roles;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.autoinsight.autoinsight_client.modules.users.UsersEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class RoleEntity {

  @Id
  private String id;

  @Column(name = "name", nullable = false, unique = true, length = 50)
  @Size(max = 50, message = "Name must not exceed 50 characters")
  private String name;

  @Column(name = "acronym", nullable = false, unique = true, length = 10)
  @Size(max = 10, message = "Acronym must not exceed 10 characters")
  private String acronym;

  @JsonIgnore
  @OneToMany(mappedBy = "role")
  private List<UsersEntity> users;
}
