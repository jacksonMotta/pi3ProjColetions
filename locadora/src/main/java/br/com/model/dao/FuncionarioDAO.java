/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.model.dao;

import br.com.conneticon.ConnectionFactory;
import br.com.model.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author micro
 */
public class FuncionarioDAO {

    public FuncionarioDAO() {
    }

    private final SimpleDateFormat dataEntrada = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat dataBanco = new SimpleDateFormat("yyyy-MM-dd");

    public void inserir(Funcionario f) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {

            stmt = con.prepareStatement(
                    "INSERT INTO FUNCIONARIO(NOME,DTNASCIMENTO,CPF,EMAIL,CARGO,LOGIN,SENHA,FILIALFUNCIONARIO,ATIVO,ENABLE) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,1)");

            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getDataNascimento());
            stmt.setString(3, f.getCpf());
            stmt.setString(4, f.getEmail());
            stmt.setString(5, f.getCargo());
            stmt.setString(6, f.getLogin());
            stmt.setString(7, f.getSenha());
            stmt.setString(8, f.getNomeFilial());
            stmt.setInt(9, f.getAtivo());

            stmt.execute();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

    public ArrayList<Funcionario> apresentarFuncionarios() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<Funcionario> Funcionarios = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT * FROM FUNCIONARIO ");
            rs = stmt.executeQuery();

            while (rs.next()) {

                Funcionario f = new Funcionario();

                f.setId(rs.getInt("IDFUNCIONARIO"));
                f.setNome(rs.getString("NOME"));
                f.setCargo(rs.getString("CARGO"));
                f.setDataNascimento(f.convertDate(rs.getString("DTNASCIMENTO")));
                f.setCpf(rs.getString("CPF"));
                f.setEmail(rs.getString("EMAIL"));

                Funcionarios.add(f);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {

            ConnectionFactory.closeConnection(con, stmt, rs);

        }
        return Funcionarios;
    }

    public void atualizar(Funcionario f) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try {

            stmt = con.prepareStatement("UPDATE FUNCIONARIO SET NOME = ?, DTNASCIMENTO = ?, "
                    + "CPF = ?, EMAIL = ?, CARGO = ?, LOGIN = ?, "
                    + "SENHA = ?,ATIVO = ?, FILIALFUNCIONARIO  = ? "
                    + "WHERE IDFUNCIONARIO = ? ");

            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getDataNascimento());
            stmt.setString(3, f.getCpf());
            stmt.setString(4, f.getEmail());
            stmt.setString(5, f.getCargo());
            stmt.setString(6, f.getLogin());
            stmt.setString(7, f.getSenha());
            stmt.setInt(8, f.getAtivo());
            stmt.setString(9, f.getNomeFilial());
            stmt.setInt(10, f.getId());

            stmt.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

    public void editar(Funcionario f) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            stmt = con.prepareStatement("SELECT * FROM FUNCIONARIO WHERE IDFUNCIONARIO = ?");
            stmt.setInt(1, f.getId());
            rs = stmt.executeQuery();

            if (rs.next()) {
                stmt.setString(1, f.getNome());
                stmt.setString(2, f.getDataNascimento());
                stmt.setString(4, String.valueOf(f.getCpf()));
                stmt.setString(5, f.getEmail());
                stmt.setString(6, f.getCargo());
                stmt.setString(7, f.getSenha());

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);

        }

    }

    public String excluir(int id) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            //stmt = con.prepareStatement("UPDATE FUNCIONARIO SET ENABLE = 0 WHERE IDFUNCIONARIO = ?");
            stmt = con.prepareStatement("DELETE FROM FUNCIONARIO WHERE IDFUNCIONARIO = ?");
            stmt.setInt(1, id);
            stmt.execute();

        } catch (SQLException ex) {
            return "Erro";
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
        return null;

    }

    public Funcionario Pesquisa(int id) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Funcionario f = new Funcionario();

        try {

            stmt = con.prepareStatement("SELECT * FROM FUNCIONARIO WHERE IDFUNCIONARIO = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {

                f.setId(rs.getInt("IDFUNCIONARIO"));
                f.setNome(rs.getString("NOME"));
                f.setDataNascimento(rs.getString("DTNASCIMENTO"));
                f.setCpf(rs.getString("CPF"));
                f.setEmail(rs.getString("EMAIL"));
                f.setCargo(rs.getString("CARGO"));
                f.setLogin(rs.getString("LOGIN"));
                f.setSenha(rs.getString("SENHA"));
                f.setNomeFilial(rs.getString("FILIALFUNCIONARIO"));
                f.setAtivo(rs.getByte("ATIVO"));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);

        }
        return f;
    }

    public ArrayList<Funcionario> buscar(String nome) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<Funcionario> funcionarios = new ArrayList<Funcionario>();

        try {

            stmt = con.prepareStatement("SELECT * FROM FUNCIONARIO WHERE ENABLE = 1 AND NOME LIKE ? ");
            stmt.setString(1, "%" + nome + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {

                Funcionario f = new Funcionario();

                String dataConvertida = dataEntrada.format(dataBanco.parse(rs.getString("DTNASCIMENTO")));

                f.setId(rs.getInt("IDFUNCIONARIO"));
                f.setNome(rs.getString("NOME"));
                f.setDataNascimento(dataConvertida);
                f.setCpf(rs.getString("CPF"));
                f.setEmail(rs.getString("EMAIL"));
                f.setCargo(rs.getString("CARGO"));
                f.setLogin(rs.getString("LOGIN"));
                f.setSenha(rs.getString("SENHA"));
                f.setNomeFilial(rs.getString("FILIALFUNCIONARIO"));
                f.setAtivo(rs.getByte("ATIVO"));

                funcionarios.add(f);

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);

        }
        return funcionarios;
    }

    public boolean checarFuncionario(String nome) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;

        try {
            stmt = con.prepareStatement("SELECT * FROM FUNCIONARIO WHERE NOME LIKE ? ");
            stmt.setString(1, "%" + nome + "%");
            rs = stmt.executeQuery();

            if (rs.next()) {
                result = true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return result;
    }

}
