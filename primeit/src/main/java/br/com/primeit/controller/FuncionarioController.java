package br.com.primeit.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.primeit.model.Funcionario;
import br.com.primeit.model.Saida;
import br.com.primeit.services.FuncionarioService;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@GetMapping
	public List<Funcionario> getAllFuncionarios() {
		return funcionarioService.getAllFuncionarios();
	}
	
	@GetMapping("/{id}")
	public Optional<Funcionario> getById(@PathVariable Integer id) {
		return funcionarioService.getById(id);
	}

	@GetMapping("/{id}/{nome}")
	public Saida getByIdAndNome(@PathVariable Integer id, @PathVariable String nome) {
	    Saida saida = funcionarioService.findByIdAndName(id, nome);
	    if (saida != null && saida.getFuncionario() != null) {
	        return saida;
	    }
	    return new Saida("ERRO: Não foi possível encontrar o funcionário.", 0, null);
	}
	
	@PostMapping("/add")
	public Saida addFuncionario(@RequestBody Funcionario funcionario) {
		Saida saida = funcionarioService.addFuncionario(funcionario);
		if (saida != null && saida.getFuncionario() != null) {
			return saida;
		}
		return new Saida("ERRO: Não foi possível criar o funcionário", 0, null);
	}
		
	@PutMapping("/update/{id}")
	public Saida atualizarFuncionario(@PathVariable Integer id, @RequestBody Funcionario funcionarioAtualizado) {
		return funcionarioService.atualizarFuncionario(id, funcionarioAtualizado);
	}
	
	@DeleteMapping("/delete/{id}")
	public Saida deletarFuncionario(@PathVariable Integer id) {
	    Saida saida = funcionarioService.deletarFuncionario(id);
	    return saida;
	}
}
