package com.desarrollo.proyectocrud.controller;

import com.desarrollo.proyectocrud.model.Categoria;
import com.desarrollo.proyectocrud.model.Marca;
import com.desarrollo.proyectocrud.model.Producto;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class ProductosController {

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtStock;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private ComboBox<Categoria> cmbCategoria;

    @FXML
    private ComboBox<Marca> cmbMarca;

    @FXML
    private DatePicker dpFecha;


    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<Categoria> cmbFiltroCategoria;

    @FXML
    private DatePicker dpDesde;

    @FXML
    private DatePicker dpHasta;

    @FXML
    private TextField txtPrecioMin;

    @FXML
    private TextField txtPrecioMax;

    @FXML
    private CheckBox chkConStock;

    @FXML
    private ComboBox<String> cmbOrdenar;


    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, String> colCodigo;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, String> colCategoria;

    @FXML
    private TableColumn<Producto, String> colMarca;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    @FXML
    private TableColumn<Producto, LocalDate> colFecha;


    @FXML
    private void nuevoProducto() {

        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
        txtStock.clear();
        txtDescripcion.clear();

        cmbCategoria.setValue(null);
        cmbMarca.setValue(null);

        dpFecha.setValue(null);
    }


    @FXML
    private void guardarProducto() {

    }


    @FXML
    private void actualizarProducto() {

    }


    @FXML
    private void eliminarProducto() {

    }


    @FXML
    private void buscarProductos() {

    }


    @FXML
    private void limpiarFiltros() {

        txtBuscar.clear();

        cmbFiltroCategoria.setValue(null);

        dpDesde.setValue(null);
        dpHasta.setValue(null);

        txtPrecioMin.clear();
        txtPrecioMax.clear();

        chkConStock.setSelected(false);

        cmbOrdenar.setValue(null);
    }
}