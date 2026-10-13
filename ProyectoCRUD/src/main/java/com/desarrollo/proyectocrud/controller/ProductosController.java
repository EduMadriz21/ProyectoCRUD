package com.desarrollo.proyectocrud.controller;

import com.desarrollo.proyectocrud.database.ConexionBD;
import com.desarrollo.proyectocrud.model.Categoria;
import com.desarrollo.proyectocrud.model.Marca;
import com.desarrollo.proyectocrud.model.Producto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;


public class ProductosController {


    // =========================================================
    // FORMULARIO DE PRODUCTOS
    // =========================================================

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


    // =========================================================
    // BÚSQUEDA Y FILTROS
    // =========================================================

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


    // =========================================================
    // TABLA DE PRODUCTOS
    // =========================================================

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
    private TableColumn<Producto, String> colDescripcion;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    @FXML
    private TableColumn<Producto, LocalDate> colFecha;


    // =========================================================
    // LISTAS
    // =========================================================

    private final ObservableList<Categoria> listaCategorias =
            FXCollections.observableArrayList();

    private final ObservableList<Marca> listaMarcas =
            FXCollections.observableArrayList();

    private final ObservableList<Producto> listaProductos =
            FXCollections.observableArrayList();


    // =========================================================
    // INICIALIZAR
    // =========================================================

    @FXML
    public void initialize() {

        configurarTableView();

        cmbOrdenar.getItems().addAll(
                "Nombre A-Z",
                "Nombre Z-A",
                "Precio menor a mayor",
                "Precio mayor a menor",
                "Stock menor a mayor",
                "Stock mayor a menor",
                "Fecha más reciente",
                "Fecha más antigua"
        );

        cmbCategoria.setPromptText("Seleccionar categoría");
        cmbMarca.setPromptText("Seleccionar marca");
        cmbFiltroCategoria.setPromptText("Todas");
        cmbOrdenar.setPromptText("Seleccionar");

        dpFecha.setValue(LocalDate.now());

        cargarCategorias();
        cargarMarcas();
        cargarProductos();
    }


    // =========================================================
    // CARGAR CATEGORÍAS
    // =========================================================

    private void cargarCategorias() {

        listaCategorias.clear();

        String sql = """
                SELECT
                    id_categoria,
                    nombre,
                    descripcion,
                    estado
                FROM catalogo.categorias
                WHERE estado = 1
                ORDER BY nombre
                """;

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Categoria categoria = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("estado")
                );

