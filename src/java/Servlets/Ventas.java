/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import DAOs.DAOArticulo;
import DAOs.DAOVentas;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelos.Articulo;
import modelos.ArticuloCarrito;

/**
 *
 * @author camilo
 */
public class Ventas extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        DAOArticulo daoarticulo = new DAOArticulo();
        DAOVentas daoventas = new DAOVentas();
        int total = 0;
        HttpSession sesion = request.getSession(true);
        LinkedList<ArticuloCarrito> articulosCarrito = sesion.getAttribute("carrito") == null ? null : (LinkedList<ArticuloCarrito>) sesion.getAttribute("carrito");
        if (articulosCarrito != null) {
            for (ArticuloCarrito ac : articulosCarrito) {
                Articulo ar = daoarticulo.seleccionarArticulo(ac.getIdArticulo());
                total = total + (ac.getCantidad() * ar.getPrecio());
            }

            daoventas.guardarVenta(1, total);

            for (ArticuloCarrito ac : articulosCarrito) {
                Articulo ar = daoarticulo.seleccionarArticulo(ac.getIdArticulo());
                daoventas.guardarVentaCantidad(daoventas.selectMaxIdVentas(), ac.getIdArticulo(), ac.getCantidad());
            }
            articulosCarrito.removeAll(articulosCarrito);
        }
        response.sendRedirect("index.htm");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
