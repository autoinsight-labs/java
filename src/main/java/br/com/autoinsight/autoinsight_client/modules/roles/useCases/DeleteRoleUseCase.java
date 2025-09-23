package br.com.autoinsight.autoinsight_client.modules.roles.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteRoleUseCase {

  @Autowired
  private RoleCachingUseCase roleCachingUseCase;

  public void execute(String id) {
    roleCachingUseCase.deleteById(id);
    roleCachingUseCase.clearCache();
  }
}
