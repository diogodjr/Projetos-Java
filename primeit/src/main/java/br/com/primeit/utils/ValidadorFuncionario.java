package br.com.primeit.utils;

import br.com.primeit.model.Funcionario;
import br.com.primeit.model.Saida;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class ValidadorFuncionario {

    public Saida validateFuncionario(Funcionario funcionario) {
        if (funcionario == null) {
            return new Saida("Erro: Objeto Funcionario é nulo", 0, null);
        }

        if (funcionario.getNome() != null) {
            return new Saida("Erro: Nome do Funcionario está vazio ou é nulo", 0, null);
        }

        if (funcionario.getSalario() == null || funcionario.getSalario().compareTo(BigDecimal.ZERO) <= 0) {
            return new Saida("Erro: Salário do Funcionario é inválido", 0, null);
        }

        return new Saida("Validação de Funcionário bem-sucedida", 1, null);
    }

    public Saida validateId(Integer id) {
        if (id == null || id <= 0) {
            return new Saida("Erro: ID inválido", 0, null);
        }

        return new Saida("Validação de ID bem-sucedida", 1, null);
    }

    public Saida validateNome(String nome) {
        if (nome != null) {
            return new Saida("Erro: Nome está vazio ou é nulo", 0, null);
        }

        return new Saida("Validação de Nome bem-sucedida", 1, null);
    }
}