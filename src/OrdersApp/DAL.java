package OrdersApp;

import java.sql.*;

import static OrdersApp.SQL.Connect;
import static OrdersApp.SQL.executeQuery;

public class DAL {

    private static Connection connection = null;

    public static Connection getConnection() {
        return connection;
    }

    public static void connect() {
        connection = Connect("...", "2");
    }

    public static ResultSet getUsername(String username, int password) {
        String sqlcmd = "select * from UTILIZADOR where Nome = '" + username + "' and PalavraPasse = " + password + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getMenus(int estabCod, boolean sorted) {
        String sqlcmd = "select m.Nome, m.NumPessoas, m.Preco, e.Nome, m.Id " +
                "from MENU m, ESTABELECIMENTO e " +
                "where m.EstabCod = e.Cod and e.Cod like '" + estabCod + "' " +
                (sorted ? "order by m.Nome" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getMenus(float minPrice, float maxPrice) {
        String sqlcmd = "select m.Nome, m.NumPessoas, m.Preco, e.Nome, m.Id " +
                "from MENU m, ESTABELECIMENTO e " +
                "where m.EstabCod = e.Cod and m.Preco <= (" + maxPrice + " + 0.00001) and m.Preco >= (" + minPrice + " - 0.00001) " +
                "order by m.Preco;";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getRecommendedMenus(int limit) {
        String sqlcmd = "select m.Nome, m.NumPessoas, m.Preco, e.Nome, m.Id " +
                "from MENU m, CLIENTE c, RECOMENDA r, ESTABELECIMENTO e " +
                "where c.Cc = " + OrdersApp.getCc() + " and c.Cc = r.ClienteCc and m.Id = r.MenuId and m.EstabCod = e.Cod " +
                "group by r.MenuId " +
                "order by count(r.MenuId) " +
                "desc " +
                "limit " + limit + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getMostOrderedMenus(int year, int limit) {
        String sqlcmd = "select m.Nome as Menu, count(e.MenuId) as Encomendas " +
                "from MENU m, ENCOMENDA e " +
                "where m.Id = e.MenuId and year(e.dataEnc) = " + year + " " +
                "group by m.Id " +
                "order by Encomendas " +
                "desc " +
                "limit " + limit + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getEstablishments(boolean sorted) {
        String sqlcmd = "select e.Nome, e.Cod " +
                "from ESTABELECIMENTO e " +
                (sorted ? "order by e.Nome" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getEstablishmentsWithMostAppraisal() {
        String sqlcmd = "select es.Nome as Estabelecimento, " +
                "case " +
                    "when avg(a.Pontos) is null then 0 else avg(a.Pontos) " +
                "end " +
                "as Pontuação_Média, es.Cod " +
                "from ESTABELECIMENTO es left join AVALIA a " +
                "on es.Cod = a.EstabCod " +
                "group by es.Cod " +
                "order by Pontuação_Média " +
                "desc;";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getEstablishmentsWithMostOrders() {
        String sqlcmd = "select es.Nome as Estabelecimento, count(m.EstabCod) as Encomendas " +
                "from ESTABELECIMENTO es left join (ENCOMENDA en, MENU m) " +
                "on en.MenuId = m.Id and m.EstabCod = es.Cod " +
                "group by es.Cod " +
                "order by Encomendas " +
                "desc;";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getClients(boolean sorted) {
        String sqlcmd = "select c.Nome, c.Cc " +
                "from CLIENTE c " +
                (sorted ? "order by c.Nome" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getTransporters(boolean sorted) {
        String sqlcmd = "select t.Nome, t.Cod " +
                "from TRANSPORTADOR t " +
                (sorted ? "order by t.Nome" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getSuppliers(boolean sorted) {
        String sqlcmd = "select f.Nome, f.Cod " +
                "from FORNECEDOR f " +
                (sorted ? "order by f.Nome" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getOrdersNotSent(boolean sorted) {
        String sqlcmd = "select e.DataEnc, e.Destino, m.Nome, c.Nome, e.Id " +
                "from MENU m, CLIENTE c, ENCOMENDA e " +
                "left join REMESSA r " +
                "on e.Id = r.EncId " +
                "where m.Id = e.MenuId and c.Cc = e.ClienteCc and r.EncId is null " +
                (sorted ? "order by e.DataEnc" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getEstablishmentsProfit(boolean sorted) {
        String sqlcmd = "select es.Nome, " +
                "case " +
                    "when sum(s1.Lucro_Por_Encomenda) is null then 0 else sum(s1.Lucro_Por_Encomenda) " +
                "end " +
                "as Lucro " +
                "from " +
                "ESTABELECIMENTO es " +
                "left join (" +
                    "select m.EstabCod as Code, (m.Preco-m.Preco*0.05)-(c.Quant*sum(f.Preco*cc.Quant)) as Lucro_Por_Encomenda " +
                    "from CONTEM c, ALIMENTO a, CONFECIONADO_COM cc, REMESSA r, ENCOMENDA en, MENU m, FORNECE f " +
                    "where " +
                        "r.EncId = en.Id and en.MenuId = m.Id and " +
                        "m.Id = c.MenuId and c.AlimId = a.Id and a.Id = cc.AlimId and " +
                        "cc.ProdCuu = f.ProdCuu and f.EstabCod = m.EstabCod " +
                    "group by en.Id " +
                ") as s1 " +
                "on es.Cod = s1.Code " +
                "group by es.Cod " +
                (sorted ? "order by Lucro desc" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getTransportersProfit(boolean sorted) {
        String sqlcmd = "select t.Nome as Transportador, " +
                "case " +
                    "when sum(m.Preco*0.05) is null then 0 else sum(m.Preco*0.05) " +
                "end " +
                "as Total_Recebido " +
                "from TRANSPORTADOR t left join (REMESSA r, ENCOMENDA en, MENU m) " +
                "on r.TransCod = t.Cod and r.EncId = en.Id and en.MenuId = m.Id " +
                "group by r.TransCod " +
                (sorted ? "order by t.Nome" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getConfections(boolean sorted) {
        String sqlcmd = "select a.Nome, a.Id " +
                "from Alimento a " +
                (sorted ? "order by a.Nome" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getConfectionables(int estabCod, boolean sorted) {

        String sqlcmd = "select s1.Alimento, s1.AlimentoId " +
            "from (" +
                    "select a.Nome as Alimento, a.Id as AlimentoId, count(p.Cuu) as ProdQuant " +
                    "from CONFECIONADO_COM cc, ALIMENTO a, PRODUTO p, FORNECE f, ESTABELECIMENTO e " +
                    "where a.Id = cc.AlimId and cc.ProdCuu = p.Cuu and p.Cuu = f.ProdCuu and " +
                    "f.EstabCod = e.Cod and e.Cod = " + estabCod + " and (e.ServeComida or !a.IsComida) " +
            "group by AlimentoId" +
            ") as s1, (" +
                "select a.Id as AlimentoId, count(p.Cuu) as ProdQuant " +
                "from CONFECIONADO_COM cc, ALIMENTO a, PRODUTO p " +
                "where a.Id = cc.AlimId and cc.ProdCuu = p.Cuu " +
                "group by a.Id " +
            ") as s2 " +
            "where s1.AlimentoId = s2.AlimentoId and s1.ProdQuant = s2.ProdQuant " +
            (sorted ? "order by s1.Alimento" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getProducts(boolean sorted) {
        String sqlcmd = "select p.Tipo, p.Cuu " +
                "from PRODUTO p " +
                (sorted ? "order by p.Tipo" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getProducts(int estabCod, boolean sorted) {
        String sqlcmd = "select p.Tipo, p.Cuu " +
                "from PRODUTO p, FORNECE f " +
                "where p.Cuu = f.ProdCuu and f.EstabCod = " + estabCod +
                (sorted ? "order by p.Tipo" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getBoughtProducts(int estabCod, boolean sorted) {
        String sqlcmd = "select p.Tipo as Produto, p.Cuu as Cuu " +
                "from PRODUTO p, FORNECE f " +
                "where p.Cuu = f.ProdCuu and f.EstabCod = " + estabCod + " " +
                (sorted ? "order by Produto" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getNotBoughtProducts(int estabCod, boolean sorted) {
        String sqlcmd = "select s1.Produto, s1.Cuu " +
                "from (" +
                "select p.Tipo as Produto, p.Cuu as Cuu " +
                "from PRODUTO p" +
                ") as s1 left join (" +
                "select p.Tipo as Produto, p.Cuu as Cuu " +
                "from PRODUTO p, FORNECE f " +
                "where p.Cuu = f.ProdCuu and f.EstabCod = " + estabCod + " " +
                ") as s2 " +
                "on s1.Cuu = s2.Cuu " +
                "where s2.Cuu is null " +
                (sorted ? "order by s1.Produto" : "") + ";";
        return executeQuery(connection, sqlcmd);
    }

    public static ResultSet getMostActiveSuppliers() {
        String sqlcmd = "select fr.Nome as Fornecedor_Mais_Ativo, count(f.FornCod) as Contrib " +
                "from FORNECEDOR fr, FORNECE f " +
                "where fr.Cod = f.FornCod " +
                "group by f.FornCod " +
                "order by Contrib " +
                "desc;";
        return executeQuery(connection, sqlcmd);
    }

    public static int getMenuCount() throws SQLException {
        String sqlcmd = "select count(m.Id) from MENU m;";
        ResultSet rs = executeQuery(connection, sqlcmd);
        rs.first();
        return rs.getInt(1);
    }

    public static int getConfectionCount() throws SQLException {
        String sqlcmd = "select count(a.Id) from ALIMENTO a;";
        ResultSet rs = executeQuery(connection, sqlcmd);
        rs.first();
        return rs.getInt(1);
    }

    public static void createUser(String username, int password, String name, int cc) throws SQLException {
        String sqlcmd1 = "insert into CLIENTE (Nome, Cc) " +
                "values ('" + name + "', " + cc + ");";
        String sqlcmd2 = "insert into UTILIZADOR (Nome, PalavraPasse, ClienteCc) " +
                "values ('" + username + "', " + password + ", " + cc + ");";
        try {
            connection.setAutoCommit(false);
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sqlcmd1);
            stmt.executeUpdate(sqlcmd2);
            connection.commit();
        } catch (SQLException throwables) {
            if (connection != null) {
                System.out.println(throwables.getMessage());
                System.err.print("Transaction is rolled back");
                OptionHandler.sendError("A criação do Utilizador não pôde ser efetuada.");
                connection.rollback();
            }
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    public static void orderMenu(int menuId, String address) throws SQLException {
        String sqlcmd = "insert into ENCOMENDA (DataEnc, Destino, MenuId, ClienteCc) " +
                "values (NOW(), ?, ?, ?);";
        PreparedStatement pstmt = connection.prepareStatement(sqlcmd);
        pstmt.setString(1, address);
        pstmt.setInt(2, menuId);
        pstmt.setString(3, OrdersApp.getCc());
        pstmt.executeUpdate();
    }

    public static void submitMenu(int enId, int transCod) throws SQLException {
        String sqlcmd2 = "insert into REMESSA " +
                "values (?, NOW(), ?);";
        PreparedStatement pstmt = connection.prepareStatement(sqlcmd2);
        pstmt.setInt(1, enId);
        pstmt.setInt(2, transCod);
        pstmt.executeUpdate();
    }

    public static void addSupply(int suppCod, int estabCod, int prodCuu, float price) throws SQLException {
        String sqlcmd = "insert into FORNECE " +
                "values (?, ?, ?, ?);";
        PreparedStatement pstmt = connection.prepareStatement(sqlcmd);
        pstmt.setInt(1, suppCod);
        pstmt.setInt(2, prodCuu);
        pstmt.setInt(3, estabCod);
        pstmt.setFloat(4, price);
        pstmt.executeUpdate();
    }

    public static void addSupplies(int suppCod, int estabCod, int[] prodCuus, Float[] prices) throws SQLException {
        try {
            connection.setAutoCommit(false);
            for (int i = 0; i < prodCuus.length; i++) {
                addSupply(suppCod, estabCod, prodCuus[i], prices[i]);
            }
            connection.commit();
        } catch (SQLException throwables) {
            if (connection != null) {
                System.out.println(throwables.getMessage());
                System.err.print("Transaction is rolled back");
                OptionHandler.sendError("A compra de Produtos não pôde ser efetuada.");
                connection.rollback();
            }
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    public static void addProduct(String prodName, int cuu) throws SQLException {
        String sqlcmd = "insert into PRODUTO " +
                "values (?, ?);";
        PreparedStatement pstmt = connection.prepareStatement(sqlcmd);
        pstmt.setString(1, prodName);
        pstmt.setInt(2, cuu);
        pstmt.executeUpdate();
    }

    public static void addConfection(String alimName, String type, int[] prodCuus, int[] quant) throws SQLException {
        // Insert confection:
        String sqlcmd1 = "insert into ALIMENTO (Nome, IsComida) " +
                "values (?, ?);";

        // Insert Confecionado com:
        String sqlcmd2 = "insert into CONFECIONADO_COM " +
                "values (?, ?, ?);";
        try {
            connection.setAutoCommit(false);

            int alimId = getConfectionCount() + 1;

            PreparedStatement pstmt1 = connection.prepareStatement(sqlcmd1);
            pstmt1.setString(1, alimName);
            pstmt1.setBoolean(2, type.equalsIgnoreCase("Comida"));
            pstmt1.executeUpdate();

            for (int i = 0; i < prodCuus.length; i++) {
                PreparedStatement pstmt2 = connection.prepareStatement(sqlcmd2);
                pstmt2.setInt(1, alimId);
                pstmt2.setInt(2, prodCuus[i]);
                pstmt2.setInt(3, quant[i]);
                pstmt2.executeUpdate();
            }
            connection.commit();
        } catch (SQLException throwables) {
            if (connection != null) {
                System.out.println(throwables.getMessage());
                System.err.print("Transaction is rolled back");
                OptionHandler.sendError("A criação do Alimento não pôde ser efetuada.");
                connection.rollback();
            }
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    public static void addMenu(int estabCod, String menuName, int numPessoas, float price, int[] alimIds, int[] quant) throws SQLException {
        // Insert menu:
        String sqlcmd1 = "insert into MENU (Nome, EstabCod, NumPessoas, Preco) " +
                "values (?, ?, ?, ?);";

        // Insert Confecionado com:
        String sqlcmd2 = "insert into CONTEM " +
                "values (?, ?, ?);";
        try {
            connection.setAutoCommit(false);

            int menuId = getMenuCount() + 1;

            PreparedStatement pstmt1 = connection.prepareStatement(sqlcmd1);
            pstmt1.setString(1, menuName);
            pstmt1.setInt(2, estabCod);
            pstmt1.setInt(3, numPessoas);
            pstmt1.setFloat(4, price);
            pstmt1.executeUpdate();

            for (int i = 0; i < alimIds.length; i++) {
                PreparedStatement pstmt2 = connection.prepareStatement(sqlcmd2);
                pstmt2.setInt(1, menuId);
                pstmt2.setInt(2, alimIds[i]);
                pstmt2.setInt(3, quant[i]);
                pstmt2.executeUpdate();
            }
            connection.commit();
        } catch (SQLException throwables) {
            if (connection != null) {
                System.out.println(throwables.getMessage());
                System.err.print("Transaction is rolled back");
                OptionHandler.sendError("A criação do Menu não pôde ser efetuada.");
                connection.rollback();
            }
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    public static void addEstablishment(String estabName, String type) throws SQLException {
        String sqlcmd = "insert into ESTABELECIMENTO (Nome, ServeComida) " +
                "values (?, ?);";
        PreparedStatement pstmt = connection.prepareStatement(sqlcmd);
        pstmt.setString(1, estabName);
        pstmt.setBoolean(2, type.equals("Comida e Bebida"));
        pstmt.executeUpdate();
    }

    public static void addTransporter(String transName, String type) throws SQLException {
        String sqlcmd = "insert into TRANSPORTADOR (Nome, IsCarMot) " +
                "values (?, ?);";
        PreparedStatement pstmt = connection.prepareStatement(sqlcmd);
        pstmt.setString(1, transName);
        pstmt.setString(2, type);
        pstmt.executeUpdate();
    }

    public static void addSupplier(String suppName) throws SQLException {
        String sqlcmd = "insert into FORNECEDOR (Nome) " +
                "values (?);";
        PreparedStatement pstmt = connection.prepareStatement(sqlcmd);
        pstmt.setString(1, suppName);
        pstmt.executeUpdate();
    }

    public static void addRecommendation(int friendCc, int menuId) throws SQLException {
        String sqlcmd = "insert into RECOMENDA " +
                "values (?, ?, ?);";
        PreparedStatement pstmt = connection.prepareStatement(sqlcmd);
        pstmt.setInt(1, Integer.parseInt(OrdersApp.getCc()));
        pstmt.setInt(2, friendCc);
        pstmt.setInt(3, menuId);
        pstmt.executeUpdate();
    }

    public static void addAppraisal(String justif, int points, int estabCod) throws SQLException {
        String sqlcmd = "insert into AVALIA (Justif, Pontos, ClienteCc, EstabCod) " +
                "values (?, ?, ?, ?);";
        PreparedStatement pstmt = connection.prepareStatement(sqlcmd);
        pstmt.setString(1, justif);
        pstmt.setInt(2, points);
        pstmt.setInt(3, Integer.parseInt(OrdersApp.getCc()));
        pstmt.setInt(4, estabCod);
        pstmt.executeUpdate();
    }
}