                listaCategorias.add(categoria);
            }

            cmbCategoria.setItems(listaCategorias);

            cmbFiltroCategoria.setItems(
                    FXCollections.observableArrayList(listaCategorias)
            );

            System.out.println(
                    "Categorias cargadas: " + listaCategorias.size()
            );

        } catch (SQLException e) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudieron cargar las categorías.\n"
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // CARGAR MARCAS
    // =========================================================

    private void cargarMarcas() {

        listaMarcas.clear();

        String sql = """
                SELECT
                    id_marca,
                    nombre,
                    descripcion,
                    estado
                FROM catalogo.marcas
                WHERE estado = 1
                ORDER BY nombre
                """;

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Marca marca = new Marca(
                        rs.getInt("id_marca"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("estado")
                );

                listaMarcas.add(marca);
            }

            cmbMarca.setItems(listaMarcas);

            System.out.println(
                    "Marcas cargadas: " + listaMarcas.size()
            );

        } catch (SQLException e) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudieron cargar las marcas.\n"
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // CARGAR PRODUCTOS
    // =========================================================

    private void cargarProductos() {

        listaProductos.clear();

        String sql = """
                SELECT
                    p.id_producto,
                    p.codigo,
                    p.nombre,
                    p.descripcion,
                    p.precio,
                    p.stock,
                    p.fecha_creacion,
                    p.id_categoria,
                    c.nombre AS categoria,
                    p.id_marca,
                    m.nombre AS marca,
                    p.estado
                FROM catalogo.productos p
                INNER JOIN catalogo.categorias c
                    ON p.id_categoria = c.id_categoria
                INNER JOIN catalogo.marcas m
                    ON p.id_marca = m.id_marca
                WHERE p.estado = 1
                ORDER BY p.id_producto
                """;

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                listaProductos.add(
                        crearProductoDesdeResultSet(rs)
                );
            }

            tablaProductos.setItems(listaProductos);

            System.out.println(
                    "Productos cargados: " + listaProductos.size()
            );

        } catch (SQLException e) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudieron cargar los productos.\n"
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // CREAR PRODUCTO DESDE RESULTSET
    // =========================================================

    private Producto crearProductoDesdeResultSet(
            ResultSet rs
    ) throws SQLException {

        return new Producto(
                rs.getInt("id_producto"),
                rs.getString("codigo"),
                rs.getString("descripcion"),
                rs.getString("nombre"),
                rs.getDouble("precio"),
                rs.getInt("stock"),
                rs.getDate("fecha_creacion").toLocalDate(),
                rs.getInt("id_categoria"),
                rs.getString("categoria"),
                rs.getInt("id_marca"),
                rs.getString("marca"),
                rs.getInt("estado")
        );
    }


    // =========================================================
    // CONFIGURAR TABLEVIEW
    // =========================================================

    private void configurarTableView() {

        colCodigo.setCellValueFactory(
                new PropertyValueFactory<>("codigo")
        );

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colCategoria.setCellValueFactory(
                new PropertyValueFactory<>("categoria")
        );

        colMarca.setCellValueFactory(
                new PropertyValueFactory<>("marca")
        );

        colDescripcion.setCellValueFactory(
                new PropertyValueFactory<>("descripcion")
        );

        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precio")
        );

        colStock.setCellValueFactory(
                new PropertyValueFactory<>("stock")
        );

        colFecha.setCellValueFactory(
                new PropertyValueFactory<>("fechaCreacion")
        );
    }


    // =========================================================
    // NUEVO PRODUCTO
    // =========================================================

    @FXML
    private void nuevoProducto() {

        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
        txtStock.clear();
        txtDescripcion.clear();

        cmbCategoria.setValue(null);
        cmbMarca.setValue(null);

        dpFecha.setValue(LocalDate.now());

        tablaProductos
                .getSelectionModel()
                .clearSelection();

        txtCodigo.requestFocus();
    }


    // =========================================================
    // GUARDAR PRODUCTO
    // =========================================================

    @FXML
    private void guardarProducto() {

    }


    // =========================================================
    // ACTUALIZAR PRODUCTO
    // =========================================================

    @FXML
    private void actualizarProducto() {

    }


    // =========================================================
    // ELIMINAR PRODUCTO
    // =========================================================

    @FXML
    private void eliminarProducto() {

    }


    // =========================================================
    // BÚSQUEDA PARAMETRIZADA
    // =========================================================

    @FXML
    private void buscarProductos() {

        listaProductos.clear();

        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.id_producto,
                    p.codigo,
                    p.nombre,
                    p.descripcion,
                    p.precio,
                    p.stock,
                    p.fecha_creacion,
                    p.id_categoria,
                    c.nombre AS categoria,
                    p.id_marca,
                    m.nombre AS marca,
                    p.estado
                FROM catalogo.productos p
                INNER JOIN catalogo.categorias c
                    ON p.id_categoria = c.id_categoria
                INNER JOIN catalogo.marcas m
                    ON p.id_marca = m.id_marca
                WHERE p.estado = 1
                """);

        List<Object> parametros =
                new ArrayList<>();


        // =====================================================
        // FILTRO POR CÓDIGO O NOMBRE
        // =====================================================

        String texto =
                txtBuscar.getText().trim();

        if (!texto.isBlank()) {

            sql.append("""
                    AND (
                        p.codigo ILIKE ?
                        OR p.nombre ILIKE ?
                    )
                    """);

            parametros.add(
                    "%" + texto + "%"
            );

            parametros.add(
                    "%" + texto + "%"
            );
        }


        // =====================================================
        // FILTRO POR CATEGORÍA
        // =====================================================

        Categoria categoria =
                cmbFiltroCategoria.getValue();

        if (categoria != null) {

            sql.append("""
                    AND p.id_categoria = ?
                    """);

            parametros.add(
                    categoria.getIdCategoria()
            );
        }


        // =====================================================
        // FILTROS POR FECHA
        // =====================================================

        LocalDate fechaDesde =
                dpDesde.getValue();

        LocalDate fechaHasta =
                dpHasta.getValue();


        if (
                fechaDesde != null
                        &&
                        fechaHasta != null
                        &&
                        fechaDesde.isAfter(fechaHasta)
        ) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Rango de fechas incorrecto",
                    "La fecha inicial no puede ser posterior a la fecha final."
            );

            return;
        }


        if (fechaDesde != null) {

            sql.append("""
                    AND p.fecha_creacion >= ?
                    """);

            parametros.add(
                    fechaDesde
            );
        }


        if (fechaHasta != null) {

            sql.append("""
                    AND p.fecha_creacion <= ?
                    """);

            parametros.add(
                    fechaHasta
            );
        }


        // =====================================================
        // PRECIO MÍNIMO
        // =====================================================

        if (!txtPrecioMin.getText().trim().isBlank()) {

            try {

                BigDecimal precioMin =
                        new BigDecimal(
                                txtPrecioMin
                                        .getText()
                                        .trim()
                        );

                sql.append("""
                        AND p.precio >= ?
                        """);

                parametros.add(
                        precioMin
                );

            } catch (NumberFormatException e) {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Precio incorrecto",
                        "El precio mínimo debe ser un número válido."
                );

                return;
            }
        }


        // =====================================================
        // PRECIO MÁXIMO
        // =====================================================

        if (!txtPrecioMax.getText().trim().isBlank()) {

            try {

                BigDecimal precioMax =
                        new BigDecimal(
                                txtPrecioMax
                                        .getText()
                                        .trim()
                        );

                sql.append("""
                        AND p.precio <= ?
                        """);

                parametros.add(
                        precioMax
                );

            } catch (NumberFormatException e) {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Precio incorrecto",
                        "El precio máximo debe ser un número válido."
                );

                return;
            }
        }


        // =====================================================
        // SOLO PRODUCTOS CON EXISTENCIA
        // =====================================================

        if (chkConStock.isSelected()) {

            sql.append("""
                    AND p.stock > 0
                    """);
        }


        // =====================================================
        // ORDENAMIENTO
        // =====================================================

        String orden =
                cmbOrdenar.getValue();

        if (orden == null) {
            orden = "Nombre A-Z";
        }


        switch (orden) {

            case "Nombre Z-A" ->
                    sql.append("""
                            ORDER BY p.nombre DESC
                            """);

            case "Precio menor a mayor" ->
                    sql.append("""
                            ORDER BY p.precio ASC
                            """);

            case "Precio mayor a menor" ->
                    sql.append("""
                            ORDER BY p.precio DESC
                            """);

            case "Stock menor a mayor" ->
                    sql.append("""
                            ORDER BY p.stock ASC
                            """);

            case "Stock mayor a menor" ->
                    sql.append("""
                            ORDER BY p.stock DESC
                            """);

            case "Fecha más reciente" ->
                    sql.append("""
                            ORDER BY p.fecha_creacion DESC
                            """);

            case "Fecha más antigua" ->
                    sql.append("""
                            ORDER BY p.fecha_creacion ASC
                            """);

            default ->
                    sql.append("""
                            ORDER BY p.nombre ASC
                            """);
        }


        // =====================================================
        // EJECUTAR CONSULTA
        // =====================================================

        try (
                Connection conexion =
                        ConexionBD.conectar();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql.toString()
                        )
        ) {

            for (
                    int i = 0;
                    i < parametros.size();
                    i++
            ) {

                Object valor =
                        parametros.get(i);


                if (valor instanceof LocalDate fecha) {

                    ps.setDate(
                            i + 1,
                            Date.valueOf(fecha)
                    );

                } else if (valor instanceof BigDecimal numero) {

                    ps.setBigDecimal(
                            i + 1,
                            numero
                    );

                } else {

                    ps.setObject(
                            i + 1,
                            valor
                    );
                }
            }


            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    listaProductos.add(
                            crearProductoDesdeResultSet(rs)
                    );
                }
            }


            tablaProductos.setItems(
                    listaProductos
            );


            if (listaProductos.isEmpty()) {

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Sin resultados",
                        "No existen productos que coincidan con los filtros aplicados."
                );
            }


        } catch (SQLException e) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error de base de datos",
                    "No se pudo realizar la búsqueda.\n"
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // LIMPIAR FILTROS
    // =========================================================

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

        cargarProductos();
    }


    // =========================================================
    // MOSTRAR ALERTAS
    // =========================================================

    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensaje
    ) {

        Alert alerta =
                new Alert(tipo);

        alerta.setTitle(
                titulo
        );

        alerta.setHeaderText(
                null
        );

        alerta.setContentText(
                mensaje
        );

        alerta.showAndWait();
    }
}