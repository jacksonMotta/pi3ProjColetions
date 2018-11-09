/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pi3.locadora.view;

import br.com.model.Veiculo;
import br.com.model.dao.VeiculoDAO;
import br.com.servico.ServicoVeiculo;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Pichau
 */

@WebServlet(name = "Editar Veiculo", urlPatterns = {"/editarVeiculo"})
public class EditarVeiculo extends HttpServlet {
    
    public EditarVeiculo(){}
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
                throws ServletException, IOException
    {
        VeiculoDAO dao = new VeiculoDAO();
        Veiculo v = new Veiculo();
        
        v = dao.Pesquisa(Integer.parseInt(request.getParameter("id")));
        
        request.setAttribute("objetivo", " Editar");
        request.setAttribute("veiculo", v);
//        
        RequestDispatcher dispatcher
                = request.getRequestDispatcher("editarveiculo.jsp");
        
        dispatcher.forward(request, response);
        
    }
    
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException 
    {   
        Veiculo veiculo = new Veiculo();
        veiculo.setModelo(request.getParameter("modelo"));
        veiculo.setCategoria(request.getParameter("categoria"));
        veiculo.setAno(request.getParameter("ano"));
        veiculo.setPlaca(request.getParameter("marca"));
        veiculo.setNumeroDoc(request.getParameter("numerodoc"));
        veiculo.setCaracter(request.getParameter("caracteristica"));
        
        ServicoVeiculo dao = new ServicoVeiculo();
        
        dao.inserir(veiculo);

        RequestDispatcher dispatcher
                = request.getRequestDispatcher("/veiculos");

        dispatcher.forward(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
    
}
