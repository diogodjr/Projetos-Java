package br.com.primeit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConexaoDAO {

    public Connection conectar() throws SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/dados_funcionarios?user=root&password=Rodrigues29!";
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível conectar ao banco de dados: " + e.getMessage());
            throw e;
        }
    }

    public void desconectar(Connection conexao) {
        try {
            if (conexao != null) {
                conexao.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível fechar a conexão com o banco de dados: " + e.getMessage());
        }
    }
}
