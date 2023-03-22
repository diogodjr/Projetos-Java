package br.com.primeit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.primeit.model.Funcionario;

@ComponentScan
@Repository
@Qualifier("funcionarioRepository")
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer>, CrudRepository<Funcionario, Integer> {
    Optional<Funcionario> findById(Integer id);
    List<Funcionario> findAll();
    void deleteById(Integer id);
    Optional<Funcionario> findByName(String nome);
    Optional<Funcionario> findByIdAndName(Integer id, String nome);
    Optional<Funcionario> addFuncionario(Funcionario funcionario);
}
