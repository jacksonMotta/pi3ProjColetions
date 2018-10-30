/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.model.dao;

import br.com.conneticon.ConnectionFactory;
import br.com.model.Cliente;
import br.com.model.Locacao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Pichau
 */
public class LocacaoDAO {
    
    public LocacaoDAO(){
    
    }
    
    public String inserir(Locacao l){
        
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        String log = "";
        try 
        {

            stmt = con.prepareStatement("INSERT INTO LOCACAO (IDCLIENTE, "
                    + "IDFUNCIONARIO, IDVEICULO, CODIGO, PROTECAO, "
                    + "PRECO_TOTAL, DIARETIRA, DIAENTREGA) VALUES "
                    + "(?,?,?,?,?,?,?,?)");

            stmt.setInt(1, Integer.parseInt(l.getIdCliente()));
            stmt.setInt(2, Integer.parseInt(l.getIdFuncionario()));
            stmt.setInt(3, Integer.parseInt(l.getIdVeiculo()));
            stmt.setString(4, l.getCodigo());
            stmt.setString(5, l.getProtecao());
            stmt.setDouble(6, Double.parseDouble(l.getPrecoTotal()));
            stmt.setDate(7, Date.valueOf(l.getDiaRetira()));
            stmt.setDate(8, Date.valueOf(l.getDiaEntrega()));

            stmt.execute();

        } 
        catch (SQLException ex) 
        {
            log = ex.toString();
        } 
        finally 
        {
            ConnectionFactory.closeConnection(con, stmt);
        }
        return log;
    }
    
    public ArrayList<Locacao> ApresentarLocacoes(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<Locacao> locacoes = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT * FROM LOCACAO WHERE enable=1");
            rs = stmt.executeQuery();
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

            while (rs.next()) {

                Locacao l = new Locacao();

                l.setId(Integer.toString(rs.getInt("IDLOCACAO")));
                l.setIdCliente(Integer.toString(rs.getInt("IDCLIENTE")));
                l.setIdFuncionario(Integer.toString(rs.getInt("IDFUNCIONARIO")));
                l.setIdVeiculo(Integer.toString(rs.getInt("IDVEICULO")));
                l.setCodigo(rs.getString("CODIGO"));
                l.setProtecao(rs.getString("PROTECAO"));
                l.setPrecoTotal(Double.toString(rs.getDouble("PRECO_TOTAL")));
                l.setDiaRetira(fmt.format(rs.getDate("DIARETIRA")));
                l.setDiaEntrega(fmt.format(rs.getDate("DIAENTREGA")));

                locacoes.add(l);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "erro ao salvar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);

        }
        return locacoes;
    }
    
    public String atualizar(Locacao l){
        
        String retur = "";
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try {

            stmt = con.prepareStatement("UPDATE VEICULO SET IDCLIENTE=?, IDFUNCIONARIO=?,"
                + " IDVEICULO=?, CODIGO=?, PROTECAO=?, "
                + "PRECO_TOTAL=?, DIARETIRA=?, DIAENTREGA=? "
                + "WHERE (IDLOCACAO=?)");

            stmt.setInt(1, Integer.parseInt(l.getIdCliente()));
            stmt.setInt(2, Integer.parseInt(l.getIdFuncionario()));
            stmt.setInt(3, Integer.parseInt(l.getIdVeiculo()));
            stmt.setString(4, l.getCodigo());
            stmt.setString(5, l.getProtecao());
            stmt.setDouble(6, Double.parseDouble(l.getPrecoTotal()));
            stmt.setDate(7, Date.valueOf(l.getDiaRetira()));
            stmt.setDate(8, Date.valueOf(l.getDiaEntrega()));
            stmt.setInt(9, Integer.parseInt(l.getId()));

            stmt.execute();
            JOptionPane.showMessageDialog(null, "Alteração feita com Sucesso!");

        } catch (SQLException ex) {
            retur = ex.toString();
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
        
        return retur;
    }
    
    public void excluir(int id){
        
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {

            stmt = con.prepareStatement("UPDATE LOCACAO SET enable=2 WHERE IDLOCACAO = ?");
            stmt.setInt(1, id);
            stmt.execute();
            
            JOptionPane.showMessageDialog(null, id);
            JOptionPane.showMessageDialog(null, "Excluído  com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "erro ao atualizar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
    public Locacao Pesquisa(int id){
        
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Locacao l = new Locacao();

        try {
            

            stmt = con.prepareStatement("SELECT * FROM LOCACAO WHERE IDLOCACAO = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

            if (rs.first()) {
                l.setId(Integer.toString(rs.getInt("IDLOCACAO")));
                l.setIdCliente(Integer.toString(rs.getInt("IDCLIENTE")));
                l.setIdFuncionario(Integer.toString(rs.getInt("IDFUNCIONARIO")));
                l.setIdVeiculo(Integer.toString(rs.getInt("IDVEICULO")));
                l.setCodigo(rs.getString("CODIGO"));
                l.setProtecao(rs.getString("PROTECAO"));
                l.setPrecoTotal(Double.toString(rs.getDouble("PRECO_TOTAL")));
                l.setDiaRetira(fmt.format(rs.getDate("DIARETIRA")));
                l.setDiaEntrega(fmt.format(rs.getDate("DIAENTREGA")));
                
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "erro ao salvar" + ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);

        }
        return l;
    }
    
    public boolean ChecarLocacao(String codigo){
        
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;

        try {
            stmt = con.prepareStatement("SELECT * FROM LOCACAO WHERE CODIGO = ?");
            stmt.setString(1, codigo);
            rs = stmt.executeQuery();

            if (rs.next()) 
            {    
                result = true;
            }

        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, "erro ao verificar locacao" + ex);
        } 
        finally 
        {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return result;
    }
    
}
