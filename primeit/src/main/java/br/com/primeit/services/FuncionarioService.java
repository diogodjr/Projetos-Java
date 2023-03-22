package br.com.primeit.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.primeit.model.Funcionario;
import br.com.primeit.model.Saida;
import br.com.primeit.repository.FuncionarioRepository;

@Service
public class FuncionarioService {
    
    private FuncionarioRepository funcionarioRepository;

	@Autowired
    public FuncionarioService(@Qualifier("funcionarioRepository") FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }
	
    @Autowired
    private FuncionarioCacheService funcionarioCacheService;
    
    @Autowired
    private RedisTemplate<String, Funcionario> redisTemplate;

    public void saveToCache(Funcionario funcionario) {
        String key = "funcionario_" + funcionario.getId();
        ValueOperations<String, Funcionario> ops = redisTemplate.opsForValue();
        ops.set(key, funcionario);
    }

    public Funcionario getFromCache(Integer id) {
        String key = "funcionario_" + id;
        ValueOperations<String, Funcionario> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }
  
    public Optional<Funcionario> getById(Integer id) {
        Optional<Funcionario> optionalFuncionario = Optional.ofNullable(getFromCache(id));
        if (optionalFuncionario.isEmpty()) {
            optionalFuncionario = funcionarioRepository.findById(id);
            if (optionalFuncionario.isPresent()) {
                saveToCache(optionalFuncionario.get());
            }	
        }
        return optionalFuncionario;
    }
    
    public List<Funcionario> getAllFuncionarios() {
        List<Funcionario> funcionarios = Optional.ofNullable(getFromCache()).orElse(Collections.emptyList());
        if (funcionarios.isEmpty()) {
            funcionarios = funcionarioRepository.findAll();
            for (Funcionario funcionario : funcionarios) {
                saveToCache(funcionario);
            }
        }
        return funcionarios;
    }
    
    public List<Funcionario> getFromCache() {
        Set<String> keys = redisTemplate.keys("funcionario_*");
        List<Funcionario> funcionarios = new ArrayList<>();
        if (!keys.isEmpty()) {
            List<Funcionario> cachedFuncionarios = redisTemplate.opsForValue().multiGet(keys);
            for (Funcionario funcionario : cachedFuncionarios) {
                if (funcionario != null) {
                    funcionarios.add(funcionario);
                }
            }
        }
        return funcionarios;
    }

    public Saida findByIdAndName(Integer id, String nome) {
        Funcionario funcionario = getFromCache(id);
        if (funcionario == null) {
            Optional<Funcionario> optionalFuncionario = funcionarioRepository.findById(id);
            if (optionalFuncionario.isPresent()) {
                funcionario = optionalFuncionario.get();
                saveToCache(funcionario);
            } else {
                return new Saida("Erro: O funcionário não existe.", 0, null);
            }
        }

        if (!funcionario.getNome().equals(nome)) {
            return new Saida("Erro: O funcionário não existe.", 0, null);
        }

        return new Saida("Sucesso. Funcionário encontrado", 1, funcionario);
    }
    
    public Saida addFuncionario(Funcionario funcionario) {
        Saida saida;
		if (funcionarioRepository.save(funcionario) != null)
			saida = new Saida("Funcionário inserido com sucesso", 1, funcionario);
		else
			saida = new Saida("Erro ao inserir funcionário", 0,  null);
        funcionarioCacheService.save(funcionario);
        return saida;
    }
     
    public Saida findById(Integer id) {
        Optional<Funcionario> optionalFuncionario = getById(id);
        if (optionalFuncionario.isEmpty()) {
            return new Saida("Erro: Funcionário Não encontrado", 1,  null);
        }
        Funcionario funcionario = optionalFuncionario.get();
        return new Saida("Sucesso", 1, funcionario);
    }
    
    public Saida atualizarFuncionario(Integer id, Funcionario funcionarioAtualizado) {
        Optional<Funcionario> optionalFuncionario = funcionarioRepository.findById(id);
        if (optionalFuncionario.isPresent()) {
            Funcionario funcionario = optionalFuncionario.get();
            funcionario.setNome(funcionarioAtualizado.getNome());
            funcionario.setSalario(funcionarioAtualizado.getSalario());
            funcionarioRepository.save(funcionario);
            funcionarioCacheService.save(funcionario);
            return new Saida("Funcionário atualizado com sucesso", 1,  funcionario);
        } else { 
            return new Saida("Funcionário não encontrado", 0, null); 
        }
     }
        
    public Saida findByName(String nome) {
            Funcionario funcionario = getFromCache(nome);
            if (funcionario == null) {
                Optional<Funcionario> optionalFuncionario = funcionarioRepository.findByName(nome);
                if (optionalFuncionario.isPresent()) {
                    funcionario = optionalFuncionario.get();
                    saveToCache(funcionario);
                } else {
                    return new Saida("Erro: O funcionário não existe.", 0, null);
                }
            }

            return new Saida("Sucesso. Funcionário encontrado.", 1, funcionario);
        }

    public Funcionario getFromCache(String nome) {
            Set<String> keys = redisTemplate.keys("funcionario_*");
            if (!keys.isEmpty()) {
                List<Funcionario> cachedFuncionarios = redisTemplate.opsForValue().multiGet(keys);
                for (Funcionario funcionario : cachedFuncionarios) {
                    if (funcionario != null && funcionario.getNome().equals(nome)) {
                        return funcionario;
                    }
                }
            }
            return null;
    }

    @Transactional
    public Saida deletarFuncionario(Integer id) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
        if (funcionario.isPresent()) {
            funcionarioRepository.deleteById(id);
            funcionarioRepository.deleteById(id);
            return new Saida("Funcionário deletado com sucesso", 1, null);
        } else {
            return new Saida("Não foi possível encontrar o funcionário com o id " + id, -1, null);
        }
    }
}
