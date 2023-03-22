package br.com.primeit.services;

import br.com.primeit.model.Funcionario;
import br.com.primeit.model.Saida;
import br.com.primeit.repository.FuncionarioRepository;
import br.com.primeit.utils.ValidadorFuncionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FuncionarioServiceTest {

    @InjectMocks
    private FuncionarioService funcionarioService;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private ValidadorFuncionario funcionarioValidador;

    private Funcionario funcionario;

    @BeforeEach
    public void setUp() {
        funcionario = new Funcionario();
        funcionario.setId(1);
        funcionario.setNome("João");
        funcionario.setSalario(new BigDecimal("1000.00"));
    }

    @Test
    public void testeAddFuncionario() {
    	when(funcionarioValidador.validateFuncionario(funcionario)).thenReturn(new Saida(1, "Validação de Funcionário bem-sucedida", null));
        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionario);
        Saida saida = funcionarioService.addFuncionario(funcionario);

        assertEquals(1, saida.getMensagem());
        assertEquals("Funcionário inserido com sucesso", saida.getCodigoMsg());
        assertEquals(funcionario, saida.getFuncionario());
        verify(funcionarioRepository, times(1)).save(funcionario);
    }

    @Test
    public void testeFindById() {
        when(funcionarioRepository.findById(any(Integer.class))).thenReturn(Optional.of(funcionario));

        Saida saida = funcionarioService.findById(funcionario.getId());

        assertEquals(1, saida.getMensagem());
        assertEquals("Sucesso", saida.getCodigoMsg());
        assertEquals(funcionario, saida.getFuncionario());
        verify(funcionarioRepository, times(1)).findById(funcionario.getId());
    }
}