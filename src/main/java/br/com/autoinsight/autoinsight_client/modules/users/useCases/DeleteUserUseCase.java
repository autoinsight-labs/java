package br.com.autoinsight.autoinsight_client.modules.users.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserUseCase {

  @Autowired
  private UsersCachingUseCase usersCachingUseCase;

  public void execute(String id) {
    if (!usersCachingUseCase.existsById(id)) {
      throw new RuntimeException("User not found");
    }

    usersCachingUseCase.deleteById(id);
  }
}