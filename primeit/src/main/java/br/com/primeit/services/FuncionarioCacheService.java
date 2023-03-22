package br.com.primeit.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import br.com.primeit.model.Funcionario;

@Service
public class FuncionarioCacheService {

	private final HashOperations<String, Integer, Funcionario> hashOperations;
    private final FuncionarioService funcionarioService;
	
    public FuncionarioCacheService(RedisTemplate<String, Funcionario> redisTemplate,
    								FuncionarioService funcionarioService) {
    		this.hashOperations = redisTemplate.opsForHash();
    		this.funcionarioService = funcionarioService;
    	}

    public List<Funcionario> getAllFuncionarios() {
        List<Funcionario> funcionarios = hashOperations.values("funcionarios");
        if (funcionarios == null || funcionarios.isEmpty()) {
            funcionarios = funcionarioService.getAllFuncionarios();
            saveAll(funcionarios);
        }
        return funcionarios;
    }

    public Optional<Funcionario> getById(Integer id) {
        Funcionario funcionario = hashOperations.get("funcionarios", id);
        if (funcionario != null) {
            return Optional.of(funcionario);
        } else {
            Optional<Funcionario> optionalFuncionario = funcionarioService.getById(id);
            if (optionalFuncionario.isPresent()) {
                Funcionario newFuncionario = optionalFuncionario.get();
                hashOperations.put("funcionarios", newFuncionario.getId(), newFuncionario);
                return optionalFuncionario;
            } else {
                return Optional.empty();
            }
        }
    }

    public void save(Funcionario funcionario) {
        hashOperations.put("funcionarios", funcionario.getId(), funcionario);
    }

    public void saveAll(List<Funcionario> funcionarios) {
        Map<Integer, Funcionario> map = new HashMap<>();
        for (Funcionario funcionario : funcionarios) {
            map.put(funcionario.getId(), funcionario);
        }
        hashOperations.putAll("funcionarios", map);
    }

    public void delete(Integer id) {
        hashOperations.delete("funcionarios", id);
    }
}
	