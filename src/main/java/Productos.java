import java.sql.*;
import java.util.Scanner;

public class Productos {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("***SISTEMA DE COMPRAS***");
        System.out.println("Productos disponibles para compra: ");
        Select();

        System.out.println("Ingrese codigo de producto que desea comprar: ");
        String codigo = scanner.nextLine();

        System.out.println("Cuantas cantidades deseas comprar? ");
        int cantidad = Integer.parseInt(scanner.nextLine());

        if (codigo.equals("")){
            System.out.println("Debes ingresar un codigo valido");
        }else if (cantidad <= 0){
            System.out.println("Debes ingresar una cantidad mayor a 0");
        }else{
            int cantidad_disponible = Select_One(codigo);
            int compra_cantidad = cantidad_disponible - cantidad;
            if (compra_cantidad >= 0){
                Editar(codigo, compra_cantidad);
            }else {
                System.out.println("La compra excede la cantidad disponible");
            }

        }


    }

    public static void Select() throws ClassNotFoundException, SQLException {
        String driver2 = "com.mysql.cj.jdbc.Driver";
        String url2 = "jdbc:mysql://localhost:3306/products";
        String username2 = "root";
        String pass2 = "";

        Class.forName(driver2);
        Connection connection2 = DriverManager.getConnection(url2, username2, pass2);

        Statement statement2 = connection2.createStatement();

        ResultSet resultSet2 = statement2.executeQuery("SELECT * FROM productos_compra");

        while(resultSet2.next()){
            String codigo = resultSet2.getString("codigo");
            String nombre = resultSet2.getString("nombre");
            int cantidad = resultSet2.getInt("cantidad_disponible");

            System.out.println("este es el codigo "+codigo+  " nombre "+nombre+ " cantidad disponible: "+cantidad);
        }
    }


    public static int Select_One(String id) throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/products";
        String username = "root";
        String password = "";
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);

        String consultaSQL = "SELECT * FROM productos_compra WHERE codigo = ?";

        PreparedStatement statement = connection.prepareStatement(consultaSQL);
        statement.setString(1, id); // Establecer el valor del parámetro

        // Ejecutar la consulta
        ResultSet resultSet = statement.executeQuery();

        // Procesar el resultado si existe
        if (resultSet.next()) {
            String codigo = resultSet.getString("codigo");
            int cantidad_disponible = resultSet.getInt("cantidad_disponible");
            return cantidad_disponible;
        } else {
            System.out.println("No se encontró un registro con el codigo especificado.");
        }

        // Cerrar recursos
        resultSet.close();
        statement.close();
        connection.close();
        return 0;
    }

    public static void Editar(String codigo, int cantidad) throws ClassNotFoundException, SQLException {
        String driver2 = "com.mysql.cj.jdbc.Driver";
        String url2 = "jdbc:mysql://localhost:3306/products";
        String username2 = "root";
        String pass2 = "";

        Class.forName(driver2);
        Connection connection2 = DriverManager.getConnection(url2, username2, pass2);

        Statement statement2 = connection2.createStatement();
        String consulta = "UPDATE productos_compra SET cantidad_disponible = ? WHERE codigo = ?";
        PreparedStatement preparedStatement = connection2.prepareStatement(consulta);
        preparedStatement.setInt(1, cantidad);
        preparedStatement.setString(2, codigo);

        int filasActualizadas = preparedStatement.executeUpdate();
        if (filasActualizadas > 0) {
            System.out.println("Producto actualizado exitosamente");
        } else {
            System.out.println("No se encontró el registro para actualizar");
        }


        preparedStatement.close();
        connection2.close();
    }

}