package br.com.primeit.model;

public class Saida {
	private int mensagem;
	private Object codigoMsg;
	private Funcionario funcionario;
	
	public Saida(Object codigoMsg, int i, Funcionario funcionario) {
		this.codigoMsg = codigoMsg;
		this.mensagem = i;
		this.funcionario = funcionario;
	}
	
	public Saida(int codigoMsg2, String string, Object funcionario2) {
	}

	public Object getCodigoMsg() {
		return codigoMsg;
	}

	public void setCodigoMsg(Object codigoMsg) {
		this.codigoMsg = codigoMsg;
	}

	public int getMensagem() {
		return mensagem;
	}

	public void setMensagem(int mensagem) {
		this.mensagem = mensagem;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
}
